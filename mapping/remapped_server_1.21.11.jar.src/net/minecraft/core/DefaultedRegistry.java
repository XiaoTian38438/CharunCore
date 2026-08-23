package net.minecraft.core;

import net.minecraft.resources.Identifier;

public interface DefaultedRegistry<T> extends Registry<T> {
  Identifier getKey(T paramT);
  
  T getValue(Identifier paramIdentifier);
  
  T byId(int paramInt);
  
  Identifier getDefaultKey();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\DefaultedRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */