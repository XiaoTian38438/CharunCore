package com.mojang.serialization;

public interface Compressable extends Keyable {
  <T> KeyCompressor<T> compressor(DynamicOps<T> paramDynamicOps);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\Compressable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */