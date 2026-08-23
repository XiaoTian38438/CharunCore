/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package com.mojang.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.kinds.ListBox;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public abstract class DynamicLike<T> {
    protected final DynamicOps<T> ops;

    public DynamicLike(DynamicOps<T> dynamicOps) {
        this.ops = dynamicOps;
    }

    public DynamicOps<T> getOps() {
        return this.ops;
    }

    public abstract DataResult<Number> asNumber();

    public abstract DataResult<String> asString();

    public abstract DataResult<Boolean> asBoolean();

    public abstract DataResult<Stream<Dynamic<T>>> asStreamOpt();

    public abstract DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt();

    public abstract DataResult<ByteBuffer> asByteBufferOpt();

    public abstract DataResult<IntStream> asIntStreamOpt();

    public abstract DataResult<LongStream> asLongStreamOpt();

    public abstract OptionalDynamic<T> get(String var1);

    public abstract DataResult<T> getGeneric(T var1);

    public abstract DataResult<T> getElement(String var1);

    public abstract DataResult<T> getElementGeneric(T var1);

    public abstract <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> var1);

    public <U> DataResult<List<U>> asListOpt(Function<Dynamic<T>, U> function) {
        return this.asStreamOpt().map(stream -> stream.map(function).collect(Collectors.toList()));
    }

    public <K, V> DataResult<Map<K, V>> asMapOpt(Function<Dynamic<T>, K> function, Function<Dynamic<T>, V> function2) {
        return this.asMapOpt().map(stream -> {
            ImmutableMap.Builder builder = ImmutableMap.builder();
            stream.forEach(pair -> builder.put(function.apply((Dynamic)pair.getFirst()), function2.apply((Dynamic)pair.getSecond())));
            return builder.build();
        });
    }

    public <A> DataResult<A> read(Decoder<? extends A> decoder) {
        return this.decode(decoder).map(Pair::getFirst);
    }

    public <E> DataResult<List<E>> readList(Decoder<E> decoder) {
        return this.asStreamOpt().map(stream -> stream.map(dynamic -> dynamic.read(decoder)).collect(Collectors.toList())).flatMap(list -> DataResult.unbox(ListBox.flip(DataResult.instance(), list)));
    }

    public <E> DataResult<List<E>> readList(Function<? super Dynamic<?>, ? extends DataResult<? extends E>> function) {
        return this.asStreamOpt().map(stream -> stream.map(function).map(dataResult -> dataResult.map(object -> object)).collect(Collectors.toList())).flatMap(list -> DataResult.unbox(ListBox.flip(DataResult.instance(), list)));
    }

    public <K, V> DataResult<List<Pair<K, V>>> readMap(Decoder<K> decoder, Decoder<V> decoder2) {
        return this.asMapOpt().map(stream -> stream.map(pair -> ((Dynamic)pair.getFirst()).read(decoder).flatMap(object -> ((Dynamic)pair.getSecond()).read(decoder2).map(object2 -> Pair.of(object, object2)))).collect(Collectors.toList())).flatMap(list -> DataResult.unbox(ListBox.flip(DataResult.instance(), list)));
    }

    public <K, V> DataResult<List<Pair<K, V>>> readMap(Decoder<K> decoder, Function<K, Decoder<V>> function) {
        return this.asMapOpt().map(stream -> stream.map(pair -> ((Dynamic)pair.getFirst()).read(decoder).flatMap(object -> ((Dynamic)pair.getSecond()).read((Decoder)function.apply(object)).map(object2 -> Pair.of(object, object2)))).collect(Collectors.toList())).flatMap(list -> DataResult.unbox(ListBox.flip(DataResult.instance(), list)));
    }

    public <R> DataResult<R> readMap(DataResult<R> dataResult, Function3<R, Dynamic<T>, Dynamic<T>, DataResult<R>> function3) {
        return this.asMapOpt().flatMap(stream -> {
            AtomicReference<DataResult> atomicReference = new AtomicReference<DataResult>(dataResult);
            stream.forEach(pair -> atomicReference.setPlain(((DataResult)atomicReference.getPlain()).flatMap(object -> (DataResult)function3.apply(object, (Dynamic)pair.getFirst(), (Dynamic)pair.getSecond()))));
            return atomicReference.getPlain();
        });
    }

    public Number asNumber(Number number) {
        return this.asNumber().result().orElse(number);
    }

    public int asInt(int n) {
        return this.asNumber(n).intValue();
    }

    public long asLong(long l) {
        return this.asNumber(l).longValue();
    }

    public float asFloat(float f) {
        return this.asNumber(Float.valueOf(f)).floatValue();
    }

    public double asDouble(double d) {
        return this.asNumber(d).doubleValue();
    }

    public byte asByte(byte by) {
        return this.asNumber(by).byteValue();
    }

    public short asShort(short s) {
        return this.asNumber(s).shortValue();
    }

    public boolean asBoolean(boolean bl) {
        return this.asBoolean().result().orElse(bl);
    }

    public String asString(String string) {
        return this.asString().result().orElse(string);
    }

    public Stream<Dynamic<T>> asStream() {
        return this.asStreamOpt().result().orElseGet(Stream::empty);
    }

    public ByteBuffer asByteBuffer() {
        return this.asByteBufferOpt().result().orElseGet(() -> ByteBuffer.wrap(new byte[0]));
    }

    public IntStream asIntStream() {
        return this.asIntStreamOpt().result().orElseGet(IntStream::empty);
    }

    public LongStream asLongStream() {
        return this.asLongStreamOpt().result().orElseGet(LongStream::empty);
    }

    public <U> List<U> asList(Function<Dynamic<T>, U> function) {
        return this.asListOpt(function).result().orElseGet(ImmutableList::of);
    }

    public <K, V> Map<K, V> asMap(Function<Dynamic<T>, K> function, Function<Dynamic<T>, V> function2) {
        return this.asMapOpt(function, function2).result().orElseGet(ImmutableMap::of);
    }

    public T getElement(String string, T t) {
        return this.getElement(string).result().orElse(t);
    }

    public T getElementGeneric(T t, T t2) {
        return this.getElementGeneric(t).result().orElse(t2);
    }

    public Dynamic<T> emptyList() {
        return new Dynamic<T>(this.ops, this.ops.emptyList());
    }

    public Dynamic<T> emptyMap() {
        return new Dynamic<T>(this.ops, this.ops.emptyMap());
    }

    public Dynamic<T> createNumeric(Number number) {
        return new Dynamic<T>(this.ops, this.ops.createNumeric(number));
    }

    public Dynamic<T> createByte(byte by) {
        return new Dynamic<T>(this.ops, this.ops.createByte(by));
    }

    public Dynamic<T> createShort(short s) {
        return new Dynamic<T>(this.ops, this.ops.createShort(s));
    }

    public Dynamic<T> createInt(int n) {
        return new Dynamic<T>(this.ops, this.ops.createInt(n));
    }

    public Dynamic<T> createLong(long l) {
        return new Dynamic<T>(this.ops, this.ops.createLong(l));
    }

    public Dynamic<T> createFloat(float f) {
        return new Dynamic<T>(this.ops, this.ops.createFloat(f));
    }

    public Dynamic<T> createDouble(double d) {
        return new Dynamic<T>(this.ops, this.ops.createDouble(d));
    }

    public Dynamic<T> createBoolean(boolean bl) {
        return new Dynamic<T>(this.ops, this.ops.createBoolean(bl));
    }

    public Dynamic<T> createString(String string) {
        return new Dynamic<T>(this.ops, this.ops.createString(string));
    }

    public Dynamic<T> createList(Stream<? extends Dynamic<?>> stream) {
        return new Dynamic<Object>(this.ops, this.ops.createList(stream.map(dynamic -> dynamic.cast(this.ops))));
    }

    public Dynamic<T> createMap(Map<? extends Dynamic<?>, ? extends Dynamic<?>> map) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (Map.Entry<Dynamic<?>, Dynamic<?>> entry : map.entrySet()) {
            builder.put(entry.getKey().cast(this.ops), entry.getValue().cast(this.ops));
        }
        return new Dynamic<T>(this.ops, this.ops.createMap((Map<T, T>)builder.build()));
    }

    public Dynamic<?> createByteList(ByteBuffer byteBuffer) {
        return new Dynamic<T>(this.ops, this.ops.createByteList(byteBuffer));
    }

    public Dynamic<?> createIntList(IntStream intStream) {
        return new Dynamic<T>(this.ops, this.ops.createIntList(intStream));
    }

    public Dynamic<?> createLongList(LongStream longStream) {
        return new Dynamic<T>(this.ops, this.ops.createLongList(longStream));
    }
}

