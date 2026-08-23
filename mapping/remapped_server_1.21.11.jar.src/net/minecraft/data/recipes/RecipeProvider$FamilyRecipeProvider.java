package net.minecraft.data.recipes;

import net.minecraft.world.level.ItemLike;

@FunctionalInterface
interface FamilyRecipeProvider {
  RecipeBuilder create(RecipeProvider paramRecipeProvider, ItemLike paramItemLike1, ItemLike paramItemLike2);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\recipes\RecipeProvider$FamilyRecipeProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */