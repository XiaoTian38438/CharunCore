/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.function.Function;

final class FunctionWrapper<A, B>
extends PointFree<Function<A, B>> {
    private final String name;
    protected final Function<DynamicOps<?>, Function<A, B>> fun;
    private final Type<Function<A, B>> type;

    FunctionWrapper(String string, Function<DynamicOps<?>, Function<A, B>> function, Type<A> type, Type<B> type2) {
        this.name = string;
        this.fun = function;
        this.type = DSL.func(type, type2);
    }

    @Override
    public Type<Function<A, B>> type() {
        return this.type;
    }

    @Override
    public String toString(int n) {
        return "fun[" + this.name + "]";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        FunctionWrapper functionWrapper = (FunctionWrapper)object;
        return Objects.equals(this.fun, functionWrapper.fun) && Objects.equals(this.type, functionWrapper.type);
    }

    public int hashCode() {
        return this.fun.hashCode();
    }

    @Override
    public Function<DynamicOps<?>, Function<A, B>> eval() {
        return this.fun;
    }
}

