package net.minecraft.world.level;

import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface ColorResolver {
  int getColor(Biome paramBiome, double paramDouble1, double paramDouble2);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\ColorResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */