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
import java.util.Optional;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record Check(String name, int index, TypeTemplate element) implements TypeTemplate
{
    @Override
    public int size() {
        return Math.max(this.index + 1, this.element.size());
    }

    @Override
    public TypeFamily apply(final TypeFamily typeFamily) {
        return new TypeFamily(){

            @Override
            public Type<?> apply(int n) {
                if (n < 0) {
                    throw new IndexOutOfBoundsException();
                }
                return new CheckType(Check.this.name, n, Check.this.index, Check.this.element.apply(typeFamily).apply(n));
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        return TypeFamily.familyOptic(n -> this.element.applyO(familyOptic, type, type2).apply(n));
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        if (n == this.index) {
            return this.element.findFieldOrType(n, string, type, type2);
        }
        return Either.right(new Type.FieldNotFoundException("Not a matching index"));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> {
            RewriteResult<?, ?> rewriteResult = this.element.hmap(typeFamily, intFunction).apply(n);
            return this.cap(typeFamily, n, rewriteResult);
        };
    }

    private <A> RewriteResult<?, ?> cap(TypeFamily typeFamily, int n, RewriteResult<A, ?> rewriteResult) {
        return CheckType.fix((CheckType)this.apply(typeFamily).apply(n), rewriteResult);
    }

    @Override
    public String toString() {
        return "Tag[" + this.name + ", " + this.index + ": " + String.valueOf(this.element) + "]";
    }

    public static final class CheckType<A>
    extends Type<A> {
        private final String name;
        private final int index;
        private final int expectedIndex;
        private final Type<A> delegate;

        public CheckType(String string, int n, int n2, Type<A> type) {
            this.name = string;
            this.index = n;
            this.expectedIndex = n2;
            this.delegate = type;
        }

        @Override
        protected Codec<A> buildCodec() {
            return Codec.of(this.delegate.codec(), this::read);
        }

        private <T> DataResult<Pair<A, T>> read(DynamicOps<T> dynamicOps, T t) {
            if (this.index != this.expectedIndex) {
                return DataResult.error(() -> "Index mismatch: " + this.index + " != " + this.expectedIndex);
            }
            return this.delegate.codec().decode(dynamicOps, t);
        }

        public static <A, B> RewriteResult<A, ?> fix(CheckType<A> checkType, RewriteResult<A, B> rewriteResult) {
            if (rewriteResult.view().isNop()) {
                return RewriteResult.nop(checkType);
            }
            return CheckType.opticView(checkType, rewriteResult, CheckType.wrapOptic(checkType, TypedOptic.adapter(rewriteResult.view().type(), rewriteResult.view().newType())));
        }

        @Override
        public RewriteResult<A, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            if (bl2 && this.index != this.expectedIndex) {
                return RewriteResult.nop(this);
            }
            return CheckType.fix(this, this.delegate.rewriteOrNop(typeRewriteRule));
        }

        @Override
        public Optional<RewriteResult<A, ?>> everywhere(TypeRewriteRule typeRewriteRule, PointFreeRule pointFreeRule, boolean bl, boolean bl2) {
            if (bl2 && this.index != this.expectedIndex) {
                return Optional.empty();
            }
            return super.everywhere(typeRewriteRule, pointFreeRule, bl, bl2);
        }

        @Override
        public Optional<RewriteResult<A, ?>> one(TypeRewriteRule typeRewriteRule) {
            return typeRewriteRule.rewrite(this.delegate).map(rewriteResult -> CheckType.fix(this, rewriteResult));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return new CheckType(this.name, this.index, this.expectedIndex, this.delegate.updateMu(recursiveTypeFamily));
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.check(this.name, this.expectedIndex, this.delegate.template());
        }

        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String string, int n) {
            if (n == this.expectedIndex) {
                return this.delegate.findChoiceType(string, n);
            }
            return Optional.empty();
        }

        @Override
        public Optional<Type<?>> findCheckedType(int n) {
            if (n == this.expectedIndex) {
                return Optional.of(this.delegate);
            }
            return Optional.empty();
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            if (this.index == this.expectedIndex) {
                return this.delegate.findFieldTypeOpt(string);
            }
            return Optional.empty();
        }

        @Override
        public Optional<A> point(DynamicOps<?> dynamicOps) {
            if (this.index == this.expectedIndex) {
                return this.delegate.point(dynamicOps);
            }
            return Optional.empty();
        }

        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            if (this.index != this.expectedIndex) {
                return Either.right(new Type.FieldNotFoundException("Incorrect index in CheckType"));
            }
            return this.delegate.findType(type, type2, typeMatcher, bl).mapLeft(typedOptic -> CheckType.wrapOptic(this, typedOptic));
        }

        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(CheckType<A> checkType, TypedOptic<A, B, FT, FR> typedOptic) {
            return typedOptic.castOuter(checkType, new CheckType<B>(checkType.name, checkType.index, checkType.expectedIndex, typedOptic.tType()));
        }

        public String toString() {
            return "TypeTag[" + this.index + "~" + this.expectedIndex + "][" + this.name + ": " + String.valueOf(this.delegate) + "]";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (!(object instanceof CheckType)) {
                return false;
            }
            CheckType checkType = (CheckType)object;
            if (this.index == checkType.index && this.expectedIndex == checkType.expectedIndex) {
                if (!bl2) {
                    return true;
                }
                if (this.delegate.equals(checkType.delegate, bl, bl2)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            int n = this.index;
            n = 31 * n + this.expectedIndex;
            n = 31 * n + this.delegate.hashCode();
            return n;
        }
    }
}

