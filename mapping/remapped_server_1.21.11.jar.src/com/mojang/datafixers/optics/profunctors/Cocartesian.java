/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.CocartesianLike;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface Cocartesian<P extends com.mojang.datafixers.kinds.K2, Mu extends Cocartesian.Mu>
/*    */   extends Profunctor<P, Mu> {
/*    */   <A, B, C> App2<P, Either<A, C>, Either<B, C>> left(App2<P, A, B> paramApp2);
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Cocartesian<P, Proof> unbox(App<Proof, P> paramApp) {
/* 15 */     return (Cocartesian<P, Proof>)paramApp;
/*    */   }
/*    */   class null extends TypeToken<Mu> {}
/*    */   
/* 19 */   public static interface Mu extends Profunctor.Mu { public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       }; }
/*    */ 
/*    */   
/*    */   default <A, B, C> App2<P, Either<C, A>, Either<C, B>> right(App2<P, A, B> paramApp2) {
/* 25 */     return dimap(left(paramApp2), Either::swap, Either::swap);
/*    */   }
/*    */   
/*    */   default FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>> toFP() {
/* 29 */     return new FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>>()
/*    */       {
/*    */         public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CocartesianLike.Mu, F> param1App, App2<P, A, B> param1App2) {
/* 32 */           return cap(CocartesianLike.unbox(param1App), param1App2);
/*    */         }
/*    */         
/*    */         private <A, B, F extends com.mojang.datafixers.kinds.K1, C> App2<P, App<F, A>, App<F, B>> cap(CocartesianLike<F, C, ?> param1CocartesianLike, App2<P, A, B> param1App2) {
/* 36 */           Objects.requireNonNull(param1CocartesianLike); return Cocartesian.this.dimap(Cocartesian.this.left(param1App2), param1App -> Either.unbox(param1CocartesianLike.to(param1App)), param1CocartesianLike::from);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Cocartesian.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */