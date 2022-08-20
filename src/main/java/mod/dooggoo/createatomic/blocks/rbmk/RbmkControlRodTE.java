package mod.dooggoo.createatomic.blocks.rbmk;

import mod.dooggoo.createatomic.network.ModNetworkPackets;
import mod.dooggoo.createatomic.network.packet.RbmkControlRodS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

public class RbmkControlRodTE extends RbmkBaseTE{
    public ExtentionPercentage extention = ExtentionPercentage.FULLIN;
    public int extentionValue = 0;


    public RbmkControlRodTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public enum ExtentionPercentage {
        FULLIN(0),
        QUARTEROUT(1),
        HALF(2),
        QUARTERIN(3),
        FULLOUT(4);
        
        private final int percentage;
        
        ExtentionPercentage(int percentage) {
            this.percentage = percentage;
        }
        
        public int getPercentage() {
            return this.percentage;
        }
    }

    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkControlRodTE be) {
        if (!Level.isClientSide()){
            be.getBlockState().setValue(RbmkControlRod.EXTENTION, be.extentionValue);
            
            ModNetworkPackets.INSTANCE.send(PacketDistributor.TRACKING_CHUNK
                    .with(() -> be.level.getChunkAt(be.worldPosition)), 
                new RbmkControlRodS2CPacket(be.worldPosition, be.extentionValue));
            
            be.sendToClient();
            be.transferHeat();
            be.passiveCooling();
            be.sendToClient();
            be.onMeltdown();
        }
        be.extentionValue = be.extention.getPercentage();
        be.onOverheat();
    }

    public void extend() {
        switch (extention) {
            case FULLIN:
                extention = ExtentionPercentage.QUARTEROUT;
                break;
            case QUARTEROUT:
                extention = ExtentionPercentage.HALF;
                break;
            case HALF:
                extention = ExtentionPercentage.QUARTERIN;
                break;
            case QUARTERIN:
                extention = ExtentionPercentage.FULLOUT;
                break;
            case FULLOUT:
                extention = ExtentionPercentage.FULLOUT;
                break;
        }
    }

    public void retract() {
        switch (extention) {
            case FULLOUT:
                extention = ExtentionPercentage.QUARTERIN;
                break;
            case QUARTERIN:
                extention = ExtentionPercentage.HALF;
                break;
            case HALF:
                extention = ExtentionPercentage.QUARTEROUT;
                break;
            case QUARTEROUT:
                extention = ExtentionPercentage.FULLIN;
                break;
            case FULLIN:
                extention = ExtentionPercentage.FULLIN;
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("extention", extentionValue);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        extentionValue = tag.getInt("extention");
        super.load(tag);
    }

}

