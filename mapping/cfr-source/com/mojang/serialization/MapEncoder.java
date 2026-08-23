/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization;

import com.mojang.serialization.CompressorHolder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.KeyCompressor;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface MapEncoder<A>
extends Keyable {
    public <T> RecordBuilder<T> encode(A var1, DynamicOps<T> var2, RecordBuilder<T> var3);

    default public <T> RecordBuilder<T> compressedBuilder(DynamicOps<T> dynamicOps) {
        if (dynamicOps.compressMaps()) {
            return MapEncoder.makeCompressedBuilder(dynamicOps, this.compressor(dynamicOps));
        }
        return dynamicOps.mapBuilder();
    }

    public <T> KeyCompressor<T> compressor(DynamicOps<T> var1);

    default public <B> MapEncoder<B> comap(final Function<? super B, ? extends A> function) {
        return new Implementation<B>(){

            @Override
            public <T> RecordBuilder<T> encode(B b, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return MapEncoder.this.encode(function.apply(b), dynamicOps, recordBuilder);
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapEncoder.this.keys(dynamicOps);
            }

            public String toString() {
                return MapEncoder.this.toString() + "[comapped]";
            }
        };
    }

    default public <B> MapEncoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
        return new Implementation<B>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapEncoder.this.keys(dynamicOps);
            }

            @Override
            public <T> RecordBuilder<T> encode(B b, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                DataResult dataResult = (DataResult)function.apply(b);
                RecordBuilder recordBuilder2 = recordBuilder.withErrorsFrom(dataResult);
                return dataResult.map(object -> MapEncoder.this.encode(object, dynamicOps, recordBuilder2)).result().orElse(recordBuilder2);
            }

            public String toString() {
                return MapEncoder.this.toString() + "[flatComapped]";
            }
        };
    }

    default public Encoder<A> encoder() {
        return new Encoder<A>(){

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return MapEncoder.this.encode(a, dynamicOps, MapEncoder.this.compressedBuilder(dynamicOps)).build(t);
            }

            public String toString() {
                return MapEncoder.this.toString();
            }
        };
    }

    default public MapEncoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Implementation<A>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapEncoder.this.keys(dynamicOps);
            }

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return MapEncoder.this.encode(a, dynamicOps, recordBuilder).setLifecycle(lifecycle);
            }

            public String toString() {
                return MapEncoder.this.toString();
            }
        };
    }

    public static <T> RecordBuilder<T> makeCompressedBuilder(final DynamicOps<T> dynamicOps, final KeyCompressor<T> keyCompressor) {
        class CompressedRecordBuilder
        extends RecordBuilder.AbstractUniversalBuilder<T, List<T>> {
            private CompressedRecordBuilder() {
                super(dynamicOps2);
            }

            @Override
            protected List<T> initBuilder() {
                ArrayList<Object> arrayList = new ArrayList<Object>(keyCompressor.size());
                for (int i = 0; i < keyCompressor.size(); ++i) {
                    arrayList.add(null);
                }
                return arrayList;
            }

            @Override
            protected List<T> append(T t, T t2, List<T> list) {
                list.set(keyCompressor.compress(t), t2);
                return list;
            }

            @Override
            protected DataResult<T> build(List<T> list, T t) {
                return this.ops().mergeToList(t, list);
            }
        }
        return new CompressedRecordBuilder();
    }

    public static abstract class Implementation<A>
    extends CompressorHolder
    implements MapEncoder<A> {
    }
}

