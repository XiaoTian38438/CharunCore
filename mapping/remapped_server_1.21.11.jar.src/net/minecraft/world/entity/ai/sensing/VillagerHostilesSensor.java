/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ public class VillagerHostilesSensor
/*    */   extends NearestVisibleLivingEntitySensor
/*    */ {
/* 14 */   private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
/* 15 */     .put(EntityType.DROWNED, Float.valueOf(8.0F))
/* 16 */     .put(EntityType.EVOKER, Float.valueOf(12.0F))
/* 17 */     .put(EntityType.HUSK, Float.valueOf(8.0F))
/* 18 */     .put(EntityType.ILLUSIONER, Float.valueOf(12.0F))
/* 19 */     .put(EntityType.PILLAGER, Float.valueOf(15.0F))
/* 20 */     .put(EntityType.RAVAGER, Float.valueOf(12.0F))
/* 21 */     .put(EntityType.VEX, Float.valueOf(8.0F))
/* 22 */     .put(EntityType.VINDICATOR, Float.valueOf(10.0F))
/* 23 */     .put(EntityType.ZOGLIN, Float.valueOf(10.0F))
/* 24 */     .put(EntityType.ZOMBIE, Float.valueOf(8.0F))
/* 25 */     .put(EntityType.ZOMBIE_VILLAGER, Float.valueOf(8.0F))
/* 26 */     .build();
/*    */ 
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 30 */     return (isHostile(paramLivingEntity2) && isClose(paramLivingEntity1, paramLivingEntity2));
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 34 */     float f = ((Float)ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(paramLivingEntity2.getType())).floatValue();
/* 35 */     return (paramLivingEntity2.distanceToSqr((Entity)paramLivingEntity1) <= (f * f));
/*    */   }
/*    */ 
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemory() {
/* 40 */     return MemoryModuleType.NEAREST_HOSTILE;
/*    */   }
/*    */   
/*    */   private boolean isHostile(LivingEntity paramLivingEntity) {
/* 44 */     return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(paramLivingEntity.getType());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\VillagerHostilesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */