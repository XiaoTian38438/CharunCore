package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

public interface LevelTickAccess<T> extends TickAccess<T> {
  boolean willTickThisTick(BlockPos paramBlockPos, T paramT);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\LevelTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */