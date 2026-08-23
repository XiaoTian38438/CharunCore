package net.minecraft.server.level;

import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public interface Synchronizer {
  void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> paramPacket);
  
  void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> paramPacket);
  
  void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> paramPacket, Predicate<ServerPlayer> paramPredicate);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ServerEntity$Synchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */