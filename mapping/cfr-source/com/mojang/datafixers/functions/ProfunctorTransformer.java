/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.function.Function;

final class ProfunctorTransformer<S, T, A, B>
extends PointFree<Function<Function<A, B>, Function<S, T>>> {
    protected final TypedOptic<S, T, A, B> optic;

    public ProfunctorTransformer(TypedOptic<S, T, A, B> typedOptic) {
        this.optic = typedOptic;
    }

    public <S2, T2> ProfunctorTransformer<S2, T2, A, B> castOuterUnchecked(Type<S2> type, Type<T2> type2) {
        return new ProfunctorTransformer<S2, T2, A, B>(this.optic.castOuterUnchecked(type, type2));
    }

    @Override
    public Type<Function<Function<A, B>, Function<S, T>>> type() {
        return DSL.func(DSL.func(this.optic.aType(), this.optic.bType()), DSL.func(this.optic.sType(), this.optic.tType()));
    }

    @Override
    public String toString(int n) {
        return "Optic[" + String.valueOf(this.optic) + "]";
    }

    @Override
    public Function<DynamicOps<?>, Function<Function<A, B>, Function<S, T>>> eval() {
        Function function = this.optic.upCast(FunctionType.Instance.Mu.TYPE_TOKEN).orElseThrow().eval(FunctionType.Instance.INSTANCE);
        Function<Function, Function> function3 = function2 -> FunctionType.unbox((App2)function.apply(FunctionType.create(function2)));
        return dynamicOps -> function3;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)object;
        return Objects.equals(this.optic, profunctorTransformer.optic);
    }

    public int hashCode() {
        return this.optic.hashCode();
    }
}

