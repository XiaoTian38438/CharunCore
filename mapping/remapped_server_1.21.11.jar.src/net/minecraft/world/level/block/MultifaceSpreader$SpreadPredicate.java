package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface SpreadPredicate {
  boolean test(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */