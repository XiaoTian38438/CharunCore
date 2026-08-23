/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.attribute.modifier.FloatWithAlpha;

public interface FloatModifier<Argument>
extends AttributeModifier<Float, Argument> {
    public static final FloatModifier<FloatWithAlpha> ALPHA_BLEND = new FloatModifier<FloatWithAlpha>(){

        @Override
        public Float apply(Float f, FloatWithAlpha floatWithAlpha) {
            return Float.valueOf(Mth.lerp(floatWithAlpha.alpha(), f.floatValue(), floatWithAlpha.value()));
        }

        @Override
        public Codec<FloatWithAlpha> argumentCodec(EnvironmentAttribute<Float> environmentAttribute) {
            return FloatWithAlpha.CODEC;
        }

        @Override
        public LerpFunction<FloatWithAlpha> argumentKeyframeLerp(EnvironmentAttribute<Float> environmentAttribute) {
            return (f, floatWithAlpha, floatWithAlpha2) -> new FloatWithAlpha(Mth.lerp(f, floatWithAlpha.value(), floatWithAlpha2.value()), Mth.lerp(f, floatWithAlpha.alpha(), floatWithAlpha2.alpha()));
        }

        @Override
        public /* synthetic */ Object apply(Object object, Object object2) {
            return this.apply((Float)object, (FloatWithAlpha)object2);
        }
    };
    public static final FloatModifier<Float> ADD = Float::sum;
    public static final FloatModifier<Float> SUBTRACT = (f, f2) -> Float.valueOf(f.floatValue() - f2.floatValue());
    public static final FloatModifier<Float> MULTIPLY = (f, f2) -> Float.valueOf(f.floatValue() * f2.floatValue());
    public static final FloatModifier<Float> MINIMUM = Math::min;
    public static final FloatModifier<Float> MAXIMUM = Math::max;

    @FunctionalInterface
    public static interface Simple
    extends FloatModifier<Float> {
        @Override
        default public Codec<Float> argumentCodec(EnvironmentAttribute<Float> environmentAttribute) {
            return Codec.FLOAT;
        }

        @Override
        default public LerpFunction<Float> argumentKeyframeLerp(EnvironmentAttribute<Float> environmentAttribute) {
            return LerpFunction.ofFloat();
        }
    }
}

