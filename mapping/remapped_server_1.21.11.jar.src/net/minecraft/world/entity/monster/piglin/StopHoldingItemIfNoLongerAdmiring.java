/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StopHoldingItemIfNoLongerAdmiring {
/*    */   public static BehaviorControl<Piglin> create() {
/* 10 */     return (BehaviorControl<Piglin>)BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.ADMIRING_ITEM)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\StopHoldingItemIfNoLongerAdmiring.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */