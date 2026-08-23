/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class VillagerCalmDown {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.HURT_BY), (App)paramInstance.registered(MemoryModuleType.HURT_BY_ENTITY), (App)paramInstance.registered(MemoryModuleType.NEAREST_HOSTILE)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */   
/*    */   private static final int SAFE_DISTANCE_FROM_DANGER = 36;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\VillagerCalmDown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */