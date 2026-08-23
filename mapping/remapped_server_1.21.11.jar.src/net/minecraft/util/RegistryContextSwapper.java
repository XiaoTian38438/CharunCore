package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;

public interface RegistryContextSwapper {
  <T> DataResult<T> swapTo(Codec<T> paramCodec, T paramT, HolderLookup.Provider paramProvider);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\RegistryContextSwapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */