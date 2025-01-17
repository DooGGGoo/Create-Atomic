package mod.dooggoo.createatomic;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import mod.dooggoo.createatomic.network.ModNetworkPackets;
import mod.dooggoo.createatomic.register.*;
import mod.dooggoo.createatomic.register.config.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CreateAtomic.MODID)
public class CreateAtomic {
	
	public static final String MODID = "createatomic";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static IEventBus modEventBus;

	public static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MODID);

	public CreateAtomic() {
		modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModNetworkPackets.register();
		ModFluids.register();
		ModItems.register();
		ModBlocks.register();
		ModEntities.register();
		ModTiles.register();
		ModSounds.register(modEventBus);
		if (DatagenModLoader.isRunningDataGen()) {
			modEventBus.addListener((GatherDataEvent g) -> ModPonder.generateLang(registrate(), g));
		}
		modEventBus.addListener((FMLClientSetupEvent e) -> ModPonder.register());
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> ModPartials::load);
		ModConfigs.register();
	}

	public static CreateRegistrate registrate() {
		return REGISTRATE.get();
	}
}
