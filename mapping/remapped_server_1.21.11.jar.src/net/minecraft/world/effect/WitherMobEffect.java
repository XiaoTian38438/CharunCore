/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class WitherMobEffect
/*    */   extends MobEffect {
/*    */   public static final int DAMAGE_INTERVAL = 40;
/*    */   
/*    */   protected WitherMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/* 11 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 16 */     paramLivingEntity.hurtServer(paramServerLevel, paramLivingEntity.damageSources().wither(), 1.0F);
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 22 */     int i = 40 >> paramInt2;
/* 23 */     if (i > 0) {
/* 24 */       return (paramInt1 % i == 0);
/*    */     }
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\WitherMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */