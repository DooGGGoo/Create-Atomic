package mod.dooggoo.createatomic.register;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkBaseTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkFuelRodTE;

public class ModTiles {

    public static final CreateRegistrate registrate = CreateAtomic.registrate()
            .creativeModeTab(() -> ModCreativeModeTab.MAIN_TAB);

    public static final BlockEntityEntry<RbmkBaseTE> RBMK_BASE_TE = registrate.tileEntity("rbmk_base_te", RbmkBaseTE::new)
            .validBlocks(ModBlocks.RBMK_BASE)
            .register();

    public static final BlockEntityEntry<RbmkFuelRodTE> RBMK_FUEL_ROD_TE = registrate.tileEntity("rbmk_fuel_rod_te", RbmkFuelRodTE::new)
            .validBlocks(ModBlocks.RBMK_FUEL_ROD)
            .register();


    public static void register() {}
}
