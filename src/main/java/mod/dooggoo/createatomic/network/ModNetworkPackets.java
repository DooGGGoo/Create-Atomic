package mod.dooggoo.createatomic.network;

import mod.dooggoo.createatomic.CreateAtomic;
import mod.dooggoo.createatomic.network.packet.RbmkControlRodS2CPacket;
import mod.dooggoo.createatomic.network.packet.RbmkFuelRodS2CPacket;
import mod.dooggoo.createatomic.network.packet.RbmkHeatS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworkPackets {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register()
    {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CreateAtomic.MODID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(RbmkHeatS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RbmkHeatS2CPacket::new)
            .encoder(RbmkHeatS2CPacket::toBytes)
            .consumer(RbmkHeatS2CPacket::handle)
            .add();

        net.messageBuilder(RbmkFuelRodS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RbmkFuelRodS2CPacket::new)
            .encoder(RbmkFuelRodS2CPacket::toBytes)
            .consumer(RbmkFuelRodS2CPacket::handle)
            .add();

        net.messageBuilder(RbmkControlRodS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RbmkControlRodS2CPacket::new)
            .encoder(RbmkControlRodS2CPacket::toBytes)
            .consumer(RbmkControlRodS2CPacket::handle)
            .add();
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}
