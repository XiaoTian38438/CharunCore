/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public interface PrimitiveCodec<A>
extends Codec<A> {
    public <T> DataResult<A> read(DynamicOps<T> var1, T var2);

    public <T> T write(DynamicOps<T> var1, A var2);

    @Override
    default public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
        return this.read(dynamicOps, t).map((? super R object) -> Pair.of(object, dynamicOps.empty()));
    }

    @Override
    default public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
        return dynamicOps.mergeToPrimitive(t, this.write(dynamicOps, a));
    }
}

