package net.minecraft.core;

import net.minecraft.data.worldgen.BootstrapContext;

@FunctionalInterface
public interface RegistryBootstrap<T> {
  void run(BootstrapContext<T> paramBootstrapContext);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistrySetBuilder$RegistryBootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */