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
    public ExtentionPercentage extension = ExtentionPercentage.FULLIN;
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
        be.state = State;
        if(be.state == null){
            be.state = Level.getBlockState(Pos);
        }
        if (!Level.isClientSide()){
            be.sendToClient();
            be.transferHeat();
            be.passiveCooling();
            be.sendToClient();
            be.onMeltdown();
        }
        be.onOverheat();
    }

    public void extend() {
        if(extension.getPercentage() == 0) extension.setPercentage(1);
        else if(extension.getPercentage() == 1) extension.setPercentage(2);
        else if(extension.getPercentage() == 2) extension.setPercentage(3);
        else if(extension.getPercentage() == 3) extension.setPercentage(4);
        else if(extension.getPercentage() == 4) extension.setPercentage(4);

        this.updateBlockState(state.setValue(RbmkControlRod.EXTENTION, extension.getPercentage()));
        assert this.level != null;
        this.level.setBlockAndUpdate(pos, state.setValue(RbmkControlRod.EXTENTION, extension.getPercentage()));
        this.setChanged();
        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    public void retract() {
        if(extension.getPercentage() == 0) extension.setPercentage(0);
        else if(extension.getPercentage() == 1) extension.setPercentage(0);
        else if(extension.getPercentage() == 2) extension.setPercentage(1);
        else if(extension.getPercentage() == 3) extension.setPercentage(2);
        else if(extension.getPercentage() == 4) extension.setPercentage(3);

        this.updateBlockState(state.setValue(RbmkControlRod.EXTENTION, extension.getPercentage()));
        if (this.level != null) {
            this.level.setBlockAndUpdate(pos, state.setValue(RbmkControlRod.EXTENTION, extension.getPercentage()));
        }
        this.setChanged();
        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("extension", extension.getPercentage());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        extension.percentage = tag.getInt("extension");
        super.load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, be -> {
            CompoundTag tag = new CompoundTag();
            tag.putInt("extension", extension.getPercentage());
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
        nbt.putInt("extension", extension.getPercentage());
        saveAdditional(nbt);
        return nbt;
    }

}

