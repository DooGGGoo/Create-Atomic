package mod.dooggoo.createatomic.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

public class PlatedIronArmorItem extends ArmorItem 
{
    public float radiationResistance;

    public PlatedIronArmorItem(EquipmentSlot slot, Properties properties) {
        super(ModArmorMaterials.PLATED_IRON, slot, properties.stacksTo(1));
    }

    
}

