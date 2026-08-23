package net.minecraft.server.packs.repository;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;

public interface ResourcesSupplier {
  PackResources openPrimary(PackLocationInfo paramPackLocationInfo);
  
  PackResources openFull(PackLocationInfo paramPackLocationInfo, Pack.Metadata paramMetadata);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\repository\Pack$ResourcesSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */