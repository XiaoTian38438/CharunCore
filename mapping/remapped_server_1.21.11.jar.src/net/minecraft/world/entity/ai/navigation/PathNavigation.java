/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PathNavigation
/*     */ {
/*     */   private static final int MAX_TIME_RECOMPUTE = 20;
/*     */   private static final int STUCK_CHECK_INTERVAL = 100;
/*     */   private static final float STUCK_THRESHOLD_DISTANCE_FACTOR = 0.25F;
/*     */   protected final Mob mob;
/*     */   protected final Level level;
/*     */   protected Path path;
/*     */   protected double speedModifier;
/*     */   protected int tick;
/*     */   protected int lastStuckCheck;
/*  62 */   protected Vec3 lastStuckCheckPos = Vec3.ZERO;
/*  63 */   protected Vec3i timeoutCachedNode = Vec3i.ZERO;
/*     */   protected long timeoutTimer;
/*     */   protected long lastTimeoutCheck;
/*     */   protected double timeoutLimit;
/*  67 */   protected float maxDistanceToWaypoint = 0.5F;
/*     */   
/*     */   protected boolean hasDelayedRecomputation;
/*     */   
/*     */   protected long timeLastRecompute;
/*     */   
/*     */   protected NodeEvaluator nodeEvaluator;
/*     */   private BlockPos targetPos;
/*     */   private int reachRange;
/*  76 */   private float maxVisitedNodesMultiplier = 1.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   private final PathFinder pathFinder;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isStuck;
/*     */ 
/*     */ 
/*     */   
/*  88 */   private float requiredPathLength = 16.0F;
/*     */   
/*     */   public PathNavigation(Mob paramMob, Level paramLevel) {
/*  91 */     this.mob = paramMob;
/*  92 */     this.level = paramLevel;
/*  93 */     this.pathFinder = createPathFinder(Mth.floor(paramMob.getAttributeBaseValue(Attributes.FOLLOW_RANGE) * 16.0D));
/*  94 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  95 */       ServerDebugSubscribers serverDebugSubscribers = serverLevel.getServer().debugSubscribers();
/*  96 */       this.pathFinder.setCaptureDebug(() -> paramServerDebugSubscribers.hasAnySubscriberFor(DebugSubscriptions.ENTITY_PATHS)); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updatePathfinderMaxVisitedNodes() {
/* 103 */     int i = Mth.floor(getMaxPathLength() * 16.0F);
/* 104 */     this.pathFinder.setMaxVisitedNodes(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredPathLength(float paramFloat) {
/* 112 */     this.requiredPathLength = paramFloat;
/*     */     
/* 114 */     updatePathfinderMaxVisitedNodes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getMaxPathLength() {
/* 121 */     return Math.max((float)this.mob.getAttributeValue(Attributes.FOLLOW_RANGE), this.requiredPathLength);
/*     */   }
/*     */   
/*     */   public void resetMaxVisitedNodesMultiplier() {
/* 125 */     this.maxVisitedNodesMultiplier = 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxVisitedNodesMultiplier(float paramFloat) {
/* 132 */     this.maxVisitedNodesMultiplier = paramFloat;
/*     */   }
/*     */   
/*     */   public BlockPos getTargetPos() {
/* 136 */     return this.targetPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpeedModifier(double paramDouble) {
/* 142 */     this.speedModifier = paramDouble;
/*     */   }
/*     */   
/*     */   public void recomputePath() {
/* 146 */     if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
/* 147 */       if (this.targetPos != null) {
/* 148 */         this.path = null;
/* 149 */         this.path = createPath(this.targetPos, this.reachRange);
/* 150 */         this.timeLastRecompute = this.level.getGameTime();
/* 151 */         this.hasDelayedRecomputation = false;
/*     */       } 
/*     */     } else {
/* 154 */       this.hasDelayedRecomputation = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public final Path createPath(double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) {
/* 159 */     return createPath(BlockPos.containing(paramDouble1, paramDouble2, paramDouble3), paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath(Stream<BlockPos> paramStream, int paramInt) {
/* 171 */     return createPath(paramStream.collect((Collector)Collectors.toSet()), 8, false, paramInt);
/*     */   }
/*     */   
/*     */   public Path createPath(Set<BlockPos> paramSet, int paramInt) {
/* 175 */     return createPath(paramSet, 8, false, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath(BlockPos paramBlockPos, int paramInt) {
/* 187 */     return createPath((Set<BlockPos>)ImmutableSet.of(paramBlockPos), 8, false, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath(BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 193 */     return createPath((Set<BlockPos>)ImmutableSet.of(paramBlockPos), 8, false, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath(Entity paramEntity, int paramInt) {
/* 205 */     return createPath((Set<BlockPos>)ImmutableSet.of(paramEntity.blockPosition()), 16, true, paramInt);
/*     */   }
/*     */   
/*     */   protected Path createPath(Set<BlockPos> paramSet, int paramInt1, boolean paramBoolean, int paramInt2) {
/* 209 */     return createPath(paramSet, paramInt1, paramBoolean, paramInt2, getMaxPathLength());
/*     */   }
/*     */   
/*     */   protected Path createPath(Set<BlockPos> paramSet, int paramInt1, boolean paramBoolean, int paramInt2, float paramFloat) {
/* 213 */     if (paramSet.isEmpty()) {
/* 214 */       return null;
/*     */     }
/*     */     
/* 217 */     if (this.mob.getY() < this.level.getMinY()) {
/* 218 */       return null;
/*     */     }
/*     */     
/* 221 */     if (!canUpdatePath()) {
/* 222 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 226 */     if (this.path != null && !this.path.isDone() && paramSet.contains(this.targetPos)) {
/* 227 */       return this.path;
/*     */     }
/*     */     
/* 230 */     ProfilerFiller profilerFiller = Profiler.get();
/* 231 */     profilerFiller.push("pathfind");
/* 232 */     BlockPos blockPos = paramBoolean ? this.mob.blockPosition().above() : this.mob.blockPosition();
/* 233 */     int i = (int)(paramFloat + paramInt1);
/*     */ 
/*     */     
/* 236 */     PathNavigationRegion pathNavigationRegion = new PathNavigationRegion(this.level, blockPos.offset(-i, -i, -i), blockPos.offset(i, i, i));
/* 237 */     Path path = this.pathFinder.findPath(pathNavigationRegion, this.mob, paramSet, paramFloat, paramInt2, this.maxVisitedNodesMultiplier);
/* 238 */     profilerFiller.pop();
/*     */     
/* 240 */     if (path != null && path.getTarget() != null) {
/*     */ 
/*     */ 
/*     */       
/* 244 */       this.targetPos = path.getTarget();
/* 245 */       this.reachRange = paramInt2;
/* 246 */       resetStuckTimeout();
/*     */     } 
/*     */     
/* 249 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 258 */     return moveTo(createPath(paramDouble1, paramDouble2, paramDouble3, 1), paramDouble4);
/*     */   }
/*     */   
/*     */   public boolean moveTo(double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double paramDouble4) {
/* 262 */     return moveTo(createPath(paramDouble1, paramDouble2, paramDouble3, paramInt), paramDouble4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(Entity paramEntity, double paramDouble) {
/* 271 */     Path path = createPath(paramEntity, 1);
/* 272 */     return (path != null && moveTo(path, paramDouble));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(Path paramPath, double paramDouble) {
/* 280 */     if (paramPath == null) {
/* 281 */       this.path = null;
/* 282 */       return false;
/*     */     } 
/* 284 */     if (!paramPath.sameAs(this.path)) {
/* 285 */       this.path = paramPath;
/*     */     }
/* 287 */     if (isDone()) {
/* 288 */       return false;
/*     */     }
/* 290 */     trimPath();
/* 291 */     if (this.path.getNodeCount() <= 0) {
/* 292 */       return false;
/*     */     }
/*     */     
/* 295 */     this.speedModifier = paramDouble;
/* 296 */     Vec3 vec3 = getTempMobPos();
/* 297 */     this.lastStuckCheck = this.tick;
/* 298 */     this.lastStuckCheckPos = vec3;
/* 299 */     return true;
/*     */   }
/*     */   
/*     */   public Path getPath() {
/* 303 */     return this.path;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 307 */     this.tick++;
/*     */     
/* 309 */     if (this.hasDelayedRecomputation) {
/* 310 */       recomputePath();
/*     */     }
/*     */     
/* 313 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 317 */     if (canUpdatePath()) {
/* 318 */       followThePath();
/* 319 */     } else if (this.path != null && !this.path.isDone()) {
/* 320 */       Vec3 vec31 = getTempMobPos();
/* 321 */       Vec3 vec32 = this.path.getNextEntityPos((Entity)this.mob);
/* 322 */       if (vec31.y > vec32.y && !this.mob.onGround() && Mth.floor(vec31.x) == Mth.floor(vec32.x) && Mth.floor(vec31.z) == Mth.floor(vec32.z)) {
/* 323 */         this.path.advance();
/*     */       }
/*     */     } 
/*     */     
/* 327 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 332 */     Vec3 vec3 = this.path.getNextEntityPos((Entity)this.mob);
/* 333 */     this.mob.getMoveControl().setWantedPosition(vec3.x, getGroundY(vec3), vec3.z, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getGroundY(Vec3 paramVec3) {
/* 338 */     BlockPos blockPos = BlockPos.containing((Position)paramVec3);
/* 339 */     return this.level.getBlockState(blockPos.below()).isAir() ? paramVec3.y : WalkNodeEvaluator.getFloorLevel((BlockGetter)this.level, blockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void followThePath() {
/* 346 */     Vec3 vec3 = getTempMobPos();
/*     */     
/* 348 */     this.maxDistanceToWaypoint = (this.mob.getBbWidth() > 0.75F) ? (this.mob.getBbWidth() / 2.0F) : (0.75F - this.mob.getBbWidth() / 2.0F);
/* 349 */     BlockPos blockPos = this.path.getNextNodePos();
/* 350 */     double d1 = Math.abs(this.mob.getX() - blockPos.getX() + 0.5D);
/* 351 */     double d2 = Math.abs(this.mob.getY() - blockPos.getY());
/* 352 */     double d3 = Math.abs(this.mob.getZ() - blockPos.getZ() + 0.5D);
/* 353 */     boolean bool = (d1 < this.maxDistanceToWaypoint && d3 < this.maxDistanceToWaypoint && d2 < 1.0D) ? true : false;
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (bool || (canCutCorner((this.path.getNextNode()).type) && shouldTargetNextNodeInDirection(vec3))) {
/* 358 */       this.path.advance();
/*     */     }
/* 360 */     doStuckDetection(vec3);
/*     */   }
/*     */   
/*     */   private boolean shouldTargetNextNodeInDirection(Vec3 paramVec3) {
/* 364 */     if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
/* 365 */       return false;
/*     */     }
/*     */     
/* 368 */     Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)this.path.getNextNodePos());
/* 369 */     if (!paramVec3.closerThan((Position)vec31, 2.0D))
/*     */     {
/*     */       
/* 372 */       return false;
/*     */     }
/*     */     
/* 375 */     if (canMoveDirectly(paramVec3, this.path.getNextEntityPos((Entity)this.mob))) {
/* 376 */       return true;
/*     */     }
/*     */     
/* 379 */     Vec3 vec32 = Vec3.atBottomCenterOf((Vec3i)this.path.getNodePos(this.path.getNextNodeIndex() + 1));
/*     */ 
/*     */     
/* 382 */     Vec3 vec33 = vec31.subtract(paramVec3);
/* 383 */     Vec3 vec34 = vec32.subtract(paramVec3);
/* 384 */     double d1 = vec33.lengthSqr();
/* 385 */     double d2 = vec34.lengthSqr();
/* 386 */     boolean bool1 = (d2 < d1) ? true : false;
/* 387 */     boolean bool2 = (d1 < 0.5D) ? true : false;
/* 388 */     if (bool1 || bool2) {
/* 389 */       Vec3 vec35 = vec33.normalize();
/* 390 */       Vec3 vec36 = vec34.normalize();
/* 391 */       return (vec36.dot(vec35) < 0.0D);
/*     */     } 
/*     */     
/* 394 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStuckDetection(Vec3 paramVec3) {
/* 399 */     if (this.tick - this.lastStuckCheck > 100) {
/*     */       
/* 401 */       float f1 = (this.mob.getSpeed() >= 1.0F) ? this.mob.getSpeed() : (this.mob.getSpeed() * this.mob.getSpeed());
/* 402 */       float f2 = f1 * 100.0F * 0.25F;
/* 403 */       if (paramVec3.distanceToSqr(this.lastStuckCheckPos) < (f2 * f2)) {
/* 404 */         this.isStuck = true;
/* 405 */         stop();
/*     */       } else {
/* 407 */         this.isStuck = false;
/*     */       } 
/* 409 */       this.lastStuckCheck = this.tick;
/* 410 */       this.lastStuckCheckPos = paramVec3;
/*     */     } 
/*     */     
/* 413 */     if (this.path != null && !this.path.isDone()) {
/* 414 */       BlockPos blockPos = this.path.getNextNodePos();
/*     */       
/* 416 */       long l = this.level.getGameTime();
/* 417 */       if (blockPos.equals(this.timeoutCachedNode)) {
/* 418 */         this.timeoutTimer += l - this.lastTimeoutCheck;
/*     */       } else {
/* 420 */         this.timeoutCachedNode = (Vec3i)blockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 426 */         double d = paramVec3.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
/* 427 */         this.timeoutLimit = (this.mob.getSpeed() > 0.0F) ? (d / this.mob.getSpeed() * 20.0D) : 0.0D;
/*     */       } 
/*     */       
/* 430 */       if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 3.0D) {
/* 431 */         timeoutPath();
/*     */       }
/* 433 */       this.lastTimeoutCheck = l;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void timeoutPath() {
/* 438 */     resetStuckTimeout();
/* 439 */     stop();
/*     */   }
/*     */   
/*     */   private void resetStuckTimeout() {
/* 443 */     this.timeoutCachedNode = Vec3i.ZERO;
/* 444 */     this.timeoutTimer = 0L;
/* 445 */     this.timeoutLimit = 0.0D;
/* 446 */     this.isStuck = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 451 */     return (this.path == null || this.path.isDone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInProgress() {
/* 458 */     return !isDone();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 462 */     this.path = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void trimPath() {
/* 474 */     if (this.path == null) {
/*     */       return;
/*     */     }
/*     */     
/* 478 */     for (byte b = 0; b < this.path.getNodeCount(); b++) {
/* 479 */       Node node1 = this.path.getNode(b);
/* 480 */       Node node2 = (b + 1 < this.path.getNodeCount()) ? this.path.getNode(b + 1) : null;
/*     */       
/* 482 */       BlockState blockState = this.level.getBlockState(new BlockPos(node1.x, node1.y, node1.z));
/*     */       
/* 484 */       if (blockState.is(BlockTags.CAULDRONS)) {
/* 485 */         this.path.replaceNode(b, node1.cloneAndMove(node1.x, node1.y + 1, node1.z));
/* 486 */         if (node2 != null && node1.y >= node2.y) {
/* 487 */           this.path.replaceNode(b + 1, node1.cloneAndMove(node2.x, node1.y + 1, node2.z));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canMoveDirectly(Vec3 paramVec31, Vec3 paramVec32) {
/* 494 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canCutCorner(PathType paramPathType) {
/* 504 */     return (paramPathType != PathType.DANGER_FIRE && paramPathType != PathType.DANGER_OTHER && paramPathType != PathType.WALKABLE_DOOR);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isClearForMovementBetween(Mob paramMob, Vec3 paramVec31, Vec3 paramVec32, boolean paramBoolean) {
/* 510 */     Vec3 vec3 = new Vec3(paramVec32.x, paramVec32.y + paramMob.getBbHeight() * 0.5D, paramVec32.z);
/* 511 */     return (paramMob.level().clip(new ClipContext(paramVec31, vec3, ClipContext.Block.COLLIDER, paramBoolean ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, (Entity)paramMob)).getType() == HitResult.Type.MISS);
/*     */   }
/*     */   
/*     */   public boolean isStableDestination(BlockPos paramBlockPos) {
/* 515 */     BlockPos blockPos = paramBlockPos.below();
/* 516 */     return this.level.getBlockState(blockPos).isSolidRender();
/*     */   }
/*     */   
/*     */   public NodeEvaluator getNodeEvaluator() {
/* 520 */     return this.nodeEvaluator;
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean paramBoolean) {
/* 524 */     this.nodeEvaluator.setCanFloat(paramBoolean);
/*     */   }
/*     */   
/*     */   public boolean canFloat() {
/* 528 */     return this.nodeEvaluator.canFloat();
/*     */   }
/*     */   
/*     */   public boolean shouldRecomputePath(BlockPos paramBlockPos) {
/* 532 */     if (this.hasDelayedRecomputation) {
/* 533 */       return false;
/*     */     }
/*     */     
/* 536 */     if (this.path == null || this.path.isDone() || this.path.getNodeCount() == 0) {
/* 537 */       return false;
/*     */     }
/*     */     
/* 540 */     Node node = this.path.getEndNode();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 545 */     Vec3 vec3 = new Vec3((node.x + this.mob.getX()) / 2.0D, (node.y + this.mob.getY()) / 2.0D, (node.z + this.mob.getZ()) / 2.0D);
/*     */ 
/*     */     
/* 548 */     return paramBlockPos.closerToCenterThan((Position)vec3, (this.path.getNodeCount() - this.path.getNextNodeIndex()));
/*     */   }
/*     */   
/*     */   public float getMaxDistanceToWaypoint() {
/* 552 */     return this.maxDistanceToWaypoint;
/*     */   }
/*     */   
/*     */   public boolean isStuck() {
/* 556 */     return this.isStuck;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanOpenDoors(boolean paramBoolean) {
/* 562 */     this.nodeEvaluator.setCanOpenDoors(paramBoolean);
/*     */   }
/*     */   
/*     */   protected abstract PathFinder createPathFinder(int paramInt);
/*     */   
/*     */   protected abstract Vec3 getTempMobPos();
/*     */   
/*     */   protected abstract boolean canUpdatePath();
/*     */   
/*     */   public abstract boolean canNavigateGround();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\navigation\PathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */