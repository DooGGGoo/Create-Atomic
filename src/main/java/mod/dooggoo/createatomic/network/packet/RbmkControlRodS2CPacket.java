package mod.dooggoo.createatomic.network.packet;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.function.Supplier;

import mod.dooggoo.createatomic.network.UpdateExtentionLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class RbmkControlRodS2CPacket {
    public final BlockPos pos;
    public final int extentionValue;

    public RbmkControlRodS2CPacket(BlockPos pos, int extentionValue) {
        this.pos = pos;
        this.extentionValue = extentionValue;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(extentionValue);
    }
    
    public RbmkControlRodS2CPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> success.set(UpdateExtentionLevel.updateExtentionLevel(pos, extentionValue))); 
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}