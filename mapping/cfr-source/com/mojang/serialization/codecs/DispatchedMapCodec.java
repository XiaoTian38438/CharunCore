/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
 */
package com.mojang.serialization.codecs;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public record DispatchedMapCodec<K, V>(Codec<K> keyCodec, Function<K, Codec<? extends V>> valueCodecFunction) implements Codec<Map<K, V>>
{
    @Override
    public <T> DataResult<T> encode(Map<K, V> map, DynamicOps<T> dynamicOps, T t) {
        RecordBuilder<T> recordBuilder = dynamicOps.mapBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            recordBuilder.add(this.keyCodec.encodeStart(dynamicOps, entry.getKey()), this.encodeValue(this.valueCodecFunction.apply(entry.getKey()), entry.getValue(), dynamicOps));
        }
        return recordBuilder.build(t);
    }

    private <T, V2 extends V> DataResult<T> encodeValue(Codec<V2> codec, V v, DynamicOps<T> dynamicOps) {
        return codec.encodeStart(dynamicOps, v);
    }

    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        return dynamicOps.getMap(t).flatMap((? super R mapLike) -> {
            Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
            Stream.Builder builder = Stream.builder();
            DataResult dataResult3 = mapLike.entries().reduce(DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (arg_0, arg_1) -> this.lambda$decode$0(dynamicOps, (Map)object2ObjectArrayMap, builder, arg_0, arg_1), (dataResult, dataResult2) -> dataResult.apply2stable((unit, unit2) -> unit, dataResult2));
            Pair<ImmutableMap, Object> pair = Pair.of(ImmutableMap.copyOf((Map)object2ObjectArrayMap), t);
            Object t = dynamicOps.createMap(builder.build());
            return dataResult3.map((? super R unit) -> pair).setPartial(pair).mapError(string -> string + " missed input: " + String.valueOf(t));
        });
    }

    private <T> DataResult<Unit> parseEntry(DataResult<Unit> dataResult, DynamicOps<T> dynamicOps, Pair<T, T> pair2, Map<K, V> map, Stream.Builder<Pair<T, T>> builder) {
        Object s;
        Object f;
        DataResult dataResult2;
        DataResult dataResult3 = this.keyCodec.parse(dynamicOps, pair2.getFirst());
        DataResult<Pair> dataResult4 = dataResult3.apply2stable(Pair::of, dataResult2 = dataResult3.map(this.valueCodecFunction).flatMap((? super R codec) -> codec.parse(dynamicOps, pair2.getSecond()).map(Function.identity())));
        Optional<Pair> optional = dataResult4.resultOrPartial();
        if (optional.isPresent() && map.putIfAbsent(f = optional.get().getFirst(), s = optional.get().getSecond()) != null) {
            builder.add(pair2);
            return dataResult.apply2stable((unit, object) -> unit, DataResult.error(() -> "Duplicate entry for key: '" + String.valueOf(f) + "'"));
        }
        if (dataResult4.isError()) {
            builder.add(pair2);
        }
        return dataResult.apply2stable((unit, pair) -> unit, dataResult4);
    }

    private /* synthetic */ DataResult lambda$decode$0(DynamicOps dynamicOps, Map map, Stream.Builder builder, DataResult dataResult, Pair pair) {
        return this.parseEntry(dataResult, dynamicOps, pair, map, builder);
    }
}

