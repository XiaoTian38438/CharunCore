/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
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
/*    */ public final class Instance<R>
/*    */   implements Cocartesian<ReForgetE.Mu<R>, ReForgetE.Instance.Mu<R>>, App<ReForgetE.Instance.Mu<R>, ReForgetE.Mu<R>>
/*    */ {
/*    */   static final class Mu<R>
/*    */     implements Cocartesian.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ReForgetE.Mu<R>, A, B>, App2<ReForgetE.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 28 */     return paramApp2 -> Optics.reForgetE("dimap", ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetE.Mu<R>, A, B> paramApp2) {
/* 38 */     ReForgetE<R, A, B> reForgetE = ReForgetE.unbox(paramApp2);
/* 39 */     return Optics.reForgetE("left", paramEither -> (Either)paramEither.map((), ()));
/*    */   }
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
/*    */   public <A, B, C> App2<ReForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetE.Mu<R>, A, B> paramApp2) {
/* 52 */     ReForgetE<R, A, B> reForgetE = ReForgetE.unbox(paramApp2);
/* 53 */     return Optics.reForgetE("right", paramEither -> (Either)paramEither.map((), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForgetE$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */