package net.minecraft.recipebook;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.RecipeHolder;

public interface CraftingMenuAccess<T extends net.minecraft.world.item.crafting.Recipe<?>> {
  void fillCraftSlotsStackedContents(StackedItemContents paramStackedItemContents);
  
  void clearCraftingContent();
  
  boolean recipeMatches(RecipeHolder<T> paramRecipeHolder);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\recipebook\ServerPlaceRecipe$CraftingMenuAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */