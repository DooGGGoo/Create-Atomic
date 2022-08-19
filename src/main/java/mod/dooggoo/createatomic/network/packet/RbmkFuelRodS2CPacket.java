package mod.dooggoo.createatomic.network.packet;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;


import mod.dooggoo.createatomic.network.UpdateFuelRod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class RbmkFuelRodS2CPacket {
    public final BlockPos pos;
    public final float heat;
    public final boolean hasFuel;

    public RbmkFuelRodS2CPacket(BlockPos pos, float heat, boolean hasFuel) {
        this.pos = pos;
        this.heat = heat;
        this.hasFuel = hasFuel;
    }

    public RbmkFuelRodS2CPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readFloat(), buf.readBoolean());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeFloat(heat);
        buf.writeBoolean(hasFuel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> success.set(UpdateFuelRod.updateFuelRod(pos, heat, hasFuel))); 
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
    
}
