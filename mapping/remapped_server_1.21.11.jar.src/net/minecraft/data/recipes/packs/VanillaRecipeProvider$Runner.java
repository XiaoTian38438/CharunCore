/*    */ package net.minecraft.data.recipes.packs;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.data.recipes.RecipeOutput;
/*    */ import net.minecraft.data.recipes.RecipeProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Runner
/*    */   extends RecipeProvider.Runner
/*    */ {
/*    */   public Runner(PackOutput paramPackOutput, CompletableFuture<HolderLookup.Provider> paramCompletableFuture) {
/* 75 */     super(paramPackOutput, paramCompletableFuture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RecipeProvider createRecipeProvider(HolderLookup.Provider paramProvider, RecipeOutput paramRecipeOutput) {
/* 80 */     return new VanillaRecipeProvider(paramProvider, paramRecipeOutput);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 85 */     return "Vanilla Recipes";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\recipes\packs\VanillaRecipeProvider$Runner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */