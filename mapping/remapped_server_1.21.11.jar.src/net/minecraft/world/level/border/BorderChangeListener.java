package net.minecraft.world.level.border;

public interface BorderChangeListener {
  void onSetSize(WorldBorder paramWorldBorder, double paramDouble);
  
  void onLerpSize(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2, long paramLong1, long paramLong2);
  
  void onSetCenter(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2);
  
  void onSetWarningTime(WorldBorder paramWorldBorder, int paramInt);
  
  void onSetWarningBlocks(WorldBorder paramWorldBorder, int paramInt);
  
  void onSetDamagePerBlock(WorldBorder paramWorldBorder, double paramDouble);
  
  void onSetSafeZone(WorldBorder paramWorldBorder, double paramDouble);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\border\BorderChangeListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */