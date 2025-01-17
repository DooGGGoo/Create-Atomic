package mod.dooggoo.createatomic.register;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab  extends CreativeModeTab {
    private final Supplier<ItemStack> suppl;
    private ModCreativeModeTab (final String name, final Supplier<ItemStack> supplier) {
        super(name);
        suppl = supplier;
    }
    @Override
    public @NotNull ItemStack makeIcon() {
        return suppl.get();
    }

    public static final String TAB_NAME = "createatomic";

    public static final CreativeModeTab MAIN_TAB = new ModCreativeModeTab(
        TAB_NAME, () -> new ItemStack(ModBlocks.URANIUM_BLOCK.get()));
}
