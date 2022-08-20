package mod.dooggoo.createatomic.blocks.rbmk;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkConrolRodEntenderRenderer extends KineticTileEntityRenderer {
    
	public RbmkConrolRodEntenderRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	protected BlockState getRenderedBlockState(KineticTileEntity te) {
		return shaft(getRotationAxisOf(te)); 
	}
}
