package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.ScoreAccess;

@FunctionalInterface
public interface Operation {
  void apply(ScoreAccess paramScoreAccess1, ScoreAccess paramScoreAccess2) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\OperationArgument$Operation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */