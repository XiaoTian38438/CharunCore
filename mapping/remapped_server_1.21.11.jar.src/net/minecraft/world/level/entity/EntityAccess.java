package net.minecraft.world.level.entity;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public interface EntityAccess extends UniquelyIdentifyable {
  int getId();
  
  BlockPos blockPosition();
  
  AABB getBoundingBox();
  
  void setLevelCallback(EntityInLevelCallback paramEntityInLevelCallback);
  
  Stream<? extends EntityAccess> getSelfAndPassengers();
  
  Stream<? extends EntityAccess> getPassengersAndSelf();
  
  void setRemoved(Entity.RemovalReason paramRemovalReason);
  
  boolean shouldBeSaved();
  
  boolean isAlwaysTicking();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */