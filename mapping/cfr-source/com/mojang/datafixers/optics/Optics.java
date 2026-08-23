/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Adapter;
import com.mojang.datafixers.optics.Affine;
import com.mojang.datafixers.optics.Forget;
import com.mojang.datafixers.optics.ForgetE;
import com.mojang.datafixers.optics.ForgetOpt;
import com.mojang.datafixers.optics.Getter;
import com.mojang.datafixers.optics.Grate;
import com.mojang.datafixers.optics.IdAdapter;
import com.mojang.datafixers.optics.Inj1;
import com.mojang.datafixers.optics.Inj2;
import com.mojang.datafixers.optics.Lens;
import com.mojang.datafixers.optics.ListTraversal;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.PStore;
import com.mojang.datafixers.optics.Prism;
import com.mojang.datafixers.optics.Proj1;
import com.mojang.datafixers.optics.Proj2;
import com.mojang.datafixers.optics.ReForget;
import com.mojang.datafixers.optics.ReForgetC;
import com.mojang.datafixers.optics.ReForgetE;
import com.mojang.datafixers.optics.ReForgetEP;
import com.mojang.datafixers.optics.ReForgetP;
import com.mojang.datafixers.optics.Traversal;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.optics.profunctors.GetterP;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Optics {
    public static <S, T, A, B> Adapter<S, T, A, B> toAdapter(Optic<? super Profunctor.Mu, S, T, A, B> optic) {
        Function function = optic.eval(new Adapter.Instance());
        return Adapter.unbox(function.apply(Optics.adapter(Function.identity(), Function.identity())));
    }

    public static <S, T, A, B> Lens<S, T, A, B> toLens(Optic<? super Cartesian.Mu, S, T, A, B> optic) {
        Function function = optic.eval(new Lens.Instance());
        return Lens.unbox(function.apply(Optics.lens(Function.identity(), (object, object2) -> object)));
    }

    public static <S, T, A, B> Prism<S, T, A, B> toPrism(Optic<? super Cocartesian.Mu, S, T, A, B> optic) {
        Function function = optic.eval(new Prism.Instance());
        return Prism.unbox(function.apply(Optics.prism(Either::right, Function.identity())));
    }

    public static <S, T, A, B> Affine<S, T, A, B> toAffine(Optic<? super AffineP.Mu, S, T, A, B> optic) {
        Function function = optic.eval(new Affine.Instance());
        return Affine.unbox(function.apply(Optics.affine(Either::right, (object, object2) -> object)));
    }

    public static <S, T, A, B> Getter<S, T, A, B> toGetter(Optic<? super GetterP.Mu, S, T, A, B> optic) {
        Function function = optic.eval(new Getter.Instance());
        return Getter.unbox(function.apply(Optics.getter(Function.identity())));
    }

    public static <S, T, A, B> Traversal<S, T, A, B> toTraversal(Optic<? super TraversalP.Mu, S, T, A, B> optic) {
        Function function = optic.eval(new Traversal.Instance());
        return Traversal.unbox(function.apply(new Traversal<A, B, A, B>(){

            @Override
            public <F extends K1> FunctionType<A, App<F, B>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> functionType) {
                return functionType;
            }
        }));
    }

    static <S, T, A, B, F> Lens<S, T, Pair<F, A>, B> merge(Lens<S, ?, F, ?> lens, Lens<S, T, A, B> lens2) {
        return Optics.lens(object -> Pair.of(lens.view(object), lens2.view(object)), lens2::update);
    }

    public static <S, T> Adapter<S, T, S, T> id() {
        return IdAdapter.INSTANCE;
    }

    public static boolean isId(Optic<?, ?, ?, ?, ?> optic) {
        return optic == IdAdapter.INSTANCE;
    }

    public static <S, T, A, B> Adapter<S, T, A, B> adapter(final Function<S, A> function, final Function<B, T> function2) {
        return new Adapter<S, T, A, B>(){

            @Override
            public A from(S s) {
                return function.apply(s);
            }

            @Override
            public T to(B b) {
                return function2.apply(b);
            }
        };
    }

    public static <S, T, A, B> Lens<S, T, A, B> lens(final Function<S, A> function, final BiFunction<B, S, T> biFunction) {
        return new Lens<S, T, A, B>(){

            @Override
            public A view(S s) {
                return function.apply(s);
            }

            @Override
            public T update(B b, S s) {
                return biFunction.apply(b, s);
            }
        };
    }

    public static <S, T, A, B> Prism<S, T, A, B> prism(final Function<S, Either<T, A>> function, final Function<B, T> function2) {
        return new Prism<S, T, A, B>(){

            @Override
            public Either<T, A> match(S s) {
                return (Either)function.apply(s);
            }

            @Override
            public T build(B b) {
                return function2.apply(b);
            }
        };
    }

    public static <S, T, A, B> Affine<S, T, A, B> affine(final Function<S, Either<T, A>> function, final BiFunction<B, S, T> biFunction) {
        return new Affine<S, T, A, B>(){

            @Override
            public Either<T, A> preview(S s) {
                return (Either)function.apply(s);
            }

            @Override
            public T set(B b, S s) {
                return biFunction.apply(b, s);
            }
        };
    }

    public static <S, T, A, B> Getter<S, T, A, B> getter(Function<S, A> function) {
        return function::apply;
    }

    public static <R, A, B> Forget<R, A, B> forget(Function<A, R> function) {
        return function::apply;
    }

    public static <R, A, B> ForgetOpt<R, A, B> forgetOpt(Function<A, Optional<R>> function) {
        return function::apply;
    }

    public static <R, A, B> ForgetE<R, A, B> forgetE(Function<A, Either<B, R>> function) {
        return function::apply;
    }

    public static <R, A, B> ReForget<R, A, B> reForget(Function<R, B> function) {
        return function::apply;
    }

    public static <S, T, A, B> Grate<S, T, A, B> grate(FunctionType<FunctionType<FunctionType<S, A>, B>, T> functionType) {
        return functionType::apply;
    }

    public static <R, A, B> ReForgetEP<R, A, B> reForgetEP(final String string, final Function<Either<A, Pair<A, R>>, B> function) {
        return new ReForgetEP<R, A, B>(){

            @Override
            public B run(Either<A, Pair<A, R>> either) {
                return function.apply(either);
            }

            public String toString() {
                return "ReForgetEP_" + string;
            }
        };
    }

    public static <R, A, B> ReForgetE<R, A, B> reForgetE(final String string, final Function<Either<A, R>, B> function) {
        return new ReForgetE<R, A, B>(){

            @Override
            public B run(Either<A, R> either) {
                return function.apply(either);
            }

            public String toString() {
                return "ReForgetE_" + string;
            }
        };
    }

    public static <R, A, B> ReForgetP<R, A, B> reForgetP(final String string, final BiFunction<A, R, B> biFunction) {
        return new ReForgetP<R, A, B>(){

            @Override
            public B run(A a, R r) {
                return biFunction.apply(a, r);
            }

            public String toString() {
                return "ReForgetP_" + string;
            }
        };
    }

    public static <R, A, B> ReForgetC<R, A, B> reForgetC(final String string, final Either<Function<R, B>, BiFunction<A, R, B>> either) {
        return new ReForgetC<R, A, B>(){

            @Override
            public Either<Function<R, B>, BiFunction<A, R, B>> impl() {
                return either;
            }

            public String toString() {
                return "ReForgetC_" + string;
            }
        };
    }

    public static <I, J, X> PStore<I, J, X> pStore(final Function<J, X> function, final Supplier<I> supplier) {
        return new PStore<I, J, X>(){

            @Override
            public X peek(J j) {
                return function.apply(j);
            }

            @Override
            public I pos() {
                return supplier.get();
            }
        };
    }

    public static <A, B> Function<A, B> getFunc(App2<FunctionType.Mu, A, B> app2) {
        return FunctionType.unbox(app2);
    }

    public static <F, G, F2> Proj1<F, G, F2> proj1() {
        return Proj1.INSTANCE;
    }

    public static boolean isProj1(Optic<?, ?, ?, ?, ?> optic) {
        return optic == Proj1.INSTANCE;
    }

    public static <F, G, G2> Proj2<F, G, G2> proj2() {
        return Proj2.INSTANCE;
    }

    public static boolean isProj2(Optic<?, ?, ?, ?, ?> optic) {
        return optic == Proj2.INSTANCE;
    }

    public static <F, G, F2> Inj1<F, G, F2> inj1() {
        return Inj1.INSTANCE;
    }

    public static boolean isInj1(Optic<?, ?, ?, ?, ?> optic) {
        return optic == Inj1.INSTANCE;
    }

    public static <F, G, G2> Inj2<F, G, G2> inj2() {
        return Inj2.INSTANCE;
    }

    public static boolean isInj2(Optic<?, ?, ?, ?, ?> optic) {
        return optic == Inj2.INSTANCE;
    }

    public static <F, G, F2, G2, A, B> Lens<Either<F, G>, Either<F2, G2>, A, B> eitherLens(Lens<F, F2, A, B> lens, Lens<G, G2, A, B> lens2) {
        return Optics.lens(either -> either.map(lens::view, lens2::view), (object, either) -> either.mapBoth(object2 -> lens.update(object, object2), object2 -> lens2.update(object, object2)));
    }

    public static <F, G, F2, G2, A, B> Affine<Either<F, G>, Either<F2, G2>, A, B> eitherAffine(Affine<F, F2, A, B> affine, Affine<G, G2, A, B> affine2) {
        return Optics.affine(either -> either.map(object -> affine.preview(object).mapLeft(Either::left), object -> affine2.preview(object).mapLeft(Either::right)), (object, either) -> either.mapBoth(object2 -> affine.set(object, object2), object2 -> affine2.set(object, object2)));
    }

    public static <F, G, F2, G2, A, B> Traversal<Either<F, G>, Either<F2, G2>, A, B> eitherTraversal(final Traversal<F, F2, A, B> traversal, final Traversal<G, G2, A, B> traversal2) {
        return new Traversal<Either<F, G>, Either<F2, G2>, A, B>(){

            @Override
            public <FT extends K1> FunctionType<Either<F, G>, App<FT, Either<F2, G2>>> wander(Applicative<FT, ?> applicative, FunctionType<A, App<FT, B>> functionType) {
                return either -> either.map(object -> applicative.ap(Either::left, traversal.wander(applicative, functionType).apply(object)), object -> applicative.ap(Either::right, traversal2.wander(applicative, functionType).apply(object)));
            }
        };
    }

    public static <A, B> ListTraversal<A, B> listTraversal() {
        return ListTraversal.INSTANCE;
    }
}

