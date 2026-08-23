/*     */ package net.minecraft.world.entity.ai.sensing;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinSpecificSensor
/*     */   extends Sensor<LivingEntity>
/*     */ {
/*     */   public Set<MemoryModuleType<?>> requires() {
/*  36 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/*  57 */     Brain brain = paramLivingEntity.getBrain();
/*     */     
/*  59 */     brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(paramServerLevel, paramLivingEntity));
/*     */     
/*  61 */     Optional<?> optional1 = Optional.empty();
/*  62 */     Optional<?> optional2 = Optional.empty();
/*  63 */     Optional<?> optional3 = Optional.empty();
/*  64 */     Optional<?> optional4 = Optional.empty();
/*  65 */     Optional<?> optional5 = Optional.empty();
/*  66 */     Optional<?> optional6 = Optional.empty();
/*  67 */     Optional<?> optional7 = Optional.empty();
/*  68 */     byte b = 0;
/*     */     
/*  70 */     ArrayList<PiglinBrute> arrayList = Lists.newArrayList();
/*  71 */     ArrayList<AbstractPiglin> arrayList1 = Lists.newArrayList();
/*     */ 
/*     */     
/*  74 */     NearestVisibleLivingEntities nearestVisibleLivingEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/*  75 */     for (LivingEntity livingEntity : nearestVisibleLivingEntities.findAll(paramLivingEntity -> true)) {
/*  76 */       if (livingEntity instanceof Hoglin) { Hoglin hoglin = (Hoglin)livingEntity;
/*  77 */         if (hoglin.isBaby() && optional3.isEmpty()) {
/*  78 */           optional3 = Optional.of(hoglin); continue;
/*  79 */         }  if (hoglin.isAdult()) {
/*  80 */           b++;
/*  81 */           if (optional2.isEmpty() && hoglin.canBeHunted())
/*  82 */             optional2 = Optional.of(hoglin); 
/*     */         }  continue; }
/*     */       
/*  85 */       if (livingEntity instanceof PiglinBrute) { PiglinBrute piglinBrute = (PiglinBrute)livingEntity;
/*  86 */         arrayList.add(piglinBrute); continue; }
/*  87 */        if (livingEntity instanceof Piglin) { Piglin piglin = (Piglin)livingEntity;
/*  88 */         if (piglin.isBaby() && optional4.isEmpty()) {
/*  89 */           optional4 = Optional.of(piglin); continue;
/*  90 */         }  if (piglin.isAdult())
/*  91 */           arrayList.add(piglin);  continue; }
/*     */       
/*  93 */       if (livingEntity instanceof Player) { Player player = (Player)livingEntity;
/*  94 */         if (optional6.isEmpty() && !PiglinAi.isWearingSafeArmor((LivingEntity)player) && paramLivingEntity.canAttack(livingEntity)) {
/*  95 */           optional6 = Optional.of(player);
/*     */         }
/*  97 */         if (optional7.isEmpty() && !player.isSpectator() && PiglinAi.isPlayerHoldingLovedItem((LivingEntity)player))
/*  98 */           optional7 = Optional.of(player);  continue; }
/*     */       
/* 100 */       if (optional1.isEmpty() && (livingEntity instanceof net.minecraft.world.entity.monster.skeleton.WitherSkeleton || livingEntity instanceof net.minecraft.world.entity.boss.wither.WitherBoss)) {
/* 101 */         optional1 = Optional.of(livingEntity); continue;
/* 102 */       }  if (optional5.isEmpty() && PiglinAi.isZombified(livingEntity.getType())) {
/* 103 */         optional5 = Optional.of(livingEntity);
/*     */       }
/*     */     } 
/*     */     
/* 107 */     List list = (List)brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());
/* 108 */     for (LivingEntity livingEntity : list) {
/* 109 */       if (livingEntity instanceof AbstractPiglin) { AbstractPiglin abstractPiglin = (AbstractPiglin)livingEntity; if (abstractPiglin.isAdult()) {
/* 110 */           arrayList1.add(abstractPiglin);
/*     */         } }
/*     */     
/*     */     } 
/* 114 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional1);
/* 115 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, optional2);
/* 116 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, optional3);
/* 117 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, optional5);
/* 118 */     brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, optional6);
/* 119 */     brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, optional7);
/* 120 */     brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, arrayList1);
/* 121 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, arrayList);
/* 122 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(arrayList.size()));
/* 123 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(b));
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> findNearestRepellent(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 127 */     return BlockPos.findClosestMatch(paramLivingEntity
/* 128 */         .blockPosition(), 8, 4, paramBlockPos -> isValidRepellent(paramServerLevel, paramBlockPos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidRepellent(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 136 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos);
/* 137 */     boolean bool = blockState.is(BlockTags.PIGLIN_REPELLENTS);
/* 138 */     if (bool && blockState.is(Blocks.SOUL_CAMPFIRE)) {
/* 139 */       return CampfireBlock.isLitCampfire(blockState);
/*     */     }
/* 141 */     return bool;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\sensing\PiglinSpecificSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */