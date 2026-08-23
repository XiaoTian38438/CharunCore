/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package com.mojang.serialization;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.function.UnaryOperator;

public interface RecordBuilder<T> {
    public DynamicOps<T> ops();

    public RecordBuilder<T> add(T var1, T var2);

    public RecordBuilder<T> add(T var1, DataResult<T> var2);

    public RecordBuilder<T> add(DataResult<T> var1, DataResult<T> var2);

    public RecordBuilder<T> withErrorsFrom(DataResult<?> var1);

    public RecordBuilder<T> setLifecycle(Lifecycle var1);

    public RecordBuilder<T> mapError(UnaryOperator<String> var1);

    public DataResult<T> build(T var1);

    default public DataResult<T> build(DataResult<T> dataResult) {
        return dataResult.flatMap(this::build);
    }

    default public RecordBuilder<T> add(String string, T t) {
        return this.add(this.ops().createString(string), t);
    }

    default public RecordBuilder<T> add(String string, DataResult<T> dataResult) {
        return this.add(this.ops().createString(string), dataResult);
    }

    default public <E> RecordBuilder<T> add(String string, E e, Encoder<E> encoder) {
        return this.add(string, encoder.encodeStart(this.ops(), e));
    }

    public static final class MapBuilder<T>
    extends AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> {
        public MapBuilder(DynamicOps<T> dynamicOps) {
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
            return this.ops().mergeToMap(t, (Map<T, T>)builder.buildKeepingLast());
        }
    }

    public static abstract class AbstractUniversalBuilder<T, R>
    extends AbstractBuilder<T, R> {
        protected AbstractUniversalBuilder(DynamicOps<T> dynamicOps) {
            super(dynamicOps);
        }

        protected abstract R append(T var1, T var2, R var3);

        @Override
        public RecordBuilder<T> add(T t, T t2) {
            this.builder = this.builder.map(object3 -> this.append(t, t2, object3));
            return this;
        }

        @Override
        public RecordBuilder<T> add(T t, DataResult<T> dataResult) {
            this.builder = this.builder.apply2stable((object2, object3) -> this.append(t, object3, object2), dataResult);
            return this;
        }

        @Override
        public RecordBuilder<T> add(DataResult<T> dataResult, DataResult<T> dataResult2) {
            this.builder = this.builder.ap(dataResult.apply2stable((object, object2) -> object3 -> this.append(object, object2, object3), dataResult2));
            return this;
        }
    }

    public static abstract class AbstractStringBuilder<T, R>
    extends AbstractBuilder<T, R> {
        protected AbstractStringBuilder(DynamicOps<T> dynamicOps) {
            super(dynamicOps);
        }

        protected abstract R append(String var1, T var2, R var3);

        @Override
        public RecordBuilder<T> add(String string, T t) {
            this.builder = this.builder.map(object2 -> this.append(string, t, object2));
            return this;
        }

        @Override
        public RecordBuilder<T> add(String string, DataResult<T> dataResult) {
            this.builder = this.builder.apply2stable((object, object2) -> this.append(string, object2, object), dataResult);
            return this;
        }

        @Override
        public RecordBuilder<T> add(T t, T t2) {
            this.builder = this.ops().getStringValue(t).flatMap(string -> {
                this.add((T)string, t2);
                return this.builder;
            });
            return this;
        }

        @Override
        public RecordBuilder<T> add(T t, DataResult<T> dataResult) {
            this.builder = this.ops().getStringValue(t).flatMap(string -> {
                this.add((T)string, dataResult);
                return this.builder;
            });
            return this;
        }

        @Override
        public RecordBuilder<T> add(DataResult<T> dataResult, DataResult<T> dataResult2) {
            this.builder = dataResult.flatMap(this.ops()::getStringValue).flatMap(string -> {
                this.add((T)string, dataResult2);
                return this.builder;
            });
            return this;
        }
    }

    public static abstract class AbstractBuilder<T, R>
    implements RecordBuilder<T> {
        private final DynamicOps<T> ops;
        protected DataResult<R> builder = DataResult.success(this.initBuilder(), Lifecycle.stable());

        protected AbstractBuilder(DynamicOps<T> dynamicOps) {
            this.ops = dynamicOps;
        }

        @Override
        public DynamicOps<T> ops() {
            return this.ops;
        }

        protected abstract R initBuilder();

        protected abstract DataResult<T> build(R var1, T var2);

        @Override
        public DataResult<T> build(T t) {
            DataResult dataResult = this.builder.flatMap(object2 -> this.build(object2, t));
            this.builder = DataResult.success(this.initBuilder(), Lifecycle.stable());
            return dataResult;
        }

        @Override
        public RecordBuilder<T> withErrorsFrom(DataResult<?> dataResult) {
            this.builder = this.builder.flatMap(object -> dataResult.map(object2 -> object));
            return this;
        }

        @Override
        public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
            this.builder = this.builder.setLifecycle(lifecycle);
            return this;
        }

        @Override
        public RecordBuilder<T> mapError(UnaryOperator<String> unaryOperator) {
            this.builder = this.builder.mapError(unaryOperator);
            return this;
        }
    }
}

