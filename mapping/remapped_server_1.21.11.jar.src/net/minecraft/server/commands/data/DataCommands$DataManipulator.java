package net.minecraft.server.commands.data;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@FunctionalInterface
interface DataManipulator {
  int modify(CommandContext<CommandSourceStack> paramCommandContext, CompoundTag paramCompoundTag, NbtPathArgument.NbtPath paramNbtPath, List<Tag> paramList) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\DataCommands$DataManipulator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */