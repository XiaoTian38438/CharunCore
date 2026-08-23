/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ShulkerBoxMenu
/*    */   extends AbstractContainerMenu {
/*    */   private static final int CONTAINER_SIZE = 27;
/*    */   private final Container container;
/*    */   
/*    */   public ShulkerBoxMenu(int paramInt, Inventory paramInventory) {
/* 16 */     this(paramInt, paramInventory, (Container)new SimpleContainer(27));
/*    */   }
/*    */   
/*    */   public ShulkerBoxMenu(int paramInt, Inventory paramInventory, Container paramContainer) {
/* 20 */     super(MenuType.SHULKER_BOX, paramInt);
/* 21 */     checkContainerSize(paramContainer, 27);
/* 22 */     this.container = paramContainer;
/* 23 */     paramContainer.startOpen((ContainerUser)paramInventory.player);
/*    */     
/* 25 */     byte b1 = 3;
/* 26 */     byte b2 = 9;
/*    */     
/* 28 */     for (byte b3 = 0; b3 < 3; b3++) {
/* 29 */       for (byte b = 0; b < 9; b++) {
/* 30 */         addSlot(new ShulkerBoxSlot(paramContainer, b + b3 * 9, 8 + b * 18, 18 + b3 * 18));
/*    */       }
/*    */     } 
/*    */     
/* 34 */     addStandardInventorySlots((Container)paramInventory, 8, 84);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 39 */     return this.container.stillValid(paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/* 44 */     ItemStack itemStack = ItemStack.EMPTY;
/* 45 */     Slot slot = (Slot)this.slots.get(paramInt);
/* 46 */     if (slot != null && slot.hasItem()) {
/* 47 */       ItemStack itemStack1 = slot.getItem();
/* 48 */       itemStack = itemStack1.copy();
/*    */       
/* 50 */       if (paramInt < this.container.getContainerSize()) {
/* 51 */         if (!moveItemStackTo(itemStack1, this.container.getContainerSize(), this.slots.size(), true)) {
/* 52 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 55 */       else if (!moveItemStackTo(itemStack1, 0, this.container.getContainerSize(), false)) {
/* 56 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 59 */       if (itemStack1.isEmpty()) {
/* 60 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 62 */         slot.setChanged();
/*    */       } 
/*    */     } 
/* 65 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player paramPlayer) {
/* 70 */     super.removed(paramPlayer);
/* 71 */     this.container.stopOpen((ContainerUser)paramPlayer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ShulkerBoxMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */