package mod.dooggoo.createatomic.network;

import mod.dooggoo.createatomic.blocks.rbmk.RbmkBaseTE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

@SuppressWarnings("resource")
public class UpdateHeat {
    public static boolean updateHeat(BlockPos pos, float heat) {
       final BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
        if (be instanceof final RbmkBaseTE rbmk) {
                rbmk.Heat = heat;
                return true;
        }
        return false;
    }
}
