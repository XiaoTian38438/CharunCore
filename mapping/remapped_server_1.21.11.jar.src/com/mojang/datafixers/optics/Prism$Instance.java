/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.function.Function;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Instance<A2, B2>
/*    */   implements Cocartesian<Prism.Mu<A2, B2>, Cocartesian.Mu>
/*    */ {
/*    */   public <A, B, C, D> FunctionType<App2<Prism.Mu<A2, B2>, A, B>, App2<Prism.Mu<A2, B2>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 40 */     return paramApp2 -> Optics.prism((), ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Prism.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Prism.Mu<A2, B2>, A, B> paramApp2) {
/* 48 */     Prism<A, B, A2, B2> prism = Prism.unbox(paramApp2);
/* 49 */     return Optics.prism(paramEither -> (Either)paramEither.map((), ()), paramObject -> Either.left(paramPrism.build(paramObject)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Prism.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Prism.Mu<A2, B2>, A, B> paramApp2) {
/* 60 */     Prism<A, B, A2, B2> prism = Prism.unbox(paramApp2);
/* 61 */     return Optics.prism(paramEither -> (Either)paramEither.map((), ()), paramObject -> Either.right(paramPrism.build(paramObject)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Prism$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */