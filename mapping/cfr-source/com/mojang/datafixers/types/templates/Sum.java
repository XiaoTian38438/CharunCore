/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types.templates;

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
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.Traversal;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record Sum(TypeTemplate f, TypeTemplate g) implements TypeTemplate
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
                return DSL.or(Sum.this.f.apply(typeFamily).apply(n), Sum.this.g.apply(typeFamily).apply(n));
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.cap(this.f.applyO(familyOptic, type, type2), this.g.applyO(familyOptic, type, type2), n));
    }

    private <A, B, LS, RS, LT, RT> TypedOptic<?, ?, A, B> cap(FamilyOptic<A, B> familyOptic, FamilyOptic<A, B> familyOptic2, int n) {
        return SumType.mergeOptics(familyOptic.apply(n), familyOptic2.apply(n));
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        Either<TypeTemplate, Type.FieldNotFoundException> either = this.f.findFieldOrType(n, string, type, type2);
        return either.map(typeTemplate -> Either.left(new Sum((TypeTemplate)typeTemplate, this.g)), fieldNotFoundException -> this.g.findFieldOrType(n, string, type, type2).mapLeft(typeTemplate -> new Sum(this.f, (TypeTemplate)typeTemplate)));
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
        return ((SumType)type).mergeViews(rewriteResult, rewriteResult2);
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(this.f) + " | " + String.valueOf(this.g) + ")";
    }

    public static final class SumType<F, G>
    extends Type<Either<F, G>> {
        protected final Type<F> first;
        protected final Type<G> second;
        private int hashCode;

        public SumType(Type<F> type, Type<G> type2) {
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
        public RewriteResult<Either<F, G>, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            return this.mergeViews(this.first.rewriteOrNop(typeRewriteRule), this.second.rewriteOrNop(typeRewriteRule));
        }

        public <F2, G2> RewriteResult<Either<F, G>, ?> mergeViews(RewriteResult<F, F2> rewriteResult, RewriteResult<G, G2> rewriteResult2) {
            RewriteResult<Either<F, G>, Either<F2, G>> rewriteResult3 = SumType.fixLeft(this, this.first, this.second, rewriteResult);
            RewriteResult<Either<F, G>, Either<F, G2>> rewriteResult4 = SumType.fixRight(rewriteResult3.view().newType(), rewriteResult.view().newType(), this.second, rewriteResult2);
            return rewriteResult4.compose(rewriteResult3);
        }

        @Override
        public Optional<RewriteResult<Either<F, G>, ?>> one(TypeRewriteRule typeRewriteRule) {
            return DataFixUtils.or(typeRewriteRule.rewrite(this.first).map(rewriteResult -> SumType.fixLeft(this, this.first, this.second, rewriteResult)), () -> typeRewriteRule.rewrite(this.second).map(rewriteResult -> SumType.fixRight(this, this.first, this.second, rewriteResult)));
        }

        private static <F, G, F2> RewriteResult<Either<F, G>, Either<F2, G>> fixLeft(Type<Either<F, G>> type, Type<F> type2, Type<G> type3, RewriteResult<F, F2> rewriteResult) {
            return SumType.opticView(type, rewriteResult, TypedOptic.inj1(type2, type3, rewriteResult.view().newType()));
        }

        private static <F, G, G2> RewriteResult<Either<F, G>, Either<F, G2>> fixRight(Type<Either<F, G>> type, Type<F> type2, Type<G> type3, RewriteResult<G, G2> rewriteResult) {
            return SumType.opticView(type, rewriteResult, TypedOptic.inj2(type2, type3, rewriteResult.view().newType()));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return DSL.or(this.first.updateMu(recursiveTypeFamily), this.second.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.or(this.first.template(), this.second.template());
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
        protected Codec<Either<F, G>> buildCodec() {
            return Codec.either(this.first.codec(), this.second.codec());
        }

        public String toString() {
            return "(" + String.valueOf(this.first) + " | " + String.valueOf(this.second) + ")";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (!(object instanceof SumType)) {
                return false;
            }
            SumType sumType = (SumType)object;
            return this.first.equals(sumType.first, bl, bl2) && this.second.equals(sumType.second, bl, bl2);
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
        public Optional<Either<F, G>> point(DynamicOps<?> dynamicOps) {
            return DataFixUtils.or(this.second.point(dynamicOps).map(Either::right), () -> this.first.point(dynamicOps).map(Either::left));
        }

        private static <A, B, LS, RS, LT, RT> TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B> mergeOptics(final TypedOptic<LS, LT, A, B> typedOptic, final TypedOptic<RS, RT, A, B> typedOptic2) {
            final TypeToken<TraversalP.Mu> typeToken = TraversalP.Mu.TYPE_TOKEN;
            return new TypedOptic<Either<LS, RS>, Either<LT, RT>, A, B>(typeToken, DSL.or(typedOptic.sType(), typedOptic2.sType()), DSL.or(typedOptic.tType(), typedOptic2.tType()), typedOptic.aType(), typedOptic.bType(), new Traversal<Either<LS, RS>, Either<LT, RT>, A, B>(){

                @Override
                public <F extends K1> FunctionType<Either<LS, RS>, App<F, Either<LT, RT>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> functionType) {
                    return either -> either.map(object -> {
                        Traversal traversal = Optics.toTraversal(typedOptic.upCast(typeToken).orElseThrow(IllegalArgumentException::new));
                        return applicative.ap(Either::left, traversal.wander(applicative, functionType).apply(object));
                    }, object -> {
                        Traversal traversal = Optics.toTraversal(typedOptic2.upCast(typeToken).orElseThrow(IllegalArgumentException::new));
                        return applicative.ap(Either::right, traversal.wander(applicative, functionType).apply(object));
                    });
                }
            });
        }

        @Override
        public <FT, FR> Either<TypedOptic<Either<F, G>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            Either<TypedOptic<F, ?, FT, FR>, Type.FieldNotFoundException> either = this.first.findType(type, type2, typeMatcher, bl);
            Either<TypedOptic<G, ?, FT, FR>, Type.FieldNotFoundException> either2 = this.second.findType(type, type2, typeMatcher, bl);
            if (either.left().isPresent() && either2.left().isPresent()) {
                return Either.left(SumType.mergeOptics(either.left().get(), either2.left().get()));
            }
            if (either.left().isPresent()) {
                return either.mapLeft(this::capLeft);
            }
            return either2.mapLeft(this::capRight);
        }

        private <FT, FR, F2> TypedOptic<Either<F, G>, ?, FT, FR> capLeft(TypedOptic<F, F2, FT, FR> typedOptic) {
            return TypedOptic.inj1(typedOptic.sType(), this.second, typedOptic.tType()).compose(typedOptic);
        }

        private <FT, FR, G2> TypedOptic<Either<F, G>, ?, FT, FR> capRight(TypedOptic<G, G2, FT, FR> typedOptic) {
            return TypedOptic.inj2(this.first, typedOptic.sType(), typedOptic.tType()).compose(typedOptic);
        }
    }
}

