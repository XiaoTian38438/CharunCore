package net.minecraft.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface NumberFormatType<T extends NumberFormat> {
  MapCodec<T> mapCodec();
  
  StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\numbers\NumberFormatType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */