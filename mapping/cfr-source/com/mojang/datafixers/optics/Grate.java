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
import com.mojang.datafixers.optics.profunctors.Closed;
import java.util.function.Function;

interface Grate<S, T, A, B>
extends App2<Mu<A, B>, S, T>,
Optic<Closed.Mu, S, T, A, B> {
    public static <S, T, A, B> Grate<S, T, A, B> unbox(App2<Mu<A, B>, S, T> app2) {
        return (Grate)app2;
    }

    public T grate(FunctionType<FunctionType<S, A>, B> var1);

    @Override
    default public <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Closed.Mu, P> app) {
        Closed closed = Closed.unbox(app);
        return app2 -> closed.dimap(closed.closed(app2), object -> functionType -> functionType.apply(object), this::grate);
    }

    public static final class Instance<A2, B2>
    implements Closed<Mu<A2, B2>, Closed.Mu> {
        @Override
        public <A, B, C, D> FunctionType<App2<Mu<A2, B2>, A, B>, App2<Mu<A2, B2>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.grate(functionType -> function2.apply(Grate.unbox(app2).grate(functionType2 -> functionType.apply(FunctionType.create(functionType2.compose(function))))));
        }

        @Override
        public <A, B, X> App2<Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>> closed(App2<Mu<A2, B2>, A, B> app2) {
            FunctionType<FunctionType, FunctionType> functionType2 = functionType -> object -> functionType.apply(functionType -> functionType.apply(object));
            return (App2)Optics.grate(functionType2).eval((App)this).apply(Grate.unbox(app2));
        }
    }

    public static final class Mu<A, B>
    implements K2 {
    }
}

