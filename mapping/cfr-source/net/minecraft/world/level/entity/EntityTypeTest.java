/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.level.entity;

import org.jspecify.annotations.Nullable;

public interface EntityTypeTest<B, T extends B> {
    public static <B, T extends B> EntityTypeTest<B, T> forClass(final Class<T> clazz) {
        return new EntityTypeTest<B, T>(){

            @Override
            public @Nullable T tryCast(B b) {
                return clazz.isInstance(b) ? b : null;
            }

            @Override
            public Class<? extends B> getBaseClass() {
                return clazz;
            }
        };
    }

    public static <B, T extends B> EntityTypeTest<B, T> forExactClass(final Class<T> clazz) {
        return new EntityTypeTest<B, T>(){

            @Override
            public @Nullable T tryCast(B b) {
                return clazz.equals(b.getClass()) ? b : null;
            }

            @Override
            public Class<? extends B> getBaseClass() {
                return clazz;
            }
        };
    }

    public @Nullable T tryCast(B var1);

    public Class<? extends B> getBaseClass();
}

