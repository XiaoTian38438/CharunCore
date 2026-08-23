/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalFieldCodec<A>
extends MapCodec<Optional<A>> {
    private final String name;
    private final Codec<A> elementCodec;
    private final boolean lenient;

    public OptionalFieldCodec(String string, Codec<A> codec, boolean bl) {
        this.name = string;
        this.elementCodec = codec;
        this.lenient = bl;
    }

    @Override
    public <T> DataResult<Optional<A>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
        T t = mapLike.get(this.name);
        if (t == null) {
            return DataResult.success(Optional.empty());
        }
        DataResult dataResult = this.elementCodec.parse(dynamicOps, t);
        if (dataResult.isError() && this.lenient) {
            return DataResult.success(Optional.empty());
        }
        return dataResult.map(Optional::of).setPartial(dataResult.resultOrPartial());
    }

    @Override
    public <T> RecordBuilder<T> encode(Optional<A> optional, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
        if (optional.isPresent()) {
            return recordBuilder.add(this.name, this.elementCodec.encodeStart(dynamicOps, optional.get()));
        }
        return recordBuilder;
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
        return Stream.of(dynamicOps.createString(this.name));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        OptionalFieldCodec optionalFieldCodec = (OptionalFieldCodec)object;
        return Objects.equals(this.name, optionalFieldCodec.name) && Objects.equals(this.elementCodec, optionalFieldCodec.elementCodec) && this.lenient == optionalFieldCodec.lenient;
    }

    public int hashCode() {
        return Objects.hash(this.name, this.elementCodec, this.lenient);
    }

    public String toString() {
        return "OptionalFieldCodec[" + this.name + ": " + String.valueOf(this.elementCodec) + "]";
    }
}

