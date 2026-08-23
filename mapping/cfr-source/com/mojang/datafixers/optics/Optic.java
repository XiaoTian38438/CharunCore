/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 */
package com.mojang.datafixers.optics;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.kinds.K2;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Optic<Proof extends K1, S, T, A, B> {
    public <P extends K2> Function<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Proof, P> var1);

    default public <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(Set<TypeToken<? extends K1>> set, TypeToken<Proof2> typeToken) {
        if (set.stream().allMatch(typeToken2 -> typeToken2.isSupertypeOf(typeToken))) {
            return Optional.of(this);
        }
        return Optional.empty();
    }

    public record CompositionOptic<Proof extends K1, S, T, A, B>(List<? extends Optic<? super Proof, ?, ?, ?, ?>> optics) implements Optic<Proof, S, T, A, B>
    {
        @Override
        public <P extends K2> Function<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Proof, P> app) {
            ArrayList arrayList = new ArrayList(this.optics.size());
            for (int i = this.optics.size() - 1; i >= 0; --i) {
                arrayList.add(this.optics.get(i).eval(app));
            }
            return app2 -> {
                App2 app22 = app2;
                for (Function function : arrayList) {
                    app22 = CompositionOptic.applyUnchecked(function, app22);
                }
                return app22;
            };
        }

        private static <P extends K2, T extends App2<P, ?, ?>> App2<P, ?, ?> applyUnchecked(Function<T, ? extends App2<P, ?, ?>> function, App2<P, ?, ?> app2) {
            return function.apply(app2);
        }

        @Override
        public String toString() {
            return "(" + this.optics.stream().map(Object::toString).collect(Collectors.joining(" \u25e6 ")) + ")";
        }
    }
}

