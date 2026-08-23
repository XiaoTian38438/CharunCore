/*    */ package net.minecraft.world.effect;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class WindChargedMobEffect extends MobEffect {
/*    */   protected WindChargedMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/* 14 */     super(paramMobEffectCategory, paramInt, (ParticleOptions)ParticleTypes.SMALL_GUST);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt, Entity.RemovalReason paramRemovalReason) {
/* 19 */     if (paramRemovalReason == Entity.RemovalReason.KILLED) {
/* 20 */       double d1 = paramLivingEntity.getX();
/* 21 */       double d2 = paramLivingEntity.getY() + (paramLivingEntity.getBbHeight() / 2.0F);
/* 22 */       double d3 = paramLivingEntity.getZ();
/* 23 */       float f = 3.0F + paramLivingEntity.getRandom().nextFloat() * 2.0F;
/* 24 */       paramServerLevel.explode((Entity)paramLivingEntity, null, AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR, d1, d2, d3, f, false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), (Holder)SoundEvents.BREEZE_WIND_CHARGE_BURST);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\WindChargedMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */