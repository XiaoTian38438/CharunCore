package net.minecraft.network.protocol;

import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
public interface CodecModifier<B, V, C> {
  StreamCodec<? super B, V> apply(StreamCodec<? super B, V> paramStreamCodec, C paramC);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\protocol\CodecModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */