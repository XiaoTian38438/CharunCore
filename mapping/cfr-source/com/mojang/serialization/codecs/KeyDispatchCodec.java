/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Function;
import java.util.stream.Stream;

public class KeyDispatchCodec<K, V>
extends MapCodec<V> {
    private static final String COMPRESSED_VALUE_KEY = "value";
    private final MapCodec<K> keyCodec;
    private final Function<? super V, ? extends DataResult<? extends K>> type;
    private final Function<? super K, ? extends DataResult<? extends MapDecoder<? extends V>>> decoder;
    private final Function<? super V, ? extends DataResult<? extends MapEncoder<V>>> encoder;

    protected KeyDispatchCodec(MapCodec<K> mapCodec, Function<? super V, ? extends DataResult<? extends K>> function, Function<? super K, ? extends DataResult<? extends MapDecoder<? extends V>>> function2, Function<? super V, ? extends DataResult<? extends MapEncoder<V>>> function3) {
        this.keyCodec = mapCodec;
        this.type = function;
        this.decoder = function2;
        this.encoder = function3;
    }

    public KeyDispatchCodec(MapCodec<K> mapCodec, Function<? super V, ? extends DataResult<? extends K>> function, Function<? super K, ? extends DataResult<? extends MapCodec<? extends V>>> function2) {
        this(mapCodec, function, function2, object -> KeyDispatchCodec.getCodec(function, function2, object));
    }

    @Override
    public <T> DataResult<V> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
        return this.keyCodec.decode(dynamicOps, mapLike).flatMap((? super R object) -> this.decoder.apply(object).flatMap((? super R mapDecoder) -> {
            if (dynamicOps.compressMaps()) {
                Object t = mapLike.get(dynamicOps.createString(COMPRESSED_VALUE_KEY));
                if (t == null) {
                    return DataResult.error(() -> "Input does not have a \"value\" entry: " + String.valueOf(mapLike));
                }
                return mapDecoder.decoder().parse(dynamicOps, t).map(Function.identity());
            }
            return mapDecoder.decode(dynamicOps, mapLike).map(Function.identity());
        }));
    }

    @Override
    public <T> RecordBuilder<T> encode(V v, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
        DataResult<MapEncoder<V>> dataResult = this.encoder.apply(v);
        DataResult<K> dataResult2 = this.type.apply(v);
        RecordBuilder<T> recordBuilder2 = recordBuilder.withErrorsFrom(dataResult).withErrorsFrom(dataResult2);
        if (dataResult.isError() || dataResult2.isError()) {
            return recordBuilder2;
        }
        MapEncoder<V> mapEncoder = dataResult.getOrThrow();
        K k = dataResult2.getOrThrow();
        if (dynamicOps.compressMaps()) {
            return this.keyCodec.encode(k, dynamicOps, recordBuilder2).add(COMPRESSED_VALUE_KEY, mapEncoder.encoder().encodeStart(dynamicOps, v));
        }
        RecordBuilder<T> recordBuilder3 = mapEncoder.encode(v, dynamicOps, recordBuilder2);
        return this.keyCodec.encode(k, dynamicOps, recordBuilder3);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
        return Stream.concat(this.keyCodec.keys(dynamicOps), Stream.of(dynamicOps.createString(COMPRESSED_VALUE_KEY)));
    }

    private static <K, V> DataResult<? extends MapEncoder<V>> getCodec(Function<? super V, ? extends DataResult<? extends K>> function, Function<? super K, ? extends DataResult<? extends MapEncoder<? extends V>>> function2, V v) {
        return function.apply(v).flatMap((? super R object) -> ((DataResult)function2.apply((Object)object)).map(Function.identity())).map((? super R mapEncoder) -> mapEncoder);
    }

    public String toString() {
        return "KeyDispatchCodec[" + this.keyCodec.toString() + " " + String.valueOf(this.type) + " " + String.valueOf(this.decoder) + "]";
    }
}

