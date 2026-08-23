/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class DispenserMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private static final int SLOT_COUNT = 9;
/*    */   private static final int INV_SLOT_START = 9;
/*    */   private static final int INV_SLOT_END = 36;
/*    */   private static final int USE_ROW_SLOT_START = 36;
/*    */   private static final int USE_ROW_SLOT_END = 45;
/*    */   private final Container dispenser;
/*    */   
/*    */   public DispenserMenu(int paramInt, Inventory paramInventory) {
/* 21 */     this(paramInt, paramInventory, (Container)new SimpleContainer(9));
/*    */   }
/*    */   
/*    */   public DispenserMenu(int paramInt, Inventory paramInventory, Container paramContainer) {
/* 25 */     super(MenuType.GENERIC_3x3, paramInt);
/* 26 */     checkContainerSize(paramContainer, 9);
/* 27 */     this.dispenser = paramContainer;
/* 28 */     paramContainer.startOpen((ContainerUser)paramInventory.player);
/*    */     
/* 30 */     add3x3GridSlots(paramContainer, 62, 17);
/*    */     
/* 32 */     addStandardInventorySlots((Container)paramInventory, 8, 84);
/*    */   }
/*    */   
/*    */   protected void add3x3GridSlots(Container paramContainer, int paramInt1, int paramInt2) {
/* 36 */     for (byte b = 0; b < 3; b++) {
/* 37 */       for (byte b1 = 0; b1 < 3; b1++) {
/* 38 */         int i = b1 + b * 3;
/* 39 */         addSlot(new Slot(paramContainer, i, paramInt1 + b1 * 18, paramInt2 + b * 18));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 46 */     return this.dispenser.stillValid(paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/* 51 */     ItemStack itemStack = ItemStack.EMPTY;
/* 52 */     Slot slot = (Slot)this.slots.get(paramInt);
/* 53 */     if (slot != null && slot.hasItem()) {
/* 54 */       ItemStack itemStack1 = slot.getItem();
/* 55 */       itemStack = itemStack1.copy();
/*    */       
/* 57 */       if (paramInt < 9) {
/* 58 */         if (!moveItemStackTo(itemStack1, 9, 45, true)) {
/* 59 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 62 */       else if (!moveItemStackTo(itemStack1, 0, 9, false)) {
/* 63 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 66 */       if (itemStack1.isEmpty()) {
/* 67 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 69 */         slot.setChanged();
/*    */       } 
/* 71 */       if (itemStack1.getCount() == itemStack.getCount())
/*    */       {
/* 73 */         return ItemStack.EMPTY;
/*    */       }
/* 75 */       slot.onTake(paramPlayer, itemStack1);
/*    */     } 
/*    */     
/* 78 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player paramPlayer) {
/* 83 */     super.removed(paramPlayer);
/* 84 */     this.dispenser.stopOpen((ContainerUser)paramPlayer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\DispenserMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */