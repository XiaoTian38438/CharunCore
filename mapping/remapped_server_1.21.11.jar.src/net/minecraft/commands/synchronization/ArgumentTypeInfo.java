package net.minecraft.commands.synchronization;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

public interface ArgumentTypeInfo<A extends com.mojang.brigadier.arguments.ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> {
  void serializeToNetwork(T paramT, FriendlyByteBuf paramFriendlyByteBuf);
  
  T deserializeFromNetwork(FriendlyByteBuf paramFriendlyByteBuf);
  
  void serializeToJson(T paramT, JsonObject paramJsonObject);
  
  T unpack(A paramA);
  
  public static interface Template<A extends com.mojang.brigadier.arguments.ArgumentType<?>> {
    A instantiate(CommandBuildContext param1CommandBuildContext);
    
    ArgumentTypeInfo<A, ?> type();
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */