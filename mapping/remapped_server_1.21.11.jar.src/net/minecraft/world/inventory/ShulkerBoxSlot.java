/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ShulkerBoxSlot extends Slot {
/*    */   public ShulkerBoxSlot(Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/*  8 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 13 */     return paramItemStack.getItem().canFitInsideContainerItems();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ShulkerBoxSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */