/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.advancements.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.Mth;

public record MovementPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles speed, MinMaxBounds.Doubles horizontalSpeed, MinMaxBounds.Doubles verticalSpeed, MinMaxBounds.Doubles fallDistance) {
    public static final Codec<MovementPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::x), MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::y), MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::z), MinMaxBounds.Doubles.CODEC.optionalFieldOf("speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::speed), MinMaxBounds.Doubles.CODEC.optionalFieldOf("horizontal_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::horizontalSpeed), MinMaxBounds.Doubles.CODEC.optionalFieldOf("vertical_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::verticalSpeed), MinMaxBounds.Doubles.CODEC.optionalFieldOf("fall_distance", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::fallDistance)).apply((Applicative<MovementPredicate, ?>)instance, MovementPredicate::new));

    public static MovementPredicate speed(MinMaxBounds.Doubles doubles) {
        return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, doubles, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
    }

    public static MovementPredicate horizontalSpeed(MinMaxBounds.Doubles doubles) {
        return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, doubles, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
    }

    public static MovementPredicate verticalSpeed(MinMaxBounds.Doubles doubles) {
        return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, doubles, MinMaxBounds.Doubles.ANY);
    }

    public static MovementPredicate fallDistance(MinMaxBounds.Doubles doubles) {
        return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, doubles);
    }

    public boolean matches(double d, double d2, double d3, double d4) {
        if (!(this.x.matches(d) && this.y.matches(d2) && this.z.matches(d3))) {
            return false;
        }
        double d5 = Mth.lengthSquared(d, d2, d3);
        if (!this.speed.matchesSqr(d5)) {
            return false;
        }
        double d6 = Mth.lengthSquared(d, d3);
        if (!this.horizontalSpeed.matchesSqr(d6)) {
            return false;
        }
        double d7 = Math.abs(d2);
        if (!this.verticalSpeed.matches(d7)) {
            return false;
        }
        return this.fallDistance.matches(d4);
    }
}

