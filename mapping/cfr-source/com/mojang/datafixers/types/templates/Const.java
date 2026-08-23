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
import com.mojang.datafixers.FamilyOptic;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.AffineP;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;
import javax.annotation.Nullable;

public record Const(Type<?> type) implements TypeTemplate
{
    @Override
    public int size() {
        return 0;
    }

    @Override
    public TypeFamily apply(TypeFamily typeFamily) {
        return new TypeFamily(){

            @Override
            public Type<?> apply(int n) {
                return Const.this.type;
            }
        };
    }

    @Override
    public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> familyOptic, Type<A> type, Type<B> type2) {
        if (Objects.equals(this.type, type)) {
            return TypeFamily.familyOptic(n -> new TypedOptic((Set<TypeToken<? extends K1>>)ImmutableSet.of(Profunctor.Mu.TYPE_TOKEN), type, type2, type, type2, Optics.id()));
        }
        TypedOptic typedOptic = this.makeIgnoreOptic(this.type, type, type2);
        return TypeFamily.familyOptic(n -> typedOptic);
    }

    private <T, A, B> TypedOptic<T, T, A, B> makeIgnoreOptic(Type<T> type, Type<A> type2, Type<B> type3) {
        return new TypedOptic<T, T, A, B>(AffineP.Mu.TYPE_TOKEN, type, type, type2, type3, Optics.affine(Either::left, (object, object2) -> object2));
    }

    public <FT, FR> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int n, @Nullable String string, Type<FT> type, Type<FR> type2) {
        return DSL.fieldFinder(string, type).findType(this.type, type2, false).mapLeft(typedOptic -> new Const(typedOptic.tType()));
    }

    @Override
    public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily typeFamily, IntFunction<RewriteResult<?, ?>> intFunction) {
        return n -> RewriteResult.nop(this.type);
    }

    @Override
    public String toString() {
        return "Const[" + String.valueOf(this.type) + "]";
    }

    public static final class PrimitiveType<A>
    extends Type<A> {
        private final Codec<A> codec;

        public PrimitiveType(Codec<A> codec) {
            this.codec = codec;
        }

        @Override
        public boolean equals(Object object, boolean bl, boolean bl2) {
            return this == object;
        }

        @Override
        public TypeTemplate buildTemplate() {
            return DSL.constType(this);
        }

        @Override
        protected Codec<A> buildCodec() {
            return this.codec;
        }

        public String toString() {
            return this.codec.toString();
        }
    }
}

