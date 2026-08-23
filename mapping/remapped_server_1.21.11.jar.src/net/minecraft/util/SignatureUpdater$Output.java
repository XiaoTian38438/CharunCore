package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface Output {
  void update(byte[] paramArrayOfbyte) throws SignatureException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SignatureUpdater$Output.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */