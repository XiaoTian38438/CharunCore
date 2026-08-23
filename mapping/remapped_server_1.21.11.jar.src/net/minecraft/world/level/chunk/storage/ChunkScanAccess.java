package net.minecraft.world.level.chunk.storage;

import java.util.concurrent.CompletableFuture;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.world.level.ChunkPos;

public interface ChunkScanAccess {
  CompletableFuture<Void> scanChunk(ChunkPos paramChunkPos, StreamTagVisitor paramStreamTagVisitor);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\ChunkScanAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */