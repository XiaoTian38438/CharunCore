/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface ForgetOpt<R, A, B>
/*    */   extends App2<ForgetOpt.Mu<R>, A, B> {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ForgetOpt<R, A, B> unbox(App2<Mu<R>, A, B> paramApp2) {
/* 20 */     return (ForgetOpt)paramApp2;
/*    */   }
/*    */   
/*    */   Optional<R> run(A paramA);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements AffineP<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     public static final class Mu<R> implements AffineP.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 30 */       return param1App2 -> Optics.forgetOpt(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ForgetOpt.Mu<R>, A, B> param1App2) {
/* 35 */       return Optics.forgetOpt(param1Pair -> ForgetOpt.unbox(param1App2).run(param1Pair.getFirst()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ForgetOpt.Mu<R>, A, B> param1App2) {
/* 40 */       return Optics.forgetOpt(param1Pair -> ForgetOpt.unbox(param1App2).run(param1Pair.getSecond()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ForgetOpt.Mu<R>, A, B> param1App2) {
/* 45 */       return Optics.forgetOpt(param1Either -> {
/*    */             Objects.requireNonNull(ForgetOpt.unbox(param1App2));
/*    */             return param1Either.left().flatMap(ForgetOpt.unbox(param1App2)::run);
/*    */           });
/*    */     } public <A, B, C> App2<ForgetOpt.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ForgetOpt.Mu<R>, A, B> param1App2) {
/* 50 */       return Optics.forgetOpt(param1Either -> {
/*    */             Objects.requireNonNull(ForgetOpt.unbox(param1App2));
/*    */             return param1Either.right().flatMap(ForgetOpt.unbox(param1App2)::run);
/*    */           });
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu<R> implements AffineP.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ForgetOpt.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */