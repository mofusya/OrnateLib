package net.mofusya.ornatelib.registries.networks.network;

import net.minecraft.server.level.ServerPlayer;

public class SimpleServerPacket extends ServerPacket {

    private final Runnable runnable;

    public SimpleServerPacket(String id, Runnable runnable) {
        super(id);
        this.runnable = runnable;
    }

    @Override
    protected ServerPacket newSelf() {
        return new SimpleServerPacket(this.id, this.runnable);
    }

    @Override
    protected void run(ServerPlayer player, int type) {
        this.runnable.run();
    }
}
