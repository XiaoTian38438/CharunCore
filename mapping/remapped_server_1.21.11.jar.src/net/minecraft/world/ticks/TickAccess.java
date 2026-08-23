package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

public interface TickAccess<T> {
  void schedule(ScheduledTick<T> paramScheduledTick);
  
  boolean hasScheduledTick(BlockPos paramBlockPos, T paramT);
  
  int count();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\TickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */