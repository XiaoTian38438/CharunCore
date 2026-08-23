/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.reflect.TypeToken
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types.templates;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.Traversal;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record Product(TypeTemplate f, TypeTemplate g) implements TypeTemplate
{
    @Override
    public int size() {
        return Math.max(this.f.size(), this.g.size());
    }

    @Override
    public TypeFamily apply(final TypeFamily typeFamily) {
        return new TypeFamily(){

            @Override
            public Type<?> apply(int n) {
                return DSL.and(Product.this.f.apply(typeFamily).apply(n), Product.this.g.apply(typeFamily).apply(n));
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.cap(this.f.applyO(familyOptic, type, type2), this.g.applyO(familyOptic, type, type2), n));
    }

    private <A, B, LS, RS, LT, RT> TypedOptic<?, ?, A, B> cap(FamilyOptic<A, B> familyOptic, FamilyOptic<A, B> familyOptic2, int n) {
        TypeToken<TraversalP.Mu> typeToken = TraversalP.Mu.TYPE_TOKEN;
        TypedOptic<?, ?, A, B> typedOptic = familyOptic.apply(n);
        TypedOptic<?, ?, A, B> typedOptic2 = familyOptic2.apply(n);
        Optic<TraversalP.Mu, ?, ?, A, B> optic = typedOptic.upCast(typeToken).orElseThrow(IllegalArgumentException::new);
        Optic<TraversalP.Mu, ?, ?, A, B> optic2 = typedOptic2.upCast(typeToken).orElseThrow(IllegalArgumentException::new);
        final Traversal<?, ?, A, B> traversal = Optics.toTraversal(optic);
        final Traversal<?, ?, A, B> traversal2 = Optics.toTraversal(optic2);
        return new TypedOptic((Set<TypeToken<? extends K1>>)ImmutableSet.of(typeToken), DSL.and(typedOptic.sType(), typedOptic2.sType()), DSL.and(typedOptic.tType(), typedOptic2.tType()), typedOptic.aType(), typedOptic.bType(), new Traversal<Pair<LS, RS>, Pair<LT, RT>, A, B>(){

            @Override
            public <F extends K1> FunctionType<Pair<LS, RS>, App<F, Pair<LT, RT>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> functionType) {
                return pair -> applicative.ap2(applicative.point(Pair::of), traversal.wander(applicative, functionType).apply(pair.getFirst()), traversal2.wander(applicative, functionType).apply(pair.getSecond()));
            }
        });
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.findFieldOrType(n, string, type, type2);
        return either.map(typeTemplate -> Either.left(new Product((TypeTemplate)typeTemplate, this.g)), fieldNotFoundException -> this.g.findFieldOrType(n, string, type, type2).mapLeft(typeTemplate -> new Product(this.f, (TypeTemplate)typeTemplate)));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult<?, ?> rewriteResult = this.f.hmap(typeFamily, intFunction).apply(n);
            RewriteResult<?, ?> rewriteResult2 = this.g.hmap(typeFamily, intFunction).apply(n);
            return this.cap(this.apply(typeFamily).apply(n), rewriteResult, rewriteResult2);
        };
    }

    private <L, R> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<L, ?> rewriteResult, RewriteResult<R, ?> rewriteResult2) {
        return ((ProductType)type).mergeViews(rewriteResult, rewriteResult2);
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(this.f) + ", " + String.valueOf(this.g) + ")";
    }

    public static final class ProductType<F, G>
    extends Type<Pair<F, G>> {
        protected final Type<F> first;
        protected final Type<G> second;
        private int hashCode;

        public ProductType(Type<F> type, Type<G> type2) {
            this.first = type;
            this.second = type2;
        }

        public Type<F> first() {
            return this.first;
        }

        public Type<G> second() {
            return this.second;
        }

        @Override
        public RewriteResult<Pair<F, G>, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            return this.mergeViews(this.first.rewriteOrNop(typeRewriteRule), this.second.rewriteOrNop(typeRewriteRule));
        }

        public <F2, G2> RewriteResult<Pair<F, G>, ?> mergeViews(RewriteResult<F, F2> rewriteResult, RewriteResult<G, G2> rewriteResult2) {
            RewriteResult<Pair<F, G>, Pair<F2, G>> rewriteResult3 = ProductType.fixLeft(this, this.first, this.second, rewriteResult);
            RewriteResult<Pair<F, G>, Pair<F, G2>> rewriteResult4 = ProductType.fixRight(rewriteResult3.view().newType(), rewriteResult.view().newType(), this.second, rewriteResult2);
            return rewriteResult4.compose(rewriteResult3);
        }

        @Override
        public Optional<RewriteResult<Pair<F, G>, ?>> one(TypeRewriteRule typeRewriteRule) {
            return DataFixUtils.or(typeRewriteRule.rewrite(this.first).map(rewriteResult -> ProductType.fixLeft(this, this.first, this.second, rewriteResult)), () -> typeRewriteRule.rewrite(this.second).map(rewriteResult -> ProductType.fixRight(this, this.first, this.second, rewriteResult)));
        }

        private static <F, G, F2> RewriteResult<Pair<F, G>, Pair<F2, G>> fixLeft(Type<Pair<F, G>> type, Type<F> type2, Type<G> type3, RewriteResult<F, F2> rewriteResult) {
            return ProductType.opticView(type, rewriteResult, TypedOptic.proj1(type2, type3, rewriteResult.view().newType()));
        }

        private static <F, G, G2> RewriteResult<Pair<F, G>, Pair<F, G2>> fixRight(Type<Pair<F, G>> type, Type<F> type2, Type<G> type3, RewriteResult<G, G2> rewriteResult) {
            return ProductType.opticView(type, rewriteResult, TypedOptic.proj2(type2, type3, rewriteResult.view().newType()));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return DSL.and(this.first.updateMu(recursiveTypeFamily), this.second.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.and(this.first.template(), this.second.template());
        }

        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String string, int n) {
            return DataFixUtils.or(this.first.findChoiceType(string, n), () -> this.second.findChoiceType(string, n));
        }

        @Override
        public Optional<Type<?>> findCheckedType(int n) {
            return DataFixUtils.or(this.first.findCheckedType(n), () -> this.second.findCheckedType(n));
        }

        @Override
        public Codec<Pair<F, G>> buildCodec() {
            return Codec.pair(this.first.codec(), this.second.codec());
        }

        public String toString() {
            return "(" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + ")";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (!(object instanceof ProductType)) {
                return false;
            }
            ProductType productType = (ProductType)object;
            return this.first.equals(productType.first, bl, bl2) && this.second.equals(productType.second, bl, bl2);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int n = this.first.hashCode();
                this.hashCode = n = 31 * n + this.second.hashCode();
            }
            return this.hashCode;
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            return DataFixUtils.or(this.first.findFieldTypeOpt(string), () -> this.second.findFieldTypeOpt(string));
        }

        @Override
        public Optional<Pair<F, G>> point(DynamicOps<?> dynamicOps) {
            return this.first.point(dynamicOps).flatMap(object -> this.second.point(dynamicOps).map(object2 -> Pair.of(object, object2)));
        }

        @Override
        public <FT, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            Either<TypedOptic<F, ?, FT, FR>, Type.FieldNotFoundException> either = this.first.findType(type, type2, typeMatcher, bl);
            return either.map(this::capLeft, fieldNotFoundException -> {
                Either either = this.second.findType(type, type2, typeMatcher, bl);
                return either.mapLeft(this::capRight);
            });
        }

        private <FT, F2, FR> Either<TypedOptic<Pair<F, G>, ?, FT, FR>, Type.FieldNotFoundException> capLeft(TypedOptic<F, F2, FT, FR> typedOptic) {
            return Either.left(TypedOptic.proj1(typedOptic.sType(), this.second, typedOptic.tType()).compose(typedOptic));
        }

        private <FT, G2, FR> TypedOptic<Pair<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> typedOptic) {
            return TypedOptic.proj2(this.first, typedOptic.sType(), typedOptic.tType()).compose(typedOptic);
        }
    }
}

