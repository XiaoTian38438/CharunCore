package net.minecraft.server.level;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface LevelChangeListener {
  void onLevelChange(ChunkPos paramChunkPos, IntSupplier paramIntSupplier, int paramInt, IntConsumer paramIntConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkHolder$LevelChangeListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */