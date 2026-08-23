/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/*
 * Uses 'sealed' constructs - enablewith --sealed true
 */
public interface DataResult<R>
extends App<Mu, R> {
    public static <R> DataResult<R> unbox(App<Mu, R> app) {
        return (DataResult)app;
    }

    public static <R> DataResult<R> success(R r) {
        return DataResult.success(r, Lifecycle.experimental());
    }

    public static <R> DataResult<R> error(Supplier<String> supplier, R r) {
        return DataResult.error(supplier, r, Lifecycle.experimental());
    }

    public static <R> DataResult<R> error(Supplier<String> supplier) {
        return DataResult.error(supplier, Lifecycle.experimental());
    }

    public static <R> DataResult<R> success(R r, Lifecycle lifecycle) {
        return new Success<R>(r, lifecycle);
    }

    public static <R> DataResult<R> error(Supplier<String> supplier, R r, Lifecycle lifecycle) {
        return new Error<R>(supplier, Optional.of(r), lifecycle);
    }

    public static <R> DataResult<R> error(Supplier<String> supplier, Lifecycle lifecycle) {
        return new Error(supplier, Optional.empty(), lifecycle);
    }

    public static <K, V> Function<K, DataResult<V>> partialGet(Function<K, V> function, Supplier<String> supplier) {
        return object -> Optional.ofNullable(function.apply(object)).map(DataResult::success).orElseGet(() -> DataResult.lambda$partialGet$1((Supplier)supplier, object));
    }

    public static Instance instance() {
        return Instance.INSTANCE;
    }

    public static String appendMessages(String string, String string2) {
        return string + "; " + string2;
    }

    public Optional<R> result();

    public Optional<Error<R>> error();

    public Lifecycle lifecycle();

    public boolean hasResultOrPartial();

    public Optional<R> resultOrPartial(Consumer<String> var1);

    public Optional<R> resultOrPartial();

    public <E extends Throwable> R getOrThrow(Function<String, E> var1) throws E;

    public <E extends Throwable> R getPartialOrThrow(Function<String, E> var1) throws E;

    default public R getOrThrow() {
        return this.getOrThrow(IllegalStateException::new);
    }

    default public R getPartialOrThrow() {
        return this.getPartialOrThrow(IllegalStateException::new);
    }

    public <T> DataResult<T> map(Function<? super R, ? extends T> var1);

    public <T> T mapOrElse(Function<? super R, ? extends T> var1, Function<? super Error<R>, ? extends T> var2);

    public DataResult<R> ifSuccess(Consumer<? super R> var1);

    public DataResult<R> ifError(Consumer<? super Error<R>> var1);

    public DataResult<R> promotePartial(Consumer<String> var1);

    public <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> var1);

    public <R2> DataResult<R2> ap(DataResult<Function<R, R2>> var1);

    default public <R2, S> DataResult<S> apply2(BiFunction<R, R2, S> biFunction, DataResult<R2> dataResult) {
        return DataResult.unbox(DataResult.instance().apply2(biFunction, this, dataResult));
    }

    default public <R2, S> DataResult<S> apply2stable(BiFunction<R, R2, S> biFunction, DataResult<R2> dataResult) {
        Instance instance = DataResult.instance();
        DataResult dataResult2 = DataResult.unbox(instance.point(biFunction)).setLifecycle(Lifecycle.stable());
        return DataResult.unbox(instance.ap2(dataResult2, this, dataResult));
    }

    default public <R2, R3, S> DataResult<S> apply3(Function3<R, R2, R3, S> function3, DataResult<R2> dataResult, DataResult<R3> dataResult2) {
        return DataResult.unbox(DataResult.instance().apply3(function3, this, dataResult, dataResult2));
    }

    public DataResult<R> setPartial(Supplier<R> var1);

    public DataResult<R> setPartial(R var1);

    public DataResult<R> mapError(UnaryOperator<String> var1);

    public DataResult<R> setLifecycle(Lifecycle var1);

    default public DataResult<R> addLifecycle(Lifecycle lifecycle) {
        return this.setLifecycle(this.lifecycle().add(lifecycle));
    }

    public boolean isSuccess();

    default public boolean isError() {
        return !this.isSuccess();
    }

    private static /* synthetic */ DataResult lambda$partialGet$1(Supplier supplier, Object object) {
        return DataResult.error(() -> DataResult.lambda$partialGet$0((Supplier)supplier, object));
    }

    private static /* synthetic */ String lambda$partialGet$0(Supplier supplier, Object object) {
        return (String)supplier.get() + String.valueOf(object);
    }

    public record Success<R>(R value, Lifecycle lifecycle) implements DataResult<R>
    {
        @Override
        public Optional<R> result() {
            return Optional.of(this.value);
        }

        @Override
        public Optional<Error<R>> error() {
            return Optional.empty();
        }

        @Override
        public boolean hasResultOrPartial() {
            return true;
        }

        @Override
        public Optional<R> resultOrPartial(Consumer<String> consumer) {
            return Optional.of(this.value);
        }

        @Override
        public Optional<R> resultOrPartial() {
            return Optional.of(this.value);
        }

        @Override
        public <E extends Throwable> R getOrThrow(Function<String, E> function) throws E {
            return this.value;
        }

        @Override
        public <E extends Throwable> R getPartialOrThrow(Function<String, E> function) throws E {
            return this.value;
        }

        @Override
        public <T> DataResult<T> map(Function<? super R, ? extends T> function) {
            return new Success<T>(function.apply(this.value), this.lifecycle);
        }

        @Override
        public <T> T mapOrElse(Function<? super R, ? extends T> function, Function<? super Error<R>, ? extends T> function2) {
            return function.apply(this.value);
        }

        @Override
        public DataResult<R> ifSuccess(Consumer<? super R> consumer) {
            consumer.accept(this.value);
            return this;
        }

        @Override
        public DataResult<R> ifError(Consumer<? super Error<R>> consumer) {
            return this;
        }

        @Override
        public DataResult<R> promotePartial(Consumer<String> consumer) {
            return this;
        }

        @Override
        public <R2> DataResult<R2> flatMap(Function<? super R, ? extends DataResult<R2>> function) {
            return function.apply(this.value).addLifecycle(this.lifecycle);
        }

        @Override
        public <R2> DataResult<R2> ap(DataResult<Function<R, R2>> dataResult) {
            Lifecycle lifecycle = this.lifecycle.add(dataResult.lifecycle());
            if (dataResult instanceof Success) {
                Success success = (Success)dataResult;
                return new Success(((Function)success.value).apply(this.value), lifecycle);
            }
            if (dataResult instanceof Error) {
                Error error = (Error)dataResult;
                return new Error<Object>(error.messageSupplier, error.partialValue.map((? super T function) -> function.apply(this.value)), lifecycle);
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public DataResult<R> setPartial(Supplier<R> supplier) {
            return this;
        }

        @Override
        public DataResult<R> setPartial(R r) {
            return this;
        }

        @Override
        public DataResult<R> mapError(UnaryOperator<String> unaryOperator) {
            return this;
        }

        @Override
        public DataResult<R> setLifecycle(Lifecycle lifecycle) {
            if (this.lifecycle.equals(lifecycle)) {
                return this;
            }
            return new Success<R>(this.value, lifecycle);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public String toString() {
            return "DataResult.Success[" + String.valueOf(this.value) + "]";
        }
    }

    public record Error<R>(Supplier<String> messageSupplier, Optional<R> partialValue, Lifecycle lifecycle) implements DataResult<R>
    {
        public String message() {
            return this.messageSupplier.get();
        }

        @Override
        public Optional<R> result() {
            return Optional.empty();
        }

        @Override
        public Optional<Error<R>> error() {
            return Optional.of(this);
        }

        @Override
        public boolean hasResultOrPartial() {
            return this.partialValue.isPresent();
        }

        @Override
        public Optional<R> resultOrPartial(Consumer<String> consumer) {
            consumer.accept(this.messageSupplier.get());
            return this.partialValue;
        }

        @Override
        public Optional<R> resultOrPartial() {
            return this.partialValue;
        }

        @Override
        public <E extends Throwable> R getOrThrow(Function<String, E> function) throws E {
            throw (Throwable)function.apply(this.message());
        }

        @Override
        public <E extends Throwable> R getPartialOrThrow(Function<String, E> function) throws E {
            if (this.partialValue.isPresent()) {
                return this.partialValue.get();
            }
            throw (Throwable)function.apply(this.message());
        }

        @Override
        public <T> Error<T> map(Function<? super R, ? extends T> function) {
            if (this.partialValue.isEmpty()) {
                return this;
            }
            return new Error<T>(this.messageSupplier, this.partialValue.map(function), this.lifecycle);
        }

        @Override
        public <T> T mapOrElse(Function<? super R, ? extends T> function, Function<? super Error<R>, ? extends T> function2) {
            return function2.apply(this);
        }

        @Override
        public DataResult<R> ifSuccess(Consumer<? super R> consumer) {
            return this;
        }

        @Override
        public DataResult<R> ifError(Consumer<? super Error<R>> consumer) {
            consumer.accept(this);
            return this;
        }

        @Override
        public DataResult<R> promotePartial(Consumer<String> consumer) {
            consumer.accept(this.messageSupplier.get());
            return this.partialValue.map((? super T object) -> new Success<Object>(object, this.lifecycle)).orElse(this);
        }

        @Override
        public <R2> Error<R2> flatMap(Function<? super R, ? extends DataResult<R2>> function) {
            if (this.partialValue.isEmpty()) {
                return this;
            }
            DataResult<R2> dataResult = function.apply(this.partialValue.get());
            Lifecycle lifecycle = this.lifecycle.add(dataResult.lifecycle());
            if (dataResult instanceof Success) {
                Success success = (Success)dataResult;
                return new Error(this.messageSupplier, Optional.of(success.value), lifecycle);
            }
            if (dataResult instanceof Error) {
                Error error = (Error)dataResult;
                return new Error<R>(() -> DataResult.appendMessages(this.messageSupplier.get(), error.messageSupplier.get()), error.partialValue, lifecycle);
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public <R2> Error<R2> ap(DataResult<Function<R, R2>> dataResult) {
            Lifecycle lifecycle = this.lifecycle.add(dataResult.lifecycle());
            if (dataResult instanceof Success) {
                Success success = (Success)dataResult;
                return new Error(this.messageSupplier, this.partialValue.map((Function)success.value), lifecycle);
            }
            if (dataResult instanceof Error) {
                Error error = (Error)dataResult;
                return new Error(() -> DataResult.appendMessages(this.messageSupplier.get(), error.messageSupplier.get()), this.partialValue.flatMap((? super T object) -> error.partialValue.map((? super T function) -> function.apply(object))), lifecycle);
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Error<R> setPartial(Supplier<R> supplier) {
            return this.setPartial((Object)supplier.get());
        }

        @Override
        public Error<R> setPartial(R r) {
            return new Error<R>(this.messageSupplier, Optional.of(r), this.lifecycle);
        }

        @Override
        public Error<R> mapError(UnaryOperator<String> unaryOperator) {
            return new Error<R>(() -> (String)unaryOperator.apply(this.messageSupplier.get()), this.partialValue, this.lifecycle);
        }

        @Override
        public Error<R> setLifecycle(Lifecycle lifecycle) {
            if (this.lifecycle.equals(lifecycle)) {
                return this;
            }
            return new Error<R>(this.messageSupplier, this.partialValue, lifecycle);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public String toString() {
            return "DataResult.Error['" + this.message() + "'" + this.partialValue.map((? super T object) -> ": " + String.valueOf(object)).orElse("") + "]";
        }
    }

    public static enum Instance implements Applicative<com.mojang.serialization.DataResult$Mu, Mu>
    {
        INSTANCE;


        @Override
        public <T, R> App<com.mojang.serialization.DataResult$Mu, R> map(Function<? super T, ? extends R> function, App<com.mojang.serialization.DataResult$Mu, T> app) {
            return DataResult.unbox(app).map(function);
        }

        @Override
        public <A> App<com.mojang.serialization.DataResult$Mu, A> point(A a) {
            return DataResult.success(a);
        }

        @Override
        public <A, R> Function<App<com.mojang.serialization.DataResult$Mu, A>, App<com.mojang.serialization.DataResult$Mu, R>> lift1(App<com.mojang.serialization.DataResult$Mu, Function<A, R>> app) {
            return app2 -> this.ap(app, (App)app2);
        }

        @Override
        public <A, R> App<com.mojang.serialization.DataResult$Mu, R> ap(App<com.mojang.serialization.DataResult$Mu, Function<A, R>> app, App<com.mojang.serialization.DataResult$Mu, A> app2) {
            return DataResult.unbox(app2).ap(DataResult.unbox(app));
        }

        @Override
        public <A, B, R> App<com.mojang.serialization.DataResult$Mu, R> ap2(App<com.mojang.serialization.DataResult$Mu, BiFunction<A, B, R>> app, App<com.mojang.serialization.DataResult$Mu, A> app2, App<com.mojang.serialization.DataResult$Mu, B> app3) {
            DataResult<BiFunction<A, B, R>> dataResult = DataResult.unbox(app);
            DataResult<A> dataResult2 = DataResult.unbox(app2);
            DataResult<B> dataResult3 = DataResult.unbox(app3);
            if (dataResult.result().isPresent() && dataResult2.result().isPresent() && dataResult3.result().isPresent()) {
                return new Success<R>(dataResult.result().get().apply(dataResult2.result().get(), dataResult3.result().get()), dataResult.lifecycle().add(dataResult2.lifecycle()).add(dataResult3.lifecycle()));
            }
            return Applicative.super.ap2(app, app2, app3);
        }

        @Override
        public <T1, T2, T3, R> App<com.mojang.serialization.DataResult$Mu, R> ap3(App<com.mojang.serialization.DataResult$Mu, Function3<T1, T2, T3, R>> app, App<com.mojang.serialization.DataResult$Mu, T1> app2, App<com.mojang.serialization.DataResult$Mu, T2> app3, App<com.mojang.serialization.DataResult$Mu, T3> app4) {
            DataResult<Function3<T1, T2, T3, R>> dataResult = DataResult.unbox(app);
            DataResult<T1> dataResult2 = DataResult.unbox(app2);
            DataResult<T2> dataResult3 = DataResult.unbox(app3);
            DataResult<T3> dataResult4 = DataResult.unbox(app4);
            if (dataResult.result().isPresent() && dataResult2.result().isPresent() && dataResult3.result().isPresent() && dataResult4.result().isPresent()) {
                return new Success<R>(dataResult.result().get().apply(dataResult2.result().get(), dataResult3.result().get(), dataResult4.result().get()), dataResult.lifecycle().add(dataResult2.lifecycle()).add(dataResult3.lifecycle()).add(dataResult4.lifecycle()));
            }
            return Applicative.super.ap3(app, app2, app3, app4);
        }

        public static final class Mu
        implements Applicative.Mu {
        }
    }

    public static final class Mu
    implements K1 {
    }
}

