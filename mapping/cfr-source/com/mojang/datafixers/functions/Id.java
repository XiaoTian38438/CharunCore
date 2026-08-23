/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

final class Id<A>
extends PointFree<Function<A, A>> {
    private final Type<Function<A, A>> type;

    Id(Type<Function<A, A>> type) {
        this.type = type;
    }

    @Override
    public Type<Function<A, A>> type() {
        return this.type;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (!(object instanceof Id)) return false;
        Id id = (Id)object;
        if (!this.type.equals(id.type)) return false;
        return true;
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    public String toString(int n) {
        return "id";
    }

    @Override
    public Function<DynamicOps<?>, Function<A, A>> eval() {
        return dynamicOps -> Function.identity();
    }
}

