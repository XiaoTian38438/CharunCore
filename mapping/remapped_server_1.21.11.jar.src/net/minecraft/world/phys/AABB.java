/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AABB
/*     */ {
/*     */   private static final double EPSILON = 1.0E-7D;
/*     */   public final double minX;
/*     */   public final double minY;
/*     */   public final double minZ;
/*     */   public final double maxX;
/*     */   public final double maxY;
/*     */   public final double maxZ;
/*     */   
/*     */   public AABB(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/*  30 */     this.minX = Math.min(paramDouble1, paramDouble4);
/*  31 */     this.minY = Math.min(paramDouble2, paramDouble5);
/*  32 */     this.minZ = Math.min(paramDouble3, paramDouble6);
/*  33 */     this.maxX = Math.max(paramDouble1, paramDouble4);
/*  34 */     this.maxY = Math.max(paramDouble2, paramDouble5);
/*  35 */     this.maxZ = Math.max(paramDouble3, paramDouble6);
/*     */   }
/*     */   
/*     */   public AABB(BlockPos paramBlockPos) {
/*  39 */     this(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), (paramBlockPos.getX() + 1), (paramBlockPos.getY() + 1), (paramBlockPos.getZ() + 1));
/*     */   }
/*     */   
/*     */   public AABB(Vec3 paramVec31, Vec3 paramVec32) {
/*  43 */     this(paramVec31.x, paramVec31.y, paramVec31.z, paramVec32.x, paramVec32.y, paramVec32.z);
/*     */   }
/*     */   
/*     */   public static AABB of(BoundingBox paramBoundingBox) {
/*  47 */     return new AABB(paramBoundingBox.minX(), paramBoundingBox.minY(), paramBoundingBox.minZ(), (paramBoundingBox.maxX() + 1), (paramBoundingBox.maxY() + 1), (paramBoundingBox.maxZ() + 1));
/*     */   }
/*     */   
/*     */   public static AABB unitCubeFromLowerCorner(Vec3 paramVec3) {
/*  51 */     return new AABB(paramVec3.x, paramVec3.y, paramVec3.z, paramVec3.x + 1.0D, paramVec3.y + 1.0D, paramVec3.z + 1.0D);
/*     */   }
/*     */   
/*     */   public static AABB encapsulatingFullBlocks(BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/*  55 */     return new AABB(Math.min(paramBlockPos1.getX(), paramBlockPos2.getX()), Math.min(paramBlockPos1.getY(), paramBlockPos2.getY()), Math.min(paramBlockPos1.getZ(), paramBlockPos2.getZ()), (Math.max(paramBlockPos1.getX(), paramBlockPos2.getX()) + 1), (Math.max(paramBlockPos1.getY(), paramBlockPos2.getY()) + 1), (Math.max(paramBlockPos1.getZ(), paramBlockPos2.getZ()) + 1));
/*     */   }
/*     */   
/*     */   public AABB setMinX(double paramDouble) {
/*  59 */     return new AABB(paramDouble, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMinY(double paramDouble) {
/*  63 */     return new AABB(this.minX, paramDouble, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMinZ(double paramDouble) {
/*  67 */     return new AABB(this.minX, this.minY, paramDouble, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMaxX(double paramDouble) {
/*  71 */     return new AABB(this.minX, this.minY, this.minZ, paramDouble, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMaxY(double paramDouble) {
/*  75 */     return new AABB(this.minX, this.minY, this.minZ, this.maxX, paramDouble, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMaxZ(double paramDouble) {
/*  79 */     return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, paramDouble);
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis paramAxis) {
/*  83 */     return paramAxis.choose(this.minX, this.minY, this.minZ);
/*     */   }
/*     */   
/*     */   public double max(Direction.Axis paramAxis) {
/*  87 */     return paramAxis.choose(this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  92 */     if (this == paramObject) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (!(paramObject instanceof AABB)) {
/*  96 */       return false;
/*     */     }
/*     */     
/*  99 */     AABB aABB = (AABB)paramObject;
/*     */     
/* 101 */     if (Double.compare(aABB.minX, this.minX) != 0) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (Double.compare(aABB.minY, this.minY) != 0) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (Double.compare(aABB.minZ, this.minZ) != 0) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (Double.compare(aABB.maxX, this.maxX) != 0) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (Double.compare(aABB.maxY, this.maxY) != 0) {
/* 114 */       return false;
/*     */     }
/* 116 */     return (Double.compare(aABB.maxZ, this.maxZ) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 121 */     long l = Double.doubleToLongBits(this.minX);
/* 122 */     int i = (int)(l ^ l >>> 32L);
/* 123 */     l = Double.doubleToLongBits(this.minY);
/* 124 */     i = 31 * i + (int)(l ^ l >>> 32L);
/* 125 */     l = Double.doubleToLongBits(this.minZ);
/* 126 */     i = 31 * i + (int)(l ^ l >>> 32L);
/* 127 */     l = Double.doubleToLongBits(this.maxX);
/* 128 */     i = 31 * i + (int)(l ^ l >>> 32L);
/* 129 */     l = Double.doubleToLongBits(this.maxY);
/* 130 */     i = 31 * i + (int)(l ^ l >>> 32L);
/* 131 */     l = Double.doubleToLongBits(this.maxZ);
/* 132 */     i = 31 * i + (int)(l ^ l >>> 32L);
/* 133 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB contract(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 143 */     double d1 = this.minX;
/* 144 */     double d2 = this.minY;
/* 145 */     double d3 = this.minZ;
/* 146 */     double d4 = this.maxX;
/* 147 */     double d5 = this.maxY;
/* 148 */     double d6 = this.maxZ;
/*     */     
/* 150 */     if (paramDouble1 < 0.0D) {
/* 151 */       d1 -= paramDouble1;
/* 152 */     } else if (paramDouble1 > 0.0D) {
/* 153 */       d4 -= paramDouble1;
/*     */     } 
/*     */     
/* 156 */     if (paramDouble2 < 0.0D) {
/* 157 */       d2 -= paramDouble2;
/* 158 */     } else if (paramDouble2 > 0.0D) {
/* 159 */       d5 -= paramDouble2;
/*     */     } 
/*     */     
/* 162 */     if (paramDouble3 < 0.0D) {
/* 163 */       d3 -= paramDouble3;
/* 164 */     } else if (paramDouble3 > 0.0D) {
/* 165 */       d6 -= paramDouble3;
/*     */     } 
/*     */     
/* 168 */     return new AABB(d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */   
/*     */   public AABB expandTowards(Vec3 paramVec3) {
/* 172 */     return expandTowards(paramVec3.x, paramVec3.y, paramVec3.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB expandTowards(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 182 */     double d1 = this.minX;
/* 183 */     double d2 = this.minY;
/* 184 */     double d3 = this.minZ;
/* 185 */     double d4 = this.maxX;
/* 186 */     double d5 = this.maxY;
/* 187 */     double d6 = this.maxZ;
/*     */     
/* 189 */     if (paramDouble1 < 0.0D) {
/* 190 */       d1 += paramDouble1;
/* 191 */     } else if (paramDouble1 > 0.0D) {
/* 192 */       d4 += paramDouble1;
/*     */     } 
/*     */     
/* 195 */     if (paramDouble2 < 0.0D) {
/* 196 */       d2 += paramDouble2;
/* 197 */     } else if (paramDouble2 > 0.0D) {
/* 198 */       d5 += paramDouble2;
/*     */     } 
/*     */     
/* 201 */     if (paramDouble3 < 0.0D) {
/* 202 */       d3 += paramDouble3;
/* 203 */     } else if (paramDouble3 > 0.0D) {
/* 204 */       d6 += paramDouble3;
/*     */     } 
/*     */     
/* 207 */     return new AABB(d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB inflate(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 217 */     double d1 = this.minX - paramDouble1;
/* 218 */     double d2 = this.minY - paramDouble2;
/* 219 */     double d3 = this.minZ - paramDouble3;
/* 220 */     double d4 = this.maxX + paramDouble1;
/* 221 */     double d5 = this.maxY + paramDouble2;
/* 222 */     double d6 = this.maxZ + paramDouble3;
/*     */     
/* 224 */     return new AABB(d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */   
/*     */   public AABB inflate(double paramDouble) {
/* 228 */     return inflate(paramDouble, paramDouble, paramDouble);
/*     */   }
/*     */   
/*     */   public AABB intersect(AABB paramAABB) {
/* 232 */     double d1 = Math.max(this.minX, paramAABB.minX);
/* 233 */     double d2 = Math.max(this.minY, paramAABB.minY);
/* 234 */     double d3 = Math.max(this.minZ, paramAABB.minZ);
/* 235 */     double d4 = Math.min(this.maxX, paramAABB.maxX);
/* 236 */     double d5 = Math.min(this.maxY, paramAABB.maxY);
/* 237 */     double d6 = Math.min(this.maxZ, paramAABB.maxZ);
/*     */     
/* 239 */     return new AABB(d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */   
/*     */   public AABB minmax(AABB paramAABB) {
/* 243 */     double d1 = Math.min(this.minX, paramAABB.minX);
/* 244 */     double d2 = Math.min(this.minY, paramAABB.minY);
/* 245 */     double d3 = Math.min(this.minZ, paramAABB.minZ);
/* 246 */     double d4 = Math.max(this.maxX, paramAABB.maxX);
/* 247 */     double d5 = Math.max(this.maxY, paramAABB.maxY);
/* 248 */     double d6 = Math.max(this.maxZ, paramAABB.maxZ);
/*     */     
/* 250 */     return new AABB(d1, d2, d3, d4, d5, d6);
/*     */   }
/*     */   
/*     */   public AABB move(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 254 */     return new AABB(this.minX + paramDouble1, this.minY + paramDouble2, this.minZ + paramDouble3, this.maxX + paramDouble1, this.maxY + paramDouble2, this.maxZ + paramDouble3);
/*     */   }
/*     */   
/*     */   public AABB move(BlockPos paramBlockPos) {
/* 258 */     return new AABB(this.minX + paramBlockPos.getX(), this.minY + paramBlockPos.getY(), this.minZ + paramBlockPos.getZ(), this.maxX + paramBlockPos.getX(), this.maxY + paramBlockPos.getY(), this.maxZ + paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   public AABB move(Vec3 paramVec3) {
/* 262 */     return move(paramVec3.x, paramVec3.y, paramVec3.z);
/*     */   }
/*     */   
/*     */   public AABB move(Vector3f paramVector3f) {
/* 266 */     return move(paramVector3f.x, paramVector3f.y, paramVector3f.z);
/*     */   }
/*     */   
/*     */   public boolean intersects(AABB paramAABB) {
/* 270 */     return intersects(paramAABB.minX, paramAABB.minY, paramAABB.minZ, paramAABB.maxX, paramAABB.maxY, paramAABB.maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/* 275 */     return (this.minX < paramDouble4 && this.maxX > paramDouble1 && this.minY < paramDouble5 && this.maxY > paramDouble2 && this.minZ < paramDouble6 && this.maxZ > paramDouble3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Vec3 paramVec31, Vec3 paramVec32) {
/* 284 */     return intersects(Math.min(paramVec31.x, paramVec32.x), Math.min(paramVec31.y, paramVec32.y), Math.min(paramVec31.z, paramVec32.z), Math.max(paramVec31.x, paramVec32.x), Math.max(paramVec31.y, paramVec32.y), Math.max(paramVec31.z, paramVec32.z));
/*     */   }
/*     */   
/*     */   public boolean intersects(BlockPos paramBlockPos) {
/* 288 */     return intersects(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), (paramBlockPos.getX() + 1), (paramBlockPos.getY() + 1), (paramBlockPos.getZ() + 1));
/*     */   }
/*     */   
/*     */   public boolean contains(Vec3 paramVec3) {
/* 292 */     return contains(paramVec3.x, paramVec3.y, paramVec3.z);
/*     */   }
/*     */   
/*     */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 296 */     return (paramDouble1 >= this.minX && paramDouble1 < this.maxX && paramDouble2 >= this.minY && paramDouble2 < this.maxY && paramDouble3 >= this.minZ && paramDouble3 < this.maxZ);
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 300 */     double d1 = getXsize();
/* 301 */     double d2 = getYsize();
/* 302 */     double d3 = getZsize();
/* 303 */     return (d1 + d2 + d3) / 3.0D;
/*     */   }
/*     */   
/*     */   public double getXsize() {
/* 307 */     return this.maxX - this.minX;
/*     */   }
/*     */   
/*     */   public double getYsize() {
/* 311 */     return this.maxY - this.minY;
/*     */   }
/*     */   
/*     */   public double getZsize() {
/* 315 */     return this.maxZ - this.minZ;
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
/*     */   public AABB deflate(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 327 */     return inflate(-paramDouble1, -paramDouble2, -paramDouble3);
/*     */   }
/*     */   
/*     */   public AABB deflate(double paramDouble) {
/* 331 */     return inflate(-paramDouble);
/*     */   }
/*     */   
/*     */   public Optional<Vec3> clip(Vec3 paramVec31, Vec3 paramVec32) {
/* 335 */     return clip(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, paramVec31, paramVec32);
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> clip(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, Vec3 paramVec31, Vec3 paramVec32) {
/* 339 */     double[] arrayOfDouble = { 1.0D };
/* 340 */     double d1 = paramVec32.x - paramVec31.x;
/* 341 */     double d2 = paramVec32.y - paramVec31.y;
/* 342 */     double d3 = paramVec32.z - paramVec31.z;
/*     */     
/* 344 */     Direction direction = getDirection(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramVec31, arrayOfDouble, null, d1, d2, d3);
/* 345 */     if (direction == null) {
/* 346 */       return Optional.empty();
/*     */     }
/*     */     
/* 349 */     double d4 = arrayOfDouble[0];
/* 350 */     return Optional.of(paramVec31.add(d4 * d1, d4 * d2, d4 * d3));
/*     */   }
/*     */   
/*     */   public static BlockHitResult clip(Iterable<AABB> paramIterable, Vec3 paramVec31, Vec3 paramVec32, BlockPos paramBlockPos) {
/* 354 */     double[] arrayOfDouble = { 1.0D };
/* 355 */     Direction direction = null;
/*     */     
/* 357 */     double d1 = paramVec32.x - paramVec31.x;
/* 358 */     double d2 = paramVec32.y - paramVec31.y;
/* 359 */     double d3 = paramVec32.z - paramVec31.z;
/*     */     
/* 361 */     for (AABB aABB : paramIterable) {
/* 362 */       direction = getDirection(aABB.move(paramBlockPos), paramVec31, arrayOfDouble, direction, d1, d2, d3);
/*     */     }
/*     */     
/* 365 */     if (direction == null) {
/* 366 */       return null;
/*     */     }
/*     */     
/* 369 */     double d4 = arrayOfDouble[0];
/* 370 */     return new BlockHitResult(paramVec31.add(d4 * d1, d4 * d2, d4 * d3), direction, paramBlockPos, false);
/*     */   }
/*     */   
/*     */   private static Direction getDirection(AABB paramAABB, Vec3 paramVec3, double[] paramArrayOfdouble, Direction paramDirection, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 374 */     return getDirection(paramAABB.minX, paramAABB.minY, paramAABB.minZ, paramAABB.maxX, paramAABB.maxY, paramAABB.maxZ, paramVec3, paramArrayOfdouble, paramDirection, paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */   
/*     */   private static Direction getDirection(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, Vec3 paramVec3, double[] paramArrayOfdouble, Direction paramDirection, double paramDouble7, double paramDouble8, double paramDouble9) {
/* 378 */     if (paramDouble7 > 1.0E-7D) {
/* 379 */       paramDirection = clipPoint(paramArrayOfdouble, paramDirection, paramDouble7, paramDouble8, paramDouble9, paramDouble1, paramDouble2, paramDouble5, paramDouble3, paramDouble6, Direction.WEST, paramVec3.x, paramVec3.y, paramVec3.z);
/* 380 */     } else if (paramDouble7 < -1.0E-7D) {
/* 381 */       paramDirection = clipPoint(paramArrayOfdouble, paramDirection, paramDouble7, paramDouble8, paramDouble9, paramDouble4, paramDouble2, paramDouble5, paramDouble3, paramDouble6, Direction.EAST, paramVec3.x, paramVec3.y, paramVec3.z);
/*     */     } 
/*     */     
/* 384 */     if (paramDouble8 > 1.0E-7D) {
/* 385 */       paramDirection = clipPoint(paramArrayOfdouble, paramDirection, paramDouble8, paramDouble9, paramDouble7, paramDouble2, paramDouble3, paramDouble6, paramDouble1, paramDouble4, Direction.DOWN, paramVec3.y, paramVec3.z, paramVec3.x);
/* 386 */     } else if (paramDouble8 < -1.0E-7D) {
/* 387 */       paramDirection = clipPoint(paramArrayOfdouble, paramDirection, paramDouble8, paramDouble9, paramDouble7, paramDouble5, paramDouble3, paramDouble6, paramDouble1, paramDouble4, Direction.UP, paramVec3.y, paramVec3.z, paramVec3.x);
/*     */     } 
/*     */     
/* 390 */     if (paramDouble9 > 1.0E-7D) {
/* 391 */       paramDirection = clipPoint(paramArrayOfdouble, paramDirection, paramDouble9, paramDouble7, paramDouble8, paramDouble3, paramDouble1, paramDouble4, paramDouble2, paramDouble5, Direction.NORTH, paramVec3.z, paramVec3.x, paramVec3.y);
/* 392 */     } else if (paramDouble9 < -1.0E-7D) {
/* 393 */       paramDirection = clipPoint(paramArrayOfdouble, paramDirection, paramDouble9, paramDouble7, paramDouble8, paramDouble6, paramDouble1, paramDouble4, paramDouble2, paramDouble5, Direction.SOUTH, paramVec3.z, paramVec3.x, paramVec3.y);
/*     */     } 
/* 395 */     return paramDirection;
/*     */   }
/*     */   
/*     */   private static Direction clipPoint(double[] paramArrayOfdouble, Direction paramDirection1, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, Direction paramDirection2, double paramDouble9, double paramDouble10, double paramDouble11) {
/* 399 */     double d1 = (paramDouble4 - paramDouble9) / paramDouble1;
/* 400 */     double d2 = paramDouble10 + d1 * paramDouble2;
/* 401 */     double d3 = paramDouble11 + d1 * paramDouble3;
/* 402 */     if (0.0D < d1 && d1 < paramArrayOfdouble[0] && paramDouble5 - 1.0E-7D < d2 && d2 < paramDouble6 + 1.0E-7D && paramDouble7 - 1.0E-7D < d3 && d3 < paramDouble8 + 1.0E-7D) {
/*     */ 
/*     */ 
/*     */       
/* 406 */       paramArrayOfdouble[0] = d1;
/* 407 */       return paramDirection2;
/*     */     } 
/* 409 */     return paramDirection1;
/*     */   }
/*     */   
/*     */   public boolean collidedAlongVector(Vec3 paramVec3, List<AABB> paramList) {
/* 413 */     Vec3 vec31 = getCenter();
/* 414 */     Vec3 vec32 = vec31.add(paramVec3);
/* 415 */     for (AABB aABB1 : paramList) {
/*     */       
/* 417 */       AABB aABB2 = aABB1.inflate(
/* 418 */           getXsize() * 0.5D - 1.0E-7D, 
/* 419 */           getYsize() * 0.5D - 1.0E-7D, 
/* 420 */           getZsize() * 0.5D - 1.0E-7D);
/*     */       
/* 422 */       if (aABB2.contains(vec32) || aABB2.contains(vec31))
/* 423 */         return true; 
/* 424 */       if (aABB2.clip(vec31, vec32).isPresent()) {
/* 425 */         return true;
/*     */       }
/*     */     } 
/* 428 */     return false;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Vec3 paramVec3) {
/* 432 */     double d1 = Math.max(Math.max(this.minX - paramVec3.x, paramVec3.x - this.maxX), 0.0D);
/* 433 */     double d2 = Math.max(Math.max(this.minY - paramVec3.y, paramVec3.y - this.maxY), 0.0D);
/* 434 */     double d3 = Math.max(Math.max(this.minZ - paramVec3.z, paramVec3.z - this.maxZ), 0.0D);
/* 435 */     return Mth.lengthSquared(d1, d2, d3);
/*     */   }
/*     */   
/*     */   public double distanceToSqr(AABB paramAABB) {
/* 439 */     double d1 = Math.max(Math.max(this.minX - paramAABB.maxX, paramAABB.minX - this.maxX), 0.0D);
/* 440 */     double d2 = Math.max(Math.max(this.minY - paramAABB.maxY, paramAABB.minY - this.maxY), 0.0D);
/* 441 */     double d3 = Math.max(Math.max(this.minZ - paramAABB.maxZ, paramAABB.minZ - this.maxZ), 0.0D);
/* 442 */     return Mth.lengthSquared(d1, d2, d3);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 447 */     return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
/*     */   }
/*     */   
/*     */   public boolean hasNaN() {
/* 451 */     return (Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ));
/*     */   }
/*     */   
/*     */   public Vec3 getCenter() {
/* 455 */     return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), Mth.lerp(0.5D, this.minY, this.maxY), Mth.lerp(0.5D, this.minZ, this.maxZ));
/*     */   }
/*     */   
/*     */   public Vec3 getBottomCenter() {
/* 459 */     return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), this.minY, Mth.lerp(0.5D, this.minZ, this.maxZ));
/*     */   }
/*     */   
/*     */   public Vec3 getMinPosition() {
/* 463 */     return new Vec3(this.minX, this.minY, this.minZ);
/*     */   }
/*     */   
/*     */   public Vec3 getMaxPosition() {
/* 467 */     return new Vec3(this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public static AABB ofSize(Vec3 paramVec3, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 471 */     return new AABB(paramVec3.x - paramDouble1 / 2.0D, paramVec3.y - paramDouble2 / 2.0D, paramVec3.z - paramDouble3 / 2.0D, paramVec3.x + paramDouble1 / 2.0D, paramVec3.y + paramDouble2 / 2.0D, paramVec3.z + paramDouble3 / 2.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 482 */     private float minX = Float.POSITIVE_INFINITY;
/* 483 */     private float minY = Float.POSITIVE_INFINITY;
/* 484 */     private float minZ = Float.POSITIVE_INFINITY;
/*     */     
/* 486 */     private float maxX = Float.NEGATIVE_INFINITY;
/* 487 */     private float maxY = Float.NEGATIVE_INFINITY;
/* 488 */     private float maxZ = Float.NEGATIVE_INFINITY;
/*     */     
/*     */     public void include(Vector3fc param1Vector3fc) {
/* 491 */       this.minX = Math.min(this.minX, param1Vector3fc.x());
/* 492 */       this.minY = Math.min(this.minY, param1Vector3fc.y());
/* 493 */       this.minZ = Math.min(this.minZ, param1Vector3fc.z());
/*     */       
/* 495 */       this.maxX = Math.max(this.maxX, param1Vector3fc.x());
/* 496 */       this.maxY = Math.max(this.maxY, param1Vector3fc.y());
/* 497 */       this.maxZ = Math.max(this.maxZ, param1Vector3fc.z());
/*     */     }
/*     */     
/*     */     public AABB build() {
/* 501 */       return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\AABB.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */