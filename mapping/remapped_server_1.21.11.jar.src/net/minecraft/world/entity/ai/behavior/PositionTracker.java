package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface PositionTracker {
  Vec3 currentPosition();
  
  BlockPos currentBlockPosition();
  
  boolean isVisibleBy(LivingEntity paramLivingEntity);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\PositionTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */