/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Comparators
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package net.minecraft.util;

import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.EasingType;
import net.minecraft.util.Keyframe;
import net.minecraft.util.KeyframeTrackSampler;
import net.minecraft.world.attribute.LerpFunction;

public record KeyframeTrack<T>(List<Keyframe<T>> keyframes, EasingType easingType) {
    public KeyframeTrack {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Track has no keyframes");
        }
    }

    public static <T> MapCodec<KeyframeTrack<T>> mapCodec(Codec<T> codec) {
        Codec<List> codec2 = Keyframe.codec(codec).listOf().validate(KeyframeTrack::validateKeyframes);
        return RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<O> instance) -> instance.group(((MapCodec)codec2.fieldOf("keyframes")).forGetter(KeyframeTrack::keyframes), EasingType.CODEC.optionalFieldOf("ease", EasingType.LINEAR).forGetter(KeyframeTrack::easingType)).apply((Applicative<KeyframeTrack, ?>)instance, KeyframeTrack::new));
    }

    static <T> DataResult<List<Keyframe<T>>> validateKeyframes(List<Keyframe<T>> list) {
        if (list.isEmpty()) {
            return DataResult.error(() -> "Keyframes must not be empty");
        }
        if (!Comparators.isInOrder(list, Comparator.comparingInt(Keyframe::ticks))) {
            return DataResult.error(() -> "Keyframes must be ordered by ticks field");
        }
        if (list.size() > 1) {
            int n = 0;
            int n2 = list.getLast().ticks();
            for (Keyframe keyframe : list) {
                if (keyframe.ticks() == n2) {
                    if (++n > 2) {
                        return DataResult.error(() -> "More than 2 keyframes on same tick: " + keyframe.ticks());
                    }
                } else {
                    n = 0;
                }
                n2 = keyframe.ticks();
            }
        }
        return DataResult.success(list);
    }

    public static DataResult<KeyframeTrack<?>> validatePeriod(KeyframeTrack<?> keyframeTrack, int n) {
        for (Keyframe<?> keyframe : keyframeTrack.keyframes()) {
            int n2 = keyframe.ticks();
            if (n2 >= 0 && n2 <= n) continue;
            return DataResult.error(() -> "Keyframe at tick " + keyframe.ticks() + " must be in range [0; " + n + "]");
        }
        return DataResult.success(keyframeTrack);
    }

    public KeyframeTrackSampler<T> bakeSampler(Optional<Integer> optional, LerpFunction<T> lerpFunction) {
        return new KeyframeTrackSampler<T>(this, optional, lerpFunction);
    }

    public static class Builder<T> {
        private final ImmutableList.Builder<Keyframe<T>> keyframes = ImmutableList.builder();
        private EasingType easing = EasingType.LINEAR;

        public Builder<T> addKeyframe(int n, T t) {
            this.keyframes.add(new Keyframe<T>(n, t));
            return this;
        }

        public Builder<T> setEasing(EasingType easingType) {
            this.easing = easingType;
            return this;
        }

        public KeyframeTrack<T> build() {
            List list = KeyframeTrack.validateKeyframes(this.keyframes.build()).getOrThrow();
            return new KeyframeTrack(list, this.easing);
        }
    }
}

