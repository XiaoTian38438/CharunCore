/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.StringUtil;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class MobEffectUtil
/*    */ {
/*    */   public static Component formatDuration(MobEffectInstance paramMobEffectInstance, float paramFloat1, float paramFloat2) {
/* 18 */     if (paramMobEffectInstance.isInfiniteDuration()) {
/* 19 */       return (Component)Component.translatable("effect.duration.infinite");
/*    */     }
/* 21 */     int i = Mth.floor(paramMobEffectInstance.getDuration() * paramFloat1);
/* 22 */     return (Component)Component.literal(StringUtil.formatTickDuration(i, paramFloat2));
/*    */   }
/*    */   
/*    */   public static boolean hasDigSpeed(LivingEntity paramLivingEntity) {
/* 26 */     return (paramLivingEntity.hasEffect(MobEffects.HASTE) || paramLivingEntity.hasEffect(MobEffects.CONDUIT_POWER));
/*    */   }
/*    */   
/*    */   public static int getDigSpeedAmplification(LivingEntity paramLivingEntity) {
/* 30 */     int i = 0, j = 0;
/* 31 */     if (paramLivingEntity.hasEffect(MobEffects.HASTE)) {
/* 32 */       i = paramLivingEntity.getEffect(MobEffects.HASTE).getAmplifier();
/*    */     }
/* 34 */     if (paramLivingEntity.hasEffect(MobEffects.CONDUIT_POWER)) {
/* 35 */       j = paramLivingEntity.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
/*    */     }
/*    */     
/* 38 */     return Math.max(i, j);
/*    */   }
/*    */   
/*    */   public static boolean hasWaterBreathing(LivingEntity paramLivingEntity) {
/* 42 */     return (paramLivingEntity.hasEffect(MobEffects.WATER_BREATHING) || paramLivingEntity.hasEffect(MobEffects.CONDUIT_POWER) || paramLivingEntity.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS));
/*    */   }
/*    */   
/*    */   public static boolean shouldEffectsRefillAirsupply(LivingEntity paramLivingEntity) {
/* 46 */     return (!paramLivingEntity.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS) || paramLivingEntity.hasEffect(MobEffects.WATER_BREATHING) || paramLivingEntity.hasEffect(MobEffects.CONDUIT_POWER));
/*    */   }
/*    */   
/*    */   public static List<ServerPlayer> addEffectToPlayersAround(ServerLevel paramServerLevel, Entity paramEntity, Vec3 paramVec3, double paramDouble, MobEffectInstance paramMobEffectInstance, int paramInt) {
/* 50 */     Holder<MobEffect> holder = paramMobEffectInstance.getEffect();
/* 51 */     List<ServerPlayer> list = paramServerLevel.getPlayers(paramServerPlayer -> 
/* 52 */         (paramServerPlayer.gameMode.isSurvival() && (paramEntity == null || !paramEntity.isAlliedTo((Entity)paramServerPlayer)) && paramVec3.closerThan((Position)paramServerPlayer.position(), paramDouble) && (!paramServerPlayer.hasEffect(paramHolder) || paramServerPlayer.getEffect(paramHolder).getAmplifier() < paramMobEffectInstance.getAmplifier() || paramServerPlayer.getEffect(paramHolder).endsWithin(paramInt - 1))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     list.forEach(paramServerPlayer -> paramServerPlayer.addEffect(new MobEffectInstance(paramMobEffectInstance), paramEntity));
/*    */     
/* 64 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\MobEffectUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */