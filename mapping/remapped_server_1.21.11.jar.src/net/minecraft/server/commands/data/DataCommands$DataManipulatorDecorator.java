package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface DataManipulatorDecorator {
  ArgumentBuilder<CommandSourceStack, ?> create(DataCommands.DataManipulator paramDataManipulator);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\DataCommands$DataManipulatorDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */