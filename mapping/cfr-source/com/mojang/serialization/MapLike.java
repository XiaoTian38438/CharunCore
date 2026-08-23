/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface MapLike<T> {
    public static final MapLike<Object> EMPTY = new MapLike<Object>(){

        @Override
        @Nullable
        public Object get(Object object) {
            return null;
        }

        @Override
        @Nullable
        public Object get(String string) {
            return null;
        }

        @Override
        public Stream<Pair<Object, Object>> entries() {
            return Stream.empty();
        }

        public String toString() {
            return "EmptyMapLike";
        }
    };

    public static <T> MapLike<T> empty() {
        return EMPTY;
    }

    @Nullable
    public T get(T var1);

    @Nullable
    public T get(String var1);

    public Stream<Pair<T, T>> entries();

    public static <T> MapLike<T> forMap(final Map<T, T> map, final DynamicOps<T> dynamicOps) {
        if (map.isEmpty()) {
            return MapLike.empty();
        }
        return new MapLike<T>(){

            @Override
            @Nullable
            public T get(T t) {
                return map.get(t);
            }

            @Override
            @Nullable
            public T get(String string) {
                return this.get(dynamicOps.createString(string));
            }

            @Override
            public Stream<Pair<T, T>> entries() {
                return map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()));
            }

            public String toString() {
                return "MapLike[" + String.valueOf(map) + "]";
            }
        };
    }
}

