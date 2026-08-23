/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ 
/*    */ public class BreedGoal extends Goal {
/* 12 */   private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
/*    */   
/*    */   protected final Animal animal;
/*    */   private final Class<? extends Animal> partnerClass;
/*    */   protected final ServerLevel level;
/*    */   protected Animal partner;
/*    */   private int loveTime;
/*    */   private final double speedModifier;
/*    */   
/*    */   public BreedGoal(Animal paramAnimal, double paramDouble) {
/* 22 */     this(paramAnimal, paramDouble, (Class)paramAnimal.getClass());
/*    */   }
/*    */   
/*    */   public BreedGoal(Animal paramAnimal, double paramDouble, Class<? extends Animal> paramClass) {
/* 26 */     this.animal = paramAnimal;
/* 27 */     this.level = getServerLevel((Entity)paramAnimal);
/* 28 */     this.partnerClass = paramClass;
/* 29 */     this.speedModifier = paramDouble;
/* 30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 35 */     if (!this.animal.isInLove()) {
/* 36 */       return false;
/*    */     }
/* 38 */     this.partner = getFreePartner();
/* 39 */     return (this.partner != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 44 */     return (this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60 && !this.partner.isPanicking());
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 49 */     this.partner = null;
/* 50 */     this.loveTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 55 */     this.animal.getLookControl().setLookAt((Entity)this.partner, 10.0F, this.animal.getMaxHeadXRot());
/* 56 */     this.animal.getNavigation().moveTo((Entity)this.partner, this.speedModifier);
/* 57 */     this.loveTime++;
/* 58 */     if (this.loveTime >= adjustedTickDelay(60) && this.animal.distanceToSqr((Entity)this.partner) < 9.0D) {
/* 59 */       breed();
/*    */     }
/*    */   }
/*    */   
/*    */   private Animal getFreePartner() {
/* 64 */     List list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, (LivingEntity)this.animal, this.animal.getBoundingBox().inflate(8.0D));
/* 65 */     double d = Double.MAX_VALUE;
/* 66 */     Animal animal = null;
/* 67 */     for (Animal animal1 : list) {
/* 68 */       if (this.animal.canMate(animal1) && !animal1.isPanicking() && this.animal.distanceToSqr((Entity)animal1) < d) {
/* 69 */         animal = animal1;
/* 70 */         d = this.animal.distanceToSqr((Entity)animal1);
/*    */       } 
/*    */     } 
/* 73 */     return animal;
/*    */   }
/*    */   
/*    */   protected void breed() {
/* 77 */     this.animal.spawnChildFromBreeding(this.level, this.partner);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\BreedGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */