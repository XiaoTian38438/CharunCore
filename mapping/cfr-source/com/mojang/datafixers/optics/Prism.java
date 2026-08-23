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
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.util.Either;
import java.util.function.Function;

public interface Prism<S, T, A, B>
extends App2<Mu<A, B>, S, T>,
Optic<Cocartesian.Mu, S, T, A, B> {
    public static <S, T, A, B> Prism<S, T, A, B> unbox(App2<Mu<A, B>, S, T> app2) {
        return (Prism)app2;
    }

    public Either<T, A> match(S var1);

    public T build(B var1);

    @Override
    default public <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Cocartesian.Mu, P> app) {
        Cocartesian cocartesian = Cocartesian.unbox(app);
        return app2 -> cocartesian.dimap(cocartesian.right(app2), this::match, either -> either.map(Function.identity(), this::build));
    }

    public static final class Instance<A2, B2>
    implements Cocartesian<Mu<A2, B2>, Cocartesian.Mu> {
        @Override
        public <A, B, C, D> FunctionType<App2<Mu<A2, B2>, A, B>, App2<Mu<A2, B2>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.prism(object -> Prism.unbox(app2).match(function.apply(object)).mapLeft(function2), object -> function2.apply(Prism.unbox(app2).build(object)));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Mu<A2, B2>, A, B> app2) {
            Prism prism = Prism.unbox(app2);
            return Optics.prism(either -> either.map(object -> prism.match(object).mapLeft(Either::left), object -> Either.left(Either.right(object))), object -> Either.left(prism.build(object)));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Mu<A2, B2>, A, B> app2) {
            Prism prism = Prism.unbox(app2);
            return Optics.prism(either -> either.map(object -> Either.left(Either.left(object)), object -> prism.match(object).mapLeft(Either::right)), object -> Either.right(prism.build(object)));
        }
    }

    public static final class Mu<A, B>
    implements K2 {
    }
}

