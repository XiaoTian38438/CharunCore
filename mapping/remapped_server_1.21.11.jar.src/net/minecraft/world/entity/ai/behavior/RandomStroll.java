/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*    */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class RandomStroll
/*    */ {
/*    */   private static final int MAX_XZ_DIST = 10;
/*    */   private static final int MAX_Y_DIST = 7;
/* 28 */   private static final int[][] SWIM_XY_DISTANCE_TIERS = new int[][] { { 1, 1 }, { 3, 3 }, { 5, 5 }, { 6, 5 }, { 7, 7 }, { 10, 7 } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static OneShot<PathfinderMob> stroll(float paramFloat) {
/* 38 */     return stroll(paramFloat, true);
/*    */   }
/*    */   
/*    */   public static OneShot<PathfinderMob> stroll(float paramFloat, boolean paramBoolean) {
/* 42 */     return strollFlyOrSwim(paramFloat, paramPathfinderMob -> LandRandomPos.getPos(paramPathfinderMob, 10, 7), paramBoolean ? (paramPathfinderMob -> true) : (paramPathfinderMob -> !paramPathfinderMob.isInWater()));
/*    */   }
/*    */   
/*    */   public static BehaviorControl<PathfinderMob> stroll(float paramFloat, int paramInt1, int paramInt2) {
/* 46 */     return strollFlyOrSwim(paramFloat, paramPathfinderMob -> LandRandomPos.getPos(paramPathfinderMob, paramInt1, paramInt2), paramPathfinderMob -> true);
/*    */   }
/*    */   
/*    */   public static BehaviorControl<PathfinderMob> fly(float paramFloat) {
/* 50 */     return strollFlyOrSwim(paramFloat, paramPathfinderMob -> getTargetFlyPos(paramPathfinderMob, 10, 7), paramPathfinderMob -> true);
/*    */   }
/*    */   
/*    */   public static BehaviorControl<PathfinderMob> swim(float paramFloat) {
/* 54 */     return strollFlyOrSwim(paramFloat, RandomStroll::getTargetSwimPos, Entity::isInWater);
/*    */   }
/*    */   
/*    */   private static OneShot<PathfinderMob> strollFlyOrSwim(float paramFloat, Function<PathfinderMob, Vec3> paramFunction, Predicate<PathfinderMob> paramPredicate) {
/* 58 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.WALK_TARGET)).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Vec3 getTargetSwimPos(PathfinderMob paramPathfinderMob) {
/* 72 */     Vec3 vec31 = null;
/* 73 */     Vec3 vec32 = null;
/*    */     
/* 75 */     for (int[] arrayOfInt : SWIM_XY_DISTANCE_TIERS) {
/*    */       
/* 77 */       if (vec31 == null) {
/* 78 */         vec32 = BehaviorUtils.getRandomSwimmablePos(paramPathfinderMob, arrayOfInt[0], arrayOfInt[1]);
/*    */       } else {
/* 80 */         vec32 = paramPathfinderMob.position().add(paramPathfinderMob.position().vectorTo(vec31).normalize().multiply(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0]));
/*    */       } 
/*    */       
/* 83 */       boolean bool = GoalUtils.mobRestricted(paramPathfinderMob, arrayOfInt[0]);
/* 84 */       if (vec32 == null || paramPathfinderMob.level().getFluidState(BlockPos.containing((Position)vec32)).isEmpty() || GoalUtils.isRestricted(bool, paramPathfinderMob, vec32)) {
/* 85 */         return vec31;
/*    */       }
/* 87 */       vec31 = vec32;
/*    */     } 
/*    */ 
/*    */     
/* 91 */     return vec32;
/*    */   }
/*    */   
/*    */   private static Vec3 getTargetFlyPos(PathfinderMob paramPathfinderMob, int paramInt1, int paramInt2) {
/* 95 */     Vec3 vec3 = paramPathfinderMob.getViewVector(0.0F);
/*    */     
/* 97 */     return AirAndWaterRandomPos.getPos(paramPathfinderMob, paramInt1, paramInt2, -2, vec3.x, vec3.z, 1.5707963705062866D);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\RandomStroll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */