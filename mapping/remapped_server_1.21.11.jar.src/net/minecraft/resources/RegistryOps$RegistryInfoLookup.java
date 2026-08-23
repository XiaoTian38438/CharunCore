package net.minecraft.resources;

import java.util.Optional;
import net.minecraft.core.Registry;

public interface RegistryInfoLookup {
  <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\resources\RegistryOps$RegistryInfoLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */