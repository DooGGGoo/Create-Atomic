package mod.dooggoo.createatomic.register;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.items.GeigerCounter;
import mod.dooggoo.createatomic.items.HeatDebug;
import mod.dooggoo.createatomic.items.PlatedIronArmorItem;
import mod.dooggoo.createatomic.items.RadioactiveItem;
import mod.dooggoo.createatomic.items.RbmkFuelItem;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.Tags;

public class ModItems {
    private static final CreateRegistrate registrate = CreateAtomic.registrate()
            .creativeModeTab(() -> ModCreativeModeTab.MAIN_TAB);
    
    public static final ItemEntry<Item> RAW_URANIUM = registrate.item("raw_uranium", Item::new)
        .tag(Tags.Items.RAW_MATERIALS)
        .register();
    
    public static final ItemEntry<RadioactiveItem> URANIUM_INGOT = registrate.item("uranium_ingot", RadioactiveItem::new)
        .onRegister(c -> c.Radiation = .25f)
        .tag(Tags.Items.INGOTS)
        .register();

    public static final ItemEntry<RadioactiveItem> URANIUM_NUGGET = registrate.item("uranium_nugget", RadioactiveItem::new)
        .onRegister(c -> c.Radiation = 0.02f)
        .tag(Tags.Items.NUGGETS)
        .register();

    public static final ItemEntry<RadioactiveItem> ENRICHED_URANIUM = registrate.item("enriched_uranium", RadioactiveItem::new)
        .onRegister(c -> c.Radiation = 1.2f)
        .register();
    
    public static final ItemEntry<GeigerCounter> GEIGER_COUNTER = registrate.item("geiger_counter", GeigerCounter::new)
        .properties(p -> p.stacksTo(1))
        .register();

    public static final ItemEntry<Item> PROTECTIVE_PLATING = registrate.item("protective_plating", Item::new)
        .tag(AllTags.AllItemTags.PLATES.tag)
        .register();

    public static final ItemEntry<HeatDebug> HEAT_DEBUG = registrate.item("heat_debug", HeatDebug::new)
        .properties(p -> p.rarity(Rarity.EPIC))
        .properties(p -> p.stacksTo(1))
        .register();

    //RBMK Fuels
    public static final ItemEntry<RbmkFuelItem> RBMK_FUEL = registrate.item("rbmk_fuel", RbmkFuelItem::new)
            .properties(p -> p.stacksTo(1))
            .onRegister(c -> c.Radiation = 2f)
            .onRegister(c -> c.setBurnFunc(RbmkFuelItem.FuelFunction.SQUARE_ROOT))
            .register();

    public static final ItemEntry<RbmkFuelItem> NEUTRON_SOURCE = registrate.item("rbmk_neutron_source", RbmkFuelItem::new)
            .properties(p -> p
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1))
            .onRegister(c -> c.Radiation = 4f)
            .onRegister(c -> c.setBurnFunc(RbmkFuelItem.FuelFunction.LOG_TEN))
            .onRegister(c -> c.fluxFromSelfIgnition = 4f)
            .onRegister(c -> c.flux = 8f)
            .register();

    public static final ItemEntry<RbmkFuelItem> RBMK_FUEL_PLUTONIUM = registrate.item("rbmk_fuel_plutonium", RbmkFuelItem::new)
            .properties(p -> p.stacksTo(1))
            .onRegister(c -> c.setBurnFunc(RbmkFuelItem.FuelFunction.QUADRATIC))
            .onRegister(c -> c.Radiation = 14f)
            .onRegister(c -> c.fluxFromSelfIgnition = 8f)
            .onRegister(c -> c.flux = 16f)
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
