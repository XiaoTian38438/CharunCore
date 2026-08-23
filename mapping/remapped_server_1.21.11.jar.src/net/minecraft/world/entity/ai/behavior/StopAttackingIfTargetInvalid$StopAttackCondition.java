package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface StopAttackCondition {
  boolean test(ServerLevel paramServerLevel, LivingEntity paramLivingEntity);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StopAttackingIfTargetInvalid$StopAttackCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */