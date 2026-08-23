/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package com.mojang.datafixers.functions;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.View;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.Algebra;
import com.mojang.datafixers.types.families.ListAlgebra;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

final class Fold<A, B>
extends PointFree<Function<A, B>> {
    private static final Map<HmapCacheKey, IntFunction<RewriteResult<?, ?>>> HMAP_CACHE = Maps.newConcurrentMap();
    private static final Map<Pair<IntFunction<RewriteResult<?, ?>>, Integer>, RewriteResult<?, ?>> HMAP_APPLY_CACHE = Maps.newConcurrentMap();
    protected final RecursivePoint.RecursivePointType<A> aType;
    protected final RecursivePoint.RecursivePointType<B> bType;
    protected final Algebra algebra;
    protected final int index;

    public Fold(RecursivePoint.RecursivePointType<A> recursivePointType, RecursivePoint.RecursivePointType<B> recursivePointType2, Algebra algebra, int n) {
        this.aType = recursivePointType;
        this.bType = recursivePointType2;
        this.algebra = algebra;
        this.index = n;
    }

    @Override
    public Type<Function<A, B>> type() {
        return DSL.func(this.aType, this.bType);
    }

    @Override
    Optional<? extends PointFree<Function<A, B>>> all(PointFreeRule pointFreeRule) {
        int n = this.aType.family().size();
        ArrayList arrayList = new ArrayList(n);
        boolean bl = false;
        for (int i = 0; i < n; ++i) {
            RewriteResult<?, ?> rewriteResult = this.algebra.apply(i);
            PointFree<Function<?, ?>> pointFree = rewriteResult.view().function();
            PointFree<Function<?, ?>> pointFree2 = pointFreeRule.rewriteOrNop(pointFree);
            if (pointFree2 != pointFree) {
                arrayList.add(Fold.cap(rewriteResult, pointFree2));
                bl = true;
                continue;
            }
            arrayList.add(rewriteResult);
        }
        if (bl) {
            return Optional.of(new Fold<A, B>(this.aType, this.bType, new ListAlgebra("Rewrite all", arrayList), this.index));
        }
        return Optional.empty();
    }

    private static <A, B> RewriteResult<A, B> cap(RewriteResult<A, B> rewriteResult, PointFree<? extends Function<?, ?>> pointFree) {
        return RewriteResult.create(new View(pointFree), rewriteResult.recData());
    }

    private <FB> PointFree<Function<A, B>> cap(RewriteResult<?, FB> rewriteResult) {
        RewriteResult<?, ?> rewriteResult2 = this.algebra.apply(this.index);
        return Functions.comp(rewriteResult2.view().function(), rewriteResult.view().function());
    }

    @Override
    public Function<DynamicOps<?>, Function<A, B>> eval() {
        return dynamicOps -> object -> {
            RecursiveTypeFamily recursiveTypeFamily = this.aType.family();
            RecursiveTypeFamily recursiveTypeFamily2 = this.bType.family();
            IntFunction intFunction = HMAP_CACHE.computeIfAbsent(new HmapCacheKey(recursiveTypeFamily, recursiveTypeFamily2, this.algebra), hmapCacheKey -> hmapCacheKey.family().template().hmap(hmapCacheKey.family(), hmapCacheKey.family().fold(hmapCacheKey.algebra(), hmapCacheKey.newFamily())));
            RewriteResult rewriteResult = HMAP_APPLY_CACHE.computeIfAbsent(Pair.of(intFunction, this.index), pair -> (RewriteResult)((IntFunction)pair.getFirst()).apply((Integer)pair.getSecond()));
            PointFree<Function<A, B>> pointFree = this.cap(rewriteResult);
            return pointFree.evalCached().apply((DynamicOps<?>)dynamicOps).apply(object);
        };
    }

    @Override
    public String toString(int n) {
        return "fold(" + String.valueOf(this.aType) + ", " + this.index + ", \n" + Fold.indent(n + 1) + this.algebra.toString(n + 1) + "\n" + Fold.indent(n) + ")";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Fold fold = (Fold)object;
        return Objects.equals(this.aType, fold.aType) && Objects.equals(this.bType, fold.bType) && Objects.equals(this.algebra, fold.algebra);
    }

    public int hashCode() {
        int n = this.aType.hashCode();
        n = 31 * n + this.bType.hashCode();
        n = 31 * n + this.algebra.hashCode();
        return n;
    }

    private record HmapCacheKey(RecursiveTypeFamily family, RecursiveTypeFamily newFamily, Algebra algebra) {
    }
}

