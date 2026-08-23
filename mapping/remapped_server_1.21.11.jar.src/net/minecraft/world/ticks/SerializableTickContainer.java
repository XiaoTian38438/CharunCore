package net.minecraft.world.ticks;

import java.util.List;

public interface SerializableTickContainer<T> {
  List<SavedTick<T>> pack(long paramLong);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\SerializableTickContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */