package mod.dooggoo.createatomic.register;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.Blocks.BlockEntity.RbmkBaseTE;

public class ModTiles {

    public static final CreateRegistrate registrate = CreateAtomic.registrate()
            .creativeModeTab(() -> AtomicCreativeModeTab.MAIN_TAB);

    public static final BlockEntityEntry<RbmkBaseTE> RBMK_BASE_TE = registrate.tileEntity("rbmk_base_te", RbmkBaseTE::new)
            .validBlock(ModBlocks.RBMK_BASE)
            .register();


    public static void register() {}
}
