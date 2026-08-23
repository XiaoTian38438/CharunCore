/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PlayTagWithOtherKids {
/*     */   private static final int MAX_FLEE_XZ_DIST = 20;
/*     */   private static final int MAX_FLEE_Y_DIST = 8;
/*     */   private static final float FLEE_SPEED_MODIFIER = 0.6F;
/*     */   private static final float CHASE_SPEED_MODIFIER = 0.6F;
/*     */   private static final int MAX_CHASERS_PER_TARGET = 5;
/*     */   private static final int AVERAGE_WAIT_TIME_BETWEEN_RUNS = 10;
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create() {
/*  34 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.VISIBLE_VILLAGER_BABIES), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.registered(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)paramInstance, ()));
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static void chaseKid(MemoryAccessor<?, LivingEntity> paramMemoryAccessor, MemoryAccessor<?, PositionTracker> paramMemoryAccessor1, MemoryAccessor<?, WalkTarget> paramMemoryAccessor2, LivingEntity paramLivingEntity) {
/*  76 */     paramMemoryAccessor.set(paramLivingEntity);
/*  77 */     paramMemoryAccessor1.set(new EntityTracker((Entity)paramLivingEntity, true));
/*  78 */     paramMemoryAccessor2.set(new WalkTarget(new EntityTracker((Entity)paramLivingEntity, false), 0.6F, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<LivingEntity> findSomeoneBeingChased(List<LivingEntity> paramList) {
/*  83 */     Map<LivingEntity, Integer> map = checkHowManyChasersEachFriendHas(paramList);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     return map.entrySet().stream()
/*  89 */       .sorted(Comparator.comparingInt(Map.Entry::getValue))
/*  90 */       .filter(paramEntry -> (((Integer)paramEntry.getValue()).intValue() > 0 && ((Integer)paramEntry.getValue()).intValue() <= 5))
/*  91 */       .map(Map.Entry::getKey)
/*  92 */       .findFirst();
/*     */   }
/*     */   
/*     */   private static Map<LivingEntity, Integer> checkHowManyChasersEachFriendHas(List<LivingEntity> paramList) {
/*  96 */     HashMap<LivingEntity, Integer> hashMap = Maps.newHashMap();
/*     */     
/*  98 */     paramList.stream()
/*  99 */       .filter(PlayTagWithOtherKids::isChasingSomeone)
/* 100 */       .forEach(paramLivingEntity -> paramMap.compute(whoAreYouChasing(paramLivingEntity), ()));
/*     */ 
/*     */ 
/*     */     
/* 104 */     return hashMap;
/*     */   }
/*     */   
/*     */   private static LivingEntity whoAreYouChasing(LivingEntity paramLivingEntity) {
/* 108 */     return paramLivingEntity.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */   }
/*     */   
/*     */   private static boolean isChasingSomeone(LivingEntity paramLivingEntity) {
/* 112 */     return paramLivingEntity.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
/*     */   }
/*     */   
/*     */   private static boolean isFriendChasingMe(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 116 */     return paramLivingEntity2.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET)
/* 117 */       .filter(paramLivingEntity2 -> (paramLivingEntity2 == paramLivingEntity1))
/* 118 */       .isPresent();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\PlayTagWithOtherKids.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */