/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record Keyframe<T>(int ticks, T value) {
    public static <T> Codec<Keyframe<T>> codec(Codec<T> codec) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks")).forGetter(Keyframe::ticks), ((MapCodec)codec.fieldOf("value")).forGetter(Keyframe::value)).apply((Applicative<Keyframe, ?>)instance, Keyframe::new));
    }
}

