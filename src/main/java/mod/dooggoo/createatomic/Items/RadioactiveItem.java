package mod.dooggoo.createatomic.Items;

import java.util.List;

import mod.dooggoo.createatomic.Api.Radiation.PlayerRadiation.PlayerRadiationDataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RadioactiveItem extends Item {
    public RadioactiveItem(Properties properties) {
        super(properties);
    }

    public float Radiation;

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(new TranslatableComponent("§s§7" + Radiation + " RAD/s"));
    }

    @Override
    public void inventoryTick(ItemStack Stack, Level Level, Entity Entity, int SlotId, boolean IsSelected) 
    {
        float finalRadiation = (Radiation * Stack.getCount()) / 20;
        if(Entity instanceof Player) 
        {
            Player player = (Player) Entity;
            player.getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).ifPresent(data -> {
                if(finalRadiation > data.radiationResistance)
                {
                    data.addRadiation(finalRadiation);
                }
            });
        }
    }
}
