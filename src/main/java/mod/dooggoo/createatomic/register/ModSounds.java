package mod.dooggoo.createatomic.register;

import mod.dooggoo.createatomic.BuildConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BuildConfig.MODID);

    public static final RegistryObject<SoundEvent> GEIGER_COUNTER_1 = registerSoundEvent("geiger_counter_1");

    public static final RegistryObject<SoundEvent> GEIGER_COUNTER_2 = registerSoundEvent("geiger_counter_2");    

    public static final RegistryObject<SoundEvent> GEIGER_COUNTER_3 = registerSoundEvent("geiger_counter_3");

    public static final RegistryObject<SoundEvent> GEIGER_COUNTER_4 = registerSoundEvent("geiger_counter_4");

    public static final RegistryObject<SoundEvent> GEIGER_COUNTER_5 = registerSoundEvent("geiger_counter_5");

    public static final RegistryObject<SoundEvent> GEIGER_COUNTER_6 = registerSoundEvent("geiger_counter_6");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(BuildConfig.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
