/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GolemSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   private static final int GOLEM_SCAN_RATE = 200;
/*    */   private static final int MEMORY_TIME_TO_LIVE = 599;
/*    */   
/*    */   public GolemSensor() {
/* 22 */     this(200);
/*    */   }
/*    */   
/*    */   public GolemSensor(int paramInt) {
/* 26 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 31 */     checkForNearbyGolem(paramLivingEntity);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 36 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   public static void checkForNearbyGolem(LivingEntity paramLivingEntity) {
/* 40 */     Optional<List> optional = paramLivingEntity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/* 41 */     if (optional.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     boolean bool = ((List)optional.get()).stream().anyMatch(paramLivingEntity -> paramLivingEntity.getType().equals(EntityType.IRON_GOLEM));
/*    */     
/* 47 */     if (bool) {
/* 48 */       golemDetected(paramLivingEntity);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void golemDetected(LivingEntity paramLivingEntity) {
/* 53 */     paramLivingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, Boolean.valueOf(true), 599L);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\GolemSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */