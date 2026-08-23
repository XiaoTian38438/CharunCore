/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.attribute.modifier.AttributeModifier;

public enum BooleanModifier implements AttributeModifier<Boolean, Boolean>
{
    AND,
    NAND,
    OR,
    NOR,
    XOR,
    XNOR;


    @Override
    public Boolean apply(Boolean bl, Boolean bl2) {
        return switch (this.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> bl2 != false && bl != false;
            case 1 -> bl2 == false || bl == false;
            case 2 -> bl2 != false || bl != false;
            case 3 -> bl2 == false && bl == false;
            case 4 -> bl2 ^ bl;
            case 5 -> bl2 == bl;
        };
    }

    @Override
    public Codec<Boolean> argumentCodec(EnvironmentAttribute<Boolean> environmentAttribute) {
        return Codec.BOOL;
    }

    @Override
    public LerpFunction<Boolean> argumentKeyframeLerp(EnvironmentAttribute<Boolean> environmentAttribute) {
        return LerpFunction.ofConstant();
    }

    @Override
    public /* synthetic */ Object apply(Object object, Object object2) {
        return this.apply((Boolean)object, (Boolean)object2);
    }
}

