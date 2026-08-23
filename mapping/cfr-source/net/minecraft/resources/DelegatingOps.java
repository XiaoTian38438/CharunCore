/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public abstract class DelegatingOps<T>
implements DynamicOps<T> {
    protected final DynamicOps<T> delegate;

    protected DelegatingOps(DynamicOps<T> dynamicOps) {
        this.delegate = dynamicOps;
    }

    @Override
    public T empty() {
        return this.delegate.empty();
    }

    @Override
    public T emptyMap() {
        return this.delegate.emptyMap();
    }

    @Override
    public T emptyList() {
        return this.delegate.emptyList();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, T t) {
        if (Objects.equals(dynamicOps, this.delegate)) {
            return (U)t;
        }
        return this.delegate.convertTo(dynamicOps, t);
    }

    @Override
    public DataResult<Number> getNumberValue(T t) {
        return this.delegate.getNumberValue(t);
    }

    @Override
    public T createNumeric(Number number) {
        return this.delegate.createNumeric(number);
    }

    @Override
    public T createByte(byte by) {
        return this.delegate.createByte(by);
    }

    @Override
    public T createShort(short s) {
        return this.delegate.createShort(s);
    }

    @Override
    public T createInt(int n) {
        return this.delegate.createInt(n);
    }

    @Override
    public T createLong(long l) {
        return this.delegate.createLong(l);
    }

    @Override
    public T createFloat(float f) {
        return this.delegate.createFloat(f);
    }

    @Override
    public T createDouble(double d) {
        return this.delegate.createDouble(d);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(T t) {
        return this.delegate.getBooleanValue(t);
    }

    @Override
    public T createBoolean(boolean bl) {
        return this.delegate.createBoolean(bl);
    }

    @Override
    public DataResult<String> getStringValue(T t) {
        return this.delegate.getStringValue(t);
    }

    @Override
    public T createString(String string) {
        return this.delegate.createString(string);
    }

    @Override
    public DataResult<T> mergeToList(T t, T t2) {
        return this.delegate.mergeToList(t, t2);
    }

    @Override
    public DataResult<T> mergeToList(T t, List<T> list) {
        return this.delegate.mergeToList(t, list);
    }

    @Override
    public DataResult<T> mergeToMap(T t, T t2, T t3) {
        return this.delegate.mergeToMap(t, t2, t3);
    }

    @Override
    public DataResult<T> mergeToMap(T t, MapLike<T> mapLike) {
        return this.delegate.mergeToMap(t, mapLike);
    }

    @Override
    public DataResult<T> mergeToMap(T t, Map<T, T> map) {
        return this.delegate.mergeToMap(t, map);
    }

    @Override
    public DataResult<T> mergeToPrimitive(T t, T t2) {
        return this.delegate.mergeToPrimitive(t, t2);
    }

    @Override
    public DataResult<Stream<Pair<T, T>>> getMapValues(T t) {
        return this.delegate.getMapValues(t);
    }

    @Override
    public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T t) {
        return this.delegate.getMapEntries(t);
    }

    @Override
    public T createMap(Map<T, T> map) {
        return this.delegate.createMap(map);
    }

    @Override
    public T createMap(Stream<Pair<T, T>> stream) {
        return this.delegate.createMap(stream);
    }

    @Override
    public DataResult<MapLike<T>> getMap(T t) {
        return this.delegate.getMap(t);
    }

    @Override
    public DataResult<Stream<T>> getStream(T t) {
        return this.delegate.getStream(t);
    }

    @Override
    public DataResult<Consumer<Consumer<T>>> getList(T t) {
        return this.delegate.getList(t);
    }

    @Override
    public T createList(Stream<T> stream) {
        return this.delegate.createList(stream);
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(T t) {
        return this.delegate.getByteBuffer(t);
    }

    @Override
    public T createByteList(ByteBuffer byteBuffer) {
        return this.delegate.createByteList(byteBuffer);
    }

    @Override
    public DataResult<IntStream> getIntStream(T t) {
        return this.delegate.getIntStream(t);
    }

    @Override
    public T createIntList(IntStream intStream) {
        return this.delegate.createIntList(intStream);
    }

    @Override
    public DataResult<LongStream> getLongStream(T t) {
        return this.delegate.getLongStream(t);
    }

    @Override
    public T createLongList(LongStream longStream) {
        return this.delegate.createLongList(longStream);
    }

    @Override
    public T remove(T t, String string) {
        return this.delegate.remove(t, string);
    }

    @Override
    public boolean compressMaps() {
        return this.delegate.compressMaps();
    }

    @Override
    public ListBuilder<T> listBuilder() {
        return new DelegateListBuilder(this.delegate.listBuilder());
    }

    @Override
    public RecordBuilder<T> mapBuilder() {
        return new DelegateRecordBuilder(this.delegate.mapBuilder());
    }

    protected class DelegateListBuilder
    implements ListBuilder<T> {
        private final ListBuilder<T> original;

        protected DelegateListBuilder(ListBuilder<T> listBuilder) {
            this.original = listBuilder;
        }

        @Override
        public DynamicOps<T> ops() {
            return DelegatingOps.this;
        }

        @Override
        public DataResult<T> build(T t) {
            return this.original.build(t);
        }

        @Override
        public ListBuilder<T> add(T t) {
            this.original.add(t);
            return this;
        }

        @Override
        public ListBuilder<T> add(DataResult<T> dataResult) {
            this.original.add(dataResult);
            return this;
        }

        @Override
        public <E> ListBuilder<T> add(E e, Encoder<E> encoder) {
            this.original.add(encoder.encodeStart(this.ops(), e));
            return this;
        }

        @Override
        public <E> ListBuilder<T> addAll(Iterable<E> iterable, Encoder<E> encoder) {
            iterable.forEach(object -> this.original.add(encoder.encode(object, this.ops(), this.ops().empty())));
            return this;
        }

        @Override
        public ListBuilder<T> withErrorsFrom(DataResult<?> dataResult) {
            this.original.withErrorsFrom(dataResult);
            return this;
        }

        @Override
        public ListBuilder<T> mapError(UnaryOperator<String> unaryOperator) {
            this.original.mapError(unaryOperator);
            return this;
        }

        @Override
        public DataResult<T> build(DataResult<T> dataResult) {
            return this.original.build(dataResult);
        }
    }

    protected class DelegateRecordBuilder
    implements RecordBuilder<T> {
        private final RecordBuilder<T> original;

        protected DelegateRecordBuilder(RecordBuilder<T> recordBuilder) {
            this.original = recordBuilder;
        }

        @Override
        public DynamicOps<T> ops() {
            return DelegatingOps.this;
        }

        @Override
        public RecordBuilder<T> add(T t, T t2) {
            this.original.add(t, t2);
            return this;
        }

        @Override
        public RecordBuilder<T> add(T t, DataResult<T> dataResult) {
            this.original.add(t, dataResult);
            return this;
        }

        @Override
        public RecordBuilder<T> add(DataResult<T> dataResult, DataResult<T> dataResult2) {
            this.original.add(dataResult, dataResult2);
            return this;
        }

        @Override
        public RecordBuilder<T> add(String string, T t) {
            this.original.add(string, t);
            return this;
        }

        @Override
        public RecordBuilder<T> add(String string, DataResult<T> dataResult) {
            this.original.add(string, dataResult);
            return this;
        }

        @Override
        public <E> RecordBuilder<T> add(String string, E e, Encoder<E> encoder) {
            return this.original.add(string, encoder.encodeStart(this.ops(), e));
        }

        @Override
        public RecordBuilder<T> withErrorsFrom(DataResult<?> dataResult) {
            this.original.withErrorsFrom(dataResult);
            return this;
        }

        @Override
        public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
            this.original.setLifecycle(lifecycle);
            return this;
        }

        @Override
        public RecordBuilder<T> mapError(UnaryOperator<String> unaryOperator) {
            this.original.mapError(unaryOperator);
            return this;
        }

        @Override
        public DataResult<T> build(T t) {
            return this.original.build(t);
        }

        @Override
        public DataResult<T> build(DataResult<T> dataResult) {
            return this.original.build(dataResult);
        }
    }
}

