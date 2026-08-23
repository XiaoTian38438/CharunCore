/*    */ package net.minecraft.world.inventory;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public abstract class AbstractMountInventoryMenu extends AbstractContainerMenu {
/*    */   protected final Container mountContainer;
/* 12 */   protected final int SLOT_SADDLE = 0; protected final LivingEntity mount;
/* 13 */   protected final int SLOT_BODY_ARMOR = 1;
/* 14 */   protected final int SLOT_INVENTORY_START = 2;
/*    */   protected static final int INVENTORY_ROWS = 3;
/*    */   
/*    */   protected AbstractMountInventoryMenu(int paramInt, Inventory paramInventory, Container paramContainer, LivingEntity paramLivingEntity) {
/* 18 */     super(null, paramInt);
/* 19 */     this.mountContainer = paramContainer;
/* 20 */     this.mount = paramLivingEntity;
/* 21 */     paramContainer.startOpen((ContainerUser)paramInventory.player);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract boolean hasInventoryChanged(Container paramContainer);
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 28 */     return (!hasInventoryChanged(this.mountContainer) && this.mountContainer.stillValid(paramPlayer) && this.mount.isAlive() && paramPlayer.isWithinEntityInteractionRange((Entity)this.mount, 4.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player paramPlayer) {
/* 33 */     super.removed(paramPlayer);
/* 34 */     this.mountContainer.stopOpen((ContainerUser)paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/* 39 */     ItemStack itemStack = ItemStack.EMPTY;
/* 40 */     Slot slot = (Slot)this.slots.get(paramInt);
/* 41 */     if (slot != null && slot.hasItem()) {
/* 42 */       ItemStack itemStack1 = slot.getItem();
/* 43 */       itemStack = itemStack1.copy();
/*    */       
/* 45 */       int i = 2 + this.mountContainer.getContainerSize();
/*    */       
/* 47 */       if (paramInt < i) {
/* 48 */         if (!moveItemStackTo(itemStack1, i, this.slots.size(), true)) {
/* 49 */           return ItemStack.EMPTY;
/*    */         }
/* 51 */       } else if (getSlot(1).mayPlace(itemStack1) && !getSlot(1).hasItem()) {
/* 52 */         if (!moveItemStackTo(itemStack1, 1, 2, false)) {
/* 53 */           return ItemStack.EMPTY;
/*    */         }
/* 55 */       } else if (getSlot(0).mayPlace(itemStack1) && !getSlot(0).hasItem()) {
/* 56 */         if (!moveItemStackTo(itemStack1, 0, 1, false)) {
/* 57 */           return ItemStack.EMPTY;
/*    */         }
/* 59 */       } else if (this.mountContainer.getContainerSize() == 0 || !moveItemStackTo(itemStack1, 2, i, false)) {
/* 60 */         int j = i + 27;
/* 61 */         int k = j;
/* 62 */         int m = k + 9;
/* 63 */         if (paramInt >= k && paramInt < m) {
/* 64 */           if (!moveItemStackTo(itemStack1, i, j, false)) {
/* 65 */             return ItemStack.EMPTY;
/*    */           }
/* 67 */         } else if (paramInt >= i && paramInt < j) {
/* 68 */           if (!moveItemStackTo(itemStack1, k, m, false)) {
/* 69 */             return ItemStack.EMPTY;
/*    */           }
/* 71 */         } else if (!moveItemStackTo(itemStack1, k, j, false)) {
/* 72 */           return ItemStack.EMPTY;
/*    */         } 
/* 74 */         return ItemStack.EMPTY;
/*    */       } 
/* 76 */       if (itemStack1.isEmpty()) {
/* 77 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 79 */         slot.setChanged();
/*    */       } 
/*    */     } 
/* 82 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static int getInventorySize(int paramInt) {
/* 86 */     return paramInt * 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\AbstractMountInventoryMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */