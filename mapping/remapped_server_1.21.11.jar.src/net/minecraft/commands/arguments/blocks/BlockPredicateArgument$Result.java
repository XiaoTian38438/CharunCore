package net.minecraft.commands.arguments.blocks;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public interface Result extends Predicate<BlockInWorld> {
  boolean requiresNbt();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument$Result.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */