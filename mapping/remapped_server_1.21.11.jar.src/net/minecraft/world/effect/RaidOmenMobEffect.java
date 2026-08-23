/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RaidOmenMobEffect extends MobEffect {
/*    */   protected RaidOmenMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt, ParticleOptions paramParticleOptions) {
/* 11 */     super(paramMobEffectCategory, paramInt, paramParticleOptions);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 16 */     return (paramInt1 == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 21 */     if (paramLivingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramLivingEntity; if (!paramLivingEntity.isSpectator()) {
/* 22 */         BlockPos blockPos = serverPlayer.getRaidOmenPosition();
/*    */         
/* 24 */         if (blockPos != null) {
/* 25 */           paramServerLevel.getRaids().createOrExtendRaid(serverPlayer, blockPos);
/* 26 */           serverPlayer.clearRaidOmenPosition();
/* 27 */           return false;
/*    */         } 
/*    */       }  }
/* 30 */      return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\RaidOmenMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */