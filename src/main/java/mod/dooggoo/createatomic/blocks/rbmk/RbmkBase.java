package mod.dooggoo.createatomic.blocks.rbmk;

import mod.dooggoo.createatomic.register.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkBase extends BaseEntityBlock {

    public RbmkBase(Properties properties) {
        super(properties);
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTiles.RBMK_BASE_TE.create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModTiles.RBMK_BASE_TE.get(), RbmkBaseTE::tick);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() == pNewState.getBlock()) {
            return;
        }
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof RbmkFuelRodTE) {
            ((RbmkFuelRodTE) be).drops();
        }
        be.setRemoved();
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
	public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
