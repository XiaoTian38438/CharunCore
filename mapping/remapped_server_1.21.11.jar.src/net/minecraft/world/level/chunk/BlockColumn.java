package net.minecraft.world.level.chunk;

import net.minecraft.world.level.block.state.BlockState;

public interface BlockColumn {
  BlockState getBlock(int paramInt);
  
  void setBlock(int paramInt, BlockState paramBlockState);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\BlockColumn.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */