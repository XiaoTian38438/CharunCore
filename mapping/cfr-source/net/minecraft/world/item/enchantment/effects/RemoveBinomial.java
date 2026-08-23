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

public record RemoveBinomial(LevelBasedValue chance) implements EnchantmentValueEffect
{
    public static final MapCodec<RemoveBinomial> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LevelBasedValue.CODEC.fieldOf("chance")).forGetter(RemoveBinomial::chance)).apply((Applicative<RemoveBinomial, ?>)instance, RemoveBinomial::new));

    @Override
    public float process(int n, RandomSource randomSource, float f) {
        float f2 = this.chance.calculate(n);
        int n2 = 0;
        if (f <= 128.0f || f * f2 < 20.0f || f * (1.0f - f2) < 20.0f) {
            int n3 = 0;
            while ((float)n3 < f) {
                if (randomSource.nextFloat() < f2) {
                    ++n2;
                }
                ++n3;
            }
        } else {
            double d = Math.floor(f * f2);
            double d2 = Math.sqrt(f * f2 * (1.0f - f2));
            n2 = (int)Math.round(d + randomSource.nextGaussian() * d2);
            n2 = Math.clamp((long)n2, 0, (int)f);
        }
        return f - (float)n2;
    }

    public MapCodec<RemoveBinomial> codec() {
        return CODEC;
    }
}

