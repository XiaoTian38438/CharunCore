/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

public record ScaleExponentially(LevelBasedValue base, LevelBasedValue exponent) implements EnchantmentValueEffect
{
    public static final MapCodec<ScaleExponentially> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LevelBasedValue.CODEC.fieldOf("base")).forGetter(ScaleExponentially::base), ((MapCodec)LevelBasedValue.CODEC.fieldOf("exponent")).forGetter(ScaleExponentially::exponent)).apply((Applicative<ScaleExponentially, ?>)instance, ScaleExponentially::new));

    @Override
    public float process(int n, RandomSource randomSource, float f) {
        return (float)((double)f * Math.pow(this.base.calculate(n), this.exponent.calculate(n)));
    }

    public MapCodec<ScaleExponentially> codec() {
        return CODEC;
    }
}

