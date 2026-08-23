/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class TryFindLandNearWater {
/*    */   public static BehaviorControl<PathfinderMob> create(int paramInt, float paramFloat) {
/* 19 */     MutableLong mutableLong = new MutableLong(0L);
/*    */     
/* 21 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.ATTACK_TARGET), (App)paramInstance.absent(MemoryModuleType.WALK_TARGET), (App)paramInstance.registered(MemoryModuleType.LOOK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\TryFindLandNearWater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */