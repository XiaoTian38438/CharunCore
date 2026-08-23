/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveToSkySeeingSpot {
/*    */   public static OneShot<LivingEntity> create(float paramFloat) {
/* 18 */     return BehaviorBuilder.create(paramInstance -> paramInstance.group((App)paramInstance.absent(MemoryModuleType.WALK_TARGET)).apply((Applicative)paramInstance, ()));
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
/*    */   
/*    */   private static Vec3 getOutdoorPosition(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 33 */     RandomSource randomSource = paramLivingEntity.getRandom();
/* 34 */     BlockPos blockPos = paramLivingEntity.blockPosition();
/*    */     
/* 36 */     for (byte b = 0; b < 10; b++) {
/* 37 */       BlockPos blockPos1 = blockPos.offset(randomSource.nextInt(20) - 10, randomSource.nextInt(6) - 3, randomSource.nextInt(20) - 10);
/*    */       
/* 39 */       if (hasNoBlocksAbove(paramServerLevel, paramLivingEntity, blockPos1)) {
/* 40 */         return Vec3.atBottomCenterOf((Vec3i)blockPos1);
/*    */       }
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean hasNoBlocksAbove(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, BlockPos paramBlockPos) {
/* 47 */     return (paramServerLevel.canSeeSky(paramBlockPos) && paramServerLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, paramBlockPos).getY() <= paramLivingEntity.getY());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\MoveToSkySeeingSpot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */