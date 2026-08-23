/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.util;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.CocartesianLike;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.Traversable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<L, R>
implements App<Mu<R>, L> {
    public static <L, R> Either<L, R> unbox(App<Mu<R>, L> app) {
        return (Either)app;
    }

    private Either() {
    }

    public abstract <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> var1, Function<? super R, ? extends D> var2);

    public abstract <T> T map(Function<? super L, ? extends T> var1, Function<? super R, ? extends T> var2);

    public abstract Either<L, R> ifLeft(Consumer<? super L> var1);

    public abstract Either<L, R> ifRight(Consumer<? super R> var1);

    public abstract Optional<L> left();

    public abstract Optional<R> right();

    public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> function) {
        return this.map(object -> Either.left(function.apply((Object)object)), Either::right);
    }

    public <T> Either<L, T> mapRight(Function<? super R, ? extends T> function) {
        return this.map(Either::left, object -> Either.right(function.apply((Object)object)));
    }

    public static <L, R> Either<L, R> left(L l) {
        return new Left(l);
    }

    public static <L, R> Either<L, R> right(R r) {
        return new Right(r);
    }

    public L orThrow() {
        return (L)this.map(object -> object, object -> {
            if (object instanceof Throwable) {
                throw new RuntimeException((Throwable)object);
            }
            throw new RuntimeException(object.toString());
        });
    }

    public Either<R, L> swap() {
        return this.map(Either::right, Either::left);
    }

    public <L2> Either<L2, R> flatMap(Function<L, Either<L2, R>> function) {
        return this.map(function, Either::right);
    }

    public static <U> U unwrap(Either<? extends U, ? extends U> either) {
        return (U)either.map(Function.identity(), Function.identity());
    }

    private static final class Left<L, R>
    extends Either<L, R> {
        private final L value;

        public Left(L l) {
            this.value = l;
        }

        @Override
        public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> function, Function<? super R, ? extends D> function2) {
            return new Left<C, R>(function.apply(this.value));
        }

        @Override
        public <T> T map(Function<? super L, ? extends T> function, Function<? super R, ? extends T> function2) {
            return function.apply(this.value);
        }

        @Override
        public Either<L, R> ifLeft(Consumer<? super L> consumer) {
            consumer.accept(this.value);
            return this;
        }

        @Override
        public Either<L, R> ifRight(Consumer<? super R> consumer) {
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.of(this.value);
        }

        @Override
        public Optional<R> right() {
            return Optional.empty();
        }

        public String toString() {
            return "Left[" + String.valueOf(this.value) + "]";
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            Left left = (Left)object;
            return Objects.equals(this.value, left.value);
        }

        public int hashCode() {
            return this.value.hashCode();
        }
    }

    private static final class Right<L, R>
    extends Either<L, R> {
        private final R value;

        public Right(R r) {
            this.value = r;
        }

        @Override
        public <C, D> Either<C, D> mapBoth(Function<? super L, ? extends C> function, Function<? super R, ? extends D> function2) {
            return new Right<L, D>(function2.apply(this.value));
        }

        @Override
        public <T> T map(Function<? super L, ? extends T> function, Function<? super R, ? extends T> function2) {
            return function2.apply(this.value);
        }

        @Override
        public Either<L, R> ifLeft(Consumer<? super L> consumer) {
            return this;
        }

        @Override
        public Either<L, R> ifRight(Consumer<? super R> consumer) {
            consumer.accept(this.value);
            return this;
        }

        @Override
        public Optional<L> left() {
            return Optional.empty();
        }

        @Override
        public Optional<R> right() {
            return Optional.of(this.value);
        }

        public String toString() {
            return "Right[" + String.valueOf(this.value) + "]";
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            Right right = (Right)object;
            return Objects.equals(this.value, right.value);
        }

        public int hashCode() {
            return this.value.hashCode();
        }
    }

    public static final class Instance<R2>
    implements Applicative<com.mojang.datafixers.util.Either$Mu<R2>, Mu<R2>>,
    Traversable<com.mojang.datafixers.util.Either$Mu<R2>, Mu<R2>>,
    CocartesianLike<com.mojang.datafixers.util.Either$Mu<R2>, R2, Mu<R2>> {
        @Override
        public <T, R> App<com.mojang.datafixers.util.Either$Mu<R2>, R> map(Function<? super T, ? extends R> function, App<com.mojang.datafixers.util.Either$Mu<R2>, T> app) {
            return Either.unbox(app).mapLeft(function);
        }

        @Override
        public <A> App<com.mojang.datafixers.util.Either$Mu<R2>, A> point(A a) {
            return Either.left(a);
        }

        @Override
        public <A, R> Function<App<com.mojang.datafixers.util.Either$Mu<R2>, A>, App<com.mojang.datafixers.util.Either$Mu<R2>, R>> lift1(App<com.mojang.datafixers.util.Either$Mu<R2>, Function<A, R>> app) {
            return app2 -> Either.unbox(app).flatMap(function -> Either.unbox(app2).mapLeft(function));
        }

        @Override
        public <A, B, R> BiFunction<App<com.mojang.datafixers.util.Either$Mu<R2>, A>, App<com.mojang.datafixers.util.Either$Mu<R2>, B>, App<com.mojang.datafixers.util.Either$Mu<R2>, R>> lift2(App<com.mojang.datafixers.util.Either$Mu<R2>, BiFunction<A, B, R>> app) {
            return (app2, app3) -> Either.unbox(app).flatMap(biFunction -> Either.unbox(app2).flatMap(object -> Either.unbox(app3).mapLeft(object2 -> biFunction.apply(object, object2))));
        }

        @Override
        public <F extends K1, A, B> App<F, App<com.mojang.datafixers.util.Either$Mu<R2>, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<com.mojang.datafixers.util.Either$Mu<R2>, A> app) {
            return Either.unbox(app).map((? super L object) -> {
                App app = (App)function.apply(object);
                return applicative.ap(Either::left, app);
            }, (? super R object) -> applicative.point(Either.right(object)));
        }

        @Override
        public <A> App<com.mojang.datafixers.util.Either$Mu<R2>, A> to(App<com.mojang.datafixers.util.Either$Mu<R2>, A> app) {
            return app;
        }

        @Override
        public <A> App<com.mojang.datafixers.util.Either$Mu<R2>, A> from(App<com.mojang.datafixers.util.Either$Mu<R2>, A> app) {
            return app;
        }

        public static final class Mu<R2>
        implements Applicative.Mu,
        Traversable.Mu,
        CocartesianLike.Mu {
        }
    }

    public static final class Mu<R>
    implements K1 {
    }
}

