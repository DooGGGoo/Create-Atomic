package mod.dooggoo.createatomic.Items;

import mod.dooggoo.createatomic.Api.Radiation.PlayerRadiation.PlayerRadiationDataProvider;
import mod.dooggoo.createatomic.register.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GeigerCounter extends Item {
    public GeigerCounter(Properties properties) {
        super(properties);
    }

    private float radPerSec = 0f;
    private float oldRad = 0f;
    private int t = 0;

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) 
    {
        if(entity instanceof Player && !level.isClientSide) 
        {
            Player player = (Player) entity;
            player.getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).ifPresent(data -> 
            {
                float rad = Math.abs(data.getRadiation());
                t++;
                if(t >= 20)
                {
                    t = 0;
                    radPerSec = rad - oldRad;
                    oldRad = Math.abs(rad);
                }
            });

            if (isSelected)
            {
                playGeigerSound(level, player, intencity(Math.abs(radPerSec)));
            }
        }
    }

    private int intencity (float rps)
    {
        if (rps <= 0f)
            return 0;
        if (rps > .1f && rps < 5f)
            return 1;
        else if (rps >= 5 && rps < 10)
            return 2;
        else if (rps >= 10 && rps < 15)
            return 3;
        else if (rps >= 15 && rps < 20)
            return 4;
        else if (rps >= 20 && rps < 25)
            return 5;
        else if (rps >= 25);
            return 6;
    }

    private void playGeigerSound(Level level, Player player, int intencity)
    {
        switch (intencity)
        {
            case 0:
                if (level.random.nextInt(160) == 0)
                    if(level.random.nextInt(2) == 0)
                    {
                        level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_1.get(), SoundSource.RECORDS, 1f, 1f);
                    }
                    else 
                    {
                        level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_2.get(), SoundSource.RECORDS, 1f, 1f);
                    }
                break;
            case 1:
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_1.get(), SoundSource.RECORDS, 1.0f, 1.0f);
                break;
            case 2:
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_2.get(), SoundSource.RECORDS, 1.0f, 1.0f);
                break;
            case 3:
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_3.get(), SoundSource.RECORDS, 1.0f, 1.0f);
                break;
            case 4:
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_4.get(), SoundSource.RECORDS, 1.0f, 1.0f);
                break;
            case 5:
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_5.get(), SoundSource.RECORDS, 1.0f, 1.0f);
                break;
            case 6:
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.GEIGER_COUNTER_6.get(),SoundSource.RECORDS, 1.0f, 1.0f);
                break;
        }
    }


}