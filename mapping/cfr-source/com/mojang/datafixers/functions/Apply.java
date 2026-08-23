/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.types.Func;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class Apply<A, B>
extends PointFree<B> {
    protected final PointFree<Function<A, B>> func;
    protected final PointFree<A> arg;
    protected final Type<B> type;

    public Apply(PointFree<Function<A, B>> pointFree, PointFree<A> pointFree2) {
        this(pointFree, pointFree2, ((Func)pointFree.type()).second());
    }

    Apply(PointFree<Function<A, B>> pointFree, PointFree<A> pointFree2, Type<B> type) {
        this.func = pointFree;
        this.arg = pointFree2;
        this.type = type;
    }

    @Override
    public Function<DynamicOps<?>, B> eval() {
        return dynamicOps -> this.func.evalCached().apply((DynamicOps<?>)dynamicOps).apply(this.arg.evalCached().apply((DynamicOps<?>)dynamicOps));
    }

    @Override
    public Type<B> type() {
        return this.type;
    }

    @Override
    public String toString(int n) {
        return "(ap " + this.func.toString(n + 1) + "\n" + Apply.indent(n + 1) + this.arg.toString(n + 1) + "\n" + Apply.indent(n) + ")";
    }

    @Override
    public Optional<? extends PointFree<B>> all(PointFreeRule pointFreeRule) {
        PointFree<Function<A, B>> pointFree = pointFreeRule.rewriteOrNop(this.func);
        PointFree<A> pointFree2 = pointFreeRule.rewriteOrNop(this.arg);
        if (pointFree == this.func && pointFree2 == this.arg) {
            return Optional.of(this);
        }
        return Optional.of(new Apply<A, B>(pointFree, pointFree2, this.type));
    }

    @Override
    public Optional<? extends PointFree<B>> one(PointFreeRule pointFreeRule) {
        return pointFreeRule.rewrite(this.func).map(pointFree -> new Apply<A, B>(pointFree, this.arg, this.type)).or(() -> pointFreeRule.rewrite(this.arg).map(pointFree -> new Apply<A, B>(this.func, pointFree, this.type)));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Apply)) {
            return false;
        }
        Apply apply = (Apply)object;
        return Objects.equals(this.func, apply.func) && Objects.equals(this.arg, apply.arg);
    }

    public int hashCode() {
        int n = this.func.hashCode();
        n = 31 * n + this.arg.hashCode();
        return n;
    }
}

