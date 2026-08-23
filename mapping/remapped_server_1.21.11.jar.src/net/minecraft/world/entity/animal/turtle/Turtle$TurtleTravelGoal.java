/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TurtleTravelGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Turtle turtle;
/*     */   private final double speedModifier;
/*     */   private boolean stuck;
/*     */   
/*     */   TurtleTravelGoal(Turtle paramTurtle, double paramDouble) {
/* 348 */     this.turtle = paramTurtle;
/* 349 */     this.speedModifier = paramDouble;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 354 */     return (!this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 359 */     char c = 'Ȁ';
/* 360 */     byte b = 4;
/* 361 */     RandomSource randomSource = Turtle.access$000(this.turtle);
/* 362 */     int i = randomSource.nextInt(1025) - 512;
/* 363 */     int j = randomSource.nextInt(9) - 4;
/* 364 */     int k = randomSource.nextInt(1025) - 512;
/*     */     
/* 366 */     if (j + this.turtle.getY() > (this.turtle.level().getSeaLevel() - 1)) {
/* 367 */       j = 0;
/*     */     }
/* 369 */     this.turtle.travelPos = BlockPos.containing(i + this.turtle.getX(), j + this.turtle.getY(), k + this.turtle.getZ());
/* 370 */     this.stuck = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 375 */     if (this.turtle.travelPos == null) {
/* 376 */       this.stuck = true;
/*     */       
/*     */       return;
/*     */     } 
/* 380 */     if (this.turtle.getNavigation().isDone()) {
/* 381 */       Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)this.turtle.travelPos);
/* 382 */       Vec3 vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, vec31, 0.3141592741012573D);
/* 383 */       if (vec32 == null) {
/* 384 */         vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, vec31, 1.5707963705062866D);
/*     */       }
/*     */ 
/*     */       
/* 388 */       if (vec32 != null) {
/* 389 */         int i = Mth.floor(vec32.x);
/* 390 */         int j = Mth.floor(vec32.z);
/* 391 */         byte b = 34;
/* 392 */         if (!this.turtle.level().hasChunksAt(i - 34, j - 34, i + 34, j + 34)) {
/* 393 */           vec32 = null;
/*     */         }
/*     */       } 
/*     */       
/* 397 */       if (vec32 == null) {
/* 398 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 402 */       this.turtle.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 408 */     return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg());
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 413 */     this.turtle.travelPos = null;
/* 414 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleTravelGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */