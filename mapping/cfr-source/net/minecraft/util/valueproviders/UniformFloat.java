/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.valueproviders;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviderType;

public class UniformFloat
extends FloatProvider {
    public static final MapCodec<UniformFloat> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.FLOAT.fieldOf("min_inclusive")).forGetter(uniformFloat -> Float.valueOf(uniformFloat.minInclusive)), ((MapCodec)Codec.FLOAT.fieldOf("max_exclusive")).forGetter(uniformFloat -> Float.valueOf(uniformFloat.maxExclusive))).apply((Applicative<UniformFloat, ?>)instance, UniformFloat::new)).validate(uniformFloat -> {
        if (uniformFloat.maxExclusive <= uniformFloat.minInclusive) {
            return DataResult.error(() -> "Max must be larger than min, min_inclusive: " + uniformFloat.minInclusive + ", max_exclusive: " + uniformFloat.maxExclusive);
        }
        return DataResult.success(uniformFloat);
    });
    private final float minInclusive;
    private final float maxExclusive;

    private UniformFloat(float f, float f2) {
        this.minInclusive = f;
        this.maxExclusive = f2;
    }

    public static UniformFloat of(float f, float f2) {
        if (f2 <= f) {
            throw new IllegalArgumentException("Max must exceed min");
        }
        return new UniformFloat(f, f2);
    }

    @Override
    public float sample(RandomSource randomSource) {
        return Mth.randomBetween(randomSource, this.minInclusive, this.maxExclusive);
    }

    @Override
    public float getMinValue() {
        return this.minInclusive;
    }

    @Override
    public float getMaxValue() {
        return this.maxExclusive;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderType.UNIFORM;
    }

    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxExclusive + "]";
    }
}

