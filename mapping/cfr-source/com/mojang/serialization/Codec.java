/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 */
package com.mojang.serialization;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.CompoundListCodec;
import com.mojang.serialization.codecs.DispatchedMapCodec;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.EitherMapCodec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import com.mojang.serialization.codecs.PairCodec;
import com.mojang.serialization.codecs.PairMapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.SimpleMapCodec;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import com.mojang.serialization.codecs.XorCodec;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public interface Codec<A>
extends Encoder<A>,
Decoder<A> {
    public static final PrimitiveCodec<Boolean> BOOL = new PrimitiveCodec<Boolean>(){

        @Override
        public <T> DataResult<Boolean> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getBooleanValue(t);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Boolean bl) {
            return dynamicOps.createBoolean(bl);
        }

        public String toString() {
            return "Bool";
        }
    };
    public static final PrimitiveCodec<Byte> BYTE = new PrimitiveCodec<Byte>(){

        @Override
        public <T> DataResult<Byte> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getNumberValue(t).map(Number::byteValue);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Byte by) {
            return dynamicOps.createByte(by);
        }

        public String toString() {
            return "Byte";
        }
    };
    public static final PrimitiveCodec<Short> SHORT = new PrimitiveCodec<Short>(){

        @Override
        public <T> DataResult<Short> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getNumberValue(t).map(Number::shortValue);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Short s) {
            return dynamicOps.createShort(s);
        }

        public String toString() {
            return "Short";
        }
    };
    public static final PrimitiveCodec<Integer> INT = new PrimitiveCodec<Integer>(){

        @Override
        public <T> DataResult<Integer> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getNumberValue(t).map(Number::intValue);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Integer n) {
            return dynamicOps.createInt(n);
        }

        public String toString() {
            return "Int";
        }
    };
    public static final PrimitiveCodec<Long> LONG = new PrimitiveCodec<Long>(){

        @Override
        public <T> DataResult<Long> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getNumberValue(t).map(Number::longValue);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Long l) {
            return dynamicOps.createLong(l);
        }

        public String toString() {
            return "Long";
        }
    };
    public static final PrimitiveCodec<Float> FLOAT = new PrimitiveCodec<Float>(){

        @Override
        public <T> DataResult<Float> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getNumberValue(t).map(Number::floatValue);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Float f) {
            return dynamicOps.createFloat(f.floatValue());
        }

        public String toString() {
            return "Float";
        }
    };
    public static final PrimitiveCodec<Double> DOUBLE = new PrimitiveCodec<Double>(){

        @Override
        public <T> DataResult<Double> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getNumberValue(t).map(Number::doubleValue);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, Double d) {
            return dynamicOps.createDouble(d);
        }

        public String toString() {
            return "Double";
        }
    };
    public static final PrimitiveCodec<String> STRING = new PrimitiveCodec<String>(){

        @Override
        public <T> DataResult<String> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getStringValue(t);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, String string) {
            return dynamicOps.createString(string);
        }

        public String toString() {
            return "String";
        }
    };
    public static final PrimitiveCodec<ByteBuffer> BYTE_BUFFER = new PrimitiveCodec<ByteBuffer>(){

        @Override
        public <T> DataResult<ByteBuffer> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getByteBuffer(t);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, ByteBuffer byteBuffer) {
            return dynamicOps.createByteList(byteBuffer);
        }

        public String toString() {
            return "ByteBuffer";
        }
    };
    public static final PrimitiveCodec<IntStream> INT_STREAM = new PrimitiveCodec<IntStream>(){

        @Override
        public <T> DataResult<IntStream> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getIntStream(t);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, IntStream intStream) {
            return dynamicOps.createIntList(intStream);
        }

        public String toString() {
            return "IntStream";
        }
    };
    public static final PrimitiveCodec<LongStream> LONG_STREAM = new PrimitiveCodec<LongStream>(){

        @Override
        public <T> DataResult<LongStream> read(DynamicOps<T> dynamicOps, T t) {
            return dynamicOps.getLongStream(t);
        }

        @Override
        public <T> T write(DynamicOps<T> dynamicOps, LongStream longStream) {
            return dynamicOps.createLongList(longStream);
        }

        public String toString() {
            return "LongStream";
        }
    };
    public static final Codec<Dynamic<?>> PASSTHROUGH = new Codec<Dynamic<?>>(){

        @Override
        public <T> DataResult<Pair<Dynamic<?>, T>> decode(DynamicOps<T> dynamicOps, T t) {
            return DataResult.success(Pair.of(new Dynamic<T>(dynamicOps, t), dynamicOps.empty()));
        }

        @Override
        public <T> DataResult<T> encode(Dynamic<?> dynamic, DynamicOps<T> dynamicOps, T t) {
            if (dynamic.getValue() == dynamic.getOps().empty()) {
                return DataResult.success(t, Lifecycle.experimental());
            }
            Object t2 = dynamic.convert(dynamicOps).getValue();
            if (t == dynamicOps.empty()) {
                return DataResult.success(t2, Lifecycle.experimental());
            }
            DataResult dataResult = dynamicOps.getMap(t2).flatMap((? super R mapLike) -> dynamicOps.mergeToMap(t, (MapLike)mapLike));
            return dataResult.result().map(DataResult::success).orElseGet(() -> {
                DataResult dataResult = dynamicOps.getStream(t2).flatMap((? super R stream) -> dynamicOps.mergeToList(t, stream.collect(Collectors.toList())));
                return dataResult.result().map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Don't know how to merge " + String.valueOf(t) + " and " + String.valueOf(t2), t, Lifecycle.experimental()));
            });
        }

        public String toString() {
            return "passthrough";
        }
    };
    public static final MapCodec<Unit> EMPTY = MapCodec.unit(Unit.INSTANCE);

    @Override
    default public Codec<A> withLifecycle(final Lifecycle lifecycle) {
        return new Codec<A>(){

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return Codec.this.encode(a, dynamicOps, t).setLifecycle(lifecycle);
            }

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return Codec.this.decode(dynamicOps, t).setLifecycle(lifecycle);
            }

            public String toString() {
                return Codec.this.toString();
            }
        };
    }

    default public Codec<A> stable() {
        return this.withLifecycle(Lifecycle.stable());
    }

    default public Codec<A> deprecated(int n) {
        return this.withLifecycle(Lifecycle.deprecated(n));
    }

    public static <A> Codec<A> of(Encoder<A> encoder, Decoder<A> decoder) {
        return Codec.of(encoder, decoder, "Codec[" + String.valueOf(encoder) + " " + String.valueOf(decoder) + "]");
    }

    public static <A> Codec<A> of(final Encoder<A> encoder, final Decoder<A> decoder, final String string) {
        return new Codec<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return decoder.decode(dynamicOps, t);
            }

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return encoder.encode(a, dynamicOps, t);
            }

            public String toString() {
                return string;
            }
        };
    }

    public static <A> MapCodec<A> of(MapEncoder<A> mapEncoder, MapDecoder<A> mapDecoder) {
        return Codec.of(mapEncoder, mapDecoder, () -> "MapCodec[" + String.valueOf(mapEncoder) + " " + String.valueOf(mapDecoder) + "]");
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

    public static <F, S> Codec<Pair<F, S>> pair(Codec<F> codec, Codec<S> codec2) {
        return new PairCodec<F, S>(codec, codec2);
    }

    public static <F, S> Codec<Either<F, S>> either(Codec<F> codec, Codec<S> codec2) {
        return new EitherCodec<F, S>(codec, codec2);
    }

    public static <F, S> Codec<Either<F, S>> xor(Codec<F> codec, Codec<S> codec2) {
        return new XorCodec<F, S>(codec, codec2);
    }

    public static <T> Codec<T> withAlternative(Codec<T> codec, Codec<? extends T> codec2) {
        return codec.withAlternative(codec2);
    }

    default public Codec<A> withAlternative(Codec<? extends A> codec) {
        return Codec.either(this, codec).xmap(Either::unwrap, Either::left);
    }

    public static <T, U> Codec<T> withAlternative(Codec<T> codec, Codec<U> codec2, Function<U, T> function) {
        return codec.withAlternative(codec2, function);
    }

    default public <U> Codec<A> withAlternative(Codec<U> codec, Function<U, A> function) {
        return Codec.either(this, codec).xmap(either -> either.map(object -> object, function), Either::left);
    }

    public static <F, S> MapCodec<Pair<F, S>> mapPair(MapCodec<F> mapCodec, MapCodec<S> mapCodec2) {
        return new PairMapCodec<F, S>(mapCodec, mapCodec2);
    }

    public static <F, S> MapCodec<Either<F, S>> mapEither(MapCodec<F> mapCodec, MapCodec<S> mapCodec2) {
        return new EitherMapCodec<F, S>(mapCodec, mapCodec2);
    }

    public static <E> Codec<List<E>> list(Codec<E> codec) {
        return Codec.list(codec, 0, Integer.MAX_VALUE);
    }

    public static <E> Codec<List<E>> list(Codec<E> codec, int n, int n2) {
        return new ListCodec<E>(codec, n, n2);
    }

    public static <K, V> Codec<List<Pair<K, V>>> compoundList(Codec<K> codec, Codec<V> codec2) {
        return new CompoundListCodec<K, V>(codec, codec2);
    }

    public static <K, V> SimpleMapCodec<K, V> simpleMap(Codec<K> codec, Codec<V> codec2, Keyable keyable) {
        return new SimpleMapCodec<K, V>(codec, codec2, keyable);
    }

    public static <K, V> UnboundedMapCodec<K, V> unboundedMap(Codec<K> codec, Codec<V> codec2) {
        return new UnboundedMapCodec<K, V>(codec, codec2);
    }

    public static <K, V> Codec<Map<K, V>> dispatchedMap(Codec<K> codec, Function<K, Codec<? extends V>> function) {
        return new DispatchedMapCodec<K, V>(codec, function);
    }

    public static <E> Codec<E> stringResolver(Function<E, String> function, Function<String, E> function2) {
        return STRING.flatXmap(string -> Optional.ofNullable(function2.apply((String)string)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + string)), object -> Optional.ofNullable((String)function.apply(object)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + String.valueOf(object))));
    }

    public static <F> MapCodec<Optional<F>> optionalField(String string, Codec<F> codec, boolean bl) {
        return new OptionalFieldCodec<F>(string, codec, bl);
    }

    public static <A> Codec<A> recursive(String string, Function<Codec<A>, Codec<A>> function) {
        return new RecursiveCodec(string, function);
    }

    public static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> supplier) {
        return new RecursiveCodec(supplier.toString(), codec -> (Codec)supplier.get());
    }

    default public Codec<List<A>> listOf() {
        return Codec.list(this);
    }

    default public Codec<List<A>> listOf(int n, int n2) {
        return Codec.list(this, n, n2);
    }

    default public Codec<List<A>> sizeLimitedListOf(int n) {
        return this.listOf(0, n);
    }

    default public <S> Codec<S> xmap(Function<? super A, ? extends S> function, Function<? super S, ? extends A> function2) {
        return Codec.of(this.comap(function2), this.map(function), this.toString() + "[xmapped]");
    }

    default public <S> Codec<S> comapFlatMap(Function<? super A, ? extends DataResult<? extends S>> function, Function<? super S, ? extends A> function2) {
        return Codec.of(this.comap(function2), this.flatMap(function), this.toString() + "[comapFlatMapped]");
    }

    default public <S> Codec<S> flatComapMap(Function<? super A, ? extends S> function, Function<? super S, ? extends DataResult<? extends A>> function2) {
        return Codec.of(this.flatComap(function2), this.map(function), this.toString() + "[flatComapMapped]");
    }

    default public <S> Codec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> function, Function<? super S, ? extends DataResult<? extends A>> function2) {
        return Codec.of(this.flatComap(function2), this.flatMap(function), this.toString() + "[flatXmapped]");
    }

    @Override
    default public MapCodec<A> fieldOf(String string) {
        return MapCodec.of(Encoder.super.fieldOf(string), Decoder.super.fieldOf(string), () -> "Field[" + string + ": " + this.toString() + "]");
    }

    default public MapCodec<Optional<A>> optionalFieldOf(String string) {
        return Codec.optionalField(string, this, false);
    }

    default public MapCodec<A> optionalFieldOf(String string, A a) {
        return this.optionalFieldOf(string, a, false);
    }

    default public MapCodec<A> optionalFieldOf(String string, A a, Lifecycle lifecycle) {
        return this.optionalFieldOf(string, Lifecycle.experimental(), a, lifecycle);
    }

    default public MapCodec<A> optionalFieldOf(String string, Lifecycle lifecycle, A a, Lifecycle lifecycle2) {
        return this.optionalFieldOf(string, lifecycle, a, lifecycle2, false);
    }

    default public MapCodec<Optional<A>> lenientOptionalFieldOf(String string) {
        return Codec.optionalField(string, this, true);
    }

    default public MapCodec<A> lenientOptionalFieldOf(String string, A a) {
        return this.optionalFieldOf(string, a, true);
    }

    default public MapCodec<A> lenientOptionalFieldOf(String string, A a, Lifecycle lifecycle) {
        return this.lenientOptionalFieldOf(string, Lifecycle.experimental(), a, lifecycle);
    }

    default public MapCodec<A> lenientOptionalFieldOf(String string, Lifecycle lifecycle, A a, Lifecycle lifecycle2) {
        return this.optionalFieldOf(string, lifecycle, a, lifecycle2, true);
    }

    private MapCodec<A> optionalFieldOf(String string, A a, boolean bl) {
        return Codec.optionalField(string, this, bl).xmap(optional -> optional.orElse(a), object2 -> Objects.equals(object2, a) ? Optional.empty() : Optional.of(object2));
    }

    private MapCodec<A> optionalFieldOf(String string, Lifecycle lifecycle, A a, Lifecycle lifecycle2, boolean bl) {
        return Codec.optionalField(string, this, bl).stable().flatXmap(optional -> optional.map((? super T object) -> DataResult.success(object, lifecycle)).orElse(DataResult.success(a, lifecycle2)), object2 -> Objects.equals(object2, a) ? DataResult.success(Optional.empty(), lifecycle2) : DataResult.success(Optional.of(object2), lifecycle));
    }

    default public Codec<A> mapResult(final ResultFunction<A> resultFunction) {
        return new Codec<A>(){

            @Override
            public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                return resultFunction.coApply(dynamicOps, a, Codec.this.encode(a, dynamicOps, t));
            }

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return resultFunction.apply(dynamicOps, t, Codec.this.decode(dynamicOps, t));
            }

            public String toString() {
                return String.valueOf(Codec.this) + "[mapResult " + String.valueOf(resultFunction) + "]";
            }
        };
    }

    default public Codec<A> orElse(Consumer<String> consumer, A a) {
        return this.orElse(DataFixUtils.consumerToFunction(consumer), a);
    }

    default public Codec<A> orElse(final UnaryOperator<String> unaryOperator, final A a) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> dynamicOps, T t, DataResult<Pair<A, T>> dataResult) {
                return DataResult.success(dataResult.mapError(unaryOperator).result().orElseGet(() -> Pair.of(a, t)));
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, A a2, DataResult<T> dataResult) {
                return dataResult.mapError(unaryOperator);
            }

            public String toString() {
                return "OrElse[" + String.valueOf(unaryOperator) + " " + String.valueOf(a) + "]";
            }
        });
    }

    default public Codec<A> orElseGet(Consumer<String> consumer, Supplier<? extends A> supplier) {
        return this.orElseGet(DataFixUtils.consumerToFunction(consumer), supplier);
    }

    default public Codec<A> orElseGet(final UnaryOperator<String> unaryOperator, final Supplier<? extends A> supplier) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> dynamicOps, T t, DataResult<Pair<A, T>> dataResult) {
                return DataResult.success(dataResult.mapError(unaryOperator).result().orElseGet(() -> 6.lambda$apply$0((Supplier)supplier, t)));
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, A a, DataResult<T> dataResult) {
                return dataResult.mapError(unaryOperator);
            }

            public String toString() {
                return "OrElseGet[" + String.valueOf(unaryOperator) + " " + String.valueOf(supplier.get()) + "]";
            }

            private static /* synthetic */ Pair lambda$apply$0(Supplier supplier2, Object object) {
                return Pair.of(supplier2.get(), object);
            }
        });
    }

    default public Codec<A> orElse(final A a) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> dynamicOps, T t, DataResult<Pair<A, T>> dataResult) {
                return DataResult.success(dataResult.result().orElseGet(() -> Pair.of(a, t)));
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, A a2, DataResult<T> dataResult) {
                return dataResult;
            }

            public String toString() {
                return "OrElse[" + String.valueOf(a) + "]";
            }
        });
    }

    default public Codec<A> orElseGet(final Supplier<? extends A> supplier) {
        return this.mapResult(new ResultFunction<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> dynamicOps, T t, DataResult<Pair<A, T>> dataResult) {
                return DataResult.success(dataResult.result().orElseGet(() -> 8.lambda$apply$0((Supplier)supplier, t)));
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> dynamicOps, A a, DataResult<T> dataResult) {
                return dataResult;
            }

            public String toString() {
                return "OrElseGet[" + String.valueOf(supplier.get()) + "]";
            }

            private static /* synthetic */ Pair lambda$apply$0(Supplier supplier2, Object object) {
                return Pair.of(supplier2.get(), object);
            }
        });
    }

    @Override
    default public Codec<A> promotePartial(Consumer<String> consumer) {
        return Codec.of(this, Decoder.super.promotePartial(consumer));
    }

    default public <E> Codec<E> dispatch(Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return this.dispatch("type", function, function2);
    }

    default public <E> Codec<E> dispatch(String string, Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return ((MapCodec)this.fieldOf(string)).dispatch(function, function2);
    }

    default public <E> Codec<E> dispatchStable(Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return ((MapCodec)this.fieldOf("type")).dispatchStable(function, function2);
    }

    default public <E> Codec<E> partialDispatch(String string, Function<? super E, ? extends DataResult<? extends A>> function, Function<? super A, ? extends DataResult<? extends MapCodec<? extends E>>> function2) {
        return ((MapCodec)this.fieldOf(string)).partialDispatch(function, function2);
    }

    default public <E> MapCodec<E> dispatchMap(Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return this.dispatchMap("type", function, function2);
    }

    default public <E> MapCodec<E> dispatchMap(String string, Function<? super E, ? extends A> function, Function<? super A, ? extends MapCodec<? extends E>> function2) {
        return ((MapCodec)this.fieldOf(string)).dispatchMap(function, function2);
    }

    default public Codec<A> validate(Function<A, DataResult<A>> function) {
        return this.flatXmap(function, function);
    }

    public static <N extends Number> Function<N, DataResult<N>> checkRange(N n, N n2) {
        return number3 -> {
            if (((Comparable)((Object)number3)).compareTo(n) >= 0 && ((Comparable)((Object)number3)).compareTo(n2) <= 0) {
                return DataResult.success(number3);
            }
            return DataResult.error(() -> "Value " + String.valueOf(number3) + " outside of range [" + String.valueOf(n) + ":" + String.valueOf(n2) + "]");
        };
    }

    public static Codec<Integer> intRange(int n, int n2) {
        Function<Integer, DataResult<Integer>> function = Codec.checkRange(n, n2);
        return INT.flatXmap(function, function);
    }

    public static Codec<Float> floatRange(float f, float f2) {
        Function<Float, DataResult<Float>> function = Codec.checkRange(Float.valueOf(f), Float.valueOf(f2));
        return FLOAT.flatXmap(function, function);
    }

    public static Codec<Double> doubleRange(double d, double d2) {
        Function<Double, DataResult<Double>> function = Codec.checkRange(d, d2);
        return DOUBLE.flatXmap(function, function);
    }

    public static Codec<String> string(int n, int n2) {
        return STRING.validate(string -> {
            int n3 = string.length();
            if (n3 < n) {
                return DataResult.error(() -> "String \"" + string + "\" is too short: " + n3 + ", expected range [" + n + "-" + n2 + "]");
            }
            if (n3 > n2) {
                return DataResult.error(() -> "String \"" + string + "\" is too long: " + n3 + ", expected range [" + n + "-" + n2 + "]");
            }
            return DataResult.success(string);
        });
    }

    public static Codec<String> sizeLimitedString(int n) {
        return Codec.string(0, n);
    }

    public static class RecursiveCodec<T>
    implements Codec<T> {
        private final String name;
        private final Supplier<Codec<T>> wrapped;

        private RecursiveCodec(String string, Function<Codec<T>, Codec<T>> function) {
            this.name = string;
            this.wrapped = Suppliers.memoize(() -> (Codec)function.apply(this));
        }

        @Override
        public <S> DataResult<Pair<T, S>> decode(DynamicOps<S> dynamicOps, S s) {
            return this.wrapped.get().decode(dynamicOps, s);
        }

        @Override
        public <S> DataResult<S> encode(T t, DynamicOps<S> dynamicOps, S s) {
            return this.wrapped.get().encode(t, dynamicOps, s);
        }

        public String toString() {
            return "RecursiveCodec[" + this.name + "]";
        }
    }

    public static interface ResultFunction<A> {
        public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> var1, T var2, DataResult<Pair<A, T>> var3);

        public <T> DataResult<T> coApply(DynamicOps<T> var1, A var2, DataResult<T> var3);
    }
}

