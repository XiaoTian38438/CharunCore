package net.minecraft.world.level.chunk.status;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface ChunkStatusTask {
  CompletableFuture<ChunkAccess> doWork(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\status\ChunkStatusTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */