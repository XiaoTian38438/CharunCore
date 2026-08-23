/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class WalkAnimationState {
/*    */   private float speedOld;
/*    */   private float speed;
/*    */   private float position;
/*  9 */   private float positionScale = 1.0F;
/*    */   
/*    */   public void setSpeed(float paramFloat) {
/* 12 */     this.speed = paramFloat;
/*    */   }
/*    */   
/*    */   public void update(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 16 */     this.speedOld = this.speed;
/* 17 */     this.speed += (paramFloat1 - this.speed) * paramFloat2;
/* 18 */     this.position += this.speed;
/* 19 */     this.positionScale = paramFloat3;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 23 */     this.speedOld = 0.0F;
/* 24 */     this.speed = 0.0F;
/* 25 */     this.position = 0.0F;
/*    */   }
/*    */   
/*    */   public float speed() {
/* 29 */     return this.speed;
/*    */   }
/*    */   
/*    */   public float speed(float paramFloat) {
/* 33 */     return Math.min(Mth.lerp(paramFloat, this.speedOld, this.speed), 1.0F);
/*    */   }
/*    */   
/*    */   public float position() {
/* 37 */     return this.position * this.positionScale;
/*    */   }
/*    */   
/*    */   public float position(float paramFloat) {
/* 41 */     return (this.position - this.speed * (1.0F - paramFloat)) * this.positionScale;
/*    */   }
/*    */   
/*    */   public boolean isMoving() {
/* 45 */     return (this.speed > 1.0E-5F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\WalkAnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */