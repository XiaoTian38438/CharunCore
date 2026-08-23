/*     */ package com.mojang.datafixers.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.CartesianLike;
/*     */ import com.mojang.datafixers.kinds.Traversable;
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
/*     */ public final class Instance<S2>
/*     */   implements Traversable<Pair.Mu<S2>, Pair.Instance.Mu<S2>>, CartesianLike<Pair.Mu<S2>, S2, Pair.Instance.Mu<S2>>
/*     */ {
/*     */   public static final class Mu<S2>
/*     */     implements Traversable.Mu, CartesianLike.Mu {}
/*     */   
/*     */   public <T, R> App<Pair.Mu<S2>, R> map(Function<? super T, ? extends R> paramFunction, App<Pair.Mu<S2>, T> paramApp) {
/*  84 */     return Pair.<T, S2>unbox(paramApp).mapFirst(paramFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   public <F extends com.mojang.datafixers.kinds.K1, A, B> App<F, App<Pair.Mu<S2>, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<Pair.Mu<S2>, A> paramApp) {
/*  89 */     Pair<A, S2> pair = Pair.unbox(paramApp);
/*  90 */     return paramApplicative.ap(paramObject -> Pair.of(paramObject, paramPair.second), paramFunction.apply((A)pair.first));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> App<Pair.Mu<S2>, A> to(App<Pair.Mu<S2>, A> paramApp) {
/*  95 */     return paramApp;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> App<Pair.Mu<S2>, A> from(App<Pair.Mu<S2>, A> paramApp) {
/* 100 */     return paramApp;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Pair$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */