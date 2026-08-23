/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Cocartesian;
import com.mojang.datafixers.util.Either;
import java.util.function.Function;

interface ReForgetE<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ReForgetE<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ReForgetE)app2;
    }

    public B run(Either<A, R> var1);

    public static final class Instance<R>
    implements Cocartesian<com.mojang.datafixers.optics.ReForgetE$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ReForgetE$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ReForgetE$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ReForgetE$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.reForgetE("dimap", either -> {
                Either either2 = either.mapLeft(function);
                Object b = ReForgetE.unbox(app2).run(either2);
                Object r = function2.apply(b);
                return r;
            });
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetE$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ReForgetE$Mu<R>, A, B> app2) {
            ReForgetE reForgetE = ReForgetE.unbox(app2);
            return Optics.reForgetE("left", either2 -> either2.map(either -> either.map(object -> Either.left(reForgetE.run(Either.left(object))), Either::right), object -> Either.left(reForgetE.run(Either.right(object)))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForgetE$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ReForgetE$Mu<R>, A, B> app2) {
            ReForgetE reForgetE = ReForgetE.unbox(app2);
            return Optics.reForgetE("right", either2 -> either2.map(either -> either.map(Either::left, object -> Either.right(reForgetE.run(Either.left(object)))), object -> Either.right(reForgetE.run(Either.right(object)))));
        }

        static final class Mu<R>
        implements Cocartesian.Mu {
            Mu() {
            }
        }
    }

    public static final class Mu<R>
    implements K2 {
    }
}

