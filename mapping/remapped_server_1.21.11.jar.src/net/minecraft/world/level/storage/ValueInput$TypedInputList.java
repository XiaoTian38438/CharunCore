package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface TypedInputList<T> extends Iterable<T> {
  boolean isEmpty();
  
  Stream<T> stream();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\ValueInput$TypedInputList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */