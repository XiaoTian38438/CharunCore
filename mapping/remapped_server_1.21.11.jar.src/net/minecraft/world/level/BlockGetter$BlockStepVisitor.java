package net.minecraft.world.level;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface BlockStepVisitor {
  boolean visit(BlockPos paramBlockPos, int paramInt);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\BlockGetter$BlockStepVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */