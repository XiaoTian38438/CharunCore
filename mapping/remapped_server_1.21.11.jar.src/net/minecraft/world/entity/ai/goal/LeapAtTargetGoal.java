/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LeapAtTargetGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private LivingEntity target;
/*    */   private final float yd;
/*    */   
/*    */   public LeapAtTargetGoal(Mob paramMob, float paramFloat) {
/* 16 */     this.mob = paramMob;
/* 17 */     this.yd = paramFloat;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (this.mob.hasControllingPassenger()) {
/* 24 */       return false;
/*    */     }
/* 26 */     this.target = this.mob.getTarget();
/* 27 */     if (this.target == null) {
/* 28 */       return false;
/*    */     }
/* 30 */     double d = this.mob.distanceToSqr((Entity)this.target);
/* 31 */     if (d < 4.0D || d > 16.0D) {
/* 32 */       return false;
/*    */     }
/* 34 */     if (!this.mob.onGround()) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (this.mob.getRandom().nextInt(reducedTickDelay(5)) != 0) {
/* 38 */       return false;
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 45 */     return !this.mob.onGround();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 51 */     Vec3 vec31 = this.mob.getDeltaMovement();
/* 52 */     Vec3 vec32 = new Vec3(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
/* 53 */     if (vec32.lengthSqr() > 1.0E-7D) {
/* 54 */       vec32 = vec32.normalize().scale(0.4D).add(vec31.scale(0.2D));
/*    */     }
/*    */     
/* 57 */     this.mob.setDeltaMovement(vec32.x, this.yd, vec32.z);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\LeapAtTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */