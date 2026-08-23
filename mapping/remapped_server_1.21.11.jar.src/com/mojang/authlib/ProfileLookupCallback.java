package com.mojang.authlib;

import java.util.UUID;

public interface ProfileLookupCallback {
  void onProfileLookupSucceeded(String paramString, UUID paramUUID);
  
  void onProfileLookupFailed(String paramString, Exception paramException);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\ProfileLookupCallback.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */