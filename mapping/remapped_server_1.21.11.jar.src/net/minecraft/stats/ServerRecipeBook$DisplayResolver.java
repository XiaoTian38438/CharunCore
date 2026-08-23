package net.minecraft.stats;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

@FunctionalInterface
public interface DisplayResolver {
  void displaysForRecipe(ResourceKey<Recipe<?>> paramResourceKey, Consumer<RecipeDisplayEntry> paramConsumer);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\stats\ServerRecipeBook$DisplayResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */