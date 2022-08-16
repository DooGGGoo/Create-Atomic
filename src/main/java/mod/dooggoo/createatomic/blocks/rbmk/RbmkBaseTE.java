package mod.dooggoo.createatomic.blocks.rbmk;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.Lang;

import mod.dooggoo.createatomic.BuildConfig;
import mod.dooggoo.createatomic.api.Directions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkBaseTE extends BlockEntity implements IHaveGoggleInformation{    
    public RbmkBaseTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState)  {
        super(type, blockPos, blockState);
    }

    public static float passiveHeatLoss = .16f;
    public static float overheatThreshold = 1025f;
    public float heat = 16f;
    public static float maxHeat = 1300f;

    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkBaseTE be) 
    {
        if(!be.hasLevel() || be.pos == null){
            be.setLevel(Level);
            be.pos = Pos;
        }

        be.transferHeat();
        be.passiveCooling();
        if (be.heat > overheatThreshold) {
            be.onOverheat();
        }
        if (be.heat > maxHeat) {
            be.onMeltdown();
        }
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

    private void transferHeat()
    {
        if (level == null)
        return;
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
                BlockEntity blockEntity = level.getBlockEntity(new BlockPos(pos.getX() + dir.offsetX, pos.getY(), pos.getZ() + dir.offsetZ));
                
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

    protected void passiveCooling(){
        this.heat -= passiveHeatLoss;
        if (this.heat < 16){
            this.heat = 16;
        }
    }

    public void onOverheat(){
        level.addParticle(ParticleTypes.LARGE_SMOKE, worldPosition.getX() * (level.random.nextFloat() * .2f), worldPosition.getY() + 0.5f, worldPosition.getZ() * (level.random.nextFloat() * .2f), level.random.nextFloat() * .1f, 3.5f * (level.random.nextFloat() * .25f), level.random.nextFloat() * .1f);
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
        if(heat < overheatThreshold){
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmkbase.heat").withStyle(ChatFormatting.GRAY)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.GOLD)));
                    //.withStyle(ChatFormatting.AQUA)).append(Lang.translateDirect("gui.goggles.at_current_speed").withStyle(ChatFormatting.DARK_GRAY)));
        }
        else{
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmkbase.overheat").withStyle(ChatFormatting.GRAY)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.RED)));
                    //.withStyle(ChatFormatting.AQUA)).append(Lang.translateDirect("gui.goggles.at_current_speed").withStyle(ChatFormatting.DARK_GRAY)));
        }
		return true;
	}

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putFloat("heat", this.heat);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.heat = nbt.getFloat("heat");
    }

    @Override
    public void setRemoved(){
        super.setRemoved();
    }
}
