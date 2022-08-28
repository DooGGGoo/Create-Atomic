package mod.dooggoo.createatomic.network.packet;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.function.Supplier;

import mod.dooggoo.createatomic.network.UpdateExtensionLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class RbmkControlRodS2CPacket {
    public final BlockPos pos;
    public final int extensionValue;

    public RbmkControlRodS2CPacket(BlockPos pos, int extensionValue) {
        this.pos = pos;
        this.extensionValue = extensionValue;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(extensionValue);
    }
    
    public RbmkControlRodS2CPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> success.set(UpdateExtensionLevel.updateExtensionLevel(pos, extensionValue)));
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}