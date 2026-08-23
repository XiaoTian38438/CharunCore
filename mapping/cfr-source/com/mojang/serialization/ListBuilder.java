/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package com.mojang.serialization;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import java.util.List;
import java.util.function.UnaryOperator;

public interface ListBuilder<T> {
    public DynamicOps<T> ops();

    public DataResult<T> build(T var1);

    public ListBuilder<T> add(T var1);

    public ListBuilder<T> add(DataResult<T> var1);

    public ListBuilder<T> withErrorsFrom(DataResult<?> var1);

    public ListBuilder<T> mapError(UnaryOperator<String> var1);

    default public DataResult<T> build(DataResult<T> dataResult) {
        return dataResult.flatMap(this::build);
    }

    default public <E> ListBuilder<T> add(E e, Encoder<E> encoder) {
        return this.add(encoder.encodeStart(this.ops(), e));
    }

    default public <E> ListBuilder<T> addAll(Iterable<E> iterable, Encoder<E> encoder) {
        iterable.forEach(object -> encoder.encode(object, this.ops(), this.ops().empty()));
        return this;
    }

    public static final class Builder<T>
    implements ListBuilder<T> {
        private final DynamicOps<T> ops;
        private DataResult<ImmutableList.Builder<T>> builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());

        public Builder(DynamicOps<T> dynamicOps) {
            this.ops = dynamicOps;
        }

        @Override
        public DynamicOps<T> ops() {
            return this.ops;
        }

        @Override
        public ListBuilder<T> add(T t) {
            this.builder = this.builder.map(builder -> builder.add(t));
            return this;
        }

        @Override
        public ListBuilder<T> add(DataResult<T> dataResult) {
            this.builder = this.builder.apply2stable(ImmutableList.Builder::add, dataResult);
            return this;
        }

        @Override
        public ListBuilder<T> withErrorsFrom(DataResult<?> dataResult) {
            this.builder = this.builder.flatMap(builder -> dataResult.map(object -> builder));
            return this;
        }

        @Override
        public ListBuilder<T> mapError(UnaryOperator<String> unaryOperator) {
            this.builder = this.builder.mapError(unaryOperator);
            return this;
        }

        @Override
        public DataResult<T> build(T t) {
            DataResult dataResult = this.builder.flatMap(builder -> this.ops.mergeToList(t, (List<Object>)builder.build()));
            this.builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
            return dataResult;
        }
    }
}

