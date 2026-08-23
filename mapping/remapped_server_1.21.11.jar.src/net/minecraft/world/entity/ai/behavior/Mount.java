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
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class Mount
/*    */ {
/*    */   public static BehaviorControl<LivingEntity> create(float paramFloat) {
/* 17 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(MemoryModuleType.RIDE_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */   
/*    */   private static final int CLOSE_ENOUGH_TO_START_RIDING_DIST = 1;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\Mount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */