/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public interface CollisionGetter
/*     */   extends BlockGetter
/*     */ {
/*     */   default boolean isUnobstructed(Entity paramEntity, VoxelShape paramVoxelShape) {
/*  28 */     return true;
/*     */   }
/*     */   
/*     */   default boolean isUnobstructed(BlockState paramBlockState, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  32 */     VoxelShape voxelShape = paramBlockState.getCollisionShape(this, paramBlockPos, paramCollisionContext);
/*  33 */     return (voxelShape.isEmpty() || isUnobstructed(null, voxelShape.move((Vec3i)paramBlockPos)));
/*     */   }
/*     */   
/*     */   default boolean isUnobstructed(Entity paramEntity) {
/*  37 */     return isUnobstructed(paramEntity, Shapes.create(paramEntity.getBoundingBox()));
/*     */   }
/*     */   
/*     */   default boolean noCollision(AABB paramAABB) {
/*  41 */     return noCollision(null, paramAABB);
/*     */   }
/*     */   
/*     */   default boolean noCollision(Entity paramEntity) {
/*  45 */     return noCollision(paramEntity, paramEntity.getBoundingBox());
/*     */   }
/*     */   
/*     */   default boolean noCollision(Entity paramEntity, AABB paramAABB) {
/*  49 */     return noCollision(paramEntity, paramAABB, false);
/*     */   }
/*     */ 
/*     */   
/*     */   default boolean noCollision(Entity paramEntity, AABB paramAABB, boolean paramBoolean) {
/*  54 */     return (noBlockCollision(paramEntity, paramAABB, paramBoolean) && noEntityCollision(paramEntity, paramAABB) && noBorderCollision(paramEntity, paramAABB));
/*     */   }
/*     */   
/*     */   default boolean noBlockCollision(Entity paramEntity, AABB paramAABB) {
/*  58 */     return noBlockCollision(paramEntity, paramAABB, false);
/*     */   }
/*     */   
/*     */   default boolean noBlockCollision(Entity paramEntity, AABB paramAABB, boolean paramBoolean) {
/*  62 */     Iterable<VoxelShape> iterable = paramBoolean ? getBlockAndLiquidCollisions(paramEntity, paramAABB) : getBlockCollisions(paramEntity, paramAABB);
/*  63 */     for (VoxelShape voxelShape : iterable) {
/*  64 */       if (!voxelShape.isEmpty()) {
/*  65 */         return false;
/*     */       }
/*     */     } 
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   default boolean noEntityCollision(Entity paramEntity, AABB paramAABB) {
/*  72 */     return getEntityCollisions(paramEntity, paramAABB).isEmpty();
/*     */   }
/*     */   
/*     */   default boolean noBorderCollision(Entity paramEntity, AABB paramAABB) {
/*  76 */     if (paramEntity != null) {
/*  77 */       VoxelShape voxelShape = borderCollision(paramEntity, paramAABB);
/*  78 */       return (voxelShape == null || !Shapes.joinIsNotEmpty(voxelShape, Shapes.create(paramAABB), BooleanOp.AND));
/*     */     } 
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default Iterable<VoxelShape> getCollisions(Entity paramEntity, AABB paramAABB) {
/*  86 */     List<VoxelShape> list = getEntityCollisions(paramEntity, paramAABB);
/*  87 */     Iterable<VoxelShape> iterable = getBlockCollisions(paramEntity, paramAABB);
/*  88 */     return list.isEmpty() ? iterable : Iterables.concat(list, iterable);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getPreMoveCollisions(Entity paramEntity, AABB paramAABB, Vec3 paramVec3) {
/*  92 */     List<VoxelShape> list = getEntityCollisions(paramEntity, paramAABB);
/*  93 */     Iterable<VoxelShape> iterable = getBlockCollisionsFromContext(CollisionContext.withPosition(paramEntity, paramVec3.y), paramAABB);
/*  94 */     return list.isEmpty() ? iterable : Iterables.concat(list, iterable);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getBlockCollisions(Entity paramEntity, AABB paramAABB) {
/*  98 */     return getBlockCollisionsFromContext((paramEntity == null) ? CollisionContext.empty() : CollisionContext.of(paramEntity), paramAABB);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity paramEntity, AABB paramAABB) {
/* 102 */     return getBlockCollisionsFromContext((paramEntity == null) ? CollisionContext.emptyWithFluidCollisions() : CollisionContext.of(paramEntity, true), paramAABB);
/*     */   }
/*     */   
/*     */   private Iterable<VoxelShape> getBlockCollisionsFromContext(CollisionContext paramCollisionContext, AABB paramAABB) {
/* 106 */     return () -> new BlockCollisions(this, paramCollisionContext, paramAABB, false, ());
/*     */   }
/*     */   
/*     */   private VoxelShape borderCollision(Entity paramEntity, AABB paramAABB) {
/* 110 */     WorldBorder worldBorder = getWorldBorder();
/* 111 */     return worldBorder.isInsideCloseToBorder(paramEntity, paramAABB) ? worldBorder.getCollisionShape() : null;
/*     */   }
/*     */   
/*     */   default BlockHitResult clipIncludingBorder(ClipContext paramClipContext) {
/* 115 */     BlockHitResult blockHitResult = clip(paramClipContext);
/* 116 */     WorldBorder worldBorder = getWorldBorder();
/* 117 */     if (worldBorder.isWithinBounds(paramClipContext.getFrom()) && !worldBorder.isWithinBounds(blockHitResult.getLocation())) {
/* 118 */       Vec3 vec31 = blockHitResult.getLocation().subtract(paramClipContext.getFrom());
/* 119 */       Direction direction = Direction.getApproximateNearest(vec31.x, vec31.y, vec31.z);
/* 120 */       Vec3 vec32 = worldBorder.clampVec3ToBound(blockHitResult.getLocation());
/* 121 */       return new BlockHitResult(vec32, direction, BlockPos.containing((Position)vec32), false, true);
/*     */     } 
/* 123 */     return blockHitResult;
/*     */   }
/*     */   
/*     */   default boolean collidesWithSuffocatingBlock(Entity paramEntity, AABB paramAABB) {
/* 127 */     BlockCollisions blockCollisions = new BlockCollisions(this, paramEntity, paramAABB, true, (paramMutableBlockPos, paramVoxelShape) -> paramVoxelShape);
/* 128 */     while (blockCollisions.hasNext()) {
/* 129 */       if (!((VoxelShape)blockCollisions.next()).isEmpty()) {
/* 130 */         return true;
/*     */       }
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   default Optional<BlockPos> findSupportingBlock(Entity paramEntity, AABB paramAABB) {
/* 137 */     BlockPos blockPos = null;
/* 138 */     double d = Double.MAX_VALUE;
/* 139 */     BlockCollisions blockCollisions = new BlockCollisions(this, paramEntity, paramAABB, false, (paramMutableBlockPos, paramVoxelShape) -> paramMutableBlockPos);
/* 140 */     while (blockCollisions.hasNext()) {
/* 141 */       BlockPos blockPos1 = (BlockPos)blockCollisions.next();
/* 142 */       double d1 = blockPos1.distToCenterSqr((Position)paramEntity.position());
/* 143 */       if (d1 < d || (d1 == d && (blockPos == null || blockPos.compareTo((Vec3i)blockPos1) < 0))) {
/* 144 */         blockPos = blockPos1.immutable();
/* 145 */         d = d1;
/*     */       } 
/*     */     } 
/* 148 */     return Optional.ofNullable(blockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Optional<Vec3> findFreePosition(Entity paramEntity, VoxelShape paramVoxelShape, Vec3 paramVec3, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 158 */     if (paramVoxelShape.isEmpty()) {
/* 159 */       return Optional.empty();
/*     */     }
/*     */     
/* 162 */     AABB aABB = paramVoxelShape.bounds().inflate(paramDouble1, paramDouble2, paramDouble3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     VoxelShape voxelShape1 = StreamSupport.stream(getBlockCollisions(paramEntity, aABB).spliterator(), false).filter(paramVoxelShape -> (getWorldBorder() == null || getWorldBorder().isWithinBounds(paramVoxelShape.bounds()))).flatMap(paramVoxelShape -> paramVoxelShape.toAabbs().stream()).map(paramAABB -> paramAABB.inflate(paramDouble1 / 2.0D, paramDouble2 / 2.0D, paramDouble3 / 2.0D)).map(Shapes::create).reduce(Shapes.empty(), Shapes::or);
/*     */ 
/*     */     
/* 171 */     VoxelShape voxelShape2 = Shapes.join(paramVoxelShape, voxelShape1, BooleanOp.ONLY_FIRST);
/*     */     
/* 173 */     return voxelShape2.closestPointTo(paramVec3);
/*     */   }
/*     */   
/*     */   WorldBorder getWorldBorder();
/*     */   
/*     */   BlockGetter getChunkForCollisions(int paramInt1, int paramInt2);
/*     */   
/*     */   List<VoxelShape> getEntityCollisions(Entity paramEntity, AABB paramAABB);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\CollisionGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */