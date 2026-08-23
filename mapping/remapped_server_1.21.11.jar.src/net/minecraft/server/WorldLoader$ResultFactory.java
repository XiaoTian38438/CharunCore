package net.minecraft.server;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.packs.resources.CloseableResourceManager;

@FunctionalInterface
public interface ResultFactory<D, R> {
  R create(CloseableResourceManager paramCloseableResourceManager, ReloadableServerResources paramReloadableServerResources, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, D paramD);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\WorldLoader$ResultFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */