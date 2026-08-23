/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.EnumMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class SwimNodeEvaluator
/*     */   extends NodeEvaluator
/*     */ {
/*     */   private final boolean allowBreaching;
/*  20 */   private final Long2ObjectMap<PathType> pathTypesByPosCache = (Long2ObjectMap<PathType>)new Long2ObjectOpenHashMap();
/*     */   
/*     */   public SwimNodeEvaluator(boolean paramBoolean) {
/*  23 */     this.allowBreaching = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare(PathNavigationRegion paramPathNavigationRegion, Mob paramMob) {
/*  28 */     super.prepare(paramPathNavigationRegion, paramMob);
/*  29 */     this.pathTypesByPosCache.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  34 */     super.done();
/*  35 */     this.pathTypesByPosCache.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  40 */     return getNode(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY + 0.5D), Mth.floor((this.mob.getBoundingBox()).minZ));
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  45 */     return getTargetNodeAt(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] paramArrayOfNode, Node paramNode) {
/*  50 */     byte b = 0;
/*     */     
/*  52 */     EnumMap<Direction, Node> enumMap = Maps.newEnumMap(Direction.class);
/*     */     
/*  54 */     for (Direction direction : Direction.values()) {
/*  55 */       Node node = findAcceptedNode(paramNode.x + direction.getStepX(), paramNode.y + direction.getStepY(), paramNode.z + direction.getStepZ());
/*  56 */       enumMap.put(direction, node);
/*  57 */       if (isNodeValid(node)) {
/*  58 */         paramArrayOfNode[b++] = node;
/*     */       }
/*     */     } 
/*     */     
/*  62 */     for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/*  63 */       Direction direction2 = direction1.getClockWise();
/*  64 */       if (hasMalus(enumMap.get(direction1)) && hasMalus(enumMap.get(direction2))) {
/*  65 */         Node node = findAcceptedNode(paramNode.x + direction1.getStepX() + direction2.getStepX(), paramNode.y, paramNode.z + direction1.getStepZ() + direction2.getStepZ());
/*  66 */         if (isNodeValid(node)) {
/*  67 */           paramArrayOfNode[b++] = node;
/*     */         }
/*     */       } 
/*     */     } 
/*  71 */     return b;
/*     */   }
/*     */   
/*     */   protected boolean isNodeValid(Node paramNode) {
/*  75 */     return (paramNode != null && !paramNode.closed);
/*     */   }
/*     */   
/*     */   private static boolean hasMalus(Node paramNode) {
/*  79 */     return (paramNode != null && paramNode.costMalus >= 0.0F);
/*     */   }
/*     */   
/*     */   protected Node findAcceptedNode(int paramInt1, int paramInt2, int paramInt3) {
/*  83 */     Node node = null;
/*  84 */     PathType pathType = getCachedBlockType(paramInt1, paramInt2, paramInt3);
/*     */     
/*  86 */     if ((this.allowBreaching && pathType == PathType.BREACH) || pathType == PathType.WATER) {
/*  87 */       float f = this.mob.getPathfindingMalus(pathType);
/*     */ 
/*     */       
/*  90 */       node = getNode(paramInt1, paramInt2, paramInt3);
/*  91 */       node.type = pathType;
/*  92 */       node.costMalus = Math.max(node.costMalus, f);
/*     */       
/*  94 */       if (f >= 0.0F && this.currentContext.level().getFluidState(new BlockPos(paramInt1, paramInt2, paramInt3)).isEmpty()) {
/*  95 */         node.costMalus += 8.0F;
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return node;
/*     */   }
/*     */   
/*     */   protected PathType getCachedBlockType(int paramInt1, int paramInt2, int paramInt3) {
/* 103 */     return (PathType)this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(paramInt1, paramInt2, paramInt3), paramLong -> getPathType(this.currentContext, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3) {
/* 108 */     return getPathTypeOfMob(paramPathfindingContext, paramInt1, paramInt2, paramInt3, this.mob);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType getPathTypeOfMob(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, Mob paramMob) {
/* 113 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 114 */     for (int i = paramInt1; i < paramInt1 + this.entityWidth; i++) {
/* 115 */       for (int j = paramInt2; j < paramInt2 + this.entityHeight; j++) {
/* 116 */         for (int k = paramInt3; k < paramInt3 + this.entityDepth; k++) {
/* 117 */           BlockState blockState1 = paramPathfindingContext.getBlockState((BlockPos)mutableBlockPos.set(i, j, k));
/* 118 */           FluidState fluidState = blockState1.getFluidState();
/*     */           
/* 120 */           if (fluidState.isEmpty() && blockState1.isPathfindable(PathComputationType.WATER) && blockState1.isAir())
/* 121 */             return PathType.BREACH; 
/* 122 */           if (!fluidState.is(FluidTags.WATER)) {
/* 123 */             return PathType.BLOCKED;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     BlockState blockState = paramPathfindingContext.getBlockState((BlockPos)mutableBlockPos);
/*     */ 
/*     */     
/* 132 */     if (blockState.isPathfindable(PathComputationType.WATER)) {
/* 133 */       return PathType.WATER;
/*     */     }
/*     */     
/* 136 */     return PathType.BLOCKED;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\SwimNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */