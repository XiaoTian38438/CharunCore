package net.minecraft.world.level.timers;

import com.mojang.serialization.MapCodec;

public interface TimerCallback<T> {
  void handle(T paramT, TimerQueue<T> paramTimerQueue, long paramLong);
  
  MapCodec<? extends TimerCallback<T>> codec();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\timers\TimerCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */