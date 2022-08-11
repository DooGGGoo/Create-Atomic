package mod.dooggoo.createatomic.Blocks.BlockEntity;

import java.util.ArrayList;
import java.util.List;

import mod.dooggoo.createatomic.Api.Radiation.Directions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RBMK_base extends BlockEntity {

    public static float passiveHeatLoss = 16f;
    public static float overheatThreshold = 1025f;
    public float heat = 16f;
    public static float maxHeat = 1300f;

    public RBMK_base(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState)  {
       super(type, blockPos, blockState);
    }

    // Thanks to Drillgon200 for his rbmk code so i can yoink it

    protected RBMK_base[] heatCache = new RBMK_base[4];

    private BlockPos pos = this.getBlockPos();

    public static final Directions[] directions = new Directions[]
    {
        Directions.NORTH, 
        Directions.SOUTH, 
        Directions.EAST, 
        Directions.WEST, 
    };

    private void transferHeat()
    {
        List<RBMK_base> connected = new ArrayList<>();
        connected.add(this);
        float heatTotal = this.heat;

        int i = 0;
        for(Directions dir : directions)
        {
            if(heatCache[i] != null)
            {
                heatCache[i] = null;
            }

            if(heatCache[i] == null)
            {
                BlockEntity blockEntity = this.level.getBlockEntity(new BlockPos(pos.getX() + dir.offsetX, pos.getY(), pos.getZ() + dir.offsetZ));
                
                if (blockEntity instanceof RBMK_base)
                {
                    RBMK_base base = (RBMK_base) blockEntity;
                    heatCache[i] = base;
                }
            }

            i++;
        }

        for (RBMK_base base : heatCache)
        {
            if (base != null)
            {
                connected.add(base);
                heatTotal += base.heat;
            }
        }

        int memb = connected.size();
        float stepSize = 0.2f;

        if (memb > 1)
        {
            float targetHeat = heatTotal / (float) memb;
            for (RBMK_base rbmk : connected)
            {
                float delta = targetHeat - rbmk.heat;
                rbmk.heat += delta * stepSize;
            }
            //this.markDirty();
        }
    }

    protected void passiveCooling()
    {
        this.heat -= passiveHeatLoss;
        if (this.heat < 16)
        {
            this.heat = 16;
        }
    }

    public void onOverheat()
    {
        if (this.heat > overheatThreshold)
        {
            if(level.isClientSide())
            {
                //
            }
        }
    }



    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putFloat("heat", this.heat);
    }

    @Override
    public void load(CompoundTag nbt) {
        this.heat = nbt.getFloat("heat");
    }

}
