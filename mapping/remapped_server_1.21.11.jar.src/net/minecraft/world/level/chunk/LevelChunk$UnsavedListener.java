package net.minecraft.world.level.chunk;

import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface UnsavedListener {
  void setUnsaved(ChunkPos paramChunkPos);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\LevelChunk$UnsavedListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */