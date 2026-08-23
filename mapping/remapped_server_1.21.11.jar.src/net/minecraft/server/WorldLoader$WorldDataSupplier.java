package net.minecraft.server;

@FunctionalInterface
public interface WorldDataSupplier<D> {
  WorldLoader.DataLoadOutput<D> get(WorldLoader.DataLoadContext paramDataLoadContext);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\WorldLoader$WorldDataSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */