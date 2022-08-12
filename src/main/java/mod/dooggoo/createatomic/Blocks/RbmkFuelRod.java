package mod.dooggoo.createatomic.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RbmkFuelRod extends Block {

    public static final BooleanProperty CONTAINS_FUEL = BooleanProperty.create("contains_fuel");
    
    
    public RbmkFuelRod(Block.Properties properties) {    
        super(properties);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONTAINS_FUEL);
    }


}
