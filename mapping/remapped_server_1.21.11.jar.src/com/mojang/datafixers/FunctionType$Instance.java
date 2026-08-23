/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Functor;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Procompose;
/*     */ import com.mojang.datafixers.optics.Wander;
/*     */ import com.mojang.datafixers.optics.profunctors.Mapping;
/*     */ import com.mojang.datafixers.optics.profunctors.MonoidProfunctor;
/*     */ import com.mojang.datafixers.optics.profunctors.Monoidal;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ public enum Instance
/*     */   implements TraversalP<FunctionType.Mu, FunctionType.Instance.Mu>, MonoidProfunctor<FunctionType.Mu, FunctionType.Instance.Mu>, Mapping<FunctionType.Mu, FunctionType.Instance.Mu>, Monoidal<FunctionType.Mu, FunctionType.Instance.Mu>, App<FunctionType.Instance.Mu, FunctionType.Mu>
/*     */ {
/*  68 */   INSTANCE;
/*     */   
/*     */   public static final class Mu implements TraversalP.Mu, MonoidProfunctor.Mu, Mapping.Mu, Monoidal.Mu {
/*  71 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*     */       
/*     */       }; }
/*     */   
/*     */   public <A, B, C, D> FunctionType<App2<FunctionType.Mu, A, B>, App2<FunctionType.Mu, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/*  76 */     return paramApp2 -> FunctionType.create(paramFunction1.compose(Optics.getFunc(paramApp2)).compose(paramFunction2));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, C> App2<FunctionType.Mu, Pair<A, C>, Pair<B, C>> first(App2<FunctionType.Mu, A, B> paramApp2) {
/*  81 */     return FunctionType.create(paramPair -> Pair.of(Optics.getFunc(paramApp2).apply(paramPair.getFirst()), paramPair.getSecond()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, C> App2<FunctionType.Mu, Pair<C, A>, Pair<C, B>> second(App2<FunctionType.Mu, A, B> paramApp2) {
/*  86 */     return FunctionType.create(paramPair -> Pair.of(paramPair.getFirst(), Optics.getFunc(paramApp2).apply(paramPair.getSecond())));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S, T, A, B> App2<FunctionType.Mu, S, T> wander(Wander<S, T, A, B> paramWander, App2<FunctionType.Mu, A, B> paramApp2) {
/*  91 */     return FunctionType.create(paramObject -> IdF.get(paramWander.wander((Applicative)IdF.Instance.INSTANCE, ()).apply(paramObject)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, B, C> App2<FunctionType.Mu, Either<A, C>, Either<B, C>> left(App2<FunctionType.Mu, A, B> paramApp2) {
/*  99 */     return FunctionType.create(paramEither -> paramEither.mapLeft(Optics.getFunc(paramApp2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, C> App2<FunctionType.Mu, Either<C, A>, Either<C, B>> right(App2<FunctionType.Mu, A, B> paramApp2) {
/* 104 */     return FunctionType.create(paramEither -> paramEither.mapRight(Optics.getFunc(paramApp2)));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, C, D> App2<FunctionType.Mu, Pair<A, C>, Pair<B, D>> par(App2<FunctionType.Mu, A, B> paramApp2, Supplier<App2<FunctionType.Mu, C, D>> paramSupplier) {
/* 109 */     return FunctionType.create(paramPair -> Pair.of(Optics.getFunc(paramApp2).apply(paramPair.getFirst()), Optics.getFunc(paramSupplier.get()).apply(paramPair.getSecond())));
/*     */   }
/*     */ 
/*     */   
/*     */   public App2<FunctionType.Mu, Void, Void> empty() {
/* 114 */     return FunctionType.create((Function)Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> App2<FunctionType.Mu, A, B> zero(App2<FunctionType.Mu, A, B> paramApp2) {
/* 119 */     return paramApp2;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> App2<FunctionType.Mu, A, B> plus(App2<Procompose.Mu<FunctionType.Mu, FunctionType.Mu>, A, B> paramApp2) {
/* 124 */     Procompose<FunctionType.Mu, FunctionType.Mu, A, B, ?> procompose = Procompose.unbox(paramApp2);
/* 125 */     return cap(procompose);
/*     */   }
/*     */   
/*     */   private <A, B, C> App2<FunctionType.Mu, A, B> cap(Procompose<FunctionType.Mu, FunctionType.Mu, A, B, C> paramProcompose) {
/* 129 */     return FunctionType.create(Optics.getFunc(paramProcompose.second()).compose(Optics.getFunc(paramProcompose.first().get())));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<FunctionType.Mu, App<F, A>, App<F, B>> mapping(Functor<F, ?> paramFunctor, App2<FunctionType.Mu, A, B> paramApp2) {
/* 134 */     return FunctionType.create(paramApp -> paramFunctor.map(Optics.getFunc(paramApp2), paramApp));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\FunctionType$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */