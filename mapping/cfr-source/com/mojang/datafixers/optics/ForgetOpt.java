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
import java.util.Optional;
import java.util.function.Function;

public interface ForgetOpt<R, A, B>
extends App2<Mu<R>, A, B> {
    public static <R, A, B> ForgetOpt<R, A, B> unbox(App2<Mu<R>, A, B> app2) {
        return (ForgetOpt)app2;
    }

    public Optional<R> run(A var1);

    public static final class Instance<R>
    implements AffineP<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, Mu<R>>,
    App<Mu<R>, com.mojang.datafixers.optics.ForgetOpt$Mu<R>> {
        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, A, B>, App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.forgetOpt(object -> ForgetOpt.unbox(app2).run(function.apply(object)));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, A, B> app2) {
            return Optics.forgetOpt(pair -> ForgetOpt.unbox(app2).run(pair.getFirst()));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, A, B> app2) {
            return Optics.forgetOpt(pair -> ForgetOpt.unbox(app2).run(pair.getSecond()));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, Either<A, C>, Either<B, C>> left(App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, A, B> app2) {
            return Optics.forgetOpt(either -> either.left().flatMap(ForgetOpt.unbox(app2)::run));
        }

        @Override
        public <A, B, C> App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, Either<C, A>, Either<C, B>> right(App2<com.mojang.datafixers.optics.ForgetOpt$Mu<R>, A, B> app2) {
            return Optics.forgetOpt(either -> either.right().flatMap(ForgetOpt.unbox(app2)::run));
        }

        public static final class Mu<R>
        implements AffineP.Mu {
        }
    }

    public static final class Mu<R>
    implements K2 {
    }
}

