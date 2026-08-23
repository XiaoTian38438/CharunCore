package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;

@FunctionalInterface
public interface ResultConsumer<S> {
  void onCommandComplete(CommandContext<S> paramCommandContext, boolean paramBoolean, int paramInt);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\ResultConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */