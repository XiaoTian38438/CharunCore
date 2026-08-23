/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LongJumpUtil
/*    */ {
/*    */   public static Optional<Vec3> calculateJumpVectorForAngle(Mob paramMob, Vec3 paramVec3, float paramFloat, int paramInt, boolean paramBoolean) {
/* 20 */     Vec3 vec31 = paramMob.position();
/*    */ 
/*    */     
/* 23 */     Vec3 vec32 = (new Vec3(paramVec3.x - vec31.x, 0.0D, paramVec3.z - vec31.z)).normalize().scale(0.5D);
/* 24 */     Vec3 vec33 = paramVec3.subtract(vec32);
/*    */     
/* 26 */     Vec3 vec34 = vec33.subtract(vec31);
/* 27 */     float f = paramInt * 3.1415927F / 180.0F;
/* 28 */     double d1 = Math.atan2(vec34.z, vec34.x);
/* 29 */     double d2 = vec34.subtract(0.0D, vec34.y, 0.0D).lengthSqr();
/* 30 */     double d3 = Math.sqrt(d2);
/* 31 */     double d4 = vec34.y;
/* 32 */     double d5 = paramMob.getGravity();
/*    */     
/* 34 */     double d6 = Math.sin((2.0F * f));
/* 35 */     double d7 = Math.pow(Math.cos(f), 2.0D);
/* 36 */     double d8 = Math.sin(f);
/* 37 */     double d9 = Math.cos(f);
/* 38 */     double d10 = Math.sin(d1);
/* 39 */     double d11 = Math.cos(d1);
/*    */     
/* 41 */     double d12 = d2 * d5 / (d3 * d6 - 2.0D * d4 * d7);
/* 42 */     if (d12 < 0.0D) {
/* 43 */       return Optional.empty();
/*    */     }
/*    */     
/* 46 */     double d13 = Math.sqrt(d12);
/* 47 */     if (d13 > paramFloat) {
/* 48 */       return Optional.empty();
/*    */     }
/*    */     
/* 51 */     double d14 = d13 * d9;
/* 52 */     double d15 = d13 * d8;
/*    */     
/* 54 */     if (paramBoolean) {
/*    */       
/* 56 */       int i = Mth.ceil(d3 / d14) * 2;
/* 57 */       double d = 0.0D;
/* 58 */       Vec3 vec3 = null;
/*    */       
/* 60 */       EntityDimensions entityDimensions = paramMob.getDimensions(Pose.LONG_JUMPING);
/* 61 */       for (byte b = 0; b < i - 1; b++) {
/* 62 */         d += d3 / i;
/* 63 */         double d16 = d8 / d9 * d - Math.pow(d, 2.0D) * d5 / 2.0D * d12 * Math.pow(d9, 2.0D);
/* 64 */         double d17 = d * d11;
/* 65 */         double d18 = d * d10;
/*    */         
/* 67 */         Vec3 vec35 = new Vec3(vec31.x + d17, vec31.y + d16, vec31.z + d18);
/* 68 */         if (vec3 != null && !isClearTransition(paramMob, entityDimensions, vec3, vec35)) {
/* 69 */           return Optional.empty();
/*    */         }
/*    */         
/* 72 */         vec3 = vec35;
/*    */       } 
/*    */     } 
/*    */     
/* 76 */     return Optional.of((new Vec3(d14 * d11, d15, d14 * d10)).scale(0.949999988079071D));
/*    */   }
/*    */   
/*    */   private static boolean isClearTransition(Mob paramMob, EntityDimensions paramEntityDimensions, Vec3 paramVec31, Vec3 paramVec32) {
/* 80 */     Vec3 vec31 = paramVec32.subtract(paramVec31);
/*    */     
/* 82 */     double d = Math.min(paramEntityDimensions.width(), paramEntityDimensions.height());
/* 83 */     int i = Mth.ceil(vec31.length() / d);
/*    */     
/* 85 */     Vec3 vec32 = vec31.normalize();
/* 86 */     Vec3 vec33 = paramVec31;
/* 87 */     for (byte b = 0; b < i; b++) {
/* 88 */       vec33 = (b == i - 1) ? paramVec32 : vec33.add(vec32.scale(d * 0.8999999761581421D));
/* 89 */       if (!paramMob.level().noCollision((Entity)paramMob, paramEntityDimensions.makeBoundingBox(vec33))) {
/* 90 */         return false;
/*    */       }
/*    */     } 
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\LongJumpUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */