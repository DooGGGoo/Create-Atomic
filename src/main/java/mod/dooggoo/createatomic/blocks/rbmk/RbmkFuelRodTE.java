package mod.dooggoo.createatomic.blocks.rbmk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.dooggoo.createatomic.api.Directions;
import mod.dooggoo.createatomic.items.RbmkFuelItem;
import mod.dooggoo.createatomic.network.ModNetworkPackets;
import mod.dooggoo.createatomic.network.packet.RbmkFuelRodS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;

public class RbmkFuelRodTE extends RbmkBaseTE {
    public float fluxFast;
    public float fluxSlow;
    public boolean hasFuel;
    public static float maxHeat = 1520f;

    public final ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public SimpleContainer container = new SimpleContainer(inventory.getSlots());

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public RbmkFuelRodTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

  
    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkFuelRodTE be)
    {
        if (!be.hasLevel() || be.pos == null){
            be.setLevel(Level);
            be.pos = Pos;
        }
        if (!be.level.isClientSide){
            be.transferHeat();
            be.passiveCooling();
            be.updateFlux();
            be.sendToClient();
            be.onMeltdown();
        }

        be.onOverheat();
    }

    @Override
    protected void sendToClient() {
        ModNetworkPackets.INSTANCE.send(PacketDistributor.TRACKING_CHUNK
                .with(() -> this.level.getChunkAt(this.worldPosition)), 
            new RbmkFuelRodS2CPacket(this.pos, this.heat, this.hasFuel));
    }

    public boolean hasFuelRod(int slot){
        if (inventory.getStackInSlot(slot).getItem() instanceof RbmkFuelItem){
            return true;
        }
        return false;
    }

    public void updateFlux() {
        if (hasFuelRod(0)) {
            RbmkFuelItem fuel = (RbmkFuelItem) inventory.getStackInSlot(0).getItem();
            
            float fluxIn = getFluxFromType(fuel.inType);
            float fluxOut = fuel.burnFuel(level, inventory.getStackInSlot(0), fluxIn);

            Type outType = fuel.outType;

            fuel.updateHeat(level, inventory.getStackInSlot(0));
            heat += fuel.releaseHeat(level, inventory.getStackInSlot(0), heat);
            sendToClient();

            fluxFast = 0f;
            fluxSlow = 0f;

            emmitFlux(outType, fluxOut);
            hasFuel = true;
        }
        else {
            fluxFast = 0f;
            fluxSlow = 0f;

            hasFuel = false;
        }
    }

    private boolean isValidFuel(ItemStack item) {
        if (item.getItem() instanceof RbmkFuelItem) return true;
        else return false;
    }

    public void setState(boolean value, BooleanProperty property) {
        BlockState state = level.getBlockState(this.pos);
        updateBlockState(state.setValue(property, value));
    }

    public boolean interact(Player player, InteractionHand hand, ItemStack itemInHand) {
        if (isValidFuel(itemInHand))
        {
            ItemStack stored = !inventory.getStackInSlot(0).isEmpty() ? inventory.getStackInSlot(0).copy() : ItemStack.EMPTY;
            inventory.setStackInSlot(0, itemInHand.copy());
            player.setItemInHand(hand, stored);
            this.setChanged();
            this.setState(!inventory.getStackInSlot(0).isEmpty() ? true : false, RbmkFuelRod.HASFUEL);
            return true;
        }
        else if (!inventory.getStackInSlot(0).isEmpty())
        {
            if (!level.isClientSide)
                player.spawnAtLocation(inventory.getStackInSlot(0).copy());
            inventory.setStackInSlot(0, ItemStack.EMPTY);
            this.setChanged();
            this.setState(false, RbmkFuelRod.HASFUEL);
            return true;
        }
        return false;
    }

    @SuppressWarnings("incomplete-switch")
    public void recieveFlux(Type type, float flux) {
        switch(type) {
        case FAST: fluxFast += flux; break;
        case SLOW: fluxSlow += flux; break;
        }   
    }

    protected void emmitFlux(Type type, float Flux) {
        int range = 4; // TODO change to configurable

        for (Directions dir : directions)
        {
            fluxStream = type;
            float flux = Flux;

            for (int i = 1; i <= range; i++)
            {
                flux = fluxInteractions(pos.getX() + dir.offsetX * i, pos.getY(), pos.getZ() + dir.offsetZ * i, flux);
                if (flux <= 0)
                    break;
            }
        }
    }

    protected float fluxInteractions(int x, int y, int z, float flux)
    {
        BlockEntity be = level.getBlockEntity(new BlockPos(x, y, z));
        
        if(be instanceof RbmkFuelRodTE) {
            RbmkFuelRodTE fuelRod = (RbmkFuelRodTE) be;
            if (hasFuelRod(0)) {
                fuelRod.recieveFlux(fluxStream, flux);
                return 0f;
            }
        }

        if(be instanceof RbmkModeratorTE) {
            fluxStream = Type.SLOW;
            return flux;
        }
        
        if(be instanceof RbmkReflectorTE) {
            this.recieveFlux(fluxStream, flux);
            return 0f;
        }

        if(be instanceof RbmkControlRodTE) {
            RbmkControlRodTE controlRod = (RbmkControlRodTE) be;
            if(controlRod.extention.getPercentage() == 0) {return 0f;}
            else if(controlRod.extention.getPercentage() == 1) {return flux * 0.25f;}
            else if(controlRod.extention.getPercentage() == 2) {return flux * 0.5f;}
            else if(controlRod.extention.getPercentage() == 3) {return flux * 0.75f;}
            else if(controlRod.extention.getPercentage() == 4) {return flux;}
        }
        
        if(be instanceof RbmkBaseTE) {
            return flux;
        }
        

        return 0;
    }

    public enum Type {
		FAST,
		SLOW,
        ANY;
    }

    protected static Type fluxStream;

    private float getFluxFromType(Type type)
    {
        switch(type) {
            case SLOW: return fluxFast * 0.5f + fluxSlow;
            case FAST: return fluxFast + fluxSlow * 0.3f;
            case ANY: return fluxFast + fluxSlow;
            }
        return 0;
    }

//#region
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", inventory.serializeNBT());
        tag.putFloat("fluxFast", this.fluxFast);
        tag.putFloat("fluxSlow", this.fluxSlow);
        tag.putFloat("heat", this.heat);
        tag.putBoolean("hasFuel", this.hasFuel);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        inventory.deserializeNBT(tag);
        fluxFast = tag.getFloat("fluxFast");
        fluxSlow = tag.getFloat("fluxSlow");
        heat = tag.getFloat("heat");
        hasFuel = tag.getBoolean("hasFuel");
        super.load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, be -> {
            CompoundTag tag = new CompoundTag();
            tag.put("inventory", inventory.serializeNBT());
            tag.putFloat("fluxFast", this.fluxFast);
            tag.putFloat("fluxSlow", this.fluxSlow);
            tag.putFloat("heat", this.heat);
            tag.putBoolean("hasFuel", this.hasFuel);
            this.saveAdditional(tag);
            return tag;
        });
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag Tag = pkt.getTag() != null ? pkt.getTag() : new CompoundTag();
        this.load(Tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putFloat("fluxFast", this.fluxFast);
        nbt.putFloat("fluxSlow", this.fluxSlow);
        nbt.putFloat("heat", this.heat);
        nbt.putBoolean("hasFuel", this.hasFuel);
        saveAdditional(nbt);
        return nbt;
    }

//#endregion

    public void drops() {
        for(int i = 0; i < inventory.getSlots(); i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, container);
        hasFuel = false;
    }

}
