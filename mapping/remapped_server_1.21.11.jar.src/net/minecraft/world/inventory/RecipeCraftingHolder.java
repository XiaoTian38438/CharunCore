/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ public interface RecipeCraftingHolder
/*    */ {
/*    */   void setRecipeUsed(RecipeHolder<?> paramRecipeHolder);
/*    */   
/*    */   RecipeHolder<?> getRecipeUsed();
/*    */   
/*    */   default void awardUsedRecipes(Player paramPlayer, List<ItemStack> paramList) {
/* 19 */     RecipeHolder<?> recipeHolder = getRecipeUsed();
/* 20 */     if (recipeHolder != null) {
/* 21 */       paramPlayer.triggerRecipeCrafted(recipeHolder, paramList);
/* 22 */       if (!recipeHolder.value().isSpecial()) {
/* 23 */         paramPlayer.awardRecipes(Collections.singleton(recipeHolder));
/* 24 */         setRecipeUsed(null);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   default boolean setRecipeUsed(ServerPlayer paramServerPlayer, RecipeHolder<?> paramRecipeHolder) {
/* 30 */     if (paramRecipeHolder.value().isSpecial() || !((Boolean)paramServerPlayer.level().getGameRules().get(GameRules.LIMITED_CRAFTING)).booleanValue() || paramServerPlayer.getRecipeBook().contains(paramRecipeHolder.id())) {
/* 31 */       setRecipeUsed(paramRecipeHolder);
/* 32 */       return true;
/*    */     } 
/*    */     
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\RecipeCraftingHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */