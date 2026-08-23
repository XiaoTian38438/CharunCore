/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.CocartesianLike;
/*    */ import com.mojang.datafixers.util.Either;
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
/*    */   implements FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>>
/*    */ {
/*    */   public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CocartesianLike.Mu, F> paramApp, App2<P, A, B> paramApp2) {
/* 32 */     return cap(CocartesianLike.unbox(paramApp), paramApp2);
/*    */   }
/*    */   
/*    */   private <A, B, F extends com.mojang.datafixers.kinds.K1, C> App2<P, App<F, A>, App<F, B>> cap(CocartesianLike<F, C, ?> paramCocartesianLike, App2<P, A, B> paramApp2) {
/* 36 */     Objects.requireNonNull(paramCocartesianLike); return Cocartesian.this.dimap(Cocartesian.this.left(paramApp2), paramApp -> Either.unbox(paramCocartesianLike.to(paramApp)), paramCocartesianLike::from);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Cocartesian$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */