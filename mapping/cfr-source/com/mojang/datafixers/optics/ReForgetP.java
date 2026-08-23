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

interface ReForgetP<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ReForgetP<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ReForgetP)app2;
    }

    public B run(A var1, R var2);

    public static final class Instance<R>
    implements AffineP<com.mojang.datafixers.optics.ReForgetP$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ReForgetP$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.reForgetP("dimap", (object, object2) -> {
                Object r = function.apply(object);
                Object b = ReForgetP.unbox(app2).run(r, object2);
                Object r2 = function2.apply(b);
                return r2;
            });
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, A, B> app2) {
            return Optics.reForgetP("left", (either, object) -> either.mapLeft(object2 -> ReForgetP.unbox(app2).run(object2, object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, A, B> app2) {
            return Optics.reForgetP("right", (either, object) -> either.mapRight(object2 -> ReForgetP.unbox(app2).run(object2, object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, A, B> app2) {
            return Optics.reForgetP("first", (pair, object) -> Pair.of(ReForgetP.unbox(app2).run(pair.getFirst(), object), pair.getSecond()));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<com.mojang.datafixers.optics.ReForgetP$Mu<R>, A, B> app2) {
            return Optics.reForgetP("second", (pair, object) -> Pair.of(pair.getFirst(), ReForgetP.unbox(app2).run(pair.getSecond(), object)));
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

