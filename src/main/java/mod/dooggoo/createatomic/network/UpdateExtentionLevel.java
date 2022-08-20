package mod.dooggoo.createatomic.network;

import mod.dooggoo.createatomic.blocks.rbmk.RbmkControlRodTE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

@SuppressWarnings("resource")
public class UpdateExtentionLevel {
    public static boolean updateExtentionLevel(BlockPos pos, int extention) {
        final BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
        
        if (be instanceof RbmkControlRodTE rbmk) {
            rbmk.extentionValue = extention;
            return true;
        }
        return false;
    }
}