package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamEncoder<O, T> {
  void encode(O paramO, T paramT);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\codec\StreamEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */