/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Monoid<T>
/*    */ {
/*    */   T point();
/*    */   
/*    */   T add(T paramT1, T paramT2);
/*    */   
/*    */   static <T> Monoid<List<T>> listMonoid() {
/* 16 */     return (Monoid)new Monoid<List<List<T>>>()
/*    */       {
/*    */         public List<T> point() {
/* 19 */           return (List<T>)ImmutableList.of();
/*    */         }
/*    */ 
/*    */         
/*    */         public List<T> add(List<T> param1List1, List<T> param1List2) {
/* 24 */           ImmutableList.Builder builder = ImmutableList.builder();
/* 25 */           builder.addAll(param1List1);
/* 26 */           builder.addAll(param1List2);
/* 27 */           return (List<T>)builder.build();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Monoid.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */