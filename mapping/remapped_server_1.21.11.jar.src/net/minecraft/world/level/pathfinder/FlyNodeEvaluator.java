/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ public class FlyNodeEvaluator
/*     */   extends WalkNodeEvaluator
/*     */ {
/*  18 */   private final Long2ObjectMap<PathType> pathTypeByPosCache = (Long2ObjectMap<PathType>)new Long2ObjectOpenHashMap();
/*     */   
/*     */   private static final float SMALL_MOB_SIZE = 1.0F;
/*     */   private static final float SMALL_MOB_INFLATED_START_NODE_BOUNDING_BOX = 1.1F;
/*     */   private static final int MAX_START_NODE_CANDIDATES = 10;
/*     */   
/*     */   public void prepare(PathNavigationRegion paramPathNavigationRegion, Mob paramMob) {
/*  25 */     super.prepare(paramPathNavigationRegion, paramMob);
/*  26 */     this.pathTypeByPosCache.clear();
/*     */     
/*  28 */     paramMob.onPathfindingStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  33 */     this.mob.onPathfindingDone();
/*     */     
/*  35 */     this.pathTypeByPosCache.clear();
/*  36 */     super.done();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*     */     int i;
/*  43 */     if (canFloat() && this.mob.isInWater()) {
/*  44 */       i = this.mob.getBlockY();
/*  45 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.mob.getX(), i, this.mob.getZ());
/*  46 */       BlockState blockState = this.currentContext.getBlockState((BlockPos)mutableBlockPos);
/*  47 */       while (blockState.is(Blocks.WATER)) {
/*  48 */         i++;
/*  49 */         mutableBlockPos.set(this.mob.getX(), i, this.mob.getZ());
/*  50 */         blockState = this.currentContext.getBlockState((BlockPos)mutableBlockPos);
/*     */       } 
/*     */     } else {
/*  53 */       i = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } 
/*     */     
/*  56 */     BlockPos blockPos = BlockPos.containing(this.mob.getX(), i, this.mob.getZ());
/*  57 */     if (!canStartAt(blockPos)) {
/*  58 */       for (BlockPos blockPos1 : iteratePathfindingStartNodeCandidatePositions(this.mob)) {
/*  59 */         if (canStartAt(blockPos1)) {
/*  60 */           return getStartNode(blockPos1);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  65 */     return getStartNode(blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStartAt(BlockPos paramBlockPos) {
/*  70 */     PathType pathType = getCachedPathType(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*  71 */     return (this.mob.getPathfindingMalus(pathType) >= 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  76 */     return getTargetNodeAt(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] paramArrayOfNode, Node paramNode) {
/*  81 */     byte b = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     Node node1 = findAcceptedNode(paramNode.x, paramNode.y, paramNode.z + 1);
/*  87 */     if (isOpen(node1)) {
/*  88 */       paramArrayOfNode[b++] = node1;
/*     */     }
/*     */     
/*  91 */     Node node2 = findAcceptedNode(paramNode.x - 1, paramNode.y, paramNode.z);
/*  92 */     if (isOpen(node2)) {
/*  93 */       paramArrayOfNode[b++] = node2;
/*     */     }
/*     */     
/*  96 */     Node node3 = findAcceptedNode(paramNode.x + 1, paramNode.y, paramNode.z);
/*  97 */     if (isOpen(node3)) {
/*  98 */       paramArrayOfNode[b++] = node3;
/*     */     }
/*     */     
/* 101 */     Node node4 = findAcceptedNode(paramNode.x, paramNode.y, paramNode.z - 1);
/* 102 */     if (isOpen(node4)) {
/* 103 */       paramArrayOfNode[b++] = node4;
/*     */     }
/*     */     
/* 106 */     Node node5 = findAcceptedNode(paramNode.x, paramNode.y + 1, paramNode.z);
/* 107 */     if (isOpen(node5)) {
/* 108 */       paramArrayOfNode[b++] = node5;
/*     */     }
/*     */     
/* 111 */     Node node6 = findAcceptedNode(paramNode.x, paramNode.y - 1, paramNode.z);
/* 112 */     if (isOpen(node6)) {
/* 113 */       paramArrayOfNode[b++] = node6;
/*     */     }
/*     */     
/* 116 */     Node node7 = findAcceptedNode(paramNode.x, paramNode.y + 1, paramNode.z + 1);
/* 117 */     if (isOpen(node7) && hasMalus(node1) && hasMalus(node5)) {
/* 118 */       paramArrayOfNode[b++] = node7;
/*     */     }
/*     */     
/* 121 */     Node node8 = findAcceptedNode(paramNode.x - 1, paramNode.y + 1, paramNode.z);
/* 122 */     if (isOpen(node8) && hasMalus(node2) && hasMalus(node5)) {
/* 123 */       paramArrayOfNode[b++] = node8;
/*     */     }
/*     */     
/* 126 */     Node node9 = findAcceptedNode(paramNode.x + 1, paramNode.y + 1, paramNode.z);
/* 127 */     if (isOpen(node9) && hasMalus(node3) && hasMalus(node5)) {
/* 128 */       paramArrayOfNode[b++] = node9;
/*     */     }
/*     */     
/* 131 */     Node node10 = findAcceptedNode(paramNode.x, paramNode.y + 1, paramNode.z - 1);
/* 132 */     if (isOpen(node10) && hasMalus(node4) && hasMalus(node5)) {
/* 133 */       paramArrayOfNode[b++] = node10;
/*     */     }
/*     */     
/* 136 */     Node node11 = findAcceptedNode(paramNode.x, paramNode.y - 1, paramNode.z + 1);
/* 137 */     if (isOpen(node11) && hasMalus(node1) && hasMalus(node6)) {
/* 138 */       paramArrayOfNode[b++] = node11;
/*     */     }
/*     */     
/* 141 */     Node node12 = findAcceptedNode(paramNode.x - 1, paramNode.y - 1, paramNode.z);
/* 142 */     if (isOpen(node12) && hasMalus(node2) && hasMalus(node6)) {
/* 143 */       paramArrayOfNode[b++] = node12;
/*     */     }
/*     */     
/* 146 */     Node node13 = findAcceptedNode(paramNode.x + 1, paramNode.y - 1, paramNode.z);
/* 147 */     if (isOpen(node13) && hasMalus(node3) && hasMalus(node6)) {
/* 148 */       paramArrayOfNode[b++] = node13;
/*     */     }
/*     */     
/* 151 */     Node node14 = findAcceptedNode(paramNode.x, paramNode.y - 1, paramNode.z - 1);
/* 152 */     if (isOpen(node14) && hasMalus(node4) && hasMalus(node6)) {
/* 153 */       paramArrayOfNode[b++] = node14;
/*     */     }
/*     */     
/* 156 */     Node node15 = findAcceptedNode(paramNode.x + 1, paramNode.y, paramNode.z - 1);
/* 157 */     if (isOpen(node15) && hasMalus(node4) && hasMalus(node3)) {
/* 158 */       paramArrayOfNode[b++] = node15;
/*     */     }
/*     */     
/* 161 */     Node node16 = findAcceptedNode(paramNode.x + 1, paramNode.y, paramNode.z + 1);
/* 162 */     if (isOpen(node16) && hasMalus(node1) && hasMalus(node3)) {
/* 163 */       paramArrayOfNode[b++] = node16;
/*     */     }
/*     */     
/* 166 */     Node node17 = findAcceptedNode(paramNode.x - 1, paramNode.y, paramNode.z - 1);
/* 167 */     if (isOpen(node17) && hasMalus(node4) && hasMalus(node2)) {
/* 168 */       paramArrayOfNode[b++] = node17;
/*     */     }
/*     */     
/* 171 */     Node node18 = findAcceptedNode(paramNode.x - 1, paramNode.y, paramNode.z + 1);
/* 172 */     if (isOpen(node18) && hasMalus(node1) && hasMalus(node2)) {
/* 173 */       paramArrayOfNode[b++] = node18;
/*     */     }
/*     */     
/* 176 */     Node node19 = findAcceptedNode(paramNode.x + 1, paramNode.y + 1, paramNode.z - 1);
/* 177 */     if (isOpen(node19) && hasMalus(node15) && hasMalus(node4) && hasMalus(node3) && hasMalus(node5) && hasMalus(node10) && hasMalus(node9)) {
/* 178 */       paramArrayOfNode[b++] = node19;
/*     */     }
/*     */     
/* 181 */     Node node20 = findAcceptedNode(paramNode.x + 1, paramNode.y + 1, paramNode.z + 1);
/* 182 */     if (isOpen(node20) && hasMalus(node16) && hasMalus(node1) && hasMalus(node3) && hasMalus(node5) && hasMalus(node7) && hasMalus(node9)) {
/* 183 */       paramArrayOfNode[b++] = node20;
/*     */     }
/*     */     
/* 186 */     Node node21 = findAcceptedNode(paramNode.x - 1, paramNode.y + 1, paramNode.z - 1);
/* 187 */     if (isOpen(node21) && hasMalus(node17) && hasMalus(node4) && hasMalus(node2) && hasMalus(node5) && hasMalus(node10) && hasMalus(node8)) {
/* 188 */       paramArrayOfNode[b++] = node21;
/*     */     }
/*     */     
/* 191 */     Node node22 = findAcceptedNode(paramNode.x - 1, paramNode.y + 1, paramNode.z + 1);
/* 192 */     if (isOpen(node22) && hasMalus(node18) && hasMalus(node1) && hasMalus(node2) && hasMalus(node5) && hasMalus(node7) && hasMalus(node8)) {
/* 193 */       paramArrayOfNode[b++] = node22;
/*     */     }
/*     */     
/* 196 */     Node node23 = findAcceptedNode(paramNode.x + 1, paramNode.y - 1, paramNode.z - 1);
/* 197 */     if (isOpen(node23) && hasMalus(node15) && hasMalus(node4) && hasMalus(node3) && hasMalus(node6) && hasMalus(node14) && hasMalus(node13)) {
/* 198 */       paramArrayOfNode[b++] = node23;
/*     */     }
/*     */     
/* 201 */     Node node24 = findAcceptedNode(paramNode.x + 1, paramNode.y - 1, paramNode.z + 1);
/* 202 */     if (isOpen(node24) && hasMalus(node16) && hasMalus(node1) && hasMalus(node3) && hasMalus(node6) && hasMalus(node11) && hasMalus(node13)) {
/* 203 */       paramArrayOfNode[b++] = node24;
/*     */     }
/*     */     
/* 206 */     Node node25 = findAcceptedNode(paramNode.x - 1, paramNode.y - 1, paramNode.z - 1);
/* 207 */     if (isOpen(node25) && hasMalus(node17) && hasMalus(node4) && hasMalus(node2) && hasMalus(node6) && hasMalus(node14) && hasMalus(node12)) {
/* 208 */       paramArrayOfNode[b++] = node25;
/*     */     }
/*     */     
/* 211 */     Node node26 = findAcceptedNode(paramNode.x - 1, paramNode.y - 1, paramNode.z + 1);
/* 212 */     if (isOpen(node26) && hasMalus(node18) && hasMalus(node1) && hasMalus(node2) && hasMalus(node6) && hasMalus(node11) && hasMalus(node12)) {
/* 213 */       paramArrayOfNode[b++] = node26;
/*     */     }
/*     */     
/* 216 */     return b;
/*     */   }
/*     */   
/*     */   private boolean hasMalus(Node paramNode) {
/* 220 */     return (paramNode != null && paramNode.costMalus >= 0.0F);
/*     */   }
/*     */   
/*     */   private boolean isOpen(Node paramNode) {
/* 224 */     return (paramNode != null && !paramNode.closed);
/*     */   }
/*     */   
/*     */   protected Node findAcceptedNode(int paramInt1, int paramInt2, int paramInt3) {
/* 228 */     Node node = null;
/*     */     
/* 230 */     PathType pathType = getCachedPathType(paramInt1, paramInt2, paramInt3);
/*     */     
/* 232 */     float f = this.mob.getPathfindingMalus(pathType);
/*     */     
/* 234 */     if (f >= 0.0F) {
/* 235 */       node = getNode(paramInt1, paramInt2, paramInt3);
/* 236 */       node.type = pathType;
/* 237 */       node.costMalus = Math.max(node.costMalus, f);
/*     */       
/* 239 */       if (pathType == PathType.WALKABLE) {
/* 240 */         node.costMalus++;
/*     */       }
/*     */     } 
/*     */     
/* 244 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathType getCachedPathType(int paramInt1, int paramInt2, int paramInt3) {
/* 249 */     return (PathType)this.pathTypeByPosCache.computeIfAbsent(BlockPos.asLong(paramInt1, paramInt2, paramInt3), paramLong -> getPathTypeOfMob(this.currentContext, paramInt1, paramInt2, paramInt3, this.mob));
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3) {
/* 254 */     PathType pathType = paramPathfindingContext.getPathTypeFromState(paramInt1, paramInt2, paramInt3);
/*     */     
/* 256 */     if (pathType == PathType.OPEN && paramInt2 >= paramPathfindingContext.level().getMinY() + 1) {
/* 257 */       BlockPos blockPos = new BlockPos(paramInt1, paramInt2 - 1, paramInt3);
/* 258 */       PathType pathType1 = paramPathfindingContext.getPathTypeFromState(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*     */       
/* 260 */       if (pathType1 == PathType.DAMAGE_FIRE || pathType1 == PathType.LAVA) {
/* 261 */         pathType = PathType.DAMAGE_FIRE;
/* 262 */       } else if (pathType1 == PathType.DAMAGE_OTHER) {
/* 263 */         pathType = PathType.DAMAGE_OTHER;
/* 264 */       } else if (pathType1 == PathType.COCOA) {
/* 265 */         pathType = PathType.COCOA;
/* 266 */       } else if (pathType1 == PathType.FENCE) {
/* 267 */         if (!blockPos.equals(paramPathfindingContext.mobPosition())) {
/* 268 */           pathType = PathType.FENCE;
/*     */         }
/*     */       } else {
/*     */         
/* 272 */         pathType = (pathType1 == PathType.WALKABLE || pathType1 == PathType.OPEN || pathType1 == PathType.WATER) ? PathType.OPEN : PathType.WALKABLE;
/*     */       } 
/*     */     } 
/*     */     
/* 276 */     if (pathType == PathType.WALKABLE || pathType == PathType.OPEN) {
/* 277 */       pathType = checkNeighbourBlocks(paramPathfindingContext, paramInt1, paramInt2, paramInt3, pathType);
/*     */     }
/*     */     
/* 280 */     return pathType;
/*     */   }
/*     */   
/*     */   private Iterable<BlockPos> iteratePathfindingStartNodeCandidatePositions(Mob paramMob) {
/* 284 */     AABB aABB1 = paramMob.getBoundingBox();
/* 285 */     boolean bool = (aABB1.getSize() < 1.0D) ? true : false;
/* 286 */     if (!bool) {
/* 287 */       return List.of(
/* 288 */           BlockPos.containing(aABB1.minX, paramMob.getBlockY(), aABB1.minZ), 
/* 289 */           BlockPos.containing(aABB1.minX, paramMob.getBlockY(), aABB1.maxZ), 
/* 290 */           BlockPos.containing(aABB1.maxX, paramMob.getBlockY(), aABB1.minZ), 
/* 291 */           BlockPos.containing(aABB1.maxX, paramMob.getBlockY(), aABB1.maxZ));
/*     */     }
/*     */     
/* 294 */     double d1 = Math.max(0.0D, 1.100000023841858D - aABB1.getZsize());
/* 295 */     double d2 = Math.max(0.0D, 1.100000023841858D - aABB1.getXsize());
/* 296 */     double d3 = Math.max(0.0D, 1.100000023841858D - aABB1.getYsize());
/* 297 */     AABB aABB2 = aABB1.inflate(d2, d3, d1);
/* 298 */     return BlockPos.randomBetweenClosed(paramMob.getRandom(), 10, 
/* 299 */         Mth.floor(aABB2.minX), Mth.floor(aABB2.minY), Mth.floor(aABB2.minZ), 
/* 300 */         Mth.floor(aABB2.maxX), Mth.floor(aABB2.maxY), Mth.floor(aABB2.maxZ));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\FlyNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */