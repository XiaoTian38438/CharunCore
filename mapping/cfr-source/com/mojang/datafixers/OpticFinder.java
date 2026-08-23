/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.datafixers;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;

public interface OpticFinder<FT> {
    public Type<FT> type();

    public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> var1, Type<FR> var2, boolean var3);

    default public <A> Either<TypedOptic<A, ?, FT, FT>, Type.FieldNotFoundException> findType(Type<A> type, boolean bl) {
        return this.findType(type, this.type(), bl);
    }

    default public <GT> OpticFinder<FT> inField(final @Nullable String string, final Type<GT> type) {
        final OpticFinder opticFinder = this;
        return new OpticFinder<FT>(){

            @Override
            public Type<FT> type() {
                return opticFinder.type();
            }

            @Override
            public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> type3, Type<FR> type2, boolean bl) {
                Either either = opticFinder.findType(type, type2, bl);
                return either.map(typedOptic -> this.cap(type3, (TypedOptic)typedOptic, bl), Either::right);
            }

            private <A, FR, GR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> cap(Type<A> type2, TypedOptic<GT, GR, FT, FR> typedOptic, boolean bl) {
                Either either = DSL.fieldFinder(string, type).findType(type2, typedOptic.tType(), bl);
                return either.mapLeft(typedOptic2 -> typedOptic2.compose(typedOptic));
            }
        };
    }
}

