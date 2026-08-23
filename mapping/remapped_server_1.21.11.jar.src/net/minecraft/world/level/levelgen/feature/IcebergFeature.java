/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ 
/*     */ public class IcebergFeature extends Feature<BlockStateConfiguration> {
/*     */   public IcebergFeature(Codec<BlockStateConfiguration> paramCodec) {
/*  16 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<BlockStateConfiguration> paramFeaturePlaceContext) {
/*  21 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  22 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  23 */     blockPos = new BlockPos(blockPos.getX(), paramFeaturePlaceContext.chunkGenerator().getSeaLevel(), blockPos.getZ());
/*  24 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  25 */     boolean bool1 = (randomSource.nextDouble() > 0.7D) ? true : false;
/*  26 */     BlockState blockState = ((BlockStateConfiguration)paramFeaturePlaceContext.config()).state;
/*     */ 
/*     */     
/*  29 */     double d = randomSource.nextDouble() * 2.0D * Math.PI;
/*  30 */     int i = 11 - randomSource.nextInt(5);
/*  31 */     int j = 3 + randomSource.nextInt(3);
/*  32 */     boolean bool2 = (randomSource.nextDouble() > 0.7D) ? true : false;
/*     */     
/*  34 */     byte b1 = 11;
/*  35 */     int k = bool2 ? (randomSource.nextInt(6) + 6) : (randomSource.nextInt(15) + 3);
/*  36 */     if (!bool2 && randomSource.nextDouble() > 0.9D) {
/*  37 */       k += randomSource.nextInt(19) + 7;
/*     */     }
/*     */     
/*  40 */     int m = Math.min(k + randomSource.nextInt(11), 18);
/*  41 */     int n = Math.min(k + randomSource.nextInt(7) - randomSource.nextInt(5), 11);
/*  42 */     byte b2 = bool2 ? i : 11;
/*     */     
/*     */     byte b3;
/*  45 */     for (b3 = -b2; b3 < b2; b3++) {
/*  46 */       for (byte b = -b2; b < b2; b++) {
/*  47 */         for (byte b4 = 0; b4 < k; b4++) {
/*  48 */           int i1 = bool2 ? heightDependentRadiusEllipse(b4, k, n) : heightDependentRadiusRound(randomSource, b4, k, n);
/*  49 */           if (bool2 || b3 < i1)
/*     */           {
/*     */             
/*  52 */             generateIcebergBlock((LevelAccessor)worldGenLevel, randomSource, blockPos, k, b3, b4, b, i1, b2, bool2, j, d, bool1, blockState);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  58 */     smooth((LevelAccessor)worldGenLevel, blockPos, n, k, bool2, i);
/*     */ 
/*     */     
/*  61 */     for (b3 = -b2; b3 < b2; b3++) {
/*  62 */       for (byte b = -b2; b < b2; b++) {
/*  63 */         for (byte b4 = -1; b4 > -m; b4--) {
/*  64 */           boolean bool = bool2 ? Mth.ceil(b2 * (1.0F - (float)Math.pow(b4, 2.0D) / m * 8.0F)) : b2;
/*  65 */           int i1 = heightDependentRadiusSteep(randomSource, -b4, m, n);
/*  66 */           if (b3 < i1)
/*     */           {
/*     */             
/*  69 */             generateIcebergBlock((LevelAccessor)worldGenLevel, randomSource, blockPos, m, b3, b4, b, i1, bool, bool2, j, d, bool1, blockState);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     b3 = bool2 ? ((randomSource.nextDouble() > 0.1D) ? 1 : 0) : ((randomSource.nextDouble() > 0.7D) ? 1 : 0);
/*  76 */     if (b3 != 0) {
/*  77 */       generateCutOut(randomSource, (LevelAccessor)worldGenLevel, n, k, blockPos, bool2, i, d, j);
/*     */     }
/*     */     
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   private void generateCutOut(RandomSource paramRandomSource, LevelAccessor paramLevelAccessor, int paramInt1, int paramInt2, BlockPos paramBlockPos, boolean paramBoolean, int paramInt3, double paramDouble, int paramInt4) {
/*  84 */     byte b1 = paramRandomSource.nextBoolean() ? -1 : 1;
/*  85 */     byte b2 = paramRandomSource.nextBoolean() ? -1 : 1;
/*     */     
/*  87 */     int i = paramRandomSource.nextInt(Math.max(paramInt1 / 2 - 2, 1));
/*  88 */     if (paramRandomSource.nextBoolean()) {
/*  89 */       i = paramInt1 / 2 + 1 - paramRandomSource.nextInt(Math.max(paramInt1 - paramInt1 / 2 - 1, 1));
/*     */     }
/*  91 */     int j = paramRandomSource.nextInt(Math.max(paramInt1 / 2 - 2, 1));
/*  92 */     if (paramRandomSource.nextBoolean()) {
/*  93 */       j = paramInt1 / 2 + 1 - paramRandomSource.nextInt(Math.max(paramInt1 - paramInt1 / 2 - 1, 1));
/*     */     }
/*     */     
/*  96 */     if (paramBoolean) {
/*  97 */       i = j = paramRandomSource.nextInt(Math.max(paramInt3 - 5, 1));
/*     */     }
/*     */     
/* 100 */     BlockPos blockPos = new BlockPos(b1 * i, 0, b2 * j);
/* 101 */     double d = paramBoolean ? (paramDouble + 1.5707963267948966D) : (paramRandomSource.nextDouble() * 2.0D * Math.PI);
/*     */     byte b;
/* 103 */     for (b = 0; b < paramInt2 - 3; b++) {
/* 104 */       int k = heightDependentRadiusRound(paramRandomSource, b, paramInt2, paramInt1);
/* 105 */       carve(k, b, paramBlockPos, paramLevelAccessor, false, d, blockPos, paramInt3, paramInt4);
/*     */     } 
/*     */     
/* 108 */     for (b = -1; b > -paramInt2 + paramRandomSource.nextInt(5); b--) {
/* 109 */       int k = heightDependentRadiusSteep(paramRandomSource, -b, paramInt2, paramInt1);
/* 110 */       carve(k, b, paramBlockPos, paramLevelAccessor, true, d, blockPos, paramInt3, paramInt4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void carve(int paramInt1, int paramInt2, BlockPos paramBlockPos1, LevelAccessor paramLevelAccessor, boolean paramBoolean, double paramDouble, BlockPos paramBlockPos2, int paramInt3, int paramInt4) {
/* 115 */     int i = paramInt1 + 1 + paramInt3 / 3;
/* 116 */     int j = Math.min(paramInt1 - 3, 3) + paramInt4 / 2 - 1;
/*     */     
/* 118 */     for (int k = -i; k < i; k++) {
/* 119 */       for (int m = -i; m < i; m++) {
/* 120 */         double d = signedDistanceEllipse(k, m, paramBlockPos2, i, j, paramDouble);
/* 121 */         if (d < 0.0D) {
/* 122 */           BlockPos blockPos = paramBlockPos1.offset(k, paramInt2, m);
/* 123 */           BlockState blockState = paramLevelAccessor.getBlockState(blockPos);
/* 124 */           if (isIcebergState(blockState) || blockState.is(Blocks.SNOW_BLOCK)) {
/* 125 */             if (paramBoolean) {
/* 126 */               setBlock((LevelWriter)paramLevelAccessor, blockPos, Blocks.WATER.defaultBlockState());
/*     */             } else {
/* 128 */               setBlock((LevelWriter)paramLevelAccessor, blockPos, Blocks.AIR.defaultBlockState());
/* 129 */               removeFloatingSnowLayer(paramLevelAccessor, blockPos);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFloatingSnowLayer(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 138 */     if (paramLevelAccessor.getBlockState(paramBlockPos.above()).is(Blocks.SNOW)) {
/* 139 */       setBlock((LevelWriter)paramLevelAccessor, paramBlockPos.above(), Blocks.AIR.defaultBlockState());
/*     */     }
/*     */   }
/*     */   
/*     */   private void generateIcebergBlock(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, int paramInt7, double paramDouble, boolean paramBoolean2, BlockState paramBlockState) {
/* 144 */     double d = paramBoolean1 ? signedDistanceEllipse(paramInt2, paramInt4, BlockPos.ZERO, paramInt6, getEllipseC(paramInt3, paramInt1, paramInt7), paramDouble) : signedDistanceCircle(paramInt2, paramInt4, BlockPos.ZERO, paramInt5, paramRandomSource);
/* 145 */     if (d < 0.0D) {
/* 146 */       BlockPos blockPos = paramBlockPos.offset(paramInt2, paramInt3, paramInt4);
/* 147 */       double d1 = paramBoolean1 ? -0.5D : (-6 - paramRandomSource.nextInt(3));
/* 148 */       if (d > d1 && paramRandomSource.nextDouble() > 0.9D) {
/*     */         return;
/*     */       }
/* 151 */       setIcebergBlock(blockPos, paramLevelAccessor, paramRandomSource, paramInt1 - paramInt3, paramInt1, paramBoolean1, paramBoolean2, paramBlockState);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setIcebergBlock(BlockPos paramBlockPos, LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, BlockState paramBlockState) {
/* 156 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/* 157 */     if (blockState.isAir() || blockState.is(Blocks.SNOW_BLOCK) || blockState.is(Blocks.ICE) || blockState.is(Blocks.WATER)) {
/* 158 */       boolean bool = (!paramBoolean1 || paramRandomSource.nextDouble() > 0.05D) ? true : false;
/* 159 */       byte b = paramBoolean1 ? 3 : 2;
/* 160 */       if (paramBoolean2 && !blockState.is(Blocks.WATER) && paramInt1 <= paramRandomSource.nextInt(Math.max(1, paramInt2 / b)) + paramInt2 * 0.6D && bool) {
/* 161 */         setBlock((LevelWriter)paramLevelAccessor, paramBlockPos, Blocks.SNOW_BLOCK.defaultBlockState());
/*     */       } else {
/* 163 */         setBlock((LevelWriter)paramLevelAccessor, paramBlockPos, paramBlockState);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getEllipseC(int paramInt1, int paramInt2, int paramInt3) {
/* 169 */     int i = paramInt3;
/* 170 */     if (paramInt1 > 0 && paramInt2 - paramInt1 <= 3) {
/* 171 */       i -= 4 - paramInt2 - paramInt1;
/*     */     }
/*     */     
/* 174 */     return i;
/*     */   }
/*     */   
/*     */   private double signedDistanceCircle(int paramInt1, int paramInt2, BlockPos paramBlockPos, int paramInt3, RandomSource paramRandomSource) {
/* 178 */     float f = 10.0F * Mth.clamp(paramRandomSource.nextFloat(), 0.2F, 0.8F) / paramInt3;
/* 179 */     return f + Math.pow((paramInt1 - paramBlockPos.getX()), 2.0D) + Math.pow((paramInt2 - paramBlockPos.getZ()), 2.0D) - Math.pow(paramInt3, 2.0D);
/*     */   }
/*     */   
/*     */   private double signedDistanceEllipse(int paramInt1, int paramInt2, BlockPos paramBlockPos, int paramInt3, int paramInt4, double paramDouble) {
/* 183 */     return Math.pow(((paramInt1 - paramBlockPos.getX()) * Math.cos(paramDouble) - (paramInt2 - paramBlockPos.getZ()) * Math.sin(paramDouble)) / paramInt3, 2.0D) + Math.pow(((paramInt1 - paramBlockPos.getX()) * Math.sin(paramDouble) + (paramInt2 - paramBlockPos.getZ()) * Math.cos(paramDouble)) / paramInt4, 2.0D) - 1.0D;
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusRound(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3) {
/* 187 */     float f1 = 3.5F - paramRandomSource.nextFloat();
/* 188 */     float f2 = (1.0F - (float)Math.pow(paramInt1, 2.0D) / paramInt2 * f1) * paramInt3;
/*     */     
/* 190 */     if (paramInt2 > 15 + paramRandomSource.nextInt(5)) {
/* 191 */       int i = (paramInt1 < 3 + paramRandomSource.nextInt(6)) ? (paramInt1 / 2) : paramInt1;
/* 192 */       f2 = (1.0F - i / paramInt2 * f1 * 0.4F) * paramInt3;
/*     */     } 
/*     */     
/* 195 */     return Mth.ceil(f2 / 2.0F);
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusEllipse(int paramInt1, int paramInt2, int paramInt3) {
/* 199 */     float f1 = 1.0F;
/* 200 */     float f2 = (1.0F - (float)Math.pow(paramInt1, 2.0D) / paramInt2 * 1.0F) * paramInt3;
/* 201 */     return Mth.ceil(f2 / 2.0F);
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusSteep(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3) {
/* 205 */     float f1 = 1.0F + paramRandomSource.nextFloat() / 2.0F;
/* 206 */     float f2 = (1.0F - paramInt1 / paramInt2 * f1) * paramInt3;
/* 207 */     return Mth.ceil(f2 / 2.0F);
/*     */   }
/*     */   
/*     */   private static boolean isIcebergState(BlockState paramBlockState) {
/* 211 */     return (paramBlockState.is(Blocks.PACKED_ICE) || paramBlockState.is(Blocks.SNOW_BLOCK) || paramBlockState.is(Blocks.BLUE_ICE));
/*     */   }
/*     */   
/*     */   private boolean belowIsAir(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 215 */     return paramBlockGetter.getBlockState(paramBlockPos.below()).isAir();
/*     */   }
/*     */   
/*     */   private void smooth(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3) {
/* 219 */     int i = paramBoolean ? paramInt3 : (paramInt1 / 2);
/*     */     
/* 221 */     for (int j = -i; j <= i; j++) {
/* 222 */       for (int k = -i; k <= i; k++) {
/* 223 */         for (byte b = 0; b <= paramInt2; b++) {
/* 224 */           BlockPos blockPos = paramBlockPos.offset(j, b, k);
/* 225 */           BlockState blockState = paramLevelAccessor.getBlockState(blockPos);
/*     */ 
/*     */           
/* 228 */           if (isIcebergState(blockState) || blockState.is(Blocks.SNOW))
/* 229 */             if (belowIsAir((BlockGetter)paramLevelAccessor, blockPos)) {
/* 230 */               setBlock((LevelWriter)paramLevelAccessor, blockPos, Blocks.AIR.defaultBlockState());
/* 231 */               setBlock((LevelWriter)paramLevelAccessor, blockPos.above(), Blocks.AIR.defaultBlockState());
/*     */ 
/*     */             
/*     */             }
/* 235 */             else if (isIcebergState(blockState)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 241 */               BlockState[] arrayOfBlockState = { paramLevelAccessor.getBlockState(blockPos.west()), paramLevelAccessor.getBlockState(blockPos.east()), paramLevelAccessor.getBlockState(blockPos.north()), paramLevelAccessor.getBlockState(blockPos.south()) };
/*     */               
/* 243 */               byte b1 = 0;
/* 244 */               for (BlockState blockState1 : arrayOfBlockState) {
/* 245 */                 if (!isIcebergState(blockState1)) {
/* 246 */                   b1++;
/*     */                 }
/*     */               } 
/* 249 */               if (b1 >= 3)
/* 250 */                 setBlock((LevelWriter)paramLevelAccessor, blockPos, Blocks.AIR.defaultBlockState()); 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\IcebergFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */