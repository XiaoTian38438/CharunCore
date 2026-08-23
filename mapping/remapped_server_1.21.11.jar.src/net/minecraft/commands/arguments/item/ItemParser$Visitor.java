package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

public interface Visitor {
  default void visitItem(Holder<Item> paramHolder) {}
  
  default <T> void visitComponent(DataComponentType<T> paramDataComponentType, T paramT) {}
  
  default <T> void visitRemovedComponent(DataComponentType<T> paramDataComponentType) {}
  
  default void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> paramFunction) {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\arguments\item\ItemParser$Visitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */