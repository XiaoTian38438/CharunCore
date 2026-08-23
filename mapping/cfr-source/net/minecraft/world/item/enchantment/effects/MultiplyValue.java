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

public record MultiplyValue(LevelBasedValue factor) implements EnchantmentValueEffect
{
    public static final MapCodec<MultiplyValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LevelBasedValue.CODEC.fieldOf("factor")).forGetter(MultiplyValue::factor)).apply((Applicative<MultiplyValue, ?>)instance, MultiplyValue::new));

    @Override
    public float process(int n, RandomSource randomSource, float f) {
        return f * this.factor.calculate(n);
    }

    public MapCodec<MultiplyValue> codec() {
        return CODEC;
    }
}

