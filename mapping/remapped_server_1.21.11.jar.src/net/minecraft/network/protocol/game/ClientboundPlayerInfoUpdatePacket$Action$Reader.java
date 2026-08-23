package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface Reader {
  void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder paramEntryBuilder, RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Action$Reader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */