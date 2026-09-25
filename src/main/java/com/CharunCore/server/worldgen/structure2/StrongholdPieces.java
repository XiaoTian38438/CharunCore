package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * 原版 StrongholdPieces / StrongholdStructure 逐件移植。
 * StartPiece(StairsDown source) 在 (startX, startY, startZ)，pendingChildren 随机弹出展开，
 * 权重池带 maxPlaceCount 与 doPlace 深度门槛(Library n>4 / PortalRoom n>5)、imposedPiece
 * (起点后强制 FiveCrossing)、5 次重试 + FillerCorridor 兜底，重试整轮直到 PortalRoom 生成。
 * 原版驱动里的 moveBelowSeaLevel 未内联：竖直锚点由调用方经 startY 控制。
 */
public final class StrongholdPieces {

    private static final int MAX_DEPTH = 50;
    public static final int MAGIC_START_Y = 64;

    private StrongholdPieces() {}

    // ── 方块 id ──────────────────────────────────────────────────────
    private static final int AIR = 0;
    private static final int STONE_BRICKS = BlockStateHelper.getDefault("stone_bricks");
    private static final int CRACKED_STONE_BRICKS = BlockStateHelper.getDefault("cracked_stone_bricks");
    private static final int MOSSY_STONE_BRICKS = BlockStateHelper.getDefault("mossy_stone_bricks");
    private static final int INFESTED_STONE_BRICKS = BlockStateHelper.getDefault("infested_stone_bricks");
    private static final int COBBLESTONE = BlockStateHelper.getDefault("cobblestone");
    private static final int OAK_PLANKS = BlockStateHelper.getDefault("oak_planks");
    private static final int BOOKSHELF = BlockStateHelper.getDefault("bookshelf");
    private static final int COBWEB = BlockStateHelper.getDefault("cobweb");
    private static final int LAVA = BlockStateHelper.getDefault("lava");
    private static final int WATER = BlockStateHelper.getDefault("water");
    private static final int END_PORTAL = BlockStateHelper.getDefault("end_portal");

    private static int stairs(String name, String facing) {
        return BlockStateHelper.withProp(BlockStateHelper.getDefault(name), "facing", facing);
    }

    private static int smoothSlabDouble() {
        return BlockStateHelper.withProp(BlockStateHelper.getDefault("smooth_stone_slab"), "type", "double");
    }

    private static int bars(String... sides) {
        int s = BlockStateHelper.getDefault("iron_bars");
        for (String p : sides) s = BlockStateHelper.withProp(s, p, "true");
        return s;
    }

    private static int oakFence(String... sides) {
        int s = BlockStateHelper.getDefault("oak_fence");
        for (String p : sides) s = BlockStateHelper.withProp(s, p, "true");
        return s;
    }

    /** 原版 BoundingBox.orientBox(x,y,z, offX,offY,offZ, w,h,d, dir)。 */
    private static BoundingBox orientBox(int x, int y, int z, int offX, int offY, int offZ,
                                          int w, int h, int d, char dir) {
        return switch (dir) {
            case 'N' -> new BoundingBox(x + offX, y + offY, z - d + 1 + offZ,
                x + w - 1 + offX, y + h - 1 + offY, z + offZ);
            case 'W' -> new BoundingBox(x - d + 1 + offZ, y + offY, z + offX,
                x + offZ, y + h - 1 + offY, z + w - 1 + offX);
            case 'E' -> new BoundingBox(x + offZ, y + offY, z + offX,
                x + d - 1 + offZ, y + h - 1 + offY, z + w - 1 + offX);
            default -> new BoundingBox(x + offX, y + offY, z + offZ,
                x + w - 1 + offX, y + h - 1 + offY, z + d - 1 + offZ);
        };
    }

    /** 原版 StructurePiece.makeBoundingBox（按轴换宽深，无负偏移）。 */
    private static BoundingBox makeBoundingBox(int x, int y, int z, char dir, int w, int h, int d) {
        boolean zAxis = dir == 'N' || dir == 'S';
        return zAxis
            ? new BoundingBox(x, y, z, x + w - 1, y + h - 1, z + d - 1)
            : new BoundingBox(x, y, z, x + d - 1, y + h - 1, z + w - 1);
    }

    /** 原版 Direction.from2DDataValue：0=SOUTH 1=WEST 2=NORTH 3=EAST。 */
    private static char dirFromIndex(int idx) {
        return switch (idx) { case 0 -> 'S'; case 1 -> 'W'; case 2 -> 'N'; default -> 'E'; };
    }

    // ── Piece 抽象基类 ───────────────────────────────────────────────

    public abstract static class Piece {
        BoundingBox box;
        char dir;
        int genDepth;

        Piece(char dir, BoundingBox box, int genDepth) {
            this.dir = dir;
            this.box = box;
            this.genDepth = genDepth;
        }

        void move(int dx, int dy, int dz) {
            box = new BoundingBox(box.minX + dx, box.minY + dy, box.minZ + dz,
                box.maxX + dx, box.maxY + dy, box.maxZ + dz);
        }

        int worldX(int x, int z) {
            return switch (dir) {
                case 'W' -> box.maxX - z;
                case 'E' -> box.minX + z;
                default -> box.minX + x;
            };
        }

        int worldY(int y) { return box.minY + y; }

        int worldZ(int x, int z) {
            return switch (dir) {
                case 'N' -> box.maxZ - z;
                case 'S', 'W', 'E' -> box.minZ + z;
                default -> box.minZ + z;
            };
        }

        boolean clipInside(int wx, int wy, int wz) {
            return wx >= box.minX && wx <= box.maxX && wy >= box.minY && wy <= box.maxY
                && wz >= box.minZ && wz <= box.maxZ;
        }

        void place(WorldGenLevel level, int state, int x, int y, int z) {
            int wx = worldX(x, z), wy = worldY(y), wz = worldZ(x, z);
            if (!clipInside(wx, wy, wz)) return;
            level.setBlock(wx, wy, wz, state);
        }

        void generateBox(WorldGenLevel level, int x1, int y1, int z1, int x2, int y2, int z2,
                         int outer, int inner, boolean onlyAir) {
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        if (onlyAir && isAirAt(level, x, y, z)) continue;
                        boolean shell = x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2;
                        place(level, shell ? outer : inner, x, y, z);
                    }
                }
            }
        }

        void generateAirBox(WorldGenLevel level, int x1, int y1, int z1, int x2, int y2, int z2) {
            generateBox(level, x1, y1, z1, x2, y2, z2, AIR, AIR, false);
        }

        /** 原版 SmoothStoneSelector 版 generateBox：外壳石砖随机变体，内部空气。 */
        void generateSmoothBox(WorldGenLevel level, int x1, int y1, int z1, int x2, int y2, int z2,
                               boolean onlyAir, RandomSource rnd) {
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        if (onlyAir && isAirAt(level, x, y, z)) continue;
                        boolean shell = x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2;
                        int state = AIR;
                        if (shell) {
                            float f = rnd.nextFloat();
                            state = f < 0.2f ? CRACKED_STONE_BRICKS
                                : f < 0.5f ? MOSSY_STONE_BRICKS
                                : f < 0.55f ? INFESTED_STONE_BRICKS
                                : STONE_BRICKS;
                        }
                        place(level, state, x, y, z);
                    }
                }
            }
        }

        void maybeGenerateBlock(WorldGenLevel level, RandomSource rnd, float chance,
                                int x, int y, int z, int state) {
            if (rnd.nextFloat() < chance) place(level, state, x, y, z);
        }

        /** 原版 generateMaybeBox；bl2(isInterior) 路径本结构未用到。 */
        void generateMaybeBox(WorldGenLevel level, RandomSource rnd, float chance,
                                      int x1, int y1, int z1, int x2, int y2, int z2,
                                      int outer, int inner, boolean onlyAir, boolean interiorOnly) {
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        if (rnd.nextFloat() > chance) continue;
                        if (onlyAir && isAirAt(level, x, y, z)) continue;
                        if (interiorOnly && !isInterior(level, x, y, z)) continue;
                        boolean shell = x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2;
                        place(level, shell ? outer : inner, x, y, z);
                    }
                }
            }
        }

        private boolean isInterior(WorldGenLevel level, int x, int y, int z) {
            int wx = worldX(x, z), wy = worldY(y + 1), wz = worldZ(x, z);
            Long key = ((long)(wx >> 4) << 32) | (wz & 0xFFFFFFFFL);
            if (!level.getWindow().containsKey(key)) return false;
            return wy < level.getHeight(wx, wz);
        }

        boolean isAirAt(WorldGenLevel level, int x, int y, int z) {
            return level.getBlock(worldX(x, z), worldY(y), worldZ(x, z)) == AIR;
        }

        void fillColumnDown(WorldGenLevel level, int state, int x, int y, int z) {
            int wx = worldX(x, z), wy = worldY(y), wz = worldZ(x, z);
            if (!clipInside(wx, wy, wz)) return;
            int minYGuard = level.getMinY() + 1;
            while (wy > minYGuard && isReplaceableByStructures(level.getBlock(wx, wy, wz))) {
                level.setBlock(wx, wy, wz, state);
                wy--;
            }
        }

        private static boolean isReplaceableByStructures(int block) {
            if (block == AIR) return true;
            String n = BlockStateHelper.getName(block);
            return n != null && (n.endsWith(":lava") || n.equals("lava") || n.endsWith("glow_lichen"));
        }

        abstract void addChildren(StartPiece start, List<Piece> pieces, RandomSource rnd);

        abstract void build(WorldGenLevel level, RandomSource rnd);

        static boolean collides(List<Piece> pieces, BoundingBox bb) {
            return findCollisionPiece(pieces, bb) != null;
        }

        static Piece findCollisionPiece(List<Piece> pieces, BoundingBox bb) {
            for (Piece p : pieces) {
                if (p.box.intersects(bb)) return p;
            }
            return null;
        }

        static boolean isOkBox(BoundingBox bb) { return bb.minY > 10; }

        // ── 小门与子件生成 ───────────────────────────────────────────

        SmallDoorType entryDoor = SmallDoorType.OPENING;

        SmallDoorType randomSmallDoor(RandomSource rnd) {
            return switch (rnd.nextInt(5)) {
                case 2 -> SmallDoorType.WOOD_DOOR;
                case 3 -> SmallDoorType.GRATES;
                case 4 -> SmallDoorType.IRON_DOOR;
                default -> SmallDoorType.OPENING;
            };
        }

        void generateSmallDoor(WorldGenLevel level, SmallDoorType type, int x, int y, int z) {
            switch (type) {
                case OPENING -> generateAirBox(level, x, y, z, x + 2, y + 2, z);
                case WOOD_DOOR -> {
                    place(level, STONE_BRICKS, x, y, z);
                    place(level, STONE_BRICKS, x, y + 1, z);
                    place(level, STONE_BRICKS, x, y + 2, z);
                    place(level, STONE_BRICKS, x + 1, y + 2, z);
                    place(level, STONE_BRICKS, x + 2, y + 2, z);
                    place(level, STONE_BRICKS, x + 2, y + 1, z);
                    place(level, STONE_BRICKS, x + 2, y, z);
                    place(level, door(BlockStateHelper.getDefault("oak_door"), false), x + 1, y, z);
                    place(level, door(BlockStateHelper.getDefault("oak_door"), true), x + 1, y + 1, z);
                }
                case GRATES -> {
                    place(level, AIR, x + 1, y, z);
                    place(level, AIR, x + 1, y + 1, z);
                    place(level, bars("west"), x, y, z);
                    place(level, bars("west"), x, y + 1, z);
                    place(level, bars("east", "west"), x, y + 2, z);
                    place(level, bars("east", "west"), x + 1, y + 2, z);
                    place(level, bars("east", "west"), x + 2, y + 2, z);
                    place(level, bars("east"), x + 2, y + 1, z);
                    place(level, bars("east"), x + 2, y, z);
                }
                case IRON_DOOR -> {
                    place(level, STONE_BRICKS, x, y, z);
                    place(level, STONE_BRICKS, x, y + 1, z);
                    place(level, STONE_BRICKS, x, y + 2, z);
                    place(level, STONE_BRICKS, x + 1, y + 2, z);
                    place(level, STONE_BRICKS, x + 2, y + 2, z);
                    place(level, STONE_BRICKS, x + 2, y + 1, z);
                    place(level, STONE_BRICKS, x + 2, y, z);
                    place(level, door(BlockStateHelper.getDefault("iron_door"), false), x + 1, y, z);
                    place(level, door(BlockStateHelper.getDefault("iron_door"), true), x + 1, y + 1, z);
                    place(level, stairs("stone_button", "north"), x + 2, y + 1, z + 1);
                    place(level, stairs("stone_button", "south"), x + 2, y + 1, z - 1);
                }
            }
        }

        private static int door(int base, boolean upper) {
            return upper ? BlockStateHelper.withProp(base, "half", "upper") : base;
        }

        Piece generateChildForward(StartPiece start, List<Piece> pieces, RandomSource rnd, int ox, int oy) {
            return switch (dir) {
                case 'N' -> generateAndAddPiece(start, pieces, rnd, box.minX + ox, box.minY + oy, box.minZ - 1, 'N', genDepth);
                case 'S' -> generateAndAddPiece(start, pieces, rnd, box.minX + ox, box.minY + oy, box.maxZ + 1, 'S', genDepth);
                case 'W' -> generateAndAddPiece(start, pieces, rnd, box.minX - 1, box.minY + oy, box.minZ + ox, 'W', genDepth);
                case 'E' -> generateAndAddPiece(start, pieces, rnd, box.maxX + 1, box.minY + oy, box.minZ + ox, 'E', genDepth);
                default -> null;
            };
        }

        /** 参数顺序同原版 generateSmallDoorChildLeft(n=高度偏移, n2=水平偏移)。 */
        Piece generateChildLeft(StartPiece start, List<Piece> pieces, RandomSource rnd, int n, int n2) {
            return switch (dir) {
                case 'N', 'S' -> generateAndAddPiece(start, pieces, rnd, box.minX - 1, box.minY + n, box.minZ + n2, 'W', genDepth);
                case 'W', 'E' -> generateAndAddPiece(start, pieces, rnd, box.minX + n2, box.minY + n, box.minZ - 1, 'N', genDepth);
                default -> null;
            };
        }

        Piece generateChildRight(StartPiece start, List<Piece> pieces, RandomSource rnd, int n, int n2) {
            return switch (dir) {
                case 'N', 'S' -> generateAndAddPiece(start, pieces, rnd, box.maxX + 1, box.minY + n, box.minZ + n2, 'E', genDepth);
                case 'W', 'E' -> generateAndAddPiece(start, pieces, rnd, box.minX + n2, box.minY + n, box.maxZ + 1, 'S', genDepth);
                default -> null;
            };
        }
    }

    enum SmallDoorType { OPENING, WOOD_DOOR, GRATES, IRON_DOOR }

    // ── 权重引擎 ─────────────────────────────────────────────────────

    static final class PieceWeight {
        final String pieceClass;
        final int weight;
        final int maxPlaceCount;
        final int minDepth;
        int placeCount;

        PieceWeight(String cls, int weight, int maxPlaceCount) { this(cls, weight, maxPlaceCount, 0); }

        /** minDepth: Library doPlace 额外要求 depth>4、PortalRoom depth>5。 */
        PieceWeight(String cls, int weight, int maxPlaceCount, int minDepth) {
            this.pieceClass = cls;
            this.weight = weight;
            this.maxPlaceCount = maxPlaceCount;
            this.minDepth = minDepth;
        }

        boolean doPlace(int depth) {
            return (maxPlaceCount == 0 || placeCount < maxPlaceCount) && depth >= minDepth;
        }

        boolean isValid() {
            return maxPlaceCount == 0 || placeCount < maxPlaceCount;
        }
    }

    private static List<PieceWeight> freshWeights() {
        List<PieceWeight> l = new ArrayList<>();
        l.add(new PieceWeight("Straight", 40, 0));
        l.add(new PieceWeight("PrisonHall", 5, 5));
        l.add(new PieceWeight("LeftTurn", 20, 0));
        l.add(new PieceWeight("RightTurn", 20, 0));
        l.add(new PieceWeight("RoomCrossing", 10, 6));
        l.add(new PieceWeight("StraightStairsDown", 5, 5));
        l.add(new PieceWeight("StairsDown", 5, 5));
        l.add(new PieceWeight("FiveCrossing", 5, 4));
        l.add(new PieceWeight("ChestCorridor", 5, 4));
        l.add(new PieceWeight("Library", 10, 2, 5));
        l.add(new PieceWeight("PortalRoom", 20, 1, 6));
        return l;
    }

    private static Piece findAndCreatePieceFactory(String clazz, List<Piece> pieces, RandomSource rnd,
                                                   int x, int y, int z, char dir, int depth) {
        return switch (clazz) {
            case "Straight" -> Straight.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "PrisonHall" -> PrisonHall.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "LeftTurn" -> LeftTurn.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "RightTurn" -> RightTurn.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "RoomCrossing" -> RoomCrossing.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "StraightStairsDown" -> StraightStairsDown.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "StairsDown" -> StairsDown.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "FiveCrossing" -> FiveCrossing.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "ChestCorridor" -> ChestCorridor.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "Library" -> Library.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "PortalRoom" -> PortalRoom.createPiece(pieces, x, y, z, dir, depth);
            default -> null;
        };
    }

    private static boolean updatePieceWeight(StartPiece start) {
        boolean anyValid = false;
        int total = 0;
        for (PieceWeight pw : start.currentPieces) {
            if (pw.maxPlaceCount > 0 && pw.placeCount < pw.maxPlaceCount) anyValid = true;
            total += pw.weight;
        }
        start.totalWeight = total;
        return anyValid;
    }

    private static Piece generatePieceFromSmallDoor(StartPiece start, List<Piece> pieces, RandomSource rnd,
                                                    int x, int y, int z, char dir, int depth) {
        if (!updatePieceWeight(start)) return null;
        if (start.imposedPiece != null) {
            Piece piece = findAndCreatePieceFactory(start.imposedPiece, pieces, rnd, x, y, z, dir, depth);
            start.imposedPiece = null;
            if (piece != null) return piece;
        }
        int tries = 0;
        retry:
        while (++tries <= 5) {
            int roll = rnd.nextInt(start.totalWeight);
            for (PieceWeight pw : start.currentPieces) {
                roll -= pw.weight;
                if (roll >= 0) continue;
                if (!pw.doPlace(depth) || pw == start.previousPiece) continue retry;
                Piece piece = findAndCreatePieceFactory(pw.pieceClass, pieces, rnd, x, y, z, dir, depth);
                if (piece == null) continue;
                pw.placeCount++;
                start.previousPiece = pw;
                if (!pw.isValid()) start.currentPieces.remove(pw);
                return piece;
            }
        }
        BoundingBox bb = FillerCorridor.findPieceBox(pieces, x, y, z, dir);
        if (bb != null && bb.minY > 1) return new FillerCorridor(dir, bb);
        return null;
    }

    private static Piece generateAndAddPiece(StartPiece start, List<Piece> pieces, RandomSource rnd,
                                             int x, int y, int z, char dir, int depth) {
        if (depth > MAX_DEPTH) return null;
        if (Math.abs(x - start.box.minX) > 112 || Math.abs(z - start.box.minZ) > 112) return null;
        Piece piece = generatePieceFromSmallDoor(start, pieces, rnd, x, y, z, dir, depth + 1);
        if (piece != null) {
            pieces.add(piece);
            start.pendingChildren.add(piece);
        }
        return piece;
    }

    // ── 具体件 ───────────────────────────────────────────────────────

    static class Straight extends Piece {
        private final boolean leftChild;
        private final boolean rightChild;

        Straight(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
            this.leftChild = rnd.nextInt(2) == 0;
            this.rightChild = rnd.nextInt(2) == 0;
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 1);
            if (leftChild) generateChildLeft(s, p, r, 1, 2);
            if (rightChild) generateChildRight(s, p, r, 1, 2);
        }

        static Straight createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -1, 0, 5, 5, 7, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new Straight(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 4, 4, 6, true, r);
            generateSmallDoor(l, entryDoor, 1, 1, 0);
            generateSmallDoor(l, SmallDoorType.OPENING, 1, 1, 6);
            maybeGenerateBlock(l, r, 0.1f, 1, 2, 1, wallTorch("east"));
            maybeGenerateBlock(l, r, 0.1f, 3, 2, 1, wallTorch("west"));
            maybeGenerateBlock(l, r, 0.1f, 1, 2, 5, wallTorch("east"));
            maybeGenerateBlock(l, r, 0.1f, 3, 2, 5, wallTorch("west"));
            if (leftChild) generateAirBox(l, 0, 1, 2, 0, 3, 4);
            if (rightChild) generateAirBox(l, 4, 1, 2, 4, 3, 4);
        }
    }

    private static int wallTorch(String facing) {
        return BlockStateHelper.withProp(BlockStateHelper.getDefault("wall_torch"), "facing", facing);
    }

    static class PrisonHall extends Piece {
        PrisonHall(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 1);
        }

        static PrisonHall createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -1, 0, 9, 5, 11, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new PrisonHall(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 8, 4, 10, true, r);
            generateSmallDoor(l, entryDoor, 1, 1, 0);
            generateAirBox(l, 1, 1, 10, 3, 3, 10);
            generateSmoothBox(l, 4, 1, 1, 4, 3, 1, false, r);
            generateSmoothBox(l, 4, 1, 3, 4, 3, 3, false, r);
            generateSmoothBox(l, 4, 1, 7, 4, 3, 7, false, r);
            generateSmoothBox(l, 4, 1, 9, 4, 3, 9, false, r);
            int ns = bars("north", "south");
            int nse = bars("north", "south", "east");
            int we = bars("west", "east");
            for (int i = 1; i <= 3; i++) {
                place(l, ns, 4, i, 4);
                place(l, nse, 4, i, 5);
                place(l, ns, 4, i, 6);
                place(l, we, 5, i, 5);
                place(l, we, 6, i, 5);
                place(l, we, 7, i, 5);
            }
            place(l, ns, 4, 3, 2);
            place(l, ns, 4, 3, 8);
            int doorLower = stairs("iron_door", "west");
            int doorUpper = BlockStateHelper.withProp(doorLower, "half", "upper");
            place(l, doorLower, 4, 1, 2);
            place(l, doorUpper, 4, 2, 2);
            place(l, doorLower, 4, 1, 8);
            place(l, doorUpper, 4, 2, 8);
        }
    }

    static class LeftTurn extends Piece {
        LeftTurn(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            if (dir == 'N' || dir == 'E') generateChildLeft(s, p, r, 1, 1);
            else generateChildRight(s, p, r, 1, 1);
        }

        static LeftTurn createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -1, 0, 5, 5, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new LeftTurn(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 4, 4, 4, true, r);
            generateSmallDoor(l, entryDoor, 1, 1, 0);
            if (dir == 'N' || dir == 'E') generateAirBox(l, 0, 1, 1, 0, 3, 3);
            else generateAirBox(l, 4, 1, 1, 4, 3, 3);
        }
    }

    static class RightTurn extends Piece {
        RightTurn(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            if (dir == 'N' || dir == 'E') generateChildRight(s, p, r, 1, 1);
            else generateChildLeft(s, p, r, 1, 1);
        }

        static RightTurn createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -1, 0, 5, 5, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new RightTurn(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 4, 4, 4, true, r);
            generateSmallDoor(l, entryDoor, 1, 1, 0);
            if (dir == 'N' || dir == 'E') generateAirBox(l, 4, 1, 1, 4, 3, 3);
            else generateAirBox(l, 0, 1, 1, 0, 3, 3);
        }
    }

    static class RoomCrossing extends Piece {
        private final int type;

        RoomCrossing(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
            this.type = rnd.nextInt(5);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 4, 1);
            generateChildLeft(s, p, r, 1, 4);
            generateChildRight(s, p, r, 1, 4);
        }

        static RoomCrossing createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -4, -1, 0, 11, 7, 11, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new RoomCrossing(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 10, 6, 10, true, r);
            generateSmallDoor(l, entryDoor, 4, 1, 0);
            generateAirBox(l, 4, 1, 10, 6, 3, 10);
            generateAirBox(l, 0, 1, 4, 0, 3, 6);
            generateAirBox(l, 10, 1, 4, 10, 3, 6);
            switch (type) {
                case 0 -> {
                    place(l, STONE_BRICKS, 5, 1, 5);
                    place(l, STONE_BRICKS, 5, 2, 5);
                    place(l, STONE_BRICKS, 5, 3, 5);
                    place(l, wallTorch("west"), 4, 3, 5);
                    place(l, wallTorch("east"), 6, 3, 5);
                    place(l, wallTorch("south"), 5, 3, 4);
                    place(l, wallTorch("north"), 5, 3, 6);
                    int slab = BlockStateHelper.getDefault("smooth_stone_slab");
                    place(l, slab, 4, 1, 4);
                    place(l, slab, 4, 1, 5);
                    place(l, slab, 4, 1, 6);
                    place(l, slab, 6, 1, 4);
                    place(l, slab, 6, 1, 5);
                    place(l, slab, 6, 1, 6);
                    place(l, slab, 5, 1, 4);
                    place(l, slab, 5, 1, 6);
                }
                case 1 -> {
                    for (int i = 0; i < 5; i++) {
                        place(l, STONE_BRICKS, 3, 1, 3 + i);
                        place(l, STONE_BRICKS, 7, 1, 3 + i);
                        place(l, STONE_BRICKS, 3 + i, 1, 3);
                        place(l, STONE_BRICKS, 3 + i, 1, 7);
                    }
                    place(l, STONE_BRICKS, 5, 1, 5);
                    place(l, STONE_BRICKS, 5, 2, 5);
                    place(l, STONE_BRICKS, 5, 3, 5);
                    place(l, WATER, 5, 4, 5);
                }
                case 2 -> {
                    for (int n = 1; n <= 9; n++) {
                        place(l, COBBLESTONE, 1, 3, n);
                        place(l, COBBLESTONE, 9, 3, n);
                        place(l, COBBLESTONE, n, 3, 1);
                        place(l, COBBLESTONE, n, 3, 9);
                    }
                    place(l, COBBLESTONE, 5, 1, 4);
                    place(l, COBBLESTONE, 5, 1, 6);
                    place(l, COBBLESTONE, 5, 3, 4);
                    place(l, COBBLESTONE, 5, 3, 6);
                    place(l, COBBLESTONE, 4, 1, 5);
                    place(l, COBBLESTONE, 6, 1, 5);
                    place(l, COBBLESTONE, 4, 3, 5);
                    place(l, COBBLESTONE, 6, 3, 5);
                    for (int n = 1; n <= 3; n++) {
                        place(l, COBBLESTONE, 4, n, 4);
                        place(l, COBBLESTONE, 6, n, 4);
                        place(l, COBBLESTONE, 4, n, 6);
                        place(l, COBBLESTONE, 6, n, 6);
                    }
                    place(l, wallTorch("north"), 5, 3, 5);
                    for (int n = 2; n <= 8; n++) {
                        place(l, OAK_PLANKS, 2, 3, n);
                        place(l, OAK_PLANKS, 3, 3, n);
                        if (n <= 3 || n >= 7) {
                            place(l, OAK_PLANKS, 4, 3, n);
                            place(l, OAK_PLANKS, 5, 3, n);
                            place(l, OAK_PLANKS, 6, 3, n);
                        }
                        place(l, OAK_PLANKS, 7, 3, n);
                        place(l, OAK_PLANKS, 8, 3, n);
                    }
                    int ladder = stairs("ladder", "west");
                    place(l, ladder, 9, 1, 3);
                    place(l, ladder, 9, 2, 3);
                    place(l, ladder, 9, 3, 3);
                    createChest(l, 3, 4, 8, "minecraft:chests/stronghold_crossing");
                }
                default -> {}
            }
        }
    }

    static class StraightStairsDown extends Piece {
        StraightStairsDown(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 1);
        }

        static StraightStairsDown createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -7, 0, 5, 11, 8, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new StraightStairsDown(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 4, 10, 7, true, r);
            generateSmallDoor(l, entryDoor, 1, 7, 0);
            generateSmallDoor(l, SmallDoorType.OPENING, 1, 1, 7);
            int stair = stairs("cobblestone_stairs", "south");
            for (int i = 0; i < 6; i++) {
                place(l, stair, 1, 6 - i, 1 + i);
                place(l, stair, 2, 6 - i, 1 + i);
                place(l, stair, 3, 6 - i, 1 + i);
                if (i < 5) {
                    place(l, STONE_BRICKS, 1, 5 - i, 1 + i);
                    place(l, STONE_BRICKS, 2, 5 - i, 1 + i);
                    place(l, STONE_BRICKS, 3, 5 - i, 1 + i);
                }
            }
        }
    }

    static class StairsDown extends Piece {
        private final boolean isSource;

        StairsDown(char dir, BoundingBox box, boolean source) {
            super(dir, box, 0);
            this.isSource = source;
            this.entryDoor = SmallDoorType.OPENING;
        }

        StairsDown(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.isSource = false;
            this.entryDoor = randomSmallDoor(rnd);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            if (isSource) s.imposedPiece = "FiveCrossing";
            generateChildForward(s, p, r, 1, 1);
        }

        static StairsDown createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -7, 0, 5, 11, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new StairsDown(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 4, 10, 4, true, r);
            generateSmallDoor(l, entryDoor, 1, 7, 0);
            generateSmallDoor(l, SmallDoorType.OPENING, 1, 1, 4);
            place(l, STONE_BRICKS, 2, 6, 1);
            place(l, STONE_BRICKS, 1, 5, 1);
            place(l, BlockStateHelper.getDefault("smooth_stone_slab"), 1, 6, 1);
            place(l, STONE_BRICKS, 1, 5, 2);
            place(l, STONE_BRICKS, 1, 4, 3);
            place(l, BlockStateHelper.getDefault("smooth_stone_slab"), 1, 5, 3);
            place(l, STONE_BRICKS, 2, 4, 3);
            place(l, STONE_BRICKS, 3, 3, 3);
            place(l, BlockStateHelper.getDefault("smooth_stone_slab"), 3, 4, 3);
            place(l, STONE_BRICKS, 3, 3, 2);
            place(l, STONE_BRICKS, 3, 2, 1);
            place(l, BlockStateHelper.getDefault("smooth_stone_slab"), 3, 3, 1);
            place(l, STONE_BRICKS, 2, 2, 1);
            place(l, STONE_BRICKS, 1, 1, 1);
            place(l, BlockStateHelper.getDefault("smooth_stone_slab"), 1, 2, 1);
            place(l, STONE_BRICKS, 1, 1, 2);
            place(l, BlockStateHelper.getDefault("smooth_stone_slab"), 1, 1, 3);
        }
    }

    static class FiveCrossing extends Piece {
        private final boolean leftLow;
        private final boolean leftHigh;
        private final boolean rightLow;
        private final boolean rightHigh;

        FiveCrossing(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
            this.leftLow = rnd.nextBoolean();
            this.leftHigh = rnd.nextBoolean();
            this.rightLow = rnd.nextBoolean();
            this.rightHigh = rnd.nextInt(3) > 0;
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            int n = 3;
            int n2 = 5;
            if (dir == 'W' || dir == 'N') { n = 8 - n; n2 = 8 - n2; }
            generateChildForward(s, p, r, 5, 1);
            if (leftLow) generateChildLeft(s, p, r, n, 1);
            if (leftHigh) generateChildLeft(s, p, r, n2, 7);
            if (rightLow) generateChildRight(s, p, r, n, 1);
            if (rightHigh) generateChildRight(s, p, r, n2, 7);
        }

        static FiveCrossing createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -4, -3, 0, 10, 9, 11, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new FiveCrossing(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 9, 8, 10, true, r);
            generateSmallDoor(l, entryDoor, 4, 3, 0);
            if (leftLow) generateAirBox(l, 0, 3, 1, 0, 5, 3);
            if (rightLow) generateAirBox(l, 9, 3, 1, 9, 5, 3);
            if (leftHigh) generateAirBox(l, 0, 5, 7, 0, 7, 9);
            if (rightHigh) generateAirBox(l, 9, 5, 7, 9, 7, 9);
            generateAirBox(l, 5, 1, 10, 7, 3, 10);
            generateSmoothBox(l, 1, 2, 1, 8, 2, 6, false, r);
            generateSmoothBox(l, 4, 1, 5, 4, 4, 9, false, r);
            generateSmoothBox(l, 8, 1, 5, 8, 4, 9, false, r);
            generateSmoothBox(l, 1, 4, 7, 3, 4, 9, false, r);
            generateSmoothBox(l, 1, 3, 5, 3, 3, 6, false, r);
            int slab = BlockStateHelper.getDefault("smooth_stone_slab");
            generateBox(l, 1, 3, 4, 3, 3, 4, slab, slab, false);
            generateBox(l, 1, 4, 6, 3, 4, 6, slab, slab, false);
            generateSmoothBox(l, 5, 1, 7, 7, 1, 8, false, r);
            generateBox(l, 5, 1, 9, 7, 1, 9, slab, slab, false);
            generateBox(l, 5, 2, 7, 7, 2, 7, slab, slab, false);
            generateBox(l, 4, 5, 7, 4, 5, 9, slab, slab, false);
            generateBox(l, 8, 5, 7, 8, 5, 9, slab, slab, false);
            int dbl = smoothSlabDouble();
            generateBox(l, 5, 5, 7, 7, 5, 9, dbl, dbl, false);
            place(l, wallTorch("south"), 6, 5, 6);
        }
    }

    static class ChestCorridor extends Piece {
        private boolean hasPlacedChest;

        ChestCorridor(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 1);
        }

        static ChestCorridor createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -1, 0, 5, 5, 7, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new ChestCorridor(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 4, 4, 6, true, r);
            generateSmallDoor(l, entryDoor, 1, 1, 0);
            generateSmallDoor(l, SmallDoorType.OPENING, 1, 1, 6);
            generateBox(l, 3, 1, 2, 3, 1, 4, STONE_BRICKS, STONE_BRICKS, false);
            int slab = BlockStateHelper.getDefault("stone_brick_slab");
            place(l, slab, 3, 1, 1);
            place(l, slab, 3, 1, 5);
            place(l, slab, 3, 2, 2);
            place(l, slab, 3, 2, 4);
            for (int i = 2; i <= 4; i++) place(l, slab, 2, 1, i);
            int cx = worldX(3, 3), cy = worldY(2), cz = worldZ(3, 3);
            if (!hasPlacedChest && clipInside(cx, cy, cz)) {
                hasPlacedChest = true;
                createChest(l, cx, cy, cz, "minecraft:chests/stronghold_corridor");
            }
        }
    }

    static class Library extends Piece {
        private final boolean isTall;

        Library(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.entryDoor = randomSmallDoor(rnd);
            this.isTall = box.getSpanY() > 6;
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {}

        static Library createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -4, -1, 0, 14, 11, 15, dir);
            if (!(isOkBox(bb) && !collides(pieces, bb))) {
                bb = orientBox(x, y, z, -4, -1, 0, 14, 6, 15, dir);
                if (!(isOkBox(bb) && !collides(pieces, bb))) return null;
            }
            return new Library(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            int height = isTall ? 11 : 6;
            generateSmoothBox(l, 0, 0, 0, 13, height - 1, 14, true, r);
            generateSmallDoor(l, entryDoor, 4, 1, 0);
            generateMaybeBox(l, r, 0.07f, 2, 1, 1, 11, 4, 13, COBWEB, COBWEB, false, false);
            for (int n = 1; n <= 13; n++) {
                if ((n - 1) % 4 == 0) {
                    generateBox(l, 1, 1, n, 1, 4, n, OAK_PLANKS, OAK_PLANKS, false);
                    generateBox(l, 12, 1, n, 12, 4, n, OAK_PLANKS, OAK_PLANKS, false);
                    place(l, wallTorch("east"), 2, 3, n);
                    place(l, wallTorch("west"), 11, 3, n);
                    if (isTall) {
                        generateBox(l, 1, 6, n, 1, 9, n, OAK_PLANKS, OAK_PLANKS, false);
                        generateBox(l, 12, 6, n, 12, 9, n, OAK_PLANKS, OAK_PLANKS, false);
                    }
                } else {
                    generateBox(l, 1, 1, n, 1, 4, n, BOOKSHELF, BOOKSHELF, false);
                    generateBox(l, 12, 1, n, 12, 4, n, BOOKSHELF, BOOKSHELF, false);
                    if (isTall) {
                        generateBox(l, 1, 6, n, 1, 9, n, BOOKSHELF, BOOKSHELF, false);
                        generateBox(l, 12, 6, n, 12, 9, n, BOOKSHELF, BOOKSHELF, false);
                    }
                }
            }
            for (int n = 3; n < 12; n += 2) {
                generateBox(l, 3, 1, n, 4, 3, n, BOOKSHELF, BOOKSHELF, false);
                generateBox(l, 6, 1, n, 7, 3, n, BOOKSHELF, BOOKSHELF, false);
                generateBox(l, 9, 1, n, 10, 3, n, BOOKSHELF, BOOKSHELF, false);
            }
            if (isTall) {
                generateBox(l, 1, 5, 1, 3, 5, 13, OAK_PLANKS, OAK_PLANKS, false);
                generateBox(l, 10, 5, 1, 12, 5, 13, OAK_PLANKS, OAK_PLANKS, false);
                generateBox(l, 4, 5, 1, 9, 5, 2, OAK_PLANKS, OAK_PLANKS, false);
                generateBox(l, 4, 5, 12, 9, 5, 13, OAK_PLANKS, OAK_PLANKS, false);
                place(l, OAK_PLANKS, 9, 5, 11);
                place(l, OAK_PLANKS, 8, 5, 11);
                place(l, OAK_PLANKS, 9, 5, 10);
                int we = oakFence("west", "east");
                int ns = oakFence("north", "south");
                generateBox(l, 3, 6, 3, 3, 6, 11, ns, ns, false);
                generateBox(l, 10, 6, 3, 10, 6, 9, ns, ns, false);
                generateBox(l, 4, 6, 2, 9, 6, 2, we, we, false);
                generateBox(l, 4, 6, 12, 7, 6, 12, we, we, false);
                place(l, oakFence("north", "east"), 3, 6, 2);
                place(l, oakFence("south", "east"), 3, 6, 12);
                place(l, oakFence("north", "west"), 10, 6, 2);
                for (int i = 0; i <= 2; i++) {
                    place(l, oakFence("south", "west"), 8 + i, 6, 12 - i);
                    if (i != 2) place(l, oakFence("north", "east"), 8 + i, 6, 11 - i);
                }
                int ladder = stairs("ladder", "south");
                for (int y = 1; y <= 7; y++) place(l, ladder, 10, y, 13);
                int fe = oakFence("east");
                int fw = oakFence("west");
                place(l, fe, 6, 9, 7);
                place(l, fw, 7, 9, 7);
                place(l, fe, 6, 8, 7);
                place(l, fw, 7, 8, 7);
                int all = oakFence("north", "south", "west", "east");
                place(l, all, 6, 7, 7);
                place(l, all, 7, 7, 7);
                place(l, fe, 5, 7, 7);
                place(l, fw, 8, 7, 7);
                place(l, oakFence("east", "north"), 6, 7, 6);
                place(l, oakFence("east", "south"), 6, 7, 8);
                place(l, oakFence("west", "north"), 7, 7, 6);
                place(l, oakFence("west", "south"), 7, 7, 8);
                int torch = BlockStateHelper.getDefault("torch");
                place(l, torch, 5, 8, 7);
                place(l, torch, 8, 8, 7);
                place(l, torch, 6, 8, 6);
                place(l, torch, 6, 8, 8);
                place(l, torch, 7, 8, 6);
                place(l, torch, 7, 8, 8);
            }
            createChest(l, 3, 3, 5, "minecraft:chests/stronghold_library");
            if (isTall) {
                place(l, AIR, 12, 9, 1);
                createChest(l, 12, 8, 1, "minecraft:chests/stronghold_library");
            }
        }
    }

    static class PortalRoom extends Piece {
        private boolean hasPlacedSpawner;

        PortalRoom(char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            s.portalRoomPiece = this;
        }

        static PortalRoom createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -4, -1, 0, 11, 8, 16, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new PortalRoom(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateSmoothBox(l, 0, 0, 0, 10, 7, 15, false, r);
            generateSmallDoor(l, SmallDoorType.GRATES, 4, 1, 0);
            generateSmoothBox(l, 1, 6, 1, 1, 6, 14, false, r);
            generateSmoothBox(l, 9, 6, 1, 9, 6, 14, false, r);
            generateSmoothBox(l, 2, 6, 1, 8, 6, 2, false, r);
            generateSmoothBox(l, 2, 6, 14, 8, 6, 14, false, r);
            generateSmoothBox(l, 1, 1, 1, 2, 1, 4, false, r);
            generateSmoothBox(l, 8, 1, 1, 9, 1, 4, false, r);
            generateBox(l, 1, 1, 1, 1, 1, 3, LAVA, LAVA, false);
            generateBox(l, 9, 1, 1, 9, 1, 3, LAVA, LAVA, false);
            generateSmoothBox(l, 3, 1, 8, 7, 1, 12, false, r);
            generateBox(l, 4, 1, 9, 6, 1, 11, LAVA, LAVA, false);
            int nsBars = bars("north", "south");
            int weBars = bars("west", "east");
            for (int n = 3; n < 14; n += 2) {
                generateBox(l, 0, 3, n, 0, 4, n, nsBars, nsBars, false);
                generateBox(l, 10, 3, n, 10, 4, n, nsBars, nsBars, false);
            }
            for (int n = 2; n < 9; n += 2) {
                generateBox(l, n, 3, 15, n, 4, 15, weBars, weBars, false);
            }
            int stairNorth = stairs("stone_brick_stairs", "north");
            generateSmoothBox(l, 4, 1, 5, 6, 1, 7, false, r);
            generateSmoothBox(l, 4, 2, 6, 6, 2, 7, false, r);
            generateSmoothBox(l, 4, 3, 7, 6, 3, 7, false, r);
            for (int i = 4; i <= 6; i++) {
                place(l, stairNorth, i, 1, 4);
                place(l, stairNorth, i, 2, 5);
                place(l, stairNorth, i, 3, 6);
            }
            int frameN = stairs("end_portal_frame", "north");
            int frameS = stairs("end_portal_frame", "south");
            int frameE = stairs("end_portal_frame", "east");
            int frameW = stairs("end_portal_frame", "west");
            boolean[] eye = new boolean[12];
            boolean all = true;
            for (int i = 0; i < eye.length; i++) {
                eye[i] = r.nextFloat() > 0.9f;
                all &= eye[i];
            }
            place(l, frame(frameN, eye[0]), 4, 3, 8);
            place(l, frame(frameN, eye[1]), 5, 3, 8);
            place(l, frame(frameN, eye[2]), 6, 3, 8);
            place(l, frame(frameS, eye[3]), 4, 3, 12);
            place(l, frame(frameS, eye[4]), 5, 3, 12);
            place(l, frame(frameS, eye[5]), 6, 3, 12);
            place(l, frame(frameE, eye[6]), 3, 3, 9);
            place(l, frame(frameE, eye[7]), 3, 3, 10);
            place(l, frame(frameE, eye[8]), 3, 3, 11);
            place(l, frame(frameW, eye[9]), 7, 3, 9);
            place(l, frame(frameW, eye[10]), 7, 3, 10);
            place(l, frame(frameW, eye[11]), 7, 3, 11);
            if (all) {
                for (int x = 4; x <= 6; x++)
                    for (int z = 9; z <= 11; z++)
                        place(l, END_PORTAL, x, 3, z);
            }
            int sx = worldX(5, 6), sy = worldY(3), sz = worldZ(5, 6);
            if (!hasPlacedSpawner && clipInside(sx, sy, sz)) {
                hasPlacedSpawner = true;
                l.setBlock(sx, sy, sz, BlockStateHelper.getDefault("spawner"));
                l.setBlockEntity(sx, sy, sz, spawnerNbt());
            }
        }
    }

    private static int frame(int base, boolean hasEye) {
        return hasEye ? BlockStateHelper.withProp(base, "eye", "true") : base;
    }

    static class FillerCorridor extends Piece {
        private final int steps;

        FillerCorridor(char dir, BoundingBox box) {
            super(dir, box, 0);
            this.steps = dir == 'N' || dir == 'S' ? box.getSpanZ() : box.getSpanX();
        }

        static BoundingBox findPieceBox(List<Piece> pieces, int x, int y, int z, char dir) {
            BoundingBox bb = orientBox(x, y, z, -1, -1, 0, 5, 5, 4, dir);
            Piece piece = findCollisionPiece(pieces, bb);
            if (piece == null) return null;
            if (piece.box.minY == bb.minY) {
                for (int i = 2; i >= 1; i--) {
                    BoundingBox test = orientBox(x, y, z, -1, -1, 0, 5, 5, i, dir);
                    if (piece.box.intersects(test)) continue;
                    return orientBox(x, y, z, -1, -1, 0, 5, 5, i + 1, dir);
                }
            }
            return null;
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {}

        @Override void build(WorldGenLevel l, RandomSource r) {
            for (int i = 0; i < steps; i++) {
                for (int x = 0; x <= 4; x++) place(l, STONE_BRICKS, x, 0, i);
                for (int y = 1; y <= 3; y++) {
                    place(l, STONE_BRICKS, 0, y, i);
                    place(l, AIR, 1, y, i);
                    place(l, AIR, 2, y, i);
                    place(l, AIR, 3, y, i);
                    place(l, STONE_BRICKS, 4, y, i);
                }
                for (int x = 0; x <= 4; x++) place(l, STONE_BRICKS, x, 4, i);
            }
        }
    }

    public static class StartPiece extends StairsDown {
        PieceWeight previousPiece;
        PortalRoom portalRoomPiece;
        final List<Piece> pendingChildren = new ArrayList<>();
        final List<PieceWeight> currentPieces = freshWeights();
        String imposedPiece;
        int totalWeight;

        StartPiece(RandomSource rnd, int x, int y, int z) {
            this(dirFromIndex(rnd.nextInt(4)), rnd, x, y, z);
        }

        private StartPiece(char dir, RandomSource rnd, int x, int y, int z) {
            super(dir, makeBoundingBox(x, y, z, dir, 5, 11, 5), true);
        }
    }

    // ── BE 工具 ──────────────────────────────────────────────────────

    /** 刷怪笼 BE（SpawnData 用 1.19.3+ entity:{id} 包装）。 */
    private static org.cloudburstmc.nbt.NbtMap spawnerNbt() {
        return org.cloudburstmc.nbt.NbtMap.builder()
            .putString("id", "minecraft:spawner")
            .putCompound("SpawnData", org.cloudburstmc.nbt.NbtMap.builder()
                .putCompound("entity", org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("id", "minecraft:silverfish").build())
                .putString("id", "minecraft:silverfish")
                .build())
            .putInt("MinSpawnDelay", 20).putInt("MaxSpawnDelay", 200)
            .putInt("SpawnCount", 4).putInt("MaxNearbyEntities", 6)
            .putInt("RequiredPlayerRange", 16).putInt("SpawnRange", 4)
            .build();
    }

    private static void createChest(WorldGenLevel level, int x, int y, int z, String lootTable) {
        int chest = BlockStateHelper.getDefault("chest");
        if (chest <= 0) return;
        level.setBlock(x, y, z, chest);
        level.setBlockEntity(x, y, z, org.cloudburstmc.nbt.NbtMap.builder()
            .putString("id", "minecraft:chest")
            .putString("LootTable", lootTable)
            .build());
    }

    // ── 结构入口（原版 StrongholdStructure.generatePieces） ──────────

    /**
     * 返回完整件列表（含 StartPiece）。原版驱动每轮 setLargeFeatureSeed 重试直到
     * PortalRoom 生成；此处以 structureSeed+attempt 等价轮换，上限 50 轮防死循环。
     * 竖直位置以 startY 为锚（原版另走 moveBelowSeaLevel）。
     */
    public static List<Piece> generate(long structureSeed, int startX, int startY, int startZ) {
        LegacyRandomSource rnd = new LegacyRandomSource(structureSeed);
        List<Piece> last = null;
        for (int attempt = 0; attempt < 50; attempt++) {
            rnd.setSeed(structureSeed + attempt);
            StartPiece start = new StartPiece(rnd, startX, startY, startZ);
            List<Piece> pieces = new ArrayList<>();
            pieces.add(start);
            start.addChildren(start, pieces, rnd);
            List<Piece> pending = start.pendingChildren;
            while (!pending.isEmpty()) {
                Piece piece = pending.remove(rnd.nextInt(pending.size()));
                piece.addChildren(start, pieces, rnd);
            }
            last = pieces;
            if (start.portalRoomPiece != null && pieces.size() > 1) break;
        }
        return last;
    }

    /** 原版 StructurePiecesBuilder.moveBelowSeaLevel：顶面落在 [worldMinY+1+ySpan, seaLevel-padding]。 */
    public static void moveBelowSeaLevel(List<Piece> pieces, RandomSource rnd,
                                         int seaLevel, int worldMinY, int padding) {
        BoundingBox total = totalBox(pieces);
        int target = seaLevel - padding;
        int lowerBound = total.getSpanY() + worldMinY + 1;
        if (lowerBound < target) lowerBound += rnd.nextInt(target - lowerBound);
        else lowerBound = Math.min(lowerBound, target);
        int dy = lowerBound - total.maxY;
        for (Piece p : pieces) p.move(0, dy, 0);
    }

    public static BoundingBox totalBox(List<Piece> pieces) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (Piece p : pieces) {
            minX = Math.min(minX, p.box.minX); maxX = Math.max(maxX, p.box.maxX);
            minY = Math.min(minY, p.box.minY); maxY = Math.max(maxY, p.box.maxY);
            minZ = Math.min(minZ, p.box.minZ); maxZ = Math.max(maxZ, p.box.maxZ);
        }
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /** 每区块构建入口：只跑与当前区块相交的件（WorldGenLevel 窗口外写入自动跳过）。 */
    public static void placeInChunk(WorldGenLevel level, List<Piece> pieces,
                                    int chunkMinX, int chunkMinZ) {
        int chunkMaxX = chunkMinX + 15, chunkMaxZ = chunkMinZ + 15;
        RandomSource rnd = new LegacyRandomSource(
            level.getSeed() ^ ((long) chunkMinX * 341873128712L) ^ ((long) chunkMinZ * 132897987541L));
        for (Piece p : pieces) {
            if (p.box.maxX < chunkMinX || p.box.minX > chunkMaxX) continue;
            if (p.box.maxZ < chunkMinZ || p.box.minZ > chunkMaxZ) continue;
            p.build(level, rnd);
        }
    }

    }
