/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ReForgetC<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ReForgetC<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ReForgetC)app2;
    }

    public Either<Function<R, B>, BiFunction<A, R, B>> impl();

    default public B run(A a, R r) {
        return (B)this.impl().map(function -> function.apply(r), biFunction -> biFunction.apply(a, r));
    }

    public static final class Instance<R>
    implements AffineP<com.mojang.datafixers.optics.ReForgetC$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ReForgetC$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.reForgetC("dimap", ReForgetC.unbox(app2).impl().map(function2 -> Either.left(object -> function2.apply(function2.apply(object))), biFunction -> Either.right((object, object2) -> function2.apply(biFunction.apply(function.apply(object), object2)))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, A, B> app2) {
            return Optics.reForgetC("first", ReForgetC.unbox(app2).impl().map(function -> Either.right((pair, object) -> Pair.of(function.apply(object), pair.getSecond())), biFunction -> Either.right((pair, object) -> Pair.of(biFunction.apply(pair.getFirst(), object), pair.getSecond()))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, A, B> app2) {
            return Optics.reForgetC("second", ReForgetC.unbox(app2).impl().map(function -> Either.right((pair, object) -> Pair.of(pair.getFirst(), function.apply(object))), biFunction -> Either.right((pair, object) -> Pair.of(pair.getFirst(), biFunction.apply(pair.getSecond(), object)))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, A, B> app2) {
            return Optics.reForgetC("left", ReForgetC.unbox(app2).impl().map(function -> Either.left(object -> Either.left(function.apply(object))), biFunction -> Either.right((either, object) -> either.mapLeft(object2 -> biFunction.apply(object2, object)))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ReForgetC$Mu<R>, A, B> app2) {
            return Optics.reForgetC("right", ReForgetC.unbox(app2).impl().map(function -> Either.left(object -> Either.right(function.apply(object))), biFunction -> Either.right((either, object) -> either.mapRight(object2 -> biFunction.apply(object2, object)))));
        }

        public static final class Mu<R>
        implements AffineP.Mu {
        }
    }

    public static final class Mu<R>
    implements K2 {
    }
}

