/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class SurfaceWaterDepthFilter
extends PlacementFilter {
    public static final MapCodec<SurfaceWaterDepthFilter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.INT.fieldOf("max_water_depth")).forGetter(surfaceWaterDepthFilter -> surfaceWaterDepthFilter.maxWaterDepth)).apply((Applicative<SurfaceWaterDepthFilter, ?>)instance, SurfaceWaterDepthFilter::new));
    private final int maxWaterDepth;

    private SurfaceWaterDepthFilter(int n) {
        this.maxWaterDepth = n;
    }

    public static SurfaceWaterDepthFilter forMaxDepth(int n) {
        return new SurfaceWaterDepthFilter(n);
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos) {
        int n = placementContext.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ());
        int n2 = placementContext.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
        return n2 - n <= this.maxWaterDepth;
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.SURFACE_WATER_DEPTH_FILTER;
    }
}

