package net.minecraft.commands.execution;

import net.minecraft.resources.Identifier;

public interface TraceCallbacks extends AutoCloseable {
  void onCommand(int paramInt, String paramString);
  
  void onReturn(int paramInt1, String paramString, int paramInt2);
  
  void onError(String paramString);
  
  void onCall(int paramInt1, Identifier paramIdentifier, int paramInt2);
  
  void close();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\TraceCallbacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */