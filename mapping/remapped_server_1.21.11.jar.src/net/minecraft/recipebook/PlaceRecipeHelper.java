/*    */ package net.minecraft.recipebook;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.ShapedRecipe;
/*    */ 
/*    */ public interface PlaceRecipeHelper
/*    */ {
/*    */   static <T> void placeRecipe(int paramInt1, int paramInt2, Recipe<?> paramRecipe, Iterable<T> paramIterable, Output<T> paramOutput) {
/* 11 */     if (paramRecipe instanceof ShapedRecipe) { ShapedRecipe shapedRecipe = (ShapedRecipe)paramRecipe;
/* 12 */       placeRecipe(paramInt1, paramInt2, shapedRecipe.getWidth(), shapedRecipe.getHeight(), paramIterable, paramOutput); }
/*    */     else
/* 14 */     { placeRecipe(paramInt1, paramInt2, paramInt1, paramInt2, paramIterable, paramOutput); }
/*    */   
/*    */   }
/*    */   
/*    */   static <T> void placeRecipe(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Iterable<T> paramIterable, Output<T> paramOutput) {
/* 19 */     Iterator<T> iterator = paramIterable.iterator();
/*    */     
/* 21 */     int i = 0;
/* 22 */     for (byte b = 0; b < paramInt2; b++) {
/* 23 */       boolean bool = (paramInt4 < paramInt2 / 2.0F) ? true : false;
/* 24 */       int j = Mth.floor(paramInt2 / 2.0F - paramInt4 / 2.0F);
/*    */       
/* 26 */       if (bool && j > b) {
/* 27 */         i += paramInt1;
/* 28 */         b++;
/*    */       } 
/*    */       
/* 31 */       for (byte b1 = 0; b1 < paramInt1; b1++) {
/* 32 */         if (!iterator.hasNext()) {
/*    */           return;
/*    */         }
/*    */         
/* 36 */         bool = (paramInt3 < paramInt1 / 2.0F) ? true : false;
/* 37 */         j = Mth.floor(paramInt1 / 2.0F - paramInt3 / 2.0F);
/* 38 */         int k = paramInt3;
/* 39 */         boolean bool1 = (b1 < paramInt3) ? true : false;
/* 40 */         if (bool) {
/* 41 */           k = j + paramInt3;
/* 42 */           bool1 = (j <= b1 && b1 < j + paramInt3) ? true : false;
/*    */         } 
/*    */ 
/*    */         
/* 46 */         if (bool1) {
/* 47 */           paramOutput.addItemToSlot(iterator.next(), i, b1, b);
/* 48 */         } else if (k == b1) {
/* 49 */           i += paramInt1 - b1;
/*    */           
/*    */           break;
/*    */         } 
/* 53 */         i++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Output<T> {
/*    */     void addItemToSlot(T param1T, int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\recipebook\PlaceRecipeHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */