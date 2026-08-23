/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.util;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Function7<T1, T2, T3, T4, T5, T6, T7, R> {
    public R apply(T1 var1, T2 var2, T3 var3, T4 var4, T5 var5, T6 var6, T7 var7);

    default public Function<T1, Function6<T2, T3, T4, T5, T6, T7, R>> curry() {
        return object -> (object2, object3, object4, object5, object6, object7) -> this.apply(object, object2, object3, object4, object5, object6, object7);
    }

    default public BiFunction<T1, T2, Function5<T3, T4, T5, T6, T7, R>> curry2() {
        return (object, object2) -> (object3, object4, object5, object6, object7) -> this.apply(object, object2, object3, object4, object5, object6, object7);
    }

    default public Function3<T1, T2, T3, Function4<T4, T5, T6, T7, R>> curry3() {
        return (object, object2, object3) -> (object4, object5, object6, object7) -> this.apply(object, object2, object3, object4, object5, object6, object7);
    }

    default public Function4<T1, T2, T3, T4, Function3<T5, T6, T7, R>> curry4() {
        return (object, object2, object3, object4) -> (object5, object6, object7) -> this.apply(object, object2, object3, object4, object5, object6, object7);
    }

    default public Function5<T1, T2, T3, T4, T5, BiFunction<T6, T7, R>> curry5() {
        return (object, object2, object3, object4, object5) -> (object6, object7) -> this.apply(object, object2, object3, object4, object5, object6, object7);
    }

    default public Function6<T1, T2, T3, T4, T5, T6, Function<T7, R>> curry6() {
        return (object, object2, object3, object4, object5, object6) -> object7 -> this.apply(object, object2, object3, object4, object5, object6, object7);
    }
}

