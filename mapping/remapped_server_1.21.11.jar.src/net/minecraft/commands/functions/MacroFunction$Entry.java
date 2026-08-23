package net.minecraft.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

interface Entry<T> {
  IntList parameters();
  
  UnboundEntryAction<T> instantiate(List<String> paramList, CommandDispatcher<T> paramCommandDispatcher, Identifier paramIdentifier) throws FunctionInstantiationException;
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\functions\MacroFunction$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */