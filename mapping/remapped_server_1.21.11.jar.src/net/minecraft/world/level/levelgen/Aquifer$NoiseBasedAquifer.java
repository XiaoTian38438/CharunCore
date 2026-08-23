/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.OverworldBiomeBuilder;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.apache.commons.lang3.mutable.MutableDouble;
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
/*     */ public class NoiseBasedAquifer
/*     */   implements Aquifer
/*     */ {
/*     */   private static final int X_RANGE = 10;
/*     */   private static final int Y_RANGE = 9;
/*     */   private static final int Z_RANGE = 10;
/*     */   private static final int X_SEPARATION = 6;
/*     */   private static final int Y_SEPARATION = 3;
/*     */   private static final int Z_SEPARATION = 6;
/*     */   private static final int X_SPACING = 16;
/*     */   private static final int Y_SPACING = 12;
/*     */   private static final int Z_SPACING = 16;
/*     */   private static final int X_SPACING_SHIFT = 4;
/*     */   private static final int Z_SPACING_SHIFT = 4;
/*     */   private static final int MAX_REASONABLE_DISTANCE_TO_AQUIFER_CENTER = 11;
/* 104 */   private static final double FLOWING_UPDATE_SIMULARITY = similarity(
/* 105 */       Mth.square(10), 
/* 106 */       Mth.square(12));
/*     */ 
/*     */   
/*     */   private static final int SAMPLE_OFFSET_X = -5;
/*     */ 
/*     */   
/*     */   private static final int SAMPLE_OFFSET_Y = 1;
/*     */ 
/*     */   
/*     */   private static final int SAMPLE_OFFSET_Z = -5;
/*     */ 
/*     */   
/*     */   private static final int MIN_CELL_SAMPLE_X = 0;
/*     */ 
/*     */   
/*     */   private static final int MIN_CELL_SAMPLE_Y = -1;
/*     */   
/*     */   private static final int MIN_CELL_SAMPLE_Z = 0;
/*     */   
/*     */   private static final int MAX_CELL_SAMPLE_X = 1;
/*     */   
/*     */   private static final int MAX_CELL_SAMPLE_Y = 1;
/*     */   
/*     */   private static final int MAX_CELL_SAMPLE_Z = 1;
/*     */   
/*     */   private final NoiseChunk noiseChunk;
/*     */   
/*     */   private final DensityFunction barrierNoise;
/*     */   
/*     */   private final DensityFunction fluidLevelFloodednessNoise;
/*     */   
/*     */   private final DensityFunction fluidLevelSpreadNoise;
/*     */   
/*     */   private final DensityFunction lavaNoise;
/*     */   
/*     */   private final PositionalRandomFactory positionalRandomFactory;
/*     */   
/*     */   private final Aquifer.FluidStatus[] aquiferCache;
/*     */   
/*     */   private final long[] aquiferLocationCache;
/*     */   
/*     */   private final Aquifer.FluidPicker globalFluidPicker;
/*     */   
/*     */   private final DensityFunction erosion;
/*     */   
/*     */   private final DensityFunction depth;
/*     */   
/*     */   private boolean shouldScheduleFluidUpdate;
/*     */   
/*     */   private final int skipSamplingAboveY;
/*     */   
/*     */   private final int minGridX;
/*     */   
/*     */   private final int minGridY;
/*     */   
/*     */   private final int minGridZ;
/*     */   
/*     */   private final int gridSizeX;
/*     */   
/*     */   private final int gridSizeZ;
/*     */   
/* 167 */   private static final int[][] SURFACE_SAMPLING_OFFSETS_IN_CHUNKS = new int[][] { { 0, 0 }, { -2, -1 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { -3, 0 }, { -2, 0 }, { -1, 0 }, { 1, 0 }, { -2, 1 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NoiseBasedAquifer(NoiseChunk paramNoiseChunk, ChunkPos paramChunkPos, NoiseRouter paramNoiseRouter, PositionalRandomFactory paramPositionalRandomFactory, int paramInt1, int paramInt2, Aquifer.FluidPicker paramFluidPicker) {
/* 175 */     this.noiseChunk = paramNoiseChunk;
/* 176 */     this.barrierNoise = paramNoiseRouter.barrierNoise();
/* 177 */     this.fluidLevelFloodednessNoise = paramNoiseRouter.fluidLevelFloodednessNoise();
/* 178 */     this.fluidLevelSpreadNoise = paramNoiseRouter.fluidLevelSpreadNoise();
/* 179 */     this.lavaNoise = paramNoiseRouter.lavaNoise();
/* 180 */     this.erosion = paramNoiseRouter.erosion();
/* 181 */     this.depth = paramNoiseRouter.depth();
/*     */     
/* 183 */     this.positionalRandomFactory = paramPositionalRandomFactory;
/*     */     
/* 185 */     this.minGridX = gridX(paramChunkPos.getMinBlockX() + -5) + 0;
/* 186 */     this.globalFluidPicker = paramFluidPicker;
/* 187 */     int i = gridX(paramChunkPos.getMaxBlockX() + -5) + 1;
/* 188 */     this.gridSizeX = i - this.minGridX + 1;
/*     */     
/* 190 */     this.minGridY = gridY(paramInt1 + 1) + -1;
/* 191 */     int j = gridY(paramInt1 + paramInt2 + 1) + 1;
/* 192 */     int k = j - this.minGridY + 1;
/*     */     
/* 194 */     this.minGridZ = gridZ(paramChunkPos.getMinBlockZ() + -5) + 0;
/* 195 */     int m = gridZ(paramChunkPos.getMaxBlockZ() + -5) + 1;
/* 196 */     this.gridSizeZ = m - this.minGridZ + 1;
/* 197 */     int n = this.gridSizeX * k * this.gridSizeZ;
/*     */     
/* 199 */     this.aquiferCache = new Aquifer.FluidStatus[n];
/*     */     
/* 201 */     this.aquiferLocationCache = new long[n];
/* 202 */     Arrays.fill(this.aquiferLocationCache, Long.MAX_VALUE);
/*     */     
/* 204 */     int i1 = adjustSurfaceLevel(paramNoiseChunk.maxPreliminarySurfaceLevel(
/* 205 */           fromGridX(this.minGridX, 0), 
/* 206 */           fromGridZ(this.minGridZ, 0), 
/* 207 */           fromGridX(i, 9), 
/* 208 */           fromGridZ(m, 9)));
/*     */     
/* 210 */     int i2 = gridY(i1 + 12) - -1;
/* 211 */     this.skipSamplingAboveY = fromGridY(i2, 11) - 1;
/*     */   }
/*     */   
/*     */   private int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 215 */     int i = paramInt1 - this.minGridX;
/* 216 */     int j = paramInt2 - this.minGridY;
/* 217 */     int k = paramInt3 - this.minGridZ;
/*     */     
/* 219 */     return (j * this.gridSizeZ + k) * this.gridSizeX + i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState computeSubstance(DensityFunction.FunctionContext paramFunctionContext, double paramDouble) {
/* 227 */     if (paramDouble > 0.0D) {
/* 228 */       this.shouldScheduleFluidUpdate = false;
/* 229 */       return null;
/*     */     } 
/*     */     
/* 232 */     int i = paramFunctionContext.blockX();
/* 233 */     int j = paramFunctionContext.blockY();
/* 234 */     int k = paramFunctionContext.blockZ();
/*     */     
/* 236 */     Aquifer.FluidStatus fluidStatus1 = this.globalFluidPicker.computeFluid(i, j, k);
/*     */ 
/*     */     
/* 239 */     if (j > this.skipSamplingAboveY) {
/* 240 */       this.shouldScheduleFluidUpdate = false;
/* 241 */       return fluidStatus1.at(j);
/*     */     } 
/*     */     
/* 244 */     if (fluidStatus1.at(j).is(Blocks.LAVA)) {
/* 245 */       this.shouldScheduleFluidUpdate = false;
/* 246 */       return SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? Blocks.AIR.defaultBlockState() : Blocks.LAVA.defaultBlockState();
/*     */     } 
/*     */     
/* 249 */     int m = gridX(i + -5);
/* 250 */     int n = gridY(j + 1);
/* 251 */     int i1 = gridZ(k + -5);
/*     */ 
/*     */     
/* 254 */     int i2 = Integer.MAX_VALUE;
/* 255 */     int i3 = Integer.MAX_VALUE;
/* 256 */     int i4 = Integer.MAX_VALUE;
/* 257 */     int i5 = Integer.MAX_VALUE;
/*     */     
/* 259 */     int i6 = 0;
/* 260 */     int i7 = 0;
/* 261 */     int i8 = 0;
/* 262 */     int i9 = 0;
/*     */     
/* 264 */     for (byte b = 0; b <= 1; b++) {
/* 265 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 266 */         for (byte b2 = 0; b2 <= 1; b2++) {
/* 267 */           long l1; int i10 = m + b;
/* 268 */           int i11 = n + b1;
/* 269 */           int i12 = i1 + b2;
/*     */           
/* 271 */           int i13 = getIndex(i10, i11, i12);
/*     */ 
/*     */           
/* 274 */           long l2 = this.aquiferLocationCache[i13];
/* 275 */           if (l2 != Long.MAX_VALUE) {
/* 276 */             l1 = l2;
/*     */           } else {
/* 278 */             RandomSource randomSource = this.positionalRandomFactory.at(i10, i11, i12);
/*     */             
/* 280 */             l1 = BlockPos.asLong(
/* 281 */                 fromGridX(i10, randomSource.nextInt(10)), 
/* 282 */                 fromGridY(i11, randomSource.nextInt(9)), 
/* 283 */                 fromGridZ(i12, randomSource.nextInt(10)));
/*     */             
/* 285 */             this.aquiferLocationCache[i13] = l1;
/*     */           } 
/*     */           
/* 288 */           int i14 = BlockPos.getX(l1) - i;
/* 289 */           int i15 = BlockPos.getY(l1) - j;
/* 290 */           int i16 = BlockPos.getZ(l1) - k;
/* 291 */           int i17 = i14 * i14 + i15 * i15 + i16 * i16;
/*     */ 
/*     */           
/* 294 */           if (i2 >= i17) {
/* 295 */             i9 = i8;
/* 296 */             i8 = i7;
/* 297 */             i7 = i6;
/* 298 */             i6 = i13;
/*     */             
/* 300 */             i5 = i4;
/* 301 */             i4 = i3;
/* 302 */             i3 = i2;
/* 303 */             i2 = i17;
/* 304 */           } else if (i3 >= i17) {
/* 305 */             i9 = i8;
/* 306 */             i8 = i7;
/* 307 */             i7 = i13;
/*     */             
/* 309 */             i5 = i4;
/* 310 */             i4 = i3;
/* 311 */             i3 = i17;
/* 312 */           } else if (i4 >= i17) {
/* 313 */             i9 = i8;
/* 314 */             i8 = i13;
/*     */             
/* 316 */             i5 = i4;
/* 317 */             i4 = i17;
/* 318 */           } else if (i5 >= i17) {
/* 319 */             i9 = i13;
/*     */             
/* 321 */             i5 = i17;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     Aquifer.FluidStatus fluidStatus2 = getAquiferStatus(i6);
/*     */     
/* 334 */     double d1 = similarity(i2, i3);
/*     */     
/* 336 */     BlockState blockState1 = fluidStatus2.at(j);
/* 337 */     BlockState blockState2 = SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? Blocks.AIR.defaultBlockState() : blockState1;
/*     */ 
/*     */ 
/*     */     
/* 341 */     if (d1 <= 0.0D) {
/* 342 */       if (d1 >= FLOWING_UPDATE_SIMULARITY) {
/* 343 */         Aquifer.FluidStatus fluidStatus = getAquiferStatus(i7);
/* 344 */         this.shouldScheduleFluidUpdate = !fluidStatus2.equals(fluidStatus);
/*     */       } else {
/* 346 */         this.shouldScheduleFluidUpdate = false;
/*     */       } 
/* 348 */       return blockState2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 353 */     if (blockState1.is(Blocks.WATER) && this.globalFluidPicker.computeFluid(i, j - 1, k).at(j - 1).is(Blocks.LAVA)) {
/* 354 */       this.shouldScheduleFluidUpdate = true;
/* 355 */       return blockState2;
/*     */     } 
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
/* 372 */     MutableDouble mutableDouble = new MutableDouble(Double.NaN);
/* 373 */     Aquifer.FluidStatus fluidStatus3 = getAquiferStatus(i7);
/*     */ 
/*     */     
/* 376 */     double d2 = d1 * calculatePressure(paramFunctionContext, mutableDouble, fluidStatus2, fluidStatus3);
/* 377 */     if (paramDouble + d2 > 0.0D) {
/* 378 */       this.shouldScheduleFluidUpdate = false;
/* 379 */       return null;
/*     */     } 
/*     */     
/* 382 */     Aquifer.FluidStatus fluidStatus4 = getAquiferStatus(i8);
/*     */     
/* 384 */     double d3 = similarity(i2, i4);
/* 385 */     if (d3 > 0.0D) {
/*     */       
/* 387 */       double d = d1 * d3 * calculatePressure(paramFunctionContext, mutableDouble, fluidStatus2, fluidStatus4);
/* 388 */       if (paramDouble + d > 0.0D) {
/* 389 */         this.shouldScheduleFluidUpdate = false;
/* 390 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     double d4 = similarity(i3, i4);
/* 395 */     if (d4 > 0.0D) {
/*     */       
/* 397 */       double d = d1 * d4 * calculatePressure(paramFunctionContext, mutableDouble, fluidStatus3, fluidStatus4);
/* 398 */       if (paramDouble + d > 0.0D) {
/* 399 */         this.shouldScheduleFluidUpdate = false;
/* 400 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 405 */     boolean bool1 = !fluidStatus2.equals(fluidStatus3) ? true : false;
/* 406 */     boolean bool2 = (d4 >= FLOWING_UPDATE_SIMULARITY && !fluidStatus3.equals(fluidStatus4)) ? true : false;
/* 407 */     boolean bool3 = (d3 >= FLOWING_UPDATE_SIMULARITY && !fluidStatus2.equals(fluidStatus4)) ? true : false;
/* 408 */     if (bool1 || bool2 || bool3) {
/* 409 */       this.shouldScheduleFluidUpdate = true;
/*     */     } else {
/*     */       
/* 412 */       this
/*     */         
/* 414 */         .shouldScheduleFluidUpdate = (d3 >= FLOWING_UPDATE_SIMULARITY && similarity(i2, i5) >= FLOWING_UPDATE_SIMULARITY && !fluidStatus2.equals(getAquiferStatus(i9)));
/*     */     } 
/*     */     
/* 417 */     return blockState2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldScheduleFluidUpdate() {
/* 422 */     return this.shouldScheduleFluidUpdate;
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
/*     */   private static double similarity(int paramInt1, int paramInt2) {
/* 435 */     double d = 25.0D;
/*     */ 
/*     */     
/* 438 */     return 1.0D - (paramInt2 - paramInt1) / 25.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double calculatePressure(DensityFunction.FunctionContext paramFunctionContext, MutableDouble paramMutableDouble, Aquifer.FluidStatus paramFluidStatus1, Aquifer.FluidStatus paramFluidStatus2) {
/*     */     double d11, d13;
/* 446 */     int i = paramFunctionContext.blockY();
/* 447 */     BlockState blockState1 = paramFluidStatus1.at(i);
/* 448 */     BlockState blockState2 = paramFluidStatus2.at(i);
/*     */     
/* 450 */     if ((blockState1.is(Blocks.LAVA) && blockState2.is(Blocks.WATER)) || (blockState1.is(Blocks.WATER) && blockState2.is(Blocks.LAVA)))
/*     */     {
/* 452 */       return 2.0D;
/*     */     }
/*     */ 
/*     */     
/* 456 */     int j = Math.abs(paramFluidStatus1.fluidLevel - paramFluidStatus2.fluidLevel);
/*     */     
/* 458 */     if (j == 0) {
/* 459 */       return 0.0D;
/*     */     }
/*     */     
/* 462 */     double d1 = 0.5D * (paramFluidStatus1.fluidLevel + paramFluidStatus2.fluidLevel);
/*     */ 
/*     */     
/* 465 */     double d2 = i + 0.5D - d1;
/*     */     
/* 467 */     double d3 = j / 2.0D;
/*     */ 
/*     */ 
/*     */     
/* 471 */     double d4 = 0.0D;
/*     */     
/* 473 */     double d5 = 2.5D;
/*     */     
/* 475 */     double d6 = 1.5D;
/*     */ 
/*     */     
/* 478 */     double d7 = 3.0D;
/* 479 */     double d8 = 10.0D;
/* 480 */     double d9 = 3.0D;
/*     */ 
/*     */ 
/*     */     
/* 484 */     double d10 = d3 - Math.abs(d2);
/*     */ 
/*     */     
/* 487 */     if (d2 > 0.0D) {
/*     */       
/* 489 */       double d = 0.0D + d10;
/* 490 */       if (d > 0.0D) {
/*     */         
/* 492 */         d11 = d / 1.5D;
/*     */       } else {
/*     */         
/* 495 */         d11 = d / 2.5D;
/*     */       } 
/*     */     } else {
/*     */       
/* 499 */       double d = 3.0D + d10;
/* 500 */       if (d > 0.0D) {
/*     */         
/* 502 */         d11 = d / 3.0D;
/*     */       } else {
/*     */         
/* 505 */         d11 = d / 10.0D;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 510 */     double d12 = 2.0D;
/*     */ 
/*     */ 
/*     */     
/* 514 */     if (d11 < -2.0D || d11 > 2.0D) {
/* 515 */       d13 = 0.0D;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 520 */       double d = paramMutableDouble.doubleValue();
/* 521 */       if (Double.isNaN(d)) {
/* 522 */         double d14 = this.barrierNoise.compute(paramFunctionContext);
/* 523 */         paramMutableDouble.setValue(d14);
/* 524 */         d13 = d14;
/*     */       } else {
/* 526 */         d13 = d;
/*     */       } 
/*     */     } 
/*     */     
/* 530 */     return 2.0D * (d13 + d11);
/*     */   }
/*     */   
/*     */   private static int gridX(int paramInt) {
/* 534 */     return paramInt >> 4;
/*     */   }
/*     */   
/*     */   private static int fromGridX(int paramInt1, int paramInt2) {
/* 538 */     return (paramInt1 << 4) + paramInt2;
/*     */   }
/*     */   
/*     */   private static int gridY(int paramInt) {
/* 542 */     return Math.floorDiv(paramInt, 12);
/*     */   }
/*     */   
/*     */   private static int fromGridY(int paramInt1, int paramInt2) {
/* 546 */     return paramInt1 * 12 + paramInt2;
/*     */   }
/*     */   
/*     */   private static int gridZ(int paramInt) {
/* 550 */     return paramInt >> 4;
/*     */   }
/*     */   
/*     */   private static int fromGridZ(int paramInt1, int paramInt2) {
/* 554 */     return (paramInt1 << 4) + paramInt2;
/*     */   }
/*     */   
/*     */   private Aquifer.FluidStatus getAquiferStatus(int paramInt) {
/* 558 */     Aquifer.FluidStatus fluidStatus1 = this.aquiferCache[paramInt];
/* 559 */     if (fluidStatus1 != null) {
/* 560 */       return fluidStatus1;
/*     */     }
/* 562 */     long l = this.aquiferLocationCache[paramInt];
/* 563 */     Aquifer.FluidStatus fluidStatus2 = computeFluid(BlockPos.getX(l), BlockPos.getY(l), BlockPos.getZ(l));
/* 564 */     this.aquiferCache[paramInt] = fluidStatus2;
/* 565 */     return fluidStatus2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Aquifer.FluidStatus computeFluid(int paramInt1, int paramInt2, int paramInt3) {
/* 573 */     Aquifer.FluidStatus fluidStatus = this.globalFluidPicker.computeFluid(paramInt1, paramInt2, paramInt3);
/*     */     
/* 575 */     int i = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 582 */     int j = paramInt2 + 12;
/* 583 */     int k = paramInt2 - 12;
/* 584 */     boolean bool = false;
/* 585 */     for (int[] arrayOfInt : SURFACE_SAMPLING_OFFSETS_IN_CHUNKS) {
/* 586 */       int n = paramInt1 + SectionPos.sectionToBlockCoord(arrayOfInt[0]);
/* 587 */       int i1 = paramInt3 + SectionPos.sectionToBlockCoord(arrayOfInt[1]);
/* 588 */       int i2 = this.noiseChunk.preliminarySurfaceLevel(n, i1);
/*     */ 
/*     */       
/* 591 */       int i3 = adjustSurfaceLevel(i2);
/*     */       
/* 593 */       boolean bool1 = (arrayOfInt[0] == 0 && arrayOfInt[1] == 0) ? true : false;
/*     */       
/* 595 */       if (bool1 && k > i3)
/*     */       {
/*     */         
/* 598 */         return fluidStatus;
/*     */       }
/*     */       
/* 601 */       boolean bool2 = (j > i3) ? true : false;
/* 602 */       if (bool2 || bool1) {
/* 603 */         Aquifer.FluidStatus fluidStatus1 = this.globalFluidPicker.computeFluid(n, i3, i1);
/* 604 */         if (!fluidStatus1.at(i3).isAir()) {
/* 605 */           if (bool1) {
/* 606 */             bool = true;
/*     */           }
/* 608 */           if (bool2)
/*     */           {
/* 610 */             return fluidStatus1;
/*     */           }
/*     */         } 
/*     */       } 
/* 614 */       i = Math.min(i, i2);
/*     */     } 
/*     */     
/* 617 */     int m = computeSurfaceLevel(paramInt1, paramInt2, paramInt3, fluidStatus, i, bool);
/*     */     
/* 619 */     return new Aquifer.FluidStatus(m, computeFluidType(paramInt1, paramInt2, paramInt3, fluidStatus, m));
/*     */   }
/*     */   
/*     */   private int adjustSurfaceLevel(int paramInt) {
/* 623 */     return paramInt + 8;
/*     */   } private int computeSurfaceLevel(int paramInt1, int paramInt2, int paramInt3, Aquifer.FluidStatus paramFluidStatus, int paramInt4, boolean paramBoolean) {
/*     */     double d1, d2;
/*     */     int i;
/* 627 */     DensityFunction.SinglePointContext singlePointContext = new DensityFunction.SinglePointContext(paramInt1, paramInt2, paramInt3);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 632 */     if (OverworldBiomeBuilder.isDeepDarkRegion(this.erosion, this.depth, singlePointContext)) {
/* 633 */       d1 = -1.0D;
/* 634 */       d2 = -1.0D;
/*     */     } else {
/* 636 */       i = paramInt4 + 8 - paramInt2;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 641 */       byte b = 64;
/* 642 */       double d3 = paramBoolean ? Mth.clampedMap(i, 0.0D, 64.0D, 1.0D, 0.0D) : 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 647 */       double d4 = Mth.clamp(this.fluidLevelFloodednessNoise.compute(singlePointContext), -1.0D, 1.0D);
/*     */ 
/*     */ 
/*     */       
/* 651 */       double d5 = Mth.map(d3, 1.0D, 0.0D, -0.3D, 0.8D);
/*     */ 
/*     */ 
/*     */       
/* 655 */       double d6 = Mth.map(d3, 1.0D, 0.0D, -0.8D, 0.4D);
/*     */       
/* 657 */       d1 = d4 - d6;
/* 658 */       d2 = d4 - d5;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 664 */     if (d2 > 0.0D) {
/*     */       
/* 666 */       i = paramFluidStatus.fluidLevel;
/* 667 */     } else if (d1 > 0.0D) {
/* 668 */       i = computeRandomizedFluidSurfaceLevel(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     } else {
/*     */       
/* 671 */       i = DimensionType.WAY_BELOW_MIN_Y;
/*     */     } 
/* 673 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int computeRandomizedFluidSurfaceLevel(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 682 */     byte b1 = 16;
/* 683 */     byte b2 = 40;
/* 684 */     int i = Math.floorDiv(paramInt1, 16);
/* 685 */     int j = Math.floorDiv(paramInt2, 40);
/* 686 */     int k = Math.floorDiv(paramInt3, 16);
/* 687 */     int m = j * 40 + 20;
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
/* 698 */     byte b3 = 10;
/* 699 */     double d = this.fluidLevelSpreadNoise.compute(new DensityFunction.SinglePointContext(i, j, k)) * 10.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 704 */     int n = Mth.quantize(d, 3);
/*     */     
/* 706 */     int i1 = m + n;
/*     */ 
/*     */     
/* 709 */     return Math.min(paramInt4, i1);
/*     */   }
/*     */   
/*     */   private BlockState computeFluidType(int paramInt1, int paramInt2, int paramInt3, Aquifer.FluidStatus paramFluidStatus, int paramInt4) {
/* 713 */     BlockState blockState = paramFluidStatus.fluidType;
/*     */ 
/*     */     
/* 716 */     if (paramInt4 <= -10 && paramInt4 != DimensionType.WAY_BELOW_MIN_Y && paramFluidStatus.fluidType != Blocks.LAVA.defaultBlockState()) {
/* 717 */       byte b1 = 64;
/* 718 */       byte b2 = 40;
/*     */       
/* 720 */       int i = Math.floorDiv(paramInt1, 64);
/* 721 */       int j = Math.floorDiv(paramInt2, 40);
/* 722 */       int k = Math.floorDiv(paramInt3, 64);
/*     */       
/* 724 */       double d = this.lavaNoise.compute(new DensityFunction.SinglePointContext(i, j, k));
/* 725 */       if (Math.abs(d) > 0.3D) {
/* 726 */         blockState = Blocks.LAVA.defaultBlockState();
/*     */       }
/*     */     } 
/* 729 */     return blockState;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\Aquifer$NoiseBasedAquifer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */