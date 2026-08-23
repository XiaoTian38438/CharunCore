package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
interface BlockEntitySupplier<T extends BlockEntity> {
  T create(BlockPos paramBlockPos, BlockState paramBlockState);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BlockEntityType$BlockEntitySupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */