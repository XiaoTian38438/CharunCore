/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class HealOrHarmMobEffect
/*    */   extends InstantenousMobEffect {
/*    */   private final boolean isHarm;
/*    */   
/*    */   public HealOrHarmMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt, boolean paramBoolean) {
/* 12 */     super(paramMobEffectCategory, paramInt);
/* 13 */     this.isHarm = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 18 */     if (this.isHarm == paramLivingEntity.isInvertedHealAndHarm()) {
/* 19 */       paramLivingEntity.heal(Math.max(4 << paramInt, 0));
/*    */     } else {
/* 21 */       paramLivingEntity.hurtServer(paramServerLevel, paramLivingEntity.damageSources().magic(), (6 << paramInt));
/*    */     } 
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyInstantenousEffect(ServerLevel paramServerLevel, Entity paramEntity1, Entity paramEntity2, LivingEntity paramLivingEntity, int paramInt, double paramDouble) {
/* 28 */     if (this.isHarm == paramLivingEntity.isInvertedHealAndHarm()) {
/* 29 */       int i = (int)(paramDouble * (4 << paramInt) + 0.5D);
/* 30 */       paramLivingEntity.heal(i);
/*    */     } else {
/* 32 */       int i = (int)(paramDouble * (6 << paramInt) + 0.5D);
/* 33 */       if (paramEntity1 == null) {
/* 34 */         paramLivingEntity.hurtServer(paramServerLevel, paramLivingEntity.damageSources().magic(), i);
/*    */       } else {
/* 36 */         paramLivingEntity.hurtServer(paramServerLevel, paramLivingEntity.damageSources().indirectMagic(paramEntity1, paramEntity2), i);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\HealOrHarmMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */