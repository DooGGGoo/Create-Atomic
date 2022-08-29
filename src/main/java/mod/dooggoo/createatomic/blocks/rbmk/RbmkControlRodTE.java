package mod.dooggoo.createatomic.blocks.rbmk;

import mod.dooggoo.createatomic.CreateAtomic;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class RbmkControlRodTE extends RbmkBaseTE {
    private BlockState state;
    private int percentage;

    public RbmkControlRodTE(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }
        
        public int getPercentage() {
            return Math.max(0, Math.min(4, percentage)); //clamping
        }

        public void setPercentage(int percentage) {
            this.percentage = Math.max(0, Math.min(4, percentage)); //clamping
        }

    public static void tick(Level Level, BlockPos Pos, BlockState State, RbmkControlRodTE be) {
        be.state = State;
        if(be.state == null){
            be.state = Level.getBlockState(Pos);
        }
        if (!Level.isClientSide()){
            be.sendToClient();
            be.transferHeat();
            be.passiveCooling();
            be.sendToClient();
            be.onMeltdown();
        }
        be.onOverheat();
    }

    public void extend() {
        switch (getPercentage()) {
            case 0 : {
                setPercentage(1);
                break;
            }
            case 1 : {
                setPercentage(2);
                break;
            }
            case 2 :  {
                setPercentage(3);
                break;
            }
            case 3, 4 :  {
                setPercentage(4);
                break;
            }
        }

        this.level.setBlockAndUpdate(pos, state.setValue(RbmkControlRod.EXTENTION, getPercentage()));
        updateBlockState(state.setValue(RbmkControlRod.EXTENTION, getPercentage()));
        this.level.sendBlockUpdated(pos, state.setValue(RbmkControlRod.EXTENTION, getPercentage()), state.setValue(RbmkControlRod.EXTENTION, getPercentage()), 3);
        setChanged();
        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    public void retract() {
        switch (getPercentage()) {
            case 4 : {
                setPercentage(3);
                break;
            }
            case 3 : {
                setPercentage(2);
                break;
            }
            case 2 : {
                setPercentage(1);
                break;
            }
            case 1, 0 : {
                setPercentage(0);
                break;
            }
        }

        this.level.setBlockAndUpdate(pos, state.setValue(RbmkControlRod.EXTENTION, getPercentage()));
        updateBlockState(state.setValue(RbmkControlRod.EXTENTION, getPercentage()));
        this.level.sendBlockUpdated(pos, state.setValue(RbmkControlRod.EXTENTION, getPercentage()), state.setValue(RbmkControlRod.EXTENTION, getPercentage()), 3);
        setChanged();
        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(heat > overheatThreshold){
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(CreateAtomic.MODID+ ".tooltip.rbmkbase.overheat").withStyle(ChatFormatting.WHITE)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.RED)));
        }
        else{
            tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(CreateAtomic.MODID + ".tooltip.rbmkbase.heat").withStyle(ChatFormatting.WHITE)));
            tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.heat + " ").withStyle(ChatFormatting.GOLD)));
        }
        tooltip.add(new TextComponent(spacing).append(new TranslatableComponent(CreateAtomic.MODID + ".tooltip.rbmkcontrol.extension").withStyle(ChatFormatting.WHITE)));
        tooltip.add(new TextComponent(spacing).append(new TextComponent(" " + this.getPercentage() + " ").withStyle(ChatFormatting.GOLD)));
        return true;
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("extension", getPercentage());
        tag.putFloat("heat", this.heat);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        percentage = tag.getInt("extension");
        this.heat = tag.getFloat("heat");
        super.load(tag);
    }
}

