/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public final class RecordCodecBuilder<O, F>
implements App<Mu<O>, F> {
    private final Function<O, F> getter;
    private final Function<O, MapEncoder<F>> encoder;
    private final MapDecoder<F> decoder;

    public static <O, F> RecordCodecBuilder<O, F> unbox(App<Mu<O>, F> app) {
        return (RecordCodecBuilder)app;
    }

    private RecordCodecBuilder(Function<O, F> function, Function<O, MapEncoder<F>> function2, MapDecoder<F> mapDecoder) {
        this.getter = function;
        this.encoder = function2;
        this.decoder = mapDecoder;
    }

    public static <O> Instance<O> instance() {
        return new Instance();
    }

    public static <O, F> RecordCodecBuilder<O, F> of(Function<O, F> function, String string, Codec<F> codec) {
        return RecordCodecBuilder.of(function, codec.fieldOf(string));
    }

    public static <O, F> RecordCodecBuilder<O, F> of(Function<O, F> function, MapCodec<F> mapCodec) {
        return new RecordCodecBuilder<Object, F>(function, object -> mapCodec, mapCodec);
    }

    public static <O, F> RecordCodecBuilder<O, F> point(F f) {
        return new RecordCodecBuilder<Object, Object>(object2 -> f, object -> Encoder.empty(), Decoder.unit(f));
    }

    public static <O, F> RecordCodecBuilder<O, F> stable(F f) {
        return RecordCodecBuilder.point(f, Lifecycle.stable());
    }

    public static <O, F> RecordCodecBuilder<O, F> deprecated(F f, int n) {
        return RecordCodecBuilder.point(f, Lifecycle.deprecated(n));
    }

    public static <O, F> RecordCodecBuilder<O, F> point(F f, Lifecycle lifecycle) {
        return new RecordCodecBuilder<Object, Object>(object2 -> f, object -> Encoder.empty().withLifecycle(lifecycle), Decoder.unit(f).withLifecycle(lifecycle));
    }

    public static <O> Codec<O> create(Function<Instance<O>, ? extends App<Mu<O>, O>> function) {
        return RecordCodecBuilder.build(function.apply(RecordCodecBuilder.instance())).codec();
    }

    public static <O> MapCodec<O> mapCodec(Function<Instance<O>, ? extends App<Mu<O>, O>> function) {
        return RecordCodecBuilder.build(function.apply(RecordCodecBuilder.instance()));
    }

    public <E> RecordCodecBuilder<O, E> dependent(Function<O, E> function, final MapEncoder<E> mapEncoder, final Function<? super F, ? extends MapDecoder<E>> function2) {
        return new RecordCodecBuilder<Object, E>(function, object -> mapEncoder, new MapDecoder.Implementation<E>(){

            @Override
            public <T> DataResult<E> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return RecordCodecBuilder.this.decoder.decode(dynamicOps, mapLike).map(function2).flatMap((? super R mapDecoder) -> mapDecoder.decode(dynamicOps, mapLike).map(Function.identity()));
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return mapEncoder.keys(dynamicOps);
            }

            public String toString() {
                return "Dependent[" + String.valueOf(mapEncoder) + "]";
            }
        });
    }

    public static <O> MapCodec<O> build(App<Mu<O>, O> app) {
        final RecordCodecBuilder<O, O> recordCodecBuilder = RecordCodecBuilder.unbox(app);
        return new MapCodec<O>(){

            @Override
            public <T> DataResult<O> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                return recordCodecBuilder.decoder.decode(dynamicOps, mapLike);
            }

            @Override
            public <T> RecordBuilder<T> encode(O o, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                return recordCodecBuilder.encoder.apply(o).encode(o, dynamicOps, recordBuilder);
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                return recordCodecBuilder.decoder.keys(dynamicOps);
            }

            public String toString() {
                return "RecordCodec[" + String.valueOf(recordCodecBuilder.decoder) + "]";
            }
        };
    }

    public static final class Instance<O>
    implements Applicative<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, Mu<O>> {
        public <A> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, A> stable(A a) {
            return RecordCodecBuilder.stable(a);
        }

        public <A> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, A> deprecated(A a, int n) {
            return RecordCodecBuilder.deprecated(a, n);
        }

        public <A> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, A> point(A a, Lifecycle lifecycle) {
            return RecordCodecBuilder.point(a, lifecycle);
        }

        @Override
        public <A> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, A> point(A a) {
            return RecordCodecBuilder.point(a);
        }

        @Override
        public <A, R> Function<App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, A>, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, R>> lift1(App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, Function<A, R>> app) {
            return app2 -> {
                final RecordCodecBuilder recordCodecBuilder = RecordCodecBuilder.unbox(app);
                final RecordCodecBuilder recordCodecBuilder2 = RecordCodecBuilder.unbox(app2);
                return new RecordCodecBuilder<Object, Object>(object -> ((Function)recordCodecBuilder.getter.apply(object)).apply(recordCodecBuilder2.getter.apply(object)), object -> {
                    final MapEncoder mapEncoder = recordCodecBuilder.encoder.apply(object);
                    final MapEncoder mapEncoder2 = recordCodecBuilder2.encoder.apply(object);
                    final Object f = recordCodecBuilder2.getter.apply(object);
                    return new MapEncoder.Implementation<R>(){

                        @Override
                        public <T> RecordBuilder<T> encode(R r, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                            mapEncoder.encode(object2 -> r, dynamicOps, recordBuilder);
                            mapEncoder2.encode(f, dynamicOps, recordBuilder);
                            return recordBuilder;
                        }

                        @Override
                        public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                            return Stream.concat(mapEncoder2.keys(dynamicOps), mapEncoder.keys(dynamicOps));
                        }

                        public String toString() {
                            return String.valueOf(mapEncoder) + " * " + String.valueOf(mapEncoder2);
                        }
                    };
                }, new MapDecoder.Implementation<R>(){

                    @Override
                    public <T> DataResult<R> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                        return recordCodecBuilder2.decoder.decode(dynamicOps, mapLike).flatMap((? super R object) -> recordCodecBuilder3.decoder.decode(dynamicOps, mapLike).map((? super R function) -> function.apply(object)));
                    }

                    @Override
                    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                        return Stream.concat(recordCodecBuilder2.decoder.keys(dynamicOps), recordCodecBuilder.decoder.keys(dynamicOps));
                    }

                    public String toString() {
                        return String.valueOf(recordCodecBuilder.decoder) + " * " + String.valueOf(recordCodecBuilder2.decoder);
                    }
                });
            };
        }

        @Override
        public <A, B, R> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, R> ap2(App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, BiFunction<A, B, R>> app, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, A> app2, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, B> app3) {
            final RecordCodecBuilder<O, BiFunction<A, B, R>> recordCodecBuilder = RecordCodecBuilder.unbox(app);
            final RecordCodecBuilder<O, A> recordCodecBuilder2 = RecordCodecBuilder.unbox(app2);
            final RecordCodecBuilder<O, B> recordCodecBuilder3 = RecordCodecBuilder.unbox(app3);
            return new RecordCodecBuilder<Object, Object>(object -> ((BiFunction)recordCodecBuilder.getter.apply(object)).apply(recordCodecBuilder2.getter.apply(object), recordCodecBuilder3.getter.apply(object)), object -> {
                final MapEncoder mapEncoder = recordCodecBuilder.encoder.apply(object);
                final MapEncoder mapEncoder2 = recordCodecBuilder2.encoder.apply(object);
                final Object f = recordCodecBuilder2.getter.apply(object);
                final MapEncoder mapEncoder3 = recordCodecBuilder3.encoder.apply(object);
                final Object f2 = recordCodecBuilder3.getter.apply(object);
                return new MapEncoder.Implementation<R>(){

                    @Override
                    public <T> RecordBuilder<T> encode(R r, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                        mapEncoder.encode((object2, object3) -> r, dynamicOps, recordBuilder);
                        mapEncoder2.encode(f, dynamicOps, recordBuilder);
                        mapEncoder3.encode(f2, dynamicOps, recordBuilder);
                        return recordBuilder;
                    }

                    @Override
                    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                        return Stream.of(mapEncoder.keys(dynamicOps), mapEncoder2.keys(dynamicOps), mapEncoder3.keys(dynamicOps)).flatMap(Function.identity());
                    }

                    public String toString() {
                        return String.valueOf(mapEncoder) + " * " + String.valueOf(mapEncoder2) + " * " + String.valueOf(mapEncoder3);
                    }
                };
            }, new MapDecoder.Implementation<R>(){

                @Override
                public <T> DataResult<R> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                    return DataResult.unbox(DataResult.instance().ap2(recordCodecBuilder.decoder.decode(dynamicOps, mapLike), recordCodecBuilder2.decoder.decode(dynamicOps, mapLike), recordCodecBuilder3.decoder.decode(dynamicOps, mapLike)));
                }

                @Override
                public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                    return Stream.of(recordCodecBuilder.decoder.keys(dynamicOps), recordCodecBuilder2.decoder.keys(dynamicOps), recordCodecBuilder3.decoder.keys(dynamicOps)).flatMap(Function.identity());
                }

                public String toString() {
                    return String.valueOf(recordCodecBuilder.decoder) + " * " + String.valueOf(recordCodecBuilder2.decoder) + " * " + String.valueOf(recordCodecBuilder3.decoder);
                }
            });
        }

        @Override
        public <T1, T2, T3, R> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, R> ap3(App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, Function3<T1, T2, T3, R>> app, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T1> app2, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T2> app3, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T3> app4) {
            final RecordCodecBuilder<O, Function3<T1, T2, T3, R>> recordCodecBuilder = RecordCodecBuilder.unbox(app);
            final RecordCodecBuilder<O, T1> recordCodecBuilder2 = RecordCodecBuilder.unbox(app2);
            final RecordCodecBuilder<O, T2> recordCodecBuilder3 = RecordCodecBuilder.unbox(app3);
            final RecordCodecBuilder<O, T3> recordCodecBuilder4 = RecordCodecBuilder.unbox(app4);
            return new RecordCodecBuilder<Object, Object>(object -> ((Function3)recordCodecBuilder.getter.apply(object)).apply(recordCodecBuilder2.getter.apply(object), recordCodecBuilder3.getter.apply(object), recordCodecBuilder4.getter.apply(object)), object -> {
                final MapEncoder mapEncoder = recordCodecBuilder.encoder.apply(object);
                final MapEncoder mapEncoder2 = recordCodecBuilder2.encoder.apply(object);
                final Object f = recordCodecBuilder2.getter.apply(object);
                final MapEncoder mapEncoder3 = recordCodecBuilder3.encoder.apply(object);
                final Object f2 = recordCodecBuilder3.getter.apply(object);
                final MapEncoder mapEncoder4 = recordCodecBuilder4.encoder.apply(object);
                final Object f3 = recordCodecBuilder4.getter.apply(object);
                return new MapEncoder.Implementation<R>(){

                    @Override
                    public <T> RecordBuilder<T> encode(R r, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                        mapEncoder.encode((object2, object3, object4) -> r, dynamicOps, recordBuilder);
                        mapEncoder2.encode(f, dynamicOps, recordBuilder);
                        mapEncoder3.encode(f2, dynamicOps, recordBuilder);
                        mapEncoder4.encode(f3, dynamicOps, recordBuilder);
                        return recordBuilder;
                    }

                    @Override
                    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                        return Stream.of(mapEncoder.keys(dynamicOps), mapEncoder2.keys(dynamicOps), mapEncoder3.keys(dynamicOps), mapEncoder4.keys(dynamicOps)).flatMap(Function.identity());
                    }

                    public String toString() {
                        return String.valueOf(mapEncoder) + " * " + String.valueOf(mapEncoder2) + " * " + String.valueOf(mapEncoder3) + " * " + String.valueOf(mapEncoder4);
                    }
                };
            }, new MapDecoder.Implementation<R>(){

                @Override
                public <T> DataResult<R> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                    return DataResult.unbox(DataResult.instance().ap3(recordCodecBuilder.decoder.decode(dynamicOps, mapLike), recordCodecBuilder2.decoder.decode(dynamicOps, mapLike), recordCodecBuilder3.decoder.decode(dynamicOps, mapLike), recordCodecBuilder4.decoder.decode(dynamicOps, mapLike)));
                }

                @Override
                public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                    return Stream.of(recordCodecBuilder.decoder.keys(dynamicOps), recordCodecBuilder2.decoder.keys(dynamicOps), recordCodecBuilder3.decoder.keys(dynamicOps), recordCodecBuilder4.decoder.keys(dynamicOps)).flatMap(Function.identity());
                }

                public String toString() {
                    return String.valueOf(recordCodecBuilder.decoder) + " * " + String.valueOf(recordCodecBuilder2.decoder) + " * " + String.valueOf(recordCodecBuilder3.decoder) + " * " + String.valueOf(recordCodecBuilder4.decoder);
                }
            });
        }

        @Override
        public <T1, T2, T3, T4, R> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, R> ap4(App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, Function4<T1, T2, T3, T4, R>> app, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T1> app2, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T2> app3, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T3> app4, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T4> app5) {
            final RecordCodecBuilder<O, Function4<T1, T2, T3, T4, R>> recordCodecBuilder = RecordCodecBuilder.unbox(app);
            final RecordCodecBuilder<O, T1> recordCodecBuilder2 = RecordCodecBuilder.unbox(app2);
            final RecordCodecBuilder<O, T2> recordCodecBuilder3 = RecordCodecBuilder.unbox(app3);
            final RecordCodecBuilder<O, T3> recordCodecBuilder4 = RecordCodecBuilder.unbox(app4);
            final RecordCodecBuilder<O, T4> recordCodecBuilder5 = RecordCodecBuilder.unbox(app5);
            return new RecordCodecBuilder<Object, Object>(object -> ((Function4)recordCodecBuilder.getter.apply(object)).apply(recordCodecBuilder2.getter.apply(object), recordCodecBuilder3.getter.apply(object), recordCodecBuilder4.getter.apply(object), recordCodecBuilder5.getter.apply(object)), object -> {
                final MapEncoder mapEncoder = recordCodecBuilder.encoder.apply(object);
                final MapEncoder mapEncoder2 = recordCodecBuilder2.encoder.apply(object);
                final Object f = recordCodecBuilder2.getter.apply(object);
                final MapEncoder mapEncoder3 = recordCodecBuilder3.encoder.apply(object);
                final Object f2 = recordCodecBuilder3.getter.apply(object);
                final MapEncoder mapEncoder4 = recordCodecBuilder4.encoder.apply(object);
                final Object f3 = recordCodecBuilder4.getter.apply(object);
                final MapEncoder mapEncoder5 = recordCodecBuilder5.encoder.apply(object);
                final Object f4 = recordCodecBuilder5.getter.apply(object);
                return new MapEncoder.Implementation<R>(){

                    @Override
                    public <T> RecordBuilder<T> encode(R r, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
                        mapEncoder.encode((object2, object3, object4, object5) -> r, dynamicOps, recordBuilder);
                        mapEncoder2.encode(f, dynamicOps, recordBuilder);
                        mapEncoder3.encode(f2, dynamicOps, recordBuilder);
                        mapEncoder4.encode(f3, dynamicOps, recordBuilder);
                        mapEncoder5.encode(f4, dynamicOps, recordBuilder);
                        return recordBuilder;
                    }

                    @Override
                    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                        return Stream.of(mapEncoder.keys(dynamicOps), mapEncoder2.keys(dynamicOps), mapEncoder3.keys(dynamicOps), mapEncoder4.keys(dynamicOps), mapEncoder5.keys(dynamicOps)).flatMap(Function.identity());
                    }

                    public String toString() {
                        return String.valueOf(mapEncoder) + " * " + String.valueOf(mapEncoder2) + " * " + String.valueOf(mapEncoder3) + " * " + String.valueOf(mapEncoder4) + " * " + String.valueOf(mapEncoder5);
                    }
                };
            }, new MapDecoder.Implementation<R>(){

                @Override
                public <T> DataResult<R> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
                    return DataResult.unbox(DataResult.instance().ap4(recordCodecBuilder.decoder.decode(dynamicOps, mapLike), recordCodecBuilder2.decoder.decode(dynamicOps, mapLike), recordCodecBuilder3.decoder.decode(dynamicOps, mapLike), recordCodecBuilder4.decoder.decode(dynamicOps, mapLike), recordCodecBuilder5.decoder.decode(dynamicOps, mapLike)));
                }

                @Override
                public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
                    return Stream.of(recordCodecBuilder.decoder.keys(dynamicOps), recordCodecBuilder2.decoder.keys(dynamicOps), recordCodecBuilder3.decoder.keys(dynamicOps), recordCodecBuilder4.decoder.keys(dynamicOps), recordCodecBuilder5.decoder.keys(dynamicOps)).flatMap(Function.identity());
                }

                public String toString() {
                    return String.valueOf(recordCodecBuilder.decoder) + " * " + String.valueOf(recordCodecBuilder2.decoder) + " * " + String.valueOf(recordCodecBuilder3.decoder) + " * " + String.valueOf(recordCodecBuilder4.decoder) + " * " + String.valueOf(recordCodecBuilder5.decoder);
                }
            });
        }

        @Override
        public <T, R> App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, R> map(Function<? super T, ? extends R> function, App<com.mojang.serialization.codecs.RecordCodecBuilder$Mu<O>, T> app) {
            final RecordCodecBuilder recordCodecBuilder = RecordCodecBuilder.unbox(app);
            final Function function2 = recordCodecBuilder.getter;
            return new RecordCodecBuilder<Object, R>(function2.andThen(function), object -> new MapEncoder.Implementation<R>(){
                private final MapEncoder<T> encoder;
                {
                    this.encoder = recordCodecBuilder.encoder.apply(object);
                }

                @Override
                public <U> RecordBuilder<U> encode(R r, DynamicOps<U> dynamicOps, RecordBuilder<U> recordBuilder) {
                    return this.encoder.encode(function2.apply(object), dynamicOps, recordBuilder);
                }

                public <U> Stream<U> keys(DynamicOps<U> dynamicOps) {
                    return this.encoder.keys(dynamicOps);
                }

                public String toString() {
                    return String.valueOf(this.encoder) + "[mapped]";
                }
            }, recordCodecBuilder.decoder.map(function));
        }

        private static final class Mu<O>
        implements Applicative.Mu {
            private Mu() {
            }
        }
    }

    public static final class Mu<O>
    implements K1 {
    }
}

