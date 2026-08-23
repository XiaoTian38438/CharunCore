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
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;

public interface Lens<S, T, A, B>
extends App2<Mu<A, B>, S, T>,
Optic<Cartesian.Mu, S, T, A, B> {
    public static <S, T, A, B> Lens<S, T, A, B> unbox(App2<Mu<A, B>, S, T> app2) {
        return (Lens)app2;
    }

    public static <S, T, A, B> Lens<S, T, A, B> unbox2(App2<Mu2<S, T>, B, A> app2) {
        return ((Box)app2).lens;
    }

    public static <S, T, A, B> App2<Mu2<S, T>, B, A> box(Lens<S, T, A, B> lens) {
        return new Box<S, T, A, B>(lens);
    }

    public A view(S var1);

    public T update(B var1, S var2);

    @Override
    default public <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Cartesian.Mu, P> app) {
        Cartesian cartesian = Cartesian.unbox(app);
        return app2 -> cartesian.dimap(cartesian.first(app2), object -> Pair.of(this.view(object), object), pair -> this.update(pair.getFirst(), pair.getSecond()));
    }

    public static final class Box<S, T, A, B>
    implements App2<Mu2<S, T>, B, A> {
        private final Lens<S, T, A, B> lens;

        public Box(Lens<S, T, A, B> lens) {
            this.lens = lens;
        }
    }

    public static final class Instance<A2, B2>
    implements Cartesian<Mu<A2, B2>, Cartesian.Mu> {
        @Override
        public <A, B, C, D> FunctionType<App2<Mu<A2, B2>, A, B>, App2<Mu<A2, B2>, C, D>> dimap(Function<C, A> function, Function<B, D> function2) {
            return app2 -> Optics.lens(object -> Lens.unbox(app2).view(function.apply(object)), (object, object2) -> function2.apply(Lens.unbox(app2).update(object, function.apply(object2))));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Mu<A2, B2>, A, B> app2) {
            return Optics.lens(pair -> Lens.unbox(app2).view(pair.getFirst()), (object, pair) -> Pair.of(Lens.unbox(app2).update(object, pair.getFirst()), pair.getSecond()));
        }

        @Override
        public <A, B, C> App2<Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Mu<A2, B2>, A, B> app2) {
            return Optics.lens(pair -> Lens.unbox(app2).view(pair.getSecond()), (object, pair) -> Pair.of(pair.getFirst(), Lens.unbox(app2).update(object, pair.getSecond())));
        }
    }

    public static final class Mu2<S, T>
    implements K2 {
    }

    public static final class Mu<A, B>
    implements K2 {
    }
}

