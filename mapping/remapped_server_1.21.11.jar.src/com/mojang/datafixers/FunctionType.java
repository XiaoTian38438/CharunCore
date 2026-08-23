/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Functor;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.K2;
/*     */ import com.mojang.datafixers.kinds.Representable;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Procompose;
/*     */ import com.mojang.datafixers.optics.Wander;
/*     */ import com.mojang.datafixers.optics.profunctors.Mapping;
/*     */ import com.mojang.datafixers.optics.profunctors.MonoidProfunctor;
/*     */ import com.mojang.datafixers.optics.profunctors.Monoidal;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ public interface FunctionType<A, B>
/*     */   extends Function<A, B>, App2<FunctionType.Mu, A, B>, App<FunctionType.ReaderMu<A>, B> {
/*     */   public static final class Mu implements K2 {}
/*     */   
/*     */   public static final class ReaderMu<A> implements K1 {}
/*     */   
/*     */   static <A, B> FunctionType<A, B> create(Function<? super A, ? extends B> paramFunction) {
/*  33 */     Objects.requireNonNull(paramFunction); return paramFunction::apply;
/*     */   }
/*     */   
/*     */   static <A, B> Function<A, B> unbox(App2<Mu, A, B> paramApp2) {
/*  37 */     return (FunctionType)paramApp2;
/*     */   }
/*     */   
/*     */   static <A, B> Function<A, B> unbox(App<ReaderMu<A>, B> paramApp) {
/*  41 */     return (FunctionType)paramApp;
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   B apply(@Nonnull A paramA);
/*     */   
/*     */   public static final class ReaderInstance<R>
/*     */     implements Representable<ReaderMu<R>, R, ReaderInstance.Mu<R>> {
/*     */     public static final class Mu<A>
/*     */       implements Representable.Mu {}
/*     */     
/*     */     public <T, R2> App<FunctionType.ReaderMu<R>, R2> map(Function<? super T, ? extends R2> param1Function, App<FunctionType.ReaderMu<R>, T> param1App) {
/*  53 */       return FunctionType.create(param1Function.compose(FunctionType.unbox(param1App)));
/*     */     }
/*     */ 
/*     */     
/*     */     public <B> App<FunctionType.ReaderMu<R>, B> to(App<FunctionType.ReaderMu<R>, B> param1App) {
/*  58 */       return param1App;
/*     */     }
/*     */ 
/*     */     
/*     */     public <B> App<FunctionType.ReaderMu<R>, B> from(App<FunctionType.ReaderMu<R>, B> param1App) {
/*  63 */       return param1App;
/*     */     } }
/*     */   
/*     */   public static final class Mu<A> implements Representable.Mu {}
/*     */   
/*  68 */   public enum Instance implements TraversalP<Mu, Instance.Mu>, MonoidProfunctor<Mu, Instance.Mu>, Mapping<Mu, Instance.Mu>, Monoidal<Mu, Instance.Mu>, App<Instance.Mu, Mu> { INSTANCE;
/*     */     
/*     */     public static final class Mu implements TraversalP.Mu, MonoidProfunctor.Mu, Mapping.Mu, Monoidal.Mu {
/*  71 */       public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*     */         
/*     */         }; }
/*     */     
/*     */     public <A, B, C, D> FunctionType<App2<FunctionType.Mu, A, B>, App2<FunctionType.Mu, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/*  76 */       return param1App2 -> FunctionType.create(param1Function1.compose(Optics.getFunc(param1App2)).compose(param1Function2));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, C> App2<FunctionType.Mu, Pair<A, C>, Pair<B, C>> first(App2<FunctionType.Mu, A, B> param1App2) {
/*  81 */       return FunctionType.create(param1Pair -> Pair.of(Optics.getFunc(param1App2).apply(param1Pair.getFirst()), param1Pair.getSecond()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, C> App2<FunctionType.Mu, Pair<C, A>, Pair<C, B>> second(App2<FunctionType.Mu, A, B> param1App2) {
/*  86 */       return FunctionType.create(param1Pair -> Pair.of(param1Pair.getFirst(), Optics.getFunc(param1App2).apply(param1Pair.getSecond())));
/*     */     }
/*     */ 
/*     */     
/*     */     public <S, T, A, B> App2<FunctionType.Mu, S, T> wander(Wander<S, T, A, B> param1Wander, App2<FunctionType.Mu, A, B> param1App2) {
/*  91 */       return FunctionType.create(param1Object -> IdF.get(param1Wander.wander((Applicative)IdF.Instance.INSTANCE, ()).apply(param1Object)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, B, C> App2<FunctionType.Mu, Either<A, C>, Either<B, C>> left(App2<FunctionType.Mu, A, B> param1App2) {
/*  99 */       return FunctionType.create(param1Either -> param1Either.mapLeft(Optics.getFunc(param1App2)));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, C> App2<FunctionType.Mu, Either<C, A>, Either<C, B>> right(App2<FunctionType.Mu, A, B> param1App2) {
/* 104 */       return FunctionType.create(param1Either -> param1Either.mapRight(Optics.getFunc(param1App2)));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, C, D> App2<FunctionType.Mu, Pair<A, C>, Pair<B, D>> par(App2<FunctionType.Mu, A, B> param1App2, Supplier<App2<FunctionType.Mu, C, D>> param1Supplier) {
/* 109 */       return FunctionType.create(param1Pair -> Pair.of(Optics.getFunc(param1App2).apply(param1Pair.getFirst()), Optics.getFunc(param1Supplier.get()).apply(param1Pair.getSecond())));
/*     */     }
/*     */ 
/*     */     
/*     */     public App2<FunctionType.Mu, Void, Void> empty() {
/* 114 */       return FunctionType.create((Function)Function.identity());
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B> App2<FunctionType.Mu, A, B> zero(App2<FunctionType.Mu, A, B> param1App2) {
/* 119 */       return param1App2;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B> App2<FunctionType.Mu, A, B> plus(App2<Procompose.Mu<FunctionType.Mu, FunctionType.Mu>, A, B> param1App2) {
/* 124 */       Procompose<FunctionType.Mu, FunctionType.Mu, A, B, ?> procompose = Procompose.unbox(param1App2);
/* 125 */       return cap(procompose);
/*     */     }
/*     */     
/*     */     private <A, B, C> App2<FunctionType.Mu, A, B> cap(Procompose<FunctionType.Mu, FunctionType.Mu, A, B, C> param1Procompose) {
/* 129 */       return FunctionType.create(Optics.getFunc(param1Procompose.second()).compose(Optics.getFunc(param1Procompose.first().get())));
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, F extends K1> App2<FunctionType.Mu, App<F, A>, App<F, B>> mapping(Functor<F, ?> param1Functor, App2<FunctionType.Mu, A, B> param1App2) {
/* 134 */       return FunctionType.create(param1App -> param1Functor.map(Optics.getFunc(param1App2), param1App));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static final class Mu implements TraversalP.Mu, MonoidProfunctor.Mu, Mapping.Mu, Monoidal.Mu {
/*     */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*     */       
/*     */       };
/*     */   }
/*     */   
/*     */   class null extends TypeToken<Instance.Mu> {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\FunctionType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */