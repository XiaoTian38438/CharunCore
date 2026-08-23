/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
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
/*     */ public class LongDistancePatrolGoal<T extends PatrollingMonster>
/*     */   extends Goal
/*     */ {
/*     */   private static final int NAVIGATION_FAILED_COOLDOWN = 200;
/*     */   private final T mob;
/*     */   private final double speedModifier;
/*     */   private final double leaderSpeedModifier;
/*     */   private long cooldownUntil;
/*     */   
/*     */   public LongDistancePatrolGoal(T paramT, double paramDouble1, double paramDouble2) {
/* 153 */     this.mob = paramT;
/* 154 */     this.speedModifier = paramDouble1;
/* 155 */     this.leaderSpeedModifier = paramDouble2;
/* 156 */     this.cooldownUntil = -1L;
/* 157 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 162 */     boolean bool = (this.mob.level().getGameTime() < this.cooldownUntil) ? true : false;
/* 163 */     return (this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.hasControllingPassenger() && this.mob.hasPatrolTarget() && !bool);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */   
/*     */   public void tick() {
/* 176 */     boolean bool = this.mob.isPatrolLeader();
/* 177 */     PathNavigation pathNavigation = this.mob.getNavigation();
/* 178 */     if (pathNavigation.isDone()) {
/* 179 */       List<PatrollingMonster> list = findPatrolCompanions();
/* 180 */       if (this.mob.isPatrolling() && list.isEmpty()) {
/* 181 */         this.mob.setPatrolling(false);
/* 182 */       } else if (!bool || !this.mob.getPatrolTarget().closerToCenterThan((Position)this.mob.position(), 10.0D)) {
/* 183 */         Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)this.mob.getPatrolTarget());
/*     */ 
/*     */         
/* 186 */         Vec3 vec32 = this.mob.position();
/* 187 */         Vec3 vec33 = vec32.subtract(vec31);
/*     */         
/* 189 */         vec31 = vec33.yRot(90.0F).scale(0.4D).add(vec31);
/*     */         
/* 191 */         Vec3 vec34 = vec31.subtract(vec32).normalize().scale(10.0D).add(vec32);
/* 192 */         BlockPos blockPos = BlockPos.containing((Position)vec34);
/* 193 */         blockPos = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos);
/*     */         
/* 195 */         if (!pathNavigation.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), bool ? this.leaderSpeedModifier : this.speedModifier)) {
/*     */           
/* 197 */           moveRandomly();
/* 198 */           this.cooldownUntil = this.mob.level().getGameTime() + 200L;
/* 199 */         } else if (bool) {
/* 200 */           for (PatrollingMonster patrollingMonster : list) {
/* 201 */             patrollingMonster.setPatrolTarget(blockPos);
/*     */           }
/*     */         } 
/*     */       } else {
/* 205 */         this.mob.findPatrolTarget();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<PatrollingMonster> findPatrolCompanions() {
/* 211 */     return this.mob.level().getEntitiesOfClass(PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0D), paramPatrollingMonster -> (paramPatrollingMonster.canJoinPatrol() && !paramPatrollingMonster.is((Entity)this.mob)));
/*     */   }
/*     */   
/*     */   private boolean moveRandomly() {
/* 215 */     RandomSource randomSource = this.mob.getRandom();
/* 216 */     BlockPos blockPos = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + randomSource.nextInt(16), 0, -8 + randomSource.nextInt(16)));
/* 217 */     return this.mob.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\PatrollingMonster$LongDistancePatrolGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */