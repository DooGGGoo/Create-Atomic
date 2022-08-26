package mod.dooggoo.createatomic.api.radiation.playerradiation;

import net.minecraft.nbt.CompoundTag;

public class PlayerRadiationData {
    
    private float radiation;
    public float radiationResistance;

    public static float clamp(float min, float max, float val) {
        return Math.max(min, Math.min(max, val));
    }

    public float getRadiation() {
        radiation = clamp(0f, 2100f, radiation);
        return radiation;
    }

    public void setRadiation(float radiation) {
        this.radiation = clamp(0f, 2100f, radiation);
        this.radiation = radiation;
    }

    public void addRadiation(float Amout) {
        this.radiation = clamp(0f, 2100f, radiation);
        this.radiation += Amout;
    }

    public void removeRadiation(float Amout) {
        this.radiation = clamp(0f, 2100f, radiation);
        this.radiation -= Amout;
    }

    public void copyFrom(PlayerRadiationData source) {
        this.radiation = clamp(0f, 2100f, radiation);
        this.radiation = source.getRadiation();
    }

//radiation resist//

    public float getRadiationResistance() {
        return this.radiationResistance;
    }

    public void setRadiationResistance(float radiationResistance) {
        this.radiationResistance = radiationResistance;
    }

    public void addRadiationResistance(float Amout) {
        this.radiationResistance += Amout;
    }

    public void removeRadiationResistance(float Amout) {
        this.radiationResistance -= Amout;
    }

    public void copyFromResistance(PlayerRadiationData source) {
        this.radiationResistance = source.getRadiationResistance();
    }

//nbt//

    public void saveNBTData(CompoundTag compound)
    {
        compound.putFloat("radiation", radiation);
        compound.putFloat("radiationResistance", radiationResistance);
    }

    public void loadNBTData(CompoundTag compound)
    {
        radiation = compound.getFloat("radiation");
        radiationResistance = compound.getFloat("radiationResistance");
    }
}
