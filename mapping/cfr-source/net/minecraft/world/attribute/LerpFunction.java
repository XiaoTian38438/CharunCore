/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute;

import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public interface LerpFunction<T> {
    public static LerpFunction<Float> ofFloat() {
        return Mth::lerp;
    }

    public static LerpFunction<Float> ofDegrees(float f) {
        return (f2, f3, f4) -> {
            float f5 = Mth.wrapDegrees(f4.floatValue() - f3.floatValue());
            if (Math.abs(f5) >= f) {
                return f4;
            }
            return Float.valueOf(f3.floatValue() + f2 * f5);
        };
    }

    public static <T> LerpFunction<T> ofConstant() {
        return (f, object, object2) -> object;
    }

    public static <T> LerpFunction<T> ofStep(float f) {
        return (f2, object, object2) -> f2 >= f ? object2 : object;
    }

    public static LerpFunction<Integer> ofColor() {
        return ARGB::srgbLerp;
    }

    public T apply(float var1, T var2, T var3);
}

