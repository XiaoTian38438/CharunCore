package net.minecraft.core;

import net.minecraft.resources.ResourceKey;

public interface PendingTags<T> {
  ResourceKey<? extends Registry<? extends T>> key();
  
  HolderLookup.RegistryLookup<T> lookup();
  
  void apply();
  
  int size();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\Registry$PendingTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */