package net.minecraft.util.debug;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public interface DebugValueAccess {
  <T> void forEachChunk(DebugSubscription<T> paramDebugSubscription, BiConsumer<ChunkPos, T> paramBiConsumer);
  
  <T> T getChunkValue(DebugSubscription<T> paramDebugSubscription, ChunkPos paramChunkPos);
  
  <T> void forEachBlock(DebugSubscription<T> paramDebugSubscription, BiConsumer<BlockPos, T> paramBiConsumer);
  
  <T> T getBlockValue(DebugSubscription<T> paramDebugSubscription, BlockPos paramBlockPos);
  
  <T> void forEachEntity(DebugSubscription<T> paramDebugSubscription, BiConsumer<Entity, T> paramBiConsumer);
  
  <T> T getEntityValue(DebugSubscription<T> paramDebugSubscription, Entity paramEntity);
  
  <T> void forEachEvent(DebugSubscription<T> paramDebugSubscription, EventVisitor<T> paramEventVisitor);
  
  @FunctionalInterface
  public static interface EventVisitor<T> {
    void accept(T param1T, int param1Int1, int param1Int2);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\debug\DebugValueAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */