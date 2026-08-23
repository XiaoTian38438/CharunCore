/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PointedDripstoneConfiguration
implements FeatureConfiguration {
    public static final Codec<PointedDripstoneConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("chance_of_taller_dripstone")).orElse(Float.valueOf(0.2f)).forGetter(pointedDripstoneConfiguration -> Float.valueOf(pointedDripstoneConfiguration.chanceOfTallerDripstone)), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("chance_of_directional_spread")).orElse(Float.valueOf(0.7f)).forGetter(pointedDripstoneConfiguration -> Float.valueOf(pointedDripstoneConfiguration.chanceOfDirectionalSpread)), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("chance_of_spread_radius2")).orElse(Float.valueOf(0.5f)).forGetter(pointedDripstoneConfiguration -> Float.valueOf(pointedDripstoneConfiguration.chanceOfSpreadRadius2)), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("chance_of_spread_radius3")).orElse(Float.valueOf(0.5f)).forGetter(pointedDripstoneConfiguration -> Float.valueOf(pointedDripstoneConfiguration.chanceOfSpreadRadius3))).apply((Applicative<PointedDripstoneConfiguration, ?>)instance, PointedDripstoneConfiguration::new));
    public final float chanceOfTallerDripstone;
    public final float chanceOfDirectionalSpread;
    public final float chanceOfSpreadRadius2;
    public final float chanceOfSpreadRadius3;

    public PointedDripstoneConfiguration(float f, float f2, float f3, float f4) {
        this.chanceOfTallerDripstone = f;
        this.chanceOfDirectionalSpread = f2;
        this.chanceOfSpreadRadius2 = f3;
        this.chanceOfSpreadRadius3 = f4;
    }
}

