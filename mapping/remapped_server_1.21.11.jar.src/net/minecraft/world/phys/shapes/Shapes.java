/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Shapes
/*     */ {
/*     */   public static final double EPSILON = 1.0E-7D;
/*     */   
/*     */   static {
/*  25 */     BLOCK = (VoxelShape)Util.make(() -> {
/*     */           BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(1, 1, 1);
/*     */           bitSetDiscreteVoxelShape.fill(0, 0, 0);
/*     */           return new CubeVoxelShape(bitSetDiscreteVoxelShape);
/*     */         });
/*     */   }
/*  31 */   public static final double BIG_EPSILON = 1.0E-6D; private static final VoxelShape BLOCK; private static final Vec3 BLOCK_CENTER = new Vec3(0.5D, 0.5D, 0.5D);
/*     */   
/*  33 */   public static final VoxelShape INFINITY = box(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final VoxelShape EMPTY = new ArrayVoxelShape(new BitSetDiscreteVoxelShape(0, 0, 0), (DoubleList)new DoubleArrayList(new double[] { 0.0D }, ), (DoubleList)new DoubleArrayList(new double[] { 0.0D }, ), (DoubleList)new DoubleArrayList(new double[] { 0.0D }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoxelShape empty() {
/*  46 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static VoxelShape block() {
/*  50 */     return BLOCK;
/*     */   }
/*     */   
/*     */   public static VoxelShape box(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/*  54 */     if (paramDouble1 > paramDouble4 || paramDouble2 > paramDouble5 || paramDouble3 > paramDouble6) {
/*  55 */       throw new IllegalArgumentException("The min values need to be smaller or equals to the max values");
/*     */     }
/*  57 */     return create(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6);
/*     */   }
/*     */   
/*     */   public static VoxelShape create(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/*  61 */     if (paramDouble4 - paramDouble1 < 1.0E-7D || paramDouble5 - paramDouble2 < 1.0E-7D || paramDouble6 - paramDouble3 < 1.0E-7D) {
/*  62 */       return empty();
/*     */     }
/*     */     
/*  65 */     int i = findBits(paramDouble1, paramDouble4);
/*  66 */     int j = findBits(paramDouble2, paramDouble5);
/*  67 */     int k = findBits(paramDouble3, paramDouble6);
/*     */     
/*  69 */     if (i < 0 || j < 0 || k < 0) {
/*  70 */       return new ArrayVoxelShape(BLOCK.shape, 
/*     */           
/*  72 */           (DoubleList)DoubleArrayList.wrap(new double[] { paramDouble1, paramDouble4
/*  73 */             }, ), (DoubleList)DoubleArrayList.wrap(new double[] { paramDouble2, paramDouble5
/*  74 */             }, ), (DoubleList)DoubleArrayList.wrap(new double[] { paramDouble3, paramDouble6 }));
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (i == 0 && j == 0 && k == 0) {
/*  79 */       return block();
/*     */     }
/*     */     
/*  82 */     int m = 1 << i;
/*  83 */     int n = 1 << j;
/*  84 */     int i1 = 1 << k;
/*     */     
/*  86 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = BitSetDiscreteVoxelShape.withFilledBounds(m, n, i1, 
/*     */ 
/*     */ 
/*     */         
/*  90 */         (int)Math.round(paramDouble1 * m), 
/*  91 */         (int)Math.round(paramDouble2 * n), 
/*  92 */         (int)Math.round(paramDouble3 * i1), 
/*  93 */         (int)Math.round(paramDouble4 * m), 
/*  94 */         (int)Math.round(paramDouble5 * n), 
/*  95 */         (int)Math.round(paramDouble6 * i1));
/*     */     
/*  97 */     return new CubeVoxelShape(bitSetDiscreteVoxelShape);
/*     */   }
/*     */   
/*     */   public static VoxelShape create(AABB paramAABB) {
/* 101 */     return create(paramAABB.minX, paramAABB.minY, paramAABB.minZ, paramAABB.maxX, paramAABB.maxY, paramAABB.maxZ);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static int findBits(double paramDouble1, double paramDouble2) {
/* 106 */     if (paramDouble1 < -1.0E-7D || paramDouble2 > 1.0000001D) {
/* 107 */       return -1;
/*     */     }
/* 109 */     for (byte b = 0; b <= 3; b++) {
/* 110 */       int i = 1 << b;
/* 111 */       double d1 = paramDouble1 * i;
/* 112 */       double d2 = paramDouble2 * i;
/* 113 */       boolean bool1 = (Math.abs(d1 - Math.round(d1)) < 1.0E-7D * i) ? true : false;
/* 114 */       boolean bool2 = (Math.abs(d2 - Math.round(d2)) < 1.0E-7D * i) ? true : false;
/* 115 */       if (bool1 && bool2) {
/* 116 */         return b;
/*     */       }
/*     */     } 
/* 119 */     return -1;
/*     */   }
/*     */   
/*     */   protected static long lcm(int paramInt1, int paramInt2) {
/* 123 */     return paramInt1 * (paramInt2 / IntMath.gcd(paramInt1, paramInt2));
/*     */   }
/*     */   
/*     */   public static VoxelShape or(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2) {
/* 127 */     return join(paramVoxelShape1, paramVoxelShape2, BooleanOp.OR);
/*     */   }
/*     */   
/*     */   public static VoxelShape or(VoxelShape paramVoxelShape, VoxelShape... paramVarArgs) {
/* 131 */     return Arrays.<VoxelShape>stream(paramVarArgs).reduce(paramVoxelShape, Shapes::or);
/*     */   }
/*     */   
/*     */   public static VoxelShape join(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2, BooleanOp paramBooleanOp) {
/* 135 */     return joinUnoptimized(paramVoxelShape1, paramVoxelShape2, paramBooleanOp).optimize();
/*     */   }
/*     */   
/*     */   public static VoxelShape joinUnoptimized(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2, BooleanOp paramBooleanOp) {
/* 139 */     if (paramBooleanOp.apply(false, false)) {
/* 140 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*     */     }
/* 142 */     if (paramVoxelShape1 == paramVoxelShape2) {
/* 143 */       return paramBooleanOp.apply(true, true) ? paramVoxelShape1 : empty();
/*     */     }
/* 145 */     boolean bool1 = paramBooleanOp.apply(true, false);
/* 146 */     boolean bool2 = paramBooleanOp.apply(false, true);
/*     */     
/* 148 */     if (paramVoxelShape1.isEmpty()) {
/* 149 */       return bool2 ? paramVoxelShape2 : empty();
/*     */     }
/* 151 */     if (paramVoxelShape2.isEmpty()) {
/* 152 */       return bool1 ? paramVoxelShape1 : empty();
/*     */     }
/*     */     
/* 155 */     IndexMerger indexMerger1 = createIndexMerger(1, paramVoxelShape1.getCoords(Direction.Axis.X), paramVoxelShape2.getCoords(Direction.Axis.X), bool1, bool2);
/* 156 */     IndexMerger indexMerger2 = createIndexMerger(indexMerger1.size() - 1, paramVoxelShape1.getCoords(Direction.Axis.Y), paramVoxelShape2.getCoords(Direction.Axis.Y), bool1, bool2);
/* 157 */     IndexMerger indexMerger3 = createIndexMerger((indexMerger1.size() - 1) * (indexMerger2.size() - 1), paramVoxelShape1.getCoords(Direction.Axis.Z), paramVoxelShape2.getCoords(Direction.Axis.Z), bool1, bool2);
/*     */     
/* 159 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = BitSetDiscreteVoxelShape.join(paramVoxelShape1.shape, paramVoxelShape2.shape, indexMerger1, indexMerger2, indexMerger3, paramBooleanOp);
/* 160 */     if (indexMerger1 instanceof DiscreteCubeMerger && indexMerger2 instanceof DiscreteCubeMerger && indexMerger3 instanceof DiscreteCubeMerger) {
/* 161 */       return new CubeVoxelShape(bitSetDiscreteVoxelShape);
/*     */     }
/* 163 */     return new ArrayVoxelShape(bitSetDiscreteVoxelShape, indexMerger1.getList(), indexMerger2.getList(), indexMerger3.getList());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean joinIsNotEmpty(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2, BooleanOp paramBooleanOp) {
/* 168 */     if (paramBooleanOp.apply(false, false)) {
/* 169 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*     */     }
/*     */     
/* 172 */     boolean bool1 = paramVoxelShape1.isEmpty();
/* 173 */     boolean bool2 = paramVoxelShape2.isEmpty();
/* 174 */     if (bool1 || bool2) {
/* 175 */       return paramBooleanOp.apply(!bool1, !bool2);
/*     */     }
/* 177 */     if (paramVoxelShape1 == paramVoxelShape2) {
/* 178 */       return paramBooleanOp.apply(true, true);
/*     */     }
/*     */     
/* 181 */     boolean bool3 = paramBooleanOp.apply(true, false);
/* 182 */     boolean bool4 = paramBooleanOp.apply(false, true);
/* 183 */     for (Direction.Axis axis : AxisCycle.AXIS_VALUES) {
/* 184 */       if (paramVoxelShape1.max(axis) < paramVoxelShape2.min(axis) - 1.0E-7D) {
/* 185 */         return (bool3 || bool4);
/*     */       }
/* 187 */       if (paramVoxelShape2.max(axis) < paramVoxelShape1.min(axis) - 1.0E-7D) {
/* 188 */         return (bool3 || bool4);
/*     */       }
/*     */     } 
/*     */     
/* 192 */     IndexMerger indexMerger1 = createIndexMerger(1, paramVoxelShape1.getCoords(Direction.Axis.X), paramVoxelShape2.getCoords(Direction.Axis.X), bool3, bool4);
/* 193 */     IndexMerger indexMerger2 = createIndexMerger(indexMerger1.size() - 1, paramVoxelShape1.getCoords(Direction.Axis.Y), paramVoxelShape2.getCoords(Direction.Axis.Y), bool3, bool4);
/* 194 */     IndexMerger indexMerger3 = createIndexMerger((indexMerger1.size() - 1) * (indexMerger2.size() - 1), paramVoxelShape1.getCoords(Direction.Axis.Z), paramVoxelShape2.getCoords(Direction.Axis.Z), bool3, bool4);
/* 195 */     return joinIsNotEmpty(indexMerger1, indexMerger2, indexMerger3, paramVoxelShape1.shape, paramVoxelShape2.shape, paramBooleanOp);
/*     */   }
/*     */   
/*     */   private static boolean joinIsNotEmpty(IndexMerger paramIndexMerger1, IndexMerger paramIndexMerger2, IndexMerger paramIndexMerger3, DiscreteVoxelShape paramDiscreteVoxelShape1, DiscreteVoxelShape paramDiscreteVoxelShape2, BooleanOp paramBooleanOp) {
/* 199 */     return !paramIndexMerger1.forMergedIndexes((paramInt1, paramInt2, paramInt3) -> paramIndexMerger1.forMergedIndexes(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double collide(Direction.Axis paramAxis, AABB paramAABB, Iterable<VoxelShape> paramIterable, double paramDouble) {
/* 209 */     for (VoxelShape voxelShape : paramIterable) {
/* 210 */       if (Math.abs(paramDouble) < 1.0E-7D) {
/* 211 */         return 0.0D;
/*     */       }
/* 213 */       paramDouble = voxelShape.collide(paramAxis, paramAABB, paramDouble);
/*     */     } 
/* 215 */     return paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean blockOccludes(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2, Direction paramDirection) {
/* 222 */     if (paramVoxelShape1 == block() && paramVoxelShape2 == block()) {
/* 223 */       return true;
/*     */     }
/* 225 */     if (paramVoxelShape2.isEmpty()) {
/* 226 */       return false;
/*     */     }
/* 228 */     Direction.Axis axis = paramDirection.getAxis();
/* 229 */     Direction.AxisDirection axisDirection = paramDirection.getAxisDirection();
/*     */     
/* 231 */     VoxelShape voxelShape1 = (axisDirection == Direction.AxisDirection.POSITIVE) ? paramVoxelShape1 : paramVoxelShape2;
/* 232 */     VoxelShape voxelShape2 = (axisDirection == Direction.AxisDirection.POSITIVE) ? paramVoxelShape2 : paramVoxelShape1;
/* 233 */     BooleanOp booleanOp = (axisDirection == Direction.AxisDirection.POSITIVE) ? BooleanOp.ONLY_FIRST : BooleanOp.ONLY_SECOND;
/*     */     
/* 235 */     return (DoubleMath.fuzzyEquals(voxelShape1.max(axis), 1.0D, 1.0E-7D) && 
/* 236 */       DoubleMath.fuzzyEquals(voxelShape2.min(axis), 0.0D, 1.0E-7D) && 
/* 237 */       !joinIsNotEmpty(new SliceShape(voxelShape1, axis, voxelShape1.shape.getSize(axis) - 1), new SliceShape(voxelShape2, axis, 0), booleanOp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mergedFaceOccludes(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2, Direction paramDirection) {
/* 244 */     if (paramVoxelShape1 == block() || paramVoxelShape2 == block()) {
/* 245 */       return true;
/*     */     }
/*     */     
/* 248 */     Direction.Axis axis = paramDirection.getAxis();
/* 249 */     Direction.AxisDirection axisDirection = paramDirection.getAxisDirection();
/*     */     
/* 251 */     VoxelShape voxelShape1 = (axisDirection == Direction.AxisDirection.POSITIVE) ? paramVoxelShape1 : paramVoxelShape2;
/* 252 */     VoxelShape voxelShape2 = (axisDirection == Direction.AxisDirection.POSITIVE) ? paramVoxelShape2 : paramVoxelShape1;
/*     */     
/* 254 */     if (!DoubleMath.fuzzyEquals(voxelShape1.max(axis), 1.0D, 1.0E-7D)) {
/* 255 */       voxelShape1 = empty();
/*     */     }
/* 257 */     if (!DoubleMath.fuzzyEquals(voxelShape2.min(axis), 0.0D, 1.0E-7D)) {
/* 258 */       voxelShape2 = empty();
/*     */     }
/*     */     
/* 261 */     return !joinIsNotEmpty(block(), joinUnoptimized(new SliceShape(voxelShape1, axis, voxelShape1.shape.getSize(axis) - 1), new SliceShape(voxelShape2, axis, 0), BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean faceShapeOccludes(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2) {
/* 268 */     if (paramVoxelShape1 == block() || paramVoxelShape2 == block()) {
/* 269 */       return true;
/*     */     }
/*     */     
/* 272 */     if (paramVoxelShape1.isEmpty() && paramVoxelShape2.isEmpty()) {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     return !joinIsNotEmpty(
/* 277 */         block(), 
/* 278 */         joinUnoptimized(paramVoxelShape1, paramVoxelShape2, BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static IndexMerger createIndexMerger(int paramInt, DoubleList paramDoubleList1, DoubleList paramDoubleList2, boolean paramBoolean1, boolean paramBoolean2) {
/* 289 */     int i = paramDoubleList1.size() - 1;
/* 290 */     int j = paramDoubleList2.size() - 1;
/* 291 */     if (paramDoubleList1 instanceof CubePointRange && paramDoubleList2 instanceof CubePointRange) {
/* 292 */       long l = lcm(i, j);
/* 293 */       if (paramInt * l <= 256L) {
/* 294 */         return new DiscreteCubeMerger(i, j);
/*     */       }
/*     */     } 
/*     */     
/* 298 */     if (paramDoubleList1.getDouble(i) < paramDoubleList2.getDouble(0) - 1.0E-7D)
/* 299 */       return new NonOverlappingMerger(paramDoubleList1, paramDoubleList2, false); 
/* 300 */     if (paramDoubleList2.getDouble(j) < paramDoubleList1.getDouble(0) - 1.0E-7D) {
/* 301 */       return new NonOverlappingMerger(paramDoubleList2, paramDoubleList1, true);
/*     */     }
/*     */     
/* 304 */     if (i == j && Objects.equals(paramDoubleList1, paramDoubleList2)) {
/* 305 */       return new IdenticalMerger(paramDoubleList1);
/*     */     }
/*     */     
/* 308 */     return new IndirectMerger(paramDoubleList1, paramDoubleList2, paramBoolean1, paramBoolean2);
/*     */   }
/*     */   
/*     */   public static VoxelShape rotate(VoxelShape paramVoxelShape, OctahedralGroup paramOctahedralGroup) {
/* 312 */     return rotate(paramVoxelShape, paramOctahedralGroup, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static VoxelShape rotate(VoxelShape paramVoxelShape, OctahedralGroup paramOctahedralGroup, Vec3 paramVec3) {
/* 316 */     if (paramOctahedralGroup == OctahedralGroup.IDENTITY) {
/* 317 */       return paramVoxelShape;
/*     */     }
/*     */     
/* 320 */     DiscreteVoxelShape discreteVoxelShape = paramVoxelShape.shape.rotate(paramOctahedralGroup);
/* 321 */     if (paramVoxelShape instanceof CubeVoxelShape && BLOCK_CENTER.equals(paramVec3)) {
/* 322 */       return new CubeVoxelShape(discreteVoxelShape);
/*     */     }
/*     */     
/* 325 */     Direction.Axis axis1 = paramOctahedralGroup.permutation().permuteAxis(Direction.Axis.X);
/* 326 */     Direction.Axis axis2 = paramOctahedralGroup.permutation().permuteAxis(Direction.Axis.Y);
/* 327 */     Direction.Axis axis3 = paramOctahedralGroup.permutation().permuteAxis(Direction.Axis.Z);
/*     */     
/* 329 */     DoubleList doubleList1 = paramVoxelShape.getCoords(axis1);
/* 330 */     DoubleList doubleList2 = paramVoxelShape.getCoords(axis2);
/* 331 */     DoubleList doubleList3 = paramVoxelShape.getCoords(axis3);
/*     */     
/* 333 */     boolean bool1 = paramOctahedralGroup.inverts(Direction.Axis.X);
/* 334 */     boolean bool2 = paramOctahedralGroup.inverts(Direction.Axis.Y);
/* 335 */     boolean bool3 = paramOctahedralGroup.inverts(Direction.Axis.Z);
/*     */     
/* 337 */     return new ArrayVoxelShape(discreteVoxelShape, 
/*     */         
/* 339 */         flipAxisIfNeeded(doubleList1, bool1, paramVec3.get(axis1), paramVec3.x), 
/* 340 */         flipAxisIfNeeded(doubleList2, bool2, paramVec3.get(axis2), paramVec3.y), 
/* 341 */         flipAxisIfNeeded(doubleList3, bool3, paramVec3.get(axis3), paramVec3.z));
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static DoubleList flipAxisIfNeeded(DoubleList paramDoubleList, boolean paramBoolean, double paramDouble1, double paramDouble2) {
/* 347 */     if (!paramBoolean && paramDouble1 == paramDouble2) {
/* 348 */       return paramDoubleList;
/*     */     }
/* 350 */     int i = paramDoubleList.size();
/* 351 */     DoubleArrayList doubleArrayList = new DoubleArrayList(i);
/*     */     
/* 353 */     if (paramBoolean) {
/* 354 */       for (int j = i - 1; j >= 0; j--) {
/* 355 */         doubleArrayList.add(-(paramDoubleList.getDouble(j) - paramDouble1) + paramDouble2);
/*     */       }
/*     */     } else {
/* 358 */       for (byte b = 0; b && b < i; b++) {
/* 359 */         doubleArrayList.add(paramDoubleList.getDouble(b) - paramDouble1 + paramDouble2);
/*     */       }
/*     */     } 
/* 362 */     return (DoubleList)doubleArrayList;
/*     */   }
/*     */   
/*     */   public static boolean equal(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2) {
/* 366 */     return !joinIsNotEmpty(paramVoxelShape1, paramVoxelShape2, BooleanOp.NOT_SAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(VoxelShape paramVoxelShape) {
/* 375 */     return rotateHorizontalAxis(paramVoxelShape, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(VoxelShape paramVoxelShape, Vec3 paramVec3) {
/* 379 */     return Maps.newEnumMap(Map.of(Direction.Axis.Z, paramVoxelShape, Direction.Axis.X, 
/*     */           
/* 381 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_90, paramVec3)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateAllAxis(VoxelShape paramVoxelShape) {
/* 386 */     return rotateAllAxis(paramVoxelShape, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateAllAxis(VoxelShape paramVoxelShape, Vec3 paramVec3) {
/* 390 */     return Maps.newEnumMap(Map.of(Direction.Axis.Z, paramVoxelShape, Direction.Axis.X, 
/*     */           
/* 392 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_90, paramVec3), Direction.Axis.Y, 
/* 393 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_X_90, paramVec3)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape paramVoxelShape) {
/* 398 */     return rotateHorizontal(paramVoxelShape, OctahedralGroup.IDENTITY, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape paramVoxelShape, OctahedralGroup paramOctahedralGroup) {
/* 402 */     return rotateHorizontal(paramVoxelShape, paramOctahedralGroup, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape paramVoxelShape, OctahedralGroup paramOctahedralGroup, Vec3 paramVec3) {
/* 406 */     return Maps.newEnumMap(Map.of(Direction.NORTH, 
/* 407 */           rotate(paramVoxelShape, paramOctahedralGroup), Direction.EAST, 
/* 408 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_90.compose(paramOctahedralGroup), paramVec3), Direction.SOUTH, 
/* 409 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_180.compose(paramOctahedralGroup), paramVec3), Direction.WEST, 
/* 410 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_270.compose(paramOctahedralGroup), paramVec3)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape paramVoxelShape) {
/* 415 */     return rotateAll(paramVoxelShape, OctahedralGroup.IDENTITY, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape paramVoxelShape, Vec3 paramVec3) {
/* 419 */     return rotateAll(paramVoxelShape, OctahedralGroup.IDENTITY, paramVec3);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape paramVoxelShape, OctahedralGroup paramOctahedralGroup, Vec3 paramVec3) {
/* 423 */     return Maps.newEnumMap(Map.of(Direction.NORTH, 
/* 424 */           rotate(paramVoxelShape, paramOctahedralGroup), Direction.EAST, 
/* 425 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_90.compose(paramOctahedralGroup), paramVec3), Direction.SOUTH, 
/* 426 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_180.compose(paramOctahedralGroup), paramVec3), Direction.WEST, 
/* 427 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_270.compose(paramOctahedralGroup), paramVec3), Direction.UP, 
/* 428 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_X_270.compose(paramOctahedralGroup), paramVec3), Direction.DOWN, 
/* 429 */           rotate(paramVoxelShape, OctahedralGroup.BLOCK_ROT_X_90.compose(paramOctahedralGroup), paramVec3)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape paramVoxelShape) {
/* 434 */     return rotateAttachFace(paramVoxelShape, OctahedralGroup.IDENTITY);
/*     */   }
/*     */   
/*     */   public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape paramVoxelShape, OctahedralGroup paramOctahedralGroup) {
/* 438 */     return Map.of(AttachFace.WALL, 
/* 439 */         rotateHorizontal(paramVoxelShape, paramOctahedralGroup), AttachFace.FLOOR, 
/* 440 */         rotateHorizontal(paramVoxelShape, OctahedralGroup.BLOCK_ROT_X_270.compose(paramOctahedralGroup)), AttachFace.CEILING, 
/*     */         
/* 442 */         rotateHorizontal(paramVoxelShape, OctahedralGroup.BLOCK_ROT_Y_180.compose(OctahedralGroup.BLOCK_ROT_X_90).compose(paramOctahedralGroup)));
/*     */   }
/*     */   
/*     */   public static interface DoubleLineConsumer {
/*     */     void consume(double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\Shapes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */