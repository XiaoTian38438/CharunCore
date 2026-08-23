/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.animal.frog.Frog;
/*    */ 
/*    */ public class FrogAttackablesSensor
/*    */   extends NearestVisibleLivingEntitySensor
/*    */ {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 10.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 17 */     if (!paramLivingEntity1.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && 
/* 18 */       Sensor.isEntityAttackable(paramServerLevel, paramLivingEntity1, paramLivingEntity2) && 
/* 19 */       Frog.canEat(paramLivingEntity2) && 
/* 20 */       !isUnreachableAttackTarget(paramLivingEntity1, paramLivingEntity2))
/*    */     {
/* 22 */       return paramLivingEntity2.closerThan((Entity)paramLivingEntity1, 10.0D);
/*    */     }
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   private boolean isUnreachableAttackTarget(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 28 */     List list = paramLivingEntity1.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS).orElseGet(java.util.ArrayList::new);
/* 29 */     return list.contains(paramLivingEntity2.getUUID());
/*    */   }
/*    */ 
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemory() {
/* 34 */     return MemoryModuleType.NEAREST_ATTACKABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\FrogAttackablesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */