package mod.dooggoo.createatomic.blocks.rbmk;

import mod.dooggoo.createatomic.register.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RbmkFuelRod extends BaseEntityBlock {
    public static final BooleanProperty HASFUEL = BooleanProperty.create("hasfuel");

    public RbmkFuelRod(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HASFUEL, Boolean.valueOf(false)));
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTiles.RBMK_FUEL_ROD_TE.create(pPos, pState);
    }

    //TODO: fix interaction with fuel on fuel rod blockS
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown()) return InteractionResult.PASS;

        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        RbmkFuelRodTE te = (RbmkFuelRodTE) pLevel.getBlockEntity(pPos);
        
        if(te.interact(pPlayer, pHand, itemstack))
            return InteractionResult.SUCCESS;
        else return InteractionResult.PASS;
    }
        
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModTiles.RBMK_FUEL_ROD_TE.get(), RbmkFuelRodTE::tick);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof RbmkFuelRodTE) {
            ((RbmkFuelRodTE) be).drops();
        }
        pLevel.removeBlockEntity(pPos);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
	public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HASFUEL);
    }
}
