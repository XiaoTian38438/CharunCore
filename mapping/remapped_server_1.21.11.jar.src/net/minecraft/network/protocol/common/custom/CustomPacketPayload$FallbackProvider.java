package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public interface FallbackProvider<B extends net.minecraft.network.FriendlyByteBuf> {
  StreamCodec<B, ? extends CustomPacketPayload> create(Identifier paramIdentifier);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\common\custom\CustomPacketPayload$FallbackProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */