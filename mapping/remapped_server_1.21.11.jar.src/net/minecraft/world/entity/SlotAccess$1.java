/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
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
/*    */ class null
/*    */   implements SlotAccess
/*    */ {
/*    */   public ItemStack get() {
/* 19 */     return getter.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean set(ItemStack paramItemStack) {
/* 24 */     setter.accept(paramItemStack);
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SlotAccess$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */