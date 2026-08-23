/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.util.BoundedFloatFunction;
/*     */ import net.minecraft.util.CubicSpline;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerrainProvider
/*     */ {
/*     */   private static final float DEEP_OCEAN_CONTINENTALNESS = -0.51F;
/*     */   private static final float OCEAN_CONTINENTALNESS = -0.4F;
/*     */   private static final float PLAINS_CONTINENTALNESS = 0.1F;
/*     */   private static final float BEACH_CONTINENTALNESS = -0.15F;
/*     */   private static final BoundedFloatFunction<Float> AMPLIFIED_OFFSET;
/*     */   private static final BoundedFloatFunction<Float> AMPLIFIED_FACTOR;
/*     */   private static final BoundedFloatFunction<Float> AMPLIFIED_JAGGEDNESS;
/*  21 */   private static final BoundedFloatFunction<Float> NO_TRANSFORM = BoundedFloatFunction.IDENTITY;
/*     */   static {
/*  23 */     AMPLIFIED_OFFSET = BoundedFloatFunction.createUnlimited(paramFloat -> (paramFloat < 0.0F) ? paramFloat : (paramFloat * 2.0F));
/*  24 */     AMPLIFIED_FACTOR = BoundedFloatFunction.createUnlimited(paramFloat -> 1.25F - 6.25F / (paramFloat + 5.0F));
/*  25 */     AMPLIFIED_JAGGEDNESS = BoundedFloatFunction.createUnlimited(paramFloat -> paramFloat * 2.0F);
/*     */   }
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> overworldOffset(I paramI1, I paramI2, I paramI3, boolean paramBoolean) {
/*  28 */     BoundedFloatFunction<Float> boundedFloatFunction = paramBoolean ? AMPLIFIED_OFFSET : NO_TRANSFORM;
/*     */ 
/*     */     
/*  31 */     CubicSpline<?, I> cubicSpline1 = buildErosionOffsetSpline(paramI2, paramI3, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, boundedFloatFunction);
/*  32 */     CubicSpline<?, I> cubicSpline2 = buildErosionOffsetSpline(paramI2, paramI3, -0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, boundedFloatFunction);
/*  33 */     CubicSpline<?, I> cubicSpline3 = buildErosionOffsetSpline(paramI2, paramI3, -0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, boundedFloatFunction);
/*  34 */     CubicSpline<?, I> cubicSpline4 = buildErosionOffsetSpline(paramI2, paramI3, -0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, boundedFloatFunction);
/*     */     
/*  36 */     return CubicSpline.builder((BoundedFloatFunction)paramI1, boundedFloatFunction)
/*  37 */       .addPoint(-1.1F, 0.044F)
/*  38 */       .addPoint(-1.02F, -0.2222F)
/*  39 */       .addPoint(-0.51F, -0.2222F)
/*  40 */       .addPoint(-0.44F, -0.12F)
/*  41 */       .addPoint(-0.18F, -0.12F)
/*  42 */       .addPoint(-0.16F, cubicSpline1)
/*  43 */       .addPoint(-0.15F, cubicSpline1)
/*  44 */       .addPoint(-0.1F, cubicSpline2)
/*  45 */       .addPoint(0.25F, cubicSpline3)
/*  46 */       .addPoint(1.0F, cubicSpline4)
/*  47 */       .build();
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> overworldFactor(I paramI1, I paramI2, I paramI3, I paramI4, boolean paramBoolean) {
/*  51 */     BoundedFloatFunction<Float> boundedFloatFunction = paramBoolean ? AMPLIFIED_FACTOR : NO_TRANSFORM;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     return CubicSpline.builder((BoundedFloatFunction)paramI1, NO_TRANSFORM)
/*     */ 
/*     */       
/*  76 */       .addPoint(-0.19F, 3.95F)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       .addPoint(-0.15F, getErosionFactor(paramI2, paramI3, paramI4, 6.25F, true, NO_TRANSFORM))
/*     */ 
/*     */       
/*  85 */       .addPoint(-0.1F, getErosionFactor(paramI2, paramI3, paramI4, 5.47F, true, boundedFloatFunction))
/*  86 */       .addPoint(0.03F, getErosionFactor(paramI2, paramI3, paramI4, 5.08F, true, boundedFloatFunction))
/*  87 */       .addPoint(0.06F, getErosionFactor(paramI2, paramI3, paramI4, 4.69F, false, boundedFloatFunction))
/*  88 */       .build();
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> overworldJaggedness(I paramI1, I paramI2, I paramI3, I paramI4, boolean paramBoolean) {
/*  92 */     BoundedFloatFunction<Float> boundedFloatFunction = paramBoolean ? AMPLIFIED_JAGGEDNESS : NO_TRANSFORM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     float f = 0.65F;
/* 101 */     return CubicSpline.builder((BoundedFloatFunction)paramI1, boundedFloatFunction)
/* 102 */       .addPoint(-0.11F, 0.0F)
/* 103 */       .addPoint(0.03F, buildErosionJaggednessSpline(paramI2, paramI3, paramI4, 1.0F, 0.5F, 0.0F, 0.0F, boundedFloatFunction))
/* 104 */       .addPoint(0.65F, buildErosionJaggednessSpline(paramI2, paramI3, paramI4, 1.0F, 1.0F, 1.0F, 0.0F, boundedFloatFunction))
/* 105 */       .build();
/*     */   }
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildErosionJaggednessSpline(I paramI1, I paramI2, I paramI3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 109 */     float f = -0.5775F;
/*     */     
/* 111 */     CubicSpline<?, I> cubicSpline1 = buildRidgeJaggednessSpline(paramI2, paramI3, paramFloat1, paramFloat3, paramBoundedFloatFunction);
/* 112 */     CubicSpline<?, I> cubicSpline2 = buildRidgeJaggednessSpline(paramI2, paramI3, paramFloat2, paramFloat4, paramBoundedFloatFunction);
/*     */     
/* 114 */     return CubicSpline.builder((BoundedFloatFunction)paramI1, paramBoundedFloatFunction)
/* 115 */       .addPoint(-1.0F, cubicSpline1)
/* 116 */       .addPoint(-0.78F, cubicSpline2)
/* 117 */       .addPoint(-0.5775F, cubicSpline2)
/* 118 */       .addPoint(-0.375F, 0.0F)
/* 119 */       .build();
/*     */   }
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildRidgeJaggednessSpline(I paramI1, I paramI2, float paramFloat1, float paramFloat2, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 123 */     float f1 = NoiseRouterData.peaksAndValleys(0.4F);
/* 124 */     float f2 = NoiseRouterData.peaksAndValleys(0.56666666F);
/*     */     
/* 126 */     float f3 = (f1 + f2) / 2.0F;
/*     */     
/* 128 */     CubicSpline.Builder builder = CubicSpline.builder((BoundedFloatFunction)paramI2, paramBoundedFloatFunction);
/*     */     
/* 130 */     builder.addPoint(f1, 0.0F);
/*     */     
/* 132 */     if (paramFloat2 > 0.0F) {
/* 133 */       builder.addPoint(f3, buildWeirdnessJaggednessSpline(paramI1, paramFloat2, paramBoundedFloatFunction));
/*     */     } else {
/* 135 */       builder.addPoint(f3, 0.0F);
/*     */     } 
/*     */     
/* 138 */     if (paramFloat1 > 0.0F) {
/* 139 */       builder.addPoint(1.0F, buildWeirdnessJaggednessSpline(paramI1, paramFloat1, paramBoundedFloatFunction));
/*     */     } else {
/* 141 */       builder.addPoint(1.0F, 0.0F);
/*     */     } 
/* 143 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildWeirdnessJaggednessSpline(I paramI, float paramFloat, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 151 */     float f1 = 0.63F * paramFloat;
/* 152 */     float f2 = 0.3F * paramFloat;
/*     */     
/* 154 */     return CubicSpline.builder((BoundedFloatFunction)paramI, paramBoundedFloatFunction)
/* 155 */       .addPoint(-0.01F, f1)
/* 156 */       .addPoint(0.01F, f2)
/* 157 */       .build();
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
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> getErosionFactor(I paramI1, I paramI2, I paramI3, float paramFloat, boolean paramBoolean, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 169 */     CubicSpline cubicSpline = CubicSpline.builder((BoundedFloatFunction)paramI2, paramBoundedFloatFunction).addPoint(-0.2F, 6.3F).addPoint(0.2F, paramFloat).build();
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
/*     */ 
/*     */ 
/*     */     
/* 186 */     CubicSpline.Builder builder = CubicSpline.builder((BoundedFloatFunction)paramI1, paramBoundedFloatFunction).addPoint(-0.6F, cubicSpline).addPoint(-0.5F, CubicSpline.builder((BoundedFloatFunction)paramI2, paramBoundedFloatFunction).addPoint(-0.05F, 6.3F).addPoint(0.05F, 2.67F).build()).addPoint(-0.35F, cubicSpline).addPoint(-0.25F, cubicSpline).addPoint(-0.1F, CubicSpline.builder((BoundedFloatFunction)paramI2, paramBoundedFloatFunction).addPoint(-0.05F, 2.67F).addPoint(0.05F, 6.3F).build()).addPoint(0.03F, cubicSpline);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (paramBoolean) {
/*     */ 
/*     */ 
/*     */       
/* 198 */       CubicSpline cubicSpline1 = CubicSpline.builder((BoundedFloatFunction)paramI2, paramBoundedFloatFunction).addPoint(0.0F, paramFloat).addPoint(0.1F, 0.625F).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 203 */       CubicSpline cubicSpline2 = CubicSpline.builder((BoundedFloatFunction)paramI3, paramBoundedFloatFunction).addPoint(-0.9F, paramFloat).addPoint(-0.69F, cubicSpline1).build();
/*     */       
/* 205 */       builder
/* 206 */         .addPoint(0.35F, paramFloat)
/* 207 */         .addPoint(0.45F, cubicSpline2)
/* 208 */         .addPoint(0.55F, cubicSpline2)
/* 209 */         .addPoint(0.62F, paramFloat);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 214 */       CubicSpline cubicSpline1 = CubicSpline.builder((BoundedFloatFunction)paramI3, paramBoundedFloatFunction).addPoint(-0.7F, cubicSpline).addPoint(-0.15F, 1.37F).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       CubicSpline cubicSpline2 = CubicSpline.builder((BoundedFloatFunction)paramI3, paramBoundedFloatFunction).addPoint(0.45F, cubicSpline).addPoint(0.7F, 1.56F).build();
/*     */       
/* 221 */       builder
/* 222 */         .addPoint(0.05F, cubicSpline2)
/* 223 */         .addPoint(0.4F, cubicSpline2)
/* 224 */         .addPoint(0.45F, cubicSpline1)
/* 225 */         .addPoint(0.55F, cubicSpline1)
/* 226 */         .addPoint(0.58F, paramFloat);
/*     */     } 
/* 228 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static float calculateSlope(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 232 */     return (paramFloat2 - paramFloat1) / (paramFloat4 - paramFloat3);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildMountainRidgeSplineWithPoints(I paramI, float paramFloat, boolean paramBoolean, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 237 */     CubicSpline.Builder builder = CubicSpline.builder((BoundedFloatFunction)paramI, paramBoundedFloatFunction);
/*     */     
/* 239 */     float f1 = -0.7F;
/* 240 */     float f2 = -1.0F;
/* 241 */     float f3 = mountainContinentalness(-1.0F, paramFloat, -0.7F);
/* 242 */     float f4 = 1.0F;
/* 243 */     float f5 = mountainContinentalness(1.0F, paramFloat, -0.7F);
/*     */     
/* 245 */     float f6 = calculateMountainRidgeZeroContinentalnessPoint(paramFloat);
/*     */     
/* 247 */     float f7 = -0.65F;
/*     */     
/* 249 */     if (-0.65F < f6 && f6 < 1.0F) {
/*     */ 
/*     */ 
/*     */       
/* 253 */       float f8 = mountainContinentalness(-0.65F, paramFloat, -0.7F);
/* 254 */       float f9 = -0.75F;
/* 255 */       float f10 = mountainContinentalness(-0.75F, paramFloat, -0.7F);
/*     */ 
/*     */       
/* 258 */       float f11 = calculateSlope(f3, f10, -1.0F, -0.75F);
/* 259 */       builder.addPoint(-1.0F, f3, f11);
/*     */ 
/*     */       
/* 262 */       builder.addPoint(-0.75F, f10);
/* 263 */       builder.addPoint(-0.65F, f8);
/*     */ 
/*     */       
/* 266 */       float f12 = mountainContinentalness(f6, paramFloat, -0.7F);
/* 267 */       float f13 = calculateSlope(f12, f5, f6, 1.0F);
/* 268 */       float f14 = 0.01F;
/* 269 */       builder.addPoint(f6 - 0.01F, f12);
/* 270 */       builder.addPoint(f6, f12, f13);
/* 271 */       builder.addPoint(1.0F, f5, f13);
/*     */     } else {
/* 273 */       float f = calculateSlope(f3, f5, -1.0F, 1.0F);
/*     */       
/* 275 */       if (paramBoolean) {
/*     */         
/* 277 */         builder.addPoint(-1.0F, Math.max(0.2F, f3));
/* 278 */         builder.addPoint(0.0F, Mth.lerp(0.5F, f3, f5), f);
/*     */       }
/*     */       else {
/*     */         
/* 282 */         builder.addPoint(-1.0F, f3, f);
/*     */       } 
/* 284 */       builder.addPoint(1.0F, f5, f);
/*     */     } 
/*     */     
/* 287 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float mountainContinentalness(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 295 */     float f1 = 1.17F;
/* 296 */     float f2 = 0.46082947F;
/* 297 */     float f3 = 1.0F - (1.0F - paramFloat2) * 0.5F;
/* 298 */     float f4 = 0.5F * (1.0F - paramFloat2);
/*     */     
/* 300 */     float f5 = (paramFloat1 + 1.17F) * 0.46082947F;
/* 301 */     float f6 = f5 * f3 - f4;
/*     */     
/* 303 */     if (paramFloat1 < paramFloat3)
/*     */     {
/*     */       
/* 306 */       return Math.max(f6, -0.2222F);
/*     */     }
/*     */     
/* 309 */     return Math.max(f6, 0.0F);
/*     */   }
/*     */   
/*     */   private static float calculateMountainRidgeZeroContinentalnessPoint(float paramFloat) {
/* 313 */     float f1 = 1.17F;
/* 314 */     float f2 = 0.46082947F;
/* 315 */     float f3 = 1.0F - (1.0F - paramFloat) * 0.5F;
/* 316 */     float f4 = 0.5F * (1.0F - paramFloat);
/*     */     
/* 318 */     return f4 / 0.46082947F * f3 - 1.17F;
/*     */   }
/*     */   
/*     */   public static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> buildErosionOffsetSpline(I paramI1, I paramI2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean1, boolean paramBoolean2, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 322 */     float f1 = 0.6F;
/*     */     
/* 324 */     float f2 = 0.5F;
/* 325 */     float f3 = 0.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     CubicSpline<?, I> cubicSpline1 = buildMountainRidgeSplineWithPoints(paramI2, Mth.lerp(paramFloat4, 0.6F, 1.5F), paramBoolean2, paramBoundedFloatFunction);
/*     */     
/* 337 */     CubicSpline<?, I> cubicSpline2 = buildMountainRidgeSplineWithPoints(paramI2, Mth.lerp(paramFloat4, 0.6F, 1.0F), paramBoolean2, paramBoundedFloatFunction);
/* 338 */     CubicSpline<?, I> cubicSpline3 = buildMountainRidgeSplineWithPoints(paramI2, paramFloat4, paramBoolean2, paramBoundedFloatFunction);
/*     */     
/* 340 */     CubicSpline<?, I> cubicSpline4 = ridgeSpline(paramI2, paramFloat1 - 0.15F, 0.5F * paramFloat4, 
/*     */ 
/*     */         
/* 343 */         Mth.lerp(0.5F, 0.5F, 0.5F) * paramFloat4, 0.5F * paramFloat4, 0.6F * paramFloat4, 0.5F, paramBoundedFloatFunction);
/*     */ 
/*     */ 
/*     */     
/* 347 */     CubicSpline<?, I> cubicSpline5 = ridgeSpline(paramI2, paramFloat1, paramFloat5 * paramFloat4, paramFloat2 * paramFloat4, 0.5F * paramFloat4, 0.6F * paramFloat4, 0.5F, paramBoundedFloatFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     CubicSpline<?, I> cubicSpline6 = ridgeSpline(paramI2, paramFloat1, paramFloat5, paramFloat5, paramFloat2, paramFloat3, 0.5F, paramBoundedFloatFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     CubicSpline<?, I> cubicSpline7 = ridgeSpline(paramI2, paramFloat1, paramFloat5, paramFloat5, paramFloat2, paramFloat3, 0.5F, paramBoundedFloatFunction);
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
/* 373 */     CubicSpline cubicSpline = CubicSpline.builder((BoundedFloatFunction)paramI2, paramBoundedFloatFunction).addPoint(-1.0F, paramFloat1).addPoint(-0.4F, cubicSpline6).addPoint(0.0F, paramFloat3 + 0.07F).build();
/*     */     
/* 375 */     CubicSpline<?, I> cubicSpline8 = ridgeSpline(paramI2, -0.02F, paramFloat6, paramFloat6, paramFloat2, paramFloat3, 0.0F, paramBoundedFloatFunction);
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
/*     */     
/* 390 */     CubicSpline.Builder builder = CubicSpline.builder((BoundedFloatFunction)paramI1, paramBoundedFloatFunction).addPoint(-0.85F, cubicSpline1).addPoint(-0.7F, cubicSpline2).addPoint(-0.4F, cubicSpline3).addPoint(-0.35F, cubicSpline4).addPoint(-0.1F, cubicSpline5).addPoint(0.2F, cubicSpline6);
/*     */     
/* 392 */     if (paramBoolean1)
/*     */     {
/*     */       
/* 395 */       builder
/* 396 */         .addPoint(0.4F, cubicSpline7)
/* 397 */         .addPoint(0.45F, cubicSpline)
/* 398 */         .addPoint(0.55F, cubicSpline)
/* 399 */         .addPoint(0.58F, cubicSpline7);
/*     */     }
/* 401 */     builder
/* 402 */       .addPoint(0.7F, cubicSpline8);
/*     */     
/* 404 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> ridgeSpline(I paramI, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, BoundedFloatFunction<Float> paramBoundedFloatFunction) {
/* 409 */     float f1 = Math.max(0.5F * (paramFloat2 - paramFloat1), paramFloat6);
/* 410 */     float f2 = 5.0F * (paramFloat3 - paramFloat2);
/* 411 */     return CubicSpline.builder((BoundedFloatFunction)paramI, paramBoundedFloatFunction)
/* 412 */       .addPoint(-1.0F, paramFloat1, f1)
/* 413 */       .addPoint(-0.4F, paramFloat2, Math.min(f1, f2))
/* 414 */       .addPoint(0.0F, paramFloat3, f2)
/* 415 */       .addPoint(0.4F, paramFloat4, 2.0F * (paramFloat4 - paramFloat3))
/* 416 */       .addPoint(1.0F, paramFloat5, 0.7F * (paramFloat5 - paramFloat4))
/* 417 */       .build();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\TerrainProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */