package net.minecraft.world.level.storage;

public interface ValueOutputList {
  ValueOutput addChild();
  
  void discardLast();
  
  boolean isEmpty();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\ValueOutput$ValueOutputList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */