/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicLike;
import com.mojang.serialization.DynamicOps;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public final class OptionalDynamic<T>
extends DynamicLike<T> {
    private final DataResult<Dynamic<T>> delegate;

    public OptionalDynamic(DynamicOps<T> dynamicOps, DataResult<Dynamic<T>> dataResult) {
        super(dynamicOps);
        this.delegate = dataResult;
    }

    public DataResult<Dynamic<T>> get() {
        return this.delegate;
    }

    public Optional<Dynamic<T>> result() {
        return this.delegate.result();
    }

    public <U> DataResult<U> map(Function<? super Dynamic<T>, U> function) {
        return this.delegate.map(function);
    }

    public <U> DataResult<U> flatMap(Function<? super Dynamic<T>, ? extends DataResult<U>> function) {
        return this.delegate.flatMap(function);
    }

    @Override
    public DataResult<Number> asNumber() {
        return this.flatMap(DynamicLike::asNumber);
    }

    @Override
    public DataResult<String> asString() {
        return this.flatMap(DynamicLike::asString);
    }

    @Override
    public DataResult<Boolean> asBoolean() {
        return this.flatMap(DynamicLike::asBoolean);
    }

    @Override
    public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
        return this.flatMap(DynamicLike::asStreamOpt);
    }

    @Override
    public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
        return this.flatMap(DynamicLike::asMapOpt);
    }

    @Override
    public DataResult<ByteBuffer> asByteBufferOpt() {
        return this.flatMap(DynamicLike::asByteBufferOpt);
    }

    @Override
    public DataResult<IntStream> asIntStreamOpt() {
        return this.flatMap(DynamicLike::asIntStreamOpt);
    }

    @Override
    public DataResult<LongStream> asLongStreamOpt() {
        return this.flatMap(DynamicLike::asLongStreamOpt);
    }

    @Override
    public OptionalDynamic<T> get(String string) {
        return new OptionalDynamic<T>(this.ops, this.delegate.flatMap((? super R dynamic) -> dynamic.get((String)string).delegate));
    }

    @Override
    public DataResult<T> getGeneric(T t) {
        return this.flatMap(dynamic -> dynamic.getGeneric(t));
    }

    @Override
    public DataResult<T> getElement(String string) {
        return this.flatMap(dynamic -> dynamic.getElement(string));
    }

    @Override
    public DataResult<T> getElementGeneric(T t) {
        return this.flatMap(dynamic -> dynamic.getElementGeneric(t));
    }

    public Dynamic<T> orElseEmptyMap() {
        return this.result().orElseGet(this::emptyMap);
    }

    public Dynamic<T> orElseEmptyList() {
        return this.result().orElseGet(this::emptyList);
    }

    public <V> DataResult<V> into(Function<? super Dynamic<T>, ? extends V> function) {
        return this.delegate.map(function);
    }

    @Override
    public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> decoder) {
        return this.delegate.flatMap((? super R dynamic) -> dynamic.decode(decoder));
    }
}

