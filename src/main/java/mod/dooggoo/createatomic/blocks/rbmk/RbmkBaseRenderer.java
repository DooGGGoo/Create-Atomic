package mod.dooggoo.createatomic.blocks.rbmk;

import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;

public class RbmkBaseRenderer<T extends RbmkBaseTE> extends SmartTileEntityRenderer<T> {
    public RbmkBaseRenderer(Context ctx){
        super(ctx);
    }
}
