package com.CharunCore.server;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.light.LightEngine;

public class LightDiagnostic {

    public static void main(String[] args) throws Exception {
        BlockStateHelper.init();
        BlockManager.init();
        LightEngine.init();
        DimensionType dim = DimensionType.OVERWORLD;

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++)
                WorldManager.preGenerateChunk(dim, dx, dz);

        Chunk c = WorldManager.getChunkCached(dim, 0, 0);
        if (c == null) { System.out.println("[光照诊断] 区块生成失败"); return; }
        c.dim = dim;

        long t0 = System.currentTimeMillis();
        c.ensureLight();
        long dt = System.currentTimeMillis() - t0;
        System.out.println("[光照诊断] 初始光照计算耗时: " + dt + "ms");

        int skySections = 0, blockSections = 0;
        for (int i = 0; i < c.getSectionCount(); i++) {
            byte[] s = c.skyLightSections()[i];
            if (s != null) { skySections++; }
            byte[] b = c.blockLightSections()[i];
            if (b != null) blockSections++;
        }
        System.out.println("[光照诊断] 天空光非空 section: " + skySections + "/" + c.getSectionCount()
                + ", 方块光非空 section: " + blockSections);

        int bx = 8, bz = 8;
        int surfaceY = 319;
        for (int y = 319; y >= -64; y--) {
            if (c.getBlock(bx, y, bz) != 0) { surfaceY = y; break; }
        }
        System.out.println("[光照诊断] 列 (8,8) 地表 y=" + surfaceY
                + " (" + BlockStateHelper.getName(c.getBlock(bx, surfaceY, bz)) + ")");
        System.out.println("[光照诊断] 地表上方 sky=" + LightEngine.skyLight(dim, bx, surfaceY + 1, bz)
                + " (期望 15)");
        System.out.println("[光照诊断] 深层 y=-50 sky=" + LightEngine.skyLight(dim, bx, -50, bz)
                + " (期望 0, 除非有洞穴贯通)");
        System.out.println("[光照诊断] y=-60 sky=" + LightEngine.skyLight(dim, bx, -60, bz));

        int lavaCount = 0, litLava = 0;
        for (int x = 0; x < 16 && lavaCount < 20; x++) {
            for (int z = 0; z < 16 && lavaCount < 20; z++) {
                for (int y = -64; y < -50 && lavaCount < 20; y++) {
                    String n = BlockStateHelper.getName(c.getBlock(x, y, z));
                    if (n.equals("lava")) {
                        lavaCount++;
                        if (LightEngine.blockLight(dim, (c.getX() << 4) + x, y, (c.getZ() << 4) + z) == 15) litLava++;
                    }
                }
            }
        }
        System.out.println("[光照诊断] 岩浆样本: " + lavaCount + " 个, 自身方块光=15 的: " + litLava);

        int undergroundAirWithSky = 0;
        for (int x = 0; x < 16; x += 2) {
            for (int z = 0; z < 16; z += 2) {
                for (int y = -40; y < 0; y += 2) {
                    if (c.getBlock(x, y, z) == 0 && LightEngine.skyLight(dim, (c.getX() << 4) + x, y, (c.getZ() << 4) + z) > 0)
                        undergroundAirWithSky++;
                }
            }
        }
        System.out.println("[光照诊断] 地下 y∈[-40,0) 空气且有天空光的位置: " + undergroundAirWithSky + " (洞穴开口渗光)");

        WorldManager.getIoExecutor().shutdown();

        int lx = 4, lz = 4, ly = 75;
        int gx = (c.getX() << 4) + lx, gz = (c.getZ() << 4) + lz;
        System.out.println("[光照诊断] 增量测试列 (" + lx + "," + lz + ") y=70 初始 sky="
                + LightEngine.skyLight(dim, gx, 70, gz) + " (期望 15)");
        WorldManager.setBlock(dim, gx, 75, gz, BlockStateHelper.getDefault("stone"));
        int afterCap = LightEngine.skyLight(dim, gx, 70, gz);
        System.out.println("[光照诊断] y=75 放 1x1 石盖板后, y=70 sky=" + afterCap + " (期望 14: 单格盖板侧面渗光)");
        WorldManager.setBlock(dim, gx, 75, gz, 0);
        int afterRemove = LightEngine.skyLight(dim, gx, 70, gz);
        System.out.println("[光照诊断] 拆掉盖板后, y=70 sky=" + afterRemove + " (期望 15)");
        int torchState = BlockStateHelper.getDefault("torch");
        WorldManager.setBlock(dim, gx, 65, gz, torchState);
        System.out.println("[光照诊断] y=65 插火把: 该格 blockLight="
                + LightEngine.blockLight(dim, gx, 65, gz) + " (期望 14), 邻格="
                + LightEngine.blockLight(dim, gx + 1, 65, gz) + " (期望 13), 下格="
                + LightEngine.blockLight(dim, gx, 64, gz) + " (期望 13)");

        System.out.println("[光照诊断] 完成");
        System.exit(0);
    }
}
