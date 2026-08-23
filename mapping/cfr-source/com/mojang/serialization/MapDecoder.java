/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.CompressorHolder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.KeyCompressor;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapLike;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface MapDecoder<A>
extends Keyable {
    public <T> DataResult<A> decode(DynamicOps<T> var1, MapLike<T> var2);

    default public <T> DataResult<A> compressedDecode(DynamicOps<T> dynamicOps, T t) {
        if (dynamicOps.compressMaps()) {
            Optional<Consumer<Consumer<T>>> optional = dynamicOps.getList(t).result();
            if (!optional.isPresent()) {
                return DataResult.error(() -> "Input is not a list");
            }
            final KeyCompressor<T> keyCompressor = this.compressor(dynamicOps);
            final ArrayList arrayList = new ArrayList();
            optional.get().accept(arrayList::add);
            MapLike mapLike2 = new MapLike<T>(){

                @Override
                @Nullable
                public T get(T t) {
                    return arrayList.get(keyCompressor.compress(t));
                }

                @Override
                @Nullable
                public T get(String string) {
                    return arrayList.get(keyCompressor.compress(string));
                }

                @Override
                public Stream<Pair<T, T>> entries() {
                    return IntStream.range(0, arrayList.size()).mapToObj(n -> Pair.of(keyCompressor.decompress(n), arrayList.get(n))).filter(pair -> pair.getSecond() != null);
                }
            };
            return this.decode(dynamicOps, mapLike2);
        }
        return dynamicOps.getMap(t).setLifecycle(Lifecycle.stable()).flatMap((? super R mapLike) -> this.decode(dynamicOps, (MapLike)mapLike));
    }

    public <T> KeyCompressor<T> compressor(DynamicOps<T> var1);

    default public Decoder<A> decoder() {
        return new Decoder<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return MapDecoder.this.compressedDecode(dynamicOps, t).map((? super R object2) -> Pair.of(object2, t));
            }

            public String toString() {
                return MapDecoder.this.toString();
            }
        };
    }

    default public <B> MapDecoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
        return new Implementation<B>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapDecoder.this.keys(dynamicOps);
            }

            @Override
            public <T> DataResult<B> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return MapDecoder.this.decode(dynamicOps, mapLike).flatMap((? super R object) -> ((DataResult)function.apply(object)).map(Function.identity()));
            }

            public String toString() {
                return MapDecoder.this.toString() + "[flatMapped]";
            }
        };
    }

    default public <B> MapDecoder<B> map(final Function<? super A, ? extends B> function) {
        return new Implementation<B>(){

            @Override
            public <T> DataResult<B> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return MapDecoder.this.decode(dynamicOps, mapLike).map(function);
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapDecoder.this.keys(dynamicOps);
            }

            public String toString() {
                return MapDecoder.this.toString() + "[mapped]";
            }
        };
    }

    default public <E> MapDecoder<E> ap(final MapDecoder<Function<? super A, ? extends E>> mapDecoder) {
        return new Implementation<E>(){

            @Override
            public <T> DataResult<E> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return MapDecoder.this.decode(dynamicOps, mapLike).flatMap((? super R object) -> mapDecoder.decode(dynamicOps, mapLike).map((? super R function) -> function.apply(object)));
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.concat(MapDecoder.this.keys(dynamicOps), mapDecoder.keys(dynamicOps));
            }

            public String toString() {
                return mapDecoder.toString() + " * " + MapDecoder.this.toString();
            }
        };
    }

    default public MapDecoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Implementation<A>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapDecoder.this.keys(dynamicOps);
            }

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return MapDecoder.this.decode(dynamicOps, mapLike).setLifecycle(lifecycle);
            }

            public String toString() {
                return MapDecoder.this.toString();
            }
        };
    }

    public static abstract class Implementation<A>
    extends CompressorHolder
    implements MapDecoder<A> {
    }
}

