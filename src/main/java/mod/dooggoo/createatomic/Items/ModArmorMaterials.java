package mod.dooggoo.createatomic.items;


import java.util.function.Supplier;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.register.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("deprecation")
public enum ModArmorMaterials implements ArmorMaterial{
     PLATED_IRON("plated_iron", 13, new int[]{2, 5, 6, 2}, 7, SoundEvents.ARMOR_EQUIP_IRON, 
      0.0F, 0.0F, () -> Ingredient.of(ModItems.PROTECTIVE_PLATING.get()));
  
     private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
     private final String name;
     private final int durabilityMultiplier;
     private final int[] slotProtections;
     private final int enchantmentValue;
     private final SoundEvent sound;
     private final float toughness;
     private final float knockbackResistance;
     private final LazyLoadedValue<Ingredient> repairIngredient;
  
     ModArmorMaterials(String pName, int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
        this.name = pName;
        this.durabilityMultiplier = pDurabilityMultiplier;
        this.slotProtections = pSlotProtections;
        this.enchantmentValue = pEnchantmentValue;
        this.sound = pSound;
        this.toughness = pToughness;
        this.knockbackResistance = pKnockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
     }
  
     public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
     }
  
     public int getDefenseForSlot(EquipmentSlot pSlot) {
        return this.slotProtections[pSlot.getIndex()];
     }
  
     public int getEnchantmentValue() {
        return this.enchantmentValue;
     }
  
     public SoundEvent getEquipSound() {
        return this.sound;
     }
  
     public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
     }
  
     public String getName() {
        return CreateAtomic.MODID + ":" + this.name;
     }
  
     public float getToughness() {
        return this.toughness;
     }

     public float getKnockbackResistance() {
        return this.knockbackResistance;
     }
    
}
