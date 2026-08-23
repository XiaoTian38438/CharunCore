/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SetWalkTargetFromLookTarget {
/*    */   public static OneShot<LivingEntity> create(float paramFloat, int paramInt) {
/* 16 */     return create(paramLivingEntity -> true, paramLivingEntity -> Float.valueOf(paramFloat), paramInt);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(Predicate<LivingEntity> paramPredicate, Function<LivingEntity, Float> paramFunction, int paramInt) {
/* 20 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(MemoryModuleType.LOOK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */