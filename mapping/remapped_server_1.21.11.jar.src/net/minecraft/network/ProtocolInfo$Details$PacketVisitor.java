package net.minecraft.network;

import net.minecraft.network.protocol.PacketType;

@FunctionalInterface
public interface PacketVisitor {
  void accept(PacketType<?> paramPacketType, int paramInt);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\ProtocolInfo$Details$PacketVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */