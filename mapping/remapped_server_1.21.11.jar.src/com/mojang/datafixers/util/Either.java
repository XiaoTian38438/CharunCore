/*     */ package com.mojang.datafixers.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.CocartesianLike;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.Traversable;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public abstract class Either<L, R>
/*     */   implements App<Either.Mu<R>, L>
/*     */ {
/*     */   public static final class Mu<R>
/*     */     implements K1 {}
/*     */   
/*     */   public static <L, R> Either<L, R> unbox(App<Mu<R>, L> paramApp) {
/*  21 */     return (Either)paramApp;
/*     */   }
/*     */   
/*     */   private static final class Left<L, R> extends Either<L, R> {
/*     */     private final L value;
/*     */     
/*     */     public Left(L param1L) {
/*  28 */       this.value = param1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> param1Function, Function<? super R, ? extends D> param1Function1) {
/*  33 */       return new Left((L)param1Function.apply(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T map(Function<? super L, ? extends T> param1Function, Function<? super R, ? extends T> param1Function1) {
/*  38 */       return param1Function.apply(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifLeft(Consumer<? super L> param1Consumer) {
/*  43 */       param1Consumer.accept(this.value);
/*  44 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifRight(Consumer<? super R> param1Consumer) {
/*  49 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<L> left() {
/*  54 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> right() {
/*  59 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  64 */       return "Left[" + String.valueOf(this.value) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/*  69 */       if (this == param1Object) {
/*  70 */         return true;
/*     */       }
/*  72 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/*  73 */         return false;
/*     */       }
/*  75 */       Left left = (Left)param1Object;
/*  76 */       return Objects.equals(this.value, left.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  81 */       return this.value.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Right<L, R> extends Either<L, R> {
/*     */     private final R value;
/*     */     
/*     */     public Right(R param1R) {
/*  89 */       this.value = param1R;
/*     */     }
/*     */ 
/*     */     
/*     */     public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> param1Function, Function<? super R, ? extends D> param1Function1) {
/*  94 */       return new Right((R)param1Function1.apply(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T map(Function<? super L, ? extends T> param1Function, Function<? super R, ? extends T> param1Function1) {
/*  99 */       return param1Function1.apply(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifLeft(Consumer<? super L> param1Consumer) {
/* 104 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<L, R> ifRight(Consumer<? super R> param1Consumer) {
/* 109 */       param1Consumer.accept(this.value);
/* 110 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<L> left() {
/* 115 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<R> right() {
/* 120 */       return Optional.of(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 125 */       return "Right[" + String.valueOf(this.value) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 130 */       if (this == param1Object) {
/* 131 */         return true;
/*     */       }
/* 133 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/* 134 */         return false;
/*     */       }
/* 136 */       Right right = (Right)param1Object;
/* 137 */       return Objects.equals(this.value, right.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 142 */       return this.value.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> paramFunction) {
/* 162 */     return map(paramObject -> left(paramFunction.apply(paramObject)), Either::right);
/*     */   }
/*     */   
/*     */   public <T> Either<L, T> mapRight(Function<? super R, ? extends T> paramFunction) {
/* 166 */     return map(Either::left, paramObject -> right(paramFunction.apply(paramObject)));
/*     */   }
/*     */   
/*     */   public static <L, R> Either<L, R> left(L paramL) {
/* 170 */     return new Left<>(paramL);
/*     */   }
/*     */   
/*     */   public static <L, R> Either<L, R> right(R paramR) {
/* 174 */     return new Right<>(paramR);
/*     */   }
/*     */   
/*     */   public L orThrow() {
/* 178 */     return map(paramObject -> paramObject, paramObject -> {
/*     */           if (paramObject instanceof Throwable) {
/*     */             throw new RuntimeException((Throwable)paramObject);
/*     */           }
/*     */           throw new RuntimeException(paramObject.toString());
/*     */         });
/*     */   }
/*     */   
/*     */   public Either<R, L> swap() {
/* 187 */     return map(Either::right, Either::left);
/*     */   }
/*     */   
/*     */   public <L2> Either<L2, R> flatMap(Function<L, Either<L2, R>> paramFunction) {
/* 191 */     return map(paramFunction, Either::right);
/*     */   } public abstract <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> paramFunction, Function<? super R, ? extends D> paramFunction1); public abstract <T> T map(Function<? super L, ? extends T> paramFunction, Function<? super R, ? extends T> paramFunction1);
/*     */   public abstract Either<L, R> ifLeft(Consumer<? super L> paramConsumer);
/*     */   public static <U> U unwrap(Either<? extends U, ? extends U> paramEither) {
/* 195 */     return paramEither.map((Function)Function.identity(), (Function)Function.identity());
/*     */   }
/*     */   public abstract Either<L, R> ifRight(Consumer<? super R> paramConsumer);
/*     */   public abstract Optional<L> left();
/*     */   
/*     */   public abstract Optional<R> right();
/*     */   
/*     */   public static final class Instance<R2> implements Applicative<Mu<R2>, Instance.Mu<R2>>, Traversable<Mu<R2>, Instance.Mu<R2>>, CocartesianLike<Mu<R2>, R2, Instance.Mu<R2>> { public <T, R> App<Either.Mu<R2>, R> map(Function<? super T, ? extends R> param1Function, App<Either.Mu<R2>, T> param1App) {
/* 203 */       return Either.<T, R2>unbox(param1App).mapLeft(param1Function);
/*     */     }
/*     */     public static final class Mu<R2> implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {}
/*     */     
/*     */     public <A> App<Either.Mu<R2>, A> point(A param1A) {
/* 208 */       return Either.left(param1A);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>> lift1(App<Either.Mu<R2>, Function<A, R>> param1App) {
/* 213 */       return param1App2 -> Either.unbox(param1App1).flatMap(());
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, B, R> BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>> lift2(App<Either.Mu<R2>, BiFunction<A, B, R>> param1App) {
/* 218 */       return (param1App2, param1App3) -> Either.unbox(param1App1).flatMap(());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <F extends K1, A, B> App<F, App<Either.Mu<R2>, B>> traverse(Applicative<F, ?> param1Applicative, Function<A, App<F, B>> param1Function, App<Either.Mu<R2>, A> param1App) {
/* 229 */       return (App<F, App<Either.Mu<R2>, B>>)Either.<A, R2>unbox(param1App).map(param1Object -> {
/*     */             App app = param1Function.apply(param1Object);
/*     */             return param1Applicative.ap(Either::left, app);
/*     */           }param1Object -> param1Applicative.point(Either.right(param1Object)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A> App<Either.Mu<R2>, A> to(App<Either.Mu<R2>, A> param1App) {
/* 240 */       return param1App;
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<Either.Mu<R2>, A> from(App<Either.Mu<R2>, A> param1App) {
/* 245 */       return param1App;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static final class Mu<R2> implements Applicative.Mu, Traversable.Mu, CocartesianLike.Mu {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Either.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */