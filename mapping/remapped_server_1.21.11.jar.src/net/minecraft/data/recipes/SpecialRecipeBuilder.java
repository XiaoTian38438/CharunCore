/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.CraftingBookCategory;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class SpecialRecipeBuilder
/*    */ {
/*    */   private final Function<CraftingBookCategory, Recipe<?>> factory;
/*    */   
/*    */   public SpecialRecipeBuilder(Function<CraftingBookCategory, Recipe<?>> paramFunction) {
/* 15 */     this.factory = paramFunction;
/*    */   }
/*    */   
/*    */   public static SpecialRecipeBuilder special(Function<CraftingBookCategory, Recipe<?>> paramFunction) {
/* 19 */     return new SpecialRecipeBuilder(paramFunction);
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput paramRecipeOutput, String paramString) {
/* 23 */     save(paramRecipeOutput, ResourceKey.create(Registries.RECIPE, Identifier.parse(paramString)));
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput paramRecipeOutput, ResourceKey<Recipe<?>> paramResourceKey) {
/* 27 */     paramRecipeOutput.accept(paramResourceKey, this.factory.apply(CraftingBookCategory.MISC), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\recipes\SpecialRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */