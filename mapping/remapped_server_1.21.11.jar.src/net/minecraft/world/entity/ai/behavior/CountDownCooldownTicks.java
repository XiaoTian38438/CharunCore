/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class CountDownCooldownTicks
/*    */   extends Behavior<LivingEntity> {
/*    */   private final MemoryModuleType<Integer> cooldownTicks;
/*    */   
/*    */   public CountDownCooldownTicks(MemoryModuleType<Integer> paramMemoryModuleType) {
/* 16 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(paramMemoryModuleType, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */     
/* 19 */     this.cooldownTicks = paramMemoryModuleType;
/*    */   }
/*    */   
/*    */   private Optional<Integer> getCooldownTickMemory(LivingEntity paramLivingEntity) {
/* 23 */     return paramLivingEntity.getBrain().getMemory(this.cooldownTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long paramLong) {
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 33 */     Optional<Integer> optional = getCooldownTickMemory(paramLivingEntity);
/* 34 */     return (optional.isPresent() && ((Integer)optional.get()).intValue() > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 39 */     Optional<Integer> optional = getCooldownTickMemory(paramLivingEntity);
/* 40 */     paramLivingEntity.getBrain().setMemory(this.cooldownTicks, Integer.valueOf(((Integer)optional.get()).intValue() - 1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, long paramLong) {
/* 45 */     paramLivingEntity.getBrain().eraseMemory(this.cooldownTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\CountDownCooldownTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */