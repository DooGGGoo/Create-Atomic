package mod.dooggoo.createatomic.register;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.Blocks.BlockEntity.RBMK_base;

public class ModTiles {

    public static final CreateRegistrate registrate = CreateAtomic.registrate()
            .creativeModeTab(() -> AtomicCreativeModeTab.MAIN_TAB);

    public static final BlockEntityEntry<RBMK_base> RBMK_BASE = registrate.tileEntity("rbmk_base", RBMK_base::new)
            .validBlock(ModBlocks.RBMK_BASE)
            .register();






    public static void register() {}
}
