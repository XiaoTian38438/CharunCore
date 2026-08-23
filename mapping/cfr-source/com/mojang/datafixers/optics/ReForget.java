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
import com.mojang.datafixers.optics.profunctors.ReCartesian;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;

interface ReForget<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ReForget<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ReForget)app2;
    }

    public B run(R var1);

    public static final class Instance<R>
    implements ReCartesian<com.mojang.datafixers.optics.ReForget$Mu<R>, Mu<R>>,
    Cocartesian<com.mojang.datafixers.optics.ReForget$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ReForget$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ReForget$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ReForget$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.reForget(object -> function2.apply(ReForget.unbox(app2).run(object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForget$Mu<R>, A, B> unfirst(App2<com.mojang.datafixers.optics.ReForget$Mu<R>, Pair<A, C>, Pair<B, C>> app2) {
            return Optics.reForget(object -> ((Pair)ReForget.unbox(app2).run(object)).getFirst());
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForget$Mu<R>, A, B> unsecond(App2<com.mojang.datafixers.optics.ReForget$Mu<R>, Pair<C, A>, Pair<C, B>> app2) {
            return Optics.reForget(object -> ((Pair)ReForget.unbox(app2).run(object)).getSecond());
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForget$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ReForget$Mu<R>, A, B> app2) {
            return Optics.reForget(object -> Either.left(ReForget.unbox(app2).run(object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ReForget$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ReForget$Mu<R>, A, B> app2) {
            return Optics.reForget(object -> Either.right(ReForget.unbox(app2).run(object)));
        }

        static final class Mu<R>
        implements ReCartesian.Mu,
        Cocartesian.Mu {
            Mu() {
            }
        }
    }

    public static final class Mu<R>
    implements K2 {
    }
}

