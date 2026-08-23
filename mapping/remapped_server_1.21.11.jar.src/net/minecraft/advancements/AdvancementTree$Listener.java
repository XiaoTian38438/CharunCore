package net.minecraft.advancements;

public interface Listener {
  void onAddAdvancementRoot(AdvancementNode paramAdvancementNode);
  
  void onRemoveAdvancementRoot(AdvancementNode paramAdvancementNode);
  
  void onAddAdvancementTask(AdvancementNode paramAdvancementNode);
  
  void onRemoveAdvancementTask(AdvancementNode paramAdvancementNode);
  
  void onAdvancementsCleared();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\AdvancementTree$Listener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */