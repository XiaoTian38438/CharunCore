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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NearestVisibleLivingEntitySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 22 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(
/* 23 */         getMemory());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 29 */     paramLivingEntity.getBrain().setMemory(getMemory(), getNearestEntity(paramServerLevel, paramLivingEntity));
/*    */   }
/*    */   
/*    */   private Optional<LivingEntity> getNearestEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 33 */     return getVisibleEntities(paramLivingEntity).flatMap(paramNearestVisibleLivingEntities -> paramNearestVisibleLivingEntities.findClosest(()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity paramLivingEntity) {
/* 38 */     return paramLivingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   protected abstract boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2);
/*    */   
/*    */   protected abstract MemoryModuleType<LivingEntity> getMemory();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\NearestVisibleLivingEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */