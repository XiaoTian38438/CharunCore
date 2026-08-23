package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
interface CodecGetter {
  <T> StreamCodec<? super RegistryFriendlyByteBuf, T> apply(DataComponentType<T> paramDataComponentType);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentPatch$CodecGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */