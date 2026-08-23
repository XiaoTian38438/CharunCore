/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  org.slf4j.Logger
 */
package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import org.slf4j.Logger;

public class UniformHeight
extends HeightProvider {
    public static final MapCodec<UniformHeight> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)VerticalAnchor.CODEC.fieldOf("min_inclusive")).forGetter(uniformHeight -> uniformHeight.minInclusive), ((MapCodec)VerticalAnchor.CODEC.fieldOf("max_inclusive")).forGetter(uniformHeight -> uniformHeight.maxInclusive)).apply((Applicative<UniformHeight, ?>)instance, UniformHeight::new));
    private static final Logger LOGGER = LogUtils.getLogger();
    private final VerticalAnchor minInclusive;
    private final VerticalAnchor maxInclusive;
    private final LongSet warnedFor = new LongOpenHashSet();

    private UniformHeight(VerticalAnchor verticalAnchor, VerticalAnchor verticalAnchor2) {
        this.minInclusive = verticalAnchor;
        this.maxInclusive = verticalAnchor2;
    }

    public static UniformHeight of(VerticalAnchor verticalAnchor, VerticalAnchor verticalAnchor2) {
        return new UniformHeight(verticalAnchor, verticalAnchor2);
    }

    @Override
    public int sample(RandomSource randomSource, WorldGenerationContext worldGenerationContext) {
        int n;
        int n2 = this.minInclusive.resolveY(worldGenerationContext);
        if (n2 > (n = this.maxInclusive.resolveY(worldGenerationContext))) {
            if (this.warnedFor.add((long)n2 << 32 | (long)n)) {
                LOGGER.warn("Empty height range: {}", (Object)this);
            }
            return n2;
        }
        return Mth.randomBetweenInclusive(randomSource, n2, n);
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.UNIFORM;
    }

    public String toString() {
        return "[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]";
    }
}

