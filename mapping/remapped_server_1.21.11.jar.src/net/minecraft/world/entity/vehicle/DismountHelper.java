/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.block.TrapDoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DismountHelper
/*     */ {
/*     */   public static int[][] offsetsForDirection(Direction paramDirection) {
/*  25 */     Direction direction1 = paramDirection.getClockWise();
/*  26 */     Direction direction2 = direction1.getOpposite();
/*  27 */     Direction direction3 = paramDirection.getOpposite();
/*     */     
/*  29 */     return new int[][] { { direction1
/*  30 */           .getStepX(), direction1.getStepZ() }, { direction2
/*  31 */           .getStepX(), direction2.getStepZ() }, { direction3
/*  32 */           .getStepX() + direction1.getStepX(), direction3.getStepZ() + direction1.getStepZ() }, { direction3
/*  33 */           .getStepX() + direction2.getStepX(), direction3.getStepZ() + direction2.getStepZ() }, { paramDirection
/*  34 */           .getStepX() + direction1.getStepX(), paramDirection.getStepZ() + direction1.getStepZ() }, { paramDirection
/*  35 */           .getStepX() + direction2.getStepX(), paramDirection.getStepZ() + direction2.getStepZ() }, { direction3
/*  36 */           .getStepX(), direction3.getStepZ() }, { paramDirection
/*  37 */           .getStepX(), paramDirection.getStepZ() } };
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBlockFloorValid(double paramDouble) {
/*  42 */     return (!Double.isInfinite(paramDouble) && paramDouble < 1.0D);
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter paramCollisionGetter, LivingEntity paramLivingEntity, AABB paramAABB) {
/*  46 */     Iterable iterable = paramCollisionGetter.getBlockCollisions((Entity)paramLivingEntity, paramAABB);
/*  47 */     for (VoxelShape voxelShape : iterable) {
/*  48 */       if (!voxelShape.isEmpty()) {
/*  49 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     if (!paramCollisionGetter.getWorldBorder().isWithinBounds(paramAABB)) {
/*  54 */       return false;
/*     */     }
/*     */     
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean canDismountTo(CollisionGetter paramCollisionGetter, Vec3 paramVec3, LivingEntity paramLivingEntity, Pose paramPose) {
/*  61 */     return canDismountTo(paramCollisionGetter, paramLivingEntity, paramLivingEntity.getLocalBoundsForPose(paramPose).move(paramVec3));
/*     */   }
/*     */   
/*     */   public static VoxelShape nonClimbableShape(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  65 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos);
/*  66 */     if (blockState.is(BlockTags.CLIMBABLE) || (blockState.getBlock() instanceof TrapDoorBlock && ((Boolean)blockState.getValue((Property)TrapDoorBlock.OPEN)).booleanValue())) {
/*  67 */       return Shapes.empty();
/*     */     }
/*  69 */     return blockState.getCollisionShape(paramBlockGetter, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static double findCeilingFrom(BlockPos paramBlockPos, int paramInt, Function<BlockPos, VoxelShape> paramFunction) {
/*  73 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*  74 */     byte b = 0;
/*  75 */     while (b < paramInt) {
/*  76 */       VoxelShape voxelShape = paramFunction.apply(mutableBlockPos);
/*  77 */       if (!voxelShape.isEmpty()) {
/*  78 */         return (paramBlockPos.getY() + b) + voxelShape.min(Direction.Axis.Y);
/*     */       }
/*  80 */       b++;
/*  81 */       mutableBlockPos.move(Direction.UP);
/*     */     } 
/*  83 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public static Vec3 findSafeDismountLocation(EntityType<?> paramEntityType, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos, boolean paramBoolean) {
/*  87 */     if (paramBoolean && paramEntityType.isBlockDangerous(paramCollisionGetter.getBlockState(paramBlockPos))) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     double d = paramCollisionGetter.getBlockFloorHeight(nonClimbableShape((BlockGetter)paramCollisionGetter, paramBlockPos), () -> nonClimbableShape((BlockGetter)paramCollisionGetter, paramBlockPos.below()));
/*  92 */     if (!isBlockFloorValid(d)) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     if (paramBoolean && d <= 0.0D && paramEntityType.isBlockDangerous(paramCollisionGetter.getBlockState(paramBlockPos.below()))) {
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     Vec3 vec3 = Vec3.upFromBottomCenterOf((Vec3i)paramBlockPos, d);
/* 101 */     AABB aABB = paramEntityType.getDimensions().makeBoundingBox(vec3);
/* 102 */     Iterable iterable = paramCollisionGetter.getBlockCollisions(null, aABB);
/* 103 */     for (VoxelShape voxelShape : iterable) {
/* 104 */       if (!voxelShape.isEmpty()) {
/* 105 */         return null;
/*     */       }
/*     */     } 
/*     */     
/* 109 */     if (paramEntityType == EntityType.PLAYER)
/*     */     {
/* 111 */       if (paramCollisionGetter.getBlockState(paramBlockPos).is(BlockTags.INVALID_SPAWN_INSIDE) || paramCollisionGetter.getBlockState(paramBlockPos.above()).is(BlockTags.INVALID_SPAWN_INSIDE)) {
/* 112 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 116 */     if (!paramCollisionGetter.getWorldBorder().isWithinBounds(aABB)) {
/* 117 */       return null;
/*     */     }
/*     */     
/* 120 */     return vec3;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\DismountHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */