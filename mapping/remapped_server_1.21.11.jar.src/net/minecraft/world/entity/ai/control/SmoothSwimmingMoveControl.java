/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ 
/*    */ public class SmoothSwimmingMoveControl
/*    */   extends MoveControl {
/*    */   private static final float FULL_SPEED_TURN_THRESHOLD = 10.0F;
/*    */   private static final float STOP_TURN_THRESHOLD = 60.0F;
/*    */   private final int maxTurnX;
/*    */   private final int maxTurnY;
/*    */   private final float inWaterSpeedModifier;
/*    */   private final float outsideWaterSpeedModifier;
/*    */   private final boolean applyGravity;
/*    */   
/*    */   public SmoothSwimmingMoveControl(Mob paramMob, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, boolean paramBoolean) {
/* 18 */     super(paramMob);
/* 19 */     this.maxTurnX = paramInt1;
/* 20 */     this.maxTurnY = paramInt2;
/* 21 */     this.inWaterSpeedModifier = paramFloat1;
/* 22 */     this.outsideWaterSpeedModifier = paramFloat2;
/* 23 */     this.applyGravity = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 28 */     if (this.applyGravity && this.mob.isInWater())
/*    */     {
/* 30 */       this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*    */     }
/*    */     
/* 33 */     if (this.operation != MoveControl.Operation.MOVE_TO || this.mob.getNavigation().isDone()) {
/*    */       
/* 35 */       this.mob.setSpeed(0.0F);
/* 36 */       this.mob.setXxa(0.0F);
/* 37 */       this.mob.setYya(0.0F);
/* 38 */       this.mob.setZza(0.0F);
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     double d1 = this.wantedX - this.mob.getX();
/* 43 */     double d2 = this.wantedY - this.mob.getY();
/* 44 */     double d3 = this.wantedZ - this.mob.getZ();
/* 45 */     double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/*    */     
/* 47 */     if (d4 < 2.500000277905201E-7D) {
/* 48 */       this.mob.setZza(0.0F);
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     float f1 = (float)(Mth.atan2(d3, d1) * 57.2957763671875D) - 90.0F;
/* 53 */     this.mob.setYRot(rotlerp(this.mob.getYRot(), f1, this.maxTurnY));
/* 54 */     this.mob.yBodyRot = this.mob.getYRot();
/* 55 */     this.mob.yHeadRot = this.mob.getYRot();
/*    */     
/* 57 */     float f2 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 58 */     if (this.mob.isInWater()) {
/* 59 */       this.mob.setSpeed(f2 * this.inWaterSpeedModifier);
/*    */       
/* 61 */       double d = Math.sqrt(d1 * d1 + d3 * d3);
/* 62 */       if (Math.abs(d2) > 9.999999747378752E-6D || Math.abs(d) > 9.999999747378752E-6D) {
/* 63 */         float f = -((float)(Mth.atan2(d2, d) * 57.2957763671875D));
/* 64 */         f = Mth.clamp(Mth.wrapDegrees(f), -this.maxTurnX, this.maxTurnX);
/* 65 */         this.mob.setXRot(rotateTowards(this.mob.getXRot(), f, 5.0F));
/*    */       } 
/*    */       
/* 68 */       float f3 = Mth.cos((this.mob.getXRot() * 0.017453292F));
/* 69 */       float f4 = Mth.sin((this.mob.getXRot() * 0.017453292F));
/* 70 */       this.mob.zza = f3 * f2;
/* 71 */       this.mob.yya = -f4 * f2;
/*    */     } else {
/* 73 */       float f3 = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - f1));
/* 74 */       float f4 = getTurningSpeedFactor(f3);
/*    */       
/* 76 */       this.mob.setSpeed(f2 * this.outsideWaterSpeedModifier * f4);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static float getTurningSpeedFactor(float paramFloat) {
/* 81 */     return 1.0F - Mth.clamp((paramFloat - 10.0F) / 50.0F, 0.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\control\SmoothSwimmingMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */