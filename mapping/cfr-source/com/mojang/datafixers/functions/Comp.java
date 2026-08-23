/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.types.Func;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

final class Comp<A, B>
extends PointFree<Function<A, B>> {
    protected final PointFree<? extends Function<?, ?>>[] functions;
    private final Type<Function<A, B>> type;

    protected Comp(PointFree<? extends Function<?, ?>> ... pointFreeArray) {
        this.functions = pointFreeArray;
        PointFree<Function<?, ?>> pointFree = pointFreeArray[0];
        PointFree<Function<?, ?>> pointFree2 = pointFreeArray[pointFreeArray.length - 1];
        this.type = DSL.func(((Func)pointFree2.type()).first(), ((Func)pointFree.type()).second());
    }

    protected Comp(PointFree<? extends Function<?, ?>>[] pointFreeArray, Type<Function<A, B>> type) {
        this.functions = pointFreeArray;
        this.type = type;
    }

    @Override
    public Type<Function<A, B>> type() {
        return this.type;
    }

    @Override
    public String toString(int n) {
        String string = Arrays.stream(this.functions).map(pointFree -> pointFree.toString(n + 1)).collect(Collectors.joining("\n" + Comp.indent(n + 1) + "\u25e6\n" + Comp.indent(n + 1)));
        return "(\n" + Comp.indent(n + 1) + string + "\n" + Comp.indent(n) + ")";
    }

    @Override
    public Optional<? extends PointFree<Function<A, B>>> all(PointFreeRule pointFreeRule) {
        ArrayList arrayList = new ArrayList(this.functions.length);
        boolean bl = false;
        for (PointFree<Function<?, ?>> pointFree : this.functions) {
            PointFree<? extends Function<?, ?>> pointFree2 = pointFreeRule.rewriteOrNop(pointFree);
            if (pointFree2 != pointFree) {
                bl = true;
                if (pointFree2 instanceof Comp) {
                    Comp comp = (Comp)pointFree2;
                    Collections.addAll(arrayList, comp.functions);
                    continue;
                }
                arrayList.add(pointFree2);
                continue;
            }
            arrayList.add(pointFree);
        }
        return Optional.of(bl ? new Comp<A, B>((PointFree[])arrayList.toArray(PointFree[]::new), this.type) : this);
    }

    @Override
    public Optional<? extends PointFree<Function<A, B>>> one(PointFreeRule pointFreeRule) {
        for (int i = 0; i < this.functions.length; ++i) {
            PointFree<Function<?, ?>> pointFree = this.functions[i];
            Optional<PointFree<Function<?, ?>>> optional = pointFreeRule.rewrite(pointFree);
            if (!optional.isPresent()) continue;
            PointFree<? extends Function<?, ?>>[] pointFreeArray = optional.get();
            if (pointFreeArray instanceof Comp) {
                Comp comp = (Comp)pointFreeArray;
                pointFreeArray = new PointFree[this.functions.length - 1 + comp.functions.length];
                System.arraycopy(this.functions, 0, pointFreeArray, 0, i);
                System.arraycopy(comp.functions, 0, pointFreeArray, i, comp.functions.length);
                System.arraycopy(this.functions, i + 1, pointFreeArray, i + comp.functions.length, this.functions.length - i - 1);
                return Optional.of(new Comp<A, B>(pointFreeArray, this.type));
            }
            pointFreeArray = Arrays.copyOf(this.functions, this.functions.length);
            pointFreeArray[i] = optional.get();
            return Optional.of(new Comp<A, B>(pointFreeArray, this.type));
        }
        return Optional.empty();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Comp comp = (Comp)object;
        return Arrays.equals(this.functions, comp.functions);
    }

    public int hashCode() {
        return Arrays.hashCode(this.functions);
    }

    @Override
    public Function<DynamicOps<?>, Function<A, B>> eval() {
        return dynamicOps -> object -> {
            Object object2 = object;
            for (int i = this.functions.length - 1; i >= 0; --i) {
                PointFree<Function<?, ?>> pointFree = this.functions[i];
                object2 = Comp.applyUnchecked(pointFree.evalCached().apply((DynamicOps<?>)dynamicOps), object2);
            }
            return object2;
        };
    }

    private static <A, B> B applyUnchecked(Function<A, B> function, Object object) {
        return function.apply(object);
    }
}

