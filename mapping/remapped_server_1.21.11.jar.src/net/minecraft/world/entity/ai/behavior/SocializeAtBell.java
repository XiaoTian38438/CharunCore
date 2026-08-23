/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SocializeAtBell {
/*    */   public static OneShot<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET), (App)paramInstance.present(MemoryModuleType.MEETING_POINT), (App)paramInstance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES), (App)paramInstance.absent(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */   
/*    */   private static final float SPEED_MODIFIER = 0.3F;
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SocializeAtBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */