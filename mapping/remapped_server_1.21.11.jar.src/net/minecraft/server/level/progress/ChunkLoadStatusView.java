package net.minecraft.server.level.progress;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public interface ChunkLoadStatusView {
  void moveTo(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos);
  
  ChunkStatus get(int paramInt1, int paramInt2);
  
  int radius();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\progress\ChunkLoadStatusView.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */