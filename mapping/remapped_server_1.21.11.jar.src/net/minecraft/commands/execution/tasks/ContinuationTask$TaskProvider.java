package net.minecraft.commands.execution.tasks;

import net.minecraft.commands.execution.CommandQueueEntry;
import net.minecraft.commands.execution.Frame;

@FunctionalInterface
public interface TaskProvider<T, P> {
  CommandQueueEntry<T> create(Frame paramFrame, P paramP);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\tasks\ContinuationTask$TaskProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */