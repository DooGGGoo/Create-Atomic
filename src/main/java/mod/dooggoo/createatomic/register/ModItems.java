package mod.dooggoo.createatomic.register;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.Items.GeigerCounter;
import mod.dooggoo.createatomic.Items.PlatedIronArmorItem;
import mod.dooggoo.createatomic.Items.RadioactiveItem;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class ModItems {
    private static final CreateRegistrate registrate = CreateAtomic.registrate()
            .creativeModeTab(() -> AtomicCreativeModeTab.MAIN_TAB);
    
    public static final ItemEntry<Item> RAW_URANIUM = registrate.item("raw_uranium", Item::new)
        .tag(Tags.Items.RAW_MATERIALS)
        .register();
    
    public static final ItemEntry<RadioactiveItem> URANIUM_INGOT = registrate.item("uranium_ingot", RadioactiveItem::new)
        .onRegister(c -> c.Radiation = 1f)
        .tag(Tags.Items.INGOTS)
        .register();

    public static final ItemEntry<RadioactiveItem> RMBK_FUEL = registrate.item("rmbk_fuel", RadioactiveItem::new)
        .properties(p -> p.stacksTo(1))
        .onRegister(c -> c.Radiation = 8f)
        .register();

    public static final ItemEntry<RadioactiveItem> URANIUM_PELLET = registrate.item("uranium_pellet", RadioactiveItem::new)
        .onRegister(c -> c.Radiation = 2f)
        .register();
    
    public static final ItemEntry<GeigerCounter> GEIGER_COUNTER = registrate.item("geiger_counter", GeigerCounter::new)
        .properties(p -> p.stacksTo(1))
        .register();

    public static final ItemEntry<Item> PROTECTIVE_PLATING = registrate.item("protective_plating", Item::new)
        .tag(AllTags.AllItemTags.PLATES.tag)
        .register();

    //Armor items
    public static final ItemEntry<? extends PlatedIronArmorItem>

        PLATED_IRON_HELMET = registrate.item("plated_iron_helmet", p -> new PlatedIronArmorItem(EquipmentSlot.HEAD, p))
            .onRegister(c -> c.radiationResistance = 4f)
            .register(),

        PLATED_IRON_CHESTPLATE = registrate.item("plated_iron_chestplate", p -> new PlatedIronArmorItem(EquipmentSlot.CHEST, p))
            .onRegister(c -> c.radiationResistance = 5f)
            .register(),

        PLATED_IRON_LEGGINGS = registrate.item("plated_iron_leggings", p -> new PlatedIronArmorItem(EquipmentSlot.LEGS, p))
            .onRegister(c -> c.radiationResistance = 4f)
            .register(),

        PLATED_IRON_BOOTS = registrate.item("plated_iron_boots", p -> new PlatedIronArmorItem(EquipmentSlot.FEET, p))
            .onRegister(c -> c.radiationResistance = 3f)
            .register();


    public static void register() {}
}
