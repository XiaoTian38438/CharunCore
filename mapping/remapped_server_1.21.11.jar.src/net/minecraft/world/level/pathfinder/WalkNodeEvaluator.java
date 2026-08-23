/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.FenceGateBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WalkNodeEvaluator
/*     */   extends NodeEvaluator
/*     */ {
/*     */   public static final double SPACE_BETWEEN_WALL_POSTS = 0.5D;
/*     */   private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125D;
/*  38 */   private final Long2ObjectMap<PathType> pathTypesByPosCacheByMob = (Long2ObjectMap<PathType>)new Long2ObjectOpenHashMap();
/*  39 */   private final Object2BooleanMap<AABB> collisionCache = (Object2BooleanMap<AABB>)new Object2BooleanOpenHashMap();
/*  40 */   private final Node[] reusableNeighbors = new Node[Direction.Plane.HORIZONTAL.length()];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare(PathNavigationRegion paramPathNavigationRegion, Mob paramMob) {
/*  47 */     super.prepare(paramPathNavigationRegion, paramMob);
/*  48 */     paramMob.onPathfindingStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  53 */     this.mob.onPathfindingDone();
/*     */     
/*  55 */     this.pathTypesByPosCacheByMob.clear();
/*  56 */     this.collisionCache.clear();
/*     */     
/*  58 */     super.done();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  64 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  65 */     int i = this.mob.getBlockY();
/*  66 */     BlockState blockState = this.currentContext.getBlockState((BlockPos)mutableBlockPos.set(this.mob.getX(), i, this.mob.getZ()));
/*     */     
/*  68 */     if (this.mob.canStandOnFluid(blockState.getFluidState())) {
/*  69 */       while (this.mob.canStandOnFluid(blockState.getFluidState())) {
/*  70 */         i++;
/*  71 */         blockState = this.currentContext.getBlockState((BlockPos)mutableBlockPos.set(this.mob.getX(), i, this.mob.getZ()));
/*     */       } 
/*  73 */       i--;
/*  74 */     } else if (canFloat() && this.mob.isInWater()) {
/*  75 */       while (blockState.is(Blocks.WATER) || blockState.getFluidState() == Fluids.WATER.getSource(false)) {
/*  76 */         i++;
/*  77 */         blockState = this.currentContext.getBlockState((BlockPos)mutableBlockPos.set(this.mob.getX(), i, this.mob.getZ()));
/*     */       } 
/*  79 */       i--;
/*     */     }
/*  81 */     else if (this.mob.onGround()) {
/*  82 */       i = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } else {
/*  84 */       mutableBlockPos.set(this.mob.getX(), this.mob.getY() + 1.0D, this.mob.getZ());
/*  85 */       while (mutableBlockPos.getY() > this.currentContext.level().getMinY()) {
/*  86 */         i = mutableBlockPos.getY();
/*  87 */         mutableBlockPos.setY(mutableBlockPos.getY() - 1);
/*  88 */         BlockState blockState1 = this.currentContext.getBlockState((BlockPos)mutableBlockPos);
/*  89 */         if (!blockState1.isAir() && !blockState1.isPathfindable(PathComputationType.LAND)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  96 */     BlockPos blockPos = this.mob.blockPosition();
/*  97 */     if (!canStartAt((BlockPos)mutableBlockPos.set(blockPos.getX(), i, blockPos.getZ()))) {
/*  98 */       AABB aABB = this.mob.getBoundingBox();
/*     */       
/* 100 */       if (canStartAt((BlockPos)mutableBlockPos.set(aABB.minX, i, aABB.minZ)) || 
/* 101 */         canStartAt((BlockPos)mutableBlockPos.set(aABB.minX, i, aABB.maxZ)) || 
/* 102 */         canStartAt((BlockPos)mutableBlockPos.set(aABB.maxX, i, aABB.minZ)) || 
/* 103 */         canStartAt((BlockPos)mutableBlockPos.set(aABB.maxX, i, aABB.maxZ)))
/*     */       {
/* 105 */         return getStartNode((BlockPos)mutableBlockPos);
/*     */       }
/*     */     } 
/* 108 */     return getStartNode(new BlockPos(blockPos.getX(), i, blockPos.getZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node getStartNode(BlockPos paramBlockPos) {
/* 114 */     Node node = getNode(paramBlockPos);
/* 115 */     node.type = getCachedPathType(node.x, node.y, node.z);
/* 116 */     node.costMalus = this.mob.getPathfindingMalus(node.type);
/* 117 */     return node;
/*     */   }
/*     */   
/*     */   protected boolean canStartAt(BlockPos paramBlockPos) {
/* 121 */     PathType pathType = getCachedPathType(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/* 122 */     return (pathType != PathType.OPEN && this.mob.getPathfindingMalus(pathType) >= 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getTarget(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 127 */     return getTargetNodeAt(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] paramArrayOfNode, Node paramNode) {
/* 132 */     byte b = 0;
/* 133 */     int i = 0;
/* 134 */     PathType pathType1 = getCachedPathType(paramNode.x, paramNode.y + 1, paramNode.z);
/* 135 */     PathType pathType2 = getCachedPathType(paramNode.x, paramNode.y, paramNode.z);
/*     */     
/* 137 */     if (this.mob.getPathfindingMalus(pathType1) >= 0.0F && pathType2 != PathType.STICKY_HONEY) {
/* 138 */       i = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
/*     */     }
/*     */     
/* 141 */     double d = getFloorLevel(new BlockPos(paramNode.x, paramNode.y, paramNode.z));
/* 142 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 143 */       Node node = findAcceptedNode(paramNode.x + direction.getStepX(), paramNode.y, paramNode.z + direction.getStepZ(), i, d, direction, pathType2);
/* 144 */       this.reusableNeighbors[direction.get2DDataValue()] = node;
/* 145 */       if (isNeighborValid(node, paramNode)) {
/* 146 */         paramArrayOfNode[b++] = node;
/*     */       }
/*     */     } 
/*     */     
/* 150 */     for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/* 151 */       Direction direction2 = direction1.getClockWise();
/* 152 */       if (isDiagonalValid(paramNode, this.reusableNeighbors[direction1.get2DDataValue()], this.reusableNeighbors[direction2.get2DDataValue()])) {
/* 153 */         Node node = findAcceptedNode(paramNode.x + direction1.getStepX() + direction2.getStepX(), paramNode.y, paramNode.z + direction1.getStepZ() + direction2.getStepZ(), i, d, direction1, pathType2);
/* 154 */         if (isDiagonalValid(node)) {
/* 155 */           paramArrayOfNode[b++] = node;
/*     */         }
/*     */       } 
/*     */     } 
/* 159 */     return b;
/*     */   }
/*     */   
/*     */   protected boolean isNeighborValid(Node paramNode1, Node paramNode2) {
/* 163 */     return (paramNode1 != null && !paramNode1.closed && (paramNode1.costMalus >= 0.0F || paramNode2.costMalus < 0.0F));
/*     */   }
/*     */   
/*     */   protected boolean isDiagonalValid(Node paramNode1, Node paramNode2, Node paramNode3) {
/* 167 */     if (paramNode3 == null || paramNode2 == null || paramNode3.y > paramNode1.y || paramNode2.y > paramNode1.y) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (paramNode2.type == PathType.WALKABLE_DOOR || paramNode3.type == PathType.WALKABLE_DOOR)
/*     */     {
/* 172 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 176 */     boolean bool = (paramNode3.type == PathType.FENCE && paramNode2.type == PathType.FENCE && this.mob.getBbWidth() < 0.5D) ? true : false;
/* 177 */     return ((paramNode3.y < paramNode1.y || paramNode3.costMalus >= 0.0F || bool) && (paramNode2.y < paramNode1.y || paramNode2.costMalus >= 0.0F || bool));
/*     */   }
/*     */   
/*     */   protected boolean isDiagonalValid(Node paramNode) {
/* 181 */     if (paramNode == null || paramNode.closed) {
/* 182 */       return false;
/*     */     }
/* 184 */     if (paramNode.type == PathType.WALKABLE_DOOR)
/*     */     {
/* 186 */       return false;
/*     */     }
/* 188 */     return (paramNode.costMalus >= 0.0F);
/*     */   }
/*     */   
/*     */   private static boolean doesBlockHavePartialCollision(PathType paramPathType) {
/* 192 */     return (paramPathType == PathType.FENCE || paramPathType == PathType.DOOR_WOOD_CLOSED || paramPathType == PathType.DOOR_IRON_CLOSED);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canReachWithoutCollision(Node paramNode) {
/* 198 */     AABB aABB = this.mob.getBoundingBox();
/*     */ 
/*     */ 
/*     */     
/* 202 */     Vec3 vec3 = new Vec3(paramNode.x - this.mob.getX() + aABB.getXsize() / 2.0D, paramNode.y - this.mob.getY() + aABB.getYsize() / 2.0D, paramNode.z - this.mob.getZ() + aABB.getZsize() / 2.0D);
/*     */     
/* 204 */     int i = Mth.ceil(vec3.length() / aABB.getSize());
/* 205 */     vec3 = vec3.scale((1.0F / i));
/* 206 */     for (byte b = 1; b <= i; b++) {
/* 207 */       aABB = aABB.move(vec3);
/* 208 */       if (hasCollisions(aABB)) {
/* 209 */         return false;
/*     */       }
/*     */     } 
/* 212 */     return true;
/*     */   }
/*     */   
/*     */   protected double getFloorLevel(BlockPos paramBlockPos) {
/* 216 */     CollisionGetter collisionGetter = this.currentContext.level();
/* 217 */     if ((canFloat() || isAmphibious()) && collisionGetter.getFluidState(paramBlockPos).is(FluidTags.WATER)) {
/* 218 */       return paramBlockPos.getY() + 0.5D;
/*     */     }
/* 220 */     return getFloorLevel((BlockGetter)collisionGetter, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static double getFloorLevel(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 224 */     BlockPos blockPos = paramBlockPos.below();
/* 225 */     VoxelShape voxelShape = paramBlockGetter.getBlockState(blockPos).getCollisionShape(paramBlockGetter, blockPos);
/* 226 */     return blockPos.getY() + (voxelShape.isEmpty() ? 0.0D : voxelShape.max(Direction.Axis.Y));
/*     */   }
/*     */   
/*     */   protected boolean isAmphibious() {
/* 230 */     return false;
/*     */   }
/*     */   
/*     */   protected Node findAcceptedNode(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, Direction paramDirection, PathType paramPathType) {
/* 234 */     Node node = null;
/* 235 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 237 */     double d = getFloorLevel((BlockPos)mutableBlockPos.set(paramInt1, paramInt2, paramInt3));
/*     */     
/* 239 */     if (d - paramDouble > getMobJumpHeight()) {
/* 240 */       return null;
/*     */     }
/*     */     
/* 243 */     PathType pathType = getCachedPathType(paramInt1, paramInt2, paramInt3);
/* 244 */     float f = this.mob.getPathfindingMalus(pathType);
/*     */     
/* 246 */     if (f >= 0.0F) {
/* 247 */       node = getNodeAndUpdateCostToMax(paramInt1, paramInt2, paramInt3, pathType, f);
/*     */     }
/*     */ 
/*     */     
/* 251 */     if (doesBlockHavePartialCollision(paramPathType) && node != null && node.costMalus >= 0.0F && !canReachWithoutCollision(node)) {
/* 252 */       node = null;
/*     */     }
/*     */     
/* 255 */     if (pathType == PathType.WALKABLE || (isAmphibious() && pathType == PathType.WATER)) {
/* 256 */       return node;
/*     */     }
/*     */     
/* 259 */     if ((node == null || node.costMalus < 0.0F) && paramInt4 > 0 && (pathType != PathType.FENCE || canWalkOverFences()) && pathType != PathType.UNPASSABLE_RAIL && pathType != PathType.TRAPDOOR && pathType != PathType.POWDER_SNOW) {
/* 260 */       node = tryJumpOn(paramInt1, paramInt2, paramInt3, paramInt4, paramDouble, paramDirection, paramPathType, mutableBlockPos);
/* 261 */     } else if (!isAmphibious() && pathType == PathType.WATER && !canFloat()) {
/* 262 */       node = tryFindFirstNonWaterBelow(paramInt1, paramInt2, paramInt3, node);
/* 263 */     } else if (pathType == PathType.OPEN) {
/* 264 */       node = tryFindFirstGroundNodeBelow(paramInt1, paramInt2, paramInt3);
/* 265 */     } else if (doesBlockHavePartialCollision(pathType) && node == null) {
/* 266 */       node = getClosedNode(paramInt1, paramInt2, paramInt3, pathType);
/*     */     } 
/* 268 */     return node;
/*     */   }
/*     */   
/*     */   private double getMobJumpHeight() {
/* 272 */     return Math.max(1.125D, this.mob.maxUpStep());
/*     */   }
/*     */   
/*     */   private Node getNodeAndUpdateCostToMax(int paramInt1, int paramInt2, int paramInt3, PathType paramPathType, float paramFloat) {
/* 276 */     Node node = getNode(paramInt1, paramInt2, paramInt3);
/* 277 */     node.type = paramPathType;
/* 278 */     node.costMalus = Math.max(node.costMalus, paramFloat);
/* 279 */     return node;
/*     */   }
/*     */   
/*     */   private Node getBlockedNode(int paramInt1, int paramInt2, int paramInt3) {
/* 283 */     Node node = getNode(paramInt1, paramInt2, paramInt3);
/* 284 */     node.type = PathType.BLOCKED;
/* 285 */     node.costMalus = -1.0F;
/* 286 */     return node;
/*     */   }
/*     */   
/*     */   private Node getClosedNode(int paramInt1, int paramInt2, int paramInt3, PathType paramPathType) {
/* 290 */     Node node = getNode(paramInt1, paramInt2, paramInt3);
/* 291 */     node.closed = true;
/* 292 */     node.type = paramPathType;
/* 293 */     node.costMalus = paramPathType.getMalus();
/* 294 */     return node;
/*     */   }
/*     */   
/*     */   private Node tryJumpOn(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, Direction paramDirection, PathType paramPathType, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 298 */     Node node = findAcceptedNode(paramInt1, paramInt2 + 1, paramInt3, paramInt4 - 1, paramDouble, paramDirection, paramPathType);
/*     */     
/* 300 */     if (node == null) {
/* 301 */       return null;
/*     */     }
/*     */     
/* 304 */     if (this.mob.getBbWidth() >= 1.0F) {
/* 305 */       return node;
/*     */     }
/*     */     
/* 308 */     if (node.type != PathType.OPEN && node.type != PathType.WALKABLE) {
/* 309 */       return node;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     double d1 = (paramInt1 - paramDirection.getStepX()) + 0.5D;
/* 316 */     double d2 = (paramInt3 - paramDirection.getStepZ()) + 0.5D;
/* 317 */     double d3 = this.mob.getBbWidth() / 2.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     AABB aABB = new AABB(d1 - d3, getFloorLevel((BlockPos)paramMutableBlockPos.set(d1, (paramInt2 + 1), d2)) + 0.001D, d2 - d3, d1 + d3, this.mob.getBbHeight() + getFloorLevel((BlockPos)paramMutableBlockPos.set(node.x, node.y, node.z)) - 0.002D, d2 + d3);
/*     */ 
/*     */     
/* 326 */     return hasCollisions(aABB) ? null : node;
/*     */   }
/*     */ 
/*     */   
/*     */   private Node tryFindFirstNonWaterBelow(int paramInt1, int paramInt2, int paramInt3, Node paramNode) {
/* 331 */     for (; --paramInt2 > this.mob.level().getMinY(); paramInt2--) {
/* 332 */       PathType pathType = getCachedPathType(paramInt1, paramInt2, paramInt3);
/*     */       
/* 334 */       if (pathType == PathType.WATER) {
/* 335 */         paramNode = getNodeAndUpdateCostToMax(paramInt1, paramInt2, paramInt3, pathType, this.mob.getPathfindingMalus(pathType));
/*     */       } else {
/* 337 */         return paramNode;
/*     */       } 
/*     */     } 
/* 340 */     return paramNode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Node tryFindFirstGroundNodeBelow(int paramInt1, int paramInt2, int paramInt3) {
/* 346 */     for (int i = paramInt2 - 1; i >= this.mob.level().getMinY(); i--) {
/* 347 */       if (paramInt2 - i > this.mob.getMaxFallDistance()) {
/* 348 */         return getBlockedNode(paramInt1, i, paramInt3);
/*     */       }
/*     */       
/* 351 */       PathType pathType = getCachedPathType(paramInt1, i, paramInt3);
/* 352 */       float f = this.mob.getPathfindingMalus(pathType);
/*     */       
/* 354 */       if (pathType != PathType.OPEN) {
/* 355 */         if (f >= 0.0F) {
/* 356 */           return getNodeAndUpdateCostToMax(paramInt1, i, paramInt3, pathType, f);
/*     */         }
/* 358 */         return getBlockedNode(paramInt1, i, paramInt3);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 363 */     return getBlockedNode(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   private boolean hasCollisions(AABB paramAABB) {
/* 367 */     return this.collisionCache.computeIfAbsent(paramAABB, paramObject -> !this.currentContext.level().noCollision((Entity)this.mob, paramAABB));
/*     */   }
/*     */   
/*     */   protected PathType getCachedPathType(int paramInt1, int paramInt2, int paramInt3) {
/* 371 */     return (PathType)this.pathTypesByPosCacheByMob.computeIfAbsent(BlockPos.asLong(paramInt1, paramInt2, paramInt3), paramLong -> getPathTypeOfMob(this.currentContext, paramInt1, paramInt2, paramInt3, this.mob));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathTypeOfMob(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, Mob paramMob) {
/* 377 */     Set<PathType> set = getPathTypeWithinMobBB(paramPathfindingContext, paramInt1, paramInt2, paramInt3);
/*     */     
/* 379 */     if (set.contains(PathType.FENCE)) {
/* 380 */       return PathType.FENCE;
/*     */     }
/*     */     
/* 383 */     if (set.contains(PathType.UNPASSABLE_RAIL)) {
/* 384 */       return PathType.UNPASSABLE_RAIL;
/*     */     }
/*     */     
/* 387 */     PathType pathType = PathType.BLOCKED;
/* 388 */     for (PathType pathType1 : set) {
/*     */       
/* 390 */       if (paramMob.getPathfindingMalus(pathType1) < 0.0F) {
/* 391 */         return pathType1;
/*     */       }
/*     */ 
/*     */       
/* 395 */       if (paramMob.getPathfindingMalus(pathType1) >= paramMob.getPathfindingMalus(pathType)) {
/* 396 */         pathType = pathType1;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 401 */     if (this.entityWidth <= 1 && pathType != PathType.OPEN && paramMob.getPathfindingMalus(pathType) == 0.0F && getPathType(paramPathfindingContext, paramInt1, paramInt2, paramInt3) == PathType.OPEN) {
/* 402 */       return PathType.OPEN;
/*     */     }
/*     */     
/* 405 */     return pathType;
/*     */   }
/*     */   
/*     */   public Set<PathType> getPathTypeWithinMobBB(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3) {
/* 409 */     EnumSet<PathType> enumSet = EnumSet.noneOf(PathType.class);
/* 410 */     for (byte b = 0; b < this.entityWidth; b++) {
/* 411 */       for (byte b1 = 0; b1 < this.entityHeight; b1++) {
/* 412 */         for (byte b2 = 0; b2 < this.entityDepth; b2++) {
/* 413 */           int i = b + paramInt1;
/* 414 */           int j = b1 + paramInt2;
/* 415 */           int k = b2 + paramInt3;
/*     */           
/* 417 */           PathType pathType = getPathType(paramPathfindingContext, i, j, k);
/*     */           
/* 419 */           BlockPos blockPos = this.mob.blockPosition();
/* 420 */           boolean bool = canPassDoors();
/* 421 */           if (pathType == PathType.DOOR_WOOD_CLOSED && canOpenDoors() && bool) {
/* 422 */             pathType = PathType.WALKABLE_DOOR;
/*     */           }
/* 424 */           if (pathType == PathType.DOOR_OPEN && !bool) {
/* 425 */             pathType = PathType.BLOCKED;
/*     */           }
/* 427 */           if (pathType == PathType.RAIL && getPathType(paramPathfindingContext, blockPos.getX(), blockPos.getY(), blockPos.getZ()) != PathType.RAIL && getPathType(paramPathfindingContext, blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()) != PathType.RAIL) {
/* 428 */             pathType = PathType.UNPASSABLE_RAIL;
/*     */           }
/*     */           
/* 431 */           enumSet.add(pathType);
/*     */         } 
/*     */       } 
/*     */     } 
/* 435 */     return enumSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathType getPathType(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3) {
/* 440 */     return getPathTypeStatic(paramPathfindingContext, new BlockPos.MutableBlockPos(paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */   
/*     */   public static PathType getPathTypeStatic(Mob paramMob, BlockPos paramBlockPos) {
/* 444 */     return getPathTypeStatic(new PathfindingContext((CollisionGetter)paramMob.level(), paramMob), paramBlockPos.mutable());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PathType getPathTypeStatic(PathfindingContext paramPathfindingContext, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 455 */     int i = paramMutableBlockPos.getX();
/* 456 */     int j = paramMutableBlockPos.getY();
/* 457 */     int k = paramMutableBlockPos.getZ();
/*     */     
/* 459 */     PathType pathType = paramPathfindingContext.getPathTypeFromState(i, j, k);
/* 460 */     if (pathType != PathType.OPEN || j < paramPathfindingContext.level().getMinY() + 1) {
/* 461 */       return pathType;
/*     */     }
/*     */     
/* 464 */     switch (paramPathfindingContext.getPathTypeFromState(i, j - 1, k)) { case OPEN: case WATER: case LAVA: case WALKABLE: case DAMAGE_FIRE: case DAMAGE_OTHER: case STICKY_HONEY: case POWDER_SNOW: case DAMAGE_CAUTIOUS: case TRAPDOOR:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 474 */       checkNeighbourBlocks(paramPathfindingContext, i, j, k, PathType.WALKABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static PathType checkNeighbourBlocks(PathfindingContext paramPathfindingContext, int paramInt1, int paramInt2, int paramInt3, PathType paramPathType) {
/* 479 */     for (byte b = -1; b <= 1; b++) {
/* 480 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 481 */         for (byte b2 = -1; b2 <= 1; b2++) {
/* 482 */           if (b != 0 || b2 != 0) {
/* 483 */             PathType pathType = paramPathfindingContext.getPathTypeFromState(paramInt1 + b, paramInt2 + b1, paramInt3 + b2);
/*     */             
/* 485 */             if (pathType == PathType.DAMAGE_OTHER)
/* 486 */               return PathType.DANGER_OTHER; 
/* 487 */             if (pathType == PathType.DAMAGE_FIRE || pathType == PathType.LAVA)
/* 488 */               return PathType.DANGER_FIRE; 
/* 489 */             if (pathType == PathType.WATER)
/* 490 */               return PathType.WATER_BORDER; 
/* 491 */             if (pathType == PathType.DAMAGE_CAUTIOUS) {
/* 492 */               return PathType.DAMAGE_CAUTIOUS;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 498 */     return paramPathType;
/*     */   }
/*     */   
/*     */   protected static PathType getPathTypeFromState(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 502 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos);
/* 503 */     Block block = blockState.getBlock();
/*     */     
/* 505 */     if (blockState.isAir()) {
/* 506 */       return PathType.OPEN;
/*     */     }
/*     */     
/* 509 */     if (blockState.is(BlockTags.TRAPDOORS) || blockState.is(Blocks.LILY_PAD) || blockState.is(Blocks.BIG_DRIPLEAF)) {
/* 510 */       return PathType.TRAPDOOR;
/*     */     }
/*     */     
/* 513 */     if (blockState.is(Blocks.POWDER_SNOW)) {
/* 514 */       return PathType.POWDER_SNOW;
/*     */     }
/*     */     
/* 517 */     if (blockState.is(Blocks.CACTUS) || blockState.is(Blocks.SWEET_BERRY_BUSH)) {
/* 518 */       return PathType.DAMAGE_OTHER;
/*     */     }
/*     */     
/* 521 */     if (blockState.is(Blocks.HONEY_BLOCK)) {
/* 522 */       return PathType.STICKY_HONEY;
/*     */     }
/*     */     
/* 525 */     if (blockState.is(Blocks.COCOA)) {
/* 526 */       return PathType.COCOA;
/*     */     }
/*     */     
/* 529 */     if (blockState.is(Blocks.WITHER_ROSE) || blockState.is(Blocks.POINTED_DRIPSTONE)) {
/* 530 */       return PathType.DAMAGE_CAUTIOUS;
/*     */     }
/*     */     
/* 533 */     FluidState fluidState = blockState.getFluidState();
/* 534 */     if (fluidState.is(FluidTags.LAVA)) {
/* 535 */       return PathType.LAVA;
/*     */     }
/*     */     
/* 538 */     if (isBurningBlock(blockState)) {
/* 539 */       return PathType.DAMAGE_FIRE;
/*     */     }
/*     */     
/* 542 */     if (block instanceof DoorBlock) { DoorBlock doorBlock = (DoorBlock)block;
/* 543 */       if (((Boolean)blockState.getValue((Property)DoorBlock.OPEN)).booleanValue()) {
/* 544 */         return PathType.DOOR_OPEN;
/*     */       }
/* 546 */       return doorBlock.type().canOpenByHand() ? PathType.DOOR_WOOD_CLOSED : PathType.DOOR_IRON_CLOSED; }
/*     */ 
/*     */     
/* 549 */     if (block instanceof net.minecraft.world.level.block.BaseRailBlock) {
/* 550 */       return PathType.RAIL;
/*     */     }
/*     */     
/* 553 */     if (block instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 554 */       return PathType.LEAVES;
/*     */     }
/*     */     
/* 557 */     if (blockState.is(BlockTags.FENCES) || blockState.is(BlockTags.WALLS) || (block instanceof FenceGateBlock && !((Boolean)blockState.getValue((Property)FenceGateBlock.OPEN)).booleanValue())) {
/* 558 */       return PathType.FENCE;
/*     */     }
/*     */ 
/*     */     
/* 562 */     if (!blockState.isPathfindable(PathComputationType.LAND)) {
/* 563 */       return PathType.BLOCKED;
/*     */     }
/*     */     
/* 566 */     if (fluidState.is(FluidTags.WATER)) {
/* 567 */       return PathType.WATER;
/*     */     }
/*     */     
/* 570 */     return PathType.OPEN;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\WalkNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */