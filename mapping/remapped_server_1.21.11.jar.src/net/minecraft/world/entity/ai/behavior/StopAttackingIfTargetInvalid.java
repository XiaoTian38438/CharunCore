/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ public class StopAttackingIfTargetInvalid
/*    */ {
/*    */   private static final int TIMEOUT_TO_GET_WITHIN_ATTACK_RANGE = 200;
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(TargetErasedCallback<E> paramTargetErasedCallback) {
/* 20 */     return create((paramServerLevel, paramLivingEntity) -> false, paramTargetErasedCallback, true);
/*    */   }
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StopAttackCondition paramStopAttackCondition) {
/* 24 */     return create(paramStopAttackCondition, (paramServerLevel, paramMob, paramLivingEntity) -> {  }true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create() {
/* 32 */     return create((paramServerLevel, paramLivingEntity) -> false, (paramServerLevel, paramMob, paramLivingEntity) -> {  }true);
/*    */   }
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StopAttackCondition paramStopAttackCondition, TargetErasedCallback<E> paramTargetErasedCallback, boolean paramBoolean) {
/* 36 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply((Applicative)paramInstance, ()));
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isTiredOfTryingToReachTarget(LivingEntity paramLivingEntity, Optional<Long> paramOptional) {
/* 58 */     return (paramOptional.isPresent() && paramLivingEntity.level().getGameTime() - ((Long)paramOptional.get()).longValue() > 200L);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface StopAttackCondition {
/*    */     boolean test(ServerLevel param1ServerLevel, LivingEntity param1LivingEntity);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TargetErasedCallback<E> {
/*    */     void accept(ServerLevel param1ServerLevel, E param1E, LivingEntity param1LivingEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StopAttackingIfTargetInvalid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */