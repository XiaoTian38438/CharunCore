package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.Collections;
import java.util.List;

public abstract class StructurePoolElement {
    protected final Projection projection;

    public static final StructurePoolElement EMPTY_SINGLETON = new StructurePoolElement(Projection.RIGID) {
        @Override
        public List<JigsawBlockInfo> getShuffledJigsawBlocks(
                StructureTemplateManager manager, int posX, int posY, int posZ,
                Rotation rotation, RandomSource random) {
            return Collections.emptyList();
        }

        @Override
        public BoundingBox getBoundingBox(StructureTemplateManager manager,
                int posX, int posY, int posZ, Rotation rotation) {
            return new BoundingBox(posX, posY, posZ, posX, posY, posZ);
        }

        @Override
        public void place(WorldGenLevel level, StructureTemplateManager manager,
                int posX, int posY, int posZ, Rotation rotation, Mirror mirror) {
        }

        @Override
        public int getGroundLevelDelta() { return 0; }
    };

    protected StructurePoolElement(Projection projection) {
        this.projection = projection;
    }

    public Projection getProjection() { return projection; }

    public abstract List<JigsawBlockInfo> getShuffledJigsawBlocks(
        StructureTemplateManager manager, int posX, int posY, int posZ,
        Rotation rotation, RandomSource random);

    public abstract BoundingBox getBoundingBox(StructureTemplateManager manager,
        int posX, int posY, int posZ, Rotation rotation);

    public abstract void place(WorldGenLevel level, StructureTemplateManager manager,
        int posX, int posY, int posZ, Rotation rotation, Mirror mirror);

    public int getGroundLevelDelta() { return 1; }
}
