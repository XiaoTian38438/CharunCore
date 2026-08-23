package net.minecraft.commands.arguments;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;

public interface Result<T, O> {
  Holder<T> parse(ImmutableStringReader paramImmutableStringReader, HolderLookup.Provider paramProvider, DynamicOps<O> paramDynamicOps, Codec<T> paramCodec, HolderLookup.RegistryLookup<T> paramRegistryLookup) throws CommandSyntaxException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$Result.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */