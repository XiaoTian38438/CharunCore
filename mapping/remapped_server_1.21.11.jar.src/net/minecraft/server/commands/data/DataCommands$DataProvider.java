package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;

public interface DataProvider {
  DataAccessor access(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
  
  ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> paramFunction);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\DataCommands$DataProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */