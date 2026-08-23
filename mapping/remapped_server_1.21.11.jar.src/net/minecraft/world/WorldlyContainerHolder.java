package net.minecraft.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface WorldlyContainerHolder {
  WorldlyContainer getContainer(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\WorldlyContainerHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */