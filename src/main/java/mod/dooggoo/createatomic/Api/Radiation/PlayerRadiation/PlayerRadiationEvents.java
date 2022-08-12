package mod.dooggoo.createatomic.api.radiation.playerradiation;

import mod.dooggoo.createatomic.BuildConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BuildConfig.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerRadiationEvents 
{
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            if(!event.getObject().getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).isPresent())
            {
                event.addCapability(new ResourceLocation(BuildConfig.MODID, "player_radiation_properties"), new PlayerRadiationDataProvider());
            }
        }
    }    

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event)
    {
        if (event.isWasDeath() == false)
        {
            event.getOriginal().getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).ifPresent(oldData -> {
                event.getPlayer().getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).ifPresent(newData -> {
                    newData.copyFrom(oldData);
                });
            });
        }
    }
    private static int t = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            Player player = event.player;
            float playerRadiation = player.getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).map(PlayerRadiationData::getRadiation).orElse(0.0F);
            Level level = player.getLevel();
            t += t;

            if (t >= 10)
            {
                t = 0;
                PlayerRadiationEffects.applyRadiationEffects(playerRadiation, player, level);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(PlayerRadiationData.class);
    }
}
