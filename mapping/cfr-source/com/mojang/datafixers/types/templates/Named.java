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
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record Named(String name, TypeTemplate element) implements TypeTemplate
{
    @Override
    public int size() {
        return this.element.size();
    }

    @Override
    public TypeFamily apply(TypeFamily typeFamily) {
        return n -> DSL.named(this.name, this.element.apply(typeFamily).apply(n));
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.element.applyO(familyOptic, type, type2).apply(n));
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        return this.element.findFieldOrType(n, string, type, type2);
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult<?, ?> rewriteResult = this.element.hmap(typeFamily, intFunction).apply(n);
            return this.cap(typeFamily, n, rewriteResult);
        };
    }

    private <A> RewriteResult<Pair<String, A>, ?> cap(TypeFamily typeFamily, int n, RewriteResult<A, ?> rewriteResult) {
        return NamedType.fix((NamedType)this.apply(typeFamily).apply(n), rewriteResult);
    }

    @Override
    public String toString() {
        return "NamedTypeTag[" + this.name + ": " + String.valueOf(this.element) + "]";
    }

    public static final class NamedType<A>
    extends Type<Pair<String, A>> {
        protected final String name;
        protected final Type<A> element;

        public NamedType(String string, Type<A> type) {
            this.name = string;
            this.element = type;
        }

        public static <A, B> RewriteResult<Pair<String, A>, ?> fix(NamedType<A> namedType, RewriteResult<A, B> rewriteResult) {
            if (rewriteResult.view().isNop()) {
                return RewriteResult.nop(namedType);
            }
            return NamedType.opticView(namedType, rewriteResult, NamedType.wrapOptic(namedType.name, TypedOptic.adapter(rewriteResult.view().type(), rewriteResult.view().newType())));
        }

        @Override
        public RewriteResult<Pair<String, A>, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            RewriteResult<A, ?> rewriteResult = this.element.rewriteOrNop(typeRewriteRule);
            return NamedType.fix(this, rewriteResult);
        }

        @Override
        public Optional<RewriteResult<Pair<String, A>, ?>> one(TypeRewriteRule typeRewriteRule) {
            Optional<RewriteResult<A, ?>> optional = typeRewriteRule.rewrite(this.element);
            return optional.map(rewriteResult -> NamedType.fix(this, rewriteResult));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return DSL.named(this.name, this.element.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.named(this.name, this.element.template());
        }

        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String string, int n) {
            return this.element.findChoiceType(string, n);
        }

        @Override
        public Optional<Type<?>> findCheckedType(int n) {
            return this.element.findCheckedType(n);
        }

        @Override
        protected Codec<Pair<String, A>> buildCodec() {
            return new Codec<Pair<String, A>>(){

                @Override
                public <T> DataResult<Pair<Pair<String, A>, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    return element.codec().decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(object -> Pair.of(name, object))).setLifecycle(Lifecycle.experimental());
                }

                @Override
                public <T> DataResult<T> encode(Pair<String, A> pair, DynamicOps<T> dynamicOps, T t) {
                    if (!Objects.equals(pair.getFirst(), name)) {
                        return DataResult.error(() -> "Named type name doesn't match: expected: " + name + ", got: " + (String)pair.getFirst(), t);
                    }
                    return element.codec().encode(pair.getSecond(), dynamicOps, t).setLifecycle(Lifecycle.experimental());
                }
            };
        }

        public String toString() {
            return "NamedType[\"" + this.name + "\", " + String.valueOf(this.element) + "]";
        }

        public String name() {
            return this.name;
        }

        public Type<A> element() {
            return this.element;
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof NamedType)) {
                return false;
            }
            NamedType namedType = (NamedType)object;
            return Objects.equals(this.name, namedType.name) && this.element.equals(namedType.element, bl, bl2);
        }

        public int hashCode() {
            int n = this.name.hashCode();
            n = 31 * n + this.element.hashCode();
            return n;
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            return this.element.findFieldTypeOpt(string);
        }

        @Override
        public Optional<Pair<String, A>> point(DynamicOps<?> dynamicOps) {
            return this.element.point(dynamicOps).map(object -> Pair.of(this.name, object));
        }

        @Override
        public <FT, FR> Either<TypedOptic<Pair<String, A>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            return this.element.findType(type, type2, typeMatcher, bl).mapLeft(typedOptic -> NamedType.wrapOptic(this.name, typedOptic));
        }

        protected static <A, B, FT, FR> TypedOptic<Pair<String, A>, Pair<String, B>, FT, FR> wrapOptic(String string, TypedOptic<A, B, FT, FR> typedOptic) {
            return new TypedOptic<Pair<String, A>, Pair<String, B>, A, B>(Cartesian.Mu.TYPE_TOKEN, DSL.named(string, typedOptic.sType()), DSL.named(string, typedOptic.tType()), typedOptic.sType(), typedOptic.tType(), Optics.proj2()).compose(typedOptic);
        }
    }
}

