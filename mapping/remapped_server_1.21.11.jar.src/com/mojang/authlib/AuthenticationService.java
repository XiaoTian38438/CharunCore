package com.mojang.authlib;

import com.mojang.authlib.minecraft.MinecraftSessionService;

public interface AuthenticationService {
  MinecraftSessionService createMinecraftSessionService();
  
  GameProfileRepository createProfileRepository();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\AuthenticationService.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */