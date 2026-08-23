/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class BlastingRecipe extends AbstractCookingRecipe {
/*    */   public BlastingRecipe(String paramString, CookingBookCategory paramCookingBookCategory, Ingredient paramIngredient, ItemStack paramItemStack, float paramFloat, int paramInt) {
/*  9 */     super(paramString, paramCookingBookCategory, paramIngredient, paramItemStack, paramFloat, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.BLAST_FURNACE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<BlastingRecipe> getSerializer() {
/* 19 */     return RecipeSerializer.BLASTING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<BlastingRecipe> getType() {
/* 24 */     return RecipeType.BLASTING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     switch (category()) { default: throw new MatchException(null, null);case BLOCKS: case FOOD: case MISC: break; }  return 
/*    */       
/* 31 */       RecipeBookCategories.BLAST_FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\BlastingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */