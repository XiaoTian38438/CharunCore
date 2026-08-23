/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface CraftingRecipe
/*    */   extends Recipe<CraftingInput> {
/*    */   default RecipeType<CraftingRecipe> getType() {
/* 10 */     return RecipeType.CRAFTING;
/*    */   }
/*    */ 
/*    */   
/*    */   RecipeSerializer<? extends CraftingRecipe> getSerializer();
/*    */ 
/*    */   
/*    */   CraftingBookCategory category();
/*    */   
/*    */   default NonNullList<ItemStack> getRemainingItems(CraftingInput paramCraftingInput) {
/* 20 */     return defaultCraftingReminder(paramCraftingInput);
/*    */   }
/*    */   
/*    */   static NonNullList<ItemStack> defaultCraftingReminder(CraftingInput paramCraftingInput) {
/* 24 */     NonNullList<ItemStack> nonNullList = NonNullList.withSize(paramCraftingInput.size(), ItemStack.EMPTY);
/*    */     
/* 26 */     for (byte b = 0; b < nonNullList.size(); b++) {
/* 27 */       Item item = paramCraftingInput.getItem(b).getItem();
/* 28 */       nonNullList.set(b, item.getCraftingRemainder());
/*    */     } 
/*    */     
/* 31 */     return nonNullList;
/*    */   }
/*    */ 
/*    */   
/*    */   default RecipeBookCategory recipeBookCategory() {
/* 36 */     switch (category()) { default: throw new MatchException(null, null);case BUILDING: case EQUIPMENT: case REDSTONE: case MISC: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 40 */       RecipeBookCategories.CRAFTING_MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\CraftingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */