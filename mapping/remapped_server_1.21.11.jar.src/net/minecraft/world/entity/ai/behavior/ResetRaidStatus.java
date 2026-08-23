/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ public class ResetRaidStatus {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 11 */     return BehaviorBuilder.create(paramInstance -> paramInstance.point(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\ResetRaidStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */