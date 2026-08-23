package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;

public final class MonsterRoomGenerator {

    private static final int AIR = 0;

    private final int cobblestone, mossyCobblestone;
    private final int spawner;
    private final int stone, deepslate, tuff, granite, diorite, andesite, dirt, gravel;

    public MonsterRoomGenerator() {
        this.cobblestone = BlockStateHelper.getDefault("cobblestone");
        this.mossyCobblestone = BlockStateHelper.getDefault("mossy_cobblestone");
        this.spawner = BlockStateHelper.getDefault("spawner");
        this.stone = BlockStateHelper.getDefault("stone");
        this.deepslate = BlockStateHelper.getDefault("deepslate");
        this.tuff = BlockStateHelper.getDefault("tuff");
        this.granite = BlockStateHelper.getDefault("granite");
        this.diorite = BlockStateHelper.getDefault("diorite");
        this.andesite = BlockStateHelper.getDefault("andesite");
        this.dirt = BlockStateHelper.getDefault("dirt");
        this.gravel = BlockStateHelper.getDefault("gravel");
    }

    public void generate(Chunk chunk, int chunkX, int chunkZ, RandomSource rng) {
        // 2 attempts per chunk
        for (int attempt = 0; attempt < 2; attempt++) {
            int lx = 1 + rng.nextInt(14);
            int lz = 1 + rng.nextInt(14);
            int y = -40 + rng.nextInt(60); // Y -40 to 19

            int w = 2 + rng.nextInt(2); // 2-3 (radius in X)
            int d = 2 + rng.nextInt(2); // 2-3 (radius in Z)
            int hTop = 4;
            int hBot = -1;

            if (tryPlaceRoom(chunk, lx, y, lz, w, d, hTop, hBot, rng)) {
                return; // one room per chunk max
            }
        }
    }

    private boolean tryPlaceRoom(Chunk chunk, int cx, int cy, int cz,
                                  int w, int d, int hTop, int hBot, RandomSource rng) {
        int minX = cx - w;
        int maxX = cx + w;
        int minZ = cz - d;
        int maxZ = cz + d;
        int minY = cy + hBot;
        int maxY = cy + hTop;

        // Check bounds
        if (minX < 1 || maxX > 14 || minZ < 1 || maxZ > 14) return false;
        if (minY < -64 || maxY > 319) return false;

        // Verify floor (minY-1) and ceiling (maxY+1) are solid
        // and count valid wall positions
        int validWalls = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!isSolid(chunk.getBlock(x, minY - 1, z))) return false;
                if (!isSolid(chunk.getBlock(x, maxY + 1, z))) return false;
            }
        }
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if ((x == minX || x == maxX || z == minZ || z == maxZ) && y == 0) {
                        if (chunk.getBlock(x, y, z) == AIR && chunk.getBlock(x, y + 1, z) == AIR) {
                            validWalls++;
                        }
                    }
                }
            }
        }

        if (validWalls < 1 || validWalls > 5) return false;

        // Build room
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean onWall = x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ;
                    if (onWall) {
                        if (isReplaceable(chunk.getBlock(x, y, z))) {
                            if (y == minY && rng.nextInt(4) != 0) {
                                chunk.setBlock(x, y, z, mossyCobblestone);
                            } else {
                                chunk.setBlock(x, y, z, cobblestone);
                            }
                        }
                    } else {
                        chunk.setBlock(x, y, z, AIR);
                    }
                }
            }
        }

        // Place spawner at center
        chunk.setBlock(cx, cy, cz, spawner);

        // Place chests (1-2 on walls)
        int chests = 0;
        int maxChests = 1 + rng.nextInt(2);
        for (int i = 0; i < maxChests * 3 && chests < maxChests; i++) {
            int side = rng.nextInt(4);
            int sx = side == 0 ? minX : (side == 1 ? maxX : cx);
            int sz = side == 2 ? minZ : (side == 3 ? maxZ : cz);
            if (side < 2) sx = (side == 0 ? minX : maxX);
            else sz = (side == 2 ? minZ : maxZ);
            int sy = cy + rng.nextInt(hTop - hBot + 1) - 1;
            if (sx < minX || sx > maxX || sz < minZ || sz > maxZ) continue;
            int lx2 = sx;
            int lz2 = sz;
            if (chunk.getBlock(lx2, sy, lz2) == AIR) {
                int solidFaces = 0;
                for (int ddx = -1; ddx <= 1; ddx++) {
                    for (int ddz = -1; ddz <= 1; ddz++) {
                        if (ddx == 0 && ddz == 0) continue;
                        int nx = lx2 + ddx;
                        int nz = lz2 + ddz;
                        if (nx < 0 || nx > 15 || nz < 0 || nz > 15) continue;
                        if (isSolid(chunk.getBlock(nx, sy, nz))) solidFaces++;
                    }
                }
                if (solidFaces > 0) {
                    chunk.setBlock(lx2, sy, lz2, BlockStateHelper.getDefault("chest"));
                    chests++;
                }
            }
        }

        return true;
    }

    private boolean isSolid(int block) {
        return block != AIR && block != spawner
            && block != BlockStateHelper.getDefault("chest");
    }

    private boolean isReplaceable(int block) {
        return block == stone || block == deepslate || block == tuff
            || block == granite || block == diorite || block == andesite
            || block == dirt || block == gravel;
    }
}
