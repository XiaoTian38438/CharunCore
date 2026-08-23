package net.minecraft.world.level.entity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.ChunkPos;

public interface EntityPersistentStorage<T> extends AutoCloseable {
  CompletableFuture<ChunkEntities<T>> loadEntities(ChunkPos paramChunkPos);
  
  void storeEntities(ChunkEntities<T> paramChunkEntities);
  
  void flush(boolean paramBoolean);
  
  default void close() throws IOException {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityPersistentStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */