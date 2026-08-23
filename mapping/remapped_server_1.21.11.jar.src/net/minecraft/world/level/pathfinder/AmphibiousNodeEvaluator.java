/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ 
/*     */ public class AmphibiousNodeEvaluator
/*     */   extends WalkNodeEvaluator {
/*     */   private final boolean prefersShallowSwimming;
/*     */   private float oldWalkableCost;
/*     */   private float oldWaterBorderCost;
/*     */   
/*     */   public AmphibiousNodeEvaluator(boolean paramBoolean) {
/*  16 */     this.prefersShallowSwimming = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare(PathNavigationRegion paramPathNavigationRegion, Mob paramMob) {
/*  21 */     super.prepare(paramPathNavigationRegion, paramMob);
/*  22 */     paramMob.setPathfindingMalus(PathType.WATER, 0.0F);
/*  23 */     this.oldWalkableCost = paramMob.getPathfindingMalus(PathType.WALKABLE);
/*  24 */     paramMob.setPathfindingMalus(PathType.WALKABLE, 6.0F);
/*  25 */     this.oldWaterBorderCost = paramMob.getPathfindingMalus(PathType.WATER_BORDER);
/*  26 */     paramMob.setPathfindingMalus(PathType.WATER_BORDER, 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  31 */     this.mob.setPathfindingMalus(PathType.WALKABLE, this.oldWalkableCost);
/*  32 */     this.mob.setPathfindingMalus(PathType.WATER_BORDER, this.oldWaterBorderCost);
/*  33 */     super.done();
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  38 */     if (!this.mob.isInWater()) {
/*  39 */       return super.getStart();
/*     */     }
/*  41 */     return getStartNode(new BlockPos(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY + 0.5D), Mth.floor((this.mob.getBoundingBox()).minZ)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  47 */     return getTargetNodeAt(paramDouble1, paramDouble2 + 0.5D, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] paramArrayOfNode, Node paramNode) {
/*     */     byte b1;
/*  53 */     int i = super.getNeighbors(paramArrayOfNode, paramNode);
/*     */ 
/*     */     
/*  56 */     PathType pathType1 = getCachedPathType(paramNode.x, paramNode.y + 1, paramNode.z);
/*  57 */     PathType pathType2 = getCachedPathType(paramNode.x, paramNode.y, paramNode.z);
/*     */     
/*  59 */     if (this.mob.getPathfindingMalus(pathType1) >= 0.0F && pathType2 != PathType.STICKY_HONEY) {
/*  60 */       b1 = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
/*     */     } else {
/*  62 */       b1 = 0;
/*     */     } 
/*     */     
/*  65 */     double d = getFloorLevel(new BlockPos(paramNode.x, paramNode.y, paramNode.z));
/*     */     
/*  67 */     Node node1 = findAcceptedNode(paramNode.x, paramNode.y + 1, paramNode.z, Math.max(0, b1 - 1), d, Direction.UP, pathType2);
/*  68 */     Node node2 = findAcceptedNode(paramNode.x, paramNode.y - 1, paramNode.z, b1, d, Direction.DOWN, pathType2);
/*     */     
/*  70 */     if (isVerticalNeighborValid(node1, paramNode)) {
/*  71 */       paramArrayOfNode[i++] = node1;
/*     */     }
/*     */     
/*  74 */     if (isVerticalNeighborValid(node2, paramNode) && pathType2 != PathType.TRAPDOOR) {
/*  75 */       paramArrayOfNode[i++] = node2;
/*     */     }
/*     */ 
/*     */     
/*  79 */     for (byte b2 = 0; b2 < i; b2++) {
/*  80 */       Node node = paramArrayOfNode[b2];
/*  81 */       if (node.type == PathType.WATER && this.prefersShallowSwimming && node.y < this.mob.level().getSeaLevel() - 10) {
/*  82 */         node.costMalus++;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     return i;
/*     */   }
/*     */   
/*     */   private boolean isVerticalNeighborValid(Node paramNode1, Node paramNode2) {
/*  90 */     return (isNeighborValid(paramNode1, paramNode2) && paramNode1.type == PathType.WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isAmphibious() {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3) {
/* 100 */     PathType pathType = paramPathfindingContext.getPathTypeFromState(paramInt1, paramInt2, paramInt3);
/*     */     
/* 102 */     if (pathType == PathType.WATER) {
/* 103 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 104 */       for (Direction direction : Direction.values()) {
/* 105 */         mutableBlockPos.set(paramInt1, paramInt2, paramInt3).move(direction);
/* 106 */         PathType pathType1 = paramPathfindingContext.getPathTypeFromState(mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ());
/* 107 */         if (pathType1 == PathType.BLOCKED) {
/* 108 */           return PathType.WATER_BORDER;
/*     */         }
/*     */       } 
/*     */       
/* 112 */       return PathType.WATER;
/*     */     } 
/*     */     
/* 115 */     return super.getPathType(paramPathfindingContext, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\AmphibiousNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */