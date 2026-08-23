package net.minecraft.server.level;

import java.util.List;
import net.minecraft.world.level.ChunkPos;

public interface PlayerProvider {
  List<ServerPlayer> getPlayers(ChunkPos paramChunkPos, boolean paramBoolean);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkHolder$PlayerProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */