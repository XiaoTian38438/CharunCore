/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  javax.annotation.CheckReturnValue
 *  javax.annotation.Nullable
 */
package com.mojang.serialization;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

public class Dynamic<T>
extends DynamicLike<T> {
    private final T value;

    public Dynamic(DynamicOps<T> dynamicOps) {
        this(dynamicOps, dynamicOps.empty());
    }

    public Dynamic(DynamicOps<T> dynamicOps, @Nullable T t) {
        super(dynamicOps);
        this.value = t == null ? dynamicOps.empty() : t;
    }

    public T getValue() {
        return this.value;
    }

    public Dynamic<T> map(Function<? super T, ? extends T> function) {
        return new Dynamic<T>(this.ops, function.apply(this.value));
    }

    public <U> Dynamic<U> castTyped(DynamicOps<U> dynamicOps) {
        if (!Objects.equals(this.ops, dynamicOps)) {
            throw new IllegalStateException("Dynamic type doesn't match");
        }
        return this;
    }

    public <U> U cast(DynamicOps<U> dynamicOps) {
        return this.castTyped(dynamicOps).getValue();
    }

    public OptionalDynamic<T> merge(Dynamic<?> dynamic) {
        DataResult<Dynamic> dataResult = this.ops.mergeToList(this.value, dynamic.cast(this.ops));
        return new OptionalDynamic(this.ops, dataResult.map((? super R object) -> new Dynamic<Object>(this.ops, object)));
    }

    public OptionalDynamic<T> merge(Dynamic<?> dynamic, Dynamic<?> dynamic2) {
        DataResult<Dynamic> dataResult = this.ops.mergeToMap(this.value, dynamic.cast(this.ops), dynamic2.cast(this.ops));
        return new OptionalDynamic(this.ops, dataResult.map((? super R object) -> new Dynamic<Object>(this.ops, object)));
    }

    public DataResult<Map<Dynamic<T>, Dynamic<T>>> getMapValues() {
        return this.ops.getMapValues(this.value).map((? super R stream) -> {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            stream.forEach(pair -> builder.put(new Dynamic(this.ops, pair.getFirst()), new Dynamic(this.ops, pair.getSecond())));
            return builder.build();
        });
    }

    public Dynamic<T> updateMapValues(Function<Pair<Dynamic<?>, Dynamic<?>>, Pair<Dynamic<?>, Dynamic<?>>> function) {
        return DataFixUtils.orElse(this.getMapValues().map((? super R map) -> map.entrySet().stream().map((? super T entry) -> {
            Pair pair = (Pair)function.apply(Pair.of((Dynamic)entry.getKey(), (Dynamic)entry.getValue()));
            return Pair.of(((Dynamic)pair.getFirst()).castTyped(this.ops), ((Dynamic)pair.getSecond()).castTyped(this.ops));
        }).collect(Pair.toMap())).map(this::createMap).result(), this);
    }

    @Override
    public DataResult<Number> asNumber() {
        return this.ops.getNumberValue(this.value);
    }

    @Override
    public DataResult<String> asString() {
        return this.ops.getStringValue(this.value);
    }

    @Override
    public DataResult<Boolean> asBoolean() {
        return this.ops.getBooleanValue(this.value);
    }

    @Override
    public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
        return this.ops.getStream(this.value).map((? super R stream) -> stream.map((? super T object) -> new Dynamic<Object>(this.ops, object)));
    }

    @Override
    public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
        return this.ops.getMapValues(this.value).map((? super R stream) -> stream.map((? super T pair) -> Pair.of(new Dynamic(this.ops, pair.getFirst()), new Dynamic(this.ops, pair.getSecond()))));
    }

    @Override
    public DataResult<ByteBuffer> asByteBufferOpt() {
        return this.ops.getByteBuffer(this.value);
    }

    @Override
    public DataResult<IntStream> asIntStreamOpt() {
        return this.ops.getIntStream(this.value);
    }

    @Override
    public DataResult<LongStream> asLongStreamOpt() {
        return this.ops.getLongStream(this.value);
    }

    @Override
    public OptionalDynamic<T> get(String string) {
        return new OptionalDynamic(this.ops, this.ops.getMap(this.value).flatMap(mapLike -> {
            Object t = mapLike.get(string);
            if (t == null) {
                return DataResult.error(() -> "key missing: " + string + " in " + String.valueOf(this.value));
            }
            return DataResult.success(new Dynamic(this.ops, t));
        }));
    }

    @Override
    public DataResult<T> getGeneric(T t) {
        return this.ops.getGeneric(this.value, t);
    }

    @CheckReturnValue
    public Dynamic<T> remove(String string) {
        return this.map(object -> this.ops.remove(object, string));
    }

    @CheckReturnValue
    public Dynamic<T> set(String string, Dynamic<?> dynamic) {
        return this.map(object -> this.ops.set(object, string, dynamic.cast(this.ops)));
    }

    @CheckReturnValue
    public Dynamic<T> update(String string, Function<Dynamic<?>, Dynamic<?>> function) {
        return this.map(object2 -> this.ops.update(object2, string, object -> ((Dynamic)function.apply(new Dynamic<Object>(this.ops, object))).cast(this.ops)));
    }

    @CheckReturnValue
    public Dynamic<T> updateGeneric(T t, Function<T, T> function) {
        return this.map(object2 -> this.ops.updateGeneric(object2, t, function));
    }

    @CheckReturnValue
    public Dynamic<T> setFieldIfPresent(String string, Optional<? extends Dynamic<?>> optional) {
        if (optional.isEmpty()) {
            return this;
        }
        return this.set(string, optional.get());
    }

    @CheckReturnValue
    public Dynamic<T> renameField(String string, String string2) {
        return this.renameAndFixField(string, string2, UnaryOperator.identity());
    }

    @CheckReturnValue
    public Dynamic<T> replaceField(String string, String string2, Optional<? extends Dynamic<?>> optional) {
        return this.remove(string).setFieldIfPresent(string2, optional);
    }

    @CheckReturnValue
    public Dynamic<T> renameAndFixField(String string, String string2, UnaryOperator<Dynamic<?>> unaryOperator) {
        return this.remove(string).setFieldIfPresent(string2, this.get(string).result().map(unaryOperator));
    }

    @Override
    public DataResult<T> getElement(String string) {
        return this.getElementGeneric(this.ops.createString(string));
    }

    @Override
    public DataResult<T> getElementGeneric(T t) {
        return this.ops.getGeneric(this.value, t);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Dynamic dynamic = (Dynamic)object;
        return Objects.equals(this.ops, dynamic.ops) && Objects.equals(this.value, dynamic.value);
    }

    public int hashCode() {
        int n = this.value.hashCode();
        n = 31 * n + this.ops.hashCode();
        return n;
    }

    public String toString() {
        return String.format("%s[%s]", this.ops, this.value);
    }

    public <R> Dynamic<R> convert(DynamicOps<R> dynamicOps) {
        return new Dynamic<R>(dynamicOps, Dynamic.convert(this.ops, dynamicOps, this.value));
    }

    public <V> V into(Function<? super Dynamic<T>, ? extends V> function) {
        return function.apply(this);
    }

    @Override
    public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> decoder) {
        return decoder.decode(this.ops, this.value).map((? super R pair) -> pair.mapFirst(Function.identity()));
    }

    public static <S, T> T convert(DynamicOps<S> dynamicOps, DynamicOps<T> dynamicOps2, S s) {
        if (Objects.equals(dynamicOps, dynamicOps2)) {
            return (T)s;
        }
        return dynamicOps.convertTo(dynamicOps2, s);
    }

    @CheckReturnValue
    public static Dynamic<?> copyField(Dynamic<?> dynamic, String string, Dynamic<?> dynamic2, String string2) {
        return Dynamic.copyAndFixField(dynamic, string, dynamic2, string2, UnaryOperator.identity());
    }

    @CheckReturnValue
    public static <T> Dynamic<?> copyAndFixField(Dynamic<T> dynamic, String string, Dynamic<?> dynamic2, String string2, UnaryOperator<Dynamic<T>> unaryOperator) {
        Optional<Dynamic<T>> optional = dynamic.get(string).result();
        if (optional.isPresent()) {
            return dynamic2.set(string2, (Dynamic)unaryOperator.apply(optional.get()));
        }
        return dynamic2;
    }
}

