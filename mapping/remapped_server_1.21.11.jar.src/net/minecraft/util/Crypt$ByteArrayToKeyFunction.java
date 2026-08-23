package net.minecraft.util;

interface ByteArrayToKeyFunction<T extends java.security.Key> {
  T apply(byte[] paramArrayOfbyte) throws CryptException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\Crypt$ByteArrayToKeyFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */