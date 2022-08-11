package mod.dooggoo.createatomic.Blocks.BlockEntity;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RBMK_base extends BlockEntity {

    public static float passiveHeatLoss = 16f;
    public float heat = 16f;
    public static float maxHeat = 1300f;
    public boolean isReflector = false;
    public boolean isModerator = false;
    public boolean isAbsorber = false;


    private final ItemStackHandler itemHandler = new ItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    
    public LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public RBMK_base(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState)  {
       super(type, blockPos, blockState);
    }







//#region Fucking wall of overrides
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nonnull Direction dir) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(capability, dir);
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }
//#endregion

    public void drops()
    {
        if (!level.isClientSide)
        {
            SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
            for (int i = 0; i < itemHandler.getSlots(); i++)
            {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }
        }
    }

}
