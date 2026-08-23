/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package com.mojang.datafixers.kinds;

import com.google.common.collect.ImmutableList;
import java.util.List;

public interface Monoid<T> {
    public T point();

    public T add(T var1, T var2);

    public static <T> Monoid<List<T>> listMonoid() {
        return new Monoid<List<T>>(){

            @Override
            public List<T> point() {
                return ImmutableList.of();
            }

            @Override
            public List<T> add(List<T> list, List<T> list2) {
                ImmutableList.Builder builder = ImmutableList.builder();
                builder.addAll(list);
                builder.addAll(list2);
                return builder.build();
            }
        };
    }
}

