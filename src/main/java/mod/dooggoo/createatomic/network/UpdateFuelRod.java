package mod.dooggoo.createatomic.network;

import mod.dooggoo.createatomic.blocks.rbmk.RbmkFuelRodTE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

@SuppressWarnings("resource")
public class UpdateFuelRod {
    public static boolean updateFuelRod(BlockPos pos, float heat, boolean hasFuel) {
       final BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);

        if (be instanceof RbmkFuelRodTE rbmk) {
            rbmk.heat = heat;
            rbmk.hasFuel = hasFuel;
            return true;
        }
        return false;
    }
}
