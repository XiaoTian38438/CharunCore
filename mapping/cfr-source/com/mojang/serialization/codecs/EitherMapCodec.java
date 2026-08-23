/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.stream.Stream;

public final class EitherMapCodec<F, S>
extends MapCodec<Either<F, S>> {
    private final MapCodec<F> first;
    private final MapCodec<S> second;

    public EitherMapCodec(MapCodec<F> mapCodec, MapCodec<S> mapCodec2) {
        this.first = mapCodec;
        this.second = mapCodec2;
    }

    @Override
    public <T> DataResult<Either<F, S>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
        DataResult<Either<F, Either>> dataResult = this.first.decode(dynamicOps, mapLike).map(Either::left);
        if (dataResult.isSuccess()) {
            return dataResult;
        }
        DataResult<Either<F, S>> dataResult2 = this.second.decode(dynamicOps, mapLike).map(Either::right);
        if (dataResult2.isSuccess()) {
            return dataResult2;
        }
        return dataResult.apply2((either, either2) -> either2, dataResult2);
    }

    @Override
    public <T> RecordBuilder<T> encode(Either<F, S> either, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
        return either.map(object -> this.first.encode(object, dynamicOps, recordBuilder), object -> this.second.encode(object, dynamicOps, recordBuilder));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        EitherMapCodec eitherMapCodec = (EitherMapCodec)object;
        return Objects.equals(this.first, eitherMapCodec.first) && Objects.equals(this.second, eitherMapCodec.second);
    }

    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    public String toString() {
        return "EitherMapCodec[" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + "]";
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
        return Stream.concat(this.first.keys(dynamicOps), this.second.keys(dynamicOps));
    }
}

