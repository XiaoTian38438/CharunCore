package net.minecraft.world.item.enchantment.effects;

import net.minecraft.util.RandomSource;

@FunctionalInterface
interface CoordinateSource {
  double getCoordinate(double paramDouble1, double paramDouble2, float paramFloat, RandomSource paramRandomSource);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSourceType$CoordinateSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */