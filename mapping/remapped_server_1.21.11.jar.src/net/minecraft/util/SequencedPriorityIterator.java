/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Queues;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Deque;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SequencedPriorityIterator<T>
/*    */   extends AbstractIterator<T>
/*    */ {
/*    */   private static final int MIN_PRIO = -2147483648;
/* 21 */   private Deque<T> highestPrioQueue = null;
/* 22 */   private int highestPrio = Integer.MIN_VALUE;
/*    */   
/* 24 */   private final Int2ObjectMap<Deque<T>> queuesByPriority = (Int2ObjectMap<Deque<T>>)new Int2ObjectOpenHashMap();
/*    */   
/*    */   public void add(T paramT, int paramInt) {
/* 27 */     if (paramInt == this.highestPrio && this.highestPrioQueue != null) {
/* 28 */       this.highestPrioQueue.addLast(paramT);
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     Deque<T> deque = (Deque)this.queuesByPriority.computeIfAbsent(paramInt, paramInt -> Queues.newArrayDeque());
/* 33 */     deque.addLast(paramT);
/*    */     
/* 35 */     if (paramInt >= this.highestPrio) {
/* 36 */       this.highestPrioQueue = deque;
/* 37 */       this.highestPrio = paramInt;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected T computeNext() {
/* 43 */     if (this.highestPrioQueue == null) {
/* 44 */       return (T)endOfData();
/*    */     }
/*    */     
/* 47 */     T t = this.highestPrioQueue.removeFirst();
/*    */     
/* 49 */     if (t == null) {
/* 50 */       return (T)endOfData();
/*    */     }
/*    */     
/* 53 */     if (this.highestPrioQueue.isEmpty()) {
/* 54 */       switchCacheToNextHighestPrioQueue();
/*    */     }
/*    */     
/* 57 */     return t;
/*    */   }
/*    */   
/*    */   private void switchCacheToNextHighestPrioQueue() {
/* 61 */     int i = Integer.MIN_VALUE;
/* 62 */     Deque<T> deque = null;
/*    */     
/* 64 */     for (ObjectIterator<Int2ObjectMap.Entry> objectIterator = Int2ObjectMaps.fastIterable(this.queuesByPriority).iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry entry = objectIterator.next();
/* 65 */       Deque<T> deque1 = (Deque)entry.getValue();
/* 66 */       int j = entry.getIntKey();
/*    */       
/* 68 */       if (j > i && !deque1.isEmpty()) {
/* 69 */         i = j;
/* 70 */         deque = deque1;
/*    */         
/* 72 */         if (j == this.highestPrio - 1) {
/*    */           break;
/*    */         }
/*    */       }  }
/*    */ 
/*    */     
/* 78 */     this.highestPrio = i;
/* 79 */     this.highestPrioQueue = deque;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SequencedPriorityIterator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */