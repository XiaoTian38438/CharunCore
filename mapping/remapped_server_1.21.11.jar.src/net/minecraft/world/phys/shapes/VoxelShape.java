/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ public abstract class VoxelShape
/*     */ {
/*     */   protected final DiscreteVoxelShape shape;
/*     */   private VoxelShape[] faces;
/*     */   
/*     */   protected VoxelShape(DiscreteVoxelShape paramDiscreteVoxelShape) {
/*  28 */     this.shape = paramDiscreteVoxelShape;
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis paramAxis) {
/*  32 */     int i = this.shape.firstFull(paramAxis);
/*  33 */     if (i >= this.shape.getSize(paramAxis)) {
/*  34 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*  36 */     return get(paramAxis, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public double max(Direction.Axis paramAxis) {
/*  41 */     int i = this.shape.lastFull(paramAxis);
/*  42 */     if (i <= 0) {
/*  43 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/*  45 */     return get(paramAxis, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public AABB bounds() {
/*  50 */     if (isEmpty()) {
/*  51 */       throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("No bounds for empty shape."));
/*     */     }
/*  53 */     return new AABB(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   public VoxelShape singleEncompassing() {
/*  57 */     if (isEmpty()) {
/*  58 */       return Shapes.empty();
/*     */     }
/*  60 */     return Shapes.box(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   protected double get(Direction.Axis paramAxis, int paramInt) {
/*  64 */     return getCoords(paramAxis).getDouble(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  70 */     return this.shape.isEmpty();
/*     */   }
/*     */   
/*     */   public VoxelShape move(Vec3 paramVec3) {
/*  74 */     return move(paramVec3.x, paramVec3.y, paramVec3.z);
/*     */   }
/*     */   
/*     */   public VoxelShape move(Vec3i paramVec3i) {
/*  78 */     return move(paramVec3i.getX(), paramVec3i.getY(), paramVec3i.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape move(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  83 */     if (isEmpty()) {
/*  84 */       return Shapes.empty();
/*     */     }
/*  86 */     return new ArrayVoxelShape(this.shape, (DoubleList)new OffsetDoubleList(
/*     */           
/*  88 */           getCoords(Direction.Axis.X), paramDouble1), (DoubleList)new OffsetDoubleList(
/*  89 */           getCoords(Direction.Axis.Y), paramDouble2), (DoubleList)new OffsetDoubleList(
/*  90 */           getCoords(Direction.Axis.Z), paramDouble3));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape optimize() {
/*  95 */     VoxelShape[] arrayOfVoxelShape = { Shapes.empty() };
/*  96 */     forAllBoxes((paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6) -> paramArrayOfVoxelShape[0] = Shapes.joinUnoptimized(paramArrayOfVoxelShape[0], Shapes.box(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6), BooleanOp.OR));
/*     */ 
/*     */     
/*  99 */     return arrayOfVoxelShape[0];
/*     */   }
/*     */   
/*     */   public void forAllEdges(Shapes.DoubleLineConsumer paramDoubleLineConsumer) {
/* 103 */     this.shape.forAllEdges((paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6) -> paramDoubleLineConsumer.consume(get(Direction.Axis.X, paramInt1), get(Direction.Axis.Y, paramInt2), get(Direction.Axis.Z, paramInt3), get(Direction.Axis.X, paramInt4), get(Direction.Axis.Y, paramInt5), get(Direction.Axis.Z, paramInt6)), true);
/*     */   }
/*     */   
/*     */   public void forAllBoxes(Shapes.DoubleLineConsumer paramDoubleLineConsumer) {
/* 107 */     DoubleList doubleList1 = getCoords(Direction.Axis.X);
/* 108 */     DoubleList doubleList2 = getCoords(Direction.Axis.Y);
/* 109 */     DoubleList doubleList3 = getCoords(Direction.Axis.Z);
/*     */     
/* 111 */     this.shape.forAllBoxes((paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6) -> paramDoubleLineConsumer.consume(paramDoubleList1.getDouble(paramInt1), paramDoubleList2.getDouble(paramInt2), paramDoubleList3.getDouble(paramInt3), paramDoubleList1.getDouble(paramInt4), paramDoubleList2.getDouble(paramInt5), paramDoubleList3.getDouble(paramInt6)), true);
/*     */   }
/*     */   
/*     */   public List<AABB> toAabbs() {
/* 115 */     ArrayList<AABB> arrayList = Lists.newArrayList();
/* 116 */     forAllBoxes((paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6) -> paramList.add(new AABB(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6)));
/* 117 */     return arrayList;
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis paramAxis, double paramDouble1, double paramDouble2) {
/* 121 */     Direction.Axis axis1 = AxisCycle.FORWARD.cycle(paramAxis);
/* 122 */     Direction.Axis axis2 = AxisCycle.BACKWARD.cycle(paramAxis);
/* 123 */     int i = findIndex(axis1, paramDouble1);
/* 124 */     int j = findIndex(axis2, paramDouble2);
/* 125 */     int k = this.shape.firstFull(paramAxis, i, j);
/* 126 */     if (k >= this.shape.getSize(paramAxis)) {
/* 127 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 129 */     return get(paramAxis, k);
/*     */   }
/*     */ 
/*     */   
/*     */   public double max(Direction.Axis paramAxis, double paramDouble1, double paramDouble2) {
/* 134 */     Direction.Axis axis1 = AxisCycle.FORWARD.cycle(paramAxis);
/* 135 */     Direction.Axis axis2 = AxisCycle.BACKWARD.cycle(paramAxis);
/* 136 */     int i = findIndex(axis1, paramDouble1);
/* 137 */     int j = findIndex(axis2, paramDouble2);
/* 138 */     int k = this.shape.lastFull(paramAxis, i, j);
/* 139 */     if (k <= 0) {
/* 140 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/* 142 */     return get(paramAxis, k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findIndex(Direction.Axis paramAxis, double paramDouble) {
/* 151 */     return Mth.binarySearch(0, this.shape.getSize(paramAxis) + 1, paramInt -> (paramDouble < get(paramAxis, paramInt))) - 1;
/*     */   }
/*     */   
/*     */   public BlockHitResult clip(Vec3 paramVec31, Vec3 paramVec32, BlockPos paramBlockPos) {
/* 155 */     if (isEmpty()) {
/* 156 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 160 */     Vec3 vec31 = paramVec32.subtract(paramVec31);
/* 161 */     if (vec31.lengthSqr() < 1.0E-7D) {
/* 162 */       return null;
/*     */     }
/*     */     
/* 165 */     Vec3 vec32 = paramVec31.add(vec31.scale(0.001D));
/*     */ 
/*     */     
/* 168 */     if (this.shape.isFullWide(findIndex(Direction.Axis.X, vec32.x - paramBlockPos.getX()), findIndex(Direction.Axis.Y, vec32.y - paramBlockPos.getY()), findIndex(Direction.Axis.Z, vec32.z - paramBlockPos.getZ()))) {
/* 169 */       return new BlockHitResult(vec32, Direction.getApproximateNearest(vec31.x, vec31.y, vec31.z).getOpposite(), paramBlockPos, true);
/*     */     }
/*     */ 
/*     */     
/* 173 */     return AABB.clip(toAabbs(), paramVec31, paramVec32, paramBlockPos);
/*     */   }
/*     */   
/*     */   public Optional<Vec3> closestPointTo(Vec3 paramVec3) {
/* 177 */     if (isEmpty()) {
/* 178 */       return Optional.empty();
/*     */     }
/* 180 */     MutableObject mutableObject = new MutableObject();
/* 181 */     forAllBoxes((paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6) -> {
/*     */           double d1 = Mth.clamp(paramVec3.x(), paramDouble1, paramDouble4);
/*     */           double d2 = Mth.clamp(paramVec3.y(), paramDouble2, paramDouble5);
/*     */           double d3 = Mth.clamp(paramVec3.z(), paramDouble3, paramDouble6);
/*     */           Vec3 vec3 = (Vec3)paramMutableObject.get();
/*     */           if (vec3 == null || paramVec3.distanceToSqr(d1, d2, d3) < paramVec3.distanceToSqr(vec3)) {
/*     */             paramMutableObject.setValue(new Vec3(d1, d2, d3));
/*     */           }
/*     */         });
/* 190 */     return Optional.of(Objects.<Vec3>requireNonNull((Vec3)mutableObject.get()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getFaceShape(Direction paramDirection) {
/* 197 */     if (isEmpty() || this == Shapes.block()) {
/* 198 */       return this;
/*     */     }
/*     */     
/* 201 */     if (this.faces != null) {
/* 202 */       VoxelShape voxelShape1 = this.faces[paramDirection.ordinal()];
/* 203 */       if (voxelShape1 != null) {
/* 204 */         return voxelShape1;
/*     */       }
/*     */     } else {
/* 207 */       this.faces = new VoxelShape[6];
/*     */     } 
/*     */     
/* 210 */     VoxelShape voxelShape = calculateFace(paramDirection);
/* 211 */     this.faces[paramDirection.ordinal()] = voxelShape;
/* 212 */     return voxelShape;
/*     */   }
/*     */   
/*     */   private VoxelShape calculateFace(Direction paramDirection) {
/* 216 */     Direction.Axis axis = paramDirection.getAxis();
/* 217 */     if (isCubeLikeAlong(axis)) {
/* 218 */       return this;
/*     */     }
/*     */     
/* 221 */     Direction.AxisDirection axisDirection = paramDirection.getAxisDirection();
/* 222 */     int i = findIndex(axis, (axisDirection == Direction.AxisDirection.POSITIVE) ? 0.9999999D : 1.0E-7D);
/* 223 */     SliceShape sliceShape = new SliceShape(this, axis, i);
/*     */ 
/*     */     
/* 226 */     if (sliceShape.isEmpty())
/* 227 */       return Shapes.empty(); 
/* 228 */     if (sliceShape.isCubeLike()) {
/* 229 */       return Shapes.block();
/*     */     }
/*     */     
/* 232 */     return sliceShape;
/*     */   }
/*     */   
/*     */   protected boolean isCubeLike() {
/* 236 */     for (Direction.Axis axis : Direction.Axis.VALUES) {
/* 237 */       if (!isCubeLikeAlong(axis)) {
/* 238 */         return false;
/*     */       }
/*     */     } 
/* 241 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isCubeLikeAlong(Direction.Axis paramAxis) {
/* 245 */     DoubleList doubleList = getCoords(paramAxis);
/* 246 */     return (doubleList.size() == 2 && DoubleMath.fuzzyEquals(doubleList.getDouble(0), 0.0D, 1.0E-7D) && DoubleMath.fuzzyEquals(doubleList.getDouble(1), 1.0D, 1.0E-7D));
/*     */   }
/*     */   
/*     */   public double collide(Direction.Axis paramAxis, AABB paramAABB, double paramDouble) {
/* 250 */     return collideX(AxisCycle.between(paramAxis, Direction.Axis.X), paramAABB, paramDouble);
/*     */   }
/*     */   
/*     */   protected double collideX(AxisCycle paramAxisCycle, AABB paramAABB, double paramDouble) {
/* 254 */     if (isEmpty()) {
/* 255 */       return paramDouble;
/*     */     }
/* 257 */     if (Math.abs(paramDouble) < 1.0E-7D) {
/* 258 */       return 0.0D;
/*     */     }
/*     */     
/* 261 */     AxisCycle axisCycle = paramAxisCycle.inverse();
/* 262 */     Direction.Axis axis1 = axisCycle.cycle(Direction.Axis.X);
/* 263 */     Direction.Axis axis2 = axisCycle.cycle(Direction.Axis.Y);
/* 264 */     Direction.Axis axis3 = axisCycle.cycle(Direction.Axis.Z);
/*     */     
/* 266 */     double d1 = paramAABB.max(axis1);
/* 267 */     double d2 = paramAABB.min(axis1);
/*     */     
/* 269 */     int i = findIndex(axis1, d2 + 1.0E-7D);
/* 270 */     int j = findIndex(axis1, d1 - 1.0E-7D);
/*     */     
/* 272 */     int k = Math.max(0, findIndex(axis2, paramAABB.min(axis2) + 1.0E-7D));
/* 273 */     int m = Math.min(this.shape.getSize(axis2), findIndex(axis2, paramAABB.max(axis2) - 1.0E-7D) + 1);
/*     */     
/* 275 */     int n = Math.max(0, findIndex(axis3, paramAABB.min(axis3) + 1.0E-7D));
/* 276 */     int i1 = Math.min(this.shape.getSize(axis3), findIndex(axis3, paramAABB.max(axis3) - 1.0E-7D) + 1);
/*     */     
/* 278 */     int i2 = this.shape.getSize(axis1);
/*     */     
/* 280 */     if (paramDouble > 0.0D) {
/* 281 */       for (int i3 = j + 1; i3 < i2; i3++) {
/* 282 */         for (int i4 = k; i4 < m; i4++) {
/* 283 */           for (int i5 = n; i5 < i1; i5++) {
/* 284 */             if (this.shape.isFullWide(axisCycle, i3, i4, i5)) {
/* 285 */               double d = get(axis1, i3) - d1;
/* 286 */               if (d >= -1.0E-7D) {
/* 287 */                 paramDouble = Math.min(paramDouble, d);
/*     */               }
/* 289 */               return paramDouble;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 294 */     } else if (paramDouble < 0.0D) {
/* 295 */       for (int i3 = i - 1; i3 >= 0; i3--) {
/* 296 */         for (int i4 = k; i4 < m; i4++) {
/* 297 */           for (int i5 = n; i5 < i1; i5++) {
/* 298 */             if (this.shape.isFullWide(axisCycle, i3, i4, i5)) {
/* 299 */               double d = get(axis1, i3 + 1) - d2;
/* 300 */               if (d <= 1.0E-7D) {
/* 301 */                 paramDouble = Math.max(paramDouble, d);
/*     */               }
/* 303 */               return paramDouble;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 309 */     return paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 316 */     return super.equals(paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 321 */     return isEmpty() ? "EMPTY" : ("VoxelShape[" + String.valueOf(bounds()) + "]");
/*     */   }
/*     */   
/*     */   public abstract DoubleList getCoords(Direction.Axis paramAxis);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\VoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */