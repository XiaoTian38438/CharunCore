package net.minecraft.network;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.util.VisibleForDebug;

public interface Details {
  ConnectionProtocol id();
  
  PacketFlow flow();
  
  @VisibleForDebug
  void listPackets(PacketVisitor paramPacketVisitor);
  
  @FunctionalInterface
  public static interface PacketVisitor {
    void accept(PacketType<?> param2PacketType, int param2Int);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\ProtocolInfo$Details.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */