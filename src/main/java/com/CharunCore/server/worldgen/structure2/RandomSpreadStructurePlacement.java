package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;

public class RandomSpreadStructurePlacement {
    private final int spacing;
    private final int separation;
    private final int salt;

    public RandomSpreadStructurePlacement(int spacing, int separation, int salt) {
        this.spacing = spacing;
        this.separation = separation;
        this.salt = salt;
    }

    public int getSpacing() { return spacing; }
    public int getSeparation() { return separation; }
    public int getSalt() { return salt; }

    public boolean isStructureChunk(long levelSeed, int chunkX, int chunkZ) {
        int gridX = StructurePlacementMath.floorDiv(chunkX, spacing);
        int gridZ = StructurePlacementMath.floorDiv(chunkZ, spacing);
        RandomSource rng = StructurePlacementMath.withLargeFeatureWithSalt(levelSeed, gridX, gridZ, salt);
        int range = spacing - separation;
        int offsetX = rng.nextInt(range);
        int offsetZ = rng.nextInt(range);
        int candidateX = gridX * spacing + offsetX;
        int candidateZ = gridZ * spacing + offsetZ;
        return chunkX == candidateX && chunkZ == candidateZ;
    }

    public int[] getPotentialStructureChunk(long levelSeed, int chunkX, int chunkZ) {
        int gridX = StructurePlacementMath.floorDiv(chunkX, spacing);
        int gridZ = StructurePlacementMath.floorDiv(chunkZ, spacing);
        RandomSource rng = StructurePlacementMath.withLargeFeatureWithSalt(levelSeed, gridX, gridZ, salt);
        int range = spacing - separation;
        int offsetX = rng.nextInt(range);
        int offsetZ = rng.nextInt(range);
        return new int[]{gridX * spacing + offsetX, gridZ * spacing + offsetZ};
    }
}
