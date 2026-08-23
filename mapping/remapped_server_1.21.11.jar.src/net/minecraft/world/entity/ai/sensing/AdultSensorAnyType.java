/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class AdultSensorAnyType
/*    */   extends AdultSensor
/*    */ {
/*    */   protected void setNearestVisibleAdult(LivingEntity paramLivingEntity, NearestVisibleLivingEntities paramNearestVisibleLivingEntities) {
/* 13 */     Optional optional = paramNearestVisibleLivingEntities.findClosest(paramLivingEntity -> (paramLivingEntity.getType().is(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS) && !paramLivingEntity.isBaby()));
/* 14 */     paramLivingEntity.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\AdultSensorAnyType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */