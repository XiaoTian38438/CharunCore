package net.minecraft.server.advancements;

import net.minecraft.advancements.AdvancementNode;

@FunctionalInterface
public interface Output {
  void accept(AdvancementNode paramAdvancementNode, boolean paramBoolean);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\advancements\AdvancementVisibilityEvaluator$Output.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */