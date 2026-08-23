/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class AbsorptionMobEffect extends MobEffect {
/*    */   protected AbsorptionMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/*  8 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 13 */     return (paramLivingEntity.getAbsorptionAmount() > 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEffectStarted(LivingEntity paramLivingEntity, int paramInt) {
/* 23 */     super.onEffectStarted(paramLivingEntity, paramInt);
/* 24 */     paramLivingEntity.setAbsorptionAmount(Math.max(paramLivingEntity.getAbsorptionAmount(), (4 * (1 + paramInt))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\AbsorptionMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */