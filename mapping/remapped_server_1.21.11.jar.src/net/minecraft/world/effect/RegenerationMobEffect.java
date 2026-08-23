/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RegenerationMobEffect extends MobEffect {
/*    */   protected RegenerationMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/*  8 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 13 */     if (paramLivingEntity.getHealth() < paramLivingEntity.getMaxHealth()) {
/* 14 */       paramLivingEntity.heal(1.0F);
/*    */     }
/* 16 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 21 */     int i = 50 >> paramInt2;
/* 22 */     if (i > 0) {
/* 23 */       return (paramInt1 % i == 0);
/*    */     }
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\RegenerationMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */