/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.block.Blocks;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TurtleGoHomeGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Turtle turtle;
/*     */   private final double speedModifier;
/*     */   private boolean stuck;
/*     */   private int closeToHomeTryTicks;
/*     */   private static final int GIVE_UP_TICKS = 600;
/*     */   
/*     */   TurtleGoHomeGoal(Turtle paramTurtle, double paramDouble) {
/* 426 */     this.turtle = paramTurtle;
/* 427 */     this.speedModifier = paramDouble;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 432 */     if (this.turtle.isBaby()) {
/* 433 */       return false;
/*     */     }
/*     */     
/* 436 */     if (this.turtle.hasEgg()) {
/* 437 */       return true;
/*     */     }
/*     */     
/* 440 */     if (this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0) {
/* 441 */       return false;
/*     */     }
/*     */     
/* 444 */     return !this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 64.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 449 */     this.turtle.goingHome = true;
/* 450 */     this.stuck = false;
/* 451 */     this.closeToHomeTryTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 456 */     this.turtle.goingHome = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 461 */     return (!this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= adjustedTickDelay(600));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 466 */     BlockPos blockPos = this.turtle.homePos;
/* 467 */     boolean bool = blockPos.closerToCenterThan((Position)this.turtle.position(), 16.0D);
/* 468 */     if (bool) {
/* 469 */       this.closeToHomeTryTicks++;
/*     */     }
/*     */     
/* 472 */     if (this.turtle.getNavigation().isDone()) {
/* 473 */       Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)blockPos);
/* 474 */       Vec3 vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, vec31, 0.3141592741012573D);
/* 475 */       if (vec32 == null) {
/* 476 */         vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, vec31, 1.5707963705062866D);
/*     */       }
/*     */       
/* 479 */       if (vec32 != null && !bool && !this.turtle.level().getBlockState(BlockPos.containing((Position)vec32)).is(Blocks.WATER))
/*     */       {
/* 481 */         vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 5, vec31, 1.5707963705062866D);
/*     */       }
/*     */       
/* 484 */       if (vec32 == null) {
/* 485 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 489 */       this.turtle.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleGoHomeGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */