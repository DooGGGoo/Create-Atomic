package mod.dooggoo.createatomic.network.packet;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import mod.dooggoo.createatomic.network.UpdateHeat;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class RbmkHeatS2CPacket {
    public final BlockPos pos;
    public final float heat;

    public RbmkHeatS2CPacket(BlockPos pos, float heat) {
        this.pos = pos;
        this.heat = heat;
    }

    public RbmkHeatS2CPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readFloat());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFloat(heat);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> success.set(UpdateHeat.updateHeat(pos, heat)));
            
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
    
}
