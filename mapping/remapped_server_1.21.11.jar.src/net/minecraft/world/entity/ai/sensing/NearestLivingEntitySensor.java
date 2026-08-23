/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestLivingEntitySensor<T extends LivingEntity>
/*    */   extends Sensor<T>
/*    */ {
/*    */   protected void doTick(ServerLevel paramServerLevel, T paramT) {
/* 23 */     double d = paramT.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 24 */     AABB aABB = paramT.getBoundingBox().inflate(d, d, d);
/* 25 */     List list = paramServerLevel.getEntitiesOfClass(LivingEntity.class, aABB, paramLivingEntity2 -> (paramLivingEntity2 != paramLivingEntity1 && paramLivingEntity2.isAlive()));
/* 26 */     Objects.requireNonNull(paramT); list.sort(Comparator.comparingDouble(paramT::distanceToSqr));
/*    */     
/* 28 */     Brain brain = paramT.getBrain();
/* 29 */     brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
/* 30 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(paramServerLevel, (LivingEntity)paramT, list));
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 35 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\NearestLivingEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */