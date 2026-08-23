package net.minecraft.commands.synchronization;

import net.minecraft.commands.CommandBuildContext;

public interface Template<A extends com.mojang.brigadier.arguments.ArgumentType<?>> {
  A instantiate(CommandBuildContext paramCommandBuildContext);
  
  ArgumentTypeInfo<A, ?> type();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */