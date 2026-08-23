/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package com.mojang.datafixers.optics;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Traversal;
import java.util.List;

public final class ListTraversal<A, B>
implements Traversal<List<A>, List<B>, A, B> {
    static final ListTraversal<?, ?> INSTANCE = new ListTraversal();

    private ListTraversal() {
    }

    @Override
    public <F extends K1> FunctionType<List<A>, App<F, List<B>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> functionType) {
        return list -> {
            App app = applicative.point(ImmutableList.builder());
            for (Object e : list) {
                app = applicative.ap2(applicative.point(ImmutableList.Builder::add), app, (App)functionType.apply((Object)e));
            }
            return applicative.map(ImmutableList.Builder::build, app);
        };
    }

    public String toString() {
        return "ListTraversal";
    }
}

