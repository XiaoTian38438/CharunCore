/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerBabiesSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 21 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 26 */     paramLivingEntity.getBrain().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, getNearestVillagerBabies(paramLivingEntity));
/*    */   }
/*    */   
/*    */   private List<LivingEntity> getNearestVillagerBabies(LivingEntity paramLivingEntity) {
/* 30 */     return (List<LivingEntity>)ImmutableList.copyOf(getVisibleEntities(paramLivingEntity).findAll(this::isVillagerBaby));
/*    */   }
/*    */   
/*    */   private boolean isVillagerBaby(LivingEntity paramLivingEntity) {
/* 34 */     return (paramLivingEntity.getType() == EntityType.VILLAGER && paramLivingEntity.isBaby());
/*    */   }
/*    */   
/*    */   private NearestVisibleLivingEntities getVisibleEntities(LivingEntity paramLivingEntity) {
/* 38 */     return paramLivingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
/* 39 */       .orElse(NearestVisibleLivingEntities.empty());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\VillagerBabiesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */