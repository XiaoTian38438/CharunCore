package net.minecraft.commands.functions;

import java.util.List;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

public interface InstantiatedFunction<T> {
  Identifier id();
  
  List<UnboundEntryAction<T>> entries();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\functions\InstantiatedFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */