package net.minecraft.server.network;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

@FunctionalInterface
interface JoinOrLeaveEncoder {
  JsonObject encode(GameProfile paramGameProfile);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\LegacyTextFilter$JoinOrLeaveEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */