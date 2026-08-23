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
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.TraversalP;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record List(TypeTemplate element) implements TypeTemplate
{
    @Override
    public int size() {
        return this.element.size();
    }

    @Override
    public TypeFamily apply(final TypeFamily typeFamily) {
        return new TypeFamily(){

            @Override
            public Type<?> apply(int n) {
                return DSL.list(List.this.element.apply(typeFamily).apply(n));
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.cap(this.element.applyO(familyOptic, type, type2).apply(n)));
    }

    private <S, T, A, B> TypedOptic<?, ?, A, B> cap(TypedOptic<S, T, A, B> typedOptic) {
        return new TypedOptic<S, T, S, T>(TraversalP.Mu.TYPE_TOKEN, DSL.list(typedOptic.sType()), DSL.list(typedOptic.tType()), typedOptic.sType(), typedOptic.tType(), Optics.listTraversal()).compose(typedOptic);
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        return this.element.findFieldOrType(n, string, type, type2).mapLeft(List::new);
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult<?, ?> rewriteResult = this.element.hmap(typeFamily, intFunction).apply(n);
            return this.cap(this.apply(typeFamily).apply(n), rewriteResult);
        };
    }

    private <E> RewriteResult<?, ?> cap(Type<?> type, RewriteResult<E, ?> rewriteResult) {
        return ((ListType)type).fix(rewriteResult);
    }

    @Override
    public String toString() {
        return "List[" + String.valueOf(this.element) + "]";
    }

    public static final class ListType<A>
    extends Type<java.util.List<A>> {
        protected final Type<A> element;

        public ListType(Type<A> type) {
            this.element = type;
        }

        @Override
        public RewriteResult<java.util.List<A>, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            RewriteResult<A, ?> rewriteResult = this.element.rewriteOrNop(typeRewriteRule);
            return this.fix(rewriteResult);
        }

        @Override
        public Optional<RewriteResult<java.util.List<A>, ?>> one(TypeRewriteRule typeRewriteRule) {
            return typeRewriteRule.rewrite(this.element).map(this::fix);
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return DSL.list(this.element.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.list(this.element.template());
        }

        @Override
        public Optional<java.util.List<A>> point(DynamicOps<?> dynamicOps) {
            return Optional.of(ImmutableList.of());
        }

        @Override
        public <FT, FR> Either<TypedOptic<java.util.List<A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> either = this.element.findType(type, type2, typeMatcher, bl);
            return either.mapLeft(this::capLeft);
        }

        private <FT, FR, B> TypedOptic<java.util.List<A>, ?, FT, FR> capLeft(TypedOptic<A, B, FT, FR> typedOptic) {
            return TypedOptic.list(typedOptic.sType(), typedOptic.tType()).compose(typedOptic);
        }

        public <B> RewriteResult<java.util.List<A>, ?> fix(RewriteResult<A, B> rewriteResult) {
            return ListType.opticView(this, rewriteResult, TypedOptic.list(this.element, rewriteResult.view().newType()));
        }

        @Override
        public Codec<java.util.List<A>> buildCodec() {
            return Codec.list(this.element.codec());
        }

        public String toString() {
            return "List[" + String.valueOf(this.element) + "]";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            return object instanceof ListType && this.element.equals(((ListType)object).element, bl, bl2);
        }

        public int hashCode() {
            return this.element.hashCode();
        }

        public Type<A> getElement() {
            return this.element;
        }
    }
}

