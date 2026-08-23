/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public record EitherCodec<F, S>(Codec<F> first, Codec<S> second) implements Codec<Either<F, S>>
{
    @Override
    public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        DataResult dataResult = this.first.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Either::left));
        if (dataResult.isSuccess()) {
            return dataResult;
        }
        DataResult dataResult2 = this.second.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Either::right));
        if (dataResult2.isSuccess()) {
            return dataResult2;
        }
        if (dataResult.hasResultOrPartial()) {
            return dataResult;
        }
        if (dataResult2.hasResultOrPartial()) {
            return dataResult2;
        }
        return DataResult.error(() -> "Failed to parse either. First: " + dataResult.error().orElseThrow().message() + "; Second: " + dataResult2.error().orElseThrow().message());
    }

    @Override
    public <T> DataResult<T> encode(Either<F, S> either, DynamicOps<T> dynamicOps, T t) {
        return either.map(object2 -> this.first.encode(object2, dynamicOps, t), object2 -> this.second.encode(object2, dynamicOps, t));
    }
}

