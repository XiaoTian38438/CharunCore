/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

public class BlockRotProcessor
extends StructureProcessor {
    public static final MapCodec<BlockRotProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("rottable_blocks").forGetter(blockRotProcessor -> blockRotProcessor.rottableBlocks), ((MapCodec)Codec.floatRange(0.0f, 1.0f).fieldOf("integrity")).forGetter(blockRotProcessor -> Float.valueOf(blockRotProcessor.integrity))).apply((Applicative<BlockRotProcessor, ?>)instance, BlockRotProcessor::new));
    private final Optional<HolderSet<Block>> rottableBlocks;
    private final float integrity;

    public BlockRotProcessor(HolderSet<Block> holderSet, float f) {
        this(Optional.of(holderSet), f);
    }

    public BlockRotProcessor(float f) {
        this(Optional.empty(), f);
    }

    private BlockRotProcessor(Optional<HolderSet<Block>> optional, float f) {
        this.integrity = f;
        this.rottableBlocks = optional;
    }

    @Override
    public  @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings) {
        RandomSource randomSource = structurePlaceSettings.getRandom(structureBlockInfo2.pos());
        if (this.rottableBlocks.isPresent() && !structureBlockInfo.state().is(this.rottableBlocks.get()) || randomSource.nextFloat() <= this.integrity) {
            return structureBlockInfo2;
        }
        return null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_ROT;
    }
}

