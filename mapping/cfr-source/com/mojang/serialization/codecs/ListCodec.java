/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record ListCodec<E>(Codec<E> elementCodec, int minSize, int maxSize) implements Codec<List<E>>
{
    private <R> DataResult<R> createTooShortError(int n) {
        return DataResult.error(() -> "List is too short: " + n + ", expected range [" + this.minSize + "-" + this.maxSize + "]");
    }

    private <R> DataResult<R> createTooLongError(int n) {
        return DataResult.error(() -> "List is too long: " + n + ", expected range [" + this.minSize + "-" + this.maxSize + "]");
    }

    @Override
    public <T> DataResult<T> encode(List<E> list, DynamicOps<T> dynamicOps, T t) {
        if (list.size() < this.minSize) {
            return this.createTooShortError(list.size());
        }
        if (list.size() > this.maxSize) {
            return this.createTooLongError(list.size());
        }
        ListBuilder<T> listBuilder = dynamicOps.listBuilder();
        for (E e : list) {
            listBuilder.add(this.elementCodec.encodeStart(dynamicOps, e));
        }
        return listBuilder.build(t);
    }

    @Override
    public <T> DataResult<Pair<List<E>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        return dynamicOps.getList(t).setLifecycle(Lifecycle.stable()).flatMap((? super R consumer) -> {
            DecoderState decoderState = new DecoderState(dynamicOps);
            consumer.accept(decoderState::accept);
            return decoderState.build();
        });
    }

    @Override
    public String toString() {
        return "ListCodec[" + String.valueOf(this.elementCodec) + "]";
    }

    private class DecoderState<T> {
        private static final DataResult<Unit> INITIAL_RESULT = DataResult.success(Unit.INSTANCE, Lifecycle.stable());
        private final DynamicOps<T> ops;
        private final List<E> elements = new ArrayList();
        private final Stream.Builder<T> failed = Stream.builder();
        private DataResult<Unit> result = INITIAL_RESULT;
        private int totalCount;

        private DecoderState(DynamicOps<T> dynamicOps) {
            this.ops = dynamicOps;
        }

        public void accept(T t) {
            ++this.totalCount;
            if (this.elements.size() >= ListCodec.this.maxSize) {
                this.failed.add(t);
                return;
            }
            DataResult dataResult = ListCodec.this.elementCodec.decode(this.ops, t);
            dataResult.error().ifPresent(error -> this.failed.add(t));
            dataResult.resultOrPartial().ifPresent(pair -> this.elements.add(pair.getFirst()));
            this.result = this.result.apply2stable((unit, pair) -> unit, dataResult);
        }

        public DataResult<Pair<List<E>, T>> build() {
            if (this.elements.size() < ListCodec.this.minSize) {
                return ListCodec.this.createTooShortError(this.elements.size());
            }
            T t = this.ops.createList(this.failed.build());
            Pair pair = Pair.of(List.copyOf(this.elements), t);
            if (this.totalCount > ListCodec.this.maxSize) {
                this.result = ListCodec.this.createTooLongError(this.totalCount);
            }
            return this.result.map(unit -> pair).setPartial(pair);
        }
    }
}

