package net.minecraft.world.level.chunk;

import com.mojang.serialization.DataResult;

public interface Unpacker<T, C extends PalettedContainerRO<T>> {
  DataResult<C> read(Strategy<T> paramStrategy, PalettedContainerRO.PackedData<T> paramPackedData);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\PalettedContainerRO$Unpacker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */