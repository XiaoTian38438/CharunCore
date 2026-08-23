/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
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
/*    */ public final class Graph
/*    */ {
/*    */   public static <T> boolean depthFirstSearch(Map<T, Set<T>> paramMap, Set<T> paramSet1, Set<T> paramSet2, Consumer<T> paramConsumer, T paramT) {
/* 25 */     if (paramSet1.contains(paramT)) {
/* 26 */       return false;
/*    */     }
/* 28 */     if (paramSet2.contains(paramT)) {
/* 29 */       return true;
/*    */     }
/* 31 */     paramSet2.add(paramT);
/* 32 */     for (T t : paramMap.getOrDefault(paramT, ImmutableSet.of())) {
/* 33 */       if (depthFirstSearch(paramMap, paramSet1, paramSet2, paramConsumer, t)) {
/* 34 */         return true;
/*    */       }
/*    */     } 
/* 37 */     paramSet2.remove(paramT);
/* 38 */     paramSet1.add(paramT);
/* 39 */     paramConsumer.accept(paramT);
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\Graph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */