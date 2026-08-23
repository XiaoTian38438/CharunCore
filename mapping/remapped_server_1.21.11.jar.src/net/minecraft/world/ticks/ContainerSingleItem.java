/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface ContainerSingleItem extends Container {
/*    */   ItemStack getTheItem();
/*    */   
/*    */   default ItemStack splitTheItem(int paramInt) {
/* 12 */     return getTheItem().split(paramInt);
/*    */   }
/*    */   
/*    */   void setTheItem(ItemStack paramItemStack);
/*    */   
/*    */   default ItemStack removeTheItem() {
/* 18 */     return splitTheItem(getMaxStackSize());
/*    */   }
/*    */ 
/*    */   
/*    */   default int getContainerSize() {
/* 23 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isEmpty() {
/* 28 */     return getTheItem().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   default void clearContent() {
/* 33 */     removeTheItem();
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItemNoUpdate(int paramInt) {
/* 38 */     return removeItem(paramInt, getMaxStackSize());
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack getItem(int paramInt) {
/* 43 */     return (paramInt == 0) ? getTheItem() : ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItem(int paramInt1, int paramInt2) {
/* 48 */     if (paramInt1 != 0) {
/* 49 */       return ItemStack.EMPTY;
/*    */     }
/* 51 */     return splitTheItem(paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   default void setItem(int paramInt, ItemStack paramItemStack) {
/* 56 */     if (paramInt == 0)
/* 57 */       setTheItem(paramItemStack); 
/*    */   }
/*    */   
/*    */   public static interface BlockContainerSingleItem
/*    */     extends ContainerSingleItem
/*    */   {
/*    */     BlockEntity getContainerBlockEntity();
/*    */     
/*    */     default boolean stillValid(Player param1Player) {
/* 66 */       return Container.stillValidBlockEntity(getContainerBlockEntity(), param1Player);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ticks\ContainerSingleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */