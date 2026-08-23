package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.util.VisibleForDebug;

public interface ProtocolInfo<T extends PacketListener> {
  ConnectionProtocol id();
  
  PacketFlow flow();
  
  StreamCodec<ByteBuf, Packet<? super T>> codec();
  
  BundlerInfo bundlerInfo();
  
  public static interface DetailsProvider {
    ProtocolInfo.Details details();
  }
  
  public static interface Details {
    ConnectionProtocol id();
    
    PacketFlow flow();
    
    @VisibleForDebug
    void listPackets(PacketVisitor param1PacketVisitor);
    
    @FunctionalInterface
    public static interface PacketVisitor {
      void accept(PacketType<?> param2PacketType, int param2Int);
    }
  }
  
  @FunctionalInterface
  public static interface PacketVisitor {
    void accept(PacketType<?> param1PacketType, int param1Int);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\ProtocolInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */