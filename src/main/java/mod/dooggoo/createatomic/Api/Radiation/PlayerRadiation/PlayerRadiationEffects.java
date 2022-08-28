package mod.dooggoo.createatomic.api.radiation.playerradiation;

import java.util.Random;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerRadiationEffects {

    public static DamageSource radiationDamageSource = new DamageSource("radiation").bypassArmor().bypassMagic();

    public static void applyRadiationEffects(float playerRadiation, Player player, Level level) {

        Random random = level.random;

        if (player.isCreative() || player.isSpectator() || player.isInvulnerable() || player.isInvulnerableTo(radiationDamageSource)) {
            return;
        }

        switch(getPlayerIrradiation(playerRadiation))
        {
            case 6:
                player.hurt(radiationDamageSource, 69420f);
                break;

            case 5:
                if (random.nextInt(250) == 0) player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 35));
                if (random.nextInt(300) == 0) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 2));
                if (random.nextInt(300) == 0) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 1));
                if (random.nextInt(500) == 0) player.addEffect(new MobEffectInstance(MobEffects.POISON, 45, 1));
                if (random.nextInt(700) == 0) player.addEffect(new MobEffectInstance(MobEffects.WITHER, 5, 0));
                if (random.nextInt(300) == 0) player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 120, 2));  
                break;

            case 4:
                if (random.nextInt(250) == 0) player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 25));
                if (random.nextInt(350) == 0) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 75, 1));
                if (random.nextInt(350) == 0) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 85, 1));
                if (random.nextInt(500) == 0) player.addEffect(new MobEffectInstance(MobEffects.POISON, 30, 1));
                if (random.nextInt(350) == 0) player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 70, 1));  
                break;

            case 3:
                if (random.nextInt(300) == 0) player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 15));
                if (random.nextInt(400) == 0) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 0));
                if (random.nextInt(350) == 0) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50, 1));
                if (random.nextInt(350) == 0) player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 45, 1));  
                break;

            case 2:
                if (random.nextInt(300) == 0) player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 15));
                if (random.nextInt(400) == 0) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 1));
                if (random.nextInt(400) == 0) player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 30, 1));  
                break;

            case 1:
                if (random.nextInt(300) == 0) player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 15));
                if (random.nextInt(400) == 0) player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20, 1));  
                break;

            case 0:
                break;
        }
    }

    private static int getPlayerIrradiation(float radiation)
    {
        if (radiation >= 1100f){
            return 6;
        }
        else if (radiation >= 750f){
            return 5;
        }
        else if (radiation >= 500f){
            return 4;
        }
        else if (radiation >= 250f){
            return 3;
        }
        else if (radiation >= 100f){
            return 2;
        }
        else if (radiation >= 50f){
            return 1;
        }
        else return 0;
    }
}
