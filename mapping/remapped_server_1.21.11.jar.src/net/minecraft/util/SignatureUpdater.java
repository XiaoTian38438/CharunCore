package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface SignatureUpdater {
  void update(Output paramOutput) throws SignatureException;
  
  @FunctionalInterface
  public static interface Output {
    void update(byte[] param1ArrayOfbyte) throws SignatureException;
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SignatureUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */