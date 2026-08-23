/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App2;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Box<S, T, A, B>
/*    */   implements App2<Lens.Mu2<S, T>, B, A>
/*    */ {
/*    */   private final Lens<S, T, A, B> lens;
/*    */   
/*    */   public Box(Lens<S, T, A, B> paramLens) {
/* 35 */     this.lens = paramLens;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Lens$Box.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */