package net.minecraft.util;

import net.minecraft.network.chat.Component;

public interface ProgressListener {
  void progressStartNoAbort(Component paramComponent);
  
  void progressStart(Component paramComponent);
  
  void progressStage(Component paramComponent);
  
  void progressStagePercentage(int paramInt);
  
  void stop();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ProgressListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */