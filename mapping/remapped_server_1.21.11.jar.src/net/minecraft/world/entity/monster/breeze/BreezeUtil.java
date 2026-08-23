/*    */ package net.minecraft.world.entity.monster.breeze;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class BreezeUtil
/*    */ {
/*    */   private static final double MAX_LINE_OF_SIGHT_TEST_RANGE = 50.0D;
/*    */   
/*    */   public static Vec3 randomPointBehindTarget(LivingEntity paramLivingEntity, RandomSource paramRandomSource) {
/* 18 */     byte b = 90;
/* 19 */     float f1 = paramLivingEntity.yHeadRot + 180.0F + (float)paramRandomSource.nextGaussian() * 90.0F / 2.0F;
/* 20 */     float f2 = Mth.lerp(paramRandomSource.nextFloat(), 4.0F, 8.0F);
/*    */     
/* 22 */     Vec3 vec3 = Vec3.directionFromRotation(0.0F, f1).scale(f2);
/* 23 */     return paramLivingEntity.position().add(vec3);
/*    */   }
/*    */   
/*    */   public static boolean hasLineOfSight(Breeze paramBreeze, Vec3 paramVec3) {
/* 27 */     Vec3 vec3 = new Vec3(paramBreeze.getX(), paramBreeze.getY(), paramBreeze.getZ());
/* 28 */     if (paramVec3.distanceTo(vec3) > getMaxLineOfSightTestRange(paramBreeze)) {
/* 29 */       return false;
/*    */     }
/* 31 */     return (paramBreeze.level().clip(new ClipContext(vec3, paramVec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)paramBreeze)).getType() == HitResult.Type.MISS);
/*    */   }
/*    */   
/*    */   private static double getMaxLineOfSightTestRange(Breeze paramBreeze) {
/* 35 */     return Math.max(50.0D, paramBreeze.getAttributeValue(Attributes.FOLLOW_RANGE));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\breeze\BreezeUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */