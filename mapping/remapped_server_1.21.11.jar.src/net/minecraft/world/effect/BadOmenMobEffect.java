/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ class BadOmenMobEffect
/*    */   extends MobEffect {
/*    */   protected BadOmenMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/* 12 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 22 */     if (paramLivingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramLivingEntity; if (!serverPlayer.isSpectator() && 
/* 23 */         paramServerLevel.getDifficulty() != Difficulty.PEACEFUL && paramServerLevel.isVillage(serverPlayer.blockPosition())) {
/* 24 */         Raid raid = paramServerLevel.getRaidAt(serverPlayer.blockPosition());
/*    */         
/* 26 */         if (raid == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
/* 27 */           serverPlayer.addEffect(new MobEffectInstance(MobEffects.RAID_OMEN, 600, paramInt));
/* 28 */           serverPlayer.setRaidOmenPosition(serverPlayer.blockPosition());
/* 29 */           return false;
/*    */         } 
/*    */       }  }
/*    */     
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\BadOmenMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */