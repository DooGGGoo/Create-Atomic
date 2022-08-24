package mod.dooggoo.createatomic.blocks.rbmk;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkConrolRodExtenderTE extends KineticTileEntity {

    public float rotationsForSwitch = 8f; // Number of rotation required to switch the extention (32 rots om 32 rpm will take 1 min, 16 rots on 32 rpm will take 30 sec, etc.)
    public float count = 0f; // Number of rotations since last switch
    private BlockEntity controlRodTE;

    public RbmkConrolRodExtenderTE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        //if (level.isClientSide()) return;
        controlRodTE = level.getBlockEntity(this.worldPosition.atY(this.worldPosition.getY() + 1));
        if(speed != 0 && controlRodTE instanceof RbmkControlRodTE) {
            count += speed / 512;
            if(count >= rotationsForSwitch) {
                count = 0;
                ((RbmkControlRodTE) controlRodTE).extend();
                level.playSound(null, worldPosition, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.5f, 1.5f);
            }
            else if(count <= -rotationsForSwitch) {
                count = 0;
                ((RbmkControlRodTE) controlRodTE).retract();
                level.playSound(null, worldPosition, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.5f, 0.5f);
            }
        }
    }
    
	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putFloat("count", count);
		super.write(compound, clientPacket);
	}
	
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		count = compound.getFloat("count");
		super.read(compound, clientPacket);
	}

}
