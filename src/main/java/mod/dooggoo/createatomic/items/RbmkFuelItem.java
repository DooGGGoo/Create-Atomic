package mod.dooggoo.createatomic.items;

import java.util.List;

import javax.annotation.Nullable;

import mod.dooggoo.createatomic.blocks.rbmk.RbmkFuelRodTE.Type;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RbmkFuelItem extends RadioactiveItem {
    public float flux = 16f;
    public float fluxFromSelfIgnition = 0f;
    public float fuelHeat = 16f;
    public float blockHeat = 16f;
    public float maxHeat = 2355f;
    public float heatPerFlux = 1f;
    public float fuel2blockHeat = 0.02f;
    public float xenonGenRate = 0.05f;
    public float xenonBurnRate = 50f;
    public float yield = 1000000f;

    public Type inType = Type.SLOW;
    public Type outType = Type.FAST;

    public RbmkFuelItem(Properties properties) {
        super(properties);
    }

    public float burnFuel(ItemStack itemStack, float flux)
    {
        flux += fluxFromSelfIgnition;
        float xenon = getXenonPoison(itemStack);

        xenon -= (flux * flux) / xenonBurnRate;

        flux *= (1f - getXenonPoisoningLevel(itemStack));

        xenon += flux * xenonGenRate;

		if(xenon < 0f) xenon = 0f;
		if(xenon > 100f) xenon = 100f;

        setXenonPoison(itemStack, xenon);

        float fluxOut = fluxFunction(flux, getEnrichment(itemStack));

        float yield = getYield(itemStack);
        yield -= flux;
        if(yield < 0f) yield = 0f;

        setYield(itemStack, yield);

        fuelHeat = getFuelHeat(itemStack);
        fuelHeat += flux * heatPerFlux;
        setFuelHeat(itemStack, fuelHeat);

        return fluxOut;
    }

    public void updateHeat(Level level, ItemStack itemStack)
    {
        fuelHeat = getFuelHeat(itemStack);
        blockHeat = getBlockHeat(itemStack);

		if(fuelHeat > blockHeat) {
			
			float mid = (fuelHeat - blockHeat) / 2f;
			
			fuelHeat -= mid * fuel2blockHeat;
			blockHeat += mid * fuel2blockHeat;
			
			setFuelHeat(itemStack, fuelHeat);
			setBlockHeat(itemStack, blockHeat);
		}
    }

    public float releaseHeat(ItemStack itemStack, float heat)
    {
        float blockHeat = getBlockHeat(itemStack);

        if(blockHeat > maxHeat)
        {
			float fuelHeat = getFuelHeat(itemStack);
			float a = (heat + fuelHeat + blockHeat) / 2f;
			setFuelHeat(itemStack, a);
			setBlockHeat(itemStack, a);
			return a;
        }

        if(blockHeat <= heat){
            return 0;
        }

        float a = (heat + blockHeat) / 2f;
        blockHeat -= a;
        setBlockHeat(itemStack, blockHeat);
        return a;
    }


//#region NBT

    public RbmkFuelItem setTypes(Type in, Type out)
    {
        inType = in;
        outType = out;
        return this;
    }

    private static void setFuelHeat(ItemStack itemStack, float fuelHeat2) {
        setFloat(itemStack, "createatomic.heat", fuelHeat2);
    }

    public float getFuelHeat(ItemStack itemStack) {
        return getFloat(itemStack, "createatomic.heat");
    }

    private static void setBlockHeat(ItemStack itemStack, float blockHeat2) {
        setFloat(itemStack, "createatomic.blockheat", blockHeat2);
    }

    private float getBlockHeat(ItemStack itemStack) {
        return getFloat(itemStack, "createatomic.blockheat");
    }

    private static void setYield(ItemStack itemStack, float yield2) {
        setFloat(itemStack, "createatomic.yield", yield2);
    }

    private float fluxFunction(float flux2, float enrichment) {
        float Flux = flux2 * enrichment;
        return (float) (Math.sqrt(Flux) * flux / 10f);
    }

    public float getEnrichment(ItemStack itemStack) {
        return getYield(itemStack) / ((RbmkFuelItem) itemStack.getItem()).getYield(itemStack);
    }

    private float getYield(ItemStack itemStack) {
        		
		if(itemStack.getItem() instanceof RbmkFuelItem) {
			return getFloat(itemStack, "createatomic.yield");
		}
		
		return 0;
    }

    private static void setXenonPoison(ItemStack itemStack, float xenon) {
        setFloat(itemStack, "createatomic.xenon", xenon);
    }

    private float getXenonPoisoningLevel(ItemStack itemStack) {
        return  getXenonPoison(itemStack) / 100f;
    }

    public float getXenonPoison(ItemStack itemStack) {
        return getFloat(itemStack, "createatomic.xenon");
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag IsAdvanced) {
        list.add(new TextComponent("Flux: " + flux + "/t").withStyle(ChatFormatting.BLUE));
        if (fluxFromSelfIgnition != 0) {
            list.add(new TextComponent("Flux from self ignition: " + fluxFromSelfIgnition + "/t").withStyle(ChatFormatting.BLUE));
        }
        list.add(new TextComponent("Flux Types (in/out): " + inType.name() + "/" + outType.name()).withStyle(ChatFormatting.GRAY));
        list.add(new TextComponent("Heat: " + getFuelHeat(itemStack) + "/" + maxHeat).withStyle(ChatFormatting.DARK_RED));
        list.add(new TextComponent("Fuel Depletion: " + (1 - getEnrichment(itemStack)) * 100f + "%").withStyle(ChatFormatting.GREEN));
        list.add(new TextComponent("Xenon: " + getXenonPoison(itemStack) + "%").withStyle(ChatFormatting.DARK_PURPLE));
        super.appendHoverText(itemStack, level, list, IsAdvanced);
    }

    public static void setFloat(ItemStack itemStack,String key, float value) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putFloat(key, value);
    }

    public static float getFloat(ItemStack itemStack,String key) {
        if (itemStack.getTag() == null) {
            setDefaultTags(itemStack);
        }

        return itemStack.getTag().getFloat(key);
    }

    private static void setDefaultTags(ItemStack itemStack) {
        itemStack.setTag(new CompoundTag());
        setYield(itemStack, ((RbmkFuelItem)itemStack.getItem()).yield);
		setFuelHeat(itemStack, 16f);
        setBlockHeat(itemStack, 16f);
        setXenonPoison(itemStack, 0f);
    }

//#endregion

}
