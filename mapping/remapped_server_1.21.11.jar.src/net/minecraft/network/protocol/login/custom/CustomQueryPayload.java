package net.minecraft.network.protocol.login.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

public interface CustomQueryPayload {
  Identifier id();
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\login\custom\CustomQueryPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */