/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetWalkTargetFromBlockMemory
/*    */ {
/*    */   public static OneShot<Villager> create(MemoryModuleType<GlobalPos> paramMemoryModuleType, float paramFloat, int paramInt1, int paramInt2, int paramInt3) {
/* 26 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromBlockMemory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */