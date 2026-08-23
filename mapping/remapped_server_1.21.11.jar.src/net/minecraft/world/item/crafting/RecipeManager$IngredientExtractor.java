package net.minecraft.world.item.crafting;

import java.util.Optional;

@FunctionalInterface
public interface IngredientExtractor {
  Optional<Ingredient> apply(Recipe<?> paramRecipe);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeManager$IngredientExtractor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */