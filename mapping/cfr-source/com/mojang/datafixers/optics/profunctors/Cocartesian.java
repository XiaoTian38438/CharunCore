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
import com.mojang.datafixers.kinds.CocartesianLike;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.profunctors.FunctorProfunctor;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.util.Either;

public interface Cocartesian<P extends K2, Mu extends Mu>
extends Profunctor<P, Mu> {
    public static <P extends K2, Proof extends Mu> Cocartesian<P, Proof> unbox(App<Proof, P> app) {
        return (Cocartesian)app;
    }

    public <A, B, C> App2<P, Either<A, C>, Either<B, C>> left(App2<P, A, B> var1);

    default public <A, B, C> App2<P, Either<C, A>, Either<C, B>> right(App2<P, A, B> app2) {
        return this.dimap(this.left(app2), Either::swap, Either::swap);
    }

    default public FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>> toFP() {
        return new FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>>(){

            @Override
            public <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CocartesianLike.Mu, F> app, App2<P, A, B> app2) {
                return this.cap(CocartesianLike.unbox(app), app2);
            }

            private <A, B, F extends K1, C> App2<P, App<F, A>, App<F, B>> cap(CocartesianLike<F, C, ?> cocartesianLike, App2<P, A, B> app2) {
                return Cocartesian.this.dimap(Cocartesian.this.left(app2), app -> Either.unbox(cocartesianLike.to(app)), cocartesianLike::from);
            }
        };
    }

    public static interface Mu
    extends Profunctor.Mu {
        public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>(){};
    }
}

