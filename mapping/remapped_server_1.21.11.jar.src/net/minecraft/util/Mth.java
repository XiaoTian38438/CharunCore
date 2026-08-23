/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import java.util.function.IntPredicate;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.joml.Math;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mth
/*     */ {
/*     */   private static final long UUID_VERSION = 61440L;
/*     */   private static final long UUID_VERSION_TYPE_4 = 16384L;
/*     */   private static final long UUID_VARIANT = -4611686018427387904L;
/*     */   private static final long UUID_VARIANT_2 = -9223372036854775808L;
/*     */   public static final float PI = 3.1415927F;
/*     */   public static final float HALF_PI = 1.5707964F;
/*     */   public static final float TWO_PI = 6.2831855F;
/*     */   public static final float DEG_TO_RAD = 0.017453292F;
/*     */   public static final float RAD_TO_DEG = 57.295776F;
/*     */   public static final float EPSILON = 1.0E-5F;
/*  34 */   public static final float SQRT_OF_TWO = sqrt(2.0F);
/*     */   
/*  36 */   public static final Vector3f Y_AXIS = new Vector3f(0.0F, 1.0F, 0.0F);
/*  37 */   public static final Vector3f X_AXIS = new Vector3f(1.0F, 0.0F, 0.0F);
/*  38 */   public static final Vector3f Z_AXIS = new Vector3f(0.0F, 0.0F, 1.0F);
/*     */   
/*     */   private static final int SIN_QUANTIZATION = 65536;
/*     */   
/*     */   private static final int SIN_MASK = 65535;
/*     */   
/*     */   static {
/*  45 */     SIN = Util.<float[]>make(new float[65536], paramArrayOffloat -> {
/*     */           for (byte b = 0; b < paramArrayOffloat.length; b++)
/*     */             paramArrayOffloat[b] = (float)Math.sin(b / 10430.378350470453D); 
/*     */         });
/*     */   }
/*     */   private static final int COS_OFFSET = 16384; private static final double SIN_SCALE = 10430.378350470453D; private static final float[] SIN;
/*  51 */   private static final RandomSource RANDOM = RandomSource.createThreadSafe();
/*     */   
/*     */   public static float sin(double paramDouble) {
/*  54 */     return SIN[(int)((long)(paramDouble * 10430.378350470453D) & 0xFFFFL)];
/*     */   }
/*     */   
/*     */   public static float cos(double paramDouble) {
/*  58 */     return SIN[(int)((long)(paramDouble * 10430.378350470453D + 16384.0D) & 0xFFFFL)];
/*     */   }
/*     */   
/*     */   public static float sqrt(float paramFloat) {
/*  62 */     return (float)Math.sqrt(paramFloat);
/*     */   }
/*     */   
/*     */   public static int floor(float paramFloat) {
/*  66 */     int i = (int)paramFloat;
/*  67 */     return (paramFloat < i) ? (i - 1) : i;
/*     */   }
/*     */   
/*     */   public static int floor(double paramDouble) {
/*  71 */     int i = (int)paramDouble;
/*  72 */     return (paramDouble < i) ? (i - 1) : i;
/*     */   }
/*     */   
/*     */   public static long lfloor(double paramDouble) {
/*  76 */     long l = (long)paramDouble;
/*  77 */     return (paramDouble < l) ? (l - 1L) : l;
/*     */   }
/*     */   
/*     */   public static float abs(float paramFloat) {
/*  81 */     return Math.abs(paramFloat);
/*     */   }
/*     */   
/*     */   public static int abs(int paramInt) {
/*  85 */     return Math.abs(paramInt);
/*     */   }
/*     */   
/*     */   public static int ceil(float paramFloat) {
/*  89 */     int i = (int)paramFloat;
/*  90 */     return (paramFloat > i) ? (i + 1) : i;
/*     */   }
/*     */   
/*     */   public static int ceil(double paramDouble) {
/*  94 */     int i = (int)paramDouble;
/*  95 */     return (paramDouble > i) ? (i + 1) : i;
/*     */   }
/*     */   
/*     */   public static long ceilLong(double paramDouble) {
/*  99 */     long l = (long)paramDouble;
/* 100 */     return (paramDouble > l) ? (l + 1L) : l;
/*     */   }
/*     */   
/*     */   public static int clamp(int paramInt1, int paramInt2, int paramInt3) {
/* 104 */     return Math.min(Math.max(paramInt1, paramInt2), paramInt3);
/*     */   }
/*     */   
/*     */   public static long clamp(long paramLong1, long paramLong2, long paramLong3) {
/* 108 */     return Math.min(Math.max(paramLong1, paramLong2), paramLong3);
/*     */   }
/*     */   
/*     */   public static float clamp(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 112 */     if (paramFloat1 < paramFloat2) {
/* 113 */       return paramFloat2;
/*     */     }
/* 115 */     return Math.min(paramFloat1, paramFloat3);
/*     */   }
/*     */   
/*     */   public static double clamp(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 119 */     if (paramDouble1 < paramDouble2) {
/* 120 */       return paramDouble2;
/*     */     }
/* 122 */     return Math.min(paramDouble1, paramDouble3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double clampedLerp(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 130 */     if (paramDouble1 < 0.0D) {
/* 131 */       return paramDouble2;
/*     */     }
/* 133 */     if (paramDouble1 > 1.0D) {
/* 134 */       return paramDouble3;
/*     */     }
/* 136 */     return lerp(paramDouble1, paramDouble2, paramDouble3);
/*     */   }
/*     */   
/*     */   public static float clampedLerp(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 140 */     if (paramFloat1 < 0.0F) {
/* 141 */       return paramFloat2;
/*     */     }
/* 143 */     if (paramFloat1 > 1.0F) {
/* 144 */       return paramFloat3;
/*     */     }
/* 146 */     return lerp(paramFloat1, paramFloat2, paramFloat3);
/*     */   }
/*     */   
/*     */   public static int absMax(int paramInt1, int paramInt2) {
/* 150 */     return Math.max(Math.abs(paramInt1), Math.abs(paramInt2));
/*     */   }
/*     */   
/*     */   public static float absMax(float paramFloat1, float paramFloat2) {
/* 154 */     return Math.max(Math.abs(paramFloat1), Math.abs(paramFloat2));
/*     */   }
/*     */   
/*     */   public static double absMax(double paramDouble1, double paramDouble2) {
/* 158 */     return Math.max(Math.abs(paramDouble1), Math.abs(paramDouble2));
/*     */   }
/*     */   
/*     */   public static int chessboardDistance(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 162 */     return absMax(paramInt3 - paramInt1, paramInt4 - paramInt2);
/*     */   }
/*     */   
/*     */   public static int floorDiv(int paramInt1, int paramInt2) {
/* 166 */     return Math.floorDiv(paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public static int nextInt(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/* 170 */     if (paramInt1 >= paramInt2) {
/* 171 */       return paramInt1;
/*     */     }
/* 173 */     return paramRandomSource.nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
/*     */   }
/*     */   
/*     */   public static float nextFloat(RandomSource paramRandomSource, float paramFloat1, float paramFloat2) {
/* 177 */     if (paramFloat1 >= paramFloat2) {
/* 178 */       return paramFloat1;
/*     */     }
/* 180 */     return paramRandomSource.nextFloat() * (paramFloat2 - paramFloat1) + paramFloat1;
/*     */   }
/*     */   
/*     */   public static double nextDouble(RandomSource paramRandomSource, double paramDouble1, double paramDouble2) {
/* 184 */     if (paramDouble1 >= paramDouble2) {
/* 185 */       return paramDouble1;
/*     */     }
/* 187 */     return paramRandomSource.nextDouble() * (paramDouble2 - paramDouble1) + paramDouble1;
/*     */   }
/*     */   
/*     */   public static boolean equal(float paramFloat1, float paramFloat2) {
/* 191 */     return (Math.abs(paramFloat2 - paramFloat1) < 1.0E-5F);
/*     */   }
/*     */   
/*     */   public static boolean equal(double paramDouble1, double paramDouble2) {
/* 195 */     return (Math.abs(paramDouble2 - paramDouble1) < 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   public static int positiveModulo(int paramInt1, int paramInt2) {
/* 199 */     return Math.floorMod(paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public static float positiveModulo(float paramFloat1, float paramFloat2) {
/* 203 */     return (paramFloat1 % paramFloat2 + paramFloat2) % paramFloat2;
/*     */   }
/*     */   
/*     */   public static double positiveModulo(double paramDouble1, double paramDouble2) {
/* 207 */     return (paramDouble1 % paramDouble2 + paramDouble2) % paramDouble2;
/*     */   }
/*     */   
/*     */   public static boolean isMultipleOf(int paramInt1, int paramInt2) {
/* 211 */     return (paramInt1 % paramInt2 == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte packDegrees(float paramFloat) {
/* 218 */     return (byte)floor(paramFloat * 256.0F / 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float unpackDegrees(byte paramByte) {
/* 225 */     return (paramByte * 360) / 256.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int wrapDegrees(int paramInt) {
/* 232 */     int i = paramInt % 360;
/* 233 */     if (i >= 180) {
/* 234 */       i -= 360;
/*     */     }
/* 236 */     if (i < -180) {
/* 237 */       i += 360;
/*     */     }
/* 239 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float wrapDegrees(long paramLong) {
/* 246 */     float f = (float)(paramLong % 360L);
/* 247 */     if (f >= 180.0F) {
/* 248 */       f -= 360.0F;
/*     */     }
/* 250 */     if (f < -180.0F) {
/* 251 */       f += 360.0F;
/*     */     }
/* 253 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float wrapDegrees(float paramFloat) {
/* 260 */     float f = paramFloat % 360.0F;
/* 261 */     if (f >= 180.0F) {
/* 262 */       f -= 360.0F;
/*     */     }
/* 264 */     if (f < -180.0F) {
/* 265 */       f += 360.0F;
/*     */     }
/* 267 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double wrapDegrees(double paramDouble) {
/* 274 */     double d = paramDouble % 360.0D;
/* 275 */     if (d >= 180.0D) {
/* 276 */       d -= 360.0D;
/*     */     }
/* 278 */     if (d < -180.0D) {
/* 279 */       d += 360.0D;
/*     */     }
/* 281 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float degreesDifference(float paramFloat1, float paramFloat2) {
/* 289 */     return wrapDegrees(paramFloat2 - paramFloat1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float degreesDifferenceAbs(float paramFloat1, float paramFloat2) {
/* 297 */     return abs(degreesDifference(paramFloat1, paramFloat2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float rotateIfNecessary(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 306 */     float f1 = degreesDifference(paramFloat1, paramFloat2);
/* 307 */     float f2 = clamp(f1, -paramFloat3, paramFloat3);
/* 308 */     return paramFloat2 - f2;
/*     */   }
/*     */   
/*     */   public static float approach(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 312 */     paramFloat3 = abs(paramFloat3);
/*     */     
/* 314 */     if (paramFloat1 < paramFloat2) {
/* 315 */       return clamp(paramFloat1 + paramFloat3, paramFloat1, paramFloat2);
/*     */     }
/* 317 */     return clamp(paramFloat1 - paramFloat3, paramFloat2, paramFloat1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float approachDegrees(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 322 */     float f = degreesDifference(paramFloat1, paramFloat2);
/* 323 */     return approach(paramFloat1, paramFloat1 + f, paramFloat3);
/*     */   }
/*     */   
/*     */   public static int getInt(String paramString, int paramInt) {
/* 327 */     return NumberUtils.toInt(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int smallestEncompassingPowerOfTwo(int paramInt) {
/* 332 */     int i = paramInt - 1;
/* 333 */     i |= i >> 1;
/* 334 */     i |= i >> 2;
/* 335 */     i |= i >> 4;
/* 336 */     i |= i >> 8;
/* 337 */     i |= i >> 16;
/* 338 */     return i + 1;
/*     */   }
/*     */   
/*     */   public static int smallestSquareSide(int paramInt) {
/* 342 */     if (paramInt < 0) {
/* 343 */       throw new IllegalArgumentException("itemCount must be greater than or equal to zero");
/*     */     }
/* 345 */     return ceil(Math.sqrt(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(int paramInt) {
/* 350 */     return (paramInt != 0 && (paramInt & paramInt - 1) == 0);
/*     */   }
/*     */ 
/*     */   
/* 354 */   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 }; private static final double ONE_SIXTH = 0.16666666666666666D;
/*     */   private static final int FRAC_EXP = 8;
/*     */   private static final int LUT_SIZE = 257;
/*     */   
/*     */   public static int ceillog2(int paramInt) {
/* 359 */     paramInt = isPowerOfTwo(paramInt) ? paramInt : smallestEncompassingPowerOfTwo(paramInt);
/* 360 */     return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(paramInt * 125613361L >> 27L) & 0x1F];
/*     */   }
/*     */   
/*     */   public static int log2(int paramInt) {
/* 364 */     return ceillog2(paramInt) - (isPowerOfTwo(paramInt) ? 0 : 1);
/*     */   }
/*     */   
/*     */   public static float frac(float paramFloat) {
/* 368 */     return paramFloat - floor(paramFloat);
/*     */   }
/*     */   
/*     */   public static double frac(double paramDouble) {
/* 372 */     return paramDouble - lfloor(paramDouble);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static long getSeed(Vec3i paramVec3i) {
/* 377 */     return getSeed(paramVec3i.getX(), paramVec3i.getY(), paramVec3i.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long getSeed(int paramInt1, int paramInt2, int paramInt3) {
/* 387 */     long l = (paramInt1 * 3129871) ^ paramInt3 * 116129781L ^ paramInt2;
/* 388 */     l = l * l * 42317861L + l * 11L;
/* 389 */     return l >> 16L;
/*     */   }
/*     */   
/*     */   public static UUID createInsecureUUID(RandomSource paramRandomSource) {
/* 393 */     long l1 = paramRandomSource.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
/* 394 */     long l2 = paramRandomSource.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
/* 395 */     return new UUID(l1, l2);
/*     */   }
/*     */   
/*     */   public static UUID createInsecureUUID() {
/* 399 */     return createInsecureUUID(RANDOM);
/*     */   }
/*     */   
/*     */   public static double inverseLerp(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 403 */     return (paramDouble1 - paramDouble2) / (paramDouble3 - paramDouble2);
/*     */   }
/*     */   
/*     */   public static float inverseLerp(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 407 */     return (paramFloat1 - paramFloat2) / (paramFloat3 - paramFloat2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean rayIntersectsAABB(Vec3 paramVec31, Vec3 paramVec32, AABB paramAABB) {
/* 412 */     double d1 = (paramAABB.minX + paramAABB.maxX) * 0.5D;
/* 413 */     double d2 = (paramAABB.maxX - paramAABB.minX) * 0.5D;
/* 414 */     double d3 = paramVec31.x - d1;
/* 415 */     if (Math.abs(d3) > d2 && d3 * paramVec32.x >= 0.0D) {
/* 416 */       return false;
/*     */     }
/*     */     
/* 419 */     double d4 = (paramAABB.minY + paramAABB.maxY) * 0.5D;
/* 420 */     double d5 = (paramAABB.maxY - paramAABB.minY) * 0.5D;
/* 421 */     double d6 = paramVec31.y - d4;
/* 422 */     if (Math.abs(d6) > d5 && d6 * paramVec32.y >= 0.0D) {
/* 423 */       return false;
/*     */     }
/*     */     
/* 426 */     double d7 = (paramAABB.minZ + paramAABB.maxZ) * 0.5D;
/* 427 */     double d8 = (paramAABB.maxZ - paramAABB.minZ) * 0.5D;
/* 428 */     double d9 = paramVec31.z - d7;
/* 429 */     if (Math.abs(d9) > d8 && d9 * paramVec32.z >= 0.0D) {
/* 430 */       return false;
/*     */     }
/*     */     
/* 433 */     double d10 = Math.abs(paramVec32.x);
/* 434 */     double d11 = Math.abs(paramVec32.y);
/* 435 */     double d12 = Math.abs(paramVec32.z);
/*     */     
/* 437 */     double d13 = paramVec32.y * d9 - paramVec32.z * d6;
/* 438 */     if (Math.abs(d13) > d5 * d12 + d8 * d11) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     d13 = paramVec32.z * d3 - paramVec32.x * d9;
/* 443 */     if (Math.abs(d13) > d2 * d12 + d8 * d10) {
/* 444 */       return false;
/*     */     }
/*     */     
/* 447 */     d13 = paramVec32.x * d6 - paramVec32.y * d3;
/*     */     
/* 449 */     return (Math.abs(d13) < d2 * d11 + d5 * d10);
/*     */   }
/*     */   
/*     */   public static double atan2(double paramDouble1, double paramDouble2) {
/* 453 */     double d1 = paramDouble2 * paramDouble2 + paramDouble1 * paramDouble1;
/*     */ 
/*     */     
/* 456 */     if (Double.isNaN(d1)) {
/* 457 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/* 461 */     boolean bool1 = (paramDouble1 < 0.0D) ? true : false;
/* 462 */     if (bool1) {
/* 463 */       paramDouble1 = -paramDouble1;
/*     */     }
/* 465 */     boolean bool2 = (paramDouble2 < 0.0D) ? true : false;
/* 466 */     if (bool2) {
/* 467 */       paramDouble2 = -paramDouble2;
/*     */     }
/* 469 */     boolean bool3 = (paramDouble1 > paramDouble2) ? true : false;
/* 470 */     if (bool3) {
/* 471 */       double d = paramDouble2;
/*     */       
/* 473 */       paramDouble2 = paramDouble1;
/* 474 */       paramDouble1 = d;
/*     */     } 
/*     */ 
/*     */     
/* 478 */     double d2 = fastInvSqrt(d1);
/* 479 */     paramDouble2 *= d2;
/* 480 */     paramDouble1 *= d2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     double d3 = FRAC_BIAS + paramDouble1;
/* 490 */     int i = (int)Double.doubleToRawLongBits(d3);
/*     */ 
/*     */     
/* 493 */     double d4 = ASIN_TAB[i];
/* 494 */     double d5 = COS_TAB[i];
/*     */ 
/*     */ 
/*     */     
/* 498 */     double d6 = d3 - FRAC_BIAS;
/* 499 */     double d7 = paramDouble1 * d5 - paramDouble2 * d6;
/*     */ 
/*     */     
/* 502 */     double d8 = (6.0D + d7 * d7) * d7 * 0.16666666666666666D;
/* 503 */     double d9 = d4 + d8;
/*     */ 
/*     */     
/* 506 */     if (bool3) {
/* 507 */       d9 = 1.5707963267948966D - d9;
/*     */     }
/* 509 */     if (bool2) {
/* 510 */       d9 = Math.PI - d9;
/*     */     }
/* 512 */     if (bool1) {
/* 513 */       d9 = -d9;
/*     */     }
/*     */     
/* 516 */     return d9;
/*     */   }
/*     */   
/*     */   public static float invSqrt(float paramFloat) {
/* 520 */     return Math.invsqrt(paramFloat);
/*     */   }
/*     */   
/*     */   public static double invSqrt(double paramDouble) {
/* 524 */     return Math.invsqrt(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static double fastInvSqrt(double paramDouble) {
/* 535 */     double d = 0.5D * paramDouble;
/* 536 */     long l = Double.doubleToRawLongBits(paramDouble);
/* 537 */     l = 6910469410427058090L - (l >> 1L);
/* 538 */     paramDouble = Double.longBitsToDouble(l);
/* 539 */     paramDouble *= 1.5D - d * paramDouble * paramDouble;
/* 540 */     return paramDouble;
/*     */   }
/*     */   
/*     */   public static float fastInvCubeRoot(float paramFloat) {
/* 544 */     int i = Float.floatToIntBits(paramFloat);
/* 545 */     i = 1419967116 - i / 3;
/* 546 */     float f = Float.intBitsToFloat(i);
/* 547 */     f = 0.6666667F * f + 1.0F / 3.0F * f * f * paramFloat;
/* 548 */     f = 0.6666667F * f + 1.0F / 3.0F * f * f * paramFloat;
/* 549 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 555 */   private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
/* 556 */   private static final double[] ASIN_TAB = new double[257];
/* 557 */   private static final double[] COS_TAB = new double[257];
/*     */ 
/*     */   
/*     */   static {
/* 561 */     for (byte b = 0; b < 'ā'; b++) {
/* 562 */       double d1 = b / 256.0D;
/* 563 */       double d2 = Math.asin(d1);
/* 564 */       COS_TAB[b] = Math.cos(d2);
/* 565 */       ASIN_TAB[b] = d2;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int hsvToRgb(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 570 */     return hsvToArgb(paramFloat1, paramFloat2, paramFloat3, 0);
/*     */   }
/*     */   public static int hsvToArgb(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
/*     */     float f5, f6, f7;
/* 574 */     int i = (int)(paramFloat1 * 6.0F) % 6;
/* 575 */     float f1 = paramFloat1 * 6.0F - i;
/* 576 */     float f2 = paramFloat3 * (1.0F - paramFloat2);
/* 577 */     float f3 = paramFloat3 * (1.0F - f1 * paramFloat2);
/* 578 */     float f4 = paramFloat3 * (1.0F - (1.0F - f1) * paramFloat2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 584 */     switch (i) {
/*     */       case 0:
/* 586 */         f5 = paramFloat3;
/* 587 */         f6 = f4;
/* 588 */         f7 = f2;
/*     */         break;
/*     */       case 1:
/* 591 */         f5 = f3;
/* 592 */         f6 = paramFloat3;
/* 593 */         f7 = f2;
/*     */         break;
/*     */       case 2:
/* 596 */         f5 = f2;
/* 597 */         f6 = paramFloat3;
/* 598 */         f7 = f4;
/*     */         break;
/*     */       case 3:
/* 601 */         f5 = f2;
/* 602 */         f6 = f3;
/* 603 */         f7 = paramFloat3;
/*     */         break;
/*     */       case 4:
/* 606 */         f5 = f4;
/* 607 */         f6 = f2;
/* 608 */         f7 = paramFloat3;
/*     */         break;
/*     */       case 5:
/* 611 */         f5 = paramFloat3;
/* 612 */         f6 = f2;
/* 613 */         f7 = f3;
/*     */         break;
/*     */       default:
/* 616 */         throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + paramFloat1 + ", " + paramFloat2 + ", " + paramFloat3);
/*     */     } 
/*     */     
/* 619 */     return ARGB.color(paramInt, 
/* 620 */         clamp((int)(f5 * 255.0F), 0, 255), 
/* 621 */         clamp((int)(f6 * 255.0F), 0, 255), 
/* 622 */         clamp((int)(f7 * 255.0F), 0, 255));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int murmurHash3Mixer(int paramInt) {
/* 628 */     paramInt ^= paramInt >>> 16;
/* 629 */     paramInt *= -2048144789;
/* 630 */     paramInt ^= paramInt >>> 13;
/* 631 */     paramInt *= -1028477387;
/* 632 */     paramInt ^= paramInt >>> 16;
/* 633 */     return paramInt;
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
/*     */   public static int binarySearch(int paramInt1, int paramInt2, IntPredicate paramIntPredicate) {
/* 649 */     int i = paramInt2 - paramInt1;
/* 650 */     while (i > 0) {
/* 651 */       int j = i / 2;
/* 652 */       int k = paramInt1 + j;
/* 653 */       if (paramIntPredicate.test(k)) {
/*     */         
/* 655 */         i = j; continue;
/*     */       } 
/* 657 */       paramInt1 = k + 1;
/* 658 */       i -= j + 1;
/*     */     } 
/*     */     
/* 661 */     return paramInt1;
/*     */   }
/*     */   
/*     */   public static int lerpInt(float paramFloat, int paramInt1, int paramInt2) {
/* 665 */     return paramInt1 + floor(paramFloat * (paramInt2 - paramInt1));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int lerpDiscrete(float paramFloat, int paramInt1, int paramInt2) {
/* 670 */     int i = paramInt2 - paramInt1;
/* 671 */     return paramInt1 + floor(paramFloat * (i - 1)) + ((paramFloat > 0.0F) ? 1 : 0);
/*     */   }
/*     */   
/*     */   public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 675 */     return paramFloat2 + paramFloat1 * (paramFloat3 - paramFloat2);
/*     */   }
/*     */   
/*     */   public static Vec3 lerp(double paramDouble, Vec3 paramVec31, Vec3 paramVec32) {
/* 679 */     return new Vec3(lerp(paramDouble, paramVec31.x, paramVec32.x), 
/* 680 */         lerp(paramDouble, paramVec31.y, paramVec32.y), 
/* 681 */         lerp(paramDouble, paramVec31.z, paramVec32.z));
/*     */   }
/*     */   
/*     */   public static double lerp(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 685 */     return paramDouble2 + paramDouble1 * (paramDouble3 - paramDouble2);
/*     */   }
/*     */   
/*     */   public static double lerp2(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/* 689 */     return lerp(paramDouble2, 
/*     */         
/* 691 */         lerp(paramDouble1, paramDouble3, paramDouble4), 
/* 692 */         lerp(paramDouble1, paramDouble5, paramDouble6));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double lerp3(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11) {
/* 701 */     return lerp(paramDouble3, 
/*     */         
/* 703 */         lerp2(paramDouble1, paramDouble2, paramDouble4, paramDouble5, paramDouble6, paramDouble7), 
/* 704 */         lerp2(paramDouble1, paramDouble2, paramDouble8, paramDouble9, paramDouble10, paramDouble11));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float catmullrom(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
/* 709 */     return 0.5F * (2.0F * paramFloat3 + (paramFloat4 - paramFloat2) * paramFloat1 + (2.0F * paramFloat2 - 5.0F * paramFloat3 + 4.0F * paramFloat4 - paramFloat5) * paramFloat1 * paramFloat1 + (3.0F * paramFloat3 - paramFloat2 - 3.0F * paramFloat4 + paramFloat5) * paramFloat1 * paramFloat1 * paramFloat1);
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
/*     */   public static double smoothstep(double paramDouble) {
/* 722 */     return paramDouble * paramDouble * paramDouble * (paramDouble * (paramDouble * 6.0D - 15.0D) + 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static double smoothstepDerivative(double paramDouble) {
/* 727 */     return 30.0D * paramDouble * paramDouble * (paramDouble - 1.0D) * (paramDouble - 1.0D);
/*     */   }
/*     */   
/*     */   public static int sign(double paramDouble) {
/* 731 */     if (paramDouble == 0.0D) {
/* 732 */       return 0;
/*     */     }
/* 734 */     return (paramDouble > 0.0D) ? 1 : -1;
/*     */   }
/*     */   
/*     */   public static float rotLerp(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 738 */     return paramFloat2 + paramFloat1 * wrapDegrees(paramFloat3 - paramFloat2);
/*     */   }
/*     */   
/*     */   public static double rotLerp(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 742 */     return paramDouble2 + paramDouble1 * wrapDegrees(paramDouble3 - paramDouble2);
/*     */   }
/*     */   
/*     */   public static float rotLerpRad(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 746 */     float f = paramFloat3 - paramFloat2;
/* 747 */     while (f < -3.1415927F) {
/* 748 */       f += 6.2831855F;
/*     */     }
/* 750 */     while (f >= 3.1415927F) {
/* 751 */       f -= 6.2831855F;
/*     */     }
/* 753 */     return paramFloat2 + paramFloat1 * f;
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
/*     */   public static float triangleWave(float paramFloat1, float paramFloat2) {
/* 765 */     return (Math.abs(paramFloat1 % paramFloat2 - paramFloat2 * 0.5F) - paramFloat2 * 0.25F) / paramFloat2 * 0.25F;
/*     */   }
/*     */   
/*     */   public static float square(float paramFloat) {
/* 769 */     return paramFloat * paramFloat;
/*     */   }
/*     */   
/*     */   public static float cube(float paramFloat) {
/* 773 */     return paramFloat * paramFloat * paramFloat;
/*     */   }
/*     */   
/*     */   public static double square(double paramDouble) {
/* 777 */     return paramDouble * paramDouble;
/*     */   }
/*     */   
/*     */   public static int square(int paramInt) {
/* 781 */     return paramInt * paramInt;
/*     */   }
/*     */   
/*     */   public static long square(long paramLong) {
/* 785 */     return paramLong * paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double clampedMap(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/* 793 */     return clampedLerp(inverseLerp(paramDouble1, paramDouble2, paramDouble3), paramDouble4, paramDouble5);
/*     */   }
/*     */   
/*     */   public static float clampedMap(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
/* 797 */     return clampedLerp(inverseLerp(paramFloat1, paramFloat2, paramFloat3), paramFloat4, paramFloat5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double map(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
/* 804 */     return lerp(inverseLerp(paramDouble1, paramDouble2, paramDouble3), paramDouble4, paramDouble5);
/*     */   }
/*     */   
/*     */   public static float map(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
/* 808 */     return lerp(inverseLerp(paramFloat1, paramFloat2, paramFloat3), paramFloat4, paramFloat5);
/*     */   }
/*     */   
/*     */   public static double wobble(double paramDouble) {
/* 812 */     return paramDouble + (2.0D * RandomSource.create(floor(paramDouble * 3000.0D)).nextDouble() - 1.0D) * 1.0E-7D / 2.0D;
/*     */   }
/*     */   
/*     */   public static int roundToward(int paramInt1, int paramInt2) {
/* 816 */     return positiveCeilDiv(paramInt1, paramInt2) * paramInt2;
/*     */   }
/*     */   
/*     */   public static int positiveCeilDiv(int paramInt1, int paramInt2) {
/* 820 */     return -Math.floorDiv(-paramInt1, paramInt2);
/*     */   }
/*     */   
/*     */   public static int randomBetweenInclusive(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/* 824 */     return paramRandomSource.nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
/*     */   }
/*     */   
/*     */   public static float randomBetween(RandomSource paramRandomSource, float paramFloat1, float paramFloat2) {
/* 828 */     return paramRandomSource.nextFloat() * (paramFloat2 - paramFloat1) + paramFloat1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float normal(RandomSource paramRandomSource, float paramFloat1, float paramFloat2) {
/* 835 */     return paramFloat1 + (float)paramRandomSource.nextGaussian() * paramFloat2;
/*     */   }
/*     */   
/*     */   public static double lengthSquared(double paramDouble1, double paramDouble2) {
/* 839 */     return paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2;
/*     */   }
/*     */   
/*     */   public static double length(double paramDouble1, double paramDouble2) {
/* 843 */     return Math.sqrt(lengthSquared(paramDouble1, paramDouble2));
/*     */   }
/*     */   
/*     */   public static float length(float paramFloat1, float paramFloat2) {
/* 847 */     return (float)Math.sqrt(lengthSquared(paramFloat1, paramFloat2));
/*     */   }
/*     */   
/*     */   public static double lengthSquared(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 851 */     return paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2 + paramDouble3 * paramDouble3;
/*     */   }
/*     */   
/*     */   public static double length(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 855 */     return Math.sqrt(lengthSquared(paramDouble1, paramDouble2, paramDouble3));
/*     */   }
/*     */   
/*     */   public static float lengthSquared(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 859 */     return paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int quantize(double paramDouble, int paramInt) {
/* 866 */     return floor(paramDouble / paramInt) * paramInt;
/*     */   }
/*     */   
/*     */   public static IntStream outFromOrigin(int paramInt1, int paramInt2, int paramInt3) {
/* 870 */     return outFromOrigin(paramInt1, paramInt2, paramInt3, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntStream outFromOrigin(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 878 */     if (paramInt2 > paramInt3) {
/* 879 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "upperBound %d expected to be > lowerBound %d", new Object[] { Integer.valueOf(paramInt3), Integer.valueOf(paramInt2) }));
/*     */     }
/*     */     
/* 882 */     if (paramInt4 < 1) {
/* 883 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "step size expected to be >= 1, was %d", new Object[] { Integer.valueOf(paramInt4) }));
/*     */     }
/*     */     
/* 886 */     int i = clamp(paramInt1, paramInt2, paramInt3);
/* 887 */     return IntStream.iterate(i, paramInt4 -> {
/*     */           int i = Math.abs(paramInt1 - paramInt4);
/* 889 */           return (paramInt1 - i >= paramInt2 || paramInt1 + i <= paramInt3);
/*     */         }paramInt5 -> {
/*     */           boolean bool1 = (paramInt5 <= paramInt1) ? true : false;
/*     */           int i = Math.abs(paramInt1 - paramInt5);
/*     */           boolean bool2 = (paramInt1 + i + paramInt2 <= paramInt3) ? true : false;
/*     */           if (!bool1 || !bool2) {
/*     */             int j = paramInt1 - i - (bool1 ? paramInt2 : 0);
/*     */             if (j >= paramInt4) {
/*     */               return j;
/*     */             }
/*     */           } 
/*     */           return paramInt1 + i + paramInt2;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Quaternionf rotationAroundAxis(Vector3f paramVector3f, Quaternionf paramQuaternionf1, Quaternionf paramQuaternionf2) {
/* 907 */     float f = paramVector3f.dot(paramQuaternionf1.x, paramQuaternionf1.y, paramQuaternionf1.z);
/* 908 */     return paramQuaternionf2.set(paramVector3f.x * f, paramVector3f.y * f, paramVector3f.z * f, paramQuaternionf1.w).normalize();
/*     */   }
/*     */   
/*     */   public static int mulAndTruncate(Fraction paramFraction, int paramInt) {
/* 912 */     return paramFraction.getNumerator() * paramInt / paramFraction.getDenominator();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\Mth.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */