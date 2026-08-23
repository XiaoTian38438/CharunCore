package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.players.NameAndId;

@FunctionalInterface
public interface Result {
  Collection<NameAndId> getNames(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\GameProfileArgument$Result.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */