/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdultSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 19 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 26 */     paramLivingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(paramNearestVisibleLivingEntities -> setNearestVisibleAdult(paramLivingEntity, paramNearestVisibleLivingEntities));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setNearestVisibleAdult(LivingEntity paramLivingEntity, NearestVisibleLivingEntities paramNearestVisibleLivingEntities) {
/* 32 */     Optional optional = paramNearestVisibleLivingEntities.findClosest(paramLivingEntity2 -> (paramLivingEntity2.getType() == paramLivingEntity1.getType() && !paramLivingEntity2.isBaby()));
/* 33 */     paramLivingEntity.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\AdultSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */