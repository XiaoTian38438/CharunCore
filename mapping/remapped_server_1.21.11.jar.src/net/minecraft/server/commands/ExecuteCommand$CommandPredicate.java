package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface CommandPredicate {
  boolean test(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ExecuteCommand$CommandPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */