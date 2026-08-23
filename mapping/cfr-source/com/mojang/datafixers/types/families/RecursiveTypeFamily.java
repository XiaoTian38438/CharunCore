/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Interner
 *  com.google.common.collect.Interners
 *  com.google.common.collect.Lists
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMaps
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types.families;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.View;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.Algebra;
import com.mojang.datafixers.types.families.ListAlgebra;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public final class RecursiveTypeFamily
implements TypeFamily {
    private static final Interner<TypeTemplate> TEMPLATE_INTERNER = Interners.newWeakInterner();
    private final String name;
    private final TypeTemplate template;
    private final int size;
    private final Int2ObjectMap<RecursivePoint.RecursivePointType<?>> types = Int2ObjectMaps.synchronize((Int2ObjectMap)new Int2ObjectOpenHashMap());
    private final int hashCode;

    public RecursiveTypeFamily(String string, TypeTemplate typeTemplate) {
        this.name = string;
        this.template = (TypeTemplate)TEMPLATE_INTERNER.intern((Object)typeTemplate);
        this.size = typeTemplate.size();
        this.hashCode = Objects.hashCode(typeTemplate);
    }

    public <A> RecursivePoint.RecursivePointType<A> buildMuType(Type<A> type, @Nullable RecursiveTypeFamily recursiveTypeFamily) {
        Object object;
        if (recursiveTypeFamily == null) {
            object = type.template();
            recursiveTypeFamily = Objects.equals(this.template, object) ? this : new RecursiveTypeFamily("ruled " + this.name, (TypeTemplate)object);
        }
        object = null;
        for (int i = 0; i < recursiveTypeFamily.size; ++i) {
            Type type2 = recursiveTypeFamily.apply(i);
            Type type3 = ((RecursivePoint.RecursivePointType)type2).unfold();
            if (!type.equals(type3, true, false)) continue;
            object = type2;
            break;
        }
        if (object == null) {
            throw new IllegalStateException("Couldn't determine the new type properly");
        }
        return object;
    }

    public String name() {
        return this.name;
    }

    public TypeTemplate template() {
        return this.template;
    }

    public int size() {
        return this.size;
    }

    public IntFunction<RewriteResult<?, ?>> fold(Algebra algebra, RecursiveTypeFamily recursiveTypeFamily) {
        return n -> {
            RewriteResult<?, ?> rewriteResult = algebra.apply(n);
            return RewriteResult.create(View.create(RecursiveTypeFamily.foldUnchecked(this, recursiveTypeFamily, algebra, n)), rewriteResult.recData());
        };
    }

    private static <A, B> PointFree<Function<A, B>> foldUnchecked(RecursiveTypeFamily recursiveTypeFamily, RecursiveTypeFamily recursiveTypeFamily2, Algebra algebra, int n) {
        Type type = recursiveTypeFamily.apply(n);
        Type type2 = recursiveTypeFamily2.apply(n);
        return Functions.fold(type, type2, algebra, n);
    }

    public RecursivePoint.RecursivePointType<?> apply(int n2) {
        if (n2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (RecursivePoint.RecursivePointType)this.types.computeIfAbsent(n2, n -> new RecursivePoint.RecursivePointType(this, n, () -> this.template.apply(this).apply(n)));
    }

    public <A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> findType(int n, Type<A> type, Type<B> type2, Type.TypeMatcher<A, B> typeMatcher, boolean bl) {
        return ((RecursivePoint.RecursivePointType)this.apply(n)).unfold().findType(type, type2, typeMatcher, false).flatMap(typedOptic -> {
            TypeTemplate typeTemplate = typedOptic.tType().template();
            ArrayList arrayList = Lists.newArrayList();
            RecursiveTypeFamily recursiveTypeFamily = new RecursiveTypeFamily(this.name, typeTemplate);
            Type type3 = this.apply(n);
            Type type4 = recursiveTypeFamily.apply(n);
            if (bl) {
                FamilyOptic familyOptic = n -> ((FamilyOptic)arrayList.get(0)).apply(n);
                arrayList.add(this.template.applyO(familyOptic, type, type2));
                TypedOptic typedOptic2 = ((FamilyOptic)arrayList.get(0)).apply(n);
                return Either.left(typedOptic2.castOuterUnchecked(type3, type4));
            }
            return this.mkSimpleOptic((RecursivePoint.RecursivePointType)type3, (RecursivePoint.RecursivePointType)type4, type, type2, typeMatcher);
        });
    }

    private <S, T, A, B> Either<TypedOptic<?, ?, A, B>, Type.FieldNotFoundException> mkSimpleOptic(RecursivePoint.RecursivePointType<S> recursivePointType, RecursivePoint.RecursivePointType<T> recursivePointType2, Type<A> type, Type<B> type2, Type.TypeMatcher<A, B> typeMatcher) {
        return recursivePointType.unfold().findType(type, type2, typeMatcher, false).mapLeft(typedOptic -> typedOptic.castOuterUnchecked(recursivePointType, recursivePointType2));
    }

    public Optional<RewriteResult<?, ?>> everywhere(int n, TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule) {
        Object object;
        Type type = ((RecursivePoint.RecursivePointType)this.apply(n)).unfold();
        RewriteResult rewriteResult = DataFixUtils.orElse(type.everywhere(typeRewriteRule, pointFreeRule, false, false), RewriteResult.nop(type));
        RecursivePoint.RecursivePointType recursivePointType = this.buildMuType(rewriteResult.view().newType(), null);
        RecursiveTypeFamily recursiveTypeFamily = recursivePointType.family();
        ArrayList arrayList = Lists.newArrayList();
        boolean bl = false;
        for (int i = 0; i < this.size; ++i) {
            object = this.apply(i);
            Type type2 = ((RecursivePoint.RecursivePointType)object).unfold();
            boolean bl2 = true;
            RewriteResult rewriteResult2 = DataFixUtils.orElse(type2.everywhere(typeRewriteRule, pointFreeRule, false, true), RewriteResult.nop(type2));
            if (!rewriteResult2.view().isNop()) {
                bl2 = false;
            }
            RecursivePoint.RecursivePointType recursivePointType2 = this.buildMuType(rewriteResult2.view().newType(), recursiveTypeFamily);
            boolean bl3 = this.cap2(arrayList, (RecursivePoint.RecursivePointType)object, typeRewriteRule, pointFreeRule, bl2, rewriteResult2, recursivePointType2);
            bl = bl || !bl3;
        }
        if (!bl) {
            return Optional.empty();
        }
        ListAlgebra listAlgebra = new ListAlgebra("everywhere", arrayList);
        object = this.fold(listAlgebra, recursiveTypeFamily).apply(n);
        return Optional.of(RewriteResult.create(View.create(((RewriteResult)object).view().function()), ((RewriteResult)object).recData()));
    }

    private <A, B> boolean cap2(List<RewriteResult<?, ?>> list, RecursivePoint.RecursivePointType<A> recursivePointType, TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule, boolean bl, RewriteResult<?, ?> rewriteResult, RecursivePoint.RecursivePointType<B> recursivePointType2) {
        RewriteResult<?, B> rewriteResult2 = RewriteResult.create(recursivePointType2.in(), new BitSet()).compose(rewriteResult);
        Optional<RewriteResult<B, ?>> optional = typeRewriteRule.rewrite(rewriteResult2.view().newType());
        if (optional.isPresent() && !optional.get().view().isNop()) {
            bl = false;
            rewriteResult = optional.get().compose(rewriteResult2);
        }
        rewriteResult = RewriteResult.create(rewriteResult.view().rewriteOrNop(pointFreeRule), rewriteResult.recData());
        list.add(rewriteResult);
        return bl;
    }

    public String toString() {
        return "Mu[" + this.name + ", " + this.size + ", " + String.valueOf(this.template) + "]";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof RecursiveTypeFamily)) {
            return false;
        }
        RecursiveTypeFamily recursiveTypeFamily = (RecursiveTypeFamily)object;
        return this.template == recursiveTypeFamily.template;
    }

    public int hashCode() {
        return this.hashCode;
    }
}

