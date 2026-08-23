package net.minecraft.world.item.enchantment;

import net.minecraft.core.Holder;

@FunctionalInterface
interface EnchantmentInSlotVisitor {
  void accept(Holder<Enchantment> paramHolder, int paramInt, EnchantedItemInUse paramEnchantedItemInUse);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper$EnchantmentInSlotVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */