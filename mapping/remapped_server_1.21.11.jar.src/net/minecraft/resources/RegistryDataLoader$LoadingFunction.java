package net.minecraft.resources;

@FunctionalInterface
interface LoadingFunction {
  void apply(RegistryDataLoader.Loader<?> paramLoader, RegistryOps.RegistryInfoLookup paramRegistryInfoLookup);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\resources\RegistryDataLoader$LoadingFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */