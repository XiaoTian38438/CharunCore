/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ public final class Proj2<F, G, G2>
/*    */   implements Lens<Pair<F, G>, Pair<F, G2>, G, G2>
/*    */ {
/*  8 */   public static final Proj2<?, ?, ?> INSTANCE = new Proj2();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public G view(Pair<F, G> paramPair) {
/* 15 */     return (G)paramPair.getSecond();
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<F, G2> update(G2 paramG2, Pair<F, G> paramPair) {
/* 20 */     return Pair.of(paramPair.getFirst(), paramG2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "π2";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Proj2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */