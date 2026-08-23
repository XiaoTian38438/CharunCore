/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class MobSensor<T extends LivingEntity>
/*    */   extends Sensor<T> {
/*    */   private final BiPredicate<T, LivingEntity> mobTest;
/*    */   private final Predicate<T> readyTest;
/*    */   private final MemoryModuleType<Boolean> toSet;
/*    */   private final int memoryTimeToLive;
/*    */   
/*    */   public MobSensor(int paramInt1, BiPredicate<T, LivingEntity> paramBiPredicate, Predicate<T> paramPredicate, MemoryModuleType<Boolean> paramMemoryModuleType, int paramInt2) {
/* 20 */     super(paramInt1);
/* 21 */     this.mobTest = paramBiPredicate;
/* 22 */     this.readyTest = paramPredicate;
/* 23 */     this.toSet = paramMemoryModuleType;
/* 24 */     this.memoryTimeToLive = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, T paramT) {
/* 29 */     if (!this.readyTest.test(paramT)) {
/* 30 */       clearMemory(paramT);
/*    */     } else {
/* 32 */       checkForMobsNearby(paramT);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 38 */     return Set.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   public void checkForMobsNearby(T paramT) {
/* 42 */     Optional<List> optional = paramT.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/* 43 */     if (optional.isEmpty()) {
/*    */       return;
/*    */     }
/* 46 */     boolean bool = ((List)optional.get()).stream().anyMatch(paramLivingEntity2 -> this.mobTest.test((T)paramLivingEntity1, paramLivingEntity2));
/*    */     
/* 48 */     if (bool) {
/* 49 */       mobDetected(paramT);
/*    */     }
/*    */   }
/*    */   
/*    */   public void mobDetected(T paramT) {
/* 54 */     paramT.getBrain().setMemoryWithExpiry(this.toSet, Boolean.valueOf(true), this.memoryTimeToLive);
/*    */   }
/*    */   
/*    */   public void clearMemory(T paramT) {
/* 58 */     paramT.getBrain().eraseMemory(this.toSet);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\MobSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */