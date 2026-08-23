package net.minecraft.world.entity.ai.behavior.declarative;

import net.minecraft.server.level.ServerLevel;

public interface Trigger<E extends net.minecraft.world.entity.LivingEntity> {
  boolean trigger(ServerLevel paramServerLevel, E paramE, long paramLong);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\declarative\Trigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */