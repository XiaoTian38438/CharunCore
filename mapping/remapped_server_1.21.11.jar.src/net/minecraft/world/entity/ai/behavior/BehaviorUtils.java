/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BehaviorUtils
/*     */ {
/*     */   public static void lockGazeAndWalkToEachOther(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2, float paramFloat, int paramInt) {
/*  37 */     lookAtEachOther(paramLivingEntity1, paramLivingEntity2);
/*  38 */     setWalkAndLookTargetMemoriesToEachOther(paramLivingEntity1, paramLivingEntity2, paramFloat, paramInt);
/*     */   }
/*     */   
/*     */   public static boolean entityIsVisible(Brain<?> paramBrain, LivingEntity paramLivingEntity) {
/*  42 */     Optional<NearestVisibleLivingEntities> optional = paramBrain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*  43 */     return (optional.isPresent() && ((NearestVisibleLivingEntities)optional.get()).contains(paramLivingEntity));
/*     */   }
/*     */   
/*     */   public static boolean targetIsValid(Brain<?> paramBrain, MemoryModuleType<? extends LivingEntity> paramMemoryModuleType, EntityType<?> paramEntityType) {
/*  47 */     return targetIsValid(paramBrain, paramMemoryModuleType, paramLivingEntity -> (paramLivingEntity.getType() == paramEntityType));
/*     */   }
/*     */   
/*     */   private static boolean targetIsValid(Brain<?> paramBrain, MemoryModuleType<? extends LivingEntity> paramMemoryModuleType, Predicate<LivingEntity> paramPredicate) {
/*  51 */     return paramBrain.getMemory(paramMemoryModuleType)
/*  52 */       .filter(paramPredicate)
/*  53 */       .filter(LivingEntity::isAlive)
/*  54 */       .filter(paramLivingEntity -> entityIsVisible(paramBrain, paramLivingEntity))
/*  55 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static void lookAtEachOther(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  59 */     lookAtEntity(paramLivingEntity1, paramLivingEntity2);
/*  60 */     lookAtEntity(paramLivingEntity2, paramLivingEntity1);
/*     */   }
/*     */   
/*     */   public static void lookAtEntity(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/*  64 */     paramLivingEntity1.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)paramLivingEntity2, true));
/*     */   }
/*     */   
/*     */   private static void setWalkAndLookTargetMemoriesToEachOther(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2, float paramFloat, int paramInt) {
/*  68 */     setWalkAndLookTargetMemories(paramLivingEntity1, (Entity)paramLivingEntity2, paramFloat, paramInt);
/*  69 */     setWalkAndLookTargetMemories(paramLivingEntity2, (Entity)paramLivingEntity1, paramFloat, paramInt);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity paramLivingEntity, Entity paramEntity, float paramFloat, int paramInt) {
/*  73 */     setWalkAndLookTargetMemories(paramLivingEntity, new EntityTracker(paramEntity, true), paramFloat, paramInt);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity paramLivingEntity, BlockPos paramBlockPos, float paramFloat, int paramInt) {
/*  77 */     setWalkAndLookTargetMemories(paramLivingEntity, new BlockPosTracker(paramBlockPos), paramFloat, paramInt);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity paramLivingEntity, PositionTracker paramPositionTracker, float paramFloat, int paramInt) {
/*  81 */     WalkTarget walkTarget = new WalkTarget(paramPositionTracker, paramFloat, paramInt);
/*  82 */     paramLivingEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, paramPositionTracker);
/*  83 */     paramLivingEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
/*     */   }
/*     */   
/*     */   public static void throwItem(LivingEntity paramLivingEntity, ItemStack paramItemStack, Vec3 paramVec3) {
/*  87 */     Vec3 vec3 = new Vec3(0.30000001192092896D, 0.30000001192092896D, 0.30000001192092896D);
/*  88 */     throwItem(paramLivingEntity, paramItemStack, paramVec3, vec3, 0.3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void throwItem(LivingEntity paramLivingEntity, ItemStack paramItemStack, Vec3 paramVec31, Vec3 paramVec32, float paramFloat) {
/*  93 */     double d = paramLivingEntity.getEyeY() - paramFloat;
/*  94 */     ItemEntity itemEntity = new ItemEntity(paramLivingEntity.level(), paramLivingEntity.getX(), d, paramLivingEntity.getZ(), paramItemStack);
/*  95 */     itemEntity.setThrower((Entity)paramLivingEntity);
/*     */     
/*  97 */     Vec3 vec3 = paramVec31.subtract(paramLivingEntity.position());
/*  98 */     vec3 = vec3.normalize().multiply(paramVec32.x, paramVec32.y, paramVec32.z);
/*     */     
/* 100 */     itemEntity.setDeltaMovement(vec3);
/* 101 */     itemEntity.setDefaultPickUpDelay();
/* 102 */     paramLivingEntity.level().addFreshEntity((Entity)itemEntity);
/*     */   }
/*     */   
/*     */   public static SectionPos findSectionClosestToVillage(ServerLevel paramServerLevel, SectionPos paramSectionPos, int paramInt) {
/* 106 */     int i = paramServerLevel.sectionsToVillage(paramSectionPos);
/*     */ 
/*     */ 
/*     */     
/* 110 */     Objects.requireNonNull(paramServerLevel); return SectionPos.cube(paramSectionPos, paramInt).filter(paramSectionPos -> (paramServerLevel.sectionsToVillage(paramSectionPos) < paramInt)).min(Comparator.comparingInt(paramServerLevel::sectionsToVillage))
/* 111 */       .orElse(paramSectionPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWithinAttackRange(Mob paramMob, LivingEntity paramLivingEntity, int paramInt) {
/* 118 */     Item item = paramMob.getMainHandItem().getItem(); if (item instanceof ProjectileWeaponItem) { ProjectileWeaponItem projectileWeaponItem = (ProjectileWeaponItem)item; if (paramMob.canUseNonMeleeWeapon(paramMob.getMainHandItem())) {
/* 119 */         int i = projectileWeaponItem.getDefaultProjectileRange() - paramInt;
/* 120 */         return paramMob.closerThan((Entity)paramLivingEntity, i);
/*     */       }  }
/* 122 */      return paramMob.isWithinMeleeAttackRange(paramLivingEntity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2, double paramDouble) {
/* 130 */     Optional<LivingEntity> optional = paramLivingEntity1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 131 */     if (optional.isEmpty()) {
/* 132 */       return false;
/*     */     }
/* 134 */     double d1 = paramLivingEntity1.distanceToSqr(((LivingEntity)optional.get()).position());
/* 135 */     double d2 = paramLivingEntity1.distanceToSqr(paramLivingEntity2.position());
/* 136 */     return (d2 > d1 + paramDouble * paramDouble);
/*     */   }
/*     */   
/*     */   public static boolean canSee(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 140 */     Brain brain = paramLivingEntity1.getBrain();
/* 141 */     if (!brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)) {
/* 142 */       return false;
/*     */     }
/* 144 */     return ((NearestVisibleLivingEntities)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).contains(paramLivingEntity2);
/*     */   }
/*     */   
/*     */   public static LivingEntity getNearestTarget(LivingEntity paramLivingEntity1, Optional<LivingEntity> paramOptional, LivingEntity paramLivingEntity2) {
/* 148 */     if (paramOptional.isEmpty()) {
/* 149 */       return paramLivingEntity2;
/*     */     }
/* 151 */     return getTargetNearestMe(paramLivingEntity1, paramOptional.get(), paramLivingEntity2);
/*     */   }
/*     */   
/*     */   public static LivingEntity getTargetNearestMe(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2, LivingEntity paramLivingEntity3) {
/* 155 */     Vec3 vec31 = paramLivingEntity2.position();
/* 156 */     Vec3 vec32 = paramLivingEntity3.position();
/* 157 */     return (paramLivingEntity1.distanceToSqr(vec31) < paramLivingEntity1.distanceToSqr(vec32)) ? paramLivingEntity2 : paramLivingEntity3;
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getLivingEntityFromUUIDMemory(LivingEntity paramLivingEntity, MemoryModuleType<UUID> paramMemoryModuleType) {
/* 161 */     Optional optional = paramLivingEntity.getBrain().getMemory(paramMemoryModuleType);
/*     */     
/* 163 */     return optional.map(paramUUID -> paramLivingEntity.level().getEntity(paramUUID)).map(paramEntity -> {
/*     */           LivingEntity livingEntity = (LivingEntity)paramEntity;
/*     */           return (paramEntity instanceof LivingEntity) ? livingEntity : null;
/*     */         }); } public static Vec3 getRandomSwimmablePos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2) {
/* 167 */     Vec3 vec3 = DefaultRandomPos.getPos(paramPathfinderMob, paramInt1, paramInt2);
/* 168 */     byte b = 0;
/* 169 */     while (vec3 != null && !paramPathfinderMob.level().getBlockState(BlockPos.containing((Position)vec3)).isPathfindable(PathComputationType.WATER) && b++ < 10) {
/* 170 */       vec3 = DefaultRandomPos.getPos(paramPathfinderMob, paramInt1, paramInt2);
/*     */     }
/* 172 */     return vec3;
/*     */   }
/*     */   
/*     */   public static boolean isBreeding(LivingEntity paramLivingEntity) {
/* 176 */     return paramLivingEntity.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\BehaviorUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */