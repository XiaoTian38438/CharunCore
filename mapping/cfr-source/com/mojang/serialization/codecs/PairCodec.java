/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;

public final class PairCodec<F, S>
implements Codec<Pair<F, S>> {
    private final Codec<F> first;
    private final Codec<S> second;

    public PairCodec(Codec<F> codec, Codec<S> codec2) {
        this.first = codec;
        this.second = codec2;
    }

    @Override
    public <T> DataResult<Pair<Pair<F, S>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        return this.first.decode(dynamicOps, t).flatMap((? super R pair) -> this.second.decode(dynamicOps, pair.getSecond()).map((? super R pair2) -> Pair.of(Pair.of(pair.getFirst(), pair2.getFirst()), pair2.getSecond())));
    }

    @Override
    public <T> DataResult<T> encode(Pair<F, S> pair, DynamicOps<T> dynamicOps, T t) {
        return this.second.encode(pair.getSecond(), dynamicOps, t).flatMap((? super R object) -> this.first.encode(pair.getFirst(), dynamicOps, object));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        PairCodec pairCodec = (PairCodec)object;
        return Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second);
    }

    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    public String toString() {
        return "PairCodec[" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + "]";
    }
}

