/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.datafixers.optics;

import com.mojang.datafixers.optics.Prism;
import com.mojang.datafixers.util.Either;

public final class Inj1<F, G, F2>
implements Prism<Either<F, G>, Either<F2, G>, F, F2> {
    public static final Inj1<?, ?, ?> INSTANCE = new Inj1();

    private Inj1() {
    }

    @Override
    public Either<Either<F2, G>, F> match(Either<F, G> either) {
        return either.map(Either::right, object -> Either.left(Either.right(object)));
    }

    @Override
    public Either<F2, G> build(F2 F2) {
        return Either.left(F2);
    }

    public String toString() {
        return "inj1";
    }
}

