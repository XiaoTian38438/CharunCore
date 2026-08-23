/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.minecraft.advancements.criterion;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public interface MinMaxBounds<T extends Number> {
    public static final SimpleCommandExceptionType ERROR_EMPTY = new SimpleCommandExceptionType(Component.translatable("argument.range.empty"));
    public static final SimpleCommandExceptionType ERROR_SWAPPED = new SimpleCommandExceptionType(Component.translatable("argument.range.swapped"));

    public Bounds<T> bounds();

    default public Optional<T> min() {
        return this.bounds().min;
    }

    default public Optional<T> max() {
        return this.bounds().max;
    }

    default public boolean isAny() {
        return this.bounds().isAny();
    }

    public static final class Bounds<T extends Number>
    extends Record {
        final Optional<T> min;
        final Optional<T> max;

        public Bounds(Optional<T> optional, Optional<T> optional2) {
            this.min = optional;
            this.max = optional2;
        }

        public boolean isAny() {
            return this.min().isEmpty() && this.max().isEmpty();
        }

        public DataResult<Bounds<T>> validateSwappedBoundsInCodec() {
            if (this.areSwapped()) {
                return DataResult.error(() -> "Swapped bounds in range: " + String.valueOf(this.min()) + " is higher than " + String.valueOf(this.max()));
            }
            return DataResult.success(this);
        }

        public boolean areSwapped() {
            return this.min.isPresent() && this.max.isPresent() && ((Comparable)((Object)((Number)this.min.get()))).compareTo((Number)this.max.get()) > 0;
        }

        public Optional<T> asPoint() {
            Optional<T> optional;
            Optional<T> optional2 = this.min();
            return optional2.equals(optional = this.max()) ? optional2 : Optional.empty();
        }

        public static <T extends Number> Bounds<T> any() {
            return new Bounds(Optional.empty(), Optional.empty());
        }

        public static <T extends Number> Bounds<T> exactly(T t) {
            Optional<T> optional = Optional.of(t);
            return new Bounds<T>(optional, optional);
        }

        public static <T extends Number> Bounds<T> between(T t, T t2) {
            return new Bounds<T>(Optional.of(t), Optional.of(t2));
        }

        public static <T extends Number> Bounds<T> atLeast(T t) {
            return new Bounds<T>(Optional.of(t), Optional.empty());
        }

        public static <T extends Number> Bounds<T> atMost(T t) {
            return new Bounds(Optional.empty(), Optional.of(t));
        }

        public <U extends Number> Bounds<U> map(Function<T, U> function) {
            return new Bounds<U>(this.min.map(function), this.max.map(function));
        }

        static <T extends Number> Codec<Bounds<T>> createCodec(Codec<T> codec) {
            Codec codec2 = RecordCodecBuilder.create(instance -> instance.group(codec.optionalFieldOf("min").forGetter(Bounds::min), codec.optionalFieldOf("max").forGetter(Bounds::max)).apply((Applicative<Bounds, ?>)instance, Bounds::new));
            return Codec.either(codec2, codec).xmap(either -> either.map(bounds -> bounds, object -> Bounds.exactly((Number)object)), bounds -> {
                Optional optional = bounds.asPoint();
                return optional.isPresent() ? Either.right((Number)optional.get()) : Either.left(bounds);
            });
        }

        static <B extends ByteBuf, T extends Number> StreamCodec<B, Bounds<T>> createStreamCodec(final StreamCodec<B, T> streamCodec) {
            return new StreamCodec<B, Bounds<T>>(){
                private static final int MIN_FLAG = 1;
                private static final int MAX_FLAG = 2;

                @Override
                public Bounds<T> decode(B b) {
                    byte by = b.readByte();
                    Optional optional = (by & 1) != 0 ? Optional.of((Number)streamCodec.decode(b)) : Optional.empty();
                    Optional optional2 = (by & 2) != 0 ? Optional.of((Number)streamCodec.decode(b)) : Optional.empty();
                    return new Bounds(optional, optional2);
                }

                @Override
                public void encode(B b, Bounds<T> bounds) {
                    Optional<Number> optional = bounds.min();
                    Optional<Number> optional2 = bounds.max();
                    b.writeByte((optional.isPresent() ? 1 : 0) | (optional2.isPresent() ? 2 : 0));
                    optional.ifPresent(number -> streamCodec.encode(b, number));
                    optional2.ifPresent(number -> streamCodec.encode(b, number));
                }

                @Override
                public /* synthetic */ void encode(Object object, Object object2) {
                    this.encode((Object)((ByteBuf)object), (Bounds)object2);
                }

                @Override
                public /* synthetic */ Object decode(Object object) {
                    return this.decode((B)((ByteBuf)object));
                }
            };
        }

        public static <T extends Number> Bounds<T> fromReader(StringReader stringReader, Function<String, T> function, Supplier<DynamicCommandExceptionType> supplier) throws CommandSyntaxException {
            if (!stringReader.canRead()) {
                throw ERROR_EMPTY.createWithContext(stringReader);
            }
            int n = stringReader.getCursor();
            try {
                Optional<T> optional;
                Optional<T> optional2 = Bounds.readNumber(stringReader, function, supplier);
                if (stringReader.canRead(2) && stringReader.peek() == '.' && stringReader.peek(1) == '.') {
                    stringReader.skip();
                    stringReader.skip();
                    optional = Bounds.readNumber(stringReader, function, supplier);
                } else {
                    optional = optional2;
                }
                if (optional2.isEmpty() && optional.isEmpty()) {
                    throw ERROR_EMPTY.createWithContext(stringReader);
                }
                return new Bounds<T>(optional2, optional);
            }
            catch (CommandSyntaxException commandSyntaxException) {
                stringReader.setCursor(n);
                throw new CommandSyntaxException(commandSyntaxException.getType(), commandSyntaxException.getRawMessage(), commandSyntaxException.getInput(), n);
            }
        }

        private static <T extends Number> Optional<T> readNumber(StringReader stringReader, Function<String, T> function, Supplier<DynamicCommandExceptionType> supplier) throws CommandSyntaxException {
            int n = stringReader.getCursor();
            while (stringReader.canRead() && Bounds.isAllowedInputChar(stringReader)) {
                stringReader.skip();
            }
            String string = stringReader.getString().substring(n, stringReader.getCursor());
            if (string.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of((Number)function.apply(string));
            }
            catch (NumberFormatException numberFormatException) {
                throw supplier.get().createWithContext(stringReader, string);
            }
        }

        private static boolean isAllowedInputChar(StringReader stringReader) {
            char c = stringReader.peek();
            if (c >= '0' && c <= '9' || c == '-') {
                return true;
            }
            if (c == '.') {
                return !stringReader.canRead(2) || stringReader.peek(1) != '.';
            }
            return false;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{Bounds.class, "min;max", "min", "max"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{Bounds.class, "min;max", "min", "max"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{Bounds.class, "min;max", "min", "max"}, this, object);
        }

        public Optional<T> min() {
            return this.min;
        }

        public Optional<T> max() {
            return this.max;
        }
    }

    public record FloatDegrees(Bounds<Float> bounds) implements MinMaxBounds<Float>
    {
        public static final FloatDegrees ANY = new FloatDegrees(Bounds.any());
        public static final Codec<FloatDegrees> CODEC = Bounds.createCodec(Codec.FLOAT).xmap(FloatDegrees::new, FloatDegrees::bounds);
        public static final StreamCodec<ByteBuf, FloatDegrees> STREAM_CODEC = Bounds.createStreamCodec(ByteBufCodecs.FLOAT).map(FloatDegrees::new, FloatDegrees::bounds);

        public static FloatDegrees fromReader(StringReader stringReader) throws CommandSyntaxException {
            Bounds<Float> bounds = Bounds.fromReader(stringReader, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat);
            return new FloatDegrees(bounds);
        }
    }

    public record Doubles(Bounds<Double> bounds, Bounds<Double> boundsSqr) implements MinMaxBounds<Double>
    {
        public static final Doubles ANY = new Doubles(Bounds.any());
        public static final Codec<Doubles> CODEC = Bounds.createCodec(Codec.DOUBLE).validate(Bounds::validateSwappedBoundsInCodec).xmap(Doubles::new, Doubles::bounds);
        public static final StreamCodec<ByteBuf, Doubles> STREAM_CODEC = Bounds.createStreamCodec(ByteBufCodecs.DOUBLE).map(Doubles::new, Doubles::bounds);

        private Doubles(Bounds<Double> bounds) {
            this(bounds, bounds.map(Mth::square));
        }

        public static Doubles exactly(double d) {
            return new Doubles(Bounds.exactly(d));
        }

        public static Doubles between(double d, double d2) {
            return new Doubles(Bounds.between(d, d2));
        }

        public static Doubles atLeast(double d) {
            return new Doubles(Bounds.atLeast(d));
        }

        public static Doubles atMost(double d) {
            return new Doubles(Bounds.atMost(d));
        }

        public boolean matches(double d) {
            if (this.bounds.min.isPresent() && (Double)this.bounds.min.get() > d) {
                return false;
            }
            return this.bounds.max.isEmpty() || !((Double)this.bounds.max.get() < d);
        }

        public boolean matchesSqr(double d) {
            if (this.boundsSqr.min.isPresent() && (Double)this.boundsSqr.min.get() > d) {
                return false;
            }
            return this.boundsSqr.max.isEmpty() || !((Double)this.boundsSqr.max.get() < d);
        }

        public static Doubles fromReader(StringReader stringReader) throws CommandSyntaxException {
            int n = stringReader.getCursor();
            Bounds<Double> bounds = Bounds.fromReader(stringReader, Double::parseDouble, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidDouble);
            if (bounds.areSwapped()) {
                stringReader.setCursor(n);
                throw ERROR_SWAPPED.createWithContext(stringReader);
            }
            return new Doubles(bounds);
        }
    }

    public record Ints(Bounds<Integer> bounds, Bounds<Long> boundsSqr) implements MinMaxBounds<Integer>
    {
        public static final Ints ANY = new Ints(Bounds.any());
        public static final Codec<Ints> CODEC = Bounds.createCodec(Codec.INT).validate(Bounds::validateSwappedBoundsInCodec).xmap(Ints::new, Ints::bounds);
        public static final StreamCodec<ByteBuf, Ints> STREAM_CODEC = Bounds.createStreamCodec(ByteBufCodecs.INT).map(Ints::new, Ints::bounds);

        private Ints(Bounds<Integer> bounds) {
            this(bounds, bounds.map(n -> Mth.square(n.longValue())));
        }

        public static Ints exactly(int n) {
            return new Ints(Bounds.exactly(n));
        }

        public static Ints between(int n, int n2) {
            return new Ints(Bounds.between(n, n2));
        }

        public static Ints atLeast(int n) {
            return new Ints(Bounds.atLeast(n));
        }

        public static Ints atMost(int n) {
            return new Ints(Bounds.atMost(n));
        }

        public boolean matches(int n) {
            if (this.bounds.min.isPresent() && (Integer)this.bounds.min.get() > n) {
                return false;
            }
            return this.bounds.max.isEmpty() || (Integer)this.bounds.max.get() >= n;
        }

        public boolean matchesSqr(long l) {
            if (this.boundsSqr.min.isPresent() && (Long)this.boundsSqr.min.get() > l) {
                return false;
            }
            return this.boundsSqr.max.isEmpty() || (Long)this.boundsSqr.max.get() >= l;
        }

        public static Ints fromReader(StringReader stringReader) throws CommandSyntaxException {
            int n = stringReader.getCursor();
            Bounds<Integer> bounds = Bounds.fromReader(stringReader, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt);
            if (bounds.areSwapped()) {
                stringReader.setCursor(n);
                throw ERROR_SWAPPED.createWithContext(stringReader);
            }
            return new Ints(bounds);
        }
    }
}

