package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原版 EndCityPieces.startHouseTower / SectionGenerator 四件套逐行移植。
 * 连接数学：childOrigin(纯向量空间) = parentOrigin + pureRotate(offset, parentRot)，
 * 与原版 calculateConnectedPosition(parentSettings, jigsawPos, childSettings, ZERO) 等价
 * （end_city 模板的入接拼图块位于本地原点）。构建时把纯向量原点换算成项目尺寸相对
 * 旋转所需的角落偏移后交给 StructureTemplate.placeInWorld（OW=false 的楼层跳过空气）。
 */
public final class EndCityPieces {

    private static final Map<String, StructureTemplate> TEMPLATE_CACHE = new HashMap<>();

    public static final class CityPiece implements BuildablePiece {
        final String templateName;
        int ox, oy, oz;
        final Rotation rot;
        final boolean overwriteAir;
        int genDepth;

        CityPiece(String name, int x, int y, int z, Rotation rot, boolean overwriteAir, int genDepth) {
            this.templateName = name;
            this.ox = x; this.oy = y; this.oz = z;
            this.rot = rot;
            this.overwriteAir = overwriteAir;
            this.genDepth = genDepth;
        }

        @Override public BoundingBox box() {
            StructureTemplate t = template(templateName);
            int ax = 0, az = 0, bx = 0, bz = 0;
            switch (rot) {
                case CLOCKWISE_90 -> { ax = -(t.sizeZ - 1); bz = t.sizeX - 1; }
                case CLOCKWISE_180 -> { ax = -(t.sizeX - 1); az = -(t.sizeZ - 1); bx = t.sizeX - 1; bz = t.sizeZ - 1; }
                case COUNTERCLOCKWISE_90 -> { az = -(t.sizeX - 1); bx = t.sizeZ - 1; }
                default -> { bx = t.sizeX - 1; bz = t.sizeZ - 1; }
            }
            return new BoundingBox(ox + Math.min(ax, bx), oy, oz + Math.min(az, bz),
                ox + Math.max(ax, bx), oy + t.sizeY - 1, oz + Math.max(az, bz));
        }

        @Override public void build(WorldGenLevel level) {
            StructureTemplate t = template(templateName);
            if (t == null) return;
            // Bug27: srShift 返回 2 元素 [dx,dz] —— 曾读 shift[2] 必抛 AIOOBE 且把 dz 误加到 Y,
            // 与末地城 start 相交的区块全部生成失败(空洞)。与静态 placeInChunk 同款正确用法。
            int[] shift = srShift(t, rot);
            t.placeInWorld(level, ox + shift[0], oy, oz + shift[1],
                rot, Mirror.NONE, !overwriteAir);
        }
    }

    private static StructureTemplate template(String name) {
        return TEMPLATE_CACHE.computeIfAbsent(name,
            n -> StructureTemplate.load("end_city/" + n));
    }

    /** 原版纯向量旋转（pivot=ZERO）：CW90=(-z,x)，CW180=(-x,-z)，CCW90=(z,-x)。 */
    private static int[] pureRotate(int x, int z, Rotation rot) {
        return switch (rot) {
            case CLOCKWISE_90 -> new int[]{-z, x};
            case CLOCKWISE_180 -> new int[]{-x, -z};
            case COUNTERCLOCKWISE_90 -> new int[]{z, -x};
            default -> new int[]{x, z};
        };
    }

    private final RandomSource rnd;
    private final List<CityPiece> pieces = new ArrayList<>();
    private boolean shipCreated;

    private EndCityPieces(RandomSource rnd) { this.rnd = rnd; }

    /** 在候选列表 out 中追加子件：原点 = 父原点 + pureRotate(offset, 父旋转)。 */
    private CityPiece add(List<CityPiece> out, CityPiece parent, int dx, int dy, int dz,
                          String name, Rotation rot, boolean ow) {
        int[] v = pureRotate(dx, dz, parent.rot);
        CityPiece piece = new CityPiece(name,
            parent.ox + v[0], parent.oy + dy, parent.oz + v[1],
            rot, ow, parent.genDepth);
        out.add(piece);
        return piece;
    }

    private interface Section {
        boolean generate(EndCityPieces city, int depth, CityPiece parent, int[] offset, List<CityPiece> out);
    }

    private static final Section TOWER = new Section() {
        @Override public boolean generate(EndCityPieces city, int depth, CityPiece parent,
                                          int[] offset, List<CityPiece> out) {
            Rotation rot = parent.rot;
            CityPiece p = city.add(out, parent, 3 + city.rnd.nextInt(2), -3, 3 + city.rnd.nextInt(2),
                "tower_base", rot, true);
            p = city.add(out, p, 0, 7, 0, "tower_piece", rot, true);
            CityPiece keep = city.rnd.nextInt(3) == 0 ? p : null;
            int n = 1 + city.rnd.nextInt(3);
            for (int b = 0; b < n; b++) {
                p = city.add(out, p, 0, 4, 0, "tower_piece", rot, true);
                if (b < n - 1 && city.rnd.nextBoolean()) keep = p;
            }
            if (keep != null) {
                int[][] bridges = {{1, -1, 0}, {6, -1, 1}, {0, -1, 5}, {5, -1, 6}};
                Rotation[] brots = {Rotation.NONE, Rotation.CLOCKWISE_90,
                    Rotation.COUNTERCLOCKWISE_90, Rotation.CLOCKWISE_180};
                for (int i = 0; i < bridges.length; i++) {
                    if (!city.rnd.nextBoolean()) continue;
                    CityPiece be = city.add(out, keep, bridges[i][0], bridges[i][1], bridges[i][2],
                        "bridge_end", rot.getRotated(brots[i]), true);
                    city.recursiveChildren(TOWER_BRIDGE, depth + 1, be, null, out);
                }
                city.add(out, p, -1, 4, -1, "tower_top", rot, true);
            } else if (depth == 7) {
                city.add(out, p, -1, 4, -1, "tower_top", rot, true);
            } else {
                return city.recursiveChildren(FAT_TOWER, depth + 1, p, null, out);
            }
            return true;
        }
    };

    private static final Section TOWER_BRIDGE = new Section() {
        @Override public boolean generate(EndCityPieces city, int depth, CityPiece parent,
                                          int[] offset, List<CityPiece> out) {
            Rotation rot = parent.rot;
            int steps = city.rnd.nextInt(4) + 1;
            CityPiece p = city.add(out, parent, 0, 0, -4, "bridge_piece", rot, true);
            int b = 0;
            for (int k = 0; k < steps; k++) {
                if (city.rnd.nextBoolean()) {
                    p = city.add(out, p, 0, b, -4, "bridge_piece", rot, true);
                    b = 0;
                } else {
                    if (city.rnd.nextBoolean()) {
                        p = city.add(out, p, 0, b, -4, "bridge_steep_stairs", rot, true);
                    } else {
                        p = city.add(out, p, 0, b, -8, "bridge_gentle_stairs", rot, true);
                    }
                    b = 4;
                }
            }
            if (!city.shipCreated && city.rnd.nextInt(Math.max(1, 10 - depth)) == 0) {
                city.add(out, p, -8 + city.rnd.nextInt(8), b, -70 + city.rnd.nextInt(10),
                    "ship", rot, true);
                city.shipCreated = true;
            } else {
                if (!city.recursiveChildren(HOUSE_TOWER, depth + 1, p,
                        new int[]{-3, b + 1, -11}, out)) {
                    return false;
                }
            }
            city.add(out, p, 4, b, 0, "bridge_end", rot.getRotated(Rotation.CLOCKWISE_180), true);
            return true;
        }
    };

    private static final Section HOUSE_TOWER = new Section() {
        @Override public boolean generate(EndCityPieces city, int depth, CityPiece parent,
                                          int[] offset, List<CityPiece> out) {
            if (depth > 8) return false;
            Rotation rot = parent.rot;
            int dx = offset != null ? offset[0] : 0;
            int dy = offset != null ? offset[1] : 0;
            int dz = offset != null ? offset[2] : 0;
            CityPiece p = city.add(out, parent, dx, dy, dz, "base_floor", rot, true);
            int i = city.rnd.nextInt(3);
            if (i == 0) {
                city.add(out, p, -1, 4, -1, "base_roof", rot, true);
            } else if (i == 1) {
                p = city.add(out, p, -1, 0, -1, "second_floor_2", rot, false);
                p = city.add(out, p, -1, 8, -1, "second_roof", rot, false);
                city.recursiveChildren(TOWER, depth + 1, p, null, out);
            } else {
                p = city.add(out, p, -1, 0, -1, "second_floor_2", rot, false);
                p = city.add(out, p, -1, 4, -1, "third_floor_2", rot, false);
                p = city.add(out, p, -1, 8, -1, "third_roof", rot, true);
                city.recursiveChildren(TOWER, depth + 1, p, null, out);
            }
            return true;
        }
    };

    private static final Section FAT_TOWER = new Section() {
        @Override public boolean generate(EndCityPieces city, int depth, CityPiece parent,
                                          int[] offset, List<CityPiece> out) {
            Rotation rot = parent.rot;
            CityPiece p = city.add(out, parent, -3, 4, -3, "fat_tower_base", rot, true);
            p = city.add(out, p, 0, 4, 0, "fat_tower_middle", rot, true);
            for (int b = 0; b < 2 && city.rnd.nextInt(3) != 0; b++) {
                p = city.add(out, p, 0, 8, 0, "fat_tower_middle", rot, true);
                int[][] bridges = {{4, -1, 0}, {12, -1, 4}, {0, -1, 8}, {8, -1, 12}};
                Rotation[] brots = {Rotation.NONE, Rotation.CLOCKWISE_90,
                    Rotation.COUNTERCLOCKWISE_90, Rotation.CLOCKWISE_180};
                for (int i = 0; i < bridges.length; i++) {
                    if (!city.rnd.nextBoolean()) continue;
                    CityPiece be = city.add(out, p, bridges[i][0], bridges[i][1], bridges[i][2],
                        "bridge_end", rot.getRotated(brots[i]), true);
                    city.recursiveChildren(TOWER_BRIDGE, depth + 1, be, null, out);
                }
            }
            city.add(out, p, -2, 8, -2, "fat_tower_top", rot, true);
            return true;
        }
    };

    /** 原版 startHouseTower。 */
    public static List<CityPiece> generate(long structureSeed, int startX, int startY, int startZ) {
        LegacyRandomSource rnd = new LegacyRandomSource(structureSeed);
        EndCityPieces city = new EndCityPieces(rnd);
        Rotation rot = Rotation.random(rnd);

        CityPiece root = new CityPiece("base_floor", startX, startY, startZ, rot, true, 0);
        city.pieces.add(root);
        CityPiece p = city.add(city.pieces, root, -1, 0, -1, "second_floor_1", rot, false);
        p = city.add(city.pieces, p, -1, 4, -1, "third_floor_1", rot, false);
        p = city.add(city.pieces, p, -1, 8, -1, "third_roof", rot, true);
        city.recursiveChildren(TOWER, 1, p, null, city.pieces);
        return city.pieces;
    }

    /** 原版 recursiveChildren：候选写入临时表，统一深度赋值与碰撞过滤后整体并入。 */
    private boolean recursiveChildren(Section section, int depth, CityPiece parent,
                                      int[] offset, List<CityPiece> target) {
        if (depth > 8) return false;
        List<CityPiece> candidates = new ArrayList<>();
        if (!section.generate(this, depth, parent, offset, candidates)) return false;

        int candidateDepth = rnd.nextInt();
        for (CityPiece c : candidates) c.genDepth = candidateDepth;
        for (CityPiece c : candidates) {
            BoundingBox bb = c.box();
            for (CityPiece existing : target) {
                if (existing.genDepth == parent.genDepth) continue;
                if (existing.box().intersects(bb)) return false;
            }
        }
        target.addAll(candidates);
        return true;
    }

    /**
     * 每区块构建：与该区块相交的模板放置。纯向量原点先加角偏移换算成
     * 项目尺寸相对变换所需的原点；skipAir = !overwriteAir。
     */
    public static void placeInChunk(WorldGenLevel level, List<CityPiece> pieces,
                                    int chunkX, int chunkZ) {
        int chunkMinX = chunkX << 4, chunkMaxX = chunkMinX + 15;
        int chunkMinZ = chunkZ << 4, chunkMaxZ = chunkMinZ + 15;
        for (CityPiece p : pieces) {
            BoundingBox bb = p.box();
            if (bb.maxX < chunkMinX || bb.minX > chunkMaxX) continue;
            if (bb.maxZ < chunkMinZ || bb.minZ > chunkMaxZ) continue;
            StructureTemplate t = template(p.templateName);
            if (t == null) continue;
            int[] shift = srShift(t, p.rot);
            t.placeInWorld(level, p.ox + shift[0], p.oy, p.oz + shift[1],
                p.rot, Mirror.NONE, !p.overwriteAir);
        }
    }

    /** 尺寸相对变换相对纯向量变换的角偏移：SR(x) = pure(x) + shift。 */
    private static int[] srShift(StructureTemplate t, Rotation rot) {
        return switch (rot) {
            case CLOCKWISE_90 -> new int[]{t.sizeZ - 1, 0};
            case CLOCKWISE_180 -> new int[]{t.sizeX - 1, t.sizeZ - 1};
            case COUNTERCLOCKWISE_90 -> new int[]{0, t.sizeX - 1};
            default -> new int[]{0, 0};
        };
    }
}
