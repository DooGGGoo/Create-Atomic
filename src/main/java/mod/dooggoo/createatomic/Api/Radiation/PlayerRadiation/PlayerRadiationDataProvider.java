package mod.dooggoo.createatomic.Api.Radiation.PlayerRadiation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerRadiationDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> 
{
    public static Capability<PlayerRadiationData> PLAYER_RADIATION = CapabilityManager.get(new CapabilityToken<>(){});

    private PlayerRadiationData radiationData = null;
    private final LazyOptional<PlayerRadiationData> opt = LazyOptional.of(this::createRadiationData);

    @Nonnull
    private PlayerRadiationData createRadiationData() 
    {
        if (radiationData == null) 
        {
            radiationData = new PlayerRadiationData();
        }
        return radiationData;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) 
    {
        if (cap == PLAYER_RADIATION) 
        {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) 
    {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() 
    {
        CompoundTag nbt = new CompoundTag();
        createRadiationData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) 
    {
        createRadiationData().loadNBTData(nbt);
    }
}
