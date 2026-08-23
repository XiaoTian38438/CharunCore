package com.mojang.authlib;

import com.mojang.authlib.yggdrasil.response.NameAndId;
import java.util.Optional;

public interface GameProfileRepository {
  void findProfilesByNames(String[] paramArrayOfString, ProfileLookupCallback paramProfileLookupCallback);
  
  Optional<NameAndId> findProfileByName(String paramString);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\GameProfileRepository.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */