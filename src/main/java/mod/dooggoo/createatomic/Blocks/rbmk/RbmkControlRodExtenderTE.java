package mod.dooggoo.createatomic.blocks.rbmk;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RbmkControlRodExtenderTE extends KineticTileEntity {
    private float count = 0f;
    private static final float rotationsForSwitch = 8f;

    public RbmkControlRodExtenderTE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            this.trySwitch(this.speed);
        }
    }

    private void trySwitch(float speed) {
        BlockEntity controlRodTE = level.getBlockEntity(this.worldPosition.above(1));

        if (speed != 0 && controlRodTE instanceof RbmkControlRodTE) {
            this.count += convertToLinear(speed);

            if (this.count >= rotationsForSwitch) {
                this.count = 0;
                ((RbmkControlRodTE) controlRodTE).extend();
                level.playSound(null, worldPosition, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.5f, 1.5f);

            } else if (this.count <= -rotationsForSwitch) {
                this.count = 0;
                ((RbmkControlRodTE) controlRodTE).retract();
                level.playSound(null, worldPosition, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 0.5f, 0.5f);
            }
        }
    }

	@Override
	public void write(CompoundTag tag, boolean clientPacket) {
		tag.putFloat("count", count);
		super.write(tag, clientPacket);
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		count = tag.getFloat("count");
		super.read(tag, clientPacket);
	}

}
