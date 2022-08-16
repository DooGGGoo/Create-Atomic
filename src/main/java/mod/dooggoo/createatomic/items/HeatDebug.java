package mod.dooggoo.createatomic.items;

import mod.dooggoo.createatomic.blocks.rbmk.RbmkBaseTE;
import mod.dooggoo.createatomic.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HeatDebug extends Item{
    public HeatDebug(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
     }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        if (!level.isClientSide && player != null) {
           BlockPos blockpos = pContext.getClickedPos();
           BlockState blockstate = level.getBlockState(blockpos);
           if (blockstate.is(ModBlocks.RBMK_BASE.get())) {
               RbmkBaseTE be = (RbmkBaseTE) level.getBlockEntity(blockpos);
                be.Heat += 200f;
           }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    
}
