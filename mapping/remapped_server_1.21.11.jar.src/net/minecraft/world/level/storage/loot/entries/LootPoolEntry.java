package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface LootPoolEntry {
  int getWeight(float paramFloat);
  
  void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */