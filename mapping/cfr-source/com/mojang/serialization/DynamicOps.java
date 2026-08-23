/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package com.mojang.serialization;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface DynamicOps<T> {
    public T empty();

    default public T emptyMap() {
        return this.createMap((Map<T, T>)ImmutableMap.of());
    }

    default public T emptyList() {
        return this.createList(Stream.empty());
    }

    public <U> U convertTo(DynamicOps<U> var1, T var2);

    public DataResult<Number> getNumberValue(T var1);

    default public Number getNumberValue(T t, Number number) {
        return this.getNumberValue(t).result().orElse(number);
    }

    public T createNumeric(Number var1);

    default public T createByte(byte by) {
        return this.createNumeric(by);
    }

    default public T createShort(short s) {
        return this.createNumeric(s);
    }

    default public T createInt(int n) {
        return this.createNumeric(n);
    }

    default public T createLong(long l) {
        return this.createNumeric(l);
    }

    default public T createFloat(float f) {
        return this.createNumeric(Float.valueOf(f));
    }

    default public T createDouble(double d) {
        return this.createNumeric(d);
    }

    default public DataResult<Boolean> getBooleanValue(T t) {
        return this.getNumberValue(t).map(number -> number.byteValue() != 0);
    }

    default public T createBoolean(boolean bl) {
        return this.createByte((byte)(bl ? 1 : 0));
    }

    public DataResult<String> getStringValue(T var1);

    public T createString(String var1);

    public DataResult<T> mergeToList(T var1, T var2);

    default public DataResult<T> mergeToList(T t, List<T> list) {
        DataResult<Object> dataResult = DataResult.success(t);
        for (T t2 : list) {
            dataResult = dataResult.flatMap(object2 -> this.mergeToList(object2, t2));
        }
        return dataResult;
    }

    public DataResult<T> mergeToMap(T var1, T var2, T var3);

    default public DataResult<T> mergeToMap(T t, Map<T, T> map) {
        return this.mergeToMap(t, MapLike.forMap(map, this));
    }

    default public DataResult<T> mergeToMap(T t, MapLike<T> mapLike) {
        AtomicReference<DataResult<T>> atomicReference = new AtomicReference<DataResult<T>>(DataResult.success(t));
        mapLike.entries().forEach(pair -> atomicReference.setPlain(((DataResult)atomicReference.getPlain()).flatMap(object -> this.mergeToMap(object, pair.getFirst(), pair.getSecond()))));
        return atomicReference.getPlain();
    }

    default public DataResult<T> mergeToPrimitive(T t, T t2) {
        if (!Objects.equals(t, this.empty())) {
            return DataResult.error(() -> "Do not know how to append a primitive value " + String.valueOf(t2) + " to " + String.valueOf(t), t2);
        }
        return DataResult.success(t2);
    }

    public DataResult<Stream<Pair<T, T>>> getMapValues(T var1);

    default public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T t) {
        return this.getMapValues(t).map(stream -> biConsumer -> stream.forEach(pair -> biConsumer.accept(pair.getFirst(), pair.getSecond())));
    }

    public T createMap(Stream<Pair<T, T>> var1);

    default public DataResult<MapLike<T>> getMap(T t) {
        return this.getMapValues(t).flatMap(stream -> {
            try {
                return DataResult.success(MapLike.forMap(stream.collect(Pair.toMap()), this));
            }
            catch (IllegalStateException illegalStateException) {
                return DataResult.error(() -> "Error while building map: " + illegalStateException.getMessage());
            }
        });
    }

    default public T createMap(Map<T, T> map) {
        return this.createMap(map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())));
    }

    public DataResult<Stream<T>> getStream(T var1);

    default public DataResult<Consumer<Consumer<T>>> getList(T t) {
        return this.getStream(t).map(stream -> stream::forEach);
    }

    public T createList(Stream<T> var1);

    default public DataResult<ByteBuffer> getByteBuffer(T t) {
        return this.getStream(t).flatMap(stream -> {
            List list = stream.collect(Collectors.toList());
            if (list.stream().allMatch(object -> this.getNumberValue(object).isSuccess())) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[list.size()]);
                for (int i = 0; i < list.size(); ++i) {
                    byteBuffer.put(i, this.getNumberValue(list.get(i)).result().get().byteValue());
                }
                return DataResult.success(byteBuffer);
            }
            return DataResult.error(() -> "Some elements are not bytes: " + String.valueOf(t));
        });
    }

    default public T createByteList(ByteBuffer byteBuffer) {
        return (T)this.createList(IntStream.range(0, byteBuffer.capacity()).mapToObj(n -> this.createByte(byteBuffer.get(n))));
    }

    default public DataResult<IntStream> getIntStream(T t) {
        return this.getStream(t).flatMap(stream -> {
            List list = stream.toList();
            if (list.stream().allMatch(object -> this.getNumberValue(object).isSuccess())) {
                return DataResult.success(list.stream().mapToInt(object -> this.getNumberValue(object).getOrThrow().intValue()));
            }
            return DataResult.error(() -> "Some elements are not ints: " + String.valueOf(t));
        });
    }

    default public T createIntList(IntStream intStream) {
        return (T)this.createList(intStream.mapToObj(this::createInt));
    }

    default public DataResult<LongStream> getLongStream(T t) {
        return this.getStream(t).flatMap(stream -> {
            List list = stream.toList();
            if (list.stream().allMatch(object -> this.getNumberValue(object).isSuccess())) {
                return DataResult.success(list.stream().mapToLong(object -> this.getNumberValue(object).getOrThrow().longValue()));
            }
            return DataResult.error(() -> "Some elements are not longs: " + String.valueOf(t));
        });
    }

    default public T createLongList(LongStream longStream) {
        return (T)this.createList(longStream.mapToObj(this::createLong));
    }

    public T remove(T var1, String var2);

    default public boolean compressMaps() {
        return false;
    }

    default public DataResult<T> get(T t, String string) {
        return this.getGeneric(t, this.createString(string));
    }

    default public DataResult<T> getGeneric(T t, T t2) {
        return this.getMap(t).flatMap(mapLike -> Optional.ofNullable(mapLike.get(t2)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "No element " + String.valueOf(t2) + " in the map " + String.valueOf(t))));
    }

    default public T set(T t, String string, T t2) {
        return this.mergeToMap(t, this.createString(string), t2).result().orElse(t);
    }

    default public T update(T t, String string, Function<T, T> function) {
        return (T)this.get(t, string).map(object2 -> this.set(t, string, function.apply(object2))).result().orElse(t);
    }

    default public T updateGeneric(T t, T t2, Function<T, T> function) {
        return (T)this.getGeneric(t, t2).flatMap(object3 -> this.mergeToMap(t, t2, function.apply(object3))).result().orElse(t);
    }

    default public ListBuilder<T> listBuilder() {
        return new ListBuilder.Builder(this);
    }

    default public RecordBuilder<T> mapBuilder() {
        return new RecordBuilder.MapBuilder(this);
    }

    default public <E> Function<E, DataResult<T>> withEncoder(Encoder<E> encoder) {
        return object -> encoder.encodeStart(this, object);
    }

    default public <E> Function<T, DataResult<Pair<E, T>>> withDecoder(Decoder<E> decoder) {
        return object -> decoder.decode(this, object);
    }

    default public <E> Function<T, DataResult<E>> withParser(Decoder<E> decoder) {
        return object -> decoder.parse(this, object);
    }

    default public <U> U convertList(DynamicOps<U> dynamicOps, T t) {
        return (U)dynamicOps.createList(this.getStream(t).result().orElse(Stream.empty()).map(object -> this.convertTo(dynamicOps, object)));
    }

    default public <U> U convertMap(DynamicOps<U> dynamicOps, T t) {
        return dynamicOps.createMap(this.getMapValues(t).result().orElse(Stream.empty()).map(pair -> Pair.of(this.convertTo(dynamicOps, pair.getFirst()), this.convertTo(dynamicOps, pair.getSecond()))));
    }
}

