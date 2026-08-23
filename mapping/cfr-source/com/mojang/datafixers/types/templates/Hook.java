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

public record Hook(TypeTemplate element, HookFunction preRead, HookFunction postWrite) implements TypeTemplate
{
    @Override
    public int size() {
        return this.element.size();
    }

    @Override
    public TypeFamily apply(TypeFamily typeFamily) {
        return n -> DSL.hook(this.element.apply(typeFamily).apply(n), this.preRead, this.postWrite);
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

    private <A> RewriteResult<A, ?> cap(TypeFamily typeFamily, int n, RewriteResult<A, ?> rewriteResult) {
        return HookType.fix((HookType)this.apply(typeFamily).apply(n), rewriteResult);
    }

    @Override
    public String toString() {
        return "Hook[" + String.valueOf(this.element) + ", " + String.valueOf(this.preRead) + ", " + String.valueOf(this.postWrite) + "]";
    }

    public static interface HookFunction {
        public static final HookFunction IDENTITY = new HookFunction(){

            @Override
            public <T> T apply(DynamicOps<T> dynamicOps, T t) {
                return t;
            }
        };

        public <T> T apply(DynamicOps<T> var1, T var2);
    }

    public static final class HookType<A>
    extends Type<A> {
        private final Type<A> delegate;
        private final HookFunction preRead;
        private final HookFunction postWrite;

        public HookType(Type<A> type, HookFunction hookFunction, HookFunction hookFunction2) {
            this.delegate = type;
            this.preRead = hookFunction;
            this.postWrite = hookFunction2;
        }

        @Override
        protected Codec<A> buildCodec() {
            return new Codec<A>(){

                @Override
                public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    return delegate.codec().decode(dynamicOps, preRead.apply(dynamicOps, t)).setLifecycle(Lifecycle.experimental());
                }

                @Override
                public <T> DataResult<T> encode(A a, DynamicOps<T> dynamicOps, T t) {
                    return delegate.codec().encode(a, dynamicOps, t).map((? super R object) -> postWrite.apply(dynamicOps, object)).setLifecycle(Lifecycle.experimental());
                }
            };
        }

        @Override
        public RewriteResult<A, ?> all(TypeRewriteRule typeRewriteRule, boolean bl, boolean bl2) {
            return HookType.fix(this, this.delegate.rewriteOrNop(typeRewriteRule));
        }

        @Override
        public Optional<RewriteResult<A, ?>> one(TypeRewriteRule typeRewriteRule) {
            return typeRewriteRule.rewrite(this.delegate).map(rewriteResult -> HookType.fix(this, rewriteResult));
        }

        @Override
        public Type<?> updateMu(RecursiveTypeFamily recursiveTypeFamily) {
            return new HookType(this.delegate.updateMu(recursiveTypeFamily), this.preRead, this.postWrite);
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.hook(this.delegate.template(), this.preRead, this.postWrite);
        }

        @Override
        public Optional<TaggedChoice.TaggedChoiceType<?>> findChoiceType(String string, int n) {
            return this.delegate.findChoiceType(string, n);
        }

        @Override
        public Optional<Type<?>> findCheckedType(int n) {
            return this.delegate.findCheckedType(n);
        }

        @Override
        public Optional<Type<?>> findFieldTypeOpt(String string) {
            return this.delegate.findFieldTypeOpt(string);
        }

        @Override
        public Optional<A> point(DynamicOps<?> dynamicOps) {
            return this.delegate.point(dynamicOps);
        }

        @Override
        public <FT, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> type2, Type.TypeMatcher<FT, FR> typeMatcher, boolean bl) {
            return this.delegate.findType(type, type2, typeMatcher, bl).mapLeft(typedOptic -> HookType.wrapOptic(typedOptic, this.preRead, this.postWrite));
        }

        public static <A, B> RewriteResult<A, ?> fix(HookType<A> hookType, RewriteResult<A, B> rewriteResult) {
            if (rewriteResult.view().isNop()) {
                return RewriteResult.nop(hookType);
            }
            return HookType.opticView(hookType, rewriteResult, HookType.wrapOptic(TypedOptic.adapter(rewriteResult.view().type(), rewriteResult.view().newType()), hookType.preRead, hookType.postWrite));
        }

        protected static <A, B, FT, FR> TypedOptic<A, B, FT, FR> wrapOptic(TypedOptic<A, B, FT, FR> typedOptic, HookFunction hookFunction, HookFunction hookFunction2) {
            return typedOptic.castOuter(DSL.hook(typedOptic.sType(), hookFunction, hookFunction2), DSL.hook(typedOptic.tType(), hookFunction, hookFunction2));
        }

        public String toString() {
            return "HookType[" + String.valueOf(this.delegate) + ", " + String.valueOf(this.preRead) + ", " + String.valueOf(this.postWrite) + "]";
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            if (!(object instanceof HookType)) {
                return false;
            }
            HookType hookType = (HookType)object;
            return this.delegate.equals(hookType.delegate, bl, bl2) && Objects.equals(this.preRead, hookType.preRead) && Objects.equals(this.postWrite, hookType.postWrite);
        }

        public int hashCode() {
            int n = this.delegate.hashCode();
            n = 31 * n + this.preRead.hashCode();
            n = 31 * n + this.postWrite.hashCode();
            return n;
        }
    }
}

