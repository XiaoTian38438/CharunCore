/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ContainerComponentManipulator<T>
/*    */ {
/*    */   default void setContents(ItemStack paramItemStack, T paramT, Stream<ItemStack> paramStream) {
/* 20 */     Object object = paramItemStack.getOrDefault(type(), paramT);
/* 21 */     T t = setContents((T)object, paramStream);
/* 22 */     paramItemStack.set(type(), t);
/*    */   }
/*    */   
/*    */   default void setContents(ItemStack paramItemStack, Stream<ItemStack> paramStream) {
/* 26 */     setContents(paramItemStack, empty(), paramStream);
/*    */   }
/*    */   
/*    */   default void modifyItems(ItemStack paramItemStack, UnaryOperator<ItemStack> paramUnaryOperator) {
/* 30 */     Object object = paramItemStack.get(type());
/* 31 */     if (object != null) {
/*    */       
/* 33 */       UnaryOperator<? super ItemStack, ? extends ItemStack> unaryOperator = paramItemStack -> {
/*    */           if (paramItemStack.isEmpty()) {
/*    */             return paramItemStack;
/*    */           }
/*    */           ItemStack itemStack = paramUnaryOperator.apply(paramItemStack);
/*    */           itemStack.limitSize(itemStack.getMaxStackSize());
/*    */           return itemStack;
/*    */         };
/* 41 */       setContents(paramItemStack, getContents((T)object).map((Function<? super ItemStack, ? extends ItemStack>)unaryOperator));
/*    */     } 
/*    */   }
/*    */   
/*    */   default SlotCollection getSlots(ItemStack paramItemStack) {
/* 46 */     return () -> {
/*    */         Object object = paramItemStack.get(type());
/*    */         return (object != null) ? getContents((T)object).filter(()) : Stream.empty();
/*    */       };
/*    */   }
/*    */   
/*    */   DataComponentType<T> type();
/*    */   
/*    */   T empty();
/*    */   
/*    */   T setContents(T paramT, Stream<ItemStack> paramStream);
/*    */   
/*    */   Stream<ItemStack> getContents(T paramT);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */