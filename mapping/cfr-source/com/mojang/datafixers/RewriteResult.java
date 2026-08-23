/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers;

import com.mojang.datafixers.View;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.RecursivePoint;
import java.util.BitSet;
import java.util.Objects;

public record RewriteResult<A, B>(View<A, B> view, BitSet recData) {
    public static <A, B> RewriteResult<A, B> create(View<A, B> view, BitSet bitSet) {
        return new RewriteResult<A, B>(view, bitSet);
    }

    public static <A> RewriteResult<A, A> nop(Type<A> type) {
        return new RewriteResult<A, A>(View.nopView(type), new BitSet());
    }

    public <C> RewriteResult<C, B> compose(RewriteResult<C, A> rewriteResult) {
        BitSet bitSet;
        if (this.view.type() instanceof RecursivePoint.RecursivePointType && rewriteResult.view.type() instanceof RecursivePoint.RecursivePointType) {
            bitSet = (BitSet)this.recData.clone();
            bitSet.or(rewriteResult.recData);
        } else {
            bitSet = this.recData;
        }
        return RewriteResult.create(this.view.compose(rewriteResult.view), bitSet);
    }

    @Override
    public String toString() {
        return "RR[" + String.valueOf(this.view) + "]";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        RewriteResult rewriteResult = (RewriteResult)object;
        return Objects.equals(this.view, rewriteResult.view);
    }

    @Override
    public int hashCode() {
        return this.view.hashCode();
    }
}

