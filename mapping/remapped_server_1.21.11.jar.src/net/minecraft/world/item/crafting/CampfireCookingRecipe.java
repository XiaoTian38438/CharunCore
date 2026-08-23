/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class CampfireCookingRecipe extends AbstractCookingRecipe {
/*    */   public CampfireCookingRecipe(String paramString, CookingBookCategory paramCookingBookCategory, Ingredient paramIngredient, ItemStack paramItemStack, float paramFloat, int paramInt) {
/*  9 */     super(paramString, paramCookingBookCategory, paramIngredient, paramItemStack, paramFloat, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.CAMPFIRE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<CampfireCookingRecipe> getSerializer() {
/* 19 */     return RecipeSerializer.CAMPFIRE_COOKING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<CampfireCookingRecipe> getType() {
/* 24 */     return RecipeType.CAMPFIRE_COOKING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     return RecipeBookCategories.CAMPFIRE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\CampfireCookingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */