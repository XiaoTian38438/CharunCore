/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.advancements.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public record LightPredicate(MinMaxBounds.Ints composite) {
    public static final Codec<LightPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(MinMaxBounds.Ints.CODEC.optionalFieldOf("light", MinMaxBounds.Ints.ANY).forGetter(LightPredicate::composite)).apply((Applicative<LightPredicate, ?>)instance, LightPredicate::new));

    public boolean matches(ServerLevel serverLevel, BlockPos blockPos) {
        if (!serverLevel.isLoaded(blockPos)) {
            return false;
        }
        return this.composite.matches(serverLevel.getMaxLocalRawBrightness(blockPos));
    }

    public static class Builder {
        private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;

        public static Builder light() {
            return new Builder();
        }

        public Builder setComposite(MinMaxBounds.Ints ints) {
            this.composite = ints;
            return this;
        }

        public LightPredicate build() {
            return new LightPredicate(this.composite);
        }
    }
}

