package mod.dooggoo.createatomic.register;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkBaseTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkConrolRodExtenderRenderer;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkControlRodExtenderTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkControlRodTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkFuelRodTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkModeratorTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkReflectorTE;
import mod.dooggoo.createatomic.blocks.rbmk.RbmkSteamChannelTE;

public class ModTiles {

    public static final CreateRegistrate registrate = CreateAtomic.registrate()
        .creativeModeTab(() -> ModCreativeModeTab.MAIN_TAB);

    public static final BlockEntityEntry<RbmkBaseTE> RBMK_BASE_TE = registrate.tileEntity("rbmk_base_te", RbmkBaseTE::new)
        .validBlocks(ModBlocks.RBMK_BASE)
        .register();

    public static final BlockEntityEntry<RbmkFuelRodTE> RBMK_FUEL_ROD_TE = registrate.tileEntity("rbmk_fuel_rod_te", RbmkFuelRodTE::new)
        .validBlocks(ModBlocks.RBMK_FUEL_ROD)
        .register();

    public static final BlockEntityEntry<RbmkReflectorTE> RBMK_REFLECTOR_TE = registrate.tileEntity("rbmk_reflector_te", RbmkReflectorTE::new)
        .validBlocks(ModBlocks.RBMK_REFLECTOR)
        .register();

    public static final BlockEntityEntry<RbmkModeratorTE> RBMK_MODERATOR_TE = registrate.tileEntity("rbmk_moderator_te", RbmkModeratorTE::new)
        .validBlocks(ModBlocks.RBMK_MODERATOR)
        .register();

    public static final BlockEntityEntry<RbmkControlRodTE> RBMK_CONTROL_ROD_TE = registrate.tileEntity("rbmk_control_rod_te", RbmkControlRodTE::new)
        .validBlocks(ModBlocks.RBMK_CONTROL_ROD)
        .register();

    public static final BlockEntityEntry<RbmkControlRodExtenderTE> RBMK_CONTROL_ROD_EXTENDER_TE = registrate.tileEntity("rbmk_control_rod_extender_te", RbmkControlRodExtenderTE::new)
        .validBlocks(ModBlocks.RBMK_CONTROL_ROD_EXTENDER)
        .renderer(() -> RbmkConrolRodExtenderRenderer::new)
        .register();

    public static final BlockEntityEntry<RbmkSteamChannelTE> RBMK_STEAM_CHANNEL_TE = registrate.tileEntity("rbmk_steam_channel", RbmkSteamChannelTE::new)
        .validBlocks(ModBlocks.RBMK_STEAM_CHANNEL)
        .register();

    public static void register() {}
}
