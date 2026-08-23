/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types.templates;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record CompoundList(TypeTemplate key, TypeTemplate element) implements TypeTemplate
{
    @Override
    public int size() {
        return Math.max(this.key.size(), this.element.size());
    }

    @Override
    public TypeFamily apply(TypeFamily typeFamily) {
        return n -> DSL.compoundList(this.key.apply(typeFamily).apply(n), this.element.apply(typeFamily).apply(n));
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.cap(this.element.applyO(familyOptic, type, type2).apply(n)));
    }

    private <S, T, A, B> TypedOptic<?, ?, A, B> cap(TypedOptic<S, T, A, B> typedOptic) {
        Type<Pair<String, S>> type = DSL.and(DSL.string(), typedOptic.sType());
        Type<Pair<String, T>> type2 = DSL.and(DSL.string(), typedOptic.tType());
        return new TypedOptic(TraversalP.Mu.TYPE_TOKEN, DSL.compoundList(typedOptic.sType()), DSL.compoundList(typedOptic.tType()), type, type2, Optics.listTraversal()).compose(new TypedOptic<Pair<String, S>, Pair<String, T>, S, T>(Cartesian.Mu.TYPE_TOKEN, type, type2, typedOptic.sType(), typedOptic.tType(), Optics.proj2())).compose(typedOptic);
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        return this.element.findFieldOrType(n, string, type, type2).mapLeft(typeTemplate -> new CompoundList(this.key, (TypeTemplate)typeTemplate));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult<?, ?> rewriteResult = this.key.hmap(typeFamily, intFunction).apply(n);
            RewriteResult<?, ?> rewriteResult2 = this.element.hmap(typeFamily, intFunction).apply(n);
            return this.cap(this.apply(typeFamily).apply(n), rewriteResult, rewriteResult2);
        };
    }

    private <L, R> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<L, ?> rewriteResult, RewriteResult<R, ?> rewriteResult2) {
        return ((CompoundListType)type).mergeViews(rewriteResult, rewriteResult2);
    }

    @Override
    public String toString() {
        return "CompoundList[" + String.valueOf(this.element) + "]";
    }

    public static final class CompoundListType<K, V>
    extends Type<List<Pair<K, V>>> {
        protected final Type<K> key;
        protected final Type<V> element;

        public CompoundListType(Type<K> type, Type<V> type2) {
            this.key = type;
            this.element = type2;
        }

        @Override
        public RewriteResult<List<Pair<K, V>>, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            return this.mergeViews(this.key.rewriteOrNop(typeRewriteRule), this.element.rewriteOrNop(typeRewriteRule));
        }

        public <K2, V2> RewriteResult<List<Pair<K, V>>, ?> mergeViews(RewriteResult<K, K2> rewriteResult, RewriteResult<V, V2> rewriteResult2) {
            RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> rewriteResult3 = CompoundListType.fixKeys(this, this.key, this.element, rewriteResult);
            RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> rewriteResult4 = CompoundListType.fixValues(rewriteResult3.view().newType(), rewriteResult.view().newType(), this.element, rewriteResult2);
            return rewriteResult4.compose(rewriteResult3);
        }

        @Override
        public Optional<RewriteResult<List<Pair<K, V>>, ?>> one(TypeRewriteRule typeRewriteRule) {
            return DataFixUtils.or(typeRewriteRule.rewrite(this.key).map(rewriteResult -> CompoundListType.fixKeys(this, this.key, this.element, rewriteResult)), () -> typeRewriteRule.rewrite(this.element).map(rewriteResult -> CompoundListType.fixValues(this, this.key, this.element, rewriteResult)));
        }

        private static <K, V, K2> RewriteResult<List<Pair<K, V>>, List<Pair<K2, V>>> fixKeys(Type<List<Pair<K, V>>> type, Type<K> type2, Type<V> type3, RewriteResult<K, K2> rewriteResult) {
            return CompoundListType.opticView(type, rewriteResult, TypedOptic.compoundListKeys(type2, rewriteResult.view().newType(), type3));
        }

        private static <K, V, V2> RewriteResult<List<Pair<K, V>>, List<Pair<K, V2>>> fixValues(Type<List<Pair<K, V>>> type, Type<K> type2, Type<V> type3, RewriteResult<V, V2> rewriteResult) {
            return CompoundListType.opticView(type, rewriteResult, TypedOptic.compoundListElements(type2, type3, rewriteResult.view().newType()));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return DSL.compoundList(this.key.updateMu(recursiveTypeFamily), this.element.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return new CompoundList(this.key.template(), this.element.template());
        }

        @Override
        public Optional<List<Pair<K, V>>> point(DynamicOps<?> dynamicOps) {
            return Optional.of(ImmutableList.of());
        }

        @Override
        public <FT, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            Either<TypedOptic<K, ?, FT, FR>, Type.FieldNotFoundException> either = this.key.findType(type, type2, typeMatcher, bl);
            return either.map(this::capLeft, fieldNotFoundException -> {
                Either either = this.element.findType(type, type2, typeMatcher, bl);
                return either.mapLeft(this::capRight);
            });
        }

        private <FT, K2, FR> Either<TypedOptic<List<Pair<K, V>>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<K, K2, FT, FR> typedOptic) {
            return Either.left(TypedOptic.compoundListKeys(typedOptic.sType(), typedOptic.tType(), this.element).compose(typedOptic));
        }

        private <FT, V2, FR> TypedOptic<List<Pair<K, V>>, ?, FT, FR> capRight(TypedOptic<V, V2, FT, FR> typedOptic) {
            return TypedOptic.compoundListElements(this.key, typedOptic.sType(), typedOptic.tType()).compose(typedOptic);
        }

        @Override
        protected Codec<List<Pair<K, V>>> buildCodec() {
            return Codec.compoundList(this.key.codec(), this.element.codec());
        }

        public String toString() {
            return "CompoundList[" + String.valueOf(this.key) + " -> " + String.valueOf(this.element) + "]";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (!(object instanceof CompoundListType)) {
                return false;
            }
            CompoundListType compoundListType = (CompoundListType)object;
            return this.key.equals(compoundListType.key, bl, bl2) && this.element.equals(compoundListType.element, bl, bl2);
        }

        public int hashCode() {
            int n = this.key.hashCode();
            n = 31 * n + this.element.hashCode();
            return n;
        }

        public Type<K> getKey() {
            return this.key;
        }

        public Type<V> getElement() {
            return this.element;
        }
    }
}

