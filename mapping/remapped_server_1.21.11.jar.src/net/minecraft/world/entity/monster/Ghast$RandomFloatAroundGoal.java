/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomFloatAroundGoal
/*     */   extends Goal
/*     */ {
/*     */   private static final int MAX_ATTEMPTS = 64;
/*     */   private final Mob ghast;
/*     */   private final int distanceToBlocks;
/*     */   
/*     */   public RandomFloatAroundGoal(Mob paramMob) {
/* 300 */     this(paramMob, 0);
/*     */   }
/*     */   
/*     */   public RandomFloatAroundGoal(Mob paramMob, int paramInt) {
/* 304 */     this.ghast = paramMob;
/* 305 */     this.distanceToBlocks = paramInt;
/* 306 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 311 */     MoveControl moveControl = this.ghast.getMoveControl();
/* 312 */     if (!moveControl.hasWanted()) {
/* 313 */       return true;
/*     */     }
/*     */     
/* 316 */     double d1 = moveControl.getWantedX() - this.ghast.getX();
/* 317 */     double d2 = moveControl.getWantedY() - this.ghast.getY();
/* 318 */     double d3 = moveControl.getWantedZ() - this.ghast.getZ();
/*     */     
/* 320 */     double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/*     */     
/* 322 */     return (d4 < 1.0D || d4 > 3600.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 332 */     Vec3 vec3 = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
/* 333 */     this.ghast.getMoveControl().setWantedPosition(vec3.x(), vec3.y(), vec3.z(), 1.0D);
/*     */   }
/*     */   
/*     */   public static Vec3 getSuitableFlyToPosition(Mob paramMob, int paramInt) {
/* 337 */     Level level = paramMob.level();
/* 338 */     RandomSource randomSource = paramMob.getRandom();
/* 339 */     Vec3 vec31 = paramMob.position();
/* 340 */     Vec3 vec32 = null;
/* 341 */     for (byte b = 0; b < 64; b++) {
/* 342 */       vec32 = chooseRandomPositionWithRestriction(paramMob, vec31, randomSource);
/* 343 */       if (vec32 != null && isGoodTarget(level, vec32, paramInt)) {
/* 344 */         return vec32;
/*     */       }
/*     */     } 
/* 347 */     if (vec32 == null) {
/* 348 */       vec32 = chooseRandomPosition(vec31, randomSource);
/*     */     }
/*     */ 
/*     */     
/* 352 */     BlockPos blockPos = BlockPos.containing((Position)vec32);
/* 353 */     int i = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ());
/* 354 */     if (i < blockPos.getY() && i > level.getMinY()) {
/* 355 */       vec32 = new Vec3(vec32.x(), paramMob.getY() - Math.abs(paramMob.getY() - vec32.y()), vec32.z());
/*     */     }
/* 357 */     return vec32;
/*     */   }
/*     */   
/*     */   private static boolean isGoodTarget(Level paramLevel, Vec3 paramVec3, int paramInt) {
/* 361 */     if (paramInt <= 0) {
/* 362 */       return true;
/*     */     }
/* 364 */     BlockPos blockPos = BlockPos.containing((Position)paramVec3);
/* 365 */     if (!paramLevel.getBlockState(blockPos).isAir()) {
/* 366 */       return false;
/*     */     }
/*     */     
/* 369 */     for (Direction direction : Direction.values()) {
/* 370 */       for (byte b = 1; b < paramInt; b++) {
/* 371 */         BlockPos blockPos1 = blockPos.relative(direction, b);
/* 372 */         if (!paramLevel.getBlockState(blockPos1).isAir()) {
/* 373 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 377 */     return false;
/*     */   }
/*     */   
/*     */   private static Vec3 chooseRandomPosition(Vec3 paramVec3, RandomSource paramRandomSource) {
/* 381 */     double d1 = paramVec3.x() + ((paramRandomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 382 */     double d2 = paramVec3.y() + ((paramRandomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 383 */     double d3 = paramVec3.z() + ((paramRandomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 384 */     return new Vec3(d1, d2, d3);
/*     */   }
/*     */   
/*     */   private static Vec3 chooseRandomPositionWithRestriction(Mob paramMob, Vec3 paramVec3, RandomSource paramRandomSource) {
/* 388 */     Vec3 vec3 = chooseRandomPosition(paramVec3, paramRandomSource);
/* 389 */     if (paramMob.hasHome() && !paramMob.isWithinHome(vec3)) {
/* 390 */       return null;
/*     */     }
/* 392 */     return vec3;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Ghast$RandomFloatAroundGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */