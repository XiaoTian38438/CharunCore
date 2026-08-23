/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class ReactToBell {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 11 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.present(MemoryModuleType.HEARD_BELL_TIME)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\ReactToBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */