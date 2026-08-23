package net.minecraft.data.loot;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

@FunctionalInterface
public interface LootTableSubProvider {
  void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> paramBiConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\loot\LootTableSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */