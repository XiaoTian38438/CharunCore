package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

import java.util.ArrayList;
import java.util.List;

public final class AmethystGeodeGenerator {

    private static final int AIR = 0;

    private final int smoothBasalt, calcite, amethystBlock;
    private final int buddingAmethyst;
    private final int smallBud, mediumBud, largeBud, amethystCluster;
    private final int stone, deepslate, tuff, granite, diorite, andesite, dirt, gravel, sand;

    public AmethystGeodeGenerator() {
        this.smoothBasalt = BlockStateHelper.getDefault("smooth_basalt");
        this.calcite = BlockStateHelper.getDefault("calcite");
        this.amethystBlock = BlockStateHelper.getDefault("amethyst_block");
        this.buddingAmethyst = BlockStateHelper.getDefault("budding_amethyst");
        this.smallBud = BlockStateHelper.getDefault("small_amethyst_bud");
        this.mediumBud = BlockStateHelper.getDefault("medium_amethyst_bud");
        this.largeBud = BlockStateHelper.getDefault("large_amethyst_bud");
        this.amethystCluster = BlockStateHelper.getDefault("amethyst_cluster");
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.tuff = BlockStateHelper.getDefault("tuff");
        this.granite = BlockStateHelper.getDefault("granite");
        this.diorite = BlockStateHelper.getDefault("diorite");
        this.andesite = BlockStateHelper.getDefault("andesite");
        this.dirt = BlockStateHelper.getDefault("dirt");
        this.gravel = BlockStateHelper.getDefault("gravel");
        this.sand = BlockStateHelper.getDefault("sand");
    }

    public void generate(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        // ~1/24 chance per chunk (matching vanilla)
        if (rng.nextInt(24) != 0) return;

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // Geode center within this chunk, deep underground
        int cx = baseX + 4 + rng.nextInt(8);
        int cz = baseZ + 4 + rng.nextInt(8);
        int cy = -50 + rng.nextInt(70); // Y -50 to 19

        // Number of distribution points (3-5)
        int numPoints = 3 + rng.nextInt(3);

        class GeodePoint {
            final int x, y, z;
            final double offset;
            GeodePoint(int x, int y, int z, double offset) {
                this.x = x; this.y = y; this.z = z; this.offset = offset;
            }
        }

        List<GeodePoint> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            int px = cx + (int)(rng.nextGaussian() * 4.0);
            int py = cy + (int)(rng.nextGaussian() * 4.0);
            int pz = cz + (int)(rng.nextGaussian() * 4.0);
            double offset = rng.nextDouble() * 8.0 - 4.0;
            points.add(new GeodePoint(px, py, pz, offset));
        }

        // Bounding box: ±12 blocks from center
        int minBX = Math.max(baseX, cx - 12);
        int maxBX = Math.min(baseX + 15, cx + 12);
        int minBZ = Math.max(baseZ, cz - 12);
        int maxBZ = Math.min(baseZ + 15, cz + 12);
        int minBY = Math.max(-64, cy - 12);
        int maxBY = Math.min(319, cy + 12);

        // Layer thresholds
        double outerThresh = 1.0 / Math.sqrt(6.0);  // outer wall
        double midThresh = 1.0 / Math.sqrt(3.5);     // middle
        double innerThresh = 1.0 / Math.sqrt(2.0);   // inner
        double fillThresh = 1.0 / Math.sqrt(1.3);    // filling (air cavity)

        for (int bx = minBX; bx <= maxBX; bx++) {
            int lx = bx & 15;
            for (int bz = minBZ; bz <= maxBZ; bz++) {
                int lz = bz & 15;
                for (int by = minBY; by <= maxBY; by++) {
                    double value = 0.0;
                    for (GeodePoint pt : points) {
                        double dx = bx - pt.x;
                        double dy = by - pt.y;
                        double dz = bz - pt.z;
                        double distSq = dx * dx + dy * dy + dz * dz;
                        value += 1.0 / Math.sqrt(distSq + pt.offset * pt.offset);
                    }

                    if (value < outerThresh) continue;

                    int cur = chunk.getBlock(lx, by, lz);
                    if (!isReplaceable(cur)) continue;

                    if (value < midThresh) {
                        // Outer layer: smooth basalt
                        chunk.setBlock(lx, by, lz, smoothBasalt);
                    } else if (value < innerThresh) {
                        // Middle layer: calcite
                        chunk.setBlock(lx, by, lz, calcite);
                    } else if (value < fillThresh) {
                        // Inner layer: amethyst block (with some budding amethyst)
                        if (rng.nextDouble() < 0.08) {
                            chunk.setBlock(lx, by, lz, buddingAmethyst);
                        } else {
                            chunk.setBlock(lx, by, lz, amethystBlock);
                        }
                    } else {
                        // Filling: air (cavity)
                        chunk.setBlock(lx, by, lz, AIR);
                    }
                }
            }
        }

        // Place amethyst clusters on inner walls
        for (int bx = minBX; bx <= maxBX; bx++) {
            int lx = bx & 15;
            for (int bz = minBZ; bz <= maxBZ; bz++) {
                int lz = bz & 15;
                for (int by = minBY; by <= maxBY; by++) {
                    if (chunk.getBlock(lx, by, lz) != AIR) continue;

                    // Check if adjacent to amethyst block
                    int[][] dirs = {{0,0,1},{0,0,-1},{1,0,0},{-1,0,0},{0,1,0},{0,-1,0}};
                    boolean hasAmethystNeighbor = false;
                    for (int[] d : dirs) {
                        int nx = (bx + d[0]) & 15;
                        int ny = by + d[1];
                        int nz = (bz + d[2]) & 15;
                        if (ny < -64 || ny > 319) continue;
                        int nb = chunk.getBlock(nx, ny, nz);
                        if (nb == amethystBlock || nb == buddingAmethyst) {
                            hasAmethystNeighbor = true;
                            break;
                        }
                    }

                    if (hasAmethystNeighbor && rng.nextDouble() < 0.15) {
                        // Place cluster/bud on a random face
                        // Simple: place amethyst cluster
                        chunk.setBlock(lx, by, lz, amethystCluster);
                    }
                }
            }
        }
    }

    private boolean isReplaceable(int block) {
        return block == stone || block == deepslate || block == tuff
            || block == granite || block == diorite || block == andesite
            || block == dirt || block == gravel || block == sand || block == AIR;
    }
}
