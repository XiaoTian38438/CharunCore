/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.FieldDecoder;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Decoder<A> {
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> var1, T var2);

    default public <T> DataResult<A> parse(DynamicOps<T> dynamicOps, T t) {
        return this.decode(dynamicOps, t).map(Pair::getFirst);
    }

    default public <T> DataResult<Pair<A, T>> decode(Dynamic<T> dynamic) {
        return this.decode(dynamic.getOps(), dynamic.getValue());
    }

    default public <T> DataResult<A> parse(Dynamic<T> dynamic) {
        return this.decode(dynamic).map(Pair::getFirst);
    }

    default public Terminal<A> terminal() {
        return this::parse;
    }

    default public Boxed<A> boxed() {
        return this::decode;
    }

    default public Simple<A> simple() {
        return this::parse;
    }

    default public MapDecoder<A> fieldOf(String string) {
        return new FieldDecoder(string, this);
    }

    default public <B> Decoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
        return new Decoder<B>(){

            @Override
            public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return Decoder.this.decode(dynamicOps, t).flatMap((? super R pair) -> ((DataResult)function.apply(pair.getFirst())).map((? super R object) -> Pair.of(object, pair.getSecond())));
            }

            public String toString() {
                return Decoder.this.toString() + "[flatMapped]";
            }
        };
    }

    default public <B> Decoder<B> map(final Function<? super A, ? extends B> function) {
        return new Decoder<B>(){

            @Override
            public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return Decoder.this.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(function));
            }

            public String toString() {
                return Decoder.this.toString() + "[mapped]";
            }
        };
    }

    default public Decoder<A> promotePartial(final Consumer<String> consumer) {
        return new Decoder<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return Decoder.this.decode(dynamicOps, t).promotePartial(consumer);
            }

            public String toString() {
                return Decoder.this.toString() + "[promotePartial]";
            }
        };
    }

    default public Decoder<A> withLifecycle(final Lifecycle lifecycle) {
        return new Decoder<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return Decoder.this.decode(dynamicOps, t).setLifecycle(lifecycle);
            }

            public String toString() {
                return Decoder.this.toString();
            }
        };
    }

    public static <A> Decoder<A> ofTerminal(Terminal<? extends A> terminal) {
        return terminal.decoder().map(Function.identity());
    }

    public static <A> Decoder<A> ofBoxed(Boxed<? extends A> boxed) {
        return boxed.decoder().map(Function.identity());
    }

    public static <A> Decoder<A> ofSimple(Simple<? extends A> simple) {
        return simple.decoder().map(Function.identity());
    }

    public static <A> MapDecoder<A> unit(A a) {
        return Decoder.unit(() -> a);
    }

    public static <A> MapDecoder<A> unit(final Supplier<A> supplier) {
        return new MapDecoder.Implementation<A>(){

            @Override
            public <T> DataResult<A> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return DataResult.success(supplier.get());
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return Stream.empty();
            }

            public String toString() {
                return "UnitDecoder[" + String.valueOf(supplier.get()) + "]";
            }
        };
    }

    public static <A> Decoder<A> error(final String string) {
        return new Decoder<A>(){

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                return DataResult.error(() -> string);
            }

            public String toString() {
                return "ErrorDecoder[" + string + "]";
            }
        };
    }

    public static interface Terminal<A> {
        public <T> DataResult<A> decode(DynamicOps<T> var1, T var2);

        default public Decoder<A> decoder() {
            return new Decoder<A>(){

                @Override
                public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    return this.decode(dynamicOps, t).map((? super R object) -> Pair.of(object, dynamicOps.empty()));
                }

                public String toString() {
                    return "TerminalDecoder[" + String.valueOf(this) + "]";
                }
            };
        }
    }

    public static interface Boxed<A> {
        public <T> DataResult<Pair<A, T>> decode(Dynamic<T> var1);

        default public Decoder<A> decoder() {
            return new Decoder<A>(){

                @Override
                public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    return this.decode(new Dynamic<T>(dynamicOps, t));
                }

                public String toString() {
                    return "BoxedDecoder[" + String.valueOf(this) + "]";
                }
            };
        }
    }

    public static interface Simple<A> {
        public <T> DataResult<A> decode(Dynamic<T> var1);

        default public Decoder<A> decoder() {
            return new Decoder<A>(){

                @Override
                public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    return this.decode(new Dynamic<T>(dynamicOps, t)).map((? super R object) -> Pair.of(object, dynamicOps.empty()));
                }

                public String toString() {
                    return "SimpleDecoder[" + String.valueOf(this) + "]";
                }
            };
        }
    }
}

