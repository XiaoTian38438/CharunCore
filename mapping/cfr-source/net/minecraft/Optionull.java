/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;

public class Optionull {
    @Deprecated
    public static <T> T orElse(@Nullable T t, T t2) {
        return Objects.requireNonNullElse(t, t2);
    }

    public static <T, R> @Nullable R map(@Nullable T t, Function<T, R> function) {
        return t == null ? null : (R)function.apply(t);
    }

    public static <T, R> R mapOrDefault(@Nullable T t, Function<T, R> function, R r) {
        return t == null ? r : function.apply(t);
    }

    public static <T, R> R mapOrElse(@Nullable T t, Function<T, R> function, Supplier<R> supplier) {
        return t == null ? supplier.get() : function.apply(t);
    }

    public static <T> @Nullable T first(Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();
        return iterator.hasNext() ? (T)iterator.next() : null;
    }

    public static <T> T firstOrDefault(Collection<T> collection, T t) {
        Iterator<T> iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : t;
    }

    public static <T> T firstOrElse(Collection<T> collection, Supplier<T> supplier) {
        Iterator<T> iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : supplier.get();
    }

    public static <T> boolean isNullOrEmpty(T @Nullable [] TArray) {
        return TArray == null || TArray.length == 0;
    }

    public static boolean isNullOrEmpty(boolean @Nullable [] blArray) {
        return blArray == null || blArray.length == 0;
    }

    public static boolean isNullOrEmpty(byte @Nullable [] byArray) {
        return byArray == null || byArray.length == 0;
    }

    public static boolean isNullOrEmpty(char @Nullable [] cArray) {
        return cArray == null || cArray.length == 0;
    }

    public static boolean isNullOrEmpty(short @Nullable [] sArray) {
        return sArray == null || sArray.length == 0;
    }

    public static boolean isNullOrEmpty(int @Nullable [] nArray) {
        return nArray == null || nArray.length == 0;
    }

    public static boolean isNullOrEmpty(long @Nullable [] lArray) {
        return lArray == null || lArray.length == 0;
    }

    public static boolean isNullOrEmpty(float @Nullable [] fArray) {
        return fArray == null || fArray.length == 0;
    }

    public static boolean isNullOrEmpty(double @Nullable [] dArray) {
        return dArray == null || dArray.length == 0;
    }
}

