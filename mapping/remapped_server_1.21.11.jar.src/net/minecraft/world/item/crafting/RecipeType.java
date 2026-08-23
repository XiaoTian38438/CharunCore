/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface RecipeType<T extends Recipe<?>> {
/*  8 */   public static final RecipeType<CraftingRecipe> CRAFTING = register("crafting");
/*  9 */   public static final RecipeType<SmeltingRecipe> SMELTING = register("smelting");
/* 10 */   public static final RecipeType<BlastingRecipe> BLASTING = register("blasting");
/* 11 */   public static final RecipeType<SmokingRecipe> SMOKING = register("smoking");
/* 12 */   public static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = register("campfire_cooking");
/* 13 */   public static final RecipeType<StonecutterRecipe> STONECUTTING = register("stonecutting");
/* 14 */   public static final RecipeType<SmithingRecipe> SMITHING = register("smithing");
/*    */   
/*    */   static <T extends Recipe<?>> RecipeType<T> register(final String name) {
/* 17 */     return (RecipeType<T>)Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.withDefaultNamespace(name), new RecipeType<T>()
/*    */         {
/*    */           public String toString() {
/* 20 */             return name;
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */