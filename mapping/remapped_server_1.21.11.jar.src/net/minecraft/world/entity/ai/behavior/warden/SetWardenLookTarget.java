/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class SetWardenLookTarget {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 15 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.registered(MemoryModuleType.DISTURBANCE_LOCATION), (App)paramInstance.registered(MemoryModuleType.ROAR_TARGET), (App)paramInstance.absent(MemoryModuleType.ATTACK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\warden\SetWardenLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */