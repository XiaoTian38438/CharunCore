/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.ItemStackWithSlot;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class PlayerEnderChestContainer
/*    */   extends SimpleContainer {
/*    */   private EnderChestBlockEntity activeChest;
/*    */   
/*    */   public PlayerEnderChestContainer() {
/* 17 */     super(27);
/*    */   }
/*    */   
/*    */   public void setActiveChest(EnderChestBlockEntity paramEnderChestBlockEntity) {
/* 21 */     this.activeChest = paramEnderChestBlockEntity;
/*    */   }
/*    */   
/*    */   public boolean isActiveChest(EnderChestBlockEntity paramEnderChestBlockEntity) {
/* 25 */     return (this.activeChest == paramEnderChestBlockEntity);
/*    */   }
/*    */   
/*    */   public void fromSlots(ValueInput.TypedInputList<ItemStackWithSlot> paramTypedInputList) {
/* 29 */     for (byte b = 0; b < getContainerSize(); b++) {
/* 30 */       setItem(b, ItemStack.EMPTY);
/*    */     }
/*    */     
/* 33 */     for (ItemStackWithSlot itemStackWithSlot : paramTypedInputList) {
/* 34 */       if (itemStackWithSlot.isValidInContainer(getContainerSize())) {
/* 35 */         setItem(itemStackWithSlot.slot(), itemStackWithSlot.stack());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void storeAsSlots(ValueOutput.TypedOutputList<ItemStackWithSlot> paramTypedOutputList) {
/* 41 */     for (byte b = 0; b < getContainerSize(); b++) {
/* 42 */       ItemStack itemStack = getItem(b);
/* 43 */       if (!itemStack.isEmpty()) {
/* 44 */         paramTypedOutputList.add(new ItemStackWithSlot(b, itemStack));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 51 */     if (this.activeChest != null && !this.activeChest.stillValid(paramPlayer)) {
/* 52 */       return false;
/*    */     }
/* 54 */     return super.stillValid(paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startOpen(ContainerUser paramContainerUser) {
/* 59 */     if (this.activeChest != null) {
/* 60 */       this.activeChest.startOpen(paramContainerUser);
/*    */     }
/* 62 */     super.startOpen(paramContainerUser);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopOpen(ContainerUser paramContainerUser) {
/* 67 */     if (this.activeChest != null) {
/* 68 */       this.activeChest.stopOpen(paramContainerUser);
/*    */     }
/* 70 */     super.stopOpen(paramContainerUser);
/* 71 */     this.activeChest = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\PlayerEnderChestContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */