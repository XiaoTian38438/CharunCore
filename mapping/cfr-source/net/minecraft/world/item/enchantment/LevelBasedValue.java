/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;

public interface LevelBasedValue {
    public static final Codec<LevelBasedValue> DISPATCH_CODEC = BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, mapCodec -> mapCodec);
    public static final Codec<LevelBasedValue> CODEC = Codec.either(Constant.CODEC, DISPATCH_CODEC).xmap(either -> either.map(constant -> constant, levelBasedValue -> levelBasedValue), levelBasedValue -> {
        Either<Object, LevelBasedValue> either;
        if (levelBasedValue instanceof Constant) {
            Constant constant = (Constant)levelBasedValue;
            either = Either.left(constant);
        } else {
            either = Either.right(levelBasedValue);
        }
        return either;
    });

    public static MapCodec<? extends LevelBasedValue> bootstrap(Registry<MapCodec<? extends LevelBasedValue>> registry) {
        Registry.register(registry, "clamped", Clamped.CODEC);
        Registry.register(registry, "fraction", Fraction.CODEC);
        Registry.register(registry, "levels_squared", LevelsSquared.CODEC);
        Registry.register(registry, "linear", Linear.CODEC);
        Registry.register(registry, "exponent", Exponent.CODEC);
        return Registry.register(registry, "lookup", Lookup.CODEC);
    }

    public static Constant constant(float f) {
        return new Constant(f);
    }

    public static Linear perLevel(float f, float f2) {
        return new Linear(f, f2);
    }

    public static Linear perLevel(float f) {
        return LevelBasedValue.perLevel(f, f);
    }

    public static Lookup lookup(List<Float> list, LevelBasedValue levelBasedValue) {
        return new Lookup(list, levelBasedValue);
    }

    public float calculate(int var1);

    public MapCodec<? extends LevelBasedValue> codec();

    public record Clamped(LevelBasedValue value, float min, float max) implements LevelBasedValue
    {
        public static final MapCodec<Clamped> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)CODEC.fieldOf("value")).forGetter(Clamped::value), ((MapCodec)Codec.FLOAT.fieldOf("min")).forGetter(Clamped::min), ((MapCodec)Codec.FLOAT.fieldOf("max")).forGetter(Clamped::max)).apply((Applicative<Clamped, ?>)instance, Clamped::new)).validate(clamped -> {
            if (clamped.max <= clamped.min) {
                return DataResult.error(() -> "Max must be larger than min, min: " + clamped.min + ", max: " + clamped.max);
            }
            return DataResult.success(clamped);
        });

        @Override
        public float calculate(int n) {
            return Mth.clamp(this.value.calculate(n), this.min, this.max);
        }

        public MapCodec<Clamped> codec() {
            return CODEC;
        }
    }

    public record Fraction(LevelBasedValue numerator, LevelBasedValue denominator) implements LevelBasedValue
    {
        public static final MapCodec<Fraction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)CODEC.fieldOf("numerator")).forGetter(Fraction::numerator), ((MapCodec)CODEC.fieldOf("denominator")).forGetter(Fraction::denominator)).apply((Applicative<Fraction, ?>)instance, Fraction::new));

        @Override
        public float calculate(int n) {
            float f = this.denominator.calculate(n);
            if (f == 0.0f) {
                return 0.0f;
            }
            return this.numerator.calculate(n) / f;
        }

        public MapCodec<Fraction> codec() {
            return CODEC;
        }
    }

    public record LevelsSquared(float added) implements LevelBasedValue
    {
        public static final MapCodec<LevelsSquared> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("added")).forGetter(LevelsSquared::added)).apply((Applicative<LevelsSquared, ?>)instance, LevelsSquared::new));

        @Override
        public float calculate(int n) {
            return (float)Mth.square(n) + this.added;
        }

        public MapCodec<LevelsSquared> codec() {
            return CODEC;
        }
    }

    public record Linear(float base, float perLevelAboveFirst) implements LevelBasedValue
    {
        public static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("base")).forGetter(Linear::base), ((MapCodec)Codec.FLOAT.fieldOf("per_level_above_first")).forGetter(Linear::perLevelAboveFirst)).apply((Applicative<Linear, ?>)instance, Linear::new));

        @Override
        public float calculate(int n) {
            return this.base + this.perLevelAboveFirst * (float)(n - 1);
        }

        public MapCodec<Linear> codec() {
            return CODEC;
        }
    }

    public record Exponent(LevelBasedValue base, LevelBasedValue power) implements LevelBasedValue
    {
        public static final MapCodec<Exponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)CODEC.fieldOf("base")).forGetter(Exponent::base), ((MapCodec)CODEC.fieldOf("power")).forGetter(Exponent::power)).apply((Applicative<Exponent, ?>)instance, Exponent::new));

        @Override
        public float calculate(int n) {
            return (float)Math.pow(this.base.calculate(n), this.power.calculate(n));
        }

        public MapCodec<Exponent> codec() {
            return CODEC;
        }
    }

    public record Lookup(List<Float> values, LevelBasedValue fallback) implements LevelBasedValue
    {
        public static final MapCodec<Lookup> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.listOf().fieldOf("values")).forGetter(Lookup::values), ((MapCodec)CODEC.fieldOf("fallback")).forGetter(Lookup::fallback)).apply((Applicative<Lookup, ?>)instance, Lookup::new));

        @Override
        public float calculate(int n) {
            return n <= this.values.size() ? this.values.get(n - 1).floatValue() : this.fallback.calculate(n);
        }

        public MapCodec<Lookup> codec() {
            return CODEC;
        }
    }

    public record Constant(float value) implements LevelBasedValue
    {
        public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
        public static final MapCodec<Constant> TYPED_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("value")).forGetter(Constant::value)).apply((Applicative<Constant, ?>)instance, Constant::new));

        @Override
        public float calculate(int n) {
            return this.value;
        }

        public MapCodec<Constant> codec() {
            return TYPED_CODEC;
        }
    }
}

