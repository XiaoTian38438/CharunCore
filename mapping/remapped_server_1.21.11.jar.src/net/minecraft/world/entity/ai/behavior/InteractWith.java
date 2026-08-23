/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class InteractWith {
/*    */   public static <T extends LivingEntity> BehaviorControl<LivingEntity> of(EntityType<? extends T> paramEntityType, int paramInt1, MemoryModuleType<T> paramMemoryModuleType, float paramFloat, int paramInt2) {
/* 18 */     return of(paramEntityType, paramInt1, paramLivingEntity -> true, paramLivingEntity -> true, paramMemoryModuleType, paramFloat, paramInt2);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity, T extends LivingEntity> BehaviorControl<E> of(EntityType<? extends T> paramEntityType, int paramInt1, Predicate<E> paramPredicate, Predicate<T> paramPredicate1, MemoryModuleType<T> paramMemoryModuleType, float paramFloat, int paramInt2) {
/* 22 */     int i = paramInt1 * paramInt1;
/* 23 */     Predicate predicate = paramLivingEntity -> (paramEntityType.equals(paramLivingEntity.getType()) && paramPredicate.test(paramLivingEntity));
/*    */     
/* 25 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(paramMemoryModuleType), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\InteractWith.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */