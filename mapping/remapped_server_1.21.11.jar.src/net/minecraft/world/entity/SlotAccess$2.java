/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ class null
/*    */   implements SlotAccess
/*    */ {
/*    */   public ItemStack get() {
/* 34 */     return entity.getItemBySlot(slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean set(ItemStack paramItemStack) {
/* 39 */     if (!validator.test(paramItemStack)) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     entity.setItemSlot(slot, paramItemStack);
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SlotAccess$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */