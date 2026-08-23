/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class AxolotlAttackablesSensor
/*    */   extends NearestVisibleLivingEntitySensor {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 8.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 14 */     return (isClose(paramLivingEntity1, paramLivingEntity2) && paramLivingEntity2.isInWater() && (
/* 15 */       isHostileTarget(paramLivingEntity2) || isHuntTarget(paramLivingEntity1, paramLivingEntity2)) && 
/* 16 */       Sensor.isEntityAttackable(paramServerLevel, paramLivingEntity1, paramLivingEntity2));
/*    */   }
/*    */   
/*    */   private boolean isHuntTarget(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 20 */     return (!paramLivingEntity1.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && paramLivingEntity2.getType().is(EntityTypeTags.AXOLOTL_HUNT_TARGETS));
/*    */   }
/*    */   
/*    */   private boolean isHostileTarget(LivingEntity paramLivingEntity) {
/* 24 */     return paramLivingEntity.getType().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 28 */     return (paramLivingEntity2.distanceToSqr((Entity)paramLivingEntity1) <= 64.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemory() {
/* 33 */     return MemoryModuleType.NEAREST_ATTACKABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\AxolotlAttackablesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */