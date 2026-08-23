/*     */ package com.mojang.datafixers.optics;
/*     */ 
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K2;
/*     */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.GetterP;
/*     */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ public abstract class Optics
/*     */ {
/*     */   public static <S, T, A, B> Adapter<S, T, A, B> toAdapter(Optic<? super Profunctor.Mu, S, T, A, B> paramOptic) {
/*  26 */     Function<App2<K2, A, B>, App2<K2, S, T>> function = paramOptic.eval((App)new Adapter.Instance<>());
/*  27 */     return Adapter.unbox((App2<Adapter.Mu<A, B>, S, T>)function.apply((App2)adapter(Function.identity(), (Function)Function.identity())));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Lens<S, T, A, B> toLens(Optic<? super Cartesian.Mu, S, T, A, B> paramOptic) {
/*  31 */     Function<App2<K2, A, B>, App2<K2, S, T>> function = paramOptic.eval((App)new Lens.Instance<>());
/*  32 */     return Lens.unbox((App2<Lens.Mu<A, B>, S, T>)function.apply((App2)lens(Function.identity(), (paramObject1, paramObject2) -> paramObject1)));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Prism<S, T, A, B> toPrism(Optic<? super Cocartesian.Mu, S, T, A, B> paramOptic) {
/*  36 */     Function<App2<K2, A, B>, App2<K2, S, T>> function = paramOptic.eval((App)new Prism.Instance<>());
/*  37 */     return Prism.unbox((App2<Prism.Mu<A, B>, S, T>)function.apply((App2)prism(Either::right, (Function)Function.identity())));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Affine<S, T, A, B> toAffine(Optic<? super AffineP.Mu, S, T, A, B> paramOptic) {
/*  41 */     Function<App2<K2, A, B>, App2<K2, S, T>> function = paramOptic.eval((App)new Affine.Instance<>());
/*  42 */     return Affine.unbox((App2<Affine.Mu<A, B>, S, T>)function.apply((App2)affine(Either::right, (paramObject1, paramObject2) -> paramObject1)));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Getter<S, T, A, B> toGetter(Optic<? super GetterP.Mu, S, T, A, B> paramOptic) {
/*  46 */     Function<App2<K2, A, B>, App2<K2, S, T>> function = paramOptic.eval((App)new Getter.Instance<>());
/*  47 */     return Getter.unbox((App2<Getter.Mu<A, B>, S, T>)function.apply((App2)getter(Function.identity())));
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Traversal<S, T, A, B> toTraversal(Optic<? super TraversalP.Mu, S, T, A, B> paramOptic) {
/*  51 */     Function<App2<K2, A, B>, App2<K2, S, T>> function = paramOptic.eval((App)new Traversal.Instance<>());
/*  52 */     return Traversal.unbox((App2<Traversal.Mu<A, B>, S, T>)function.apply((App2)new Traversal<A, B, A, B>()
/*     */           {
/*     */             public <F extends com.mojang.datafixers.kinds.K1> FunctionType<A, App<F, B>> wander(Applicative<F, ?> param1Applicative, FunctionType<A, App<F, B>> param1FunctionType) {
/*  55 */               return param1FunctionType;
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <S, T, A, B, F> Lens<S, T, Pair<F, A>, B> merge(Lens<S, ?, F, ?> paramLens, Lens<S, T, A, B> paramLens1) {
/*  63 */     Objects.requireNonNull(paramLens1); return lens(paramObject -> Pair.of(paramLens1.view(paramObject), paramLens2.view(paramObject)), paramLens1::update);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S, T> Adapter<S, T, S, T> id() {
/*  69 */     return (Adapter)IdAdapter.INSTANCE;
/*     */   }
/*     */   
/*     */   public static boolean isId(Optic<?, ?, ?, ?, ?> paramOptic) {
/*  73 */     return (paramOptic == IdAdapter.INSTANCE);
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Adapter<S, T, A, B> adapter(final Function<S, A> from, final Function<B, T> to) {
/*  77 */     return new Adapter<S, T, A, B>()
/*     */       {
/*     */         public A from(S param1S) {
/*  80 */           return from.apply(param1S);
/*     */         }
/*     */ 
/*     */         
/*     */         public T to(B param1B) {
/*  85 */           return to.apply(param1B);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Lens<S, T, A, B> lens(final Function<S, A> view, final BiFunction<B, S, T> update) {
/*  91 */     return new Lens<S, T, A, B>()
/*     */       {
/*     */         public A view(S param1S) {
/*  94 */           return view.apply(param1S);
/*     */         }
/*     */ 
/*     */         
/*     */         public T update(B param1B, S param1S) {
/*  99 */           return update.apply(param1B, param1S);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Prism<S, T, A, B> prism(final Function<S, Either<T, A>> match, final Function<B, T> build) {
/* 105 */     return new Prism<S, T, A, B>()
/*     */       {
/*     */         public Either<T, A> match(S param1S) {
/* 108 */           return match.apply(param1S);
/*     */         }
/*     */ 
/*     */         
/*     */         public T build(B param1B) {
/* 113 */           return build.apply(param1B);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Affine<S, T, A, B> affine(final Function<S, Either<T, A>> preview, final BiFunction<B, S, T> build) {
/* 119 */     return new Affine<S, T, A, B>()
/*     */       {
/*     */         public Either<T, A> preview(S param1S) {
/* 122 */           return preview.apply(param1S);
/*     */         }
/*     */ 
/*     */         
/*     */         public T set(B param1B, S param1S) {
/* 127 */           return build.apply(param1B, param1S);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Getter<S, T, A, B> getter(Function<S, A> paramFunction) {
/* 133 */     Objects.requireNonNull(paramFunction); return paramFunction::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> Forget<R, A, B> forget(Function<A, R> paramFunction) {
/* 137 */     Objects.requireNonNull(paramFunction); return paramFunction::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ForgetOpt<R, A, B> forgetOpt(Function<A, Optional<R>> paramFunction) {
/* 141 */     Objects.requireNonNull(paramFunction); return paramFunction::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ForgetE<R, A, B> forgetE(Function<A, Either<B, R>> paramFunction) {
/* 145 */     Objects.requireNonNull(paramFunction); return paramFunction::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForget<R, A, B> reForget(Function<R, B> paramFunction) {
/* 149 */     Objects.requireNonNull(paramFunction); return paramFunction::apply;
/*     */   }
/*     */   
/*     */   public static <S, T, A, B> Grate<S, T, A, B> grate(FunctionType<FunctionType<FunctionType<S, A>, B>, T> paramFunctionType) {
/* 153 */     Objects.requireNonNull(paramFunctionType); return paramFunctionType::apply;
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetEP<R, A, B> reForgetEP(final String name, final Function<Either<A, Pair<A, R>>, B> function) {
/* 157 */     return new ReForgetEP<R, A, B>()
/*     */       {
/*     */         public B run(Either<A, Pair<A, R>> param1Either) {
/* 160 */           return function.apply(param1Either);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 165 */           return "ReForgetEP_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetE<R, A, B> reForgetE(final String name, final Function<Either<A, R>, B> function) {
/* 171 */     return new ReForgetE<R, A, B>()
/*     */       {
/*     */         public B run(Either<A, R> param1Either) {
/* 174 */           return function.apply(param1Either);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 179 */           return "ReForgetE_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetP<R, A, B> reForgetP(final String name, final BiFunction<A, R, B> function) {
/* 185 */     return new ReForgetP<R, A, B>()
/*     */       {
/*     */         public B run(A param1A, R param1R) {
/* 188 */           return function.apply(param1A, param1R);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 193 */           return "ReForgetP_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <R, A, B> ReForgetC<R, A, B> reForgetC(final String name, final Either<Function<R, B>, BiFunction<A, R, B>> either) {
/* 199 */     return new ReForgetC<R, A, B>()
/*     */       {
/*     */         public Either<Function<R, B>, BiFunction<A, R, B>> impl() {
/* 202 */           return either;
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 207 */           return "ReForgetC_" + name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <I, J, X> PStore<I, J, X> pStore(final Function<J, X> peek, final Supplier<I> pos) {
/* 213 */     return new PStore<I, J, X>()
/*     */       {
/*     */         public X peek(J param1J) {
/* 216 */           return peek.apply(param1J);
/*     */         }
/*     */ 
/*     */         
/*     */         public I pos() {
/* 221 */           return pos.get();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <A, B> Function<A, B> getFunc(App2<FunctionType.Mu, A, B> paramApp2) {
/* 227 */     return FunctionType.unbox(paramApp2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, F2> Proj1<F, G, F2> proj1() {
/* 232 */     return (Proj1)Proj1.INSTANCE;
/*     */   }
/*     */   
/*     */   public static boolean isProj1(Optic<?, ?, ?, ?, ?> paramOptic) {
/* 236 */     return (paramOptic == Proj1.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, G2> Proj2<F, G, G2> proj2() {
/* 241 */     return (Proj2)Proj2.INSTANCE;
/*     */   }
/*     */   
/*     */   public static boolean isProj2(Optic<?, ?, ?, ?, ?> paramOptic) {
/* 245 */     return (paramOptic == Proj2.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, F2> Inj1<F, G, F2> inj1() {
/* 250 */     return (Inj1)Inj1.INSTANCE;
/*     */   }
/*     */   
/*     */   public static boolean isInj1(Optic<?, ?, ?, ?, ?> paramOptic) {
/* 254 */     return (paramOptic == Inj1.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, G2> Inj2<F, G, G2> inj2() {
/* 259 */     return (Inj2)Inj2.INSTANCE;
/*     */   }
/*     */   
/*     */   public static boolean isInj2(Optic<?, ?, ?, ?, ?> paramOptic) {
/* 263 */     return (paramOptic == Inj2.INSTANCE);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, G, F2, G2, A, B> Lens<Either<F, G>, Either<F2, G2>, A, B> eitherLens(Lens<F, F2, A, B> paramLens, Lens<G, G2, A, B> paramLens1) {
/* 292 */     return lens(paramEither -> {
/*     */           Objects.requireNonNull(paramLens1);
/*     */           Objects.requireNonNull(paramLens2);
/*     */           return paramEither.map(paramLens1::view, paramLens2::view);
/*     */         }(paramObject, paramEither) -> paramEither.mapBoth((), ()));
/*     */   }
/*     */   public static <F, G, F2, G2, A, B> Affine<Either<F, G>, Either<F2, G2>, A, B> eitherAffine(Affine<F, F2, A, B> paramAffine, Affine<G, G2, A, B> paramAffine1) {
/* 299 */     return affine(paramEither -> (Either)paramEither.map((), ()), (paramObject, paramEither) -> paramEither.mapBoth((), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, G, F2, G2, A, B> Traversal<Either<F, G>, Either<F2, G2>, A, B> eitherTraversal(final Traversal<F, F2, A, B> fOptic, final Traversal<G, G2, A, B> gOptic) {
/* 309 */     return new Traversal<Either<F, G>, Either<F2, G2>, A, B>()
/*     */       {
/*     */         public <FT extends com.mojang.datafixers.kinds.K1> FunctionType<Either<F, G>, App<FT, Either<F2, G2>>> wander(Applicative<FT, ?> param1Applicative, FunctionType<A, App<FT, B>> param1FunctionType) {
/* 312 */           return param1Either -> (App)param1Either.map((), ());
/*     */         }
/*     */       };
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
/*     */   public static <A, B> ListTraversal<A, B> listTraversal() {
/* 326 */     return (ListTraversal)ListTraversal.INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Optics.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */