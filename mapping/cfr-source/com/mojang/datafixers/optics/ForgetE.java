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

interface ForgetE<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ForgetE<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ForgetE)app2;
    }

    public Either<B, R> run(A var1);

    public static final class Instance<R>
    implements AffineP<com.mojang.datafixers.optics.ForgetE$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ForgetE$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.forgetE(object -> ForgetE.unbox(app2).run(function.apply(object)).mapLeft(function2));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, A, B> app2) {
            return Optics.forgetE(pair -> ForgetE.unbox(app2).run(pair.getFirst()).mapLeft(object -> Pair.of(object, pair.getSecond())));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, A, B> app2) {
            return Optics.forgetE(pair -> ForgetE.unbox(app2).run(pair.getSecond()).mapLeft(object -> Pair.of(pair.getFirst(), object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, A, B> app2) {
            return Optics.forgetE(either -> either.map(object -> ForgetE.unbox(app2).run(object).mapLeft(Either::left), object -> Either.left(Either.right(object))));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ForgetE$Mu<R>, A, B> app2) {
            return Optics.forgetE(either -> either.map(object -> Either.left(Either.left(object)), object -> ForgetE.unbox(app2).run(object).mapLeft(Either::right)));
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

