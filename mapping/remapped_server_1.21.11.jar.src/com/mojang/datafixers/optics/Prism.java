/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Prism<S, T, A, B>
/*    */   extends App2<Prism.Mu<A, B>, S, T>, Optic<Cocartesian.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Prism<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 18 */     return (Prism)paramApp2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Cocartesian.Mu, P> paramApp) {
/* 27 */     Cocartesian cocartesian = Cocartesian.unbox(paramApp);
/* 28 */     return paramApp2 -> paramCocartesian.dimap(paramCocartesian.right(paramApp2), this::match, ());
/*    */   }
/*    */ 
/*    */   
/*    */   Either<T, A> match(S paramS);
/*    */   
/*    */   T build(B paramB);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Cocartesian<Mu<A2, B2>, Cocartesian.Mu>
/*    */   {
/*    */     public <A, B, C, D> FunctionType<App2<Prism.Mu<A2, B2>, A, B>, App2<Prism.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 40 */       return param1App2 -> Optics.prism((), ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Prism.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Prism.Mu<A2, B2>, A, B> param1App2) {
/* 48 */       Prism<A, B, A2, B2> prism = Prism.unbox(param1App2);
/* 49 */       return Optics.prism(param1Either -> (Either)param1Either.map((), ()), param1Object -> Either.left(param1Prism.build(param1Object)));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Prism.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Prism.Mu<A2, B2>, A, B> param1App2) {
/* 60 */       Prism<A, B, A2, B2> prism = Prism.unbox(param1App2);
/* 61 */       return Optics.prism(param1Either -> (Either)param1Either.map((), ()), param1Object -> Either.right(param1Prism.build(param1Object)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Prism.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */