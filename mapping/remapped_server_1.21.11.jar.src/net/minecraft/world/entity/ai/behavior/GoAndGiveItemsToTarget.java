/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.animal.allay.Allay;
/*     */ import net.minecraft.world.entity.animal.allay.AllayAi;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GoAndGiveItemsToTarget<E extends LivingEntity & InventoryCarrier>
/*     */   extends Behavior<E>
/*     */ {
/*     */   private static final int CLOSE_ENOUGH_DISTANCE_TO_TARGET = 3;
/*     */   private static final int ITEM_PICKUP_COOLDOWN_AFTER_THROWING = 60;
/*     */   private final Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter;
/*     */   private final float speedModifier;
/*     */   
/*     */   public GoAndGiveItemsToTarget(Function<LivingEntity, Optional<PositionTracker>> paramFunction, float paramFloat, int paramInt) {
/*  34 */     super(Map.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.REGISTERED), paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     this.targetPositionGetter = paramFunction;
/*  40 */     this.speedModifier = paramFloat;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel paramServerLevel, E paramE) {
/*  45 */     return canThrowItemToTarget(paramE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel paramServerLevel, E paramE, long paramLong) {
/*  50 */     return canThrowItemToTarget(paramE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel paramServerLevel, E paramE, long paramLong) {
/*  55 */     ((Optional)this.targetPositionGetter.apply((LivingEntity)paramE)).ifPresent(paramPositionTracker -> BehaviorUtils.setWalkAndLookTargetMemories(paramLivingEntity, paramPositionTracker, this.speedModifier, 3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel paramServerLevel, E paramE, long paramLong) {
/*  62 */     Optional<PositionTracker> optional = this.targetPositionGetter.apply((LivingEntity)paramE);
/*  63 */     if (optional.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     PositionTracker positionTracker = optional.get();
/*  68 */     double d = positionTracker.currentPosition().distanceTo(paramE.getEyePosition());
/*  69 */     if (d < 3.0D) {
/*  70 */       ItemStack itemStack = ((InventoryCarrier)paramE).getInventory().removeItem(0, 1);
/*  71 */       if (!itemStack.isEmpty()) {
/*  72 */         throwItem((LivingEntity)paramE, itemStack, getThrowPosition(positionTracker));
/*  73 */         if (paramE instanceof Allay) { Allay allay = (Allay)paramE;
/*  74 */           AllayAi.getLikedPlayer((LivingEntity)allay).ifPresent(paramServerPlayer -> triggerDropItemOnBlock(paramPositionTracker, paramItemStack, paramServerPlayer)); }
/*     */         
/*  76 */         paramE.getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, Integer.valueOf(60));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void triggerDropItemOnBlock(PositionTracker paramPositionTracker, ItemStack paramItemStack, ServerPlayer paramServerPlayer) {
/*  82 */     BlockPos blockPos = paramPositionTracker.currentBlockPosition().below();
/*  83 */     CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.trigger(paramServerPlayer, blockPos, paramItemStack);
/*     */   }
/*     */   
/*     */   private boolean canThrowItemToTarget(E paramE) {
/*  87 */     if (((InventoryCarrier)paramE).getInventory().isEmpty()) {
/*  88 */       return false;
/*     */     }
/*  90 */     Optional optional = this.targetPositionGetter.apply((LivingEntity)paramE);
/*  91 */     return optional.isPresent();
/*     */   }
/*     */   
/*     */   private static Vec3 getThrowPosition(PositionTracker paramPositionTracker) {
/*  95 */     return paramPositionTracker.currentPosition().add(0.0D, 1.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public static void throwItem(LivingEntity paramLivingEntity, ItemStack paramItemStack, Vec3 paramVec3) {
/*  99 */     Vec3 vec3 = new Vec3(0.20000000298023224D, 0.30000001192092896D, 0.20000000298023224D);
/* 100 */     BehaviorUtils.throwItem(paramLivingEntity, paramItemStack, paramVec3, vec3, 0.2F);
/*     */     
/* 102 */     Level level = paramLivingEntity.level();
/* 103 */     if (level.getGameTime() % 7L == 0L && level.random.nextDouble() < 0.9D) {
/* 104 */       float f = ((Float)Util.getRandom((List)Allay.THROW_SOUND_PITCHES, level.getRandom())).floatValue();
/* 105 */       level.playSound(null, (Entity)paramLivingEntity, SoundEvents.ALLAY_THROW, SoundSource.NEUTRAL, 1.0F, f);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\GoAndGiveItemsToTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */