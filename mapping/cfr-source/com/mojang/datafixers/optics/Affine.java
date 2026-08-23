/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;

public interface Affine<S, T, A, B>
extends App2<Mu<A, B>, S, T>,
Optic<AffineP.Mu, S, T, A, B> {
    public static <S, T, A, B> Affine<S, T, A, B> unbox(App2<Mu<A, B>, S, T> app2) {
        return (Affine)app2;
    }

    public Either<T, A> preview(S var1);

    public T set(B var1, S var2);

    @Override
    default public <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends AffineP.Mu, P> app) {
        Cartesian cartesian = Cartesian.unbox(app);
        Cocartesian cocartesian = Cocartesian.unbox(app);
        return app2 -> cartesian.dimap(cocartesian.left(cartesian.rmap(cartesian.first(app2), pair -> this.set(pair.getFirst(), pair.getSecond()))), object -> this.preview(object).map(Either::right, object2 -> Either.left(Pair.of(object2, object))), Either::unwrap);
    }

    public static final class Instance<A2, B2>
    implements AffineP<Mu<A2, B2>, AffineP.Mu> {
        @Override
        public <A, B, C, D> FunctionType<App2<Mu<A2, B2>, A, B>, App2<Mu<A2, B2>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.affine(object -> Affine.unbox(app2).preview(function.apply(object)).mapLeft(function2), (object, object2) -> function2.apply(Affine.unbox(app2).set(object, function.apply(object2))));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Mu<A2, B2>, A, B> app2) {
            Affine affine = Affine.unbox(app2);
            return Optics.affine(pair -> affine.preview(pair.getFirst()).mapBoth(object -> Pair.of(object, pair.getSecond()), Function.identity()), (object, pair) -> Pair.of(affine.set(object, pair.getFirst()), pair.getSecond()));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Mu<A2, B2>, A, B> app2) {
            Affine affine = Affine.unbox(app2);
            return Optics.affine(pair -> affine.preview(pair.getSecond()).mapBoth(object -> Pair.of(pair.getFirst(), object), Function.identity()), (object, pair) -> Pair.of(pair.getFirst(), affine.set(object, pair.getSecond())));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Mu<A2, B2>, A, B> app2) {
            Affine affine = Affine.unbox(app2);
            return Optics.affine(either -> either.map(object -> affine.preview(object).mapLeft(Either::left), object -> Either.left(Either.right(object))), (object, either) -> either.map(object2 -> Either.left(affine.set(object, object2)), Either::right));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Mu<A2, B2>, A, B> app2) {
            Affine affine = Affine.unbox(app2);
            return Optics.affine(either -> either.map(object -> Either.left(Either.left(object)), object -> affine.preview(object).mapLeft(Either::right)), (object, either) -> either.map(Either::left, object2 -> Either.right(affine.set(object, object2))));
        }
    }

    public static final class Mu<A, B>
    implements K2 {
    }
}

