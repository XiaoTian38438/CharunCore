/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmokingRecipe extends AbstractCookingRecipe {
/*    */   public SmokingRecipe(String paramString, CookingBookCategory paramCookingBookCategory, Ingredient paramIngredient, ItemStack paramItemStack, float paramFloat, int paramInt) {
/*  9 */     super(paramString, paramCookingBookCategory, paramIngredient, paramItemStack, paramFloat, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item furnaceIcon() {
/* 14 */     return Items.SMOKER;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<SmokingRecipe> getType() {
/* 19 */     return RecipeType.SMOKING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<SmokingRecipe> getSerializer() {
/* 24 */     return RecipeSerializer.SMOKING_RECIPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     return RecipeBookCategories.SMOKER_FOOD;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\SmokingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */