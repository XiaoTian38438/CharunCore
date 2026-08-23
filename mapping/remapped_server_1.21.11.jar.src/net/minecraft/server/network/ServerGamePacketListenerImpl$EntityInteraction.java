package net.minecraft.server.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
interface EntityInteraction {
  InteractionResult run(ServerPlayer paramServerPlayer, Entity paramEntity, InteractionHand paramInteractionHand);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\ServerGamePacketListenerImpl$EntityInteraction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */