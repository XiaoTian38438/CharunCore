package net.minecraft.world.level.border;

import net.minecraft.world.phys.shapes.VoxelShape;

interface BorderExtent {
  double getMinX(float paramFloat);
  
  double getMaxX(float paramFloat);
  
  double getMinZ(float paramFloat);
  
  double getMaxZ(float paramFloat);
  
  double getSize();
  
  double getLerpSpeed();
  
  long getLerpTime();
  
  double getLerpTarget();
  
  BorderStatus getStatus();
  
  void onAbsoluteMaxSizeChange();
  
  void onCenterChange();
  
  BorderExtent update();
  
  VoxelShape getCollisionShape();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\border\WorldBorder$BorderExtent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */