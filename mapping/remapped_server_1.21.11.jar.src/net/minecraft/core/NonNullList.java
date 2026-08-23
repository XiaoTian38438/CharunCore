/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.AbstractList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class NonNullList<E> extends AbstractList<E> {
/*    */   private final List<E> list;
/*    */   
/*    */   public static <E> NonNullList<E> create() {
/* 13 */     return new NonNullList<>(Lists.newArrayList(), null);
/*    */   }
/*    */   private final E defaultValue;
/*    */   public static <E> NonNullList<E> createWithCapacity(int paramInt) {
/* 17 */     return new NonNullList<>(Lists.newArrayListWithCapacity(paramInt), null);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <E> NonNullList<E> withSize(int paramInt, E paramE) {
/* 22 */     Objects.requireNonNull(paramE);
/*    */     
/* 24 */     Object[] arrayOfObject = new Object[paramInt];
/* 25 */     Arrays.fill(arrayOfObject, paramE);
/* 26 */     return new NonNullList<>(Arrays.asList((E[])arrayOfObject), paramE);
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   public static <E> NonNullList<E> of(E paramE, E... paramVarArgs) {
/* 31 */     return new NonNullList<>(Arrays.asList(paramVarArgs), paramE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected NonNullList(List<E> paramList, E paramE) {
/* 38 */     this.list = paramList;
/* 39 */     this.defaultValue = paramE;
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int paramInt) {
/* 44 */     return this.list.get(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public E set(int paramInt, E paramE) {
/* 49 */     Objects.requireNonNull(paramE);
/*    */     
/* 51 */     return this.list.set(paramInt, paramE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int paramInt, E paramE) {
/* 56 */     Objects.requireNonNull(paramE);
/*    */     
/* 58 */     this.list.add(paramInt, paramE);
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int paramInt) {
/* 63 */     return this.list.remove(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 68 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 73 */     if (this.defaultValue == null) {
/* 74 */       super.clear();
/*    */     } else {
/* 76 */       for (byte b = 0; b < size(); b++)
/* 77 */         set(b, this.defaultValue); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\NonNullList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */