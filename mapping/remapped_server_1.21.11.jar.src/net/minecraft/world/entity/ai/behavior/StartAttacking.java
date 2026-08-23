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
/*    */ public class StartAttacking {
/*    */   public static <E extends Mob> BehaviorControl<E> create(TargetFinder<E> paramTargetFinder) {
/* 16 */     return create((paramServerLevel, paramMob) -> true, paramTargetFinder);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StartAttackingCondition<E> paramStartAttackingCondition, TargetFinder<E> paramTargetFinder) {
/* 23 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface StartAttackingCondition<E> {
/*    */     boolean test(ServerLevel param1ServerLevel, E param1E);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TargetFinder<E> {
/*    */     Optional<? extends LivingEntity> get(ServerLevel param1ServerLevel, E param1E);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */