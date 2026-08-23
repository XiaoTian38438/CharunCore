/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class LocateHidingPlace {
/*    */   public static OneShot<LivingEntity> create(int paramInt1, float paramFloat, int paramInt2) {
/* 13 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.HOME), (App)paramInstance.registered(MemoryModuleType.HIDING_PLACE), (App)paramInstance.registered(MemoryModuleType.PATH), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.registered(MemoryModuleType.BREED_TARGET), (App)paramInstance.registered(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\LocateHidingPlace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */