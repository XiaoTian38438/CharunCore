/*   */ package net.minecraft.world.entity.ai.behavior;
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*   */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*   */ 
/*   */ public class BecomePassiveIfMemoryPresent {
/*   */   public static BehaviorControl<LivingEntity> create(MemoryModuleType<?> paramMemoryModuleType, int paramInt) {
/* 9 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.absent(MemoryModuleType.PACIFIED), (App)paramInstance.present(paramMemoryModuleType)).apply((Applicative)paramInstance, (App)paramInstance.point((), ())));
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\BecomePassiveIfMemoryPresent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */