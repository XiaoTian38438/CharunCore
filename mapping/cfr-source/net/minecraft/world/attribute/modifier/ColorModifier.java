/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute.modifier;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.attribute.modifier.AttributeModifier;

public interface ColorModifier<Argument>
extends AttributeModifier<Integer, Argument> {
    public static final ColorModifier<Integer> ALPHA_BLEND = new ColorModifier<Integer>(){

        @Override
        public Integer apply(Integer n, Integer n2) {
            return ARGB.alphaBlend(n, n2);
        }

        @Override
        public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> environmentAttribute) {
            return ExtraCodecs.STRING_ARGB_COLOR;
        }

        @Override
        public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> environmentAttribute) {
            return LerpFunction.ofColor();
        }

        @Override
        public /* synthetic */ Object apply(Object object, Object object2) {
            return this.apply((Integer)object, (Integer)object2);
        }
    };
    public static final ColorModifier<Integer> ADD = ARGB::addRgb;
    public static final ColorModifier<Integer> SUBTRACT = ARGB::subtractRgb;
    public static final ColorModifier<Integer> MULTIPLY_RGB = ARGB::multiply;
    public static final ColorModifier<Integer> MULTIPLY_ARGB = ARGB::multiply;
    public static final ColorModifier<BlendToGray> BLEND_TO_GRAY = new ColorModifier<BlendToGray>(){

        @Override
        public Integer apply(Integer n, BlendToGray blendToGray) {
            int n2 = ARGB.scaleRGB(ARGB.greyscale(n), blendToGray.brightness);
            return ARGB.srgbLerp(blendToGray.factor, n, n2);
        }

        @Override
        public Codec<BlendToGray> argumentCodec(EnvironmentAttribute<Integer> environmentAttribute) {
            return BlendToGray.CODEC;
        }

        @Override
        public LerpFunction<BlendToGray> argumentKeyframeLerp(EnvironmentAttribute<Integer> environmentAttribute) {
            return (f, blendToGray, blendToGray2) -> new BlendToGray(Mth.lerp(f, blendToGray.brightness, blendToGray2.brightness), Mth.lerp(f, blendToGray.factor, blendToGray2.factor));
        }

        @Override
        public /* synthetic */ Object apply(Object object, Object object2) {
            return this.apply((Integer)object, (BlendToGray)object2);
        }
    };

    @FunctionalInterface
    public static interface RgbModifier
    extends ColorModifier<Integer> {
        @Override
        default public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> environmentAttribute) {
            return ExtraCodecs.STRING_RGB_COLOR;
        }

        @Override
        default public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> environmentAttribute) {
            return LerpFunction.ofColor();
        }
    }

    @FunctionalInterface
    public static interface ArgbModifier
    extends ColorModifier<Integer> {
        @Override
        default public Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> environmentAttribute) {
            return Codec.either(ExtraCodecs.STRING_ARGB_COLOR, ExtraCodecs.RGB_COLOR_CODEC).xmap(Either::unwrap, n -> ARGB.alpha(n) == 255 ? Either.right(n) : Either.left(n));
        }

        @Override
        default public LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> environmentAttribute) {
            return LerpFunction.ofColor();
        }
    }

    public static final class BlendToGray
    extends Record {
        final float brightness;
        final float factor;
        public static final Codec<BlendToGray> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("brightness")).forGetter(BlendToGray::brightness), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("factor")).forGetter(BlendToGray::factor)).apply((Applicative<BlendToGray, ?>)instance, BlendToGray::new));

        public BlendToGray(float f, float f2) {
            this.brightness = f;
            this.factor = f2;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{BlendToGray.class, "brightness;factor", "brightness", "factor"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{BlendToGray.class, "brightness;factor", "brightness", "factor"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{BlendToGray.class, "brightness;factor", "brightness", "factor"}, this, object);
        }

        public float brightness() {
            return this.brightness;
        }

        public float factor() {
            return this.factor;
        }
    }
}

