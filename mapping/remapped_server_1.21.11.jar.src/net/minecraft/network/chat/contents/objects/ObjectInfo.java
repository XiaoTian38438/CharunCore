package net.minecraft.network.chat.contents.objects;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.FontDescription;

public interface ObjectInfo {
  FontDescription fontDescription();
  
  String description();
  
  MapCodec<? extends ObjectInfo> codec();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\contents\objects\ObjectInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */