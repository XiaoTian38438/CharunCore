/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CrafterSlot
/*    */   extends Slot {
/*    */   private final CrafterMenu menu;
/*    */   
/*    */   public CrafterSlot(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, CrafterMenu paramCrafterMenu) {
/* 11 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/* 12 */     this.menu = paramCrafterMenu;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 17 */     return (!this.menu.isSlotDisabled(this.index) && super.mayPlace(paramItemStack));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setChanged() {
/* 22 */     super.setChanged();
/* 23 */     this.menu.slotsChanged(this.container);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\CrafterSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */