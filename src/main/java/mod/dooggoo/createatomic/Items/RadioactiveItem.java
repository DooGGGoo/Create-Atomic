package mod.dooggoo.createatomic.items;

import mod.dooggoo.createatomic.api.radiation.playerradiation.PlayerRadiationDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RadioactiveItem extends Item {
    public RadioactiveItem(Properties properties) {
        super(properties);
    }

    public float Radiation;

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(new TextComponent(this.Radiation + " RAD/s").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack Stack, Level Level, Entity Entity, int SlotId, boolean IsSelected) 
    {
        float finalRadiation = (Radiation * Stack.getCount()) / 20;
        if(Entity instanceof Player player)
        {
            player.getCapability(PlayerRadiationDataProvider.PLAYER_RADIATION).ifPresent(data -> {
                if(finalRadiation > data.radiationResistance)
                {
                    data.addRadiation(finalRadiation);
                }
            });
        }
    }
}
