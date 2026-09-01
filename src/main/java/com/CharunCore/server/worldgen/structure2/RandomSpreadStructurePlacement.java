package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.RandomSource;

/**
 * 原版 RandomSpreadStructurePlacement + StructurePlacement.FrequencyReductionMethod 移植：
 * 候选 = 网格内 (spacing-separation) 范围取点（linear 一次 nextInt / triangular 两次取平均），
 * frequency<1 时按 reduction_method 决定是否生成，exclusion_zone 排除其它结构附近。
 */
public class RandomSpreadStructurePlacement implements StructurePlacementLike {

    public static final int LINEAR = 0;
    public static final int TRIANGULAR = 1;

    private static final int HIGHLY_ARBITRARY_RANDOM_SALT = 10387320;

    private final int spacing;
    private final int separation;
    private final int salt;
    private final int spreadType;
    private final float frequency;
    private final String frequencyMethod;
    private String exclusionOtherSet;
    private int exclusionChunkCount;

    public RandomSpreadStructurePlacement(int spacing, int separation, int salt) {
        this(spacing, separation, salt, LINEAR);
    }

    public RandomSpreadStructurePlacement(int spacing, int separation, int salt, int spreadType) {
        this.spacing = spacing;
        this.separation = separation;
        this.salt = salt;
        this.spreadType = spreadType;
        this.frequency = 1.0f;
        this.frequencyMethod = "default";
    }

    public RandomSpreadStructurePlacement(int spacing, int separation, int salt, int spreadType,
                                          float frequency, String frequencyMethod) {
        this.spacing = spacing;
        this.separation = separation;
        this.salt = salt;
        this.spreadType = spreadType;
        this.frequency = frequency;
        this.frequencyMethod = frequencyMethod;
    }

    public void setExclusionZone(String otherSetId, int chunkCount) {
        this.exclusionOtherSet = otherSetId;
        this.exclusionChunkCount = chunkCount;
    }

    public int getSpacing() { return spacing; }
    public int getSeparation() { return separation; }
    public int getSalt() { return salt; }
    public int getSpreadType() { return spreadType; }

    /** 原版 RandomSpreadType.evaluate。 */
    private int evaluate(RandomSource rng, int range) {
        if (spreadType == TRIANGULAR) return (rng.nextInt(range) + rng.nextInt(range)) / 2;
        return rng.nextInt(range);
    }

    public int[] getPotentialStructureChunk(long levelSeed, int chunkX, int chunkZ) {
        int gridX = StructurePlacementMath.floorDiv(chunkX, spacing);
        int gridZ = StructurePlacementMath.floorDiv(chunkZ, spacing);
        RandomSource rng = StructurePlacementMath.withLargeFeatureWithSalt(levelSeed, gridX, gridZ, salt);
        int range = spacing - separation;
        int offsetX = evaluate(rng, range);
        int offsetZ = evaluate(rng, range);
        return new int[]{gridX * spacing + offsetX, gridZ * spacing + offsetZ};
    }

    @Override
    public boolean isStructureChunk(long levelSeed, int chunkX, int chunkZ) {
        int[] candidate = getPotentialStructureChunk(levelSeed, chunkX, chunkZ);
        if (candidate[0] != chunkX || candidate[1] != chunkZ) return false;
        // 原版 applyAdditionalChunkRestrictions
        if (frequency < 1.0f && !shouldGenerateFrequency(levelSeed, chunkX, chunkZ)) return false;
        // 原版 applyInteractionsWithOtherStructures（exclusion_zone）
        if (exclusionOtherSet != null && isExclusionForbidden(levelSeed, chunkX, chunkZ)) return false;
        return true;
    }

    private boolean shouldGenerateFrequency(long seed, int x, int z) {
        LegacyRandomSource rng = new LegacyRandomSource(0L);
        switch (frequencyMethod) {
            case "legacy_type_1": {
                int rx = x >> 4, rz = z >> 4;
                rng.setSeed((long) (rx ^ rz << 4) ^ seed);
                rng.nextInt();
                return rng.nextInt((int) (1.0f / frequency)) == 0;
            }
            case "legacy_type_2": {
                long l2 = (long) x * 341873128712L + (long) z * 132897987541L + seed + HIGHLY_ARBITRARY_RANDOM_SALT;
                rng.setSeed(l2);
                return rng.nextFloat() < frequency;
            }
            case "legacy_type_3": {
                LegacyRandomSource r2 = new LegacyRandomSource(seed);
                long l2 = r2.nextLong();
                long l3 = r2.nextLong();
                rng.setSeed((long) x * l2 ^ (long) z * l3 ^ seed);
                return rng.nextDouble() < (double) frequency;
            }
            default: {
                long l2 = (long) x * 341873128712L + (long) z * 132897987541L + seed + (long) salt;
                rng.setSeed(l2);
                return rng.nextFloat() < frequency;
            }
        }
    }

    private boolean isExclusionForbidden(long seed, int cx, int cz) {
        StructureSet other = StructureSet.get(exclusionOtherSet);
        if (other == null || other.getPlacement() == null) return false;
        RandomSpreadStructurePlacement op = other.getPlacement();
        for (int dx = -exclusionChunkCount; dx <= exclusionChunkCount; dx++) {
            for (int dz = -exclusionChunkCount; dz <= exclusionChunkCount; dz++) {
                if (op.isCandidateChunkOnly(seed, cx + dx, cz + dz)) return true;
            }
        }
        return false;
    }

    /** 只判断网格候选（不含频率/排除），供排除区扫描使用。 */
    boolean isCandidateChunkOnly(long levelSeed, int chunkX, int chunkZ) {
        int[] candidate = getPotentialStructureChunk(levelSeed, chunkX, chunkZ);
        return candidate[0] == chunkX && candidate[1] == chunkZ;
    }
}
