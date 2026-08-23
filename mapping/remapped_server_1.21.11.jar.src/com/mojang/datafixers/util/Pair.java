/*     */ package com.mojang.datafixers.util;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.CartesianLike;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.Traversable;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class Pair<F, S>
/*     */   implements App<Pair.Mu<S>, F>
/*     */ {
/*     */   private final F first;
/*     */   
/*     */   public static <F, S> Pair<F, S> unbox(App<Mu<S>, F> paramApp) {
/*  21 */     return (Pair)paramApp;
/*     */   }
/*     */   private final S second;
/*     */   
/*     */   public static final class Mu<S> implements K1 {}
/*     */   
/*     */   public Pair(F paramF, S paramS) {
/*  28 */     this.first = paramF;
/*  29 */     this.second = paramS;
/*     */   }
/*     */   
/*     */   public F getFirst() {
/*  33 */     return this.first;
/*     */   }
/*     */   
/*     */   public S getSecond() {
/*  37 */     return this.second;
/*     */   }
/*     */   
/*     */   public Pair<S, F> swap() {
/*  41 */     return of(this.second, this.first);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  46 */     return "(" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  51 */     if (!(paramObject instanceof Pair)) {
/*  52 */       return false;
/*     */     }
/*  54 */     Pair pair = (Pair)paramObject;
/*  55 */     return (Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  60 */     return Objects.hashCode(new Object[] { this.first, this.second });
/*     */   }
/*     */   
/*     */   public <F2> Pair<F2, S> mapFirst(Function<? super F, ? extends F2> paramFunction) {
/*  64 */     return of(paramFunction.apply(this.first), this.second);
/*     */   }
/*     */   
/*     */   public <S2> Pair<F, S2> mapSecond(Function<? super S, ? extends S2> paramFunction) {
/*  68 */     return of(this.first, paramFunction.apply(this.second));
/*     */   }
/*     */   
/*     */   public static <F, S> Pair<F, S> of(F paramF, S paramS) {
/*  72 */     return new Pair<>(paramF, paramS);
/*     */   }
/*     */   
/*     */   public static <F, S> Collector<Pair<F, S>, ?, Map<F, S>> toMap() {
/*  76 */     return Collectors.toMap(Pair::getFirst, Pair::getSecond);
/*     */   }
/*     */   
/*     */   public static final class Instance<S2>
/*     */     implements Traversable<Mu<S2>, Instance.Mu<S2>>, CartesianLike<Mu<S2>, S2, Instance.Mu<S2>> {
/*     */     public static final class Mu<S2> implements Traversable.Mu, CartesianLike.Mu {}
/*     */     
/*     */     public <T, R> App<Pair.Mu<S2>, R> map(Function<? super T, ? extends R> param1Function, App<Pair.Mu<S2>, T> param1App) {
/*  84 */       return Pair.<T, S2>unbox(param1App).mapFirst(param1Function);
/*     */     }
/*     */ 
/*     */     
/*     */     public <F extends K1, A, B> App<F, App<Pair.Mu<S2>, B>> traverse(Applicative<F, ?> param1Applicative, Function<A, App<F, B>> param1Function, App<Pair.Mu<S2>, A> param1App) {
/*  89 */       Pair<A, S2> pair = Pair.unbox(param1App);
/*  90 */       return param1Applicative.ap(param1Object -> Pair.of(param1Object, param1Pair.second), param1Function.apply((A)pair.first));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<Pair.Mu<S2>, A> to(App<Pair.Mu<S2>, A> param1App) {
/*  95 */       return param1App;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<Pair.Mu<S2>, A> from(App<Pair.Mu<S2>, A> param1App) {
/* 100 */       return param1App;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Mu<S2> implements Traversable.Mu, CartesianLike.Mu {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Pair.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */