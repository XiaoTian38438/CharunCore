package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceKey;

public interface RecipeAccess {
  RecipePropertySet propertySet(ResourceKey<RecipePropertySet> paramResourceKey);
  
  SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */