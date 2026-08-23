/*     */ package net.minecraft.world.entity.ai.control;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LookControl
/*     */   implements Control {
/*     */   protected final Mob mob;
/*     */   protected float yMaxRotSpeed;
/*     */   protected float xMaxRotAngle;
/*     */   protected int lookAtCooldown;
/*     */   protected double wantedX;
/*     */   protected double wantedY;
/*     */   protected double wantedZ;
/*     */   
/*     */   public LookControl(Mob paramMob) {
/*  20 */     this.mob = paramMob;
/*     */   }
/*     */   
/*     */   public void setLookAt(Vec3 paramVec3) {
/*  24 */     setLookAt(paramVec3.x, paramVec3.y, paramVec3.z);
/*     */   }
/*     */   
/*     */   public void setLookAt(Entity paramEntity) {
/*  28 */     setLookAt(paramEntity.getX(), paramEntity.getEyeY(), paramEntity.getZ());
/*     */   }
/*     */   
/*     */   public void setLookAt(Entity paramEntity, float paramFloat1, float paramFloat2) {
/*  32 */     setLookAt(paramEntity.getX(), paramEntity.getEyeY(), paramEntity.getZ(), paramFloat1, paramFloat2);
/*     */   }
/*     */   
/*     */   public void setLookAt(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  36 */     setLookAt(paramDouble1, paramDouble2, paramDouble3, this.mob.getHeadRotSpeed(), this.mob.getMaxHeadXRot());
/*     */   }
/*     */   
/*     */   public void setLookAt(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
/*  40 */     this.wantedX = paramDouble1;
/*  41 */     this.wantedY = paramDouble2;
/*  42 */     this.wantedZ = paramDouble3;
/*  43 */     this.yMaxRotSpeed = paramFloat1;
/*  44 */     this.xMaxRotAngle = paramFloat2;
/*  45 */     this.lookAtCooldown = 2;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  49 */     if (resetXRotOnTick()) {
/*  50 */       this.mob.setXRot(0.0F);
/*     */     }
/*     */     
/*  53 */     if (this.lookAtCooldown > 0) {
/*  54 */       this.lookAtCooldown--;
/*  55 */       getYRotD().ifPresent(paramFloat -> this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, paramFloat.floatValue(), this.yMaxRotSpeed));
/*  56 */       getXRotD().ifPresent(paramFloat -> this.mob.setXRot(rotateTowards(this.mob.getXRot(), paramFloat.floatValue(), this.xMaxRotAngle)));
/*     */     } else {
/*  58 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
/*     */     } 
/*     */     
/*  61 */     clampHeadRotationToBody();
/*     */   }
/*     */   
/*     */   protected void clampHeadRotationToBody() {
/*  65 */     if (!this.mob.getNavigation().isDone())
/*     */     {
/*  67 */       this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean resetXRotOnTick() {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isLookingAtTarget() {
/*  77 */     return (this.lookAtCooldown > 0);
/*     */   }
/*     */   
/*     */   public double getWantedX() {
/*  81 */     return this.wantedX;
/*     */   }
/*     */   
/*     */   public double getWantedY() {
/*  85 */     return this.wantedY;
/*     */   }
/*     */   
/*     */   public double getWantedZ() {
/*  89 */     return this.wantedZ;
/*     */   }
/*     */   
/*     */   protected Optional<Float> getXRotD() {
/*  93 */     double d1 = this.wantedX - this.mob.getX();
/*  94 */     double d2 = this.wantedY - this.mob.getEyeY();
/*  95 */     double d3 = this.wantedZ - this.mob.getZ();
/*  96 */     double d4 = Math.sqrt(d1 * d1 + d3 * d3);
/*  97 */     return (Math.abs(d2) > 9.999999747378752E-6D || Math.abs(d4) > 9.999999747378752E-6D) ? Optional.<Float>of(Float.valueOf((float)-(Mth.atan2(d2, d4) * 57.2957763671875D))) : Optional.<Float>empty();
/*     */   }
/*     */   
/*     */   protected Optional<Float> getYRotD() {
/* 101 */     double d1 = this.wantedX - this.mob.getX();
/* 102 */     double d2 = this.wantedZ - this.mob.getZ();
/* 103 */     return (Math.abs(d2) > 9.999999747378752E-6D || Math.abs(d1) > 9.999999747378752E-6D) ? Optional.<Float>of(Float.valueOf((float)(Mth.atan2(d2, d1) * 57.2957763671875D) - 90.0F)) : Optional.<Float>empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\control\LookControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */