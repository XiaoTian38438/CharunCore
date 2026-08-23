/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.functions;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.functions.Apply;
import com.mojang.datafixers.functions.Bang;
import com.mojang.datafixers.functions.Comp;
import com.mojang.datafixers.functions.Fold;
import com.mojang.datafixers.functions.FunctionWrapper;
import com.mojang.datafixers.functions.Id;
import com.mojang.datafixers.functions.In;
import com.mojang.datafixers.functions.Out;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.ProfunctorTransformer;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.Algebra;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

public abstract class Functions {
    public static <A, B, C> PointFree<Function<A, C>> comp(PointFree<Function<B, C>> pointFree, PointFree<Function<A, B>> pointFree2) {
        if (Functions.isId(pointFree)) {
            return pointFree2;
        }
        if (Functions.isId(pointFree2)) {
            return pointFree;
        }
        if (pointFree instanceof Comp) {
            Comp comp = (Comp)pointFree;
            if (pointFree2 instanceof Comp) {
                Comp comp2 = (Comp)pointFree2;
                PointFree[] pointFreeArray = new PointFree[comp.functions.length + comp2.functions.length];
                System.arraycopy(comp.functions, 0, pointFreeArray, 0, comp.functions.length);
                System.arraycopy(comp2.functions, 0, pointFreeArray, comp.functions.length, comp2.functions.length);
                return new Comp(pointFreeArray);
            }
        }
        if (pointFree instanceof Comp) {
            Comp comp = (Comp)pointFree;
            PointFree[] pointFreeArray = new PointFree[comp.functions.length + 1];
            System.arraycopy(comp.functions, 0, pointFreeArray, 0, comp.functions.length);
            pointFreeArray[pointFreeArray.length - 1] = pointFree2;
            return new Comp(pointFreeArray);
        }
        if (pointFree2 instanceof Comp) {
            Comp comp = (Comp)pointFree2;
            PointFree[] pointFreeArray = new PointFree[1 + comp.functions.length];
            pointFreeArray[0] = pointFree;
            System.arraycopy(comp.functions, 0, pointFreeArray, 1, comp.functions.length);
            return new Comp(pointFreeArray);
        }
        return new Comp(pointFree, pointFree2);
    }

    public static <A, B> PointFree<Function<A, B>> fun(String string, Function<DynamicOps<?>, Function<A, B>> function, Type<A> type, Type<B> type2) {
        return new FunctionWrapper<A, B>(string, function, type, type2);
    }

    public static <A, B> PointFree<B> app(PointFree<Function<A, B>> pointFree, PointFree<A> pointFree2) {
        return new Apply<A, B>(pointFree, pointFree2);
    }

    public static <S, T, A, B> PointFree<Function<Function<A, B>, Function<S, T>>> profunctorTransformer(TypedOptic<S, T, A, B> typedOptic) {
        return new ProfunctorTransformer<S, T, A, B>(typedOptic);
    }

    public static <A> Bang<A> bang(Type<A> type) {
        return new Bang<A>(type);
    }

    public static <A> PointFree<Function<A, A>> in(RecursivePoint.RecursivePointType<A> recursivePointType) {
        return new In<A>(recursivePointType);
    }

    public static <A> PointFree<Function<A, A>> out(RecursivePoint.RecursivePointType<A> recursivePointType) {
        return new Out<A>(recursivePointType);
    }

    public static <A, B> PointFree<Function<A, B>> fold(RecursivePoint.RecursivePointType<A> recursivePointType, RecursivePoint.RecursivePointType<B> recursivePointType2, Algebra algebra, int n) {
        return new Fold<A, B>(recursivePointType, recursivePointType2, algebra, n);
    }

    public static <A> PointFree<Function<A, A>> id(Type<A> type) {
        return new Id<A>(DSL.func(type, type));
    }

    public static boolean isId(PointFree<?> pointFree) {
        return pointFree instanceof Id;
    }
}

