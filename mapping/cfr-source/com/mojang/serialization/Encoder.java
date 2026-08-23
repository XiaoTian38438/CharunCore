/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.FieldEncoder;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Encoder<A> {
    public <T> DataResult<T> encode(A var1, DynamicOps<T> var2, T var3);

    default public <T> DataResult<T> encodeStart(DynamicOps<T> dynamicOps, A a) {
        return this.encode(a, dynamicOps, dynamicOps.empty());
    }

    default public MapEncoder<A> fieldOf(String string) {
        return new FieldEncoder(string, this);
    }

    default public <B> Encoder<B> comap(final Function<? super B, ? extends A> function) {
        return new Encoder<B>(){

            @Override
            public <T> DataResult<T> encode(B b, DynamicOps<T> dynamicOps, T t) {
                return Encoder.this.encode(function.apply(b), dynamicOps, t);
            }

            public String toString() {
                return Encoder.this.toString() + "[comapped]";
            }
        };
    }

    default public <B> Encoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
        return new Encoder<B>(){

            @Override
            public <T> DataResult<T> encode(B b, DynamicOps<T> dynamicOps, T t) {
                return ((DataResult)function.apply(b)).flatMap(object2 -> Encoder.this.encode(object2, dynamicOps, t));
            }

            public String toString() {
                return Encoder.this.toString() + "[flatComapped]";
            }
        };
    }

    default public Encoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Encoder<A>(){

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return Encoder.this.encode(a, dynamicOps, t).setLifecycle(lifecycle);
            }

            public String toString() {
                return Encoder.this.toString();
            }
        };
    }

    public static <A> MapEncoder<A> empty() {
        return new MapEncoder.Implementation<A>(){

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return recordBuilder;
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.empty();
            }

            public String toString() {
                return "EmptyEncoder";
            }
        };
    }

    public static <A> Encoder<A> error(final String string) {
        return new Encoder<A>(){

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return DataResult.error(() -> string + " " + String.valueOf(a));
            }

            public String toString() {
                return "ErrorEncoder[" + string + "]";
            }
        };
    }
}

