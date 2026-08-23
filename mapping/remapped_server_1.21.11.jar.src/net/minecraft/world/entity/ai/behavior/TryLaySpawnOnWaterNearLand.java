/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TryLaySpawnOnWaterNearLand {
/*    */   public static BehaviorControl<LivingEntity> create(Block paramBlock) {
/* 17 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.present(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(MemoryModuleType.IS_PREGNANT)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\TryLaySpawnOnWaterNearLand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */