/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.kinds;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.Functor;
import com.mojang.datafixers.kinds.K1;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class IdF<A>
implements App<Mu, A> {
    protected final A value;

    IdF(A a) {
        this.value = a;
    }

    public A value() {
        return this.value;
    }

    public static <A> A get(App<Mu, A> app) {
        return ((IdF)app).value;
    }

    public static <A> IdF<A> create(A a) {
        return new IdF<A>(a);
    }

    public static enum Instance implements Functor<com.mojang.datafixers.kinds.IdF$Mu, Mu>,
    Applicative<com.mojang.datafixers.kinds.IdF$Mu, Mu>
    {
        INSTANCE;


        @Override
        public <T, R> App<com.mojang.datafixers.kinds.IdF$Mu, R> map(Function<? super T, ? extends R> function, App<com.mojang.datafixers.kinds.IdF$Mu, T> app) {
            IdF idF = (IdF)app;
            return new IdF<R>(function.apply(idF.value));
        }

        @Override
        public <A> App<com.mojang.datafixers.kinds.IdF$Mu, A> point(A a) {
            return IdF.create(a);
        }

        @Override
        public <A, R> Function<App<com.mojang.datafixers.kinds.IdF$Mu, A>, App<com.mojang.datafixers.kinds.IdF$Mu, R>> lift1(App<com.mojang.datafixers.kinds.IdF$Mu, Function<A, R>> app) {
            return app2 -> IdF.create(((Function)IdF.get(app)).apply(IdF.get(app2)));
        }

        @Override
        public <A, B, R> BiFunction<App<com.mojang.datafixers.kinds.IdF$Mu, A>, App<com.mojang.datafixers.kinds.IdF$Mu, B>, App<com.mojang.datafixers.kinds.IdF$Mu, R>> lift2(App<com.mojang.datafixers.kinds.IdF$Mu, BiFunction<A, B, R>> app) {
            return (app2, app3) -> IdF.create(((BiFunction)IdF.get(app)).apply(IdF.get(app2), IdF.get(app3)));
        }

        public static final class Mu
        implements Functor.Mu,
        Applicative.Mu {
        }
    }

    public static final class Mu
    implements K1 {
    }
}

