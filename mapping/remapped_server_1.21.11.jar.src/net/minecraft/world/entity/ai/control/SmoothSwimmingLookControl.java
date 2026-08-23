/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class SmoothSwimmingLookControl extends LookControl {
/*    */   private final int maxYRotFromCenter;
/*    */   private static final int HEAD_TILT_X = 10;
/*    */   private static final int HEAD_TILT_Y = 20;
/*    */   
/*    */   public SmoothSwimmingLookControl(Mob paramMob, int paramInt) {
/* 12 */     super(paramMob);
/* 13 */     this.maxYRotFromCenter = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.lookAtCooldown > 0) {
/* 19 */       this.lookAtCooldown--;
/*    */       
/* 21 */       getYRotD().ifPresent(paramFloat -> this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, paramFloat.floatValue() + 20.0F, this.yMaxRotSpeed));
/* 22 */       getXRotD().ifPresent(paramFloat -> this.mob.setXRot(rotateTowards(this.mob.getXRot(), paramFloat.floatValue() + 10.0F, this.xMaxRotAngle)));
/*    */     } else {
/* 24 */       if (this.mob.getNavigation().isDone()) {
/* 25 */         this.mob.setXRot(rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
/*    */       }
/* 27 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
/*    */     } 
/*    */     
/* 30 */     float f = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
/*    */ 
/*    */     
/* 33 */     if (f < -this.maxYRotFromCenter) {
/* 34 */       this.mob.yBodyRot -= 4.0F;
/* 35 */     } else if (f > this.maxYRotFromCenter) {
/* 36 */       this.mob.yBodyRot += 4.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\control\SmoothSwimmingLookControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */