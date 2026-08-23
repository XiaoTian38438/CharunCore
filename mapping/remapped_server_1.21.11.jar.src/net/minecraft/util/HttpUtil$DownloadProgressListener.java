package net.minecraft.util;

import java.util.OptionalLong;

public interface DownloadProgressListener {
  void requestStart();
  
  void downloadStart(OptionalLong paramOptionalLong);
  
  void downloadedBytes(long paramLong);
  
  void requestFinished(boolean paramBoolean);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\HttpUtil$DownloadProgressListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */