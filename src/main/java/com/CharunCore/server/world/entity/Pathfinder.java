package com.CharunCore.server.world.entity;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轻量 A* 寻路：在生物与目标之间的有限网格内寻找绕障路径。
 * 仅作「下一步方向」建议；失败(null)时调用方回退为直线 homing。
 */
public final class Pathfinder {

    private Pathfinder() {}

    // 可通行：空气或非实心方块（水/树叶等视为可穿过）
    private static boolean passable(int state) {
        return state == 0 || !BlockStateHelper.isSolidOpaque(state);
    }

    /** 返回从 (sx,sy,sz) 走向 (tx,ty,tz) 的下一步方块增量 [dx,dy,dz]，无解返回 null。 */
    public static int[] step(DimensionType dim, double sx, double sy, double sz,
                             double tx, double ty, double tz) {
        int bx = (int) Math.floor(sx), by = (int) Math.floor(sy), bz = (int) Math.floor(sz);
        int ex = (int) Math.floor(tx), ey = (int) Math.floor(ty), ez = (int) Math.floor(tz);
        if (Math.abs(ex - bx) > 14 || Math.abs(ey - by) > 8 || Math.abs(ez - bz) > 14) return null;

        // 直线已可达则无需绕路
        if (lineClear(dim, bx, by, bz, ex, ey, ez)) {
            return new int[] { Integer.compare(ex, bx), Integer.compare(ey, by), Integer.compare(ez, bz) };
        }

        int maxNodes = 1500;
        Map<Long, Node> nodes = new HashMap<>();
        List<Node> open = new ArrayList<>();
        Node start = new Node(bx, by, bz, 0, heur(bx, by, bz, ex, ey, ez), 0L);
        nodes.put(start.key, start);
        open.add(start);

        int searched = 0;
        while (!open.isEmpty() && searched < maxNodes) {
            searched++;
            open.sort(Comparator.comparingDouble(n -> n.f));
            Node cur = open.remove(0);
            if (cur.x == ex && cur.y == ey && cur.z == ez) {
                return firstStep(cur, bx, by, bz, ex, ey, ez, nodes);
            }
            for (int[] d : DIRS) {
                int nx = cur.x + d[0], ny = cur.y + d[1], nz = cur.z + d[2];
                int here = WorldManager.getBlockState(dim, nx, ny, nz);
                int head = WorldManager.getBlockState(dim, nx, ny + 1, nz);
                if (!passable(here) || !passable(head)) continue;
                if (d[1] == -1) { // 向下需有落脚实心
                    if (!passable(WorldManager.getBlockState(dim, nx, ny - 1, nz))) continue;
                }
                long nk = key(nx, ny, nz);
                double ng = cur.g + (d[0] != 0 && d[2] != 0 && d[1] == 0 ? 1.414 : 1.0);
                Node exist = nodes.get(nk);
                if (exist == null) {
                    Node nn = new Node(nx, ny, nz, ng, ng + heur(nx, ny, nz, ex, ey, ez), cur.key);
                    nodes.put(nk, nn);
                    open.add(nn);
                } else if (ng < exist.g) {
                    exist.g = ng;
                    exist.f = ng + heur(nx, ny, nz, ex, ey, ez);
                    exist.parent = cur.key;
                }
            }
        }
        return null;
    }

    private static int[] firstStep(Node end, int bx, int by, int bz, int ex, int ey, int ez,
                                   Map<Long, Node> nodes) {
        Node n = end;
        while (n.parent != 0L) {
            Node p = nodes.get(n.parent);
            if (p == null) break;
            if (p.x == bx && p.y == by && p.z == bz) {
                return new int[] { Integer.compare(n.x, p.x), Integer.compare(n.y, p.y), Integer.compare(n.z, p.z) };
            }
            n = p;
        }
        return new int[] { Integer.compare(ex, bx), Integer.compare(ey, by), Integer.compare(ez, bz) };
    }

    private static boolean lineClear(DimensionType dim, int x0, int y0, int z0, int x1, int y1, int z1) {
        int dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0), dz = Math.abs(z1 - z0);
        int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1, sz = z0 < z1 ? 1 : -1;
        int errX = dx - dz;
        int cx = x0, cy = y0, cz = z0;
        while (true) {
            if (!passable(WorldManager.getBlockState(dim, cx, cy, cz))
                    || !passable(WorldManager.getBlockState(dim, cx, cy + 1, cz))) return false;
            if (cx == x1 && cy == y1 && cz == z1) return true;
            int e2 = 2 * errX;
            if (e2 > -dz) { errX -= dz; cx += sx; }
            if (e2 < dx) { errX += dx; cz += sz; }
            if (cy != y1) cy += sy;
        }
    }

    private static double heur(int x, int y, int z, int ex, int ey, int ez) {
        double dx = Math.abs(x - ex), dy = Math.abs(y - ey), dz = Math.abs(z - ez);
        double min = Math.min(dx, dz), max = Math.max(dx, dz);
        return (max - min) + 1.414 * min + dy;
    }

    private static long key(int x, int y, int z) {
        return ((long) (x & 0x1FFFFL) << 42) | ((long) (y & 0x1FFFFL) << 21) | ((long) (z & 0x1FFFFL));
    }

    private static final int[][] DIRS = {
        {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1},
        {1, 0, 1}, {1, 0, -1}, {-1, 0, 1}, {-1, 0, -1},
        {0, 1, 0}, {0, -1, 0}
    };

    private static final class Node {
        final int x, y, z;
        double g, f;
        long parent;
        final long key;
        Node(int x, int y, int z, double g, double f, long parent) {
            this.x = x; this.y = y; this.z = z; this.g = g; this.f = f; this.parent = parent;
            this.key = key(x, y, z);
        }
    }
}
