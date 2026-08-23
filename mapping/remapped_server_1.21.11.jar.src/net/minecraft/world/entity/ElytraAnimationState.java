/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class ElytraAnimationState
/*    */ {
/*    */   private static final float DEFAULT_X_ROT = 0.2617994F;
/*    */   private static final float DEFAULT_Z_ROT = -0.2617994F;
/*    */   private float rotX;
/*    */   private float rotY;
/*    */   private float rotZ;
/*    */   private float rotXOld;
/*    */   private float rotYOld;
/*    */   private float rotZOld;
/*    */   private final LivingEntity entity;
/*    */   
/*    */   public ElytraAnimationState(LivingEntity paramLivingEntity) {
/* 20 */     this.entity = paramLivingEntity;
/*    */   }
/*    */   public void tick() {
/*    */     float f1, f2, f3;
/* 24 */     this.rotXOld = this.rotX;
/* 25 */     this.rotYOld = this.rotY;
/* 26 */     this.rotZOld = this.rotZ;
/*    */ 
/*    */ 
/*    */     
/* 30 */     if (this.entity.isFallFlying()) {
/*    */       
/* 32 */       float f = 1.0F;
/* 33 */       Vec3 vec3 = this.entity.getDeltaMovement();
/* 34 */       if (vec3.y < 0.0D) {
/* 35 */         Vec3 vec31 = vec3.normalize();
/* 36 */         f = 1.0F - (float)Math.pow(-vec31.y, 1.5D);
/*    */       } 
/*    */       
/* 39 */       f1 = Mth.lerp(f, 0.2617994F, 0.34906584F);
/* 40 */       f2 = Mth.lerp(f, -0.2617994F, -1.5707964F);
/* 41 */       f3 = 0.0F;
/* 42 */     } else if (this.entity.isCrouching()) {
/* 43 */       f1 = 0.6981317F;
/* 44 */       f2 = -0.7853982F;
/* 45 */       f3 = 0.08726646F;
/*    */     } else {
/* 47 */       f1 = 0.2617994F;
/* 48 */       f2 = -0.2617994F;
/* 49 */       f3 = 0.0F;
/*    */     } 
/* 51 */     this.rotX += (f1 - this.rotX) * 0.3F;
/* 52 */     this.rotY += (f3 - this.rotY) * 0.3F;
/* 53 */     this.rotZ += (f2 - this.rotZ) * 0.3F;
/*    */   }
/*    */   
/*    */   public float getRotX(float paramFloat) {
/* 57 */     return Mth.lerp(paramFloat, this.rotXOld, this.rotX);
/*    */   }
/*    */   
/*    */   public float getRotY(float paramFloat) {
/* 61 */     return Mth.lerp(paramFloat, this.rotYOld, this.rotY);
/*    */   }
/*    */   
/*    */   public float getRotZ(float paramFloat) {
/* 65 */     return Mth.lerp(paramFloat, this.rotZOld, this.rotZ);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ElytraAnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */