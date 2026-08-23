/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.kinds;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.Traversable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class OptionalBox<T>
implements App<Mu, T> {
    private final Optional<T> value;

    public static <T> Optional<T> unbox(App<Mu, T> app) {
        return ((OptionalBox)app).value;
    }

    public static <T> OptionalBox<T> create(Optional<T> optional) {
        return new OptionalBox<T>(optional);
    }

    private OptionalBox(Optional<T> optional) {
        this.value = optional;
    }

    public static enum Instance implements Applicative<com.mojang.datafixers.kinds.OptionalBox$Mu, Mu>,
    Traversable<com.mojang.datafixers.kinds.OptionalBox$Mu, Mu>
    {
        INSTANCE;


        @Override
        public <T, R> App<com.mojang.datafixers.kinds.OptionalBox$Mu, R> map(Function<? super T, ? extends R> function, App<com.mojang.datafixers.kinds.OptionalBox$Mu, T> app) {
            return OptionalBox.create(OptionalBox.unbox(app).map(function));
        }

        @Override
        public <A> App<com.mojang.datafixers.kinds.OptionalBox$Mu, A> point(A a) {
            return OptionalBox.create(Optional.of(a));
        }

        @Override
        public <A, R> Function<App<com.mojang.datafixers.kinds.OptionalBox$Mu, A>, App<com.mojang.datafixers.kinds.OptionalBox$Mu, R>> lift1(App<com.mojang.datafixers.kinds.OptionalBox$Mu, Function<A, R>> app) {
            return app2 -> OptionalBox.create(OptionalBox.unbox(app).flatMap(function -> OptionalBox.unbox(app2).map(function)));
        }

        @Override
        public <A, B, R> BiFunction<App<com.mojang.datafixers.kinds.OptionalBox$Mu, A>, App<com.mojang.datafixers.kinds.OptionalBox$Mu, B>, App<com.mojang.datafixers.kinds.OptionalBox$Mu, R>> lift2(App<com.mojang.datafixers.kinds.OptionalBox$Mu, BiFunction<A, B, R>> app) {
            return (app2, app3) -> OptionalBox.create(OptionalBox.unbox(app).flatMap(biFunction -> OptionalBox.unbox(app2).flatMap(object -> OptionalBox.unbox(app3).map(object2 -> biFunction.apply(object, object2)))));
        }

        @Override
        public <F extends K1, A, B> App<F, App<com.mojang.datafixers.kinds.OptionalBox$Mu, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<com.mojang.datafixers.kinds.OptionalBox$Mu, A> app) {
            Optional<App<F, B>> optional = OptionalBox.unbox(app).map(function);
            if (optional.isPresent()) {
                return applicative.map((? super T object) -> OptionalBox.create(Optional.of(object)), optional.get());
            }
            return applicative.point(OptionalBox.create(Optional.empty()));
        }

        public static final class Mu
        implements Applicative.Mu,
        Traversable.Mu {
        }
    }

    public static final class Mu
    implements K1 {
    }
}

