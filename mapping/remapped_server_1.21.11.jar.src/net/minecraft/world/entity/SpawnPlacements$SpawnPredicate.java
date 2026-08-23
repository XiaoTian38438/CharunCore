package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;

@FunctionalInterface
public interface SpawnPredicate<T extends Entity> {
  boolean test(EntityType<T> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SpawnPlacements$SpawnPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */