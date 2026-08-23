/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.level.biome;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.jspecify.annotations.Nullable;

public class FixedBiomeSource
extends BiomeSource
implements BiomeManager.NoiseBiomeSource {
    public static final MapCodec<FixedBiomeSource> CODEC = ((MapCodec)Biome.CODEC.fieldOf("biome")).xmap(FixedBiomeSource::new, fixedBiomeSource -> fixedBiomeSource.biome).stable();
    private final Holder<Biome> biome;

    public FixedBiomeSource(Holder<Biome> holder) {
        this.biome = holder;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.biome);
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int n, int n2, int n3, Climate.Sampler sampler) {
        return this.biome;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int n, int n2, int n3) {
        return this.biome;
    }

    @Override
    public @Nullable Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int n, int n2, int n3, int n4, int n5, Predicate<Holder<Biome>> predicate, RandomSource randomSource, boolean bl, Climate.Sampler sampler) {
        if (predicate.test(this.biome)) {
            if (bl) {
                return Pair.of(new BlockPos(n, n2, n3), this.biome);
            }
            return Pair.of(new BlockPos(n - n4 + randomSource.nextInt(n4 * 2 + 1), n2, n3 - n4 + randomSource.nextInt(n4 * 2 + 1)), this.biome);
        }
        return null;
    }

    @Override
    public @Nullable Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos blockPos, int n, int n2, int n3, Predicate<Holder<Biome>> predicate, Climate.Sampler sampler, LevelReader levelReader) {
        return predicate.test(this.biome) ? Pair.of(blockPos.atY(Mth.clamp(blockPos.getY(), levelReader.getMinY() + 1, levelReader.getMaxY() + 1)), this.biome) : null;
    }

    @Override
    public Set<Holder<Biome>> getBiomesWithin(int n, int n2, int n3, int n4, Climate.Sampler sampler) {
        return Sets.newHashSet(Set.of(this.biome));
    }
}

