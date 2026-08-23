package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface StartAttackingCondition<E> {
  boolean test(ServerLevel paramServerLevel, E paramE);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking$StartAttackingCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */