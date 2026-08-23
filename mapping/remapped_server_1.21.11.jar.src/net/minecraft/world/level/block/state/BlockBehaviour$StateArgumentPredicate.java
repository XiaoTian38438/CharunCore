package net.minecraft.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface StateArgumentPredicate<A> {
  boolean test(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, A paramA);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\BlockBehaviour$StateArgumentPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */