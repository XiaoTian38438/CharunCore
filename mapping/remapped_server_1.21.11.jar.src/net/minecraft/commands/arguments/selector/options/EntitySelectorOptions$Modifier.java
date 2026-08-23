package net.minecraft.commands.arguments.selector.options;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

@FunctionalInterface
public interface Modifier {
  void handle(EntitySelectorParser paramEntitySelectorParser) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\selector\options\EntitySelectorOptions$Modifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */