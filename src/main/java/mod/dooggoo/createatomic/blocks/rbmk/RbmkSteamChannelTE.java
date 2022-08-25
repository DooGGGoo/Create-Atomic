package mod.dooggoo.createatomic.blocks.rbmk;

import java.util.List;

import com.simibubi.create.content.contraptions.fluids.VirtualFluid;

import mod.dooggoo.createatomic.BuildConfig;
import mod.dooggoo.createatomic.register.ModFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class RbmkSteamChannelTE extends RbmkBaseTE {

    FluidTank water = new FluidTank(4000, f -> f.getFluid() == Fluids.WATER);
    FluidTank steam = new FluidTank(8000);
    VirtualFluid steamType = ModFluids.STEAM.get();

    private IFluidHandler fluidHandler = new IFluidHandler() {
		@Override
		public int getTanks() {
			return 2;
		}

//#region deez

		@Override
		public FluidStack getFluidInTank(int tank) {
			switch (tank) {
			case 0:
				return water.getFluid();
			case 1:
				return steam.getFluid();
			default:
				return null;
			}
		}

		@Override
		public int getTankCapacity(int tank) {
			return 8000;
		}

		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			if (tank == 0 && stack.getFluid() == Fluids.WATER)
				return true;
			return false;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return water.fill(resource, action);
		}

		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return steam.drain(resource, action);
		}

		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return steam.drain(maxDrain, action);
		}
    };
    
//#endregion

    private LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> fluidHandler);
    
    public RbmkSteamChannelTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkSteamChannelTE be) {
        if (!be.hasLevel() || be.pos == null) {
            be.setLevel(Level);
            be.pos = Pos;
        }

        if (!be.level.isClientSide) {
            be.transferHeat();
            be.passiveCooling();
            be.boilSteam();
            be.sendToClient();
            be.onMeltdown();
        }

        be.onOverheat();
    }

    public void boilSteam() {
        float heatCap = this.getHeatReqForSteam(steamType);
        float heatProvided = this.heat - heatCap;
        
        if(heatProvided > 0) {
            int waterUsed = (int)Math.floor(heatProvided / .1f);
            waterUsed = Math.min(waterUsed, water.getFluidAmount());
            water.drain(waterUsed, FluidAction.EXECUTE);
            int steamProduced = (int)Math.floor((waterUsed * 100) / steamMultiplier(steamType));
            steam.fill(new FluidStack(steamType, steamProduced), FluidAction.EXECUTE); 
            
            this.heat -= waterUsed * .1f;
        }
    }

    //Currently this function is not necessary but if in future i will adding other steam types this might be helpful
    public float getHeatReqForSteam(VirtualFluid steamType) {
        if (steamType == ModFluids.STEAM.get()) {
            return 130f;
        }
        return 0f;
    }

    public float steamMultiplier(VirtualFluid steamType) {
        if (steamType == ModFluids.STEAM.get()) {
            return 1f;
        }
        return 0f;
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (heat > overheatThreshold) {
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmkbase.overheat").withStyle(ChatFormatting.GRAY)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.RED)));
        }
        else {
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmkbase.heat").withStyle(ChatFormatting.GRAY)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.GOLD)));
        }
        //tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmksteamchannel.water").withStyle(ChatFormatting.GRAY)));
        this.containedFluidTooltip(tooltip, isPlayerSneaking, LazyOptional.of(() -> water));
        //tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmksteamchannel.steam").withStyle(ChatFormatting.GRAY)));
        this.containedFluidTooltip(tooltip, isPlayerSneaking, LazyOptional.of(() -> steam));
        return true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.holder.isPresent()) {
            LazyOptional<IFluidHandler> oldCap = this.holder;
            this.holder = LazyOptional.of(() -> {
                return this.fluidHandler;
            });
            oldCap.invalidate();
        }
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? holder.cast() : super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("steam", steam.writeToNBT(new CompoundTag()));
        tag.put("water", water.writeToNBT(new CompoundTag()));
        tag.putFloat("heat", this.heat);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        water = water.readFromNBT(tag.getCompound("water"));
        steam = steam.readFromNBT(tag.getCompound("steam"));
        heat = tag.getFloat("heat");
        super.load(tag);
    }
}
