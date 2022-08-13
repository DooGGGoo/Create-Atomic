package mod.dooggoo.createatomic.blocks.rbmk;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;

import mod.dooggoo.createatomic.BuildConfig;
import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.api.Directions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkBaseTE extends SmartTileEntity implements IHaveGoggleInformation {

    public static float passiveHeatLoss = 16f;
    public static float overheatThreshold = 1025f;
    public float heat = 16f;
    public static float maxHeat = 1300f;

    public RbmkBaseTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState)  {
       super(type, blockPos, blockState);
    }

    @Override
    public void tick() {
        if (!level.isClientSide)
        { 
            transferHeat();
            passiveCooling();
            if (heat > overheatThreshold) {
                onOverheat();
            }
            if (heat > maxHeat) {
                onMeltdown();
            }
            CreateAtomic.LOGGER.debug("RbmkBaseTe at: " + pos + " : heat: " + heat);
        }
        super.tick();
    }



    // Thanks to Drillgon200 for his rbmk code so i can yoink it

    protected RbmkBaseTE[] heatCache = new RbmkBaseTE[4];

    private BlockPos pos = this.getBlockPos();

    public static final Directions[] directions = new Directions[]
    {
        Directions.NORTH, 
        Directions.SOUTH, 
        Directions.EAST, 
        Directions.WEST, 
    };

    /*
     *  Gotta move dat heat up!
     */

    private void transferHeat()
    {
        List<RbmkBaseTE> connected = new ArrayList<>();
        connected.add(this);
        float heatTotal = this.heat;

        int i = 0;
        for(Directions dir : directions)
        {
            if(heatCache[i] != null){
                heatCache[i] = null;
            }

            if(heatCache[i] == null)
            {
                BlockEntity blockEntity = this.level.getBlockEntity(new BlockPos(pos.getX() + dir.offsetX, pos.getY(), pos.getZ() + dir.offsetZ));
                
                if (blockEntity instanceof RbmkBaseTE){
                    RbmkBaseTE base = (RbmkBaseTE) blockEntity;
                    heatCache[i] = base;
                }
            }
            i++;
        }

        for (RbmkBaseTE base : heatCache)
        {
            if (base != null){
                connected.add(base);
                heatTotal += base.heat;
            }
        }

        int memb = connected.size();
        float stepSize = 0.2f;

        if (memb > 1)
        {
            float targetHeat = heatTotal / (float) memb;
            for (RbmkBaseTE rbmk : connected)
            {
                float delta = targetHeat - rbmk.heat;
                rbmk.heat += delta * stepSize;
            }
        }
    }

    protected void passiveCooling()
    {
        this.heat -= passiveHeatLoss;
        if (this.heat < 16){
            this.heat = 16;
        }
    }

    public void onOverheat()
    {
        if(level.isClientSide()){
            level.addParticle(ParticleTypes.LARGE_SMOKE, worldPosition.getX() * (level.random.nextFloat() * .2f), worldPosition.getY() + 0.5f, worldPosition.getZ() * (level.random.nextFloat() * .2f), level.random.nextFloat() * .1f, 3.5f * (level.random.nextFloat() * .25f), level.random.nextFloat() * .1f);
        }
        level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.3f, 1.18f - (level.random.nextFloat() * .2f), true);
    }

    public void onMeltdown()
    {
        if(!level.isClientSide()){
            //blow up
            level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2, BlockInteraction.DESTROY);
            if(level.random.nextInt(3) == 0){
                level.setBlockAndUpdate(this.pos, Blocks.LAVA.defaultBlockState());
            }
        }
    }

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (this.heat <= 16f){
            return true;
        }
        else if (this.heat >= overheatThreshold)
        {
            Lang.translate(BuildConfig.MODID + "gui.goggles.rbmkreactorbase.overheated")
			.add(Lang.number(this.heat))
				.style(ChatFormatting.DARK_RED)
                .style(ChatFormatting.BOLD)
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip, 1);
            return true;
        }
        else
        {
            Lang.translate(BuildConfig.MODID + "gui.goggles.rbmkreactorbase.heat")
            .add(Lang.number(this.heat))
                .style(ChatFormatting.GOLD)
                .style(ChatFormatting.BOLD)
            .style(ChatFormatting.GRAY)
            .forGoggles(tooltip, 1);
            return true;
        }
	}

    @Override
    protected void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        nbt.putFloat("heat", this.heat);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        this.heat = nbt.getFloat("heat");
    }

    @Override
    public void setRemoved(){
        super.setRemoved();
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
    }
}
