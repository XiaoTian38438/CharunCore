/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class HopperMenu
/*    */   extends AbstractContainerMenu {
/*    */   public static final int CONTAINER_SIZE = 5;
/*    */   private final Container hopper;
/*    */   
/*    */   public HopperMenu(int paramInt, Inventory paramInventory) {
/* 16 */     this(paramInt, paramInventory, (Container)new SimpleContainer(5));
/*    */   }
/*    */   
/*    */   public HopperMenu(int paramInt, Inventory paramInventory, Container paramContainer) {
/* 20 */     super(MenuType.HOPPER, paramInt);
/* 21 */     this.hopper = paramContainer;
/* 22 */     checkContainerSize(paramContainer, 5);
/*    */     
/* 24 */     paramContainer.startOpen((ContainerUser)paramInventory.player);
/*    */     
/* 26 */     for (byte b = 0; b < 5; b++) {
/* 27 */       addSlot(new Slot(paramContainer, b, 44 + b * 18, 20));
/*    */     }
/*    */     
/* 30 */     addStandardInventorySlots((Container)paramInventory, 8, 51);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 35 */     return this.hopper.stillValid(paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/* 40 */     ItemStack itemStack = ItemStack.EMPTY;
/* 41 */     Slot slot = (Slot)this.slots.get(paramInt);
/* 42 */     if (slot != null && slot.hasItem()) {
/* 43 */       ItemStack itemStack1 = slot.getItem();
/* 44 */       itemStack = itemStack1.copy();
/*    */       
/* 46 */       if (paramInt < this.hopper.getContainerSize()) {
/* 47 */         if (!moveItemStackTo(itemStack1, this.hopper.getContainerSize(), this.slots.size(), true)) {
/* 48 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 51 */       else if (!moveItemStackTo(itemStack1, 0, this.hopper.getContainerSize(), false)) {
/* 52 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 55 */       if (itemStack1.isEmpty()) {
/* 56 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 58 */         slot.setChanged();
/*    */       } 
/*    */     } 
/* 61 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player paramPlayer) {
/* 66 */     super.removed(paramPlayer);
/* 67 */     this.hopper.stopOpen((ContainerUser)paramPlayer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\HopperMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */