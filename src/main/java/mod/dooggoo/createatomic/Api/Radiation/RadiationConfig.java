package mod.dooggoo.createatomic.Api.Radiation;

import net.minecraftforge.common.ForgeConfigSpec;

public class RadiationConfig {
    
    public static ForgeConfigSpec.DoubleValue PlayerRadiationDeathThreshold;// = 1f;



    public static void registerServerConfig(ForgeConfigSpec.Builder server) {
        server.comment("Radiation Config");
        PlayerRadiationDeathThreshold = server
            .comment("The amount of radiation a player must have to die")
            .defineInRange("radiationconfig.playerDeathThreshold", 1f, 0f, Float.MAX_VALUE);

        server.pop();
    }
}
