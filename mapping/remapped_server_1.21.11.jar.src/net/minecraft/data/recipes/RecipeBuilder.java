/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.crafting.CraftingBookCategory;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface RecipeBuilder
/*    */ {
/* 15 */   public static final Identifier ROOT_RECIPE_ADVANCEMENT = Identifier.withDefaultNamespace("recipes/root");
/*    */   
/*    */   RecipeBuilder unlockedBy(String paramString, Criterion<?> paramCriterion);
/*    */   
/*    */   RecipeBuilder group(String paramString);
/*    */   
/*    */   Item getResult();
/*    */   
/*    */   void save(RecipeOutput paramRecipeOutput, ResourceKey<Recipe<?>> paramResourceKey);
/*    */   
/*    */   default void save(RecipeOutput paramRecipeOutput) {
/* 26 */     save(paramRecipeOutput, ResourceKey.create(Registries.RECIPE, getDefaultRecipeId((ItemLike)getResult())));
/*    */   }
/*    */   
/*    */   default void save(RecipeOutput paramRecipeOutput, String paramString) {
/* 30 */     Identifier identifier1 = getDefaultRecipeId((ItemLike)getResult());
/* 31 */     Identifier identifier2 = Identifier.parse(paramString);
/* 32 */     if (identifier2.equals(identifier1)) {
/* 33 */       throw new IllegalStateException("Recipe " + paramString + " should remove its 'save' argument as it is equal to default one");
/*    */     }
/* 35 */     save(paramRecipeOutput, ResourceKey.create(Registries.RECIPE, identifier2));
/*    */   }
/*    */   
/*    */   static Identifier getDefaultRecipeId(ItemLike paramItemLike) {
/* 39 */     return BuiltInRegistries.ITEM.getKey(paramItemLike.asItem());
/*    */   }
/*    */   
/*    */   static CraftingBookCategory determineBookCategory(RecipeCategory paramRecipeCategory) {
/* 43 */     switch (paramRecipeCategory) { case BUILDING_BLOCKS: case TOOLS: case COMBAT: case REDSTONE:  }  return 
/*    */ 
/*    */ 
/*    */       
/* 47 */       CraftingBookCategory.MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\recipes\RecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */