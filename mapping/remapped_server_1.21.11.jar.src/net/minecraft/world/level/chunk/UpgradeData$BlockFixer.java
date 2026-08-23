package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockFixer {
  BlockState updateShape(BlockState paramBlockState1, Direction paramDirection, BlockState paramBlockState2, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos1, BlockPos paramBlockPos2);
  
  default void processChunk(LevelAccessor paramLevelAccessor) {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */