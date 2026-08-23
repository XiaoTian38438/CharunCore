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
import java.util.function.Function;

interface ReForgetEP<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ReForgetEP<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ReForgetEP)app2;
    }

    public B run(Either<A, Pair<A, R>> var1);

    public static final class Instance<R>
    implements AffineP<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ReForgetEP$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.reForgetEP("dimap", either -> {
                Either either2 = either.mapBoth(function, pair -> Pair.of(function.apply(pair.getFirst()), pair.getSecond()));
                Object b = ReForgetEP.unbox(app2).run(either2);
                Object r = function2.apply(b);
                return r;
            });
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, A, B> app2) {
            ReForgetEP reForgetEP = ReForgetEP.unbox(app2);
            return Optics.reForgetEP("left", either2 -> either2.map(either -> either.mapLeft(object -> reForgetEP.run(Either.left(object))), pair -> ((Either)pair.getFirst()).mapLeft(object -> reForgetEP.run(Either.right(Pair.of(object, pair.getSecond()))))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, A, B> app2) {
            ReForgetEP reForgetEP = ReForgetEP.unbox(app2);
            return Optics.reForgetEP("right", either2 -> either2.map(either -> either.mapRight(object -> reForgetEP.run(Either.left(object))), pair -> ((Either)pair.getFirst()).mapRight(object -> reForgetEP.run(Either.right(Pair.of(object, pair.getSecond()))))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, A, B> app2) {
            ReForgetEP reForgetEP = ReForgetEP.unbox(app2);
            return Optics.reForgetEP("first", either -> either.map(pair -> Pair.of(reForgetEP.run(Either.left(pair.getFirst())), pair.getSecond()), pair -> Pair.of(reForgetEP.run(Either.right(Pair.of(((Pair)pair.getFirst()).getFirst(), pair.getSecond()))), ((Pair)pair.getFirst()).getSecond())));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<com.mojang.datafixers.optics.ReForgetEP$Mu<R>, A, B> app2) {
            ReForgetEP reForgetEP = ReForgetEP.unbox(app2);
            return Optics.reForgetEP("second", either -> either.map(pair -> Pair.of(pair.getFirst(), reForgetEP.run(Either.left(pair.getSecond()))), pair -> Pair.of(((Pair)pair.getFirst()).getFirst(), reForgetEP.run(Either.right(Pair.of(((Pair)pair.getFirst()).getSecond(), pair.getSecond()))))));
        }

        static final class Mu<R>
        implements AffineP.Mu {
            Mu() {
            }
        }
    }

    public static final class Mu<R>
    implements K2 {
    }
}

