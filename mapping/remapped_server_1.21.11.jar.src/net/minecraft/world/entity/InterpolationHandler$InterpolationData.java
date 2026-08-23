/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class InterpolationData
/*    */ {
/*    */   protected int steps;
/*    */   Vec3 position;
/*    */   float yRot;
/*    */   float xRot;
/*    */   
/*    */   InterpolationData(int paramInt, Vec3 paramVec3, float paramFloat1, float paramFloat2) {
/* 22 */     this.steps = paramInt;
/* 23 */     this.position = paramVec3;
/* 24 */     this.yRot = paramFloat1;
/* 25 */     this.xRot = paramFloat2;
/*    */   }
/*    */   
/*    */   public void decrease() {
/* 29 */     this.steps--;
/*    */   }
/*    */   
/*    */   public void addDelta(Vec3 paramVec3) {
/* 33 */     this.position = this.position.add(paramVec3);
/*    */   }
/*    */   
/*    */   public void addRotation(float paramFloat1, float paramFloat2) {
/* 37 */     this.yRot += paramFloat1;
/* 38 */     this.xRot += paramFloat2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\InterpolationHandler$InterpolationData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */