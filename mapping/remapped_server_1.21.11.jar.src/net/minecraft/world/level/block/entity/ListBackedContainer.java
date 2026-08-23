/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface ListBackedContainer
/*    */   extends Container {
/*    */   NonNullList<ItemStack> getItems();
/*    */   
/*    */   default int count() {
/* 15 */     return (int)getItems().stream().filter(Predicate.not(ItemStack::isEmpty)).count();
/*    */   }
/*    */   
/*    */   default int getContainerSize() {
/* 19 */     return getItems().size();
/*    */   }
/*    */   
/*    */   default void clearContent() {
/* 23 */     getItems().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isEmpty() {
/* 28 */     return getItems().stream().allMatch(ItemStack::isEmpty);
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack getItem(int paramInt) {
/* 33 */     return (ItemStack)getItems().get(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItem(int paramInt1, int paramInt2) {
/* 38 */     ItemStack itemStack = ContainerHelper.removeItem((List)getItems(), paramInt1, paramInt2);
/* 39 */     if (!itemStack.isEmpty()) {
/* 40 */       setChanged();
/*    */     }
/* 42 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItemNoUpdate(int paramInt) {
/* 47 */     return ContainerHelper.removeItem((List)getItems(), paramInt, getMaxStackSize());
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean canPlaceItem(int paramInt, ItemStack paramItemStack) {
/* 52 */     return (acceptsItemType(paramItemStack) && (
/* 53 */       getItem(paramInt).isEmpty() || getItem(paramInt).getCount() < getMaxStackSize(paramItemStack)));
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean acceptsItemType(ItemStack paramItemStack) {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   default void setItem(int paramInt, ItemStack paramItemStack) {
/* 63 */     setItemNoUpdate(paramInt, paramItemStack);
/* 64 */     setChanged();
/*    */   }
/*    */   
/*    */   default void setItemNoUpdate(int paramInt, ItemStack paramItemStack) {
/* 68 */     getItems().set(paramInt, paramItemStack);
/* 69 */     paramItemStack.limitSize(getMaxStackSize(paramItemStack));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\ListBackedContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */