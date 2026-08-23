/*    */ package net.minecraft.world.entity.monster.hoglin;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface HoglinBase
/*    */ {
/*    */   public static final int ATTACK_ANIMATION_DURATION = 10;
/*    */   public static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
/*    */   
/*    */   int getAttackAnimationRemainingTicks();
/*    */   
/*    */   static boolean hurtAndThrowTarget(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 19 */     float f1, f2 = (float)paramLivingEntity1.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 20 */     if (!paramLivingEntity1.isBaby() && (int)f2 > 0) {
/* 21 */       f1 = f2 / 2.0F + paramServerLevel.random.nextInt((int)f2);
/*    */     } else {
/* 23 */       f1 = f2;
/*    */     } 
/*    */     
/* 26 */     DamageSource damageSource = paramLivingEntity1.damageSources().mobAttack(paramLivingEntity1);
/* 27 */     boolean bool = paramLivingEntity2.hurtServer(paramServerLevel, damageSource, f1);
/* 28 */     if (bool) {
/* 29 */       EnchantmentHelper.doPostAttackEffects(paramServerLevel, (Entity)paramLivingEntity2, damageSource);
/* 30 */       if (!paramLivingEntity1.isBaby()) {
/* 31 */         throwTarget(paramLivingEntity1, paramLivingEntity2);
/*    */       }
/*    */     } 
/* 34 */     return bool;
/*    */   }
/*    */   
/*    */   static void throwTarget(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 38 */     double d1 = paramLivingEntity1.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/* 39 */     double d2 = paramLivingEntity2.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
/* 40 */     double d3 = d1 - d2;
/* 41 */     if (d3 <= 0.0D) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     double d4 = paramLivingEntity2.getX() - paramLivingEntity1.getX();
/* 46 */     double d5 = paramLivingEntity2.getZ() - paramLivingEntity1.getZ();
/* 47 */     float f = ((paramLivingEntity1.level()).random.nextInt(21) - 10);
/* 48 */     double d6 = d3 * ((paramLivingEntity1.level()).random.nextFloat() * 0.5F + 0.2F);
/* 49 */     Vec3 vec3 = (new Vec3(d4, 0.0D, d5)).normalize().scale(d6).yRot(f);
/*    */     
/* 51 */     double d7 = d3 * (paramLivingEntity1.level()).random.nextFloat() * 0.5D;
/* 52 */     paramLivingEntity2.push(vec3.x, d7, vec3.z);
/* 53 */     paramLivingEntity2.hurtMarked = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\hoglin\HoglinBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */