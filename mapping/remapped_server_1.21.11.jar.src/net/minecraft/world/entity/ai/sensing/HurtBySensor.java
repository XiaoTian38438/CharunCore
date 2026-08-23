/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HurtBySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 21 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 26 */     Brain brain = paramLivingEntity.getBrain();
/* 27 */     DamageSource damageSource = paramLivingEntity.getLastDamageSource();
/* 28 */     if (damageSource != null) {
/* 29 */       brain.setMemory(MemoryModuleType.HURT_BY, paramLivingEntity.getLastDamageSource());
/* 30 */       Entity entity = damageSource.getEntity();
/* 31 */       if (entity instanceof LivingEntity) {
/* 32 */         brain.setMemory(MemoryModuleType.HURT_BY_ENTITY, entity);
/*    */       }
/*    */     } else {
/* 35 */       brain.eraseMemory(MemoryModuleType.HURT_BY);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(paramLivingEntity -> {
/*    */           if (!paramLivingEntity.isAlive() || paramLivingEntity.level() != paramServerLevel)
/*    */             paramBrain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\HurtBySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */