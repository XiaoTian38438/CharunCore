/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.goal.WrappedGoal;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public abstract class PathfinderMob
/*    */   extends Mob {
/*    */   protected static final float DEFAULT_WALK_TARGET_VALUE = 0.0F;
/*    */   
/*    */   protected PathfinderMob(EntityType<? extends PathfinderMob> paramEntityType, Level paramLevel) {
/* 17 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public float getWalkTargetValue(BlockPos paramBlockPos) {
/* 21 */     return getWalkTargetValue(paramBlockPos, (LevelReader)level());
/*    */   }
/*    */   
/*    */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 25 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkSpawnRules(LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason) {
/* 30 */     return (getWalkTargetValue(blockPosition(), (LevelReader)paramLevelAccessor) >= 0.0F);
/*    */   }
/*    */   
/*    */   public boolean isPathFinding() {
/* 34 */     return !getNavigation().isDone();
/*    */   }
/*    */   
/*    */   public boolean isPanicking() {
/* 38 */     if (this.brain.hasMemoryValue(MemoryModuleType.IS_PANICKING)) {
/* 39 */       return this.brain.getMemory(MemoryModuleType.IS_PANICKING).isPresent();
/*    */     }
/* 41 */     for (WrappedGoal wrappedGoal : this.goalSelector.getAvailableGoals()) {
/* 42 */       if (wrappedGoal.isRunning() && wrappedGoal.getGoal() instanceof net.minecraft.world.entity.ai.goal.PanicGoal) {
/* 43 */         return true;
/*    */       }
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldStayCloseToLeashHolder() {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void closeRangeLeashBehaviour(Entity paramEntity) {
/* 56 */     super.closeRangeLeashBehaviour(paramEntity);
/* 57 */     if (shouldStayCloseToLeashHolder() && !isPanicking()) {
/* 58 */       this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
/* 59 */       float f1 = 2.0F;
/* 60 */       float f2 = distanceTo(paramEntity);
/*    */       
/* 62 */       Vec3 vec3 = (new Vec3(paramEntity.getX() - getX(), paramEntity.getY() - getY(), paramEntity.getZ() - getZ())).normalize().scale(Math.max(f2 - 2.0F, 0.0F));
/* 63 */       getNavigation().moveTo(getX() + vec3.x, getY() + vec3.y, getZ() + vec3.z, followLeashSpeed());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void whenLeashedTo(Entity paramEntity) {
/* 69 */     setHomeTo(paramEntity.blockPosition(), (int)leashElasticDistance() - 1);
/* 70 */     super.whenLeashedTo(paramEntity);
/*    */   }
/*    */   
/*    */   protected double followLeashSpeed() {
/* 74 */     return 1.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\PathfinderMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */