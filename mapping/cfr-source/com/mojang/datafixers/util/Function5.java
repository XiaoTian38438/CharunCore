/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.util;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function5<T1, T2, T3, T4, T5, R> {
    public R apply(T1 var1, T2 var2, T3 var3, T4 var4, T5 var5);

    default public Function<T1, Function4<T2, T3, T4, T5, R>> curry() {
        return object -> (object2, object3, object4, object5) -> this.apply(object, object2, object3, object4, object5);
    }

    default public BiFunction<T1, T2, Function3<T3, T4, T5, R>> curry2() {
        return (object, object2) -> (object3, object4, object5) -> this.apply(object, object2, object3, object4, object5);
    }

    default public Function3<T1, T2, T3, BiFunction<T4, T5, R>> curry3() {
        return (object, object2, object3) -> (object4, object5) -> this.apply(object, object2, object3, object4, object5);
    }

    default public Function4<T1, T2, T3, T4, Function<T5, R>> curry4() {
        return (object, object2, object3, object4) -> object5 -> this.apply(object, object2, object3, object4, object5);
    }
}

