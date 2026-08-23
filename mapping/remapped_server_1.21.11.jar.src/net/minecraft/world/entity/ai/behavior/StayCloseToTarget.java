/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StayCloseToTarget {
/*    */   public static BehaviorControl<LivingEntity> create(Function<LivingEntity, Optional<PositionTracker>> paramFunction, Predicate<LivingEntity> paramPredicate, int paramInt1, int paramInt2, float paramFloat) {
/* 14 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.registered(MemoryModuleType.WALK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\StayCloseToTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */