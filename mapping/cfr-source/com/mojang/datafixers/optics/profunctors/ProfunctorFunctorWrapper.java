/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics.profunctors;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.Functor;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import java.util.function.Function;

public class ProfunctorFunctorWrapper<P extends K2, F extends K1, G extends K1, A, B>
implements App2<Mu<P, F, G>, A, B> {
    private final App2<P, App<F, A>, App<G, B>> value;

    public static <P extends K2, F extends K1, G extends K1, A, B> ProfunctorFunctorWrapper<P, F, G, A, B> unbox(App2<Mu<P, F, G>, A, B> app2) {
        return (ProfunctorFunctorWrapper)app2;
    }

    public ProfunctorFunctorWrapper(App2<P, App<F, A>, App<G, B>> app2) {
        this.value = app2;
    }

    public App2<P, App<F, A>, App<G, B>> value() {
        return this.value;
    }

    public static final class Instance<P extends K2, F extends K1, G extends K1>
    implements Profunctor<com.mojang.datafixers.optics.profunctors.ProfunctorFunctorWrapper$Mu<P, F, G>, Mu>,
    App<Mu, com.mojang.datafixers.optics.profunctors.ProfunctorFunctorWrapper$Mu<P, F, G>> {
        private final Profunctor<P, ? extends Profunctor.Mu> profunctor;
        private final Functor<F, ?> fFunctor;
        private final Functor<G, ?> gFunctor;

        public Instance(App<? extends Profunctor.Mu, P> app, Functor<F, ?> functor, Functor<G, ?> functor2) {
            this.profunctor = Profunctor.unbox(app);
            this.fFunctor = functor;
            this.gFunctor = functor2;
        }

        @Override
        public <A, B, C, D> FunctionType<App2<com.mojang.datafixers.optics.profunctors.ProfunctorFunctorWrapper$Mu<P, F, G>, A, B>, App2<com.mojang.datafixers.optics.profunctors.ProfunctorFunctorWrapper$Mu<P, F, G>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> {
                App2 app22 = ProfunctorFunctorWrapper.unbox(app2).value();
                App2<P, App, App> app23 = this.profunctor.dimap(app22, (C app) -> this.fFunctor.map(function, app), (B app) -> this.gFunctor.map(function2, app));
                return new ProfunctorFunctorWrapper(app23);
            };
        }

        public static final class Mu
        implements Profunctor.Mu {
        }
    }

    public static final class Mu<P extends K2, F extends K1, G extends K1>
    implements K2 {
    }
}

