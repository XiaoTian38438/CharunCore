/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers.types.templates;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.Const;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record Tag(String name, TypeTemplate element) implements TypeTemplate
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
                return DSL.field(Tag.this.name, Tag.this.element.apply(typeFamily).apply(n));
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.element.applyO(familyOptic, type, type2).apply(n));
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        if (!Objects.equals(string, this.name)) {
            return Either.right(new Type.FieldNotFoundException("Names don't match"));
        }
        if (this.element instanceof Const) {
            Const const_ = (Const)this.element;
            if (Objects.equals(type, const_.type())) {
                return Either.left(new Tag(string, new Const(type2)));
            }
            return Either.right(new Type.FieldNotFoundException("don't match"));
        }
        if (Objects.equals(type, type2)) {
            return Either.left(this);
        }
        if (type instanceof RecursivePoint.RecursivePointType && this.element instanceof RecursivePoint && ((RecursivePoint)this.element).index() == ((RecursivePoint.RecursivePointType)type).index()) {
            if (type2 instanceof RecursivePoint.RecursivePointType) {
                if (((RecursivePoint.RecursivePointType)type2).index() == ((RecursivePoint)this.element).index()) {
                    return Either.left(this);
                }
            } else {
                return Either.left(DSL.constType(type2));
            }
        }
        return Either.right(new Type.FieldNotFoundException("Recursive field"));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return this.element.hmap(typeFamily, intFunction);
    }

    @Override
    public String toString() {
        return "NameTag[" + this.name + ": " + String.valueOf(this.element) + "]";
    }

    public static final class TagType<A>
    extends Type<A> {
        protected final String name;
        protected final Type<A> element;

        public TagType(String string, Type<A> type) {
            this.name = string;
            this.element = type;
        }

        @Override
        public RewriteResult<A, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            return this.wrap(this.element.rewriteOrNop(typeRewriteRule));
        }

        private <B> RewriteResult<A, B> wrap(RewriteResult<A, B> rewriteResult) {
            if (rewriteResult.view().isNop()) {
                return rewriteResult;
            }
            TagType<B> tagType = DSL.field(this.name, rewriteResult.view().newType());
            return TagType.opticView(this, rewriteResult, new TypedOptic(Profunctor.Mu.TYPE_TOKEN, this, tagType, rewriteResult.view().type(), rewriteResult.view().newType(), Optics.id()));
        }

        @Override
        public Optional<RewriteResult<A, ?>> one(TypeRewriteRule typeRewriteRule) {
            Optional<RewriteResult<A, ?>> optional = typeRewriteRule.rewrite(this.element);
            return optional.map(this::wrap);
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return DSL.field(this.name, this.element.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.field(this.name, this.element.template());
        }

        @Override
        protected Codec<A> buildCodec() {
            return ((MapCodec)this.element.codec().fieldOf(this.name)).codec();
        }

        public String toString() {
            return "Tag[\"" + this.name + "\", " + String.valueOf(this.element) + "]";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            TagType tagType = (TagType)object;
            return Objects.equals(this.name, tagType.name) && this.element.equals(tagType.element, bl, bl2);
        }

        public int hashCode() {
            int n = this.name.hashCode();
            n = 31 * n + this.element.hashCode();
            return n;
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            if (Objects.equals(string, this.name)) {
                return Optional.of(this.element);
            }
            return Optional.empty();
        }

        @Override
        public Optional<A> point(DynamicOps<?> dynamicOps) {
            return this.element.point(dynamicOps);
        }

        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            return this.element.findType(type, type2, typeMatcher, bl).mapLeft(this::wrapOptic);
        }

        private <B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> typedOptic) {
            return typedOptic.castOuter(DSL.field(this.name, typedOptic.sType()), DSL.field(this.name, typedOptic.tType()));
        }

        public String name() {
            return this.name;
        }

        public Type<A> element() {
            return this.element;
        }
    }
}

