/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
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
/*    */ public final class Instance<R>
/*    */   implements AffineP<ForgetOpt.Mu<R>, ForgetOpt.Instance.Mu<R>>, App<ForgetOpt.Instance.Mu<R>, ForgetOpt.Mu<R>>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements AffineP.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 30 */     return paramApp2 -> Optics.forgetOpt(());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ForgetOpt.Mu<R>, A, B> paramApp2) {
/* 35 */     return Optics.forgetOpt(paramPair -> ForgetOpt.unbox(paramApp2).run(paramPair.getFirst()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ForgetOpt.Mu<R>, A, B> paramApp2) {
/* 40 */     return Optics.forgetOpt(paramPair -> ForgetOpt.unbox(paramApp2).run(paramPair.getSecond()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetOpt.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ForgetOpt.Mu<R>, A, B> paramApp2) {
/* 45 */     return Optics.forgetOpt(paramEither -> {
/*    */           Objects.requireNonNull(ForgetOpt.unbox(paramApp2));
/*    */           return paramEither.left().flatMap(ForgetOpt.unbox(paramApp2)::run);
/*    */         });
/*    */   } public <A, B, C> App2<ForgetOpt.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ForgetOpt.Mu<R>, A, B> paramApp2) {
/* 50 */     return Optics.forgetOpt(paramEither -> {
/*    */           Objects.requireNonNull(ForgetOpt.unbox(paramApp2));
/*    */           return paramEither.right().flatMap(ForgetOpt.unbox(paramApp2)::run);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ForgetOpt$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */