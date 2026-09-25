package com.CharunCore.server.world;

import java.util.*;

/**
 * Bug29 验证（模型级）: 复刻 FluidEngine.processFluid 的"铺水 + 滋养判定"核心逻辑，
 * 在内存网格上跑 N tick，观察水流前缘是否稳定。
 *
 * 复刻要点（与真实 FluidEngine 一致）:
 *  - 水源 level=0; 流动水 level 1..maxLevel(水=7); 下落水 level=8。
 *  - 向下优先铺(垂直), 随后水平铺 level+1。
 *  - 流动水(level>0) 每刻校验滋养; 无滋养则变空气(干涸)。
 *  - 滋养判定 hasFluidFeed: 6 邻居中是否存在"更低 level / 水源"——这正是 Bug29 的根因点。
 *
 * 通过 FIXED 开关切换"修复版(允许水平更低 level 滋养)"与"旧版(水平只认水源)"，
 * 证明旧版会在 level>=2 的水平流上反复"铺出→干涸"产生抖动, 修复版稳定。
 */
public final class FluidSimDiagnostic {
    static final boolean FIXED = !"old".equals(System.getProperty("fluidmode")); // -Dfluidmode=old 切换旧版
    static final int MAX_LEVEL = 7;               // 水
    static final int SIZE = 24;                   // 网格半径(仅算 x,z, y 固定一层)
    static final int Y = 64;
    // grid[x][z] = level; -1 = 空气, 0 = 水源, 1..7 = 流动, 8 = 下落
    static int[][] grid = new int[SIZE * 2][SIZE * 2];
    static final int O = SIZE;                    // 原点偏移

    static int get(int x, int z) {
        if (x < 0 || z < 0 || x >= SIZE * 2 || z >= SIZE * 2) return -1;
        return grid[x][z];
    }
    static void set(int x, int z, int v) {
        if (x < 0 || z < 0 || x >= SIZE * 2 || z >= SIZE * 2) return; // 越界忽略
        grid[x][z] = v;
    }

    static boolean isWater(int v) { return v >= 0; } // 空气=-1, 其余都是水态

    static int getB(int x, int z) {
        if (x < 0 || z < 0 || x >= SIZE * 2 || z >= SIZE * 2) return -1; // 越界当作空气(不可再流)
        return grid[x][z];
    }

    /** 复刻 hasFluidFeed：this block level=L, 检查 4 水平 + 上 + 下 邻居。 */
    static boolean hasFluidFeed(int x, int z, int level) {
        int[][] dirs = {{1,0},{ -1,0},{0,1},{0,-1},{0,-1 /*up y+1*/},{0,1 /*down y-1*/}};
        // 用 dy 区分上/下: dirs[4]=上(dy=-1 在世界坐标, 这里仅语义), dirs[5]=下
        int[][] n = {
            {x+1,z,0},{x-1,z,0},{x,z+1,0},{x,z-1,0},
            {x,z,-1},{x,z,1} // [4]=上, [5]=下 (dy)
        };
        for (int i = 0; i < 6; i++) {
            int nx = n[i][0], nz = n[i][1], dy = n[i][2];
            int ns = getB(nx, nz);
            if (ns < 0) continue;
            int nl = ns;
            if (nl == 0) return true;                 // 水源
            if (nl == 8 && dy == -1) return true;     // 上方下落水(瀑布列)
            if (dy == -1 && nl < level) return true;  // 上方更低 level
            if (dy == 1 && nl == 0) return true;      // 下方水源
            // 水平更低 level 滋养 —— 修复版才允许
            if (dy == 0 && nl < level && FIXED) return true;
        }
        return false;
    }

    /** 复刻 processFluid 单层水平铺水 + 干涸逻辑（垂直下落简化为同格 level=8 不在此模型展开）。 */
    static void process(int x, int z) {
        int cur = getB(x, z);
        if (cur < 0) return;
        int level = cur;
        if (level > 0) {
            if (!hasFluidFeed(x, z, level)) { set(x, z, -1); return; }
        }
        // 水平铺: 水源/下落水铺 level=1; 流动水铺 level+1 (<=MAX_LEVEL)
        if (level < MAX_LEVEL || level == 8) {
            int next = (level == 0 || level == 8) ? 1 : level + 1;
            int[][] h = {{x+1,z},{x-1,z},{x,z+1},{x,z-1}};
            for (int[] nb : h) {
                int t = getB(nb[0], nb[1]);
                if (t < 0) { // 空气可流入
                    set(nb[0], nb[1], next);
                }
            }
        }
    }

    public static void main(String[] args) {
        // 初始化整个网格为空气(-1), 仅原点放一个水源
        for (int x = 0; x < SIZE * 2; x++)
            for (int z = 0; z < SIZE * 2; z++)
                grid[x][z] = -1;
        set(O, O, 0);

        int ticks = 60;
        int prevFront = -1;
        java.util.Deque<Integer> recent = new ArrayDeque<>();
        boolean oscillated = false;
        for (int t = 0; t < ticks; t++) {
            // 收集当前所有水格快照并统一处理(模拟一 tick 内全部待处理)
            List<int[]> cells = new ArrayList<>();
            for (int x = 0; x < SIZE * 2; x++)
                for (int z = 0; z < SIZE * 2; z++)
                    if (getB(x, z) >= 0) cells.add(new int[]{x, z});
            for (int[] c : cells) process(c[0], c[1]);

            // 统计最远水格(前缘半径) 与 水格总数
            int front = 0;
            int wc = 0;
            for (int x = 0; x < SIZE * 2; x++)
                for (int z = 0; z < SIZE * 2; z++)
                    if (getB(x, z) >= 0) { front = Math.max(front, Math.abs(x - O) + Math.abs(z - O)); wc++; }
            if (t > 15) {
                // 稳定后 waterCells 应保持不变; 若近 10 tick 内反复变化 -> 抖动
                recent.addLast(wc);
                if (recent.size() > 10) recent.removeFirst();
                boolean allSame = true;
                for (int v : recent) if (v != wc) allSame = false;
                if (!allSame) oscillated = true;
            }
            prevFront = front;
            if (t < 12 || t == ticks - 1) {
                System.out.printf("tick %2d: frontRadius=%d  waterCells=%d%n",
                    t, front, wc);
            }
        }
        System.out.println("=== mode=" + (FIXED ? "FIXED" : "OLD") + " ===");
        System.out.println("final water cells: " + countWater());
        System.out.println(oscillated ? "RESULT: OSCILLATION detected (抖动)" : "RESULT: stable (稳定)");
    }

    static int countWater() {
        int c = 0;
        for (int x = 0; x < SIZE * 2; x++)
            for (int z = 0; z < SIZE * 2; z++)
                if (get(x, z) >= 0) c++;
        return c;
    }
}
