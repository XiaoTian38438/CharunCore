/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 */
package com.mojang.serialization.codecs;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface BaseMapCodec<K, V> {
    public Codec<K> keyCodec();

    public Codec<V> elementCodec();

    default public <T> DataResult<Map<K, V>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
        Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
        Stream.Builder builder = Stream.builder();
        DataResult dataResult3 = mapLike.entries().reduce(DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (arg_0, arg_1) -> this.lambda$decode$3(dynamicOps, (Object2ObjectMap)object2ObjectArrayMap, builder, arg_0, arg_1), (dataResult, dataResult2) -> dataResult.apply2stable((unit, unit2) -> unit, dataResult2));
        ImmutableMap immutableMap = ImmutableMap.copyOf((Map)object2ObjectArrayMap);
        Object t = dynamicOps.createMap(builder.build());
        return dataResult3.map(arg_0 -> BaseMapCodec.lambda$decode$6((Map)immutableMap, arg_0)).setPartial((Map)immutableMap).mapError(string -> string + " missed input: " + String.valueOf(t));
    }

    default public <T> RecordBuilder<T> encode(Map<K, V> map, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            recordBuilder.add(this.keyCodec().encodeStart(dynamicOps, entry.getKey()), this.elementCodec().encodeStart(dynamicOps, entry.getValue()));
        }
        return recordBuilder;
    }

    private static /* synthetic */ Map lambda$decode$6(Map map, Unit unit) {
        return map;
    }

    private /* synthetic */ DataResult lambda$decode$3(DynamicOps dynamicOps, Object2ObjectMap object2ObjectMap, Stream.Builder builder, DataResult dataResult, Pair pair2) {
        Object object2;
        DataResult dataResult2;
        DataResult dataResult3 = this.keyCodec().parse(dynamicOps, pair2.getFirst());
        DataResult<Pair> dataResult4 = dataResult3.apply2stable(Pair::of, dataResult2 = this.elementCodec().parse(dynamicOps, pair2.getSecond()));
        Optional<Pair> optional = dataResult4.resultOrPartial();
        if (optional.isPresent() && (object2 = object2ObjectMap.putIfAbsent(optional.get().getFirst(), optional.get().getSecond())) != null) {
            builder.add(pair2);
            return dataResult.apply2stable((unit, object) -> unit, DataResult.error(() -> "Duplicate entry for key: '" + String.valueOf(((Pair)optional.get()).getFirst()) + "'"));
        }
        if (dataResult4.isError()) {
            builder.add(pair2);
        }
        return dataResult.apply2stable((unit, pair) -> unit, dataResult4);
    }
}

