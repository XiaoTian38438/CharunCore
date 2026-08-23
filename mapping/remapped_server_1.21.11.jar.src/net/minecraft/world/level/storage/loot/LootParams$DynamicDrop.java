package net.minecraft.world.level.storage.loot;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface DynamicDrop {
  void add(Consumer<ItemStack> paramConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\LootParams$DynamicDrop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */