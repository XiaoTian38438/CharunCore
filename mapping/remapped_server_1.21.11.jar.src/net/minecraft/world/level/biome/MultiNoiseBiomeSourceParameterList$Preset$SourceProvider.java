package net.minecraft.world.level.biome;

import java.util.function.Function;
import net.minecraft.resources.ResourceKey;

@FunctionalInterface
interface SourceProvider {
  <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> paramFunction);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList$Preset$SourceProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */