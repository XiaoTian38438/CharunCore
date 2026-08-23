/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Procompose<F extends K2, G extends K2, A, B, C>
implements App2<Mu<F, G>, A, B> {
    private final Supplier<App2<F, A, C>> first;
    private final App2<G, C, B> second;

    public Procompose(Supplier<App2<F, A, C>> supplier, App2<G, C, B> app2) {
        this.first = supplier;
        this.second = app2;
    }

    public static <F extends K2, G extends K2, A, B> Procompose<F, G, A, B, ?> unbox(App2<Mu<F, G>, A, B> app2) {
        return (Procompose)app2;
    }

    public Supplier<App2<F, A, C>> first() {
        return this.first;
    }

    public App2<G, C, B> second() {
        return this.second;
    }

    static final class ProfunctorInstance<F extends K2, G extends K2>
    implements Profunctor<Mu<F, G>, Profunctor.Mu> {
        private final Profunctor<F, Profunctor.Mu> p1;
        private final Profunctor<G, Profunctor.Mu> p2;

        ProfunctorInstance(Profunctor<F, Profunctor.Mu> profunctor, Profunctor<G, Profunctor.Mu> profunctor2) {
            this.p1 = profunctor;
            this.p2 = profunctor2;
        }

        @Override
        public <A, B, C, D> FunctionType<App2<Mu<F, G>, A, B>, App2<Mu<F, G>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> this.cap(Procompose.unbox(app2), function, function2);
        }

        private <A, B, C, D, E> App2<Mu<F, G>, C, D> cap(Procompose<F, G, A, B, E> procompose, Function<C, A> function, Function<B, D> function2) {
            return new Procompose(() -> this.p1.dimap(function, Function.identity()).apply(procompose.first.get()), this.p2.dimap(Function.identity(), function2).apply(procompose.second));
        }
    }

    public static final class Mu<F extends K2, G extends K2>
    implements K2 {
    }
}

