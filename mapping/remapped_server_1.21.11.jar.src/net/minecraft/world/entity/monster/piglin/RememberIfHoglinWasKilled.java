/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class RememberIfHoglinWasKilled {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 11 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.registered(MemoryModuleType.HUNTED_RECENTLY)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\RememberIfHoglinWasKilled.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */