package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;

public interface CachedCheck<I extends RecipeInput, T extends Recipe<I>> {
  Optional<RecipeHolder<T>> getRecipeFor(I paramI, ServerLevel paramServerLevel);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeManager$CachedCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */