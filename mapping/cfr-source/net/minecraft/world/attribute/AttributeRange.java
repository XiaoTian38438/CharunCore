/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute;

import com.mojang.serialization.DataResult;
import net.minecraft.util.Mth;

public interface AttributeRange<Value> {
    public static final AttributeRange<Float> UNIT_FLOAT = AttributeRange.ofFloat(0.0f, 1.0f);
    public static final AttributeRange<Float> NON_NEGATIVE_FLOAT = AttributeRange.ofFloat(0.0f, Float.POSITIVE_INFINITY);

    public static <Value> AttributeRange<Value> any() {
        return new AttributeRange<Value>(){

            @Override
            public DataResult<Value> validate(Value Value2) {
                return DataResult.success(Value2);
            }

            @Override
            public Value sanitize(Value Value2) {
                return Value2;
            }
        };
    }

    public static AttributeRange<Float> ofFloat(final float f, final float f2) {
        return new AttributeRange<Float>(){

            @Override
            public DataResult<Float> validate(Float f3) {
                if (f3.floatValue() >= f && f3.floatValue() <= f2) {
                    return DataResult.success(f3);
                }
                return DataResult.error(() -> f3 + " is not in range [" + f + "; " + f2 + "]");
            }

            @Override
            public Float sanitize(Float f3) {
                if (f3.floatValue() >= f && f3.floatValue() <= f2) {
                    return f3;
                }
                return Float.valueOf(Mth.clamp(f3.floatValue(), f, f2));
            }
        };
    }

    public DataResult<Value> validate(Value var1);

    public Value sanitize(Value var1);
}

