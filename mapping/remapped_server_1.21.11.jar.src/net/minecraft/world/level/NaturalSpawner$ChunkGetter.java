package net.minecraft.world.level;

import java.util.function.Consumer;
import net.minecraft.world.level.chunk.LevelChunk;

@FunctionalInterface
public interface ChunkGetter {
  void query(long paramLong, Consumer<LevelChunk> paramConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\NaturalSpawner$ChunkGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */