/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;

public record XorCodec<F, S>(Codec<F> first, Codec<S> second) implements Codec<Either<F, S>>
{
    @Override
    public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        DataResult<Pair<Either<F, Pair>, T>> dataResult = this.first.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Either::left));
        DataResult<Pair<Either<F, S>, T>> dataResult2 = this.second.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Either::right));
        Optional<Pair> optional = dataResult.result();
        Optional<Pair> optional2 = dataResult2.result();
        if (optional.isPresent() && optional2.isPresent()) {
            return DataResult.error(() -> "Both alternatives read successfully, can not pick the correct one; first: " + String.valueOf(optional.get()) + " second: " + String.valueOf(optional2.get()), optional.get());
        }
        if (optional.isPresent()) {
            return dataResult;
        }
        if (optional2.isPresent()) {
            return dataResult2;
        }
        return dataResult.apply2((pair, pair2) -> pair2, dataResult2);
    }

    @Override
    public <T> DataResult<T> encode(Either<F, S> either, DynamicOps<T> dynamicOps, T t) {
        return either.map(object2 -> this.first.encode(object2, dynamicOps, t), object2 -> this.second.encode(object2, dynamicOps, t));
    }
}

