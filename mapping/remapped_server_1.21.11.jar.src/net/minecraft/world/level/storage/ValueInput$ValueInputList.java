package net.minecraft.world.level.storage;

import java.util.stream.Stream;

public interface ValueInputList extends Iterable<ValueInput> {
  boolean isEmpty();
  
  Stream<ValueInput> stream();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\ValueInput$ValueInputList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */