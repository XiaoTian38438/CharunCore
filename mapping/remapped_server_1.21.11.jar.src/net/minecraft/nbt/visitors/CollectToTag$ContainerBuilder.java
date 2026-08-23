package net.minecraft.nbt.visitors;

import net.minecraft.nbt.Tag;

interface ContainerBuilder {
  default void acceptKey(String paramString) {}
  
  void acceptValue(Tag paramTag);
  
  Tag build();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\visitors\CollectToTag$ContainerBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */