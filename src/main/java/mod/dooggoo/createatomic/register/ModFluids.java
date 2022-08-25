package mod.dooggoo.createatomic.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.FluidEntry;

import mod.dooggoo.createatomic.BuildConfig;
import mod.dooggoo.createatomic.CreateAtomic;
import net.minecraft.resources.ResourceLocation;

public class ModFluids {
    private static final CreateRegistrate registrate = CreateAtomic.registrate();

	public static final ResourceLocation STEAM_STILL = new ResourceLocation(BuildConfig.MODID, "fluid/steam_still");
	public static final ResourceLocation STEAM_FLOW = new ResourceLocation(BuildConfig.MODID,"fluid/steam_flowing");

	public static final FluidEntry<VirtualFluid> STEAM = registrate.virtualFluid("steam", STEAM_STILL, STEAM_FLOW)
		.lang("Steam")
		.tag(AllTags.forgeFluidTag("steam"))
		.register();

            
    public static void register() {}
}
