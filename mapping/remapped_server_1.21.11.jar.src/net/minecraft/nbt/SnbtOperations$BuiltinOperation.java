package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import net.minecraft.util.parsing.packrat.ParseState;

public interface BuiltinOperation {
  <T> T run(DynamicOps<T> paramDynamicOps, List<T> paramList, ParseState<StringReader> paramParseState);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\nbt\SnbtOperations$BuiltinOperation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */