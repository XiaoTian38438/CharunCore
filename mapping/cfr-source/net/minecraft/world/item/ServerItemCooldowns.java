/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item;

import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemCooldowns;

public class ServerItemCooldowns
extends ItemCooldowns {
    private final ServerPlayer player;

    public ServerItemCooldowns(ServerPlayer serverPlayer) {
        this.player = serverPlayer;
    }

    @Override
    protected void onCooldownStarted(Identifier identifier, int n) {
        super.onCooldownStarted(identifier, n);
        this.player.connection.send(new ClientboundCooldownPacket(identifier, n));
    }

    @Override
    protected void onCooldownEnded(Identifier identifier) {
        super.onCooldownEnded(identifier);
        this.player.connection.send(new ClientboundCooldownPacket(identifier, 0));
    }
}

