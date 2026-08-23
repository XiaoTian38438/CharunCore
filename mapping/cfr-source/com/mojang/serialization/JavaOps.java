/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  it.unimi.dsi.fastutil.bytes.ByteArrayList
 *  it.unimi.dsi.fastutil.bytes.ByteList
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 *  it.unimi.dsi.fastutil.longs.LongArrayList
 *  it.unimi.dsi.fastutil.longs.LongList
 *  javax.annotation.Nullable
 */
package com.mojang.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class JavaOps
implements DynamicOps<Object> {
    public static final JavaOps INSTANCE = new JavaOps();

    private JavaOps() {
    }

    @Override
    public Object empty() {
        return null;
    }

    @Override
    public Object emptyMap() {
        return Map.of();
    }

    @Override
    public Object emptyList() {
        return List.of();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, Object object) {
        if (object == null) {
            return dynamicOps.empty();
        }
        if (object instanceof Map) {
            return this.convertMap(dynamicOps, object);
        }
        if (object instanceof ByteList) {
            ByteList byteList = (ByteList)object;
            return dynamicOps.createByteList(ByteBuffer.wrap(byteList.toByteArray()));
        }
        if (object instanceof IntList) {
            IntList intList = (IntList)object;
            return dynamicOps.createIntList(intList.intStream());
        }
        if (object instanceof LongList) {
            LongList longList = (LongList)object;
            return dynamicOps.createLongList(longList.longStream());
        }
        if (object instanceof List) {
            return this.convertList(dynamicOps, object);
        }
        if (object instanceof String) {
            String string = (String)object;
            return dynamicOps.createString(string);
        }
        if (object instanceof Boolean) {
            Boolean bl = (Boolean)object;
            return dynamicOps.createBoolean(bl);
        }
        if (object instanceof Byte) {
            Byte by = (Byte)object;
            return dynamicOps.createByte(by);
        }
        if (object instanceof Short) {
            Short s = (Short)object;
            return dynamicOps.createShort(s);
        }
        if (object instanceof Integer) {
            Integer n = (Integer)object;
            return dynamicOps.createInt(n);
        }
        if (object instanceof Long) {
            Long l = (Long)object;
            return dynamicOps.createLong(l);
        }
        if (object instanceof Float) {
            Float f = (Float)object;
            return dynamicOps.createFloat(f.floatValue());
        }
        if (object instanceof Double) {
            Double d = (Double)object;
            return dynamicOps.createDouble(d);
        }
        if (object instanceof Number) {
            Number number = (Number)object;
            return dynamicOps.createNumeric(number);
        }
        throw new IllegalStateException("Don't know how to convert " + String.valueOf(object));
    }

    @Override
    public DataResult<Number> getNumberValue(Object object) {
        if (object instanceof Number) {
            Number number = (Number)object;
            return DataResult.success(number);
        }
        return DataResult.error(() -> "Not a number: " + String.valueOf(object));
    }

    @Override
    public Object createNumeric(Number number) {
        return number;
    }

    @Override
    public Object createByte(byte by) {
        return by;
    }

    @Override
    public Object createShort(short s) {
        return s;
    }

    @Override
    public Object createInt(int n) {
        return n;
    }

    @Override
    public Object createLong(long l) {
        return l;
    }

    @Override
    public Object createFloat(float f) {
        return Float.valueOf(f);
    }

    @Override
    public Object createDouble(double d) {
        return d;
    }

    @Override
    public DataResult<Boolean> getBooleanValue(Object object) {
        if (object instanceof Boolean) {
            Boolean bl = (Boolean)object;
            return DataResult.success(bl);
        }
        return DataResult.error(() -> "Not a boolean: " + String.valueOf(object));
    }

    @Override
    public Object createBoolean(boolean bl) {
        return bl;
    }

    @Override
    public DataResult<String> getStringValue(Object object) {
        if (object instanceof String) {
            String string = (String)object;
            return DataResult.success(string);
        }
        return DataResult.error(() -> "Not a string: " + String.valueOf(object));
    }

    @Override
    public Object createString(String string) {
        return string;
    }

    @Override
    public DataResult<Object> mergeToList(Object object, Object object2) {
        if (object == this.empty()) {
            return DataResult.success(List.of(object2));
        }
        if (object instanceof List) {
            List list = (List)object;
            if (list.isEmpty()) {
                return DataResult.success(List.of(object2));
            }
            return DataResult.success(ImmutableList.builder().addAll((Iterable)list).add(object2).build());
        }
        return DataResult.error(() -> "Not a list: " + String.valueOf(object));
    }

    @Override
    public DataResult<Object> mergeToList(Object object, List<Object> list) {
        if (object == this.empty()) {
            return DataResult.success(list);
        }
        if (object instanceof List) {
            List list2 = (List)object;
            if (list.isEmpty()) {
                return DataResult.success(list2);
            }
            if (list2.isEmpty()) {
                return DataResult.success(list);
            }
            return DataResult.success(ImmutableList.builder().addAll((Iterable)list2).addAll(list).build());
        }
        return DataResult.error(() -> "Not a list: " + String.valueOf(object));
    }

    @Override
    public DataResult<Object> mergeToMap(Object object, Object object2, Object object3) {
        if (object == this.empty()) {
            return DataResult.success(Map.of(object2, object3));
        }
        if (object instanceof Map) {
            Map map = (Map)object;
            if (map.isEmpty()) {
                return DataResult.success(Map.of(object2, object3));
            }
            ImmutableMap.Builder builder = ImmutableMap.builderWithExpectedSize((int)(map.size() + 1));
            builder.putAll(map);
            builder.put(object2, object3);
            return DataResult.success(builder.buildKeepingLast());
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(object));
    }

    @Override
    public DataResult<Object> mergeToMap(Object object, Map<Object, Object> map) {
        if (object == this.empty()) {
            return DataResult.success(map);
        }
        if (object instanceof Map) {
            Map map2 = (Map)object;
            if (map.isEmpty()) {
                return DataResult.success(map2);
            }
            if (map2.isEmpty()) {
                return DataResult.success(map);
            }
            ImmutableMap.Builder builder = ImmutableMap.builderWithExpectedSize((int)(map2.size() + map.size()));
            builder.putAll(map2);
            builder.putAll(map);
            return DataResult.success(builder.buildKeepingLast());
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(object));
    }

    private static Map<Object, Object> mapLikeToMap(MapLike<Object> mapLike) {
        return (Map)mapLike.entries().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    @Override
    public DataResult<Object> mergeToMap(Object object, MapLike<Object> mapLike) {
        if (object == this.empty()) {
            return DataResult.success(JavaOps.mapLikeToMap(mapLike));
        }
        if (object instanceof Map) {
            Map map = (Map)object;
            if (map.isEmpty()) {
                return DataResult.success(JavaOps.mapLikeToMap(mapLike));
            }
            Iterator iterator = mapLike.entries().iterator();
            if (!iterator.hasNext()) {
                return DataResult.success(map);
            }
            ImmutableMap.Builder builder = ImmutableMap.builderWithExpectedSize((int)map.size());
            builder.putAll(map);
            iterator.forEachRemaining(pair -> builder.put(pair.getFirst(), pair.getSecond()));
            return DataResult.success(builder.buildKeepingLast());
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(object));
    }

    private static Stream<Pair<Object, Object>> getMapEntries(Map<?, ?> map) {
        return map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object object) {
        if (object instanceof Map) {
            Map map = (Map)object;
            return DataResult.success(JavaOps.getMapEntries(map));
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(object));
    }

    @Override
    public DataResult<Consumer<BiConsumer<Object, Object>>> getMapEntries(Object object) {
        if (object instanceof Map) {
            Map map = (Map)object;
            return DataResult.success(map::forEach);
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(object));
    }

    @Override
    public Object createMap(Stream<Pair<Object, Object>> stream) {
        return stream.collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    @Override
    public DataResult<MapLike<Object>> getMap(Object object) {
        if (object instanceof Map) {
            final Map map = (Map)object;
            return DataResult.success(new MapLike<Object>(){

                @Override
                @Nullable
                public Object get(Object object) {
                    return map.get(object);
                }

                @Override
                @Nullable
                public Object get(String string) {
                    return map.get(string);
                }

                @Override
                public Stream<Pair<Object, Object>> entries() {
                    return JavaOps.getMapEntries(map);
                }

                public String toString() {
                    return "MapLike[" + String.valueOf(map) + "]";
                }
            });
        }
        return DataResult.error(() -> "Not a map: " + String.valueOf(object));
    }

    @Override
    public Object createMap(Map<Object, Object> map) {
        return map;
    }

    @Override
    public DataResult<Stream<Object>> getStream(Object object2) {
        if (object2 instanceof List) {
            List list = (List)object2;
            return DataResult.success(list.stream().map(object -> object));
        }
        return DataResult.error(() -> "Not an list: " + String.valueOf(object2));
    }

    @Override
    public DataResult<Consumer<Consumer<Object>>> getList(Object object) {
        if (object instanceof List) {
            List list = (List)object;
            return DataResult.success(list::forEach);
        }
        return DataResult.error(() -> "Not an list: " + String.valueOf(object));
    }

    @Override
    public Object createList(Stream<Object> stream) {
        return stream.toList();
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(Object object) {
        if (object instanceof ByteList) {
            ByteList byteList = (ByteList)object;
            return DataResult.success(ByteBuffer.wrap(byteList.toByteArray()));
        }
        return DataResult.error(() -> "Not a byte list: " + String.valueOf(object));
    }

    @Override
    public Object createByteList(ByteBuffer byteBuffer) {
        ByteBuffer byteBuffer2 = byteBuffer.duplicate().clear();
        ByteArrayList byteArrayList = new ByteArrayList();
        byteArrayList.size(byteBuffer2.capacity());
        byteBuffer2.get(0, byteArrayList.elements(), 0, byteArrayList.size());
        return byteArrayList;
    }

    @Override
    public DataResult<IntStream> getIntStream(Object object) {
        if (object instanceof IntList) {
            IntList intList = (IntList)object;
            return DataResult.success(intList.intStream());
        }
        return DataResult.error(() -> "Not an int list: " + String.valueOf(object));
    }

    @Override
    public Object createIntList(IntStream intStream) {
        return IntArrayList.toList((IntStream)intStream);
    }

    @Override
    public DataResult<LongStream> getLongStream(Object object) {
        if (object instanceof LongList) {
            LongList longList = (LongList)object;
            return DataResult.success(longList.longStream());
        }
        return DataResult.error(() -> "Not a long list: " + String.valueOf(object));
    }

    @Override
    public Object createLongList(LongStream longStream) {
        return LongArrayList.toList((LongStream)longStream);
    }

    @Override
    public Object remove(Object object, String string) {
        if (object instanceof Map) {
            Map map = (Map)object;
            LinkedHashMap linkedHashMap = new LinkedHashMap(map);
            linkedHashMap.remove(string);
            return Map.copyOf(linkedHashMap);
        }
        return object;
    }

    @Override
    public RecordBuilder<Object> mapBuilder() {
        return new FixedMapBuilder<Object>(this);
    }

    public String toString() {
        return "Java";
    }

    private static final class FixedMapBuilder<T>
    extends RecordBuilder.AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> {
        public FixedMapBuilder(DynamicOps<T> dynamicOps) {
            super(dynamicOps);
        }

        @Override
        protected ImmutableMap.Builder<T, T> initBuilder() {
            return ImmutableMap.builder();
        }

        @Override
        protected ImmutableMap.Builder<T, T> append(T t, T t2, ImmutableMap.Builder<T, T> builder) {
            return builder.put(t, t2);
        }

        @Override
        protected DataResult<T> build(ImmutableMap.Builder<T, T> builder, T t) {
            ImmutableMap immutableMap = builder.buildKeepingLast();
            return this.ops().mergeToMap(t, (Map<T, T>)immutableMap);
        }
    }
}

