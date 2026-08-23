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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

public class GravityProcessor
extends StructureProcessor {
    public static final MapCodec<GravityProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Heightmap.Types.CODEC.fieldOf("heightmap")).orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter(gravityProcessor -> gravityProcessor.heightmap), ((MapCodec)Codec.INT.fieldOf("offset")).orElse(0).forGetter(gravityProcessor -> gravityProcessor.offset)).apply((Applicative<GravityProcessor, ?>)instance, GravityProcessor::new));
    private final Heightmap.Types heightmap;
    private final int offset;

    public GravityProcessor(Heightmap.Types types, int n) {
        this.heightmap = types;
        this.offset = n;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings) {
        Heightmap.Types types = levelReader instanceof ServerLevel ? (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG ? Heightmap.Types.WORLD_SURFACE : (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG ? Heightmap.Types.OCEAN_FLOOR : this.heightmap)) : this.heightmap;
        BlockPos blockPos3 = structureBlockInfo2.pos();
        int n = levelReader.getHeight(types, blockPos3.getX(), blockPos3.getZ()) + this.offset;
        int n2 = structureBlockInfo.pos().getY();
        return new StructureTemplate.StructureBlockInfo(new BlockPos(blockPos3.getX(), n + n2, blockPos3.getZ()), structureBlockInfo2.state(), structureBlockInfo2.nbt());
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.GRAVITY;
    }
}

