package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.numbers.NumberFormat;

@FunctionalInterface
public interface NumberFormatCommandExecutor {
  int run(CommandContext<CommandSourceStack> paramCommandContext, NumberFormat paramNumberFormat) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ScoreboardCommand$NumberFormatCommandExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */