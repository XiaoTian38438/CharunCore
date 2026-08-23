/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.function.Function;

final class In<A>
extends PointFree<Function<A, A>> {
    protected final RecursivePoint.RecursivePointType<A> type;

    public In(RecursivePoint.RecursivePointType<A> recursivePointType) {
        this.type = recursivePointType;
    }

    @Override
    public Type<Function<A, A>> type() {
        return DSL.func(this.type.unfold(), this.type);
    }

    @Override
    public String toString(int n) {
        return "In[" + String.valueOf(this.type) + "]";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof In && Objects.equals(this.type, ((In)object).type);
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    public Function<DynamicOps<?>, Function<A, A>> eval() {
        return dynamicOps -> Function.identity();
    }
}

