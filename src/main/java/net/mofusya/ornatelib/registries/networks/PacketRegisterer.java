package net.mofusya.ornatelib.registries.networks;

import net.mofusya.ornatelib.registries.networks.network.ClientPacket;
import net.mofusya.ornatelib.registries.networks.network.ServerPacket;

import java.util.ArrayList;

public class PacketRegisterer {
    private static int serverPacketId = 0;
    private static int clientPacketId = 0;

    private final ArrayList<ServerPacket> ServerPACKETS = new ArrayList<>();
    private final ArrayList<ClientPacket> ClientPACKETS = new ArrayList<>();
    private final String modId;

    public PacketRegisterer(String modId) {
        this.modId = modId;
    }

    public <T extends ServerPacket> T register(T packet) {
        ServerPACKETS.add(packet);
        return packet;
    }

    public <T extends ClientPacket> T register(T packet) {
        ClientPACKETS.add(packet);
        return packet;
    }

    public void register() {
        for (ServerPacket serverPacket : ServerPACKETS) {
            serverPacket.cast(this.modId).registerMessage(serverPacketId++, serverPacket.getClass(), serverPacket::encode, serverPacket::decode, serverPacket::handle);
        }
        for (ClientPacket clientPacket : ClientPACKETS) {
            clientPacket.cast(this.modId).registerMessage(clientPacketId++, clientPacket.getClass(), clientPacket::encode, clientPacket::decode, clientPacket::handle);
        }
    }
}
