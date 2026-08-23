/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 */
package com.mojang.datafixers.optics.profunctors;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.CartesianLike;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.profunctors.FunctorProfunctor;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.util.Pair;

public interface Cartesian<P extends K2, Mu extends Mu>
extends Profunctor<P, Mu> {
    public static <P extends K2, Proof extends Mu> Cartesian<P, Proof> unbox(App<Proof, P> app) {
        return (Cartesian)app;
    }

    public <A, B, C> App2<P, Pair<A, C>, Pair<B, C>> first(App2<P, A, B> var1);

    default public <A, B, C> App2<P, Pair<C, A>, Pair<C, B>> second(App2<P, A, B> app2) {
        return this.dimap(this.first(app2), Pair::swap, Pair::swap);
    }

    default public FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>> toFP2() {
        return new FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>>(){

            @Override
            public <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CartesianLike.Mu, F> app, App2<P, A, B> app2) {
                return this.cap(CartesianLike.unbox(app), app2);
            }

            private <A, B, F extends K1, C> App2<P, App<F, A>, App<F, B>> cap(CartesianLike<F, C, ?> cartesianLike, App2<P, A, B> app2) {
                return Cartesian.this.dimap(Cartesian.this.first(app2), app -> Pair.unbox(cartesianLike.to(app)), cartesianLike::from);
            }
        };
    }

    public static interface Mu
    extends Profunctor.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>(){};
    }
}

