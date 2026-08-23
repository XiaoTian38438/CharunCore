/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 */
package com.mojang.serialization;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.CompressorHolder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public abstract class MapCodec<A>
extends CompressorHolder
implements MapDecoder<A>,
MapEncoder<A> {
    public static <A> MapCodec<A> assumeMapUnsafe(final Codec<A> codec) {
        return new MapCodec<A>(){
            private static final String COMPRESSED_VALUE_KEY = "value";

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.of(dynamicOps.createString(COMPRESSED_VALUE_KEY));
            }

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                if (dynamicOps.compressMaps()) {
                    T t = mapLike.get(COMPRESSED_VALUE_KEY);
                    if (t == null) {
                        return DataResult.error(() -> "Missing value");
                    }
                    return codec.parse(dynamicOps, t);
                }
                return codec.parse(dynamicOps, dynamicOps.createMap(mapLike.entries()));
            }

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                DataResult<T> dataResult = codec.encodeStart(dynamicOps, a);
                if (dynamicOps.compressMaps()) {
                    return recordBuilder.add(COMPRESSED_VALUE_KEY, dataResult);
                }
                DataResult dataResult2 = dataResult.flatMap(dynamicOps::getMap);
                return dataResult2.map((? super R mapLike) -> {
                    mapLike.entries().forEach(pair -> recordBuilder.add(pair.getFirst(), pair.getSecond()));
                    return recordBuilder;
                }).result().orElseGet(() -> recordBuilder.withErrorsFrom(dataResult2));
            }
        };
    }

    public final <O> RecordCodecBuilder<O, A> forGetter(Function<O, A> function) {
        return RecordCodecBuilder.of(function, this);
    }

    public static <A> MapCodec<A> of(MapEncoder<A> mapEncoder, MapDecoder<A> mapDecoder) {
        return MapCodec.of(mapEncoder, mapDecoder, () -> "MapCodec[" + String.valueOf(mapEncoder) + " " + String.valueOf(mapDecoder) + "]");
    }

    public static <A> MapCodec<A> of(final MapEncoder<A> mapEncoder, final MapDecoder<A> mapDecoder, final Supplier<String> supplier) {
        return new MapCodec<A>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.concat(mapEncoder.keys(dynamicOps), mapDecoder.keys(dynamicOps));
            }

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return mapDecoder.decode(dynamicOps, mapLike);
            }

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return mapEncoder.encode(a, dynamicOps, recordBuilder);
            }

            public String toString() {
                return (String)supplier.get();
            }
        };
    }

    public static <A> MapCodec<A> recursive(String string, Function<Codec<A>, MapCodec<A>> function) {
        return new RecursiveMapCodec<A>(string, function);
    }

    public MapCodec<A> fieldOf(String string) {
        return this.codec().fieldOf(string);
    }

    @Override
    public MapCodec<A> withLifecycle(final Lifecycle lifecycle) {
        return new MapCodec<A>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapCodec.this.keys(dynamicOps);
            }

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return MapCodec.this.decode(dynamicOps, mapLike).setLifecycle(lifecycle);
            }

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return MapCodec.this.encode(a, dynamicOps, recordBuilder).setLifecycle(lifecycle);
            }

            public String toString() {
                return MapCodec.this.toString();
            }
        };
    }

    public Codec<A> codec() {
        return new MapCodecCodec(this);
    }

    public MapCodec<A> stable() {
        return this.withLifecycle(Lifecycle.stable());
    }

    public MapCodec<A> deprecated(int n) {
        return this.withLifecycle(Lifecycle.deprecated(n));
    }

    public <S> MapCodec<S> xmap(Function<? super A, ? extends S> function, Function<? super S, ? extends A> function2) {
        return MapCodec.of(this.comap(function2), this.map(function), () -> this.toString() + "[xmapped]");
    }

    public <S> MapCodec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> function, Function<? super S, ? extends DataResult<? extends A>> function2) {
        return Codec.of(this.flatComap(function2), this.flatMap(function), () -> this.toString() + "[flatXmapped]");
    }

    public MapCodec<A> validate(Function<A, DataResult<A>> function) {
        return this.flatXmap(function, function);
    }

    public <E> MapCodec<A> dependent(MapCodec<E> mapCodec, Function<A, Pair<E, MapCodec<E>>> function, BiFunction<A, E, A> biFunction) {
        return new Dependent<A, E>(this, mapCodec, function, biFunction);
    }

    public <E> Codec<E> dispatch(Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return this.partialDispatch(function.andThen(DataResult::success), function2.andThen(DataResult::success));
    }

    public <E> Codec<E> dispatchStable(Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return this.partialDispatch(object -> DataResult.success(function.apply((Object)object), Lifecycle.stable()), object -> DataResult.success((MapCodec)function2.apply((Object)object), Lifecycle.stable()));
    }

    public <E> Codec<E> partialDispatch(Function<? super E, ? extends DataResult<? extends A>> function, Function<? super A, ? extends DataResult<? extends MapCodec<? extends E>>> function2) {
        return new KeyDispatchCodec<A, E>(this, function, function2).codec();
    }

    public <E> MapCodec<E> dispatchMap(Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return new KeyDispatchCodec<A, E>(this, function.andThen(DataResult::success), function2.andThen(DataResult::success));
    }

    @Override
    public abstract <T> Stream<T> keys(DynamicOps<T> var1);

    public MapCodec<A> mapResult(final ResultFunction<A> resultFunction) {
        return new MapCodec<A>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return MapCodec.this.keys(dynamicOps);
            }

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return resultFunction.coApply(dynamicOps, a, MapCodec.this.encode(a, dynamicOps, recordBuilder));
            }

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return resultFunction.apply(dynamicOps, mapLike, MapCodec.this.decode(dynamicOps, mapLike));
            }

            public String toString() {
                return String.valueOf(MapCodec.this) + "[mapResult " + String.valueOf(resultFunction) + "]";
            }
        };
    }

    public MapCodec<A> orElse(Consumer<String> consumer, A a) {
        return this.orElse(DataFixUtils.consumerToFunction(consumer), a);
    }

    public MapCodec<A> orElse(final UnaryOperator<String> unaryOperator, final A a) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<A> apply(DynamicOps<T> dynamicOps, MapLike<T> mapLike, DataResult<A> dataResult) {
                return DataResult.success(dataResult.mapError(unaryOperator).result().orElse(a));
            }

            @Override
            public <T> RecordBuilder<T> coApply(DynamicOps<T> dynamicOps, A a2, RecordBuilder<T> recordBuilder) {
                return recordBuilder.mapError(unaryOperator);
            }

            public String toString() {
                return "OrElse[" + String.valueOf(unaryOperator) + " " + String.valueOf(a) + "]";
            }
        });
    }

    public MapCodec<A> orElseGet(Consumer<String> consumer, Supplier<? extends A> supplier) {
        return this.orElseGet(DataFixUtils.consumerToFunction(consumer), supplier);
    }

    public MapCodec<A> orElseGet(final UnaryOperator<String> unaryOperator, final Supplier<? extends A> supplier) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<A> apply(DynamicOps<T> dynamicOps, MapLike<T> mapLike, DataResult<A> dataResult) {
                return DataResult.success(dataResult.mapError(unaryOperator).result().orElseGet(supplier));
            }

            @Override
            public <T> RecordBuilder<T> coApply(DynamicOps<T> dynamicOps, A a, RecordBuilder<T> recordBuilder) {
                return recordBuilder.mapError(unaryOperator);
            }

            public String toString() {
                return "OrElseGet[" + String.valueOf(unaryOperator) + " " + String.valueOf(supplier.get()) + "]";
            }
        });
    }

    public MapCodec<A> orElse(final A a) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<A> apply(DynamicOps<T> dynamicOps, MapLike<T> mapLike, DataResult<A> dataResult) {
                return DataResult.success(dataResult.result().orElse(a));
            }

            @Override
            public <T> RecordBuilder<T> coApply(DynamicOps<T> dynamicOps, A a2, RecordBuilder<T> recordBuilder) {
                return recordBuilder;
            }

            public String toString() {
                return "OrElse[" + String.valueOf(a) + "]";
            }
        });
    }

    public MapCodec<A> orElseGet(final Supplier<? extends A> supplier) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<A> apply(DynamicOps<T> dynamicOps, MapLike<T> mapLike, DataResult<A> dataResult) {
                return DataResult.success(dataResult.result().orElseGet(supplier));
            }

            @Override
            public <T> RecordBuilder<T> coApply(DynamicOps<T> dynamicOps, A a, RecordBuilder<T> recordBuilder) {
                return recordBuilder;
            }

            public String toString() {
                return "OrElseGet[" + String.valueOf(supplier.get()) + "]";
            }
        });
    }

    public MapCodec<A> setPartial(final Supplier<A> supplier) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<A> apply(DynamicOps<T> dynamicOps, MapLike<T> mapLike, DataResult<A> dataResult) {
                return dataResult.setPartial((Object)supplier);
            }

            @Override
            public <T> RecordBuilder<T> coApply(DynamicOps<T> dynamicOps, A a, RecordBuilder<T> recordBuilder) {
                return recordBuilder;
            }

            public String toString() {
                return "SetPartial[" + String.valueOf(supplier) + "]";
            }
        });
    }

    public static <A> MapCodec<A> unit(A a) {
        return MapCodec.unit(() -> a);
    }

    public static <A> MapCodec<A> unit(final Supplier<A> supplier) {
        return new MapCodec<A>(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.empty();
            }

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return DataResult.success(supplier.get());
            }

            @Override
            public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return recordBuilder;
            }

            @Override
            public Codec<A> codec() {
                return 10.unitCodec(supplier);
            }

            public String toString() {
                return "Unit[" + String.valueOf(supplier.get()) + "]";
            }
        };
    }

    public static <A> Codec<A> unitCodec(A a) {
        return MapCodec.unitCodec(() -> a);
    }

    public static <A> Codec<A> unitCodec(final Supplier<A> supplier) {
        return new Codec<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                DataResult<Object> dataResult = dynamicOps.compressMaps() ? dynamicOps.getList(t) : dynamicOps.getMap(t);
                return dataResult.map(arg_0 -> 11.lambda$decode$0((Supplier)supplier, t, arg_0));
            }

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return dynamicOps.mergeToMap(t, MapLike.empty());
            }

            public String toString() {
                return "Unit[" + String.valueOf(supplier.get()) + "]";
            }

            private static /* synthetic */ Pair lambda$decode$0(Supplier supplier2, Object object, Object object2) {
                return Pair.of(supplier2.get(), object);
            }
        };
    }

    private static class RecursiveMapCodec<A>
    extends MapCodec<A> {
        private final String name;
        private final Supplier<MapCodec<A>> wrapped;

        private RecursiveMapCodec(String string, Function<Codec<A>, MapCodec<A>> function) {
            this.name = string;
            this.wrapped = Suppliers.memoize(() -> (MapCodec)function.apply(this.codec()));
        }

        @Override
        public <T> RecordBuilder<T> encode(A a, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
            return this.wrapped.get().encode(a, dynamicOps, recordBuilder);
        }

        @Override
        public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
            return this.wrapped.get().decode(dynamicOps, mapLike);
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
            return this.wrapped.get().keys(dynamicOps);
        }

        public String toString() {
            return "RecursiveMapCodec[" + this.name + "]";
        }
    }

    public record MapCodecCodec<A>(MapCodec<A> codec) implements Codec<A>
    {
        @Override
        public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
            return this.codec.compressedDecode(dynamicOps, t).map((? super R object2) -> Pair.of(object2, t));
        }

        @Override
        public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
            return this.codec.encode(a, dynamicOps, this.codec.compressedBuilder(dynamicOps)).build(t);
        }

        @Override
        public String toString() {
            return this.codec.toString();
        }
    }

    private static class Dependent<O, E>
    extends MapCodec<O> {
        private final MapCodec<E> initialInstance;
        private final Function<O, Pair<E, MapCodec<E>>> splitter;
        private final MapCodec<O> codec;
        private final BiFunction<O, E, O> combiner;

        public Dependent(MapCodec<O> mapCodec, MapCodec<E> mapCodec2, Function<O, Pair<E, MapCodec<E>>> function, BiFunction<O, E, O> biFunction) {
            this.initialInstance = mapCodec2;
            this.splitter = function;
            this.codec = mapCodec;
            this.combiner = biFunction;
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
            return Stream.concat(this.codec.keys(dynamicOps), this.initialInstance.keys(dynamicOps));
        }

        @Override
        public <T> DataResult<O> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
            return this.codec.decode(dynamicOps, mapLike).flatMap((? super R object) -> this.splitter.apply(object).getSecond().decode(dynamicOps, mapLike).map((? super R object2) -> this.combiner.apply(object, object2)).setLifecycle(Lifecycle.experimental()));
        }

        @Override
        public <T> RecordBuilder<T> encode(O o, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
            this.codec.encode(o, dynamicOps, recordBuilder);
            Pair<E, MapCodec<E>> pair = this.splitter.apply(o);
            pair.getSecond().encode(pair.getFirst(), dynamicOps, recordBuilder);
            return recordBuilder.setLifecycle(Lifecycle.experimental());
        }
    }

    public static interface ResultFunction<A> {
        public <T> DataResult<A> apply(DynamicOps<T> var1, MapLike<T> var2, DataResult<A> var3);

        public <T> RecordBuilder<T> coApply(DynamicOps<T> var1, A var2, RecordBuilder<T> var3);
    }
}

