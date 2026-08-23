/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 */
package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import org.slf4j.Logger;

public class TrapezoidHeight
extends HeightProvider {
    public static final MapCodec<TrapezoidHeight> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)VerticalAnchor.CODEC.fieldOf("min_inclusive")).forGetter(trapezoidHeight -> trapezoidHeight.minInclusive), ((MapCodec)VerticalAnchor.CODEC.fieldOf("max_inclusive")).forGetter(trapezoidHeight -> trapezoidHeight.maxInclusive), Codec.INT.optionalFieldOf("plateau", 0).forGetter(trapezoidHeight -> trapezoidHeight.plateau)).apply((Applicative<TrapezoidHeight, ?>)instance, TrapezoidHeight::new));
    private static final Logger LOGGER = LogUtils.getLogger();
    private final VerticalAnchor minInclusive;
    private final VerticalAnchor maxInclusive;
    private final int plateau;

    private TrapezoidHeight(VerticalAnchor verticalAnchor, VerticalAnchor verticalAnchor2, int n) {
        this.minInclusive = verticalAnchor;
        this.maxInclusive = verticalAnchor2;
        this.plateau = n;
    }

    public static TrapezoidHeight of(VerticalAnchor verticalAnchor, VerticalAnchor verticalAnchor2, int n) {
        return new TrapezoidHeight(verticalAnchor, verticalAnchor2, n);
    }

    public static TrapezoidHeight of(VerticalAnchor verticalAnchor, VerticalAnchor verticalAnchor2) {
        return TrapezoidHeight.of(verticalAnchor, verticalAnchor2, 0);
    }

    @Override
    public int sample(RandomSource randomSource, WorldGenerationContext worldGenerationContext) {
        int n;
        int n2 = this.minInclusive.resolveY(worldGenerationContext);
        if (n2 > (n = this.maxInclusive.resolveY(worldGenerationContext))) {
            LOGGER.warn("Empty height range: {}", (Object)this);
            return n2;
        }
        int n3 = n - n2;
        if (this.plateau >= n3) {
            return Mth.randomBetweenInclusive(randomSource, n2, n);
        }
        int n4 = (n3 - this.plateau) / 2;
        int n5 = n3 - n4;
        return n2 + Mth.randomBetweenInclusive(randomSource, 0, n5) + Mth.randomBetweenInclusive(randomSource, 0, n4);
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.TRAPEZOID;
    }

    public String toString() {
        if (this.plateau == 0) {
            return "triangle (" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + ")";
        }
        return "trapezoid(" + this.plateau + ") in [" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]";
    }
}

