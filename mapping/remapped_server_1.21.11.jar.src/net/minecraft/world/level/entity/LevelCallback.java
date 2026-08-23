package net.minecraft.world.level.entity;

public interface LevelCallback<T> {
  void onCreated(T paramT);
  
  void onDestroyed(T paramT);
  
  void onTickingStart(T paramT);
  
  void onTickingEnd(T paramT);
  
  void onTrackingStart(T paramT);
  
  void onTrackingEnd(T paramT);
  
  void onSectionChange(T paramT);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\LevelCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */