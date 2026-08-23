/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
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
/*     */ class BlazeAttackGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Blaze blaze;
/*     */   private int attackStep;
/*     */   private int attackTime;
/*     */   private int lastSeen;
/*     */   
/*     */   public BlazeAttackGoal(Blaze paramBlaze) {
/* 163 */     this.blaze = paramBlaze;
/*     */     
/* 165 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 170 */     LivingEntity livingEntity = this.blaze.getTarget();
/* 171 */     return (livingEntity != null && livingEntity.isAlive() && this.blaze.canAttack(livingEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 176 */     this.attackStep = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 181 */     this.blaze.setCharged(false);
/* 182 */     this.lastSeen = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 192 */     this.attackTime--;
/*     */     
/* 194 */     LivingEntity livingEntity = this.blaze.getTarget();
/*     */     
/* 196 */     if (livingEntity == null) {
/*     */       return;
/*     */     }
/*     */     
/* 200 */     boolean bool = this.blaze.getSensing().hasLineOfSight((Entity)livingEntity);
/*     */     
/* 202 */     if (bool) {
/* 203 */       this.lastSeen = 0;
/*     */     } else {
/* 205 */       this.lastSeen++;
/*     */     } 
/*     */     
/* 208 */     double d = this.blaze.distanceToSqr((Entity)livingEntity);
/*     */     
/* 210 */     if (d < 4.0D) {
/* 211 */       if (!bool) {
/*     */         return;
/*     */       }
/*     */       
/* 215 */       if (this.attackTime <= 0) {
/* 216 */         this.attackTime = 20;
/* 217 */         this.blaze.doHurtTarget(getServerLevel((Entity)this.blaze), (Entity)livingEntity);
/*     */       } 
/* 219 */       this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
/* 220 */     } else if (d < getFollowDistance() * getFollowDistance() && bool) {
/* 221 */       double d1 = livingEntity.getX() - this.blaze.getX();
/* 222 */       double d2 = livingEntity.getY(0.5D) - this.blaze.getY(0.5D);
/* 223 */       double d3 = livingEntity.getZ() - this.blaze.getZ();
/*     */       
/* 225 */       if (this.attackTime <= 0) {
/* 226 */         this.attackStep++;
/* 227 */         if (this.attackStep == 1) {
/* 228 */           this.attackTime = 60;
/* 229 */           this.blaze.setCharged(true);
/* 230 */         } else if (this.attackStep <= 4) {
/* 231 */           this.attackTime = 6;
/*     */         } else {
/* 233 */           this.attackTime = 100;
/* 234 */           this.attackStep = 0;
/* 235 */           this.blaze.setCharged(false);
/*     */         } 
/*     */         
/* 238 */         if (this.attackStep > 1) {
/* 239 */           double d4 = Math.sqrt(Math.sqrt(d)) * 0.5D;
/*     */           
/* 241 */           if (!this.blaze.isSilent()) {
/* 242 */             this.blaze.level().levelEvent(null, 1018, this.blaze.blockPosition(), 0);
/*     */           }
/* 244 */           for (byte b = 0; b < 1; b++) {
/* 245 */             Vec3 vec3 = new Vec3(this.blaze.getRandom().triangle(d1, 2.297D * d4), d2, this.blaze.getRandom().triangle(d3, 2.297D * d4));
/* 246 */             SmallFireball smallFireball = new SmallFireball(this.blaze.level(), (LivingEntity)this.blaze, vec3.normalize());
/* 247 */             smallFireball.setPos(smallFireball.getX(), this.blaze.getY(0.5D) + 0.5D, smallFireball.getZ());
/* 248 */             this.blaze.level().addFreshEntity((Entity)smallFireball);
/*     */           } 
/*     */         } 
/*     */       } 
/* 252 */       this.blaze.getLookControl().setLookAt((Entity)livingEntity, 10.0F, 10.0F);
/*     */     }
/* 254 */     else if (this.lastSeen < 5) {
/* 255 */       this.blaze.getMoveControl().setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 1.0D);
/*     */     } 
/*     */ 
/*     */     
/* 259 */     super.tick();
/*     */   }
/*     */   
/*     */   private double getFollowDistance() {
/* 263 */     return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Blaze$BlazeAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */