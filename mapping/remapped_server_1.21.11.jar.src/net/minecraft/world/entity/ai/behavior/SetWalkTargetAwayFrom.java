/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SetWalkTargetAwayFrom {
/*    */   public static BehaviorControl<PathfinderMob> pos(MemoryModuleType<BlockPos> paramMemoryModuleType, float paramFloat, int paramInt, boolean paramBoolean) {
/* 17 */     return create(paramMemoryModuleType, paramFloat, paramInt, paramBoolean, Vec3::atBottomCenterOf);
/*    */   }
/*    */   
/*    */   public static OneShot<PathfinderMob> entity(MemoryModuleType<? extends Entity> paramMemoryModuleType, float paramFloat, int paramInt, boolean paramBoolean) {
/* 21 */     return create(paramMemoryModuleType, paramFloat, paramInt, paramBoolean, Entity::position);
/*    */   }
/*    */   
/*    */   private static <T> OneShot<PathfinderMob> create(MemoryModuleType<T> paramMemoryModuleType, float paramFloat, int paramInt, boolean paramBoolean, Function<T, Vec3> paramFunction) {
/* 25 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.registered(MemoryModuleType.WALK_TARGET), (App)paramInstance.present(paramMemoryModuleType)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetAwayFrom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */