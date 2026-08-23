package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamMemberEncoder<O, T> {
  void encode(T paramT, O paramO);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\codec\StreamMemberEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */