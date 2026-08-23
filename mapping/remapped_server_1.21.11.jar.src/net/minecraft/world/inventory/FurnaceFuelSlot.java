/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class FurnaceFuelSlot extends Slot {
/*    */   private final AbstractFurnaceMenu menu;
/*    */   
/*    */   public FurnaceFuelSlot(AbstractFurnaceMenu paramAbstractFurnaceMenu, Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/* 11 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/* 12 */     this.menu = paramAbstractFurnaceMenu;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 17 */     return (this.menu.isFuel(paramItemStack) || isBucket(paramItemStack));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxStackSize(ItemStack paramItemStack) {
/* 22 */     return isBucket(paramItemStack) ? 1 : super.getMaxStackSize(paramItemStack);
/*    */   }
/*    */   
/*    */   public static boolean isBucket(ItemStack paramItemStack) {
/* 26 */     return paramItemStack.is(Items.BUCKET);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\FurnaceFuelSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */