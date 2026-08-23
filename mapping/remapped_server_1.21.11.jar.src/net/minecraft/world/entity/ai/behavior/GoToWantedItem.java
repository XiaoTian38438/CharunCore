/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class GoToWantedItem {
/*    */   public static BehaviorControl<LivingEntity> create(float paramFloat, boolean paramBoolean, int paramInt) {
/* 13 */     return create(paramLivingEntity -> true, paramFloat, paramBoolean, paramInt);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> paramPredicate, float paramFloat, boolean paramBoolean, int paramInt) {
/* 17 */     return BehaviorBuilder.create(paramInstance -> {
/*    */           BehaviorBuilder behaviorBuilder = paramBoolean ? paramInstance.registered(MemoryModuleType.WALK_TARGET) : paramInstance.absent(MemoryModuleType.WALK_TARGET);
/*    */           return paramInstance.group((App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)behaviorBuilder, (App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), (App)paramInstance.registered(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)).apply((Applicative)paramInstance, ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\GoToWantedItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */