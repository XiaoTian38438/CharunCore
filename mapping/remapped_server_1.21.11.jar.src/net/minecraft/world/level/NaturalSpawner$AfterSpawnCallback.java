package net.minecraft.world.level;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface AfterSpawnCallback {
  void run(Mob paramMob, ChunkAccess paramChunkAccess);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\NaturalSpawner$AfterSpawnCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */