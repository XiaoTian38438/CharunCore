/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ public final class Proj1<F, G, F2>
/*    */   implements Lens<Pair<F, G>, Pair<F2, G>, F, F2>
/*    */ {
/*  8 */   public static final Proj1<?, ?, ?> INSTANCE = new Proj1();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public F view(Pair<F, G> paramPair) {
/* 15 */     return (F)paramPair.getFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<F2, G> update(F2 paramF2, Pair<F, G> paramPair) {
/* 20 */     return Pair.of(paramF2, paramPair.getSecond());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "π1";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Proj1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */