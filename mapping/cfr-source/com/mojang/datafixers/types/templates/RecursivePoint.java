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
import com.mojang.datafixers.View;
import com.mojang.datafixers.functions.Functions;
import com.mojang.datafixers.functions.PointFreeRule;
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
import java.util.BitSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public record RecursivePoint(int index) implements TypeTemplate
{
    @Override
    public int size() {
        return this.index + 1;
    }

    @Override
    public TypeFamily apply(TypeFamily typeFamily) {
        final Type<?> type = typeFamily.apply(this.index);
        return new TypeFamily(){

            @Override
            public Type<?> apply(int n) {
                return type;
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> familyOptic.apply(this.index));
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        return Either.right(new Type.FieldNotFoundException("Recursion point"));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult rewriteResult = (RewriteResult)intFunction.apply(this.index);
            return this.cap(typeFamily, rewriteResult);
        };
    }

    public <S, T> RewriteResult<S, T> cap(TypeFamily typeFamily, RewriteResult<S, T> rewriteResult) {
        Type<?> type = typeFamily.apply(this.index);
        if (!(type instanceof RecursivePointType)) {
            throw new IllegalArgumentException("Type error: Recursive point template template got a non-recursice type as an input.");
        }
        if (!Objects.equals(rewriteResult.view().type(), type)) {
            throw new IllegalArgumentException("Type error: hmap function input type");
        }
        BitSet bitSet = (BitSet)rewriteResult.recData().clone();
        bitSet.set(this.index);
        return RewriteResult.create(rewriteResult.view(), bitSet);
    }

    @Override
    public String toString() {
        return "Id[" + this.index + "]";
    }

    public static final class RecursivePointType<A>
    extends Type<A> {
        private final RecursiveTypeFamily family;
        private final int index;
        private final Supplier<Type<A>> delegate;
        @Nullable
        private volatile Type<A> type;

        public RecursivePointType(RecursiveTypeFamily recursiveTypeFamily, int n, Supplier<Type<A>> supplier) {
            this.family = recursiveTypeFamily;
            this.index = n;
            this.delegate = supplier;
        }

        public RecursiveTypeFamily family() {
            return this.family;
        }

        public int index() {
            return this.index;
        }

        public Type<A> unfold() {
            if (this.type == null) {
                this.type = this.delegate.get();
            }
            return this.type;
        }

        @Override
        protected Codec<A> buildCodec() {
            return new Codec<A>(){

                @Override
                public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    return this.unfold().codec().decode(dynamicOps, t).setLifecycle(Lifecycle.experimental());
                }

                @Override
                public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                    return this.unfold().codec().encode(a, dynamicOps, t).setLifecycle(Lifecycle.experimental());
                }
            };
        }

        @Override
        public RewriteResult<A, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            return this.unfold().all(typeRewriteRule, bl, bl2);
        }

        @Override
        public Optional<RewriteResult<A, ?>> one(TypeRewriteRule typeRewriteRule) {
            return this.unfold().one(typeRewriteRule);
        }

        @Override
        public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule, boolean bl, boolean bl2) {
            Optional<RewriteResult<A, ?>> optional;
            if (bl && (optional = this.family.everywhere(this.index, typeRewriteRule, pointFreeRule).map(rewriteResult -> rewriteResult)).isPresent()) {
                return optional;
            }
            return Optional.of(RewriteResult.nop(this));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return recursiveTypeFamily.apply(this.index);
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.id(this.index);
        }

        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String string, int n) {
            return this.unfold().findChoiceType(string, this.index);
        }

        @Override
        public Optional<Type<?>> findCheckedType(int n) {
            return this.unfold().findCheckedType(this.index);
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            return this.unfold().findFieldTypeOpt(string);
        }

        @Override
        public Optional<A> point(DynamicOps<?> dynamicOps) {
            return this.unfold().point(dynamicOps);
        }

        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            return this.family.findType(this.index, type, type2, typeMatcher, bl).mapLeft(typedOptic -> {
                if (!Objects.equals(this, typedOptic.sType())) {
                    throw new IllegalStateException(":/");
                }
                return typedOptic;
            });
        }

        public String toString() {
            return "MuType[" + this.family.name() + "_" + this.index + "]";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (!(object instanceof RecursivePointType)) {
                return false;
            }
            RecursivePointType recursivePointType = (RecursivePointType)object;
            return (bl || Objects.equals(this.family, recursivePointType.family)) && this.index == recursivePointType.index;
        }

        public int hashCode() {
            int n = this.family.hashCode();
            n = 31 * n + this.index;
            return n;
        }

        public View<A, A> in() {
            return View.create(Functions.in(this));
        }

        public View<A, A> out() {
            return View.create(Functions.out(this));
        }
    }
}

