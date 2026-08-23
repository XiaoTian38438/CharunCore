package com.CharunCore.server.worldgen.density;

import com.CharunCore.server.worldgen.density.DensityFunction.FunctionContext;
import com.CharunCore.server.worldgen.density.DensityFunction.SinglePointContext;

/**
 * Stage 0G-1 验证：建立 NoiseRouterData.overworld() 后能否无 NPE 运行 finalDensity，
 * 输出 (0, y, 0) 处的高度密度曲线。
 * 验证通过后下一步是 NoiseChunk 插补层（关键以匹配原版 byte-identical）。
 */
public final class DensityLinkSmokeTest {
    public static void main(String[] args) {
        System.out.println("[DensityLink] 开始构建 overworld NoiseRouter...");
        long t0 = System.currentTimeMillis();
        java.util.Map<String, DensityFunction> map = NoiseRouterData.bootstrap();
        NoiseRouter router = NoiseRouterData.overworld(map, false, false);
        long t1 = System.currentTimeMillis();
        System.out.println("[DensityLink] 构建 OK，耗时 " + (t1 - t0) + "ms");
        System.out.println("[DensityLink] 已注册密度函数: " + map.size() + " 个");

        DensityFunction finalDensity = router.finalDensity();
        System.out.println("[DensityLink] finalDensity min=" + finalDensity.minValue() + " max=" + finalDensity.maxValue());

        System.out.println("[DensityLink] 高度密度曲线 (x=0, z=0):");
        for (int y = -64; y <= 320; y += 16) {
            FunctionContext ctx = new SinglePointContext(0, y, 0);
            double d;
            try {
                d = finalDensity.compute(ctx);
            } catch (Throwable ex) {
                System.out.println("  y=" + y + "  ERROR: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
                continue;
            }
            String bar = bar(d, 60);
            System.out.printf("  y=%4d  d=%+9.4f  %s%n", y, d, bar);
        }

        // 也输出 (16, 64, 16) 处的值
        System.out.println("[DensityLink] (16, 64, 16) 处: d=" + finalDensity.compute(new SinglePointContext(16, 64, 16)));
        System.out.println("[DensityLink] (100, 64, 100) 处: d=" + finalDensity.compute(new SinglePointContext(100, 64, 100)));
        System.out.println("[DensityLink] DONE");
    }

    private static String bar(double v, int width) {
        int mid = width / 2;
        char[] arr = new char[width];
        java.util.Arrays.fill(arr, ' ');
        arr[mid] = '|';
        int pos;
        if (v >= 0) {
            pos = mid + (int) Math.min(mid, Math.round(v * mid / 1.0));
            for (int i = mid + 1; i <= pos && i < width; i++) arr[i] = '#';
        } else {
            pos = mid - (int) Math.min(mid, Math.round(-v * mid / 1.0));
            for (int i = mid - 1; i >= pos && i >= 0; i--) arr[i] = '#';
        }
        return new String(arr);
    }

    private DensityLinkSmokeTest() {}
}
