/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.EasingType;
import net.minecraft.util.Keyframe;
import net.minecraft.util.KeyframeTrack;
import net.minecraft.world.attribute.LerpFunction;

public class KeyframeTrackSampler<T> {
    private final Optional<Integer> periodTicks;
    private final LerpFunction<T> lerp;
    private final List<Segment<T>> segments;

    KeyframeTrackSampler(KeyframeTrack<T> keyframeTrack, Optional<Integer> optional, LerpFunction<T> lerpFunction) {
        this.periodTicks = optional;
        this.lerp = lerpFunction;
        this.segments = KeyframeTrackSampler.bakeSegments(keyframeTrack, optional);
    }

    private static <T> List<Segment<T>> bakeSegments(KeyframeTrack<T> keyframeTrack, Optional<Integer> optional) {
        List<Keyframe<T>> list = keyframeTrack.keyframes();
        if (list.size() == 1) {
            T t = list.getFirst().value();
            return List.of(new Segment<T>(EasingType.CONSTANT, t, 0, t, 0));
        }
        ArrayList<Segment<T>> arrayList = new ArrayList<Segment<T>>();
        if (optional.isPresent()) {
            Keyframe<T> keyframe = list.getFirst();
            Keyframe<T> keyframe2 = list.getLast();
            arrayList.add(new Segment<T>(keyframeTrack, keyframe2, keyframe2.ticks() - optional.get(), keyframe, keyframe.ticks()));
            KeyframeTrackSampler.addSegmentsFromKeyframes(keyframeTrack, list, arrayList);
            arrayList.add(new Segment<T>(keyframeTrack, keyframe2, keyframe2.ticks(), keyframe, keyframe.ticks() + optional.get()));
        } else {
            KeyframeTrackSampler.addSegmentsFromKeyframes(keyframeTrack, list, arrayList);
        }
        return List.copyOf(arrayList);
    }

    private static <T> void addSegmentsFromKeyframes(KeyframeTrack<T> keyframeTrack, List<Keyframe<T>> list, List<Segment<T>> list2) {
        for (int i = 0; i < list.size() - 1; ++i) {
            Keyframe<T> keyframe = list.get(i);
            Keyframe<T> keyframe2 = list.get(i + 1);
            list2.add(new Segment<T>(keyframeTrack, keyframe, keyframe.ticks(), keyframe2, keyframe2.ticks()));
        }
    }

    public T sample(long l) {
        long l2 = this.loopTicks(l);
        Segment<T> segment = this.getSegmentAt(l2);
        if (l2 <= (long)segment.fromTicks) {
            return segment.fromValue;
        }
        if (l2 >= (long)segment.toTicks) {
            return segment.toValue;
        }
        float f = (float)(l2 - (long)segment.fromTicks) / (float)(segment.toTicks - segment.fromTicks);
        float f2 = segment.easing.apply(f);
        return this.lerp.apply(f2, segment.fromValue, segment.toValue);
    }

    private Segment<T> getSegmentAt(long l) {
        for (Segment<T> segment : this.segments) {
            if (l >= (long)segment.toTicks) continue;
            return segment;
        }
        return this.segments.getLast();
    }

    private long loopTicks(long l) {
        if (this.periodTicks.isPresent()) {
            return Math.floorMod(l, (int)this.periodTicks.get());
        }
        return l;
    }

    static final class Segment<T>
    extends Record {
        final EasingType easing;
        final T fromValue;
        final int fromTicks;
        final T toValue;
        final int toTicks;

        public Segment(KeyframeTrack<T> keyframeTrack, Keyframe<T> keyframe, int n, Keyframe<T> keyframe2, int n2) {
            this(keyframeTrack.easingType(), keyframe.value(), n, keyframe2.value(), n2);
        }

        Segment(EasingType easingType, T t, int n, T t2, int n2) {
            this.easing = easingType;
            this.fromValue = t;
            this.fromTicks = n;
            this.toValue = t2;
            this.toTicks = n2;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{Segment.class, "easing;fromValue;fromTicks;toValue;toTicks", "easing", "fromValue", "fromTicks", "toValue", "toTicks"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{Segment.class, "easing;fromValue;fromTicks;toValue;toTicks", "easing", "fromValue", "fromTicks", "toValue", "toTicks"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{Segment.class, "easing;fromValue;fromTicks;toValue;toTicks", "easing", "fromValue", "fromTicks", "toValue", "toTicks"}, this, object);
        }

        public EasingType easing() {
            return this.easing;
        }

        public T fromValue() {
            return this.fromValue;
        }

        public int fromTicks() {
            return this.fromTicks;
        }

        public T toValue() {
            return this.toValue;
        }

        public int toTicks() {
            return this.toTicks;
        }
    }
}

