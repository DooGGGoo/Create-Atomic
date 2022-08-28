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

public class RbmkSteamChannel extends BaseEntityBlock {

    public RbmkSteamChannel(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTiles.RBMK_STEAM_CHANNEL_TE.create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
            BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModTiles.RBMK_STEAM_CHANNEL_TE.get(), RbmkSteamChannelTE::tick);
    }

    // It took me fucking 2 days to figure out that I need to put this shit not only in rbmkbase block
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() == state.getBlock()) {
            return;
        }
        BlockEntity be = level.getBlockEntity(pos);
        be.setRemoved();
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
