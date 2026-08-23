/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  com.google.common.collect.Sets$SetView
 *  com.google.common.reflect.TypeToken
 */
package com.mojang.datafixers.functions;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.functions.Apply;
import com.mojang.datafixers.functions.Bang;
import com.mojang.datafixers.functions.Comp;
import com.mojang.datafixers.functions.Fold;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.ProfunctorTransformer;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.types.Func;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.constant.EmptyPart;
import com.mojang.datafixers.types.families.ListAlgebra;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.templates.Product;
import com.mojang.datafixers.types.templates.Sum;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PointFreeRule {
    public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> var1);

    default public <A> PointFree<A> rewriteOrNop(PointFree<A> pointFree) {
        return DataFixUtils.orElse(this.rewrite(pointFree), pointFree);
    }

    public static PointFreeRule nop() {
        return Nop.INSTANCE;
    }

    public static PointFreeRule seq(PointFreeRule ... pointFreeRuleArray) {
        return new Seq(pointFreeRuleArray);
    }

    public static PointFreeRule choice(PointFreeRule ... pointFreeRuleArray) {
        if (pointFreeRuleArray.length == 1) {
            return pointFreeRuleArray[0];
        }
        if (pointFreeRuleArray.length == 2) {
            return new Choice2(pointFreeRuleArray[0], pointFreeRuleArray[1]);
        }
        return new Choice(pointFreeRuleArray);
    }

    public static PointFreeRule all(PointFreeRule pointFreeRule) {
        return new All(pointFreeRule);
    }

    public static PointFreeRule one(PointFreeRule pointFreeRule) {
        return new One(pointFreeRule);
    }

    public static PointFreeRule once(PointFreeRule pointFreeRule) {
        return new Once(pointFreeRule);
    }

    public static PointFreeRule many(PointFreeRule pointFreeRule) {
        return new Many(pointFreeRule);
    }

    public static PointFreeRule everywhere(PointFreeRule pointFreeRule, PointFreeRule pointFreeRule2) {
        return new Everywhere(pointFreeRule, pointFreeRule2);
    }

    public static enum Nop implements PointFreeRule,
    Supplier<PointFreeRule>
    {
        INSTANCE;


        public <A> Optional<PointFree<A>> rewrite(PointFree<A> pointFree) {
            return Optional.of(pointFree);
        }

        @Override
        public PointFreeRule get() {
            return this;
        }
    }

    public record Seq(PointFreeRule[] rules) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            PointFree<A> pointFree2 = pointFree;
            for (PointFreeRule pointFreeRule : this.rules) {
                pointFree2 = pointFreeRule.rewriteOrNop(pointFree2);
            }
            return Optional.of(pointFree2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Seq)) return false;
            Seq seq = (Seq)object;
            if (!Arrays.equals(this.rules, seq.rules)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.rules);
        }
    }

    public record Choice2(PointFreeRule first, PointFreeRule second) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            Optional<PointFree<A>> optional = this.first.rewrite(pointFree);
            if (optional.isPresent()) {
                return optional;
            }
            return this.second.rewrite(pointFree);
        }
    }

    public record Choice(PointFreeRule[] rules) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            for (PointFreeRule pointFreeRule : this.rules) {
                Optional<PointFree<A>> optional = pointFreeRule.rewrite(pointFree);
                if (!optional.isPresent()) continue;
                return optional;
            }
            return Optional.empty();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Choice)) return false;
            Choice choice = (Choice)object;
            if (!Arrays.equals(this.rules, choice.rules)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.rules);
        }
    }

    public record All(PointFreeRule rule) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            return pointFree.all(this.rule);
        }
    }

    public record One(PointFreeRule rule) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            return pointFree.one(this.rule);
        }
    }

    public record Once(PointFreeRule rule) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            Optional<PointFree<A>> optional = this.rule.rewrite(pointFree);
            if (optional.isPresent()) {
                return optional;
            }
            return pointFree.one(this);
        }
    }

    public record Many(PointFreeRule rule) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            Optional<PointFree<Object>> optional = Optional.of(pointFree);
            Optional optional2;
            while (!(optional2 = optional.flatMap(this.rule::rewrite)).isEmpty()) {
                optional = optional2;
            }
            return optional;
        }
    }

    public record Everywhere(PointFreeRule topDown, PointFreeRule bottomUp) implements PointFreeRule
    {
        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            PointFree<A> pointFree2 = this.topDown.rewriteOrNop(pointFree);
            PointFree<A> pointFree3 = DataFixUtils.orElse(pointFree2.all(this), pointFree2);
            PointFree<A> pointFree4 = this.bottomUp.rewriteOrNop(pointFree3);
            return Optional.of(pointFree4);
        }
    }

    public static enum CataFuseDifferent implements CompRewrite
    {
        INSTANCE;


        @Override
        public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> pointFree, PointFree<? extends Function<?, ?>> pointFree2) {
            if (pointFree instanceof Fold) {
                Fold fold = (Fold)pointFree;
                if (pointFree2 instanceof Fold) {
                    Fold fold2 = (Fold)pointFree2;
                    RecursiveTypeFamily recursiveTypeFamily = fold.aType.family();
                    if (fold.index == fold2.index && Objects.equals(recursiveTypeFamily, fold2.aType.family())) {
                        RewriteResult<?, ?> rewriteResult;
                        RewriteResult<?, ?> rewriteResult2;
                        int n;
                        RecursiveTypeFamily recursiveTypeFamily2 = fold.bType.family();
                        ArrayList arrayList = Lists.newArrayList();
                        BitSet bitSet = new BitSet(recursiveTypeFamily.size());
                        BitSet bitSet2 = new BitSet(recursiveTypeFamily.size());
                        for (n = 0; n < recursiveTypeFamily.size(); ++n) {
                            rewriteResult2 = fold.algebra.apply(n);
                            rewriteResult = fold2.algebra.apply(n);
                            boolean bl = rewriteResult2.view().isNop();
                            boolean bl2 = rewriteResult.view().isNop();
                            if (!bl && !bl2) {
                                return Optional.empty();
                            }
                            bitSet.set(n, !bl);
                            bitSet2.set(n, !bl2);
                        }
                        for (n = 0; n < recursiveTypeFamily.size(); ++n) {
                            rewriteResult2 = fold.algebra.apply(n);
                            rewriteResult = fold2.algebra.apply(n);
                            if (rewriteResult2.recData().intersects(bitSet2) || rewriteResult.recData().intersects(bitSet)) {
                                return Optional.empty();
                            }
                            if (rewriteResult2.view().isNop()) {
                                arrayList.add(rewriteResult);
                                continue;
                            }
                            arrayList.add(rewriteResult2);
                        }
                        ListAlgebra listAlgebra = new ListAlgebra("FusedDifferent", arrayList);
                        return Optional.of(recursiveTypeFamily.fold(listAlgebra, recursiveTypeFamily2).apply(fold.index).view().function());
                    }
                }
            }
            return Optional.empty();
        }
    }

    public static enum CataFuseSame implements CompRewrite
    {
        INSTANCE;


        @Override
        public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> pointFree, PointFree<? extends Function<?, ?>> pointFree2) {
            if (pointFree instanceof Fold) {
                Fold fold = (Fold)pointFree;
                if (pointFree2 instanceof Fold) {
                    Fold fold2 = (Fold)pointFree2;
                    RecursiveTypeFamily recursiveTypeFamily = fold.aType.family();
                    if (fold.index == fold2.index && Objects.equals(recursiveTypeFamily, fold2.aType.family())) {
                        RecursiveTypeFamily recursiveTypeFamily2 = fold.bType.family();
                        ArrayList arrayList = Lists.newArrayList();
                        boolean bl = false;
                        for (int i = 0; i < recursiveTypeFamily.size(); ++i) {
                            RewriteResult<?, ?> rewriteResult = fold.algebra.apply(i);
                            RewriteResult<?, ?> rewriteResult2 = fold2.algebra.apply(i);
                            boolean bl2 = rewriteResult.view().isNop();
                            boolean bl3 = rewriteResult2.view().isNop();
                            if (bl2 && bl3) {
                                arrayList.add(rewriteResult);
                                continue;
                            }
                            if (!(bl || bl2 || bl3)) {
                                arrayList.add(this.getCompose(rewriteResult, rewriteResult2));
                                bl = true;
                                continue;
                            }
                            return Optional.empty();
                        }
                        ListAlgebra listAlgebra = new ListAlgebra("FusedSame", arrayList);
                        return Optional.of(recursiveTypeFamily.fold(listAlgebra, recursiveTypeFamily2).apply(fold.index).view().function());
                    }
                }
            }
            return Optional.empty();
        }

        private <B> RewriteResult<?, ?> getCompose(RewriteResult<B, ?> rewriteResult, RewriteResult<?, ?> rewriteResult2) {
            return rewriteResult.compose(rewriteResult2);
        }
    }

    public static enum LensComp implements CompRewrite
    {
        INSTANCE;


        @Override
        public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> pointFree, PointFree<? extends Function<?, ?>> pointFree2) {
            if (pointFree instanceof Apply) {
                Apply apply = (Apply)pointFree;
                if (pointFree2 instanceof Apply) {
                    Apply apply2 = (Apply)pointFree2;
                    PointFree pointFree3 = apply.func;
                    PointFree pointFree4 = apply2.func;
                    if (pointFree3 instanceof ProfunctorTransformer) {
                        ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree3;
                        if (pointFree4 instanceof ProfunctorTransformer) {
                            List<TypedOptic.Element<?, ?, ?, ?>> list;
                            ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)pointFree4;
                            List<TypedOptic.Element<?, ?, ?, ?>> list2 = profunctorTransformer.optic.elements();
                            int n = LensComp.findCommonPrefix(list2, list = profunctorTransformer2.optic.elements());
                            if (n == 0) {
                                return Optional.empty();
                            }
                            if (n == list2.size() && n == list.size()) {
                                return Optional.of(this.capApp(profunctorTransformer.optic, this.capComp(apply.arg, apply2.arg)));
                            }
                            Sets.SetView setView = Sets.union(profunctorTransformer.optic.bounds(), profunctorTransformer2.optic.bounds());
                            TypedOptic typedOptic = new TypedOptic((Set<TypeToken<? extends K1>>)setView, list2.subList(0, n));
                            PointFree pointFree5 = this.capApp(new TypedOptic((Set<TypeToken<? extends K1>>)setView, list2.subList(n, list2.size())), apply.arg);
                            PointFree pointFree6 = this.capApp(new TypedOptic((Set<TypeToken<? extends K1>>)setView, list.subList(n, list.size())), apply2.arg);
                            return Optional.of(this.capApp(typedOptic, this.capComp(pointFree5, pointFree6)));
                        }
                    }
                }
            }
            return Optional.empty();
        }

        private static int findCommonPrefix(List<? extends TypedOptic.Element<?, ?, ?, ?>> list, List<? extends TypedOptic.Element<?, ?, ?, ?>> list2) {
            int n = Math.min(list.size(), list2.size());
            for (int i = 0; i < n; ++i) {
                if (list.get(i).optic().equals(list2.get(i).optic())) continue;
                return i;
            }
            return n;
        }

        private <A, B, C> PointFree<Function<A, C>> capComp(PointFree<?> pointFree, PointFree<?> pointFree2) {
            return Functions.comp(pointFree, pointFree2);
        }

        private <R, A, B, S, T> PointFree<R> capApp(TypedOptic<S, T, A, B> typedOptic, PointFree<?> pointFree) {
            if (typedOptic.elements().isEmpty()) {
                return pointFree;
            }
            return Functions.app(new ProfunctorTransformer<S, T, A, B>(typedOptic), pointFree);
        }
    }

    public static enum SortInj implements CompRewrite
    {
        INSTANCE;


        @Override
        public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> pointFree, PointFree<? extends Function<?, ?>> pointFree2) {
            if (pointFree instanceof Apply) {
                Apply apply = (Apply)pointFree;
                if (pointFree2 instanceof Apply) {
                    Apply apply2 = (Apply)pointFree2;
                    PointFree pointFree3 = apply.func;
                    PointFree pointFree4 = apply2.func;
                    if (pointFree3 instanceof ProfunctorTransformer) {
                        ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree3;
                        if (pointFree4 instanceof ProfunctorTransformer) {
                            ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)pointFree4;
                            if (!Optics.isInj2(profunctorTransformer.optic.outermost())) {
                                return Optional.empty();
                            }
                            if (!Optics.isInj1(profunctorTransformer2.optic.outermost())) {
                                return Optional.empty();
                            }
                            return Optional.of((PointFree)this.cap(apply, apply2));
                        }
                    }
                }
            }
            return Optional.empty();
        }

        private <R, A, A2, B, B2> R cap(Apply<?, ?> apply, Apply<?, ?> apply2) {
            ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)apply.func;
            ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)apply2.func;
            PointFree pointFree = apply.arg;
            PointFree pointFree2 = apply2.arg;
            Func func = (Func)apply.type;
            Func func2 = (Func)apply2.type;
            Sum.SumType sumType = (Sum.SumType)func2.first();
            Sum.SumType sumType2 = (Sum.SumType)func.second();
            return (R)new Comp(new Apply(profunctorTransformer2.castOuterUnchecked(DSL.or(sumType2.first(), sumType.second()), sumType2), pointFree2), new Apply(profunctorTransformer.castOuterUnchecked(sumType, DSL.or(sumType2.first(), sumType.second())), pointFree));
        }
    }

    public static enum SortProj implements CompRewrite
    {
        INSTANCE;


        @Override
        public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> pointFree, PointFree<? extends Function<?, ?>> pointFree2) {
            if (pointFree instanceof Apply) {
                Apply apply = (Apply)pointFree;
                if (pointFree2 instanceof Apply) {
                    Apply apply2 = (Apply)pointFree2;
                    PointFree pointFree3 = apply.func;
                    PointFree pointFree4 = apply2.func;
                    if (pointFree3 instanceof ProfunctorTransformer) {
                        ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree3;
                        if (pointFree4 instanceof ProfunctorTransformer) {
                            ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)pointFree4;
                            if (!Optics.isProj2(profunctorTransformer.optic.outermost())) {
                                return Optional.empty();
                            }
                            if (!Optics.isProj1(profunctorTransformer2.optic.outermost())) {
                                return Optional.empty();
                            }
                            return Optional.of((PointFree)this.cap(apply, apply2));
                        }
                    }
                }
            }
            return Optional.empty();
        }

        private <R, A, A2, B, B2> R cap(Apply<?, ?> apply, Apply<?, ?> apply2) {
            ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)apply.func;
            ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)apply2.func;
            PointFree pointFree = apply.arg;
            PointFree pointFree2 = apply2.arg;
            Func func = (Func)apply.type;
            Func func2 = (Func)apply2.type;
            Product.ProductType productType = (Product.ProductType)func2.first();
            Product.ProductType productType2 = (Product.ProductType)func.second();
            return (R)new Comp(new Apply(profunctorTransformer2.castOuterUnchecked(DSL.and(productType2.first(), productType.second()), productType2), pointFree2), new Apply(profunctorTransformer.castOuterUnchecked(productType, DSL.and(productType2.first(), productType.second())), pointFree));
        }
    }

    public static interface CompRewrite
    extends PointFreeRule {
        public static CompRewrite together(CompRewrite ... compRewriteArray) {
            return (pointFree, pointFree2) -> {
                for (CompRewrite compRewrite : compRewriteArray) {
                    Optional<PointFree<Function<?, ?>>> optional = compRewrite.doRewrite(pointFree, pointFree2);
                    if (!optional.isPresent()) continue;
                    return optional;
                }
                return Optional.empty();
            };
        }

        @Override
        default public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            if (pointFree instanceof Comp) {
                Comp comp = (Comp)pointFree;
                return this.rewrite(comp.functions).map(pointFreeArray -> {
                    if (((PointFree[])pointFreeArray).length == 1) {
                        return pointFreeArray[0];
                    }
                    return new Comp((PointFree<? extends Function<?, ?>>)pointFreeArray);
                });
            }
            return Optional.empty();
        }

        private Optional<PointFree<? extends Function<?, ?>>[]> rewrite(PointFree<? extends Function<?, ?>>[] pointFreeArray) {
            ArrayDeque<PointFree> arrayDeque = new ArrayDeque<PointFree>(pointFreeArray.length);
            boolean bl = false;
            ArrayDeque arrayDeque2 = new ArrayDeque(pointFreeArray.length);
            Collections.addAll(arrayDeque2, pointFreeArray);
            while (!arrayDeque2.isEmpty()) {
                Optional optional;
                PointFree pointFree = (PointFree)arrayDeque2.removeFirst();
                PointFree pointFree2 = (PointFree)arrayDeque.peekLast();
                Optional<Object> optional2 = optional = pointFree2 != null ? this.doRewrite(pointFree2, pointFree) : Optional.empty();
                if (optional.isPresent()) {
                    arrayDeque.removeLast();
                    CompRewrite.addFirst(arrayDeque2, (PointFree)optional.get());
                    bl = true;
                    continue;
                }
                arrayDeque.add(pointFree);
            }
            return bl ? Optional.of((PointFree[])arrayDeque.toArray(PointFree[]::new)) : Optional.empty();
        }

        private static void addFirst(Deque<PointFree<? extends Function<?, ?>>> deque, PointFree<? extends Function<?, ?>> pointFree) {
            if (pointFree instanceof Comp) {
                Comp comp = (Comp)pointFree;
                for (int i = comp.functions.length - 1; i >= 0; --i) {
                    deque.addFirst(comp.functions[i]);
                }
            } else {
                deque.addFirst(pointFree);
            }
        }

        public Optional<? extends PointFree<? extends Function<?, ?>>> doRewrite(PointFree<? extends Function<?, ?>> var1, PointFree<? extends Function<?, ?>> var2);
    }

    public static enum AppNest implements PointFreeRule
    {
        INSTANCE;


        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            if (pointFree instanceof Apply) {
                Apply apply = (Apply)pointFree;
                PointFree pointFree2 = apply.arg;
                if (pointFree2 instanceof Apply) {
                    Apply apply2 = (Apply)pointFree2;
                    return Optional.of(Functions.app(this.compose(apply.func, apply2.func), apply2.arg));
                }
            }
            return Optional.empty();
        }

        private <A, B, C> PointFree<Function<A, C>> compose(PointFree<? extends Function<?, ?>> pointFree, PointFree<? extends Function<?, ?>> pointFree2) {
            if (pointFree instanceof ProfunctorTransformer) {
                ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)pointFree;
                if (pointFree2 instanceof ProfunctorTransformer) {
                    ProfunctorTransformer profunctorTransformer2 = (ProfunctorTransformer)pointFree2;
                    return (PointFree)this.cap(profunctorTransformer, profunctorTransformer2);
                }
            }
            return Functions.comp(pointFree, pointFree2);
        }

        private <R, X, Y, S, T, A, B> R cap(ProfunctorTransformer<X, Y, ?, ?> profunctorTransformer, ProfunctorTransformer<S, T, A, B> profunctorTransformer2) {
            ProfunctorTransformer<X, Y, ?, ?> profunctorTransformer3 = profunctorTransformer;
            return (R)Functions.profunctorTransformer(profunctorTransformer3.optic.compose(profunctorTransformer2.optic));
        }
    }

    public static enum LensAppId implements PointFreeRule
    {
        INSTANCE;


        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            if (pointFree instanceof Apply) {
                Apply apply = (Apply)pointFree;
                PointFree pointFree2 = apply.func;
                if (pointFree2 instanceof ProfunctorTransformer && Functions.isId(apply.arg)) {
                    return Optional.of(Functions.id(((Func)apply.type()).first()));
                }
            }
            return Optional.empty();
        }
    }

    public static enum BangEta implements PointFreeRule
    {
        INSTANCE;


        @Override
        public <A> Optional<? extends PointFree<A>> rewrite(PointFree<A> pointFree) {
            Func func;
            if (pointFree instanceof Bang) {
                return Optional.empty();
            }
            Type<A> type = pointFree.type();
            if (type instanceof Func && (func = (Func)type).second() instanceof EmptyPart) {
                return Optional.of(Functions.bang(func.first()));
            }
            return Optional.empty();
        }
    }
}

