package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.gen.LegacyRandomSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JigsawPlacement {

    private static final boolean DEBUG = false;

    public static List<PoolElementStructurePiece> addPieces(
            StructureTemplateManager templateManager,
            String startPoolId,
            int maxDepth,
            int startX, int startY, int startZ,
            int heightmapY,
            long seed,
            java.util.function.IntBinaryOperator terrainHeightAt) {
        return addPieces(templateManager, startPoolId, maxDepth, 80,
            startX, startY, startZ, heightmapY, seed, terrainHeightAt);
    }

    /** 完整版：maxDistance = 原版 max_distance_from_center（试炼密室=116, 其余默认 80）。 */
    public static List<PoolElementStructurePiece> addPieces(
            StructureTemplateManager templateManager,
            String startPoolId,
            int maxDepth,
            int maxDistance,
            int startX, int startY, int startZ,
            int heightmapY,
            long seed,
            java.util.function.IntBinaryOperator terrainHeightAt) {
        return addPieces(templateManager, startPoolId, maxDepth, maxDistance,
            startX, startY, startZ, heightmapY, seed, terrainHeightAt, java.util.Map.of());
    }

    /** Bug15: 带 pool_aliases 的完整版(每个 start 决议一次别名映射)。 */
    public static List<PoolElementStructurePiece> addPieces(
            StructureTemplateManager templateManager,
            String startPoolId,
            int maxDepth,
            int maxDistance,
            int startX, int startY, int startZ,
            int heightmapY,
            long seed,
            java.util.function.IntBinaryOperator terrainHeightAt,
            java.util.Map<String, String> poolAliases) {

        RandomSource random = new LegacyRandomSource(seed);
        Rotation rotation = Rotation.values()[random.nextInt(4)];

        StructureTemplatePool startPool = StructureTemplatePool.get(startPoolId);
        if (startPool == null || startPool.size() == 0) return null;

        StructurePoolElement startElement = startPool.getRandomTemplate(random);
        if (startElement == null || startElement == StructurePoolElement.EMPTY_SINGLETON) return null;

        PoolElementStructurePiece rootPiece = new PoolElementStructurePiece(
            startElement, startX, startY, startZ,
            startElement.getGroundLevelDelta(), rotation,
            startElement.getBoundingBox(templateManager, startX, startY, startZ, rotation));

        BoundingBox rootBB = rootPiece.getBoundingBox();
        int centerX = (rootBB.maxX + rootBB.minX) / 2;
        int centerZ = (rootBB.maxZ + rootBB.minZ) / 2;
        int targetY = heightmapY;
        int currentBottomY = rootBB.minY + rootPiece.getGroundLevelDelta();
        rootPiece.move(0, targetY - currentBottomY, 0);

        List<PoolElementStructurePiece> pieces = new ArrayList<>();
        pieces.add(rootPiece);

        if (maxDepth <= 0) return pieces;

        int minY = -64, maxY = 320;

        Placer placer = new Placer(templateManager, maxDepth, random, pieces, maxDistance,
                minY, maxY, heightmapY, centerX, centerZ, terrainHeightAt, poolAliases);
        placer.tryPlacingChildren(rootPiece, 0);

        while (!placer.queue.isEmpty()) {
            PieceState state = placer.queue.poll();
            placer.tryPlacingChildren(state.piece, state.depth);
        }

        return pieces;
    }

    /** 兼容重载：不传地形高度函数时使用默认海平线 64（供测试与其他调用方使用）。 */
    public static List<PoolElementStructurePiece> addPieces(
            StructureTemplateManager templateManager,
            String startPoolId,
            int maxDepth,
            int startX, int startY, int startZ,
            int heightmapY,
            long seed) {
        return addPieces(templateManager, startPoolId, maxDepth, startX, startY, startZ,
                heightmapY, seed, (_x, _z) -> 64);
    }

    private static class PieceState {
        final PoolElementStructurePiece piece;
        final int depth;
        PieceState(PoolElementStructurePiece piece, int depth) {
            this.piece = piece;
            this.depth = depth;
        }
    }

    private static class Placer {
        final StructureTemplateManager templateManager;
        final int maxDepth;
        final RandomSource random;
        final List<PoolElementStructurePiece> pieces;
        final int maxDistance;
        final int minY, maxY;
        int heightmapY; // 基准高度（根片段使用），子片段可能覆盖
        final int centerX, centerZ;
        final java.util.function.IntBinaryOperator terrainHeightAt; // (x,z) → 表面Y
        final java.util.Map<String, String> poolAliases; // Bug15: pool_aliases 决议结果
        final Queue<PieceState> queue = new LinkedList<>();

        Placer(StructureTemplateManager templateManager, int maxDepth, RandomSource random,
               List<PoolElementStructurePiece> pieces, int maxDistance, int minY, int maxY,
               int heightmapY, int centerX, int centerZ,
               java.util.function.IntBinaryOperator terrainHeightAt,
               java.util.Map<String, String> poolAliases) {
            this.templateManager = templateManager;
            this.maxDepth = maxDepth;
            this.random = random;
            this.pieces = pieces;
            this.maxDistance = maxDistance;
            this.minY = minY;
            this.maxY = maxY;
            this.heightmapY = heightmapY;
            this.centerX = centerX;
            this.centerZ = centerZ;
            this.terrainHeightAt = terrainHeightAt;
            this.poolAliases = poolAliases == null ? java.util.Map.of() : poolAliases;
        }

        void tryPlacingChildren(PoolElementStructurePiece parentPiece, int depth) {
            StructurePoolElement parentElement = parentPiece.getElement();
            int posX = parentPiece.getPositionX();
            int posY = parentPiece.getPositionY();
            int posZ = parentPiece.getPositionZ();
            Rotation parentRotation = parentPiece.getRotation();
            BoundingBox parentBB = parentPiece.getBoundingBox();
            int parentMinY = parentBB.minY;

            List<JigsawBlockInfo> parentJigsaws = parentElement.getShuffledJigsawBlocks(
                templateManager, posX, posY, posZ, parentRotation, random);

            if (depth == 0 && DEBUG) {
                System.out.println("[DBG] root jigsaws to expand: " + parentJigsaws.size());
            }

            for (JigsawBlockInfo parentJigsaw : parentJigsaws) {
                String parentFront = parentJigsaw.frontFacing();
                int jigsawX = parentJigsaw.x();
                int jigsawY = parentJigsaw.y();
                int jigsawZ = parentJigsaw.z();

                int frontDx = directionDx(parentFront);
                int frontDy = directionDy(parentFront);
                int frontDz = directionDz(parentFront);
                int attachX = jigsawX + frontDx;
                int attachY = jigsawY + frontDy;
                int attachZ = jigsawZ + frontDz;

                int deltaToParentMinY = jigsawY - parentMinY;

                String poolId = parentJigsaw.pool();
                if (poolId == null) continue;
                // Bug15: 经 pool_aliases 映射(试炼密室 spawner/contents/* 等)
                String mappedPool = poolId.startsWith("minecraft:")
                    ? poolId.substring(10) : poolId;
                mappedPool = poolAliases.getOrDefault(mappedPool, mappedPool);
                StructureTemplatePool pool = StructureTemplatePool.get(mappedPool);
                if (pool == null || pool.size() == 0) continue;
                if (depth == 0 && DEBUG)
                    System.out.println("[DBG] parentJigsaw front=" + parentJigsaw.frontFacing()
                        + " target=" + parentJigsaw.target() + " pool=" + poolId + " candidates=" + pool.size()
                        + " jpos=[" + jigsawX + "," + jigsawY + "," + jigsawZ + "]"
                        + " attach=[" + attachX + "," + attachY + "," + attachZ + "]");

                StructureTemplatePool fallbackPool = StructureTemplatePool.get(pool.getFallbackPoolId());
                if (fallbackPool == null) fallbackPool = StructureTemplatePool.EMPTY;

                List<StructurePoolElement> candidates = new ArrayList<>();
                if (depth != maxDepth) {
                    candidates.addAll(pool.getShuffledTemplates(random));
                }
                candidates.addAll(fallbackPool.getShuffledTemplates(random));

                boolean placed = false;
                for (StructurePoolElement candidate : candidates) {
                    if (candidate == StructurePoolElement.EMPTY_SINGLETON) continue;
                    if (placed) break;

                    // ROLLED joint 处理：原版 JigsawPlacement 对 ROLLED 父子连接并不显式把子片段
                    // 旋转额外翻转 180°，而是枚举全部 4 个旋转、由 JigsawBlockInfo.canAttach 决定
                    // 是否可接（ROLLABLE 时仅要求 front 相反、不要求 top 相等）。本实现的 canAttach
                    // 已按此语义实现（see JigsawBlockInfo.canAttach），故此处枚举 + canAttach 已与原版
                    // 等价，不额外叠加 CLOCKWISE_180（否则会改变子片段 front 方向、破坏拼接连通）。
                    Rotation[] rotations = Rotation.values();
                    for (Rotation childRotation : rotations) {
                        List<JigsawBlockInfo> childJigsaws = candidate.getShuffledJigsawBlocks(
                            templateManager, 0, 0, 0, childRotation, random);
                        BoundingBox childBBTemplate = candidate.getBoundingBox(
                            templateManager, 0, 0, 0, childRotation);

                        for (JigsawBlockInfo childJigsaw : childJigsaws) {
                            if (!JigsawBlockInfo.canAttach(parentJigsaw, childJigsaw)) continue;

                            int childJigsawX = childJigsaw.x();
                            int childJigsawY = childJigsaw.y();
                            int childJigsawZ = childJigsaw.z();

                            int childPosX = attachX - childJigsawX;
                            int childPosY = attachY - childJigsawY;
                            int childPosZ = attachZ - childJigsawZ;

                            BoundingBox childBB = candidate.getBoundingBox(
                                templateManager, childPosX, childPosY, childPosZ, childRotation);

                            int childMinY = childBB.minY;
                            Projection parentProj = parentElement.getProjection();
                            Projection childProj = candidate.getProjection();
                            boolean parentRigid = parentProj == Projection.RIGID;
                            boolean childRigid = childProj == Projection.RIGID;

                            int adjustedY;
                            if (parentRigid && childRigid) {
                                adjustedY = parentMinY + deltaToParentMinY + frontDy - childJigsawY;
                            } else {
                                // terrain_adaptation: 非刚性片段使用其位置下方的实际地形高度
                                int localTerrainY = terrainHeightAt.applyAsInt(childPosX, childPosZ);
                                adjustedY = localTerrainY - childJigsawY;
                            }

                            int moveDy = adjustedY - childMinY;
                            BoundingBox finalBB = new BoundingBox(
                                childBB.minX, childBB.minY + moveDy, childBB.minZ,
                                childBB.maxX, childBB.maxY + moveDy, childBB.maxZ);

                            if (finalBB.minY < minY || finalBB.maxY > maxY) continue;

                            // 距结构中心（而非父片段）的曼哈顿/切比雪夫距离，限定村庄整体半径
                            int centerDistanceX = Math.max(
                                Math.abs(finalBB.minX - centerX), Math.abs(finalBB.maxX - centerX));
                            int centerDistanceZ = Math.max(
                                Math.abs(finalBB.minZ - centerZ), Math.abs(finalBB.maxZ - centerZ));
                            if (centerDistanceX > maxDistance || centerDistanceZ > maxDistance) continue;

                            // 仅当包围盒真正"重叠"（排除仅共面/相切的已连接片段）时拒绝，
                            // 否则会把正常拼接、共享一面墙的相邻片段误杀，导致村庄只剩 1 块。
                            boolean collides = false;
                            for (PoolElementStructurePiece existing : pieces) {
                                if (existing == parentPiece) continue;
                                BoundingBox eb = existing.getBoundingBox();
                                if (finalBB.minX < eb.maxX && finalBB.maxX > eb.minX
                                    && finalBB.minY < eb.maxY && finalBB.maxY > eb.minY
                                    && finalBB.minZ < eb.maxZ && finalBB.maxZ > eb.minZ) {
                                    collides = true;
                                    break;
                                }
                            }
                            if (depth == 0 && DEBUG && !placed) {
                                System.out.println("[DBG]   cand=" + candidate.toString().substring(0, Math.min(60, candidate.toString().length()))
                                    + " canAttach=" + JigsawBlockInfo.canAttach(parentJigsaw, childJigsaw)
                                    + " collides=" + collides
                                    + " bb=[" + finalBB.minX + "," + finalBB.minY + "," + finalBB.minZ + "->" + finalBB.maxX + "," + finalBB.maxY + "," + finalBB.maxZ + "]"
                                    + " oob=" + (finalBB.minY < minY || finalBB.maxY > maxY)
                                    + " cj=[" + childJigsawX + "," + childJigsawY + "," + childJigsawZ + "]"
                                    + " cpos=[" + childPosX + "," + childPosY + "," + childPosZ + "]");
                            }
                            if (collides) continue;

                            int childGroundLevelDelta = childRigid
                                ? parentPiece.getGroundLevelDelta() - (deltaToParentMinY + frontDy - childJigsawY)
                                : candidate.getGroundLevelDelta();

                            // 安全上限：防止极端种子下片段数失控(试炼密室 size=20 可达数百片段)
                            if (pieces.size() >= 1024) continue;

                            PoolElementStructurePiece childPiece = new PoolElementStructurePiece(
                                candidate, childPosX, childPosY + moveDy, childPosZ,
                                childGroundLevelDelta, childRotation, finalBB);

                            pieces.add(childPiece);

                            parentPiece.addJunction(new JigsawJunction(
                                attachX, adjustedY - deltaToParentMinY + parentPiece.getGroundLevelDelta(),
                                attachZ, deltaToParentMinY + frontDy - childJigsawY, childProj));
                            childPiece.addJunction(new JigsawJunction(
                                jigsawX, adjustedY - childJigsawY + childGroundLevelDelta,
                                jigsawZ, -(deltaToParentMinY + frontDy - childJigsawY), parentProj));

                            if (depth + 1 <= maxDepth) {
                                queue.add(new PieceState(childPiece, depth + 1));
                            }
                            placed = true;
                            break;
                        }
                        if (placed) break;
                    }
                }
            }
        }
    }

    private static int directionDx(String dir) {
        return switch (dir) {
            case "east" -> 1;
            case "west" -> -1;
            default -> 0;
        };
    }

    private static int directionDy(String dir) {
        return switch (dir) {
            case "up" -> 1;
            case "down" -> -1;
            default -> 0;
        };
    }

    private static int directionDz(String dir) {
        return switch (dir) {
            case "south" -> 1;
            case "north" -> -1;
            default -> 0;
        };
    }
}
