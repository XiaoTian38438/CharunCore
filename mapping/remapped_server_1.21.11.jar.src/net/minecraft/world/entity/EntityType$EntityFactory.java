package net.minecraft.world.entity;

import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityFactory<T extends Entity> {
  T create(EntityType<T> paramEntityType, Level paramLevel);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\EntityType$EntityFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */