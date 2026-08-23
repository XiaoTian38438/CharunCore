package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
interface StringProcessor {
  String process(String paramString) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\DataCommands$StringProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */