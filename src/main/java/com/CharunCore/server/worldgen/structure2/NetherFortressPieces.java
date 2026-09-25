package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * 原版 NetherFortressPieces / NetherFortressStructure 逐件移植。
 * 件图生成：StartPiece(BridgeCrossing) 在 (chunkX*16+2, 64, chunkZ*16+2)，
 * pendingChildren 随机弹出展开，权重池(bridge/castle)带 maxPlaceCount/allowInRow，
 * 最后整体 moveInsideHeights 到 y 48..70 带。14 种件的 build() 逐行对照原版 postProcess。
 */
public final class NetherFortressPieces {

    private static final int MAX_DEPTH = 30;
    private static final int MAGIC_START_Y = 64;

    private NetherFortressPieces() {}

    // ── 方块 id ──────────────────────────────────────────────────────
    private static final int BRICKS = BlockStateHelper.getDefault("nether_bricks");
    private static final int AIR = 0;
    private static final int FENCE_N = fence("north");
    private static final int FENCE_S = fence("south");
    private static final int FENCE_E = fence("east");
    private static final int FENCE_W = fence("west");
    private static final int FENCE_NS = BlockStateHelper.withProp(FENCE_N, "south", "true");
    private static final int FENCE_WE = BlockStateHelper.withProp(FENCE_W, "east", "true");
    private static final int FENCE_NS_E = BlockStateHelper.withProp(FENCE_NS, "east", "true");
    private static final int FENCE_NS_W = BlockStateHelper.withProp(FENCE_NS, "west", "true");
    private static final int FENCE_WE_N = BlockStateHelper.withProp(FENCE_WE, "north", "true");
    private static final int FENCE_WE_S = BlockStateHelper.withProp(FENCE_WE, "south", "true");

    private static int fence(String side) {
        return BlockStateHelper.withProp(BlockStateHelper.getDefault("nether_brick_fence"), side, "true");
    }

    private static int stairsFacing(String facing) {
        return BlockStateHelper.withProp(BlockStateHelper.getDefault("nether_brick_stairs"), "facing", facing);
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

    public abstract static class Piece {
        BoundingBox box;
        char dir;
        int genDepth;
        int selfSeed;

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
                case 'S' -> box.minZ + z;
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
                        if (onlyAir) {
                            int wx = worldX(x, z), wy = worldY(y), wz = worldZ(x, z);
                            if (level.getBlock(wx, wy, wz) != AIR) continue;
                        }
                        boolean shell = x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2;
                        place(level, shell ? outer : inner, x, y, z);
                    }
                }
            }
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

        /** 与已放置片段包围盒相交检测（原版 findCollisionPiece）。 */
        static boolean collides(List<Piece> pieces, BoundingBox bb) {
            for (Piece p : pieces) {
                if (p.box.intersects(bb)) return true;
            }
            return false;
        }
    }

    // ── 权重引擎 ─────────────────────────────────────────────────────
    static final class PieceWeight {
        final String pieceClass;
        final int weight;
        final int maxPlaceCount;
        final boolean allowInRow;
        int placeCount;

        PieceWeight(String cls, int weight, int maxPlaceCount, boolean allowInRow) {
            this.pieceClass = cls;
            this.weight = weight;
            this.maxPlaceCount = maxPlaceCount;
            this.allowInRow = allowInRow;
        }

        boolean doPlace(int depth) {
            return maxPlaceCount == 0 || placeCount < maxPlaceCount;
        }

        boolean isValid() {
            return maxPlaceCount == 0 || placeCount < maxPlaceCount;
        }
    }

    private static final PieceWeight[] BRIDGE_WEIGHTS = {
        new PieceWeight("BridgeStraight", 30, 0, true),
        new PieceWeight("BridgeCrossing", 10, 4, false),
        new PieceWeight("RoomCrossing", 10, 4, false),
        new PieceWeight("StairsRoom", 10, 3, false),
        new PieceWeight("MonsterThrone", 5, 2, false),
        new PieceWeight("CastleEntrance", 5, 1, false)
    };

    private static final PieceWeight[] CASTLE_WEIGHTS = {
        new PieceWeight("CastleSmallCorridorPiece", 25, 0, true),
        new PieceWeight("CastleSmallCorridorCrossingPiece", 15, 5, false),
        new PieceWeight("CastleSmallCorridorRightTurnPiece", 5, 10, false),
        new PieceWeight("CastleSmallCorridorLeftTurnPiece", 5, 10, false),
        new PieceWeight("CastleCorridorStairsPiece", 10, 3, true),
        new PieceWeight("CastleCorridorTBalconyPiece", 7, 2, false),
        new PieceWeight("CastleStalkRoom", 5, 2, false)
    };

    public static final class StartPiece extends BridgeCrossing {
        PieceWeight previousPiece;
        final List<PieceWeight> availableBridgePieces = new ArrayList<>();
        final List<PieceWeight> availableCastlePieces = new ArrayList<>();
        final List<Piece> pendingChildren = new ArrayList<>();

        StartPiece(RandomSource rnd, int x, int z) {
            this(rnd.nextInt(4), rnd, x, z);
        }

        private StartPiece(int dirIdx, RandomSource rnd, int x, int z) {
            super(dirFromIndex(dirIdx),
                makeBoundingBox(x, MAGIC_START_Y, z, dirFromIndex(dirIdx), 19, 10, 19),
                0);
            for (PieceWeight pw : BRIDGE_WEIGHTS) { pw.placeCount = 0; availableBridgePieces.add(pw); }
            for (PieceWeight pw : CASTLE_WEIGHTS) { pw.placeCount = 0; availableCastlePieces.add(pw); }
        }
    }

    private static char getRandomHorizontalDirection(RandomSource rnd) {
        return dirFromIndex(rnd.nextInt(4));
    }

    /** 原版 Direction.from2DDataValue：0=SOUTH 1=WEST 2=NORTH 3=EAST。 */
    private static char dirFromIndex(int idx) {
        return switch (idx) { case 0 -> 'S'; case 1 -> 'W'; case 2 -> 'N'; default -> 'E'; };
    }

    /** 原版 StructurePiece.makeBoundingBox(x,y,z,dir,w,h,d)。 */
    private static BoundingBox makeBoundingBox(int x, int y, int z, char dir, int w, int h, int d) {
        return switch (dir) {
            case 'N' -> new BoundingBox(x - w + 1, y, z, x, y + h - 1, z + d - 1);
            case 'W' -> new BoundingBox(x, y, z - d + 1, x + w - 1, y + h - 1, z);
            case 'E' -> new BoundingBox(x, y, z, x + d - 1, y + h - 1, z + w - 1);
            default -> new BoundingBox(x, y, z, x + w - 1, y + h - 1, z + d - 1);
        };
    }

    private static int updatePieceWeight(List<PieceWeight> list) {
        boolean anyValid = false;
        int total = 0;
        for (PieceWeight pw : list) {
            if (pw.maxPlaceCount > 0 && pw.placeCount < pw.maxPlaceCount) anyValid = true;
            total += pw.weight;
        }
        return anyValid ? total : -1;
    }

    private static Piece createPieceByWeight(PieceWeight pw, List<Piece> pieces, RandomSource rnd,
                                             int x, int y, int z, char dir, int depth) {
        return switch (pw.pieceClass) {
            case "BridgeStraight" -> BridgeStraight.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "BridgeCrossing" -> BridgeCrossing.createPiece(pieces, x, y, z, dir, depth);
            case "RoomCrossing" -> RoomCrossing.createPiece(pieces, x, y, z, dir, depth);
            case "StairsRoom" -> StairsRoom.createPiece(pieces, x, y, z, depth, dir);
            case "MonsterThrone" -> MonsterThrone.createPiece(pieces, x, y, z, depth, dir);
            case "CastleEntrance" -> CastleEntrance.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "CastleSmallCorridorPiece" -> CastleSmallCorridor.createPiece(pieces, x, y, z, dir, depth);
            case "CastleSmallCorridorRightTurnPiece" -> CastleRightTurn.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "CastleSmallCorridorLeftTurnPiece" -> CastleLeftTurn.createPiece(pieces, rnd, x, y, z, dir, depth);
            case "CastleCorridorStairsPiece" -> CastleCorridorStairs.createPiece(pieces, x, y, z, dir, depth);
            case "CastleCorridorTBalconyPiece" -> CastleTBalcony.createPiece(pieces, x, y, z, dir, depth);
            case "CastleSmallCorridorCrossingPiece" -> CastleSmallCrossing.createPiece(pieces, x, y, z, dir, depth);
            case "CastleStalkRoom" -> CastleStalkRoom.createPiece(pieces, x, y, z, dir, depth);
            default -> null;
        };
    }

    private static Piece generatePiece(StartPiece start, List<PieceWeight> list, List<Piece> pieces,
                                       RandomSource rnd, int x, int y, int z, char dir, int depth) {
        int total = updatePieceWeight(list);
        boolean go = total > 0 && depth <= MAX_DEPTH;
        int tries = 0;
        while (tries++ < 5 && go) {
            int roll = rnd.nextInt(total);
            for (PieceWeight pw : list) {
                roll -= pw.weight;
                if (roll < 0) {
                    if (!pw.doPlace(depth) || (pw == start.previousPiece && !pw.allowInRow)) break;
                    Piece piece = createPieceByWeight(pw, pieces, rnd, x, y, z, dir, depth);
                    if (piece != null) {
                        pw.placeCount++;
                        start.previousPiece = pw;
                        if (!pw.isValid()) list.remove(pw);
                        return piece;
                    }
                }
            }
        }
        return BridgeEndFiller.createPiece(pieces, rnd, x, y, z, dir, depth);
    }

    private static Piece doPlace(PieceWeight ignored, StartPiece start, List<Piece> pieces, RandomSource rnd,
                                 int x, int y, int z, char dir, int depth, boolean castle) {
        if (Math.abs(x - start.box.minX) > 112 || Math.abs(z - start.box.minZ) > 112) {
            return BridgeEndFiller.createPiece(pieces, rnd, x, y, z, dir, depth);
        }
        List<PieceWeight> pool = castle ? start.availableCastlePieces : start.availableBridgePieces;
        Piece piece = generatePiece(start, pool, pieces, rnd, x, y, z, dir, depth + 1);
        if (piece != null) {
            pieces.add(piece);
            start.pendingChildren.add(piece);
        }
        return piece;
    }

    private static Piece generateChildForward(StartPiece start, List<Piece> pieces, RandomSource rnd,
                                              int ox, int oy, boolean castle, Piece self) {
        return switch (self.dir) {
            case 'N' -> doPlace(null, start, pieces, rnd, self.box.minX + ox, self.box.minY + oy, self.box.minZ - 1, self.dir, self.genDepth, castle);
            case 'S' -> doPlace(null, start, pieces, rnd, self.box.minX + ox, self.box.minY + oy, self.box.maxZ + 1, self.dir, self.genDepth, castle);
            case 'W' -> doPlace(null, start, pieces, rnd, self.box.minX - 1, self.box.minY + oy, self.box.minZ + ox, self.dir, self.genDepth, castle);
            case 'E' -> doPlace(null, start, pieces, rnd, self.box.maxX + 1, self.box.minY + oy, self.box.minZ + ox, self.dir, self.genDepth, castle);
            default -> null;
        };
    }

    private static Piece generateChildLeft(StartPiece start, List<Piece> pieces, RandomSource rnd,
                                           int oy, int ox, boolean castle, Piece self) {
        return switch (self.dir) {
            case 'N', 'S' -> doPlace(null, start, pieces, rnd, self.box.minX - 1, self.box.minY + oy, self.box.minZ + ox, 'W', self.genDepth, castle);
            case 'W', 'E' -> doPlace(null, start, pieces, rnd, self.box.minX + ox, self.box.minY + oy, self.box.minZ - 1, 'N', self.genDepth, castle);
            default -> null;
        };
    }

    private static Piece generateChildRight(StartPiece start, List<Piece> pieces, RandomSource rnd,
                                            int oy, int ox, boolean castle, Piece self) {
        return switch (self.dir) {
            case 'N', 'S' -> doPlace(null, start, pieces, rnd, self.box.maxX + 1, self.box.minY + oy, self.box.minZ + ox, 'E', self.genDepth, castle);
            case 'W', 'E' -> doPlace(null, start, pieces, rnd, self.box.minX + ox, self.box.minY + oy, self.box.maxZ + 1, 'S', self.genDepth, castle);
            default -> null;
        };
    }

    private static boolean isOkBox(BoundingBox bb) { return bb.minY > 10; }

    // ── 具体件 ───────────────────────────────────────────────────────

    static class BridgeStraight extends Piece {
        BridgeStraight(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 3, false, this);
        }

        static BridgeStraight createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -3, 0, 5, 10, 19, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new BridgeStraight(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 3, 0, 4, 4, 18, BRICKS, BRICKS, false);
            generateBox(l, 1, 5, 0, 3, 7, 18, AIR, AIR, false);
            generateBox(l, 0, 5, 0, 0, 5, 18, BRICKS, BRICKS, false);
            generateBox(l, 4, 5, 0, 4, 5, 18, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 4, 2, 5, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 13, 4, 2, 18, BRICKS, BRICKS, false);
            generateBox(l, 0, 0, 0, 4, 1, 3, BRICKS, BRICKS, false);
            generateBox(l, 0, 0, 15, 4, 1, 18, BRICKS, BRICKS, false);
            for (int i = 0; i <= 4; i++)
                for (int k = 0; k <= 2; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, i, -1, 18 - k);
                }
            generateBox(l, 0, 1, 1, 0, 4, 1, FENCE_WE, FENCE_WE, false);
            generateBox(l, 0, 3, 4, 0, 4, 4, FENCE_WE, FENCE_WE, false);
            generateBox(l, 0, 3, 14, 0, 4, 14, FENCE_WE, FENCE_WE, false);
            generateBox(l, 0, 1, 17, 0, 4, 17, FENCE_WE, FENCE_WE, false);
            generateBox(l, 4, 1, 1, 4, 4, 1, FENCE_W, FENCE_W, false);
            generateBox(l, 4, 3, 4, 4, 4, 4, FENCE_W, FENCE_W, false);
            generateBox(l, 4, 3, 14, 4, 4, 14, FENCE_W, FENCE_W, false);
            generateBox(l, 4, 1, 17, 4, 4, 17, FENCE_W, FENCE_W, false);
        }
    }

    static class BridgeEndFiller extends Piece {
        BridgeEndFiller(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.selfSeed = rnd.nextInt();
        }

        static BridgeEndFiller createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -3, 0, 5, 10, 8, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new BridgeEndFiller(rnd, dir, bb, depth);
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {}

        @Override void build(WorldGenLevel l, RandomSource r) {
            LegacyRandomSource random = new LegacyRandomSource(selfSeed & 0xFFFFFFFFL);
            for (int i = 0; i <= 4; i++)
                for (int b = 3; b <= 4; b++) {
                    int j = random.nextInt(8);
                    generateBox(l, i, b, 0, i, b, j, BRICKS, BRICKS, false);
                }
            int i = random.nextInt(8);
            generateBox(l, 0, 5, 0, 0, 5, i, BRICKS, BRICKS, false);
            i = random.nextInt(8);
            generateBox(l, 4, 5, 0, 4, 5, i, BRICKS, BRICKS, false);
            for (int k = 0; k <= 4; k++) {
                int j = random.nextInt(5);
                generateBox(l, k, 2, 0, k, 2, j, BRICKS, BRICKS, false);
            }
            for (int k = 0; k <= 4; k++)
                for (int b = 0; b <= 1; b++) {
                    int j = random.nextInt(3);
                    generateBox(l, k, b, 0, k, b, j, BRICKS, BRICKS, false);
                }
        }
    }

    static class BridgeCrossing extends Piece {
        BridgeCrossing(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 8, 3, false, this);
            generateChildLeft(s, p, r, 3, 8, false, this);
            generateChildRight(s, p, r, 3, 8, false, this);
        }

        static BridgeCrossing createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -8, -3, 0, 19, 10, 19, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new BridgeCrossing(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 7, 3, 0, 11, 4, 18, BRICKS, BRICKS, false);
            generateBox(l, 0, 3, 7, 18, 4, 11, BRICKS, BRICKS, false);
            generateBox(l, 8, 5, 0, 10, 7, 18, AIR, AIR, false);
            generateBox(l, 0, 5, 8, 18, 7, 10, AIR, AIR, false);
            generateBox(l, 7, 5, 0, 7, 5, 7, BRICKS, BRICKS, false);
            generateBox(l, 7, 5, 11, 7, 5, 18, BRICKS, BRICKS, false);
            generateBox(l, 11, 5, 0, 11, 5, 7, BRICKS, BRICKS, false);
            generateBox(l, 11, 5, 11, 11, 5, 18, BRICKS, BRICKS, false);
            generateBox(l, 0, 5, 7, 7, 5, 7, BRICKS, BRICKS, false);
            generateBox(l, 11, 5, 7, 18, 5, 7, BRICKS, BRICKS, false);
            generateBox(l, 0, 5, 11, 7, 5, 11, BRICKS, BRICKS, false);
            generateBox(l, 11, 5, 11, 18, 5, 11, BRICKS, BRICKS, false);
            generateBox(l, 7, 2, 0, 11, 2, 5, BRICKS, BRICKS, false);
            generateBox(l, 7, 2, 13, 11, 2, 18, BRICKS, BRICKS, false);
            generateBox(l, 7, 0, 0, 11, 1, 3, BRICKS, BRICKS, false);
            generateBox(l, 7, 0, 15, 11, 1, 18, BRICKS, BRICKS, false);
            for (int i = 7; i <= 11; i++)
                for (int k = 0; k <= 2; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, i, -1, 18 - k);
                }
            generateBox(l, 0, 2, 7, 5, 2, 11, BRICKS, BRICKS, false);
            generateBox(l, 13, 2, 7, 18, 2, 11, BRICKS, BRICKS, false);
            generateBox(l, 0, 0, 7, 3, 1, 11, BRICKS, BRICKS, false);
            generateBox(l, 15, 0, 7, 18, 1, 11, BRICKS, BRICKS, false);
            for (int i = 0; i <= 2; i++)
                for (int k = 7; k <= 11; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, 18 - i, -1, k);
                }
        }
    }

    static class RoomCrossing extends Piece {
        RoomCrossing(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 2, 0, false, this);
            generateChildLeft(s, p, r, 0, 2, false, this);
            generateChildRight(s, p, r, 0, 2, false, this);
        }

        static RoomCrossing createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -2, 0, 0, 7, 9, 7, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new RoomCrossing(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 6, 1, 6, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 6, 7, 6, AIR, AIR, false);
            generateBox(l, 0, 2, 0, 1, 6, 0, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 6, 1, 6, 6, BRICKS, BRICKS, false);
            generateBox(l, 5, 2, 0, 6, 6, 0, BRICKS, BRICKS, false);
            generateBox(l, 5, 2, 6, 6, 6, 6, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 0, 6, 1, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 5, 0, 6, 6, BRICKS, BRICKS, false);
            generateBox(l, 6, 2, 0, 6, 6, 1, BRICKS, BRICKS, false);
            generateBox(l, 6, 2, 5, 6, 6, 6, BRICKS, BRICKS, false);
            generateBox(l, 2, 6, 0, 4, 6, 0, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 0, 4, 5, 0, FENCE_WE, FENCE_WE, false);
            generateBox(l, 2, 6, 6, 4, 6, 6, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 6, 4, 5, 6, FENCE_WE, FENCE_WE, false);
            generateBox(l, 0, 6, 2, 0, 6, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 5, 2, 0, 5, 4, FENCE_NS, FENCE_NS, false);
            generateBox(l, 6, 6, 2, 6, 6, 4, BRICKS, BRICKS, false);
            generateBox(l, 6, 5, 2, 6, 5, 4, FENCE_NS, FENCE_NS, false);
            for (int i = 0; i <= 6; i++)
                for (int k = 0; k <= 6; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class StairsRoom extends Piece {
        StairsRoom(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildRight(s, p, r, 6, 2, false, this);
        }

        static StairsRoom createPiece(List<Piece> pieces, int x, int y, int z, int depth, char dir) {
            BoundingBox bb = orientBox(x, y, z, -2, 0, 0, 7, 11, 7, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new StairsRoom(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 6, 1, 6, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 6, 10, 6, AIR, AIR, false);
            generateBox(l, 0, 2, 0, 1, 8, 0, BRICKS, BRICKS, false);
            generateBox(l, 5, 2, 0, 6, 8, 0, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 1, 0, 8, 6, BRICKS, BRICKS, false);
            generateBox(l, 6, 2, 1, 6, 8, 6, BRICKS, BRICKS, false);
            generateBox(l, 1, 2, 6, 5, 8, 6, BRICKS, BRICKS, false);
            generateBox(l, 0, 3, 2, 0, 5, 4, FENCE_NS, FENCE_NS, false);
            generateBox(l, 6, 3, 2, 6, 5, 2, FENCE_NS, FENCE_NS, false);
            generateBox(l, 6, 3, 4, 6, 5, 4, FENCE_NS, FENCE_NS, false);
            place(l, BRICKS, 5, 2, 5);
            generateBox(l, 4, 2, 5, 4, 3, 5, BRICKS, BRICKS, false);
            generateBox(l, 3, 2, 5, 3, 4, 5, BRICKS, BRICKS, false);
            generateBox(l, 2, 2, 5, 2, 5, 5, BRICKS, BRICKS, false);
            generateBox(l, 1, 2, 5, 1, 6, 5, BRICKS, BRICKS, false);
            generateBox(l, 1, 7, 1, 5, 7, 4, BRICKS, BRICKS, false);
            generateBox(l, 6, 8, 2, 6, 8, 4, AIR, AIR, false);
            generateBox(l, 2, 6, 0, 4, 8, 0, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 0, 4, 5, 0, FENCE_WE, FENCE_WE, false);
            for (int i = 0; i <= 6; i++)
                for (int k = 0; k <= 6; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class MonsterThrone extends Piece {
        private boolean hasPlacedSpawner;

        MonsterThrone(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {}

        static MonsterThrone createPiece(List<Piece> pieces, int x, int y, int z, int depth, char dir) {
            BoundingBox bb = orientBox(x, y, z, -2, 0, 0, 7, 8, 9, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new MonsterThrone(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 2, 0, 6, 7, 7, AIR, AIR, false);
            generateBox(l, 1, 0, 0, 5, 1, 7, BRICKS, BRICKS, false);
            generateBox(l, 1, 2, 1, 5, 2, 7, BRICKS, BRICKS, false);
            generateBox(l, 1, 3, 2, 5, 3, 7, BRICKS, BRICKS, false);
            generateBox(l, 1, 4, 3, 5, 4, 7, BRICKS, BRICKS, false);
            generateBox(l, 1, 2, 0, 1, 4, 2, BRICKS, BRICKS, false);
            generateBox(l, 5, 2, 0, 5, 4, 2, BRICKS, BRICKS, false);
            generateBox(l, 1, 5, 2, 1, 5, 3, BRICKS, BRICKS, false);
            generateBox(l, 5, 5, 2, 5, 5, 3, BRICKS, BRICKS, false);
            generateBox(l, 0, 5, 3, 0, 5, 8, BRICKS, BRICKS, false);
            generateBox(l, 6, 5, 3, 6, 5, 8, BRICKS, BRICKS, false);
            generateBox(l, 1, 5, 8, 5, 5, 8, BRICKS, BRICKS, false);
            place(l, FENCE_W, 1, 6, 3);
            place(l, FENCE_E, 5, 6, 3);
            place(l, FENCE_NS_E, 0, 6, 3);
            place(l, FENCE_NS_W, 6, 6, 3);
            generateBox(l, 0, 6, 4, 0, 6, 7, FENCE_NS, FENCE_NS, false);
            generateBox(l, 6, 6, 4, 6, 6, 7, FENCE_NS, FENCE_NS, false);
            place(l, FENCE_WE_S, 0, 6, 8);
            place(l, FENCE_WE_N, 6, 6, 8);
            generateBox(l, 1, 6, 8, 5, 6, 8, FENCE_WE, FENCE_WE, false);
            place(l, FENCE_E, 1, 7, 8);
            generateBox(l, 2, 7, 8, 4, 7, 8, FENCE_WE, FENCE_WE, false);
            place(l, FENCE_W, 5, 7, 8);
            place(l, FENCE_E, 2, 8, 8);
            place(l, FENCE_WE, 3, 8, 8);
            place(l, FENCE_W, 4, 8, 8);
            int sx = worldX(3, 5), sy = worldY(5), sz = worldZ(3, 5);
            if (!hasPlacedSpawner && clipInside(sx, sy, sz)) {
                hasPlacedSpawner = true;
                l.setBlock(sx, sy, sz, BlockStateHelper.getDefault("spawner"));
                l.setBlockEntity(sx, sy, sz, spawnerNbt(sx, sy, sz));
            }
            for (int i = 0; i <= 6; i++)
                for (int k = 0; k <= 6; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class CastleEntrance extends Piece {
        CastleEntrance(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 5, 3, true, this);
        }

        static CastleEntrance createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -5, -3, 0, 13, 14, 13, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleEntrance(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 3, 0, 12, 4, 12, BRICKS, BRICKS, false);
            generateBox(l, 0, 5, 0, 12, 13, 12, AIR, AIR, false);
            generateBox(l, 0, 5, 0, 1, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 11, 5, 0, 12, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 11, 4, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 8, 5, 11, 10, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 5, 9, 11, 7, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 0, 4, 12, 1, BRICKS, BRICKS, false);
            generateBox(l, 8, 5, 0, 10, 12, 1, BRICKS, BRICKS, false);
            generateBox(l, 5, 9, 0, 7, 12, 1, BRICKS, BRICKS, false);
            generateBox(l, 2, 11, 2, 10, 12, 10, BRICKS, BRICKS, false);
            generateBox(l, 5, 8, 0, 7, 8, 0, BlockStateHelper.getDefault("nether_brick_fence"),
                BlockStateHelper.getDefault("nether_brick_fence"), false);
            int plain = BlockStateHelper.getDefault("nether_brick_fence");
            for (int i = 1; i <= 11; i += 2) {
                generateBox(l, i, 10, 0, i, 11, 0, FENCE_WE, FENCE_WE, false);
                generateBox(l, i, 10, 12, i, 11, 12, FENCE_WE, FENCE_WE, false);
                generateBox(l, 0, 10, i, 0, 11, i, FENCE_NS, FENCE_NS, false);
                generateBox(l, 12, 10, i, 12, 11, i, FENCE_NS, FENCE_NS, false);
                place(l, BRICKS, i, 13, 0);
                place(l, BRICKS, i, 13, 12);
                place(l, BRICKS, 0, 13, i);
                place(l, BRICKS, 12, 13, i);
                if (i != 11) {
                    place(l, FENCE_WE, i + 1, 13, 0);
                    place(l, FENCE_WE, i + 1, 13, 12);
                    place(l, FENCE_NS, 0, 13, i + 1);
                    place(l, FENCE_NS, 12, 13, i + 1);
                }
            }
            place(l, FENCE_NS_E, 0, 13, 0);
            place(l, FENCE_WE_S, 0, 13, 12);
            place(l, FENCE_WE_N, 12, 13, 0);
            place(l, plain, 12, 13, 12);
            int nsW = BlockStateHelper.withProp(FENCE_NS, "west", "true");
            int nsE = BlockStateHelper.withProp(FENCE_NS, "east", "true");
            for (int i = 3; i <= 9; i += 2) {
                generateBox(l, 1, 7, i, 1, 8, i, nsW, nsW, false);
                generateBox(l, 11, 7, i, 11, 8, i, nsE, nsE, false);
            }
            int stairN = stairsFacing("north");
            for (int b = 0; b <= 6; b++) {
                int i = b + 4;
                for (int k = 5; k <= 7; k++) place(l, stairN, k, 5 + b, i);
                if (i >= 5 && i <= 8) generateBox(l, 5, 5, i, 7, b + 4, i, BRICKS, BRICKS, false);
                else if (i >= 9 && i <= 10) generateBox(l, 5, 8, i, 7, b + 4, i, BRICKS, BRICKS, false);
                if (b >= 1) generateBox(l, 5, 6 + b, i, 7, 9 + b, i, AIR, AIR, false);
            }
            for (int k = 5; k <= 7; k++) place(l, stairN, k, 12, 11);
            generateBox(l, 5, 6, 7, 5, 7, 7, nsE, nsE, false);
            generateBox(l, 7, 6, 7, 7, 7, 7, nsW, nsW, false);
            generateBox(l, 5, 13, 12, 7, 13, 12, AIR, AIR, false);
            generateBox(l, 2, 5, 2, 3, 5, 3, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 9, 3, 5, 10, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 4, 2, 5, 8, BRICKS, BRICKS, false);
            generateBox(l, 9, 5, 2, 10, 5, 3, BRICKS, BRICKS, false);
            generateBox(l, 9, 5, 9, 10, 5, 10, BRICKS, BRICKS, false);
            generateBox(l, 10, 5, 4, 10, 5, 8, BRICKS, BRICKS, false);
            int stairE = stairsFacing("east");
            int stairW = stairsFacing("west");
            place(l, stairW, 4, 5, 2); place(l, stairW, 4, 5, 3);
            place(l, stairW, 4, 5, 9); place(l, stairW, 4, 5, 10);
            place(l, stairE, 8, 5, 2); place(l, stairE, 8, 5, 3);
            place(l, stairE, 8, 5, 9); place(l, stairE, 8, 5, 10);
            generateBox(l, 3, 4, 4, 4, 4, 8, BlockStateHelper.getDefault("soul_sand"),
                BlockStateHelper.getDefault("soul_sand"), false);
            generateBox(l, 8, 4, 4, 9, 4, 8, BlockStateHelper.getDefault("soul_sand"),
                BlockStateHelper.getDefault("soul_sand"), false);
            generateBox(l, 3, 5, 4, 4, 5, 8, BlockStateHelper.getDefault("nether_wart"),
                BlockStateHelper.getDefault("nether_wart"), false);
            generateBox(l, 8, 5, 4, 9, 5, 8, BlockStateHelper.getDefault("nether_wart"),
                BlockStateHelper.getDefault("nether_wart"), false);
            generateBox(l, 4, 2, 0, 8, 2, 12, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 4, 12, 2, 8, BRICKS, BRICKS, false);
            generateBox(l, 4, 0, 0, 8, 1, 3, BRICKS, BRICKS, false);
            generateBox(l, 4, 0, 9, 8, 1, 12, BRICKS, BRICKS, false);
            generateBox(l, 0, 0, 4, 3, 1, 8, BRICKS, BRICKS, false);
            generateBox(l, 9, 0, 4, 12, 1, 8, BRICKS, BRICKS, false);
            for (int i = 4; i <= 8; i++)
                for (int k = 0; k <= 2; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, i, -1, 12 - k);
                }
            for (int i = 0; i <= 2; i++)
                for (int k = 4; k <= 8; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, 12 - i, -1, k);
                }
        }
    }

    static class CastleSmallCorridor extends Piece {
        CastleSmallCorridor(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 0, true, this);
        }

        static CastleSmallCorridor createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, 0, 0, 5, 7, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleSmallCorridor(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 4, 1, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 4, 5, 4, AIR, AIR, false);
            generateBox(l, 0, 2, 0, 0, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 4, 2, 0, 4, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 3, 1, 0, 4, 1, FENCE_NS, FENCE_NS, false);
            generateBox(l, 0, 3, 3, 0, 4, 3, FENCE_NS, FENCE_NS, false);
            generateBox(l, 4, 3, 1, 4, 4, 1, FENCE_NS, FENCE_NS, false);
            generateBox(l, 4, 3, 3, 4, 4, 3, FENCE_NS, FENCE_NS, false);
            generateBox(l, 0, 6, 0, 4, 6, 4, BRICKS, BRICKS, false);
            for (int i = 0; i <= 4; i++)
                for (int k = 0; k <= 4; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class CastleSmallCrossing extends Piece {
        CastleSmallCrossing(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 0, true, this);
            generateChildLeft(s, p, r, 0, 1, true, this);
            generateChildRight(s, p, r, 0, 1, true, this);
        }

        static CastleSmallCrossing createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, 0, 0, 5, 7, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleSmallCrossing(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 4, 1, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 4, 5, 4, AIR, AIR, false);
            generateBox(l, 0, 2, 0, 0, 5, 0, BRICKS, BRICKS, false);
            generateBox(l, 4, 2, 0, 4, 5, 0, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 4, 0, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 4, 2, 4, 4, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 6, 0, 4, 6, 4, BRICKS, BRICKS, false);
            for (int i = 0; i <= 4; i++)
                for (int k = 0; k <= 4; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class CastleRightTurn extends Piece {
        private boolean needsChest;

        CastleRightTurn(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.needsChest = rnd.nextInt(3) == 0;
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildRight(s, p, r, 0, 1, true, this);
        }

        static CastleRightTurn createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, 0, 0, 5, 7, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleRightTurn(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 4, 1, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 4, 5, 4, AIR, AIR, false);
            generateBox(l, 0, 2, 0, 0, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 3, 1, 0, 4, 1, FENCE_NS, FENCE_NS, false);
            generateBox(l, 0, 3, 3, 0, 4, 3, FENCE_NS, FENCE_NS, false);
            generateBox(l, 4, 2, 0, 4, 5, 0, BRICKS, BRICKS, false);
            generateBox(l, 1, 2, 4, 4, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 1, 3, 4, 1, 4, 4, FENCE_WE, FENCE_WE, false);
            generateBox(l, 3, 3, 4, 3, 4, 4, FENCE_WE, FENCE_WE, false);
            int cx = worldX(1, 3), cy = worldY(2), cz = worldZ(1, 3);
            if (needsChest && clipInside(cx, cy, cz)) {
                needsChest = false;
                placeChest(l, cx, cy, cz);
            }
            generateBox(l, 0, 6, 0, 4, 6, 4, BRICKS, BRICKS, false);
            for (int i = 0; i <= 4; i++)
                for (int k = 0; k <= 4; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class CastleLeftTurn extends Piece {
        private boolean needsChest;

        CastleLeftTurn(RandomSource rnd, char dir, BoundingBox box, int depth) {
            super(dir, box, depth);
            this.needsChest = rnd.nextInt(3) == 0;
        }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildLeft(s, p, r, 0, 1, true, this);
        }

        static CastleLeftTurn createPiece(List<Piece> pieces, RandomSource rnd, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, 0, 0, 5, 7, 5, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleLeftTurn(rnd, dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 4, 1, 4, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 4, 5, 4, AIR, AIR, false);
            generateBox(l, 4, 2, 0, 4, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 4, 3, 1, 4, 4, 1, FENCE_NS, FENCE_NS, false);
            generateBox(l, 4, 3, 3, 4, 4, 3, FENCE_NS, FENCE_NS, false);
            generateBox(l, 0, 2, 0, 0, 5, 0, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 4, 3, 5, 4, BRICKS, BRICKS, false);
            generateBox(l, 1, 3, 4, 1, 4, 4, FENCE_WE, FENCE_WE, false);
            generateBox(l, 3, 3, 4, 3, 4, 4, FENCE_WE, FENCE_WE, false);
            int cx = worldX(3, 3), cy = worldY(2), cz = worldZ(3, 3);
            if (needsChest && clipInside(cx, cy, cz)) {
                needsChest = false;
                placeChest(l, cx, cy, cz);
            }
            generateBox(l, 0, 6, 0, 4, 6, 4, BRICKS, BRICKS, false);
            for (int i = 0; i <= 4; i++)
                for (int k = 0; k <= 4; k++) fillColumnDown(l, BRICKS, i, -1, k);
        }
    }

    static class CastleCorridorStairs extends Piece {
        CastleCorridorStairs(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 1, 0, true, this);
        }

        static CastleCorridorStairs createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -1, -7, 0, 5, 14, 10, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleCorridorStairs(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            int stairS = stairsFacing("south");
            for (int b = 0; b <= 9; b++) {
                int i = Math.max(1, 7 - b);
                int j = Math.min(Math.max(i + 5, 14 - b), 13);
                generateBox(l, 0, 0, b, 4, i, b, BRICKS, BRICKS, false);
                generateBox(l, 1, i + 1, b, 3, j - 1, b, AIR, AIR, false);
                if (b <= 6) {
                    place(l, stairS, 1, i + 1, b);
                    place(l, stairS, 2, i + 1, b);
                    place(l, stairS, 3, i + 1, b);
                }
                generateBox(l, 0, j, b, 4, j, b, BRICKS, BRICKS, false);
                generateBox(l, 0, i + 1, b, 0, j - 1, b, BRICKS, BRICKS, false);
                generateBox(l, 4, i + 1, b, 4, j - 1, b, BRICKS, BRICKS, false);
                if ((b & 1) == 0) {
                    generateBox(l, 0, i + 2, b, 0, i + 3, b, FENCE_NS, FENCE_NS, false);
                    generateBox(l, 4, i + 2, b, 4, i + 3, b, FENCE_NS, FENCE_NS, false);
                }
                for (int k = 0; k <= 4; k++) fillColumnDown(l, BRICKS, k, -1, b);
            }
        }
    }

    static class CastleTBalcony extends Piece {
        CastleTBalcony(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            int b = (dir == 'W' || dir == 'N') ? 5 : 1;
            generateChildLeft(s, p, r, 0, b, r.nextInt(8) > 0, this);
            generateChildRight(s, p, r, 0, b, r.nextInt(8) > 0, this);
        }

        static CastleTBalcony createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -3, 0, 0, 9, 7, 9, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleTBalcony(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 0, 0, 8, 1, 8, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 8, 5, 8, AIR, AIR, false);
            generateBox(l, 0, 6, 0, 8, 6, 5, BRICKS, BRICKS, false);
            generateBox(l, 0, 2, 0, 2, 5, 0, BRICKS, BRICKS, false);
            generateBox(l, 6, 2, 0, 8, 5, 0, BRICKS, BRICKS, false);
            generateBox(l, 1, 3, 0, 1, 4, 0, FENCE_WE, FENCE_WE, false);
            generateBox(l, 7, 3, 0, 7, 4, 0, FENCE_WE, FENCE_WE, false);
            generateBox(l, 0, 2, 4, 8, 2, 8, BRICKS, BRICKS, false);
            generateBox(l, 1, 1, 4, 2, 2, 4, AIR, AIR, false);
            generateBox(l, 6, 1, 4, 7, 2, 4, AIR, AIR, false);
            generateBox(l, 1, 3, 8, 7, 3, 8, FENCE_WE, FENCE_WE, false);
            place(l, FENCE_WE_S, 0, 3, 8);
            place(l, FENCE_WE_N, 8, 3, 8);
            generateBox(l, 0, 3, 6, 0, 3, 7, FENCE_NS, FENCE_NS, false);
            generateBox(l, 8, 3, 6, 8, 3, 7, FENCE_NS, FENCE_NS, false);
            generateBox(l, 0, 3, 4, 0, 5, 5, BRICKS, BRICKS, false);
            generateBox(l, 8, 3, 4, 8, 5, 5, BRICKS, BRICKS, false);
            generateBox(l, 1, 3, 5, 2, 5, 5, BRICKS, BRICKS, false);
            generateBox(l, 6, 3, 5, 7, 5, 5, BRICKS, BRICKS, false);
            generateBox(l, 1, 4, 5, 1, 5, 5, FENCE_WE, FENCE_WE, false);
            generateBox(l, 7, 4, 5, 7, 5, 5, FENCE_WE, FENCE_WE, false);
            for (int b = 0; b <= 5; b++)
                for (int i = 0; i <= 8; i++) fillColumnDown(l, BRICKS, i, -1, b);
        }
    }

    static class CastleStalkRoom extends Piece {
        CastleStalkRoom(char dir, BoundingBox box, int depth) { super(dir, box, depth); }

        @Override void addChildren(StartPiece s, List<Piece> p, RandomSource r) {
            generateChildForward(s, p, r, 5, 3, true, this);
            generateChildForward(s, p, r, 5, 11, true, this);
        }

        static CastleStalkRoom createPiece(List<Piece> pieces, int x, int y, int z, char dir, int depth) {
            BoundingBox bb = orientBox(x, y, z, -5, -3, 0, 13, 14, 13, dir);
            if (!isOkBox(bb) || collides(pieces, bb)) return null;
            return new CastleStalkRoom(dir, bb, depth);
        }

        @Override void build(WorldGenLevel l, RandomSource r) {
            generateBox(l, 0, 3, 0, 12, 4, 12, BRICKS, BRICKS, false);
            generateBox(l, 0, 5, 0, 12, 13, 12, AIR, AIR, false);
            generateBox(l, 0, 5, 0, 1, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 11, 5, 0, 12, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 11, 4, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 8, 5, 11, 10, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 5, 9, 11, 7, 12, 12, BRICKS, BRICKS, false);
            generateBox(l, 2, 5, 0, 4, 12, 1, BRICKS, BRICKS, false);
            generateBox(l, 8, 5, 0, 10, 12, 1, BRICKS, BRICKS, false);
            generateBox(l, 5, 9, 0, 7, 12, 1, BRICKS, BRICKS, false);
            generateBox(l, 2, 11, 2, 10, 12, 10, BRICKS, BRICKS, false);
            int plain = BlockStateHelper.getDefault("nether_brick_fence");
            for (int i = 1; i <= 11; i += 2) {
                generateBox(l, i, 10, 0, i, 11, 0, FENCE_WE, FENCE_WE, false);
                generateBox(l, i, 10, 12, i, 11, 12, FENCE_WE, FENCE_WE, false);
                generateBox(l, 0, 10, i, 0, 11, i, FENCE_NS, FENCE_NS, false);
                generateBox(l, 12, 10, i, 12, 11, i, FENCE_NS, FENCE_NS, false);
                place(l, BRICKS, i, 13, 0);
                place(l, BRICKS, i, 13, 12);
                place(l, BRICKS, 0, 13, i);
                place(l, BRICKS, 12, 13, i);
                if (i != 11) {
                    place(l, FENCE_WE, i + 1, 13, 0);
                    place(l, FENCE_WE, i + 1, 13, 12);
                    place(l, FENCE_NS, 0, 13, i + 1);
                    place(l, FENCE_NS, 12, 13, i + 1);
                }
            }
            int nsW = BlockStateHelper.withProp(FENCE_NS, "west", "true");
            int nsE = BlockStateHelper.withProp(FENCE_NS, "east", "true");
            place(l, FENCE_NS_E, 0, 13, 0);
            place(l, FENCE_WE_S, 0, 13, 12);
            place(l, FENCE_WE_N, 12, 13, 0);
            place(l, plain, 12, 13, 12);
            for (int i = 3; i <= 9; i += 2) {
                generateBox(l, 1, 7, i, 1, 8, i, nsW, nsW, false);
                generateBox(l, 11, 7, i, 11, 8, i, nsE, nsE, false);
            }
            generateBox(l, 5, 6, 7, 5, 7, 7, nsE, nsE, false);
            generateBox(l, 7, 6, 7, 7, 7, 7, nsW, nsW, false);
            generateBox(l, 5, 13, 12, 7, 13, 12, AIR, AIR, false);
            for (int i = 4; i <= 8; i++)
                for (int k = 0; k <= 2; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, i, -1, 12 - k);
                }
            for (int i = 0; i <= 2; i++)
                for (int k = 4; k <= 8; k++) {
                    fillColumnDown(l, BRICKS, i, -1, k);
                    fillColumnDown(l, BRICKS, 12 - i, -1, k);
                }
        }
    }

    /** 原版 MonsterThrone 烈焰人刷怪笼 BE（SpawnData 用 1.19.3+ entity:{id} 包装）。 */
    private static org.cloudburstmc.nbt.NbtMap spawnerNbt(int x, int y, int z) {
        return org.cloudburstmc.nbt.NbtMap.builder()
            .putString("id", "minecraft:spawner")
            .putCompound("SpawnData", org.cloudburstmc.nbt.NbtMap.builder()
                .putCompound("entity", org.cloudburstmc.nbt.NbtMap.builder()
                    .putString("id", "minecraft:blaze").build())
                .putString("id", "minecraft:blaze")
                .build())
            // Bug43: 延迟用原版默认 200-800(曾 20-200 -> 刷怪频率高一个数量级,
            // MaxNearbyEntities 又没人读 -> 烈焰人越积越多漫游出要塞)。
            .putInt("MinSpawnDelay", 200).putInt("MaxSpawnDelay", 800)
            .putInt("SpawnCount", 4).putInt("MaxNearbyEntities", 6)
            .putInt("RequiredPlayerRange", 16).putInt("SpawnRange", 4)
            .build();
    }

    private static void placeChest(WorldGenLevel level, int x, int y, int z) {
        int chest = BlockStateHelper.getDefault("chest");
        if (chest <= 0) return;
        level.setBlock(x, y, z, chest);
        level.setBlockEntity(x, y, z, org.cloudburstmc.nbt.NbtMap.builder()
            .putString("id", "minecraft:chest")
            .putString("LootTable", "minecraft:chests/nether_bridge")
            .build());
    }

    // ── 结构入口（原版 NetherFortressStructure.generatePieces） ──────

    /** 返回完整件列表（含 StartPiece），已做 moveInsideHeights(48..70)。 */
    public static List<Piece> generate(long structureSeed, int chunkCenterX, int chunkCenterZ) {
        LegacyRandomSource rnd = new LegacyRandomSource(structureSeed);
        rnd.setSeed(structureSeed);
        StartPiece start = new StartPiece(rnd, chunkCenterX, chunkCenterZ);
        List<Piece> pieces = new ArrayList<>();
        pieces.add(start);
        start.addChildren(start, pieces, rnd);
        List<Piece> pending = start.pendingChildren;
        while (!pending.isEmpty()) {
            int i = rnd.nextInt(pending.size());
            Piece piece = pending.remove(i);
            piece.addChildren(start, pieces, rnd);
        }
        moveInsideHeights(pieces, rnd, 48, 70);
        return pieces;
    }

    /** 原版 StructurePiecesBuilder.moveInsideHeights：整体竖移使 minY 落在 [min..max] 带。 */
    private static void moveInsideHeights(List<Piece> pieces, RandomSource rnd, int minY, int maxY) {
        BoundingBox total = totalBox(pieces);
        int span = total.getSpanY();
        int range = maxY - minY + 1 - span;
        int target = range > 1 ? minY + rnd.nextInt(range) : minY;
        int dy = target - total.minY;
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
                                    int chunkMinX, int chunkMinZ, long seedForBuild) {
        int chunkMaxX = chunkMinX + 15, chunkMaxZ = chunkMinZ + 15;
        RandomSource rnd = new LegacyRandomSource(seedForBuild ^ 0x5DEECE66DL);
        for (Piece p : pieces) {
            if (p.box.maxX < chunkMinX || p.box.minX > chunkMaxX) continue;
            if (p.box.maxZ < chunkMinZ || p.box.minZ > chunkMaxZ) continue;
            p.build(level, rnd);
        }
    }
}
