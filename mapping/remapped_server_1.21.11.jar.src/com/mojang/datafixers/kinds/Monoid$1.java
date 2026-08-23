/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
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
/*    */ class null
/*    */   implements Monoid<List<T>>
/*    */ {
/*    */   public List<T> point() {
/* 19 */     return (List<T>)ImmutableList.of();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> add(List<T> paramList1, List<T> paramList2) {
/* 24 */     ImmutableList.Builder builder = ImmutableList.builder();
/* 25 */     builder.addAll(paramList1);
/* 26 */     builder.addAll(paramList2);
/* 27 */     return (List<T>)builder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Monoid$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */