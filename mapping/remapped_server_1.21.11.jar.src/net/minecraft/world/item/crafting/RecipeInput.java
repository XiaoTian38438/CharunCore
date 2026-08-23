/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface RecipeInput {
/*    */   ItemStack getItem(int paramInt);
/*    */   
/*    */   int size();
/*    */   
/*    */   default boolean isEmpty() {
/* 11 */     for (byte b = 0; b < size(); b++) {
/* 12 */       if (!getItem(b).isEmpty()) {
/* 13 */         return false;
/*    */       }
/*    */     } 
/* 16 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\crafting\RecipeInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */