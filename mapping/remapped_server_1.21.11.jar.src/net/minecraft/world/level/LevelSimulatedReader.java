package net.minecraft.world.level;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;

public interface LevelSimulatedReader {
  boolean isStateAtPosition(BlockPos paramBlockPos, Predicate<BlockState> paramPredicate);
  
  boolean isFluidAtPosition(BlockPos paramBlockPos, Predicate<FluidState> paramPredicate);
  
  <T extends net.minecraft.world.level.block.entity.BlockEntity> Optional<T> getBlockEntity(BlockPos paramBlockPos, BlockEntityType<T> paramBlockEntityType);
  
  BlockPos getHeightmapPos(Heightmap.Types paramTypes, BlockPos paramBlockPos);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LevelSimulatedReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */