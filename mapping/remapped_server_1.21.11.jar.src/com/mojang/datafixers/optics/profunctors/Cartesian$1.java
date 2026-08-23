/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.CartesianLike;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
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
/*    */ class null
/*    */   implements FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>>
/*    */ {
/*    */   public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CartesianLike.Mu, F> paramApp, App2<P, A, B> paramApp2) {
/* 32 */     return cap(CartesianLike.unbox(paramApp), paramApp2);
/*    */   }
/*    */   
/*    */   private <A, B, F extends com.mojang.datafixers.kinds.K1, C> App2<P, App<F, A>, App<F, B>> cap(CartesianLike<F, C, ?> paramCartesianLike, App2<P, A, B> paramApp2) {
/* 36 */     Objects.requireNonNull(paramCartesianLike); return Cartesian.this.dimap(Cartesian.this.first(paramApp2), paramApp -> Pair.unbox(paramCartesianLike.to(paramApp)), paramCartesianLike::from);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Cartesian$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */