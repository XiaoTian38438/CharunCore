package net.minecraft.data.structures;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface Filter {
  CompoundTag apply(String paramString, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\structures\SnbtToNbt$Filter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */