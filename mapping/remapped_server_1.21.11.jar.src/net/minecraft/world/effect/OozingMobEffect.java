/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.ArrayList;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Slime;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.entity.EntityTypeTest;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ class OozingMobEffect extends MobEffect {
/*    */   private static final int RADIUS_TO_CHECK_SLIMES = 2;
/*    */   public static final int SLIME_SIZE = 2;
/*    */   private final ToIntFunction<RandomSource> spawnedCount;
/*    */   
/*    */   protected OozingMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt, ToIntFunction<RandomSource> paramToIntFunction) {
/* 26 */     super(paramMobEffectCategory, paramInt, (ParticleOptions)ParticleTypes.ITEM_SLIME);
/* 27 */     this.spawnedCount = paramToIntFunction;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected static int numberOfSlimesToSpawn(int paramInt1, NearbySlimes paramNearbySlimes, int paramInt2) {
/* 32 */     if (paramInt1 < 1) {
/* 33 */       return paramInt2;
/*    */     }
/*    */ 
/*    */     
/* 37 */     return Mth.clamp(0, paramInt1 - paramNearbySlimes.count(paramInt1), paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt, Entity.RemovalReason paramRemovalReason) {
/* 42 */     if (paramRemovalReason != Entity.RemovalReason.KILLED) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     int i = this.spawnedCount.applyAsInt(paramLivingEntity.getRandom());
/* 47 */     int j = ((Integer)paramServerLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING)).intValue();
/* 48 */     int k = numberOfSlimesToSpawn(j, NearbySlimes.closeTo(paramLivingEntity), i);
/*    */     
/* 50 */     for (byte b = 0; b < k; b++)
/* 51 */       spawnSlimeOffspring(paramLivingEntity.level(), paramLivingEntity.getX(), paramLivingEntity.getY() + 0.5D, paramLivingEntity.getZ()); 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   protected static interface NearbySlimes
/*    */   {
/*    */     int count(int param1Int);
/*    */     
/*    */     static NearbySlimes closeTo(LivingEntity param1LivingEntity) {
/* 60 */       return param1Int -> {
/*    */           ArrayList arrayList = new ArrayList();
/*    */           param1LivingEntity.level().getEntities((EntityTypeTest)EntityType.SLIME, param1LivingEntity.getBoundingBox().inflate(2.0D), (), arrayList, param1Int);
/*    */           return arrayList.size();
/*    */         };
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnSlimeOffspring(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 69 */     Slime slime = (Slime)EntityType.SLIME.create(paramLevel, EntitySpawnReason.TRIGGERED);
/*    */     
/* 71 */     if (slime == null) {
/*    */       return;
/*    */     }
/*    */     
/* 75 */     slime.setSize(2, true);
/* 76 */     slime.snapTo(paramDouble1, paramDouble2, paramDouble3, paramLevel.getRandom().nextFloat() * 360.0F, 0.0F);
/* 77 */     paramLevel.addFreshEntity((Entity)slime);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\OozingMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */