/*     */ package com.mojang.datafixers.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.CocartesianLike;
/*     */ import com.mojang.datafixers.kinds.Traversable;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Instance<R2>
/*     */   implements Applicative<Either.Mu<R2>, Either.Instance.Mu<R2>>, Traversable<Either.Mu<R2>, Either.Instance.Mu<R2>>, CocartesianLike<Either.Mu<R2>, R2, Either.Instance.Mu<R2>>
/*     */ {
/*     */   public static final class Mu<R2>
/*     */     implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {}
/*     */   
/*     */   public <T, R> App<Either.Mu<R2>, R> map(Function<? super T, ? extends R> paramFunction, App<Either.Mu<R2>, T> paramApp) {
/* 203 */     return Either.<T, R2>unbox(paramApp).mapLeft(paramFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> App<Either.Mu<R2>, A> point(A paramA) {
/* 208 */     return Either.left(paramA);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, R> Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>> lift1(App<Either.Mu<R2>, Function<A, R>> paramApp) {
/* 213 */     return paramApp2 -> Either.unbox(paramApp1).flatMap(());
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, R> BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>> lift2(App<Either.Mu<R2>, BiFunction<A, B, R>> paramApp) {
/* 218 */     return (paramApp2, paramApp3) -> Either.unbox(paramApp1).flatMap(());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <F extends com.mojang.datafixers.kinds.K1, A, B> App<F, App<Either.Mu<R2>, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<Either.Mu<R2>, A> paramApp) {
/* 229 */     return (App<F, App<Either.Mu<R2>, B>>)Either.<A, R2>unbox(paramApp).map(paramObject -> {
/*     */           App app = paramFunction.apply(paramObject);
/*     */           return paramApplicative.ap(Either::left, app);
/*     */         }paramObject -> paramApplicative.point(Either.right(paramObject)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> App<Either.Mu<R2>, A> to(App<Either.Mu<R2>, A> paramApp) {
/* 240 */     return paramApp;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> App<Either.Mu<R2>, A> from(App<Either.Mu<R2>, A> paramApp) {
/* 245 */     return paramApp;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Either$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */