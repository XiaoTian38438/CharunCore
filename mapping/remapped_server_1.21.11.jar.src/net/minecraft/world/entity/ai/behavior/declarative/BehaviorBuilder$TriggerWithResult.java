package net.minecraft.world.entity.ai.behavior.declarative;

import net.minecraft.server.level.ServerLevel;

interface TriggerWithResult<E extends net.minecraft.world.entity.LivingEntity, R> {
  R tryTrigger(ServerLevel paramServerLevel, E paramE, long paramLong);
  
  String debugString();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$TriggerWithResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */