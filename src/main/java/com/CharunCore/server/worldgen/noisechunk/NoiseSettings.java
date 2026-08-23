package com.CharunCore.server.worldgen.noisechunk;

/**
 * 原版 net.minecraft.world.level.levelgen.NoiseSettings — record(minY, height, noiseSizeHorizontal, noiseSizeVertical)。
 * cellWidth = noiseSizeHorizontal * 4, cellHeight = noiseSizeVertical * 4。
 * Overworld 默认: create(-64, 384, 1, 2) -> cellWidth=4, cellHeight=8。
 */
public record NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {

    public static final NoiseSettings OVERWORLD = create(-64, 384, 1, 2);
    public static final NoiseSettings NETHER = create(0, 128, 1, 2);
    public static final NoiseSettings END = create(0, 128, 2, 1);

    public static NoiseSettings create(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
        return new NoiseSettings(minY, height, noiseSizeHorizontal, noiseSizeVertical);
    }

    public int getCellWidth() {
        return QuartPos.toBlock(noiseSizeHorizontal);
    }

    public int getCellHeight() {
        return QuartPos.toBlock(noiseSizeVertical);
    }
}
