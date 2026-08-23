package net.minecraft.server;

import net.minecraft.server.dedicated.DedicatedServerProperties;

public interface ServerInterface extends ServerInfo {
  DedicatedServerProperties getProperties();
  
  String getServerIp();
  
  int getServerPort();
  
  String getServerName();
  
  String[] getPlayerNames();
  
  String getLevelIdName();
  
  String getPluginNames();
  
  String runCommand(String paramString);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ServerInterface.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */