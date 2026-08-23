/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class OcelotAttackGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private LivingEntity target;
/*    */   private int attackTime;
/*    */   
/*    */   public OcelotAttackGoal(Mob paramMob) {
/* 15 */     this.mob = paramMob;
/* 16 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 21 */     LivingEntity livingEntity = this.mob.getTarget();
/* 22 */     if (livingEntity == null) {
/* 23 */       return false;
/*    */     }
/* 25 */     this.target = livingEntity;
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 31 */     if (!this.target.isAlive()) {
/* 32 */       return false;
/*    */     }
/* 34 */     if (this.mob.distanceToSqr((Entity)this.target) > 225.0D) {
/* 35 */       return false;
/*    */     }
/* 37 */     return (!this.mob.getNavigation().isDone() || canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 42 */     this.target = null;
/* 43 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresUpdateEveryTick() {
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 53 */     this.mob.getLookControl().setLookAt((Entity)this.target, 30.0F, 30.0F);
/*    */     
/* 55 */     double d1 = (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F);
/* 56 */     double d2 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
/*    */     
/* 58 */     double d3 = 0.8D;
/* 59 */     if (d2 > d1 && d2 < 16.0D) {
/* 60 */       d3 = 1.33D;
/* 61 */     } else if (d2 < 225.0D) {
/* 62 */       d3 = 0.6D;
/*    */     } 
/*    */     
/* 65 */     this.mob.getNavigation().moveTo((Entity)this.target, d3);
/*    */     
/* 67 */     this.attackTime = Math.max(this.attackTime - 1, 0);
/*    */     
/* 69 */     if (d2 > d1) {
/*    */       return;
/*    */     }
/* 72 */     if (this.attackTime > 0) {
/*    */       return;
/*    */     }
/* 75 */     this.attackTime = 20;
/* 76 */     this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\OcelotAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */