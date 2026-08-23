/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*    */ 
/*    */ public class PiglinBruteSpecificSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 23 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 35 */     Brain brain = paramLivingEntity.getBrain();
/*    */     
/* 37 */     ArrayList<AbstractPiglin> arrayList = Lists.newArrayList();
/*    */ 
/*    */     
/* 40 */     NearestVisibleLivingEntities nearestVisibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/* 41 */     Objects.requireNonNull(Mob.class); Optional optional = nearestVisibleLivingEntities.findClosest(paramLivingEntity -> (paramLivingEntity instanceof net.minecraft.world.entity.monster.skeleton.WitherSkeleton || paramLivingEntity instanceof net.minecraft.world.entity.boss.wither.WitherBoss)).map(Mob.class::cast);
/*    */     
/* 43 */     List list = (List)brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());
/* 44 */     for (LivingEntity livingEntity : list) {
/* 45 */       if (livingEntity instanceof AbstractPiglin && ((AbstractPiglin)livingEntity).isAdult()) {
/* 46 */         arrayList.add((AbstractPiglin)livingEntity);
/*    */       }
/*    */     } 
/*    */     
/* 50 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
/* 51 */     brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, arrayList);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\PiglinBruteSpecificSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */