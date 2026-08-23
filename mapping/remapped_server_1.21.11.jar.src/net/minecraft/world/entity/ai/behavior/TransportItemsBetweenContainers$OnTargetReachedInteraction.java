package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.PathfinderMob;
import org.apache.commons.lang3.function.TriConsumer;

@FunctionalInterface
public interface OnTargetReachedInteraction extends TriConsumer<PathfinderMob, TransportItemsBetweenContainers.TransportItemTarget, Integer> {}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\TransportItemsBetweenContainers$OnTargetReachedInteraction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */