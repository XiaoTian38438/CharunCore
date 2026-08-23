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

public record AddValue(LevelBasedValue value) implements EnchantmentValueEffect
{
    public static final MapCodec<AddValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)LevelBasedValue.CODEC.fieldOf("value")).forGetter(AddValue::value)).apply((Applicative<AddValue, ?>)instance, AddValue::new));

    @Override
    public float process(int n, RandomSource randomSource, float f) {
        return f + this.value.calculate(n);
    }

    public MapCodec<AddValue> codec() {
        return CODEC;
    }
}

