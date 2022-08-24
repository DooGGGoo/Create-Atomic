package mod.dooggoo.createatomic.blocks.rbmk;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class RbmkControlRodTE extends RbmkBaseTE{
    public ExtentionPercentage extention = ExtentionPercentage.FULLIN;
    private BlockState state;

    public RbmkControlRodTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public enum ExtentionPercentage {
        FULLIN(0),
        QUARTEROUT(1),
        HALF(2),
        QUARTERIN(3),
        FULLOUT(4);
        
        private int percentage;
        
        ExtentionPercentage(int percentage) {
            this.percentage = percentage;
        }
        
        public int getPercentage() {
            return this.percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }
    }

    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkControlRodTE be) {
        if (!Level.isClientSide()){
            be.state = State;

            // ModNetworkPackets.INSTANCE.send(PacketDistributor.TRACKING_CHUNK
            //         .with(() -> be.level.getChunkAt(be.worldPosition)), 
            //     new RbmkControlRodS2CPacket(be.worldPosition, be.extention.getPercentage()));
            
            be.sendToClient();
            be.transferHeat();
            be.passiveCooling();
            be.sendToClient();
            be.onMeltdown();
        }
        be.onOverheat();
    }

    public void extend() {
        if(extention.getPercentage() == 0) extention.setPercentage(1);
        else if(extention.getPercentage() == 1) extention.setPercentage(2);
        else if(extention.getPercentage() == 2) extention.setPercentage(3);
        else if(extention.getPercentage() == 3) extention.setPercentage(4);
        else if(extention.getPercentage() == 4) extention.setPercentage(4);

        //this.updateBlockState(state.setValue(RbmkControlRod.EXTENTION, Integer.valueOf(extention.getPercentage())));
        this.level.setBlockAndUpdate(pos, state.setValue(RbmkControlRod.EXTENTION, Integer.valueOf(extention.getPercentage())));
        this.setChanged();
        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    public void retract() {
        if(extention.getPercentage() == 0) extention.setPercentage(0);
        else if(extention.getPercentage() == 1) extention.setPercentage(0);
        else if(extention.getPercentage() == 2) extention.setPercentage(1);
        else if(extention.getPercentage() == 3) extention.setPercentage(2);
        else if(extention.getPercentage() == 4) extention.setPercentage(3);

        //this.updateBlockState(state.setValue(RbmkControlRod.EXTENTION, Integer.valueOf(extention.getPercentage())));
        this.level.setBlockAndUpdate(pos, state.setValue(RbmkControlRod.EXTENTION, Integer.valueOf(extention.getPercentage())));
        this.setChanged();
        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("extention", extention.getPercentage());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        extention.percentage = tag.getInt("extention");
        super.load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, be -> {
            CompoundTag tag = new CompoundTag();
            tag.putInt("extention", extention.getPercentage());
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
        nbt.putInt("extention", extention.getPercentage());
        saveAdditional(nbt);
        return nbt;
    }

}

