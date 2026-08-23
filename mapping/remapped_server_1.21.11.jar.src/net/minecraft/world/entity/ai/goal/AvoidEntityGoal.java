/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AvoidEntityGoal<T extends LivingEntity> extends Goal {
/*    */   protected final PathfinderMob mob;
/*    */   private final double walkSpeedModifier;
/*    */   private final double sprintSpeedModifier;
/*    */   protected T toAvoid;
/*    */   protected final float maxDist;
/*    */   protected Path path;
/*    */   protected final PathNavigation pathNav;
/*    */   protected final Class<T> avoidClass;
/*    */   protected final Predicate<? super LivingEntity> avoidPredicate;
/*    */   protected final Predicate<? super LivingEntity> predicateOnAvoidEntity;
/*    */   private final TargetingConditions avoidEntityTargeting;
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob paramPathfinderMob, Class<T> paramClass, float paramFloat, double paramDouble1, double paramDouble2) {
/* 30 */     this(paramPathfinderMob, paramClass, paramLivingEntity -> true, paramFloat, paramDouble1, paramDouble2, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/*    */   }
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob paramPathfinderMob, Class<T> paramClass, Predicate<LivingEntity> paramPredicate, float paramFloat, double paramDouble1, double paramDouble2, Predicate<? super LivingEntity> paramPredicate1) {
/* 34 */     this.mob = paramPathfinderMob;
/* 35 */     this.avoidClass = paramClass;
/* 36 */     this.avoidPredicate = paramPredicate;
/* 37 */     this.maxDist = paramFloat;
/* 38 */     this.walkSpeedModifier = paramDouble1;
/* 39 */     this.sprintSpeedModifier = paramDouble2;
/* 40 */     this.predicateOnAvoidEntity = paramPredicate1;
/* 41 */     this.pathNav = paramPathfinderMob.getNavigation();
/* 42 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */     
/* 44 */     this.avoidEntityTargeting = TargetingConditions.forCombat().range(paramFloat).selector((paramLivingEntity, paramServerLevel) -> (paramPredicate1.test(paramLivingEntity) && paramPredicate2.test(paramLivingEntity)));
/*    */   }
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob paramPathfinderMob, Class<T> paramClass, float paramFloat, double paramDouble1, double paramDouble2, Predicate<? super LivingEntity> paramPredicate) {
/* 48 */     this(paramPathfinderMob, paramClass, paramLivingEntity -> true, paramFloat, paramDouble1, paramDouble2, paramPredicate);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 53 */     this.toAvoid = (T)getServerLevel((Entity)this.mob).getNearestEntity(this.mob.level().getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate(this.maxDist, 3.0D, this.maxDist), paramLivingEntity -> true), this.avoidEntityTargeting, (LivingEntity)this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
/* 54 */     if (this.toAvoid == null) {
/* 55 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 61 */     Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
/* 62 */     if (vec3 == null) {
/* 63 */       return false;
/*    */     }
/* 65 */     if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr((Entity)this.mob)) {
/* 66 */       return false;
/*    */     }
/* 68 */     this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
/* 69 */     return (this.path != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 74 */     return !this.pathNav.isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 79 */     this.pathNav.moveTo(this.path, this.walkSpeedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 84 */     this.toAvoid = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 89 */     if (this.mob.distanceToSqr((Entity)this.toAvoid) < 49.0D) {
/* 90 */       this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
/*    */     } else {
/* 92 */       this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\AvoidEntityGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */