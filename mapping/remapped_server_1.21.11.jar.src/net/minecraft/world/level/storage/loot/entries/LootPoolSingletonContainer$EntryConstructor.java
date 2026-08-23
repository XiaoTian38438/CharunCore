package net.minecraft.world.level.storage.loot.entries;

import java.util.List;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@FunctionalInterface
public interface EntryConstructor {
  LootPoolSingletonContainer build(int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$EntryConstructor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */