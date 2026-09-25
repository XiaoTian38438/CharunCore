package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.gen.LegacyRandomSource;

import java.util.ArrayList;
import java.util.List;

/** Bug27: 废弃矿井分件系统(原版 MineshaftPieces 移植)。
 *  房间/走廊/十字口/楼梯四类分件 + addChildren 递归 + 包围盒碰撞排除,
 *  跨区块中央注册(ProceduralStructureStart 管线)。曾用"每区块独立 14 段迷你走廊"
 *  简化版 -> 矿井碎片化互不连通、无房间无轨道无刷怪笼矿车。 */
public final class MineshaftPieces {

    public enum Kind { ROOM, CORRIDOR, CROSSING, STAIRS }
    public enum Orient { NORTH, SOUTH, WEST, EAST }

    private static final int MAX_DEPTH = 8;
    private static final int MAX_RANGE = 80;

    public static final class Piece implements BuildablePiece {
        public final Kind kind;
        public final BoundingBox box;
        public final Orient orient;
        public final int genDepth;
        public final boolean mesa;
        // corridor
        public boolean hasRails;
        public boolean spiderCorridor;
        public boolean hasPlacedSpider;
        public int numSections;
        // crossing
        public boolean isTwoFloored;
        // room
        public final List<BoundingBox> childEntrances = new ArrayList<>();

        Piece(Kind kind, BoundingBox box, Orient orient, int genDepth, boolean mesa) {
            this.kind = kind; this.box = box; this.orient = orient;
            this.genDepth = genDepth; this.mesa = mesa;
        }

        @Override public BoundingBox box() { return box; }

        // ── 构建(世界坐标输出, 窗口外写入由 WorldGenLevel 拒绝) ──────────────
        @Override
        public void build(WorldGenLevel level) {
            build(level, new LegacyRandomSource(
                ((long) level.getCenterCX() * 341873128712L)
                    ^ ((long) level.getCenterCZ() * 132897987541L)));
        }

        public void build(WorldGenLevel level, RandomSource rng) {
            switch (kind) {
                case ROOM -> buildRoom(level, rng);
                case CORRIDOR -> buildCorridor(level, rng);
                case CROSSING -> buildCrossing(level);
                case STAIRS -> buildStairs(level);
            }
        }

        private int planks() {
            return BlockStateHelper.getDefault(mesa ? "dark_oak_planks" : "oak_planks");
        }
        private int wood() {
            return BlockStateHelper.getDefault(mesa ? "dark_oak_log" : "oak_log");
        }
        private int fence() {
            return BlockStateHelper.getDefault(mesa ? "dark_oak_fence" : "oak_fence");
        }
        private int chain() {
            return BlockStateHelper.getDefault("iron_chain");
        }

        /** 局部坐标 -> 世界坐标(与原版 StructurePiece.getWorldPos 映射一致)。 */
        private int[] worldPos(int lx, int ly, int lz) {
            return switch (orient) {
                case NORTH -> new int[]{box.minX + lx, box.minY + ly, box.minZ + lz};
                case SOUTH -> new int[]{box.maxX - lx, box.minY + ly, box.maxZ - lz};
                case WEST  -> new int[]{box.minX + lz, box.minY + ly, box.minZ + lx};
                case EAST  -> new int[]{box.maxX - lz, box.minY + ly, box.maxZ - lx};
            };
        }

        private void fillLocal(WorldGenLevel level, int x1, int y1, int z1, int x2, int y2, int z2, int state) {
            for (int x = x1; x <= x2; x++)
                for (int y = y1; y <= y2; y++)
                    for (int z = z1; z <= z2; z++) {
                        int[] p = worldPos(x, y, z);
                        level.setBlock(p[0], p[1], p[2], state);
                    }
        }

        private void setLocalIfAir(WorldGenLevel level, int x, int y, int z, int state) {
            int[] p = worldPos(x, y, z);
            if (level.getBlock(p[0], p[1], p[2]) == 0) level.setBlock(p[0], p[1], p[2], state);
        }

        private void fillColumnDown(WorldGenLevel level, int wx, int wy, int wz, int state) {
            int minY = level.getMinY();
            int y = wy;
            while (y > minY + 1) {
                int below = level.getBlock(wx, y - 1, wz);
                if (!isReplaceableForColumn(below)) break;
                y--;
                level.setBlock(wx, y, wz, state);
            }
        }

        private static boolean isReplaceableForColumn(int state) {
            if (state == 0) return true;
            String n = BlockStateHelper.getName(state);
            if (n == null) return true;
            if (n.startsWith("minecraft:")) n = n.substring(10);
            return n.equals("water") || n.equals("lava") || BlockStateHelper.isReplaceable(n);
        }

        // ── ROOM ────────────────────────────────────────────────────────────
        private void buildRoom(WorldGenLevel level, RandomSource rng) {
            int air = 0;
            int spanX = box.maxX - box.minX, spanZ = box.maxZ - box.minZ;
            fillLocal(level, 0, 1, 0, spanX, Math.min(3, spanY()), spanZ, air);
            for (BoundingBox e : childEntrances) {
                for (int x = e.minX; x <= e.maxX; x++)
                    for (int y = e.maxY - 2; y <= e.maxY; y++)
                        for (int z = e.minZ; z <= e.maxZ; z++)
                            level.setBlock(x, y, z, air);
            }
            // 上半球扩大挖空(原版 generateUpperHalfSphere)
            int cx = (box.minX + box.maxX) / 2, cz = (box.minZ + box.maxZ) / 2;
            int baseY = box.minY + 4, topY = box.maxY;
            int rx = spanX / 2 + 1, rz = spanZ / 2 + 1;
            for (int x = box.minX; x <= box.maxX; x++)
                for (int y = baseY; y <= topY; y++)
                    for (int z = box.minZ; z <= box.maxZ; z++) {
                        double dx = (x - cx) / (double) rx;
                        double dy = (y - topY) / (double) (topY - baseY + 1);
                        double dz = (z - cz) / (double) rz;
                        if (dx * dx + dy * dy + dz * dz <= 1.0) level.setBlock(x, y, z, air);
                    }
        }

        // ── CORRIDOR ────────────────────────────────────────────────────────
        private void buildCorridor(WorldGenLevel level, RandomSource rng) {
            int air = 0;
            int cobweb = BlockStateHelper.getDefault("cobweb");
            int rail = BlockStateHelper.getDefault("rail");
            int wallTorch = BlockStateHelper.getDefault("wall_torch");
            int n6 = numSections * 5 - 1;

            fillLocal(level, 0, 0, 0, 2, 1, n6, air);
            // 顶部 0.8 概率挖空(原版 generateMaybeBox)
            for (int z = 0; z <= n6; z++)
                for (int x = 0; x <= 2; x++)
                    if (rng.nextFloat() < 0.8f) setLocalIfAir(level, x, 2, z, air);

            for (int s = 0; s < numSections; s++) {
                int n2 = 2 + s * 5;
                placeSupport(level, rng, n2);
                maybeCobweb(level, rng, cobweb, 0.1f, 0, 2, n2 - 1);
                maybeCobweb(level, rng, cobweb, 0.1f, 2, 2, n2 - 1);
                maybeCobweb(level, rng, cobweb, 0.1f, 0, 2, n2 + 1);
                maybeCobweb(level, rng, cobweb, 0.1f, 2, 2, n2 + 1);
                maybeCobweb(level, rng, cobweb, 0.05f, 0, 2, n2 - 2);
                maybeCobweb(level, rng, cobweb, 0.05f, 2, 2, n2 - 2);
                maybeCobweb(level, rng, cobweb, 0.05f, 0, 2, n2 + 2);
                maybeCobweb(level, rng, cobweb, 0.05f, 2, 2, n2 + 2);
                if (rng.nextInt(100) == 0) placeChest(level, 2, 0, n2 - 1);
                if (rng.nextInt(100) == 0) placeChest(level, 0, 0, n2 + 1);
                if (spiderCorridor && !hasPlacedSpider) {
                    int n7 = n2 - 1 + rng.nextInt(3);
                    int[] p = worldPos(1, 0, n7);
                    int here = level.getBlock(p[0], p[1], p[2]);
                    String hn = BlockStateHelper.getName(here);
                    if (here == 0 || "air".equals(hn) || BlockStateHelper.isReplaceable(hn)) {
                        hasPlacedSpider = true;
                        placeSpawner(level, p[0], p[1], p[2], "cave_spider");
                    }
                }
            }
            // 地板补板(悬空段铺木板走道)
            int pl = planks();
            for (int x = 0; x <= 2; x++)
                for (int z = 0; z <= n6; z++) {
                    int[] p = worldPos(x, -1, z);
                    int below = level.getBlock(p[0], p[1], p[2]);
                    String bn = below == 0 ? null : BlockStateHelper.getName(below);
                    boolean solidFloor = below != 0 && bn != null && !BlockStateHelper.isReplaceable(bn)
                        && !bn.equals("water") && !bn.equals("lava");
                    if (!solidFloor) setLocalIfAir(level, x, -1, z, pl);
                }
            // 支撑柱向下延伸(入口两根)
            extendSupportDown(level, 0, -1, 2);
            if (numSections > 1) extendSupportDown(level, 0, -1, n6 - 2);
            // 轨道
            if (hasRails && rail > 0) {
                String shape = (orient == Orient.NORTH || orient == Orient.SOUTH)
                    ? "north_south" : "east_west";
                for (int z = 0; z <= n6; z++) {
                    int[] p = worldPos(1, -1, z);
                    int ground = level.getBlock(p[0], p[1], p[2]);
                    String gn = ground == 0 ? null : BlockStateHelper.getName(ground);
                    if (ground == 0 || gn == null || BlockStateHelper.isReplaceable(gn)) continue;
                    if (rng.nextFloat() < 0.7f) {
                        int[] rp = worldPos(1, 0, z);
                        level.setBlock(rp[0], rp[1], rp[2],
                            BlockStateHelper.withProp(rail, "shape", shape));
                    }
                }
            }
        }

        /** 原版 placeSupport: 仅顶部完整(无洞)时搭围栏柱+木板横梁, 梁下挂火把。 */
        private void placeSupport(WorldGenLevel level, RandomSource rng, int z) {
            int pl = planks(), fc = fence();
            int wallTorch = BlockStateHelper.getDefault("wall_torch");
            // isSupportingBox: 局部 x 0..2 顶上必须有实心
            for (int x = 0; x <= 2; x++) {
                int[] p = worldPos(x, 3, z);
                int above = level.getBlock(p[0], p[1], p[2]);
                if (above == 0) return;
            }
            int[] a = worldPos(0, 0, z);
            int[] b = worldPos(2, 0, z);
            for (int[] p : new int[][]{a, b}) {
                for (int y = 0; y <= 1; y++) level.setBlock(p[0], box.minY + y, p[2], fc);
            }
            if (rng.nextInt(4) == 0) {
                int[] pa = worldPos(0, 2, z), pb = worldPos(2, 2, z);
                level.setBlock(pa[0], pa[1], pa[2], pl);
                level.setBlock(pb[0], pb[1], pb[2], pl);
            } else {
                for (int x = 0; x <= 2; x++) {
                    int[] p = worldPos(x, 2, z);
                    level.setBlock(p[0], p[1], p[2], pl);
                }
                if (wallTorch > 0 && rng.nextFloat() < 0.05f) {
                    int[] t = worldPos(1, 2, z - 1);
                    level.setBlock(t[0], t[1], t[2],
                        BlockStateHelper.withProp(wallTorch, "facing",
                            (orient == Orient.NORTH || orient == Orient.SOUTH) ? "east" : "south"));
                }
                if (wallTorch > 0 && rng.nextFloat() < 0.05f) {
                    int[] t = worldPos(1, 2, z + 1);
                    level.setBlock(t[0], t[1], t[2],
                        BlockStateHelper.withProp(wallTorch, "facing",
                            (orient == Orient.NORTH || orient == Orient.SOUTH) ? "west" : "north"));
                }
            }
        }

        private void extendSupportDown(WorldGenLevel level, int lx, int ly, int lz) {
            int wood = wood();
            int[] p = worldPos(lx, ly, lz);
            if (level.getBlock(p[0], p[1], p[2]) == planks()) {
                fillColumnDown(level, p[0], p[1], p[2], wood);
            }
            int[] p2 = worldPos(lx + 2, ly, lz);
            if (level.getBlock(p2[0], p2[1], p2[2]) == planks()) {
                fillColumnDown(level, p2[0], p2[1], p2[2], wood);
            }
        }

        private void maybeCobweb(WorldGenLevel level, RandomSource rng, int cobweb,
                                 float chance, int x, int y, int z) {
            if (cobweb <= 0 || rng.nextFloat() >= chance) return;
            int[] p = worldPos(x, y, z);
            if (level.getBlock(p[0], p[1], p[2]) == 0) level.setBlock(p[0], p[1], p[2], cobweb);
        }

        private void placeChest(WorldGenLevel level, int lx, int ly, int lz) {
            int chest = BlockStateHelper.getDefault("chest");
            if (chest <= 0) return;
            int[] p = worldPos(lx, ly, lz);
            if (level.getBlock(p[0], p[1], p[2]) != 0) return;
            int below = level.getBlock(p[0], p[1] - 1, p[2]);
            if (below == 0) return;
            level.setBlock(p[0], p[1], p[2], chest);
            level.setBlockEntity(p[0], p[1], p[2], org.cloudburstmc.nbt.NbtMap.builder()
                .putString("id", "minecraft:chest")
                .putString("LootTable", "minecraft:chests/abandoned_mineshaft")
                .putInt("x", p[0]).putInt("y", p[1]).putInt("z", p[2])
                .build());
        }

        private void placeSpawner(WorldGenLevel level, int x, int y, int z, String mob) {
            int spawner = BlockStateHelper.getDefault("spawner");
            if (spawner <= 0) return;
            level.setBlock(x, y, z, spawner);
            level.setBlockEntity(x, y, z, org.cloudburstmc.nbt.NbtMap.builder()
                .putString("id", "minecraft:spawner")
                .putCompound("SpawnData", org.cloudburstmc.nbt.NbtMap.builder()
                    .putCompound("entity", org.cloudburstmc.nbt.NbtMap.builder()
                        .putString("id", "minecraft:" + mob).build())
                    .putString("id", "minecraft:" + mob)
                    .build())
                .putInt("MinSpawnDelay", 200).putInt("MaxSpawnDelay", 800)
                .putInt("SpawnCount", 4).putInt("MaxNearbyEntities", 6)
                .putInt("RequiredPlayerRange", 16).putInt("SpawnRange", 4)
                .build());
        }

        // ── CROSSING ────────────────────────────────────────────────────────
        private void buildCrossing(WorldGenLevel level) {
            int air = 0;
            int pl = planks();
            int spanX = box.maxX - box.minX, spanZ = box.maxZ - box.minZ;
            if (isTwoFloored) {
                fillLocal(level, 1, 0, 0, spanX - 1, 2, spanZ, air);
                fillLocal(level, 0, 0, 1, spanX, 2, spanZ - 1, air);
                fillLocal(level, 1, spanY() - 2, 0, spanX - 1, spanY(), spanZ, air);
                fillLocal(level, 0, spanY() - 2, 1, spanX, spanY(), spanZ - 1, air);
                fillLocal(level, 1, 3, 1, spanX - 1, 3, spanZ - 1, air);
            } else {
                fillLocal(level, 1, 0, 0, spanX - 1, spanY(), spanZ, air);
                fillLocal(level, 0, 0, 1, spanX, spanY(), spanZ - 1, air);
            }
            // 四角支撑柱: 顶上无空气才立
            int[][] corners = {{1, 1}, {1, spanZ - 1}, {spanX - 1, 1}, {spanX - 1, spanZ - 1}};
            for (int[] c : corners) {
                int[] top = worldPos(c[0], spanY() + 1, c[1]);
                if (level.getBlock(top[0], top[1], top[2]) != 0) {
                    for (int y = 0; y < spanY(); y++) {
                        int[] p = worldPos(c[0], y, c[1]);
                        level.setBlock(p[0], p[1], p[2], pl);
                    }
                }
            }
            // 地板
            int fy = box.minY - 1;
            for (int x = box.minX; x <= box.maxX; x++)
                for (int z = box.minZ; z <= box.maxZ; z++) {
                    int below = level.getBlock(x, fy, z);
                    String bn = below == 0 ? null : BlockStateHelper.getName(below);
                    boolean solid = below != 0 && bn != null && !BlockStateHelper.isReplaceable(bn);
                    if (!solid) level.setBlock(x, fy, z, pl);
                }
        }

        private int spanY() { return box.maxY - box.minY; }

        // ── STAIRS ──────────────────────────────────────────────────────────
        private void buildStairs(WorldGenLevel level) {
            int air = 0;
            // 与原版 postProcess 的局部盒一致(默认朝向 NORTH 语义, 其余旋转由 worldPos 处理)
            fillLocal(level, 0, 5, 0, 2, 7, 1, air);
            fillLocal(level, 0, 0, 7, 2, 2, 8, air);
            for (int i = 0; i < 5; i++) {
                fillLocal(level, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, air);
            }
        }
    }

    // ── 分件树生成(原版 generateAndAddPiece 递归) ────────────────────────────

    public static List<Piece> generate(long seed, int startBlockX, int startBlockZ, boolean mesa) {
        RandomSource rng = new LegacyRandomSource(seed);
        List<Piece> pieces = new ArrayList<>();
        Piece room = new Piece(Kind.ROOM,
            new BoundingBox(startBlockX, 50, startBlockZ,
                startBlockX + 7 + rng.nextInt(6),
                54 + rng.nextInt(6),
                startBlockZ + 7 + rng.nextInt(6)),
            Orient.NORTH, 0, mesa);
        pieces.add(room);
        addRoomChildren(room, pieces, rng);
        return pieces;
    }

    private static boolean collides(List<Piece> pieces, BoundingBox bb) {
        for (Piece p : pieces) {
            BoundingBox b = p.box;
            if (bb.maxX >= b.minX && bb.minX <= b.maxX
                && bb.maxY >= b.minY && bb.minY <= b.maxY
                && bb.maxZ >= b.minZ && bb.minZ <= b.maxZ) return true;
        }
        return false;
    }

    private static Piece generateAndAddPiece(List<Piece> pieces, RandomSource rng,
                                            int x, int y, int z, Orient dir, int depth, boolean mesa) {
        Piece root = pieces.get(0);
        if (depth > MAX_DEPTH) return null;
        if (Math.abs(x - root.box.minX) > MAX_RANGE || Math.abs(z - root.box.minZ) > MAX_RANGE) return null;
        // 总量保险: 同深度交叉链 + 蛇形路径可能把 DFS 递归深度推到数千帧 -> StackOverflow。
        // 原版实际矿井规模 ~100-300 分件, 400 上限不影响正常形态。
        if (pieces.size() >= 400) return null;
        Piece p = createRandomShaftPiece(pieces, rng, x, y, z, dir, depth, mesa);
        if (p != null) {
            pieces.add(p);
            addChildren(p, pieces, rng);
        }
        return p;
    }

    private static Piece createRandomShaftPiece(List<Piece> pieces, RandomSource rng,
                                                int x, int y, int z, Orient dir, int depth, boolean mesa) {
        int roll = rng.nextInt(100);
        if (roll >= 80) {
            BoundingBox bb = findCrossing(pieces, rng, x, y, z, dir);
            if (bb != null) return newCrossing(depth, bb, dir, mesa);
        } else if (roll >= 70) {
            BoundingBox bb = findStairs(pieces, rng, x, y, z, dir);
            if (bb != null) return new Piece(Kind.STAIRS, bb, dir, depth, mesa);
        } else {
            BoundingBox bb = findCorridorSize(pieces, rng, x, y, z, dir);
            if (bb != null) {
                Piece c = new Piece(Kind.CORRIDOR, bb, dir, depth, mesa);
                c.hasRails = rng.nextInt(3) == 0;
                c.spiderCorridor = !c.hasRails && rng.nextInt(23) == 0;
                c.numSections = (dir == Orient.NORTH || dir == Orient.SOUTH)
                    ? spanZ(bb) / 5 : spanX(bb) / 5;
                return c;
            }
        }
        return null;
    }

    private static Piece newCrossing(int depth, BoundingBox bb, Orient dir, boolean mesa) {
        Piece c = new Piece(Kind.CROSSING, bb, dir, depth, mesa);
        c.isTwoFloored = spanY(bb) > 3;
        return c;
    }

    private static BoundingBox findCrossing(List<Piece> pieces, RandomSource rng,
                                            int x, int y, int z, Orient dir) {
        int h = rng.nextInt(4) == 0 ? 6 : 2;
        BoundingBox bb = switch (dir) {
            case NORTH -> new BoundingBox(x - 1, y, z - 4, x + 3, y + h, z);
            case SOUTH -> new BoundingBox(x - 1, y, z, x + 3, y + h, z + 4);
            case WEST  -> new BoundingBox(x - 4, y, z - 1, x, y + h, z + 3);
            case EAST  -> new BoundingBox(x, y, z - 1, x + 4, y + h, z + 3);
        };
        if (collides(pieces, bb)) return null;
        return bb;
    }

    private static BoundingBox findStairs(List<Piece> pieces, RandomSource rng,
                                          int x, int y, int z, Orient dir) {
        BoundingBox bb = switch (dir) {
            case NORTH -> new BoundingBox(x, y - 5, z - 8, x + 2, y + 2, z);
            case SOUTH -> new BoundingBox(x, y - 5, z, x + 2, y + 2, z + 8);
            case WEST  -> new BoundingBox(x - 8, y - 5, z, x, y + 2, z + 2);
            case EAST  -> new BoundingBox(x, y - 5, z, x + 8, y + 2, z + 2);
        };
        if (collides(pieces, bb)) return null;
        return bb;
    }

    private static BoundingBox findCorridorSize(List<Piece> pieces, RandomSource rng,
                                                int x, int y, int z, Orient dir) {
        int sections = rng.nextInt(3) + 2;
        while (sections > 0) {
            int len = sections * 5;
            BoundingBox bb = switch (dir) {
                case NORTH -> new BoundingBox(x, y, z - (len - 1), x + 2, y + 2, z);
                case SOUTH -> new BoundingBox(x, y, z, x + 2, y + 2, z + len - 1);
                case WEST  -> new BoundingBox(x - (len - 1), y, z, x, y + 2, z + 2);
                case EAST  -> new BoundingBox(x, y, z, x + len - 1, y + 2, z + 2);
            };
            if (!collides(pieces, bb)) return bb;
            sections--;
        }
        return null;
    }

    private static int spanX(BoundingBox bb) { return bb.maxX - bb.minX; }
    private static int spanY(BoundingBox bb) { return bb.maxY - bb.minY; }
    private static int spanZ(BoundingBox bb) { return bb.maxZ - bb.minZ; }

    private static void addChildren(Piece p, List<Piece> pieces, RandomSource rng) {
        switch (p.kind) {
            case ROOM -> addRoomChildren(p, pieces, rng);
            case CORRIDOR -> addCorridorChildren(p, pieces, rng);
            case CROSSING -> addCrossingChildren(p, pieces, rng);
            case STAIRS -> addStairsChildren(p, pieces, rng);
        }
    }

    private static void addRoomChildren(Piece room, List<Piece> pieces, RandomSource rng) {
        int depth = room.genDepth;
        BoundingBox b = room.box;
        int yRange = Math.max(1, spanY(b) - 3 - 1);
        for (int n = 0; n < spanX(b)
                && (n += rng.nextInt(spanX(b))) + 3 <= spanX(b); n += 4) {
            Piece child = generateAndAddPiece(pieces, rng,
                b.minX + n, b.minY + rng.nextInt(yRange) + 1, b.minZ - 1, Orient.NORTH, depth, room.mesa);
            if (child != null) room.childEntrances.add(new BoundingBox(
                child.box.minX, child.box.minY, b.minZ, child.box.maxX, child.box.maxY, b.minZ + 1));
        }
        for (int n = 0; n < spanX(b)
                && (n += rng.nextInt(spanX(b))) + 3 <= spanX(b); n += 4) {
            Piece child = generateAndAddPiece(pieces, rng,
                b.minX + n, b.minY + rng.nextInt(yRange) + 1, b.maxZ + 1, Orient.SOUTH, depth, room.mesa);
            if (child != null) room.childEntrances.add(new BoundingBox(
                child.box.minX, child.box.minY, b.maxZ - 1, child.box.maxX, child.box.maxY, b.maxZ));
        }
        for (int n = 0; n < spanZ(b)
                && (n += rng.nextInt(spanZ(b))) + 3 <= spanZ(b); n += 4) {
            Piece child = generateAndAddPiece(pieces, rng,
                b.minX - 1, b.minY + rng.nextInt(yRange) + 1, b.minZ + n, Orient.WEST, depth, room.mesa);
            if (child != null) room.childEntrances.add(new BoundingBox(
                b.minX, child.box.minY, child.box.minZ, b.minX + 1, child.box.maxY, child.box.maxZ));
        }
        for (int n = 0; n < spanZ(b)
                && (n += rng.nextInt(spanZ(b))) + 3 <= spanZ(b); n += 4) {
            Piece child = generateAndAddPiece(pieces, rng,
                b.maxX + 1, b.minY + rng.nextInt(yRange) + 1, b.minZ + n, Orient.EAST, depth, room.mesa);
            if (child != null) room.childEntrances.add(new BoundingBox(
                b.maxX - 1, child.box.minY, child.box.minZ, b.maxX, child.box.maxY, child.box.maxZ));
        }
    }

    private static void addCorridorChildren(Piece p, List<Piece> pieces, RandomSource rng) {
        int depth = p.genDepth;
        BoundingBox b = p.box;
        int roll = rng.nextInt(4);
        int yJitter = b.minY - 1 + rng.nextInt(3);
        switch (p.orient) {
            case NORTH -> {
                if (roll <= 1) generateAndAddPiece(pieces, rng, b.minX, yJitter, b.minZ - 1, Orient.NORTH, depth, p.mesa);
                else if (roll == 2) generateAndAddPiece(pieces, rng, b.minX - 1, yJitter, b.minZ, Orient.WEST, depth, p.mesa);
                else generateAndAddPiece(pieces, rng, b.maxX + 1, yJitter, b.minZ, Orient.EAST, depth, p.mesa);
            }
            case SOUTH -> {
                if (roll <= 1) generateAndAddPiece(pieces, rng, b.minX, yJitter, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
                else if (roll == 2) generateAndAddPiece(pieces, rng, b.minX - 1, yJitter, b.maxZ - 3, Orient.WEST, depth, p.mesa);
                else generateAndAddPiece(pieces, rng, b.maxX + 1, yJitter, b.maxZ - 3, Orient.EAST, depth, p.mesa);
            }
            case WEST -> {
                if (roll <= 1) generateAndAddPiece(pieces, rng, b.minX - 1, yJitter, b.minZ, Orient.WEST, depth, p.mesa);
                else if (roll == 2) generateAndAddPiece(pieces, rng, b.minX, yJitter, b.minZ - 1, Orient.NORTH, depth, p.mesa);
                else generateAndAddPiece(pieces, rng, b.minX, yJitter, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
            }
            case EAST -> {
                if (roll <= 1) generateAndAddPiece(pieces, rng, b.maxX + 1, yJitter, b.minZ, Orient.EAST, depth, p.mesa);
                else if (roll == 2) generateAndAddPiece(pieces, rng, b.maxX - 3, yJitter, b.minZ - 1, Orient.NORTH, depth, p.mesa);
                else generateAndAddPiece(pieces, rng, b.maxX - 3, yJitter, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
            }
        }
        if (depth >= MAX_DEPTH) return;
        // 侧面支线
        if (p.orient == Orient.NORTH || p.orient == Orient.SOUTH) {
            for (int z = b.minZ + 3; z + 3 <= b.maxZ; z += 5) {
                int r = rng.nextInt(5);
                if (r == 0) generateAndAddPiece(pieces, rng, b.minX - 1, b.minY, z, Orient.WEST, depth + 1, p.mesa);
                else if (r == 1) generateAndAddPiece(pieces, rng, b.maxX + 1, b.minY, z, Orient.EAST, depth + 1, p.mesa);
            }
        } else {
            for (int x = b.minX + 3; x + 3 <= b.maxX; x += 5) {
                int r = rng.nextInt(5);
                if (r == 0) generateAndAddPiece(pieces, rng, x, b.minY, b.minZ - 1, Orient.NORTH, depth + 1, p.mesa);
                else if (r == 1) generateAndAddPiece(pieces, rng, x, b.minY, b.maxZ + 1, Orient.SOUTH, depth + 1, p.mesa);
            }
        }
    }

    private static void addCrossingChildren(Piece p, List<Piece> pieces, RandomSource rng) {
        int depth = p.genDepth;
        BoundingBox b = p.box;
        switch (p.orient) {
            case NORTH -> {
                generateAndAddPiece(pieces, rng, b.minX + 1, b.minY, b.minZ - 1, Orient.NORTH, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.minX - 1, b.minY, b.minZ + 1, Orient.WEST, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.maxX + 1, b.minY, b.minZ + 1, Orient.EAST, depth, p.mesa);
            }
            case SOUTH -> {
                generateAndAddPiece(pieces, rng, b.minX + 1, b.minY, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.minX - 1, b.minY, b.minZ + 1, Orient.WEST, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.maxX + 1, b.minY, b.minZ + 1, Orient.EAST, depth, p.mesa);
            }
            case WEST -> {
                generateAndAddPiece(pieces, rng, b.minX + 1, b.minY, b.minZ - 1, Orient.NORTH, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.minX + 1, b.minY, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.minX - 1, b.minY, b.minZ + 1, Orient.WEST, depth, p.mesa);
            }
            case EAST -> {
                generateAndAddPiece(pieces, rng, b.minX + 1, b.minY, b.minZ - 1, Orient.NORTH, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.minX + 1, b.minY, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
                generateAndAddPiece(pieces, rng, b.maxX + 1, b.minY, b.minZ + 1, Orient.EAST, depth, p.mesa);
            }
        }
        if (p.isTwoFloored) {
            if (rng.nextBoolean()) generateAndAddPiece(pieces, rng, b.minX + 1, b.minY + 4, b.minZ - 1, Orient.NORTH, depth, p.mesa);
            if (rng.nextBoolean()) generateAndAddPiece(pieces, rng, b.minX - 1, b.minY + 4, b.minZ + 1, Orient.WEST, depth, p.mesa);
            if (rng.nextBoolean()) generateAndAddPiece(pieces, rng, b.maxX + 1, b.minY + 4, b.minZ + 1, Orient.EAST, depth, p.mesa);
            if (rng.nextBoolean()) generateAndAddPiece(pieces, rng, b.minX + 1, b.minY + 4, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
        }
    }

    private static void addStairsChildren(Piece p, List<Piece> pieces, RandomSource rng) {
        int depth = p.genDepth;
        BoundingBox b = p.box;
        switch (p.orient) {
            case NORTH -> generateAndAddPiece(pieces, rng, b.minX, b.minY, b.minZ - 1, Orient.NORTH, depth, p.mesa);
            case SOUTH -> generateAndAddPiece(pieces, rng, b.minX, b.minY, b.maxZ + 1, Orient.SOUTH, depth, p.mesa);
            case WEST -> generateAndAddPiece(pieces, rng, b.minX - 1, b.minY, b.minZ, Orient.WEST, depth, p.mesa);
            case EAST -> generateAndAddPiece(pieces, rng, b.maxX + 1, b.minY, b.minZ, Orient.EAST, depth, p.mesa);
        }
    }
}
