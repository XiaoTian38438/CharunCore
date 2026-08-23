/*    */ package net.minecraft.world;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class ContainerHelper
/*    */ {
/*    */   public static final String TAG_ITEMS = "Items";
/*    */   
/*    */   public static ItemStack removeItem(List<ItemStack> paramList, int paramInt1, int paramInt2) {
/* 15 */     if (paramInt1 < 0 || paramInt1 >= paramList.size() || ((ItemStack)paramList.get(paramInt1)).isEmpty() || paramInt2 <= 0) {
/* 16 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 19 */     return ((ItemStack)paramList.get(paramInt1)).split(paramInt2);
/*    */   }
/*    */   
/*    */   public static ItemStack takeItem(List<ItemStack> paramList, int paramInt) {
/* 23 */     if (paramInt < 0 || paramInt >= paramList.size()) {
/* 24 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 27 */     return paramList.set(paramInt, ItemStack.EMPTY);
/*    */   }
/*    */   
/*    */   public static void saveAllItems(ValueOutput paramValueOutput, NonNullList<ItemStack> paramNonNullList) {
/* 31 */     saveAllItems(paramValueOutput, paramNonNullList, true);
/*    */   }
/*    */   
/*    */   public static void saveAllItems(ValueOutput paramValueOutput, NonNullList<ItemStack> paramNonNullList, boolean paramBoolean) {
/* 35 */     ValueOutput.TypedOutputList typedOutputList = paramValueOutput.list("Items", ItemStackWithSlot.CODEC);
/* 36 */     for (byte b = 0; b < paramNonNullList.size(); b++) {
/* 37 */       ItemStack itemStack = (ItemStack)paramNonNullList.get(b);
/* 38 */       if (!itemStack.isEmpty()) {
/* 39 */         typedOutputList.add(new ItemStackWithSlot(b, itemStack));
/*    */       }
/*    */     } 
/* 42 */     if (typedOutputList.isEmpty() && !paramBoolean) {
/* 43 */       paramValueOutput.discard("Items");
/*    */     }
/*    */   }
/*    */   
/*    */   public static void loadAllItems(ValueInput paramValueInput, NonNullList<ItemStack> paramNonNullList) {
/* 48 */     for (ItemStackWithSlot itemStackWithSlot : paramValueInput.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
/* 49 */       if (itemStackWithSlot.isValidInContainer(paramNonNullList.size())) {
/* 50 */         paramNonNullList.set(itemStackWithSlot.slot(), itemStackWithSlot.stack());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public static int clearOrCountMatchingItems(Container paramContainer, Predicate<ItemStack> paramPredicate, int paramInt, boolean paramBoolean) {
/* 56 */     int i = 0;
/* 57 */     for (byte b = 0; b < paramContainer.getContainerSize(); b++) {
/* 58 */       ItemStack itemStack = paramContainer.getItem(b);
/* 59 */       int j = clearOrCountMatchingItems(itemStack, paramPredicate, paramInt - i, paramBoolean);
/* 60 */       if (j > 0 && !paramBoolean && itemStack.isEmpty()) {
/* 61 */         paramContainer.setItem(b, ItemStack.EMPTY);
/*    */       }
/* 63 */       i += j;
/*    */     } 
/* 65 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int clearOrCountMatchingItems(ItemStack paramItemStack, Predicate<ItemStack> paramPredicate, int paramInt, boolean paramBoolean) {
/* 70 */     if (paramItemStack.isEmpty() || !paramPredicate.test(paramItemStack)) {
/* 71 */       return 0;
/*    */     }
/*    */     
/* 74 */     if (paramBoolean) {
/* 75 */       return paramItemStack.getCount();
/*    */     }
/*    */     
/* 78 */     int i = (paramInt < 0) ? paramItemStack.getCount() : Math.min(paramInt, paramItemStack.getCount());
/* 79 */     paramItemStack.shrink(i);
/* 80 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\ContainerHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */