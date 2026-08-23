/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class IsInWaterSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 14 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.IS_IN_WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 19 */     if (paramLivingEntity.isInWater()) {
/* 20 */       paramLivingEntity.getBrain().setMemory(MemoryModuleType.IS_IN_WATER, Unit.INSTANCE);
/*    */     } else {
/* 22 */       paramLivingEntity.getBrain().eraseMemory(MemoryModuleType.IS_IN_WATER);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\IsInWaterSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */