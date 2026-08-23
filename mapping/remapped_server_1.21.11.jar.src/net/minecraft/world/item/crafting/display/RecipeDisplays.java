/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class RecipeDisplays {
/*    */   public static RecipeDisplay.Type<?> bootstrap(Registry<RecipeDisplay.Type<?>> paramRegistry) {
/*  7 */     Registry.register(paramRegistry, "crafting_shapeless", ShapelessCraftingRecipeDisplay.TYPE);
/*  8 */     Registry.register(paramRegistry, "crafting_shaped", ShapedCraftingRecipeDisplay.TYPE);
/*  9 */     Registry.register(paramRegistry, "furnace", FurnaceRecipeDisplay.TYPE);
/* 10 */     Registry.register(paramRegistry, "stonecutter", StonecutterRecipeDisplay.TYPE);
/* 11 */     return (RecipeDisplay.Type)Registry.register(paramRegistry, "smithing", SmithingRecipeDisplay.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\display\RecipeDisplays.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */