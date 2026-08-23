/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class PoisonMobEffect extends MobEffect {
/*    */   public static final int DAMAGE_INTERVAL = 25;
/*    */   
/*    */   protected PoisonMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/* 10 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 15 */     if (paramLivingEntity.getHealth() > 1.0F) {
/* 16 */       paramLivingEntity.hurtServer(paramServerLevel, paramLivingEntity.damageSources().magic(), 1.0F);
/*    */     }
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 23 */     int i = 25 >> paramInt2;
/* 24 */     if (i > 0) {
/* 25 */       return (paramInt1 % i == 0);
/*    */     }
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\PoisonMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */