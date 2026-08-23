package net.minecraft.world.item.equipment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface AllowedEntitiesProvider {
  HolderSet<EntityType<?>> get(HolderGetter<EntityType<?>> paramHolderGetter);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\equipment\AllowedEntitiesProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */