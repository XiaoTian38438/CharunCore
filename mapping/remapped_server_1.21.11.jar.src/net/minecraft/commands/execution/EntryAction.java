package net.minecraft.commands.execution;

@FunctionalInterface
public interface EntryAction<T> {
  void execute(ExecutionContext<T> paramExecutionContext, Frame paramFrame);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\EntryAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */