/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.advancements.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.Mth;

public record DistancePredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles horizontal, MinMaxBounds.Doubles absolute) {
    public static final Codec<DistancePredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::x), MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::y), MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::z), MinMaxBounds.Doubles.CODEC.optionalFieldOf("horizontal", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::horizontal), MinMaxBounds.Doubles.CODEC.optionalFieldOf("absolute", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::absolute)).apply((Applicative<DistancePredicate, ?>)instance, DistancePredicate::new));

    public static DistancePredicate horizontal(MinMaxBounds.Doubles doubles) {
        return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, doubles, MinMaxBounds.Doubles.ANY);
    }

    public static DistancePredicate vertical(MinMaxBounds.Doubles doubles) {
        return new DistancePredicate(MinMaxBounds.Doubles.ANY, doubles, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
    }

    public static DistancePredicate absolute(MinMaxBounds.Doubles doubles) {
        return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, doubles);
    }

    public boolean matches(double d, double d2, double d3, double d4, double d5, double d6) {
        float f = (float)(d - d4);
        float f2 = (float)(d2 - d5);
        float f3 = (float)(d3 - d6);
        if (!(this.x.matches(Mth.abs(f)) && this.y.matches(Mth.abs(f2)) && this.z.matches(Mth.abs(f3)))) {
            return false;
        }
        if (!this.horizontal.matchesSqr(f * f + f3 * f3)) {
            return false;
        }
        return this.absolute.matchesSqr(f * f + f2 * f2 + f3 * f3);
    }
}

