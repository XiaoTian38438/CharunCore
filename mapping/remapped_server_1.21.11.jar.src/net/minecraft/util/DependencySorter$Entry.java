package net.minecraft.util;

import java.util.function.Consumer;

public interface Entry<K> {
  void visitRequiredDependencies(Consumer<K> paramConsumer);
  
  void visitOptionalDependencies(Consumer<K> paramConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\DependencySorter$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */