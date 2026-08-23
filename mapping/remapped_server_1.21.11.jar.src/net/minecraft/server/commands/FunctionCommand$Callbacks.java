package net.minecraft.server.commands;

import net.minecraft.resources.Identifier;

public interface Callbacks<T> {
  void signalResult(T paramT, Identifier paramIdentifier, int paramInt);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\FunctionCommand$Callbacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */