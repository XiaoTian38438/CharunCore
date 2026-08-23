/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.biome;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public class CheckerboardColumnBiomeSource
extends BiomeSource {
    public static final MapCodec<CheckerboardColumnBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Biome.LIST_CODEC.fieldOf("biomes")).forGetter(checkerboardColumnBiomeSource -> checkerboardColumnBiomeSource.allowedBiomes), ((MapCodec)Codec.intRange(0, 62).fieldOf("scale")).orElse(2).forGetter(checkerboardColumnBiomeSource -> checkerboardColumnBiomeSource.size)).apply((Applicative<CheckerboardColumnBiomeSource, ?>)instance, CheckerboardColumnBiomeSource::new));
    private final HolderSet<Biome> allowedBiomes;
    private final int bitShift;
    private final int size;

    public CheckerboardColumnBiomeSource(HolderSet<Biome> holderSet, int n) {
        this.allowedBiomes = holderSet;
        this.bitShift = n + 2;
        this.size = n;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.allowedBiomes.stream();
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int n, int n2, int n3, Climate.Sampler sampler) {
        return this.allowedBiomes.get(Math.floorMod((n >> this.bitShift) + (n3 >> this.bitShift), this.allowedBiomes.size()));
    }
}

