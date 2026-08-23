/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.kinds.App2;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.types.Func;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import java.util.function.Function;

public record View<A, B>(PointFree<Function<A, B>> function) implements App2<Mu, A, B>
{
    static <A, B> View<A, B> unbox(App2<Mu, A, B> app2) {
        return (View)app2;
    }

    public static <A> View<A, A> nopView(Type<A> type) {
        return new View(Functions.id(type));
    }

    public Type<A> type() {
        return ((Func)this.funcType()).first();
    }

    public Type<B> newType() {
        return ((Func)this.funcType()).second();
    }

    public Type<Function<A, B>> funcType() {
        return this.function.type();
    }

    @Override
    public String toString() {
        return "View[" + String.valueOf(this.function) + "," + String.valueOf(this.newType()) + "]";
    }

    public Optional<? extends View<A, B>> rewrite(PointFreeRule pointFreeRule) {
        return pointFreeRule.rewrite(this.function()).map(View::new);
    }

    public View<A, B> rewriteOrNop(PointFreeRule pointFreeRule) {
        return DataFixUtils.orElse(this.rewrite(pointFreeRule), this);
    }

    public <C> View<A, C> flatMap(Function<Type<B>, View<B, C>> function) {
        View<B, C> view = function.apply(this.newType());
        return new View<A, B>(Functions.comp(view.function(), this.function()));
    }

    public static <A, B> View<A, B> create(PointFree<Function<A, B>> pointFree) {
        return new View<A, B>(pointFree);
    }

    public static <A, B> View<A, B> create(String string, Type<A> type, Type<B> type2, Function<DynamicOps<?>, Function<A, B>> function) {
        return new View<A, B>(Functions.fun(string, function, type, type2));
    }

    public <C> View<C, B> compose(View<C, A> view) {
        if (this.isNop()) {
            return new View<A, B>(view.function());
        }
        if (view.isNop()) {
            return new View<A, B>(this.function());
        }
        return new View<A, B>(Functions.comp(this.function(), view.function()));
    }

    public boolean isNop() {
        return Functions.isId(this.function());
    }

    static final class Mu
    implements K2 {
        Mu() {
        }
    }
}

