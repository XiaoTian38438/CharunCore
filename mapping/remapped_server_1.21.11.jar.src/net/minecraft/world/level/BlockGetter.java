/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface BlockGetter
/*     */   extends LevelHeightAccessor
/*     */ {
/*     */   default <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos paramBlockPos, BlockEntityType<T> paramBlockEntityType) {
/*  39 */     BlockEntity blockEntity = getBlockEntity(paramBlockPos);
/*  40 */     if (blockEntity == null || blockEntity.getType() != paramBlockEntityType) {
/*  41 */       return Optional.empty();
/*     */     }
/*  43 */     return Optional.of((T)blockEntity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default int getLightEmission(BlockPos paramBlockPos) {
/*  51 */     return getBlockState(paramBlockPos).getLightEmission();
/*     */   }
/*     */   
/*     */   default Stream<BlockState> getBlockStates(AABB paramAABB) {
/*  55 */     return BlockPos.betweenClosedStream(paramAABB).map(this::getBlockState);
/*     */   }
/*     */   
/*     */   default BlockHitResult isBlockInLine(ClipBlockStateContext paramClipBlockStateContext) {
/*  59 */     return traverseBlocks(paramClipBlockStateContext.getFrom(), paramClipBlockStateContext.getTo(), paramClipBlockStateContext, (paramClipBlockStateContext, paramBlockPos) -> {
/*     */           BlockState blockState = getBlockState(paramBlockPos);
/*     */           Vec3 vec3 = paramClipBlockStateContext.getFrom().subtract(paramClipBlockStateContext.getTo());
/*     */           return paramClipBlockStateContext.isTargetBlock().test(blockState) ? new BlockHitResult(paramClipBlockStateContext.getTo(), Direction.getApproximateNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing((Position)paramClipBlockStateContext.getTo()), false) : null;
/*     */         }paramClipBlockStateContext -> {
/*     */           Vec3 vec3 = paramClipBlockStateContext.getFrom().subtract(paramClipBlockStateContext.getTo());
/*     */           return BlockHitResult.miss(paramClipBlockStateContext.getTo(), Direction.getApproximateNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing((Position)paramClipBlockStateContext.getTo()));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   default BlockHitResult clip(ClipContext paramClipContext) {
/*  71 */     return traverseBlocks(paramClipContext.getFrom(), paramClipContext.getTo(), paramClipContext, (paramClipContext, paramBlockPos) -> {
/*     */           BlockState blockState = getBlockState(paramBlockPos);
/*     */           FluidState fluidState = getFluidState(paramBlockPos);
/*     */           Vec3 vec31 = paramClipContext.getFrom();
/*     */           Vec3 vec32 = paramClipContext.getTo();
/*     */           VoxelShape voxelShape1 = paramClipContext.getBlockShape(blockState, this, paramBlockPos);
/*     */           BlockHitResult blockHitResult1 = clipWithInteractionOverride(vec31, vec32, paramBlockPos, voxelShape1, blockState);
/*     */           VoxelShape voxelShape2 = paramClipContext.getFluidShape(fluidState, this, paramBlockPos);
/*     */           BlockHitResult blockHitResult2 = voxelShape2.clip(vec31, vec32, paramBlockPos);
/*     */           double d1 = (blockHitResult1 == null) ? Double.MAX_VALUE : paramClipContext.getFrom().distanceToSqr(blockHitResult1.getLocation());
/*     */           double d2 = (blockHitResult2 == null) ? Double.MAX_VALUE : paramClipContext.getFrom().distanceToSqr(blockHitResult2.getLocation());
/*     */           return (d1 <= d2) ? blockHitResult1 : blockHitResult2;
/*     */         }paramClipContext -> {
/*     */           Vec3 vec3 = paramClipContext.getFrom().subtract(paramClipContext.getTo());
/*     */           return BlockHitResult.miss(paramClipContext.getTo(), Direction.getApproximateNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing((Position)paramClipContext.getTo()));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default BlockHitResult clipWithInteractionOverride(Vec3 paramVec31, Vec3 paramVec32, BlockPos paramBlockPos, VoxelShape paramVoxelShape, BlockState paramBlockState) {
/*  96 */     BlockHitResult blockHitResult = paramVoxelShape.clip(paramVec31, paramVec32, paramBlockPos);
/*  97 */     if (blockHitResult != null) {
/*     */       
/*  99 */       BlockHitResult blockHitResult1 = paramBlockState.getInteractionShape(this, paramBlockPos).clip(paramVec31, paramVec32, paramBlockPos);
/* 100 */       if (blockHitResult1 != null && blockHitResult1.getLocation().subtract(paramVec31).lengthSqr() < blockHitResult.getLocation().subtract(paramVec31).lengthSqr()) {
/* 101 */         return blockHitResult.withDirection(blockHitResult1.getDirection());
/*     */       }
/*     */     } 
/* 104 */     return blockHitResult;
/*     */   }
/*     */   
/*     */   default double getBlockFloorHeight(VoxelShape paramVoxelShape, Supplier<VoxelShape> paramSupplier) {
/* 108 */     if (!paramVoxelShape.isEmpty()) {
/* 109 */       return paramVoxelShape.max(Direction.Axis.Y);
/*     */     }
/*     */ 
/*     */     
/* 113 */     double d = ((VoxelShape)paramSupplier.get()).max(Direction.Axis.Y);
/* 114 */     if (d >= 1.0D) {
/* 115 */       return d - 1.0D;
/*     */     }
/*     */     
/* 118 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   default double getBlockFloorHeight(BlockPos paramBlockPos) {
/* 122 */     return getBlockFloorHeight(getBlockState(paramBlockPos).getCollisionShape(this, paramBlockPos), () -> {
/*     */           BlockPos blockPos = paramBlockPos.below();
/*     */           return getBlockState(blockPos).getCollisionShape(this, blockPos);
/*     */         });
/*     */   }
/*     */   
/*     */   static <T, C> T traverseBlocks(Vec3 paramVec31, Vec3 paramVec32, C paramC, BiFunction<C, BlockPos, T> paramBiFunction, Function<C, T> paramFunction) {
/* 129 */     if (paramVec31.equals(paramVec32)) {
/* 130 */       return paramFunction.apply(paramC);
/*     */     }
/*     */ 
/*     */     
/* 134 */     double d1 = Mth.lerp(-1.0E-7D, paramVec32.x, paramVec31.x);
/* 135 */     double d2 = Mth.lerp(-1.0E-7D, paramVec32.y, paramVec31.y);
/* 136 */     double d3 = Mth.lerp(-1.0E-7D, paramVec32.z, paramVec31.z);
/*     */     
/* 138 */     double d4 = Mth.lerp(-1.0E-7D, paramVec31.x, paramVec32.x);
/* 139 */     double d5 = Mth.lerp(-1.0E-7D, paramVec31.y, paramVec32.y);
/* 140 */     double d6 = Mth.lerp(-1.0E-7D, paramVec31.z, paramVec32.z);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     int i = Mth.floor(d4);
/* 146 */     int j = Mth.floor(d5);
/* 147 */     int k = Mth.floor(d6);
/*     */     
/* 149 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(i, j, k);
/* 150 */     T t = paramBiFunction.apply(paramC, mutableBlockPos);
/* 151 */     if (t != null) {
/* 152 */       return t;
/*     */     }
/*     */     
/* 155 */     double d7 = d1 - d4;
/* 156 */     double d8 = d2 - d5;
/* 157 */     double d9 = d3 - d6;
/*     */     
/* 159 */     int m = Mth.sign(d7);
/* 160 */     int n = Mth.sign(d8);
/* 161 */     int i1 = Mth.sign(d9);
/*     */     
/* 163 */     double d10 = (m == 0) ? Double.MAX_VALUE : (m / d7);
/* 164 */     double d11 = (n == 0) ? Double.MAX_VALUE : (n / d8);
/* 165 */     double d12 = (i1 == 0) ? Double.MAX_VALUE : (i1 / d9);
/*     */     
/* 167 */     double d13 = d10 * ((m > 0) ? (1.0D - Mth.frac(d4)) : Mth.frac(d4));
/* 168 */     double d14 = d11 * ((n > 0) ? (1.0D - Mth.frac(d5)) : Mth.frac(d5));
/* 169 */     double d15 = d12 * ((i1 > 0) ? (1.0D - Mth.frac(d6)) : Mth.frac(d6));
/*     */     
/* 171 */     while (d13 <= 1.0D || d14 <= 1.0D || d15 <= 1.0D) {
/* 172 */       if (d13 < d14) {
/* 173 */         if (d13 < d15) {
/* 174 */           i += m;
/* 175 */           d13 += d10;
/*     */         } else {
/* 177 */           k += i1;
/* 178 */           d15 += d12;
/*     */         }
/*     */       
/* 181 */       } else if (d14 < d15) {
/* 182 */         j += n;
/* 183 */         d14 += d11;
/*     */       } else {
/* 185 */         k += i1;
/* 186 */         d15 += d12;
/*     */       } 
/*     */ 
/*     */       
/* 190 */       T t1 = paramBiFunction.apply(paramC, mutableBlockPos.set(i, j, k));
/* 191 */       if (t1 != null) {
/* 192 */         return t1;
/*     */       }
/*     */     } 
/*     */     
/* 196 */     return paramFunction.apply(paramC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean forEachBlockIntersectedBetween(Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB, BlockStepVisitor paramBlockStepVisitor) {
/* 212 */     Vec3 vec3 = paramVec32.subtract(paramVec31);
/*     */     
/* 214 */     if (vec3.lengthSqr() < Mth.square(1.0E-5F)) {
/*     */       
/* 216 */       for (BlockPos blockPos : BlockPos.betweenClosed(paramAABB)) {
/* 217 */         if (!paramBlockStepVisitor.visit(blockPos, 0)) {
/* 218 */           return false;
/*     */         }
/*     */       } 
/* 221 */       return true;
/*     */     } 
/*     */     
/* 224 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/*     */     
/* 226 */     for (BlockPos blockPos : BlockPos.betweenCornersInDirection(paramAABB.move(vec3.scale(-1.0D)), vec3)) {
/* 227 */       if (!paramBlockStepVisitor.visit(blockPos, 0)) {
/* 228 */         return false;
/*     */       }
/* 230 */       longOpenHashSet.add(blockPos.asLong());
/*     */     } 
/*     */ 
/*     */     
/* 234 */     int i = addCollisionsAlongTravel((LongSet)longOpenHashSet, vec3, paramAABB, paramBlockStepVisitor);
/* 235 */     if (i < 0)
/*     */     {
/* 237 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 241 */     for (BlockPos blockPos : BlockPos.betweenCornersInDirection(paramAABB, vec3)) {
/* 242 */       if (longOpenHashSet.add(blockPos.asLong()) && !paramBlockStepVisitor.visit(blockPos, i + 1)) {
/* 243 */         return false;
/*     */       }
/*     */     } 
/* 246 */     return true;
/*     */   }
/*     */   
/*     */   private static int addCollisionsAlongTravel(LongSet paramLongSet, Vec3 paramVec3, AABB paramAABB, BlockStepVisitor paramBlockStepVisitor) {
/* 250 */     double d1 = paramAABB.getXsize();
/* 251 */     double d2 = paramAABB.getYsize();
/* 252 */     double d3 = paramAABB.getZsize();
/*     */ 
/*     */ 
/*     */     
/* 256 */     Vec3i vec3i = getFurthestCorner(paramVec3);
/* 257 */     Vec3 vec31 = paramAABB.getCenter();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     Vec3 vec32 = new Vec3(vec31.x() + d1 * 0.5D * vec3i.getX(), vec31.y() + d2 * 0.5D * vec3i.getY(), vec31.z() + d3 * 0.5D * vec3i.getZ());
/*     */ 
/*     */     
/* 265 */     Vec3 vec33 = vec32.subtract(paramVec3);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     int i = Mth.floor(vec33.x);
/* 271 */     int j = Mth.floor(vec33.y);
/* 272 */     int k = Mth.floor(vec33.z);
/*     */     
/* 274 */     int m = Mth.sign(paramVec3.x);
/* 275 */     int n = Mth.sign(paramVec3.y);
/* 276 */     int i1 = Mth.sign(paramVec3.z);
/*     */     
/* 278 */     double d4 = (m == 0) ? Double.MAX_VALUE : (m / paramVec3.x);
/* 279 */     double d5 = (n == 0) ? Double.MAX_VALUE : (n / paramVec3.y);
/* 280 */     double d6 = (i1 == 0) ? Double.MAX_VALUE : (i1 / paramVec3.z);
/*     */     
/* 282 */     double d7 = d4 * ((m > 0) ? (1.0D - Mth.frac(vec33.x)) : Mth.frac(vec33.x));
/* 283 */     double d8 = d5 * ((n > 0) ? (1.0D - Mth.frac(vec33.y)) : Mth.frac(vec33.y));
/* 284 */     double d9 = d6 * ((i1 > 0) ? (1.0D - Mth.frac(vec33.z)) : Mth.frac(vec33.z));
/* 285 */     byte b = 0;
/*     */     
/* 287 */     while (d7 <= 1.0D || d8 <= 1.0D || d9 <= 1.0D) {
/* 288 */       if (d7 < d8) {
/* 289 */         if (d7 < d9) {
/* 290 */           i += m;
/* 291 */           d7 += d4;
/*     */         } else {
/* 293 */           k += i1;
/* 294 */           d9 += d6;
/*     */         }
/*     */       
/* 297 */       } else if (d8 < d9) {
/* 298 */         j += n;
/* 299 */         d8 += d5;
/*     */       } else {
/* 301 */         k += i1;
/* 302 */         d9 += d6;
/*     */       } 
/*     */ 
/*     */       
/* 306 */       Optional<Vec3> optional = AABB.clip(i, j, k, (i + 1), (j + 1), (k + 1), vec33, vec32);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       if (optional.isEmpty()) {
/*     */         continue;
/*     */       }
/*     */       
/* 315 */       b++;
/*     */ 
/*     */       
/* 318 */       Vec3 vec3 = optional.get();
/* 319 */       double d10 = Mth.clamp(vec3.x, i + 9.999999747378752E-6D, i + 1.0D - 9.999999747378752E-6D);
/* 320 */       double d11 = Mth.clamp(vec3.y, j + 9.999999747378752E-6D, j + 1.0D - 9.999999747378752E-6D);
/* 321 */       double d12 = Mth.clamp(vec3.z, k + 9.999999747378752E-6D, k + 1.0D - 9.999999747378752E-6D);
/*     */       
/* 323 */       int i2 = Mth.floor(d10 - d1 * vec3i.getX());
/* 324 */       int i3 = Mth.floor(d11 - d2 * vec3i.getY());
/* 325 */       int i4 = Mth.floor(d12 - d3 * vec3i.getZ());
/*     */       
/* 327 */       byte b1 = b;
/* 328 */       for (BlockPos blockPos : BlockPos.betweenCornersInDirection(i, j, k, i2, i3, i4, paramVec3)) {
/*     */ 
/*     */ 
/*     */         
/* 332 */         if (paramLongSet.add(blockPos.asLong()) && !paramBlockStepVisitor.visit(blockPos, b1)) {
/* 333 */           return -1;
/*     */         }
/*     */       } 
/*     */     } 
/* 337 */     return b;
/*     */   }
/*     */   
/*     */   private static Vec3i getFurthestCorner(Vec3 paramVec3) {
/* 341 */     double d1 = Math.abs(Vec3.X_AXIS.dot(paramVec3));
/* 342 */     double d2 = Math.abs(Vec3.Y_AXIS.dot(paramVec3));
/* 343 */     double d3 = Math.abs(Vec3.Z_AXIS.dot(paramVec3));
/*     */     
/* 345 */     byte b1 = (paramVec3.x >= 0.0D) ? 1 : -1;
/* 346 */     byte b2 = (paramVec3.y >= 0.0D) ? 1 : -1;
/* 347 */     byte b3 = (paramVec3.z >= 0.0D) ? 1 : -1;
/*     */     
/* 349 */     if (d1 <= d2 && d1 <= d3)
/* 350 */       return new Vec3i(-b1, -b3, b2); 
/* 351 */     if (d2 <= d3) {
/* 352 */       return new Vec3i(b3, -b2, -b1);
/*     */     }
/* 354 */     return new Vec3i(-b2, b1, -b3);
/*     */   }
/*     */   
/*     */   BlockEntity getBlockEntity(BlockPos paramBlockPos);
/*     */   
/*     */   BlockState getBlockState(BlockPos paramBlockPos);
/*     */   
/*     */   FluidState getFluidState(BlockPos paramBlockPos);
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BlockStepVisitor {
/*     */     boolean visit(BlockPos param1BlockPos, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\BlockGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */