package com.CharunCore.server.worldgen;

import java.util.Map;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.OverworldChunkGenerator;
import com.CharunCore.server.worldgen.density.DensityFunction;
import com.CharunCore.server.worldgen.density.NoiseRouter;
import com.CharunCore.server.worldgen.density.NoiseRouterData;
import com.CharunCore.server.worldgen.density.DensityFunction.NoiseHolder;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

/**
 * Debug: compare old and new generators at same position.
 * Key insight: if slopedCheese is always > 1.5625, cave system is always active.
 */
public final class DensityDebugTest {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;
        int bx = 8 * 16 + 8, bz = 8 * 16 + 8; // (136, 136)

        // Old generator (compare by generating full chunk)
        OverworldChunkGenerator oldGen = new OverworldChunkGenerator(seed);
        Chunk oldChunk = oldGen.generate(8, 8);

        // New router
        NoiseHolder.setWorldSeed(seed, 0L);
        Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);
        DensityFunction finalDensity = router.finalDensity();
        DensityFunction continents = router.continents();
        DensityFunction erosion = router.erosion();
        DensityFunction ridges = router.ridges();
        DensityFunction depth = router.depth();

        // New generator chunk
        DensityRouterChunkGenerator newGen = new DensityRouterChunkGenerator(seed);
        Chunk newChunk = newGen.generate(8, 8);

        // Also compute the "slopedCheese" equivalent manually via router components
        // We can't get it directly, but we can compute depth and compare

        System.out.println("=== Column (8,8) in chunk (8,8) ===");
        System.out.println("y\toldBlk\tnewBlk\tdirectFinal\trouterDepth\trouterCont\trouterErosion\troutRidges");
        System.out.println("---\t------\t------\t-----------\t-----------\t-----------\t------------\t-----------");

        for (int y = -64; y < 100; y++) {
            int ob = oldChunk.getBlock(8, y, 8);
            int nb = newChunk.getBlock(8, y, 8);

            double df = finalDensity.compute(new SinglePointContext(bx, y, bz));
            double d = depth.compute(new SinglePointContext(bx, y, bz));

            // Continents, erosion, ridges are 2D (XZ only) — cache by Y
            double c = continents.compute(new SinglePointContext(bx, y, bz));
            double e = erosion.compute(new SinglePointContext(bx, y, bz));
            double r = ridges.compute(new SinglePointContext(bx, y, bz));

            System.out.printf("%d\t%s\t%s\t%.6f\t%.6f\t%.4f\t%.4f\t%.4f%n",
                y, ob == 0 ? "a" : "S", nb == 0 ? "a" : "S",
                df, d, c, e, r);
        }

        // Also compute from old gen for comparison
        System.out.println("\n=== Old generator manual computeFinalDensity (via reflection-free copy) ===");
        System.out.println("We'll compute the old formula directly:");
        // We can't access private methods, so let's just report the chunk block diff
        int mismatches = 0;
        System.out.println("\nBlock mismatch at y where new=air, old=solid:");
        for (int y = -64; y < 320; y++) {
            int ob = oldChunk.getBlock(8, y, 8);
            int nb = newChunk.getBlock(8, y, 8);
            if (ob != nb) {
                String on = ob == 0 ? "air" : "block";
                String nn = nb == 0 ? "air" : "block";
                if (mismatches < 20) {
                    System.out.printf("  y=%d: old=%s, new=%s%n", y, on, nn);
                }
                mismatches++;
            }
        }
        System.out.println("Total mismatches in column: " + mismatches + " / 384");
    }
}
