package mod.dooggoo.createatomic.api.radiation.playerradiation;

import com.jozufozu.flywheel.repack.joml.Math;

import net.minecraft.nbt.CompoundTag;

public class PlayerRadiationData {
    
    private float radiation;
    public float radiationResistance;

    public float getRadiation() {
        radiation = Math.clamp(0f, 2100f, radiation);
        return radiation;
    }

    public void setRadiation(float radiation) {
        this.radiation = Math.clamp(0f, 2100f, radiation);
        this.radiation = radiation;
    }

    public void addRadiation(float Amout) {
        this.radiation = Math.clamp(0f, 2100f, radiation);
        this.radiation += Amout;
    }

    public void removeRadiation(float Amout) {
        this.radiation = Math.clamp(0f, 2100f, radiation);
        this.radiation -= Amout;
    }

    public void copyFrom(PlayerRadiationData source) {
        this.radiation = Math.clamp(0f, 2100f, radiation);
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
