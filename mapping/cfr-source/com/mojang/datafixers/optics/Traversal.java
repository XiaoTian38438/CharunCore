/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.Wander;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import java.util.function.Function;

public interface Traversal<S, T, A, B>
extends Wander<S, T, A, B>,
App2<Mu<A, B>, S, T>,
Optic<TraversalP.Mu, S, T, A, B> {
    public static <S, T, A, B> Traversal<S, T, A, B> unbox(App2<Mu<A, B>, S, T> app2) {
        return (Traversal)app2;
    }

    @Override
    default public <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends TraversalP.Mu, P> app) {
        TraversalP traversalP = TraversalP.unbox(app);
        return app2 -> traversalP.wander(this, app2);
    }

    public static final class Instance<A2, B2>
    implements TraversalP<Mu<A2, B2>, TraversalP.Mu> {
        @Override
        public <A, B, C, D> FunctionType<App2<Mu<A2, B2>, A, B>, App2<Mu<A2, B2>, C, D>> dimap(Function<C, A> function, final Function<B, D> function2) {
            return app2 -> new Traversal<C, D, A2, B2>(){
                final /* synthetic */ App2 val$tr;
                final /* synthetic */ Function val$g;
                {
                    this.val$tr = app2;
                    this.val$g = function22;
                }

                @Override
                public <F extends K1> FunctionType<C, App<F, D>> wander(Applicative<F, ?> applicative, FunctionType<A2, App<F, B2>> functionType) {
                    return object -> applicative.map(function2, Traversal.unbox(this.val$tr).wander(applicative, functionType).apply(this.val$g.apply(object)));
                }
            };
        }

        @Override
        public <S, T, A, B> App2<Mu<A2, B2>, S, T> wander(final Wander<S, T, A, B> wander, final App2<Mu<A2, B2>, A, B> app2) {
            return new Traversal<S, T, A2, B2>(){

                @Override
                public <F extends K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> applicative, FunctionType<A2, App<F, B2>> functionType) {
                    return wander.wander(applicative, Traversal.unbox(app2).wander(applicative, functionType));
                }
            };
        }
    }

    public static final class Mu<A, B>
    implements K2 {
    }
}

