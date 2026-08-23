/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Deque;
/*    */ import java.util.List;
/*    */ import java.util.RandomAccess;
/*    */ import java.util.SequencedCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ListAndDeque<T>
/*    */   extends Serializable, Cloneable, Deque<T>, List<T>, RandomAccess
/*    */ {
/*    */   default boolean offer(T paramT) {
/* 34 */     return offerLast(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   default T remove() {
/* 39 */     return removeFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   default T poll() {
/* 44 */     return pollFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   default T element() {
/* 49 */     return getFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   default T peek() {
/* 54 */     return peekFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   default void push(T paramT) {
/* 59 */     addFirst(paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   default T pop() {
/* 64 */     return removeFirst();
/*    */   }
/*    */   
/*    */   ListAndDeque<T> reversed();
/*    */   
/*    */   T getFirst();
/*    */   
/*    */   T getLast();
/*    */   
/*    */   void addFirst(T paramT);
/*    */   
/*    */   void addLast(T paramT);
/*    */   
/*    */   T removeFirst();
/*    */   
/*    */   T removeLast();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\ListAndDeque.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */