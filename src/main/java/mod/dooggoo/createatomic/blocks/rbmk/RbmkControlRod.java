package mod.dooggoo.createatomic.blocks.rbmk;

import mod.dooggoo.createatomic.register.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RbmkControlRod extends BaseEntityBlock {
    public static final IntegerProperty EXTENTION = IntegerProperty.create("extention", 0, 4);
    
    public RbmkControlRod(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(EXTENTION, 0));
    }

    public void updateState(int val) {
        this.stateDefinition.any().setValue(EXTENTION, val);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTiles.RBMK_CONTROL_ROD_TE.create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModTiles.RBMK_CONTROL_ROD_TE.get(), RbmkControlRodTE::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(EXTENTION);
    }

    @Override
	public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // It took me fucking 2 days to figure out that i need to put this shit not only in rbmkbase block
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
}
