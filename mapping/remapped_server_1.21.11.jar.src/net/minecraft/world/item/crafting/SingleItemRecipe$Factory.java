package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface Factory<T extends SingleItemRecipe> {
  T create(String paramString, Ingredient paramIngredient, ItemStack paramItemStack);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\SingleItemRecipe$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */