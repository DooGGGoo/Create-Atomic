package mod.dooggoo.createatomic.blocks.rbmk;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;

import mod.dooggoo.createatomic.BuildConfig;
import mod.dooggoo.createatomic.api.Directions;
import mod.dooggoo.createatomic.network.ModNetworkPackets;
import mod.dooggoo.createatomic.network.packet.RbmkHeatS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

public class RbmkBaseTE extends BlockEntity implements IHaveGoggleInformation{    
    public RbmkBaseTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState)  {
        super(type, blockPos, blockState);
    }

    public static float passiveHeatLoss = 0.16f;
    public static float overheatThreshold = 1025f;
    public float heat = 16f;
    public static float maxHeat = 1300f;


    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkBaseTE be) 
    {
        if(!be.hasLevel() || be.pos == null){
            be.setLevel(Level);
            be.pos = Pos;
        }

        if(!be.level.isClientSide) {
            be.transferHeat();
            be.passiveCooling();
            be.sendToClient();
            be.onMeltdown();
        }

        be.onOverheat();
    }

    protected void sendToClient() {
        ModNetworkPackets.INSTANCE.send(PacketDistributor.TRACKING_CHUNK
                .with(() -> this.level.getChunkAt(this.worldPosition)), 
            new RbmkHeatS2CPacket(this.pos, this.heat));
    }

    protected RbmkBaseTE[] heatCache = new RbmkBaseTE[4];

    public BlockPos pos = this.getBlockPos();

    public static final Directions[] directions = new Directions[]
    {
        Directions.NORTH, 
        Directions.SOUTH, 
        Directions.EAST, 
        Directions.WEST, 
    };

    protected void transferHeat()
    {
        if (level == null)
        return;
        List<RbmkBaseTE> connected = new ArrayList<>();
        connected.add(this);
        float heatTotal = heat;

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
        float stepSize = 0.5f;

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
       heat = heat - passiveHeatLoss;
        if (heat < 16){
            heat = 16;
        }
    }

    private int t;

    public void onOverheat() {
        if (heat < overheatThreshold) {return;}
        t++;
        if (t > 5){
            if (level.isClientSide)  {
                this.level.addParticle(ParticleTypes.LAVA, pos.getX(), pos.getY() + 0.5f, pos.getZ(), level.random.nextFloat() * .1f, 3.5f * (level.random.nextFloat() * .1f), level.random.nextFloat() * .1f);
            }
            this.level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.7f, 1.18f - (level.random.nextFloat() * .2f), true);
            t = 0;
        }
    }

    public void onMeltdown()
    {
        if (heat < maxHeat) {return;}
        if(!level.isClientSide()){
            //blow up
            if(level.random.nextInt(2) == 0){
                level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
            }
            level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 5, BlockInteraction.BREAK);
            for(int i = 0; i < 8; i++) {
                //                                                          position                                    speed
                this.level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX(), pos.getY() + 0.5f, pos.getZ(), level.random.nextFloat() * .3f, 3.5f * (level.random.nextFloat() * .1f), level.random.nextFloat() * .3f);
            } 
        }
    }

    @Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = IHaveGoggleInformation.super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if(heat > overheatThreshold){
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmkbase.overheat").withStyle(ChatFormatting.GRAY)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.RED)));
        }
        else{
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(BuildConfig.MODID + ".tooltip.rbmkbase.heat").withStyle(ChatFormatting.GRAY)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.GOLD)));
        }
		return added;
	}

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putFloat("heat", this.heat);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        heat = tag.getFloat("heat");
        super.load(tag);
    }

    public void updateBlockState(@Nullable BlockState newState) {
        if (this.level != null) {
            BlockState state = level.getBlockState(pos);
            if (newState == null)
                newState = state;
            level.sendBlockUpdated(pos, state, newState, 3);
            level.updateNeighborsAt(pos, newState.getBlock());
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, be -> {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("heat", heat);
            saveAdditional(tag);
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
        nbt.putFloat("heat", heat);
        saveAdditional(nbt);
        return nbt;
    }
}
