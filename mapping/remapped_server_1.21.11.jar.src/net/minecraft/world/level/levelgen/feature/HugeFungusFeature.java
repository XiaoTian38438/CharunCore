/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ 
/*     */ public class HugeFungusFeature extends Feature<HugeFungusConfiguration> {
/*     */   public HugeFungusFeature(Codec<HugeFungusConfiguration> paramCodec) {
/*  20 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<HugeFungusConfiguration> paramFeaturePlaceContext) {
/*  25 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  26 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/*  27 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  28 */     ChunkGenerator chunkGenerator = paramFeaturePlaceContext.chunkGenerator();
/*  29 */     HugeFungusConfiguration hugeFungusConfiguration = paramFeaturePlaceContext.config();
/*  30 */     Block block = hugeFungusConfiguration.validBaseState.getBlock();
/*  31 */     BlockPos blockPos2 = null;
/*     */     
/*  33 */     BlockState blockState = worldGenLevel.getBlockState(blockPos1.below());
/*  34 */     if (blockState.is(block)) {
/*  35 */       blockPos2 = blockPos1;
/*     */     }
/*     */     
/*  38 */     if (blockPos2 == null) {
/*  39 */       return false;
/*     */     }
/*     */     
/*  42 */     int i = Mth.nextInt(randomSource, 4, 13);
/*  43 */     if (randomSource.nextInt(12) == 0) {
/*  44 */       i *= 2;
/*     */     }
/*     */     
/*  47 */     if (!hugeFungusConfiguration.planted) {
/*  48 */       int j = chunkGenerator.getGenDepth();
/*  49 */       if (blockPos2.getY() + i + 1 >= j) {
/*  50 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  54 */     boolean bool = (!hugeFungusConfiguration.planted && randomSource.nextFloat() < 0.06F) ? true : false;
/*     */     
/*  56 */     worldGenLevel.setBlock(blockPos1, Blocks.AIR.defaultBlockState(), 260);
/*     */     
/*  58 */     placeStem(worldGenLevel, randomSource, hugeFungusConfiguration, blockPos2, i, bool);
/*  59 */     placeHat(worldGenLevel, randomSource, hugeFungusConfiguration, blockPos2, i, bool);
/*     */     
/*  61 */     return true;
/*     */   }
/*     */   private static final float HUGE_PROBABILITY = 0.06F;
/*     */   private static boolean isReplaceable(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, HugeFungusConfiguration paramHugeFungusConfiguration, boolean paramBoolean) {
/*  65 */     if (paramWorldGenLevel.isStateAtPosition(paramBlockPos, BlockBehaviour.BlockStateBase::canBeReplaced)) {
/*  66 */       return true;
/*     */     }
/*  68 */     if (paramBoolean)
/*     */     {
/*     */       
/*  71 */       return paramHugeFungusConfiguration.replaceableBlocks.test(paramWorldGenLevel, paramBlockPos);
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeStem(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, HugeFungusConfiguration paramHugeFungusConfiguration, BlockPos paramBlockPos, int paramInt, boolean paramBoolean) {
/*  78 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  79 */     BlockState blockState = paramHugeFungusConfiguration.stemState;
/*  80 */     byte b1 = paramBoolean ? 1 : 0;
/*     */     
/*  82 */     for (byte b2 = -b1; b2 <= b1; b2++) {
/*  83 */       for (byte b = -b1; b <= b1; b++) {
/*  84 */         boolean bool = (paramBoolean && Mth.abs(b2) == b1 && Mth.abs(b) == b1) ? true : false;
/*     */         
/*  86 */         for (byte b3 = 0; b3 < paramInt; b3++) {
/*  87 */           mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, b2, b3, b);
/*  88 */           if (isReplaceable(paramWorldGenLevel, (BlockPos)mutableBlockPos, paramHugeFungusConfiguration, true)) {
/*  89 */             if (paramHugeFungusConfiguration.planted) {
/*  90 */               if (!paramWorldGenLevel.getBlockState(mutableBlockPos.below()).isAir()) {
/*  91 */                 paramWorldGenLevel.destroyBlock((BlockPos)mutableBlockPos, true);
/*     */               }
/*     */               
/*  94 */               paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, blockState, 3);
/*     */             }
/*  96 */             else if (bool) {
/*  97 */               if (paramRandomSource.nextFloat() < 0.1F) {
/*  98 */                 setBlock((LevelWriter)paramWorldGenLevel, (BlockPos)mutableBlockPos, blockState);
/*     */               }
/*     */             } else {
/* 101 */               setBlock((LevelWriter)paramWorldGenLevel, (BlockPos)mutableBlockPos, blockState);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeHat(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, HugeFungusConfiguration paramHugeFungusConfiguration, BlockPos paramBlockPos, int paramInt, boolean paramBoolean) {
/* 111 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 112 */     boolean bool = paramHugeFungusConfiguration.hatState.is(Blocks.NETHER_WART_BLOCK);
/* 113 */     int i = Math.min(paramRandomSource.nextInt(1 + paramInt / 3) + 5, paramInt);
/* 114 */     int j = paramInt - i;
/* 115 */     for (int k = j; k <= paramInt; k++) {
/* 116 */       byte b1 = (k < paramInt - paramRandomSource.nextInt(3)) ? 2 : 1;
/* 117 */       if (i > 8 && k < j + 4) {
/* 118 */         b1 = 3;
/*     */       }
/*     */       
/* 121 */       if (paramBoolean) {
/* 122 */         b1++;
/*     */       }
/*     */       
/* 125 */       for (byte b2 = -b1; b2 <= b1; b2++) {
/* 126 */         for (byte b = -b1; b <= b1; b++) {
/* 127 */           boolean bool1 = (b2 == -b1 || b2 == b1) ? true : false;
/* 128 */           boolean bool2 = (b == -b1 || b == b1) ? true : false;
/* 129 */           boolean bool3 = (!bool1 && !bool2 && k != paramInt) ? true : false;
/* 130 */           boolean bool4 = (bool1 && bool2) ? true : false;
/* 131 */           boolean bool5 = (k < j + 3) ? true : false;
/*     */           
/* 133 */           mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, b2, k, b);
/* 134 */           if (isReplaceable(paramWorldGenLevel, (BlockPos)mutableBlockPos, paramHugeFungusConfiguration, false)) {
/* 135 */             if (paramHugeFungusConfiguration.planted && !paramWorldGenLevel.getBlockState(mutableBlockPos.below()).isAir()) {
/* 136 */               paramWorldGenLevel.destroyBlock((BlockPos)mutableBlockPos, true);
/*     */             }
/*     */             
/* 139 */             if (bool5) {
/* 140 */               if (!bool3) {
/* 141 */                 placeHatDropBlock((LevelAccessor)paramWorldGenLevel, paramRandomSource, (BlockPos)mutableBlockPos, paramHugeFungusConfiguration.hatState, bool);
/*     */               }
/* 143 */             } else if (bool3) {
/* 144 */               placeHatBlock((LevelAccessor)paramWorldGenLevel, paramRandomSource, paramHugeFungusConfiguration, mutableBlockPos, 0.1F, 0.2F, bool ? 0.1F : 0.0F);
/* 145 */             } else if (bool4) {
/* 146 */               placeHatBlock((LevelAccessor)paramWorldGenLevel, paramRandomSource, paramHugeFungusConfiguration, mutableBlockPos, 0.01F, 0.7F, bool ? 0.083F : 0.0F);
/*     */             } else {
/* 148 */               placeHatBlock((LevelAccessor)paramWorldGenLevel, paramRandomSource, paramHugeFungusConfiguration, mutableBlockPos, 5.0E-4F, 0.98F, bool ? 0.07F : 0.0F);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeHatBlock(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, HugeFungusConfiguration paramHugeFungusConfiguration, BlockPos.MutableBlockPos paramMutableBlockPos, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 157 */     if (paramRandomSource.nextFloat() < paramFloat1) {
/* 158 */       setBlock((LevelWriter)paramLevelAccessor, (BlockPos)paramMutableBlockPos, paramHugeFungusConfiguration.decorState);
/* 159 */     } else if (paramRandomSource.nextFloat() < paramFloat2) {
/* 160 */       setBlock((LevelWriter)paramLevelAccessor, (BlockPos)paramMutableBlockPos, paramHugeFungusConfiguration.hatState);
/* 161 */       if (paramRandomSource.nextFloat() < paramFloat3) {
/* 162 */         tryPlaceWeepingVines((BlockPos)paramMutableBlockPos, paramLevelAccessor, paramRandomSource);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeHatDropBlock(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 168 */     if (paramLevelAccessor.getBlockState(paramBlockPos.below()).is(paramBlockState.getBlock())) {
/* 169 */       setBlock((LevelWriter)paramLevelAccessor, paramBlockPos, paramBlockState);
/* 170 */     } else if (paramRandomSource.nextFloat() < 0.15D) {
/* 171 */       setBlock((LevelWriter)paramLevelAccessor, paramBlockPos, paramBlockState);
/* 172 */       if (paramBoolean && paramRandomSource.nextInt(11) == 0) {
/* 173 */         tryPlaceWeepingVines(paramBlockPos, paramLevelAccessor, paramRandomSource);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void tryPlaceWeepingVines(BlockPos paramBlockPos, LevelAccessor paramLevelAccessor, RandomSource paramRandomSource) {
/* 179 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable().move(Direction.DOWN);
/*     */     
/* 181 */     if (!paramLevelAccessor.isEmptyBlock((BlockPos)mutableBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     int i = Mth.nextInt(paramRandomSource, 1, 5);
/* 186 */     if (paramRandomSource.nextInt(7) == 0) {
/* 187 */       i *= 2;
/*     */     }
/*     */     
/* 190 */     byte b1 = 23;
/* 191 */     byte b2 = 25;
/* 192 */     WeepingVinesFeature.placeWeepingVinesColumn(paramLevelAccessor, paramRandomSource, mutableBlockPos, i, 23, 25);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */