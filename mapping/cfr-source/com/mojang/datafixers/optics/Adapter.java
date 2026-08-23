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
import com.mojang.datafixers.optics.profunctors.Profunctor;
import java.util.function.Function;

public interface Adapter<S, T, A, B>
extends App2<Mu<A, B>, S, T>,
Optic<Profunctor.Mu, S, T, A, B> {
    public static <S, T, A, B> Adapter<S, T, A, B> unbox(App2<Mu<A, B>, S, T> app2) {
        return (Adapter)app2;
    }

    public A from(S var1);

    public T to(B var1);

    @Override
    default public <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Profunctor.Mu, P> app) {
        Profunctor profunctor = Profunctor.unbox(app);
        return app2 -> profunctor.dimap(app2, this::from, this::to);
    }

    public static final class Instance<A2, B2>
    implements Profunctor<Mu<A2, B2>, Profunctor.Mu> {
        @Override
        public <A, B, C, D> FunctionType<App2<Mu<A2, B2>, A, B>, App2<Mu<A2, B2>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.adapter(object -> Adapter.unbox(app2).from(function.apply(object)), object -> function2.apply(Adapter.unbox(app2).to(object)));
        }
    }

    public static final class Mu<A, B>
    implements K2 {
    }
}

