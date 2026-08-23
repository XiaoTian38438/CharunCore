/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.CartesianLike;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface Cartesian<P extends com.mojang.datafixers.kinds.K2, Mu extends Cartesian.Mu>
/*    */   extends Profunctor<P, Mu> {
/*    */   <A, B, C> App2<P, Pair<A, C>, Pair<B, C>> first(App2<P, A, B> paramApp2);
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Cartesian<P, Proof> unbox(App<Proof, P> paramApp) {
/* 15 */     return (Cartesian<P, Proof>)paramApp;
/*    */   }
/*    */   class null extends TypeToken<Mu> {}
/*    */   
/* 19 */   public static interface Mu extends Profunctor.Mu { public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       }; }
/*    */ 
/*    */   
/*    */   default <A, B, C> App2<P, Pair<C, A>, Pair<C, B>> second(App2<P, A, B> paramApp2) {
/* 25 */     return dimap(first(paramApp2), Pair::swap, Pair::swap);
/*    */   }
/*    */   
/*    */   default FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>> toFP2() {
/* 29 */     return new FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>>()
/*    */       {
/*    */         public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CartesianLike.Mu, F> param1App, App2<P, A, B> param1App2) {
/* 32 */           return cap(CartesianLike.unbox(param1App), param1App2);
/*    */         }
/*    */         
/*    */         private <A, B, F extends com.mojang.datafixers.kinds.K1, C> App2<P, App<F, A>, App<F, B>> cap(CartesianLike<F, C, ?> param1CartesianLike, App2<P, A, B> param1App2) {
/* 36 */           Objects.requireNonNull(param1CartesianLike); return Cartesian.this.dimap(Cartesian.this.first(param1App2), param1App -> Pair.unbox(param1CartesianLike.to(param1App)), param1CartesianLike::from);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Cartesian.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */