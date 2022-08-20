package mod.dooggoo.createatomic.blocks.rbmk;

import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.foundation.block.ITE;

import mod.dooggoo.createatomic.register.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkControlRodExtender extends KineticBlock implements ITE<RbmkConrolRodExtenderTE> {

    public RbmkControlRodExtender(Properties properties) {
        super(properties);
    }

    @Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModTiles.RBMK_CONTROL_ROD_EXTENDER_TE.create(pos, state);
	}

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        worldIn.removeBlockEntity(pos);
    }

	@Override
	public Direction.Axis getRotationAxis(BlockState arg0) {
		return Direction.Axis.Y;
	}

	@Override
	public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}

    @Override
    public BlockEntityType<? extends RbmkConrolRodExtenderTE> getTileEntityType() {
        return ModTiles.RBMK_CONTROL_ROD_EXTENDER_TE.get();
    }

    @Override
    public Class<RbmkConrolRodExtenderTE> getTileEntityClass() {
        return RbmkConrolRodExtenderTE.class;
    }
    
}
