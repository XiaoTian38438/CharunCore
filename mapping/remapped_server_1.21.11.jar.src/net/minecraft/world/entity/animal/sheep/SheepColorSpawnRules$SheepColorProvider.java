package net.minecraft.world.entity.animal.sheep;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;

@FunctionalInterface
interface SheepColorProvider {
  DyeColor get(RandomSource paramRandomSource);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\sheep\SheepColorSpawnRules$SheepColorProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */