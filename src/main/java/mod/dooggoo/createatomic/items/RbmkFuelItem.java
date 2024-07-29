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
    public float skinHeat = 16f;
    public float maxHeat = 2500f;
    public float heatPerFlux = 1f;
    public float fuel2skinHeat = 0.02f;
    public float xenonGenRate = 0.5f;
    public float xenonBurnRate = 50f;
    public float yield = 100000000f;

    public FuelFunction func = FuelFunction.LOG_TEN;

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
        skinHeat = getBlockHeat(itemStack);

		if(fuelHeat > skinHeat) {
			
			float mid = (fuelHeat - skinHeat) / 2f;
			
			fuelHeat -= mid * fuel2skinHeat;
			skinHeat += mid * fuel2skinHeat;
			
			setFuelHeat(itemStack, fuelHeat);
			setSkinHeat(itemStack, skinHeat);
		}
    }

    public float releaseHeat(ItemStack itemStack, float heat)
    {
        float skinHeat = getBlockHeat(itemStack);

        if(skinHeat > maxHeat)
        {
			float fuelHeat = getFuelHeat(itemStack);
			float a = (heat + fuelHeat + skinHeat) / 3f;
			setFuelHeat(itemStack, a);
			setSkinHeat(itemStack, a);
			return a;
        }

        if(skinHeat <= heat){
            return 0;
        }

        float a = (skinHeat - heat) / 2f;
        skinHeat -= a;
        setSkinHeat(itemStack, skinHeat);
        return a;
    }

    /**
     * PASSIVE: fluxFromSelfIgnition * depletion (S)
     * <p>
     * EULER: 100 / (1 + 2.7^(-(flux - 50) / 10)) (S)
     * <p>
     * LOG_TEN: log10(x + 1) * flux * 50 (M)
     * <p>
     * SQUARE_ROOT: sqrt(flux) * 10 * flux (M)
     * <p>
     * LINEAR: "flux^2 (D)
     * <p>
     * QUADRATIC: flux^2 / 100 * flux (D)
     */
    public enum FuelFunction {
        EULER(),
        LOG_TEN(),
        SQUARE_ROOT(),
        LINEAR(),
        QUADRATIC();

        FuelFunction() {}
    }

    private float fluxFunction(float fluxIn, float enrichment) {
        float fluxF = fluxIn * fluxModByEnrichment(enrichment);

        switch (func) {
            case EULER: return (float) ((1 - Math.pow(Math.E, -fluxF / 25f)) * flux);
            case LOG_TEN: return (float) (Math.log10(fluxF + 1) * 0.5f * flux);
            case SQUARE_ROOT: return (float) (Math.sqrt(fluxF) * flux / 10f);
            case LINEAR: return fluxF / 100f * flux;
            case QUADRATIC: return  fluxF * fluxF / 10000f * flux;
        }
        return  0;
    }

    public String funcDesc (ItemStack stack) {
        String function = "";

        switch (this.func) {
            case EULER: function = (ChatFormatting.DARK_GREEN + "100 / (1 + 2.7^(-(flux - 50) / 10))");
            case LOG_TEN: function = (ChatFormatting.YELLOW + "log10(x + 1) * flux * 50");
            case SQUARE_ROOT: function = (ChatFormatting.GOLD + "sqrt(flux) * 10 * flux");
            case LINEAR: function = (ChatFormatting.RED + "flux^2");
            case QUADRATIC: function = (ChatFormatting.DARK_RED + "flux^2 / 100 * flux");
            default: function = (ChatFormatting.DARK_RED + "ERROR");
        }
        return function;
    }

    /**
     * @return enrichment [0;1]
     */
    public float getEnrichment(ItemStack itemStack) {
        return getYield(itemStack) / ((RbmkFuelItem) itemStack.getItem()).getYield(itemStack);
    }

    /**
     * @return poison [0;1]
     */
    public float getXenonPoisoningLevel(ItemStack itemStack) {
        return  getXenonPoison(itemStack) / 100f;
    }

    private float fluxModByEnrichment(float enrichment) {
        return (float) (enrichment + (Math.sin(enrichment * Math.PI) / 3f));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag IsAdvanced) {
        list.add(new TextComponent("Flux: " + flux + "/t").withStyle(ChatFormatting.BLUE));
        if (fluxFromSelfIgnition != 0) {
            list.add(new TextComponent("Flux from self ignition: " + fluxFromSelfIgnition + "/t").withStyle(ChatFormatting.BLUE));
        }
        list.add(new TextComponent("Flux Types (in/out): " + inType.name() + "/" + outType.name()).withStyle(ChatFormatting.GRAY));
        list.add(new TextComponent("Flux Function: " + funcDesc(itemStack)));
        list.add(new TextComponent("Heat: " + getFuelHeat(itemStack) + "/" + maxHeat).withStyle(ChatFormatting.DARK_RED));
        list.add(new TextComponent("Fuel Depletion: " + (1 - getEnrichment(itemStack)) * 100f + "%").withStyle(ChatFormatting.GREEN));
        list.add(new TextComponent("Xenon: " + getXenonPoison(itemStack) + "%").withStyle(ChatFormatting.DARK_PURPLE));
        super.appendHoverText(itemStack, level, list, IsAdvanced);
    }

    public void setBurnFunc(FuelFunction function) {
        this.func = function;
    }

    public FuelFunction getBurnFunc() {
        return  this.func;
    }

//#region NBT

    public RbmkFuelItem setTypes(Type in, Type out)
    {
        inType = in;
        outType = out;
        return this;
    }

    private static void setFuelHeat(ItemStack itemStack, float fuelHeat) {
        setFloat(itemStack, "createatomic.heat", fuelHeat);
    }

    public float getFuelHeat(ItemStack itemStack) {
        return getFloat(itemStack, "createatomic.heat");
    }

    private static void setSkinHeat(ItemStack itemStack, float skinHeat) {
        setFloat(itemStack, "createatomic.blockheat", skinHeat);
    }

    private float getBlockHeat(ItemStack itemStack) {
        return getFloat(itemStack, "createatomic.blockheat");
    }

    private static void setYield(ItemStack itemStack, float yield) {
        setFloat(itemStack, "createatomic.yield", yield);
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


    public float getXenonPoison(ItemStack itemStack) {
        return getFloat(itemStack, "createatomic.xenon");
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
        setSkinHeat(itemStack, 16f);
        setXenonPoison(itemStack, 0f);
    }

//#endregion

}
