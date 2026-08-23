/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.optics.profunctors.ReCocartesian;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;

public interface Forget<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> Forget<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (Forget)app2;
    }

    public R run(A var1);

    public static final class Instance<R>
    implements Cartesian<com.mojang.datafixers.optics.Forget$Mu<R>, Mu<R>>,
    ReCocartesian<com.mojang.datafixers.optics.Forget$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.Forget$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.Forget$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.Forget$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.forget(object -> Forget.unbox(app2).run(function.apply(object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.Forget$Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<com.mojang.datafixers.optics.Forget$Mu<R>, A, B> app2) {
            return Optics.forget(pair -> Forget.unbox(app2).run(pair.getFirst()));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.Forget$Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<com.mojang.datafixers.optics.Forget$Mu<R>, A, B> app2) {
            return Optics.forget(pair -> Forget.unbox(app2).run(pair.getSecond()));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.Forget$Mu<R>, A, B> unleft(App2<com.mojang.datafixers.optics.Forget$Mu<R>, Either<A, C>, Either<B, C>> app2) {
            return Optics.forget(object -> Forget.unbox(app2).run(Either.left(object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.Forget$Mu<R>, A, B> unright(App2<com.mojang.datafixers.optics.Forget$Mu<R>, Either<C, A>, Either<C, B>> app2) {
            return Optics.forget(object -> Forget.unbox(app2).run(Either.right(object)));
        }

        public static final class Mu<R>
        implements Cartesian.Mu,
        ReCocartesian.Mu {
        }
    }

    public static final class Mu<R>
    implements K2 {
    }
}

