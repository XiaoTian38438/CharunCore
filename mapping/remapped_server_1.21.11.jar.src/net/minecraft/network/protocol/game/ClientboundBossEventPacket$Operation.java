package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

interface Operation {
  ClientboundBossEventPacket.OperationType getType();
  
  void dispatch(UUID paramUUID, ClientboundBossEventPacket.Handler paramHandler);
  
  void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$Operation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */