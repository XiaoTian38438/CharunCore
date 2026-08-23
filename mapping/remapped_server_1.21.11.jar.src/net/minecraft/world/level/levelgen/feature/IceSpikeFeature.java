/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ 
/*     */ public class IceSpikeFeature extends Feature<NoneFeatureConfiguration> {
/*     */   public IceSpikeFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/*  14 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/*  19 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  20 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  21 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  22 */     while (worldGenLevel.isEmptyBlock(blockPos) && blockPos.getY() > worldGenLevel.getMinY() + 2) {
/*  23 */       blockPos = blockPos.below();
/*     */     }
/*     */     
/*  26 */     if (!worldGenLevel.getBlockState(blockPos).is(Blocks.SNOW_BLOCK)) {
/*  27 */       return false;
/*     */     }
/*  29 */     blockPos = blockPos.above(randomSource.nextInt(4));
/*     */     
/*  31 */     int i = randomSource.nextInt(4) + 7;
/*  32 */     int j = i / 4 + randomSource.nextInt(2);
/*     */     
/*  34 */     if (j > 1 && randomSource.nextInt(60) == 0) {
/*  35 */       blockPos = blockPos.above(10 + randomSource.nextInt(30));
/*     */     }
/*     */     int k;
/*  38 */     for (k = 0; k < i; k++) {
/*  39 */       float f = (1.0F - k / i) * j;
/*  40 */       int n = Mth.ceil(f);
/*     */       
/*  42 */       for (int i1 = -n; i1 <= n; i1++) {
/*  43 */         float f1 = Mth.abs(i1) - 0.25F;
/*  44 */         for (int i2 = -n; i2 <= n; i2++) {
/*  45 */           float f2 = Mth.abs(i2) - 0.25F;
/*  46 */           if ((i1 == 0 && i2 == 0) || f1 * f1 + f2 * f2 <= f * f)
/*     */           {
/*     */             
/*  49 */             if ((i1 != -n && i1 != n && i2 != -n && i2 != n) || 
/*  50 */               randomSource.nextFloat() <= 0.75F) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  55 */               BlockState blockState = worldGenLevel.getBlockState(blockPos.offset(i1, k, i2));
/*     */               
/*  57 */               if (blockState.isAir() || isDirt(blockState) || blockState.is(Blocks.SNOW_BLOCK) || blockState.is(Blocks.ICE)) {
/*  58 */                 setBlock((LevelWriter)worldGenLevel, blockPos.offset(i1, k, i2), Blocks.PACKED_ICE.defaultBlockState());
/*     */               }
/*     */               
/*  61 */               if (k != 0 && n > 1) {
/*  62 */                 blockState = worldGenLevel.getBlockState(blockPos.offset(i1, -k, i2));
/*     */                 
/*  64 */                 if (blockState.isAir() || isDirt(blockState) || blockState.is(Blocks.SNOW_BLOCK) || blockState.is(Blocks.ICE))
/*  65 */                   setBlock((LevelWriter)worldGenLevel, blockPos.offset(i1, -k, i2), Blocks.PACKED_ICE.defaultBlockState()); 
/*     */               } 
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  71 */     k = j - 1;
/*  72 */     if (k < 0) {
/*  73 */       k = 0;
/*  74 */     } else if (k > 1) {
/*  75 */       k = 1;
/*     */     } 
/*  77 */     for (int m = -k; m <= k; m++) {
/*  78 */       for (int n = -k; n <= k; n++) {
/*  79 */         BlockPos blockPos1 = blockPos.offset(m, -1, n);
/*  80 */         int i1 = 50;
/*  81 */         if (Math.abs(m) == 1 && Math.abs(n) == 1) {
/*  82 */           i1 = randomSource.nextInt(5);
/*     */         }
/*  84 */         while (blockPos1.getY() > 50) {
/*  85 */           BlockState blockState = worldGenLevel.getBlockState(blockPos1);
/*     */           
/*  87 */           if (blockState.isAir() || isDirt(blockState) || blockState.is(Blocks.SNOW_BLOCK) || blockState.is(Blocks.ICE) || blockState.is(Blocks.PACKED_ICE)) {
/*  88 */             setBlock((LevelWriter)worldGenLevel, blockPos1, Blocks.PACKED_ICE.defaultBlockState());
/*     */ 
/*     */ 
/*     */             
/*  92 */             blockPos1 = blockPos1.below();
/*  93 */             i1--;
/*  94 */             if (i1 <= 0) {
/*  95 */               blockPos1 = blockPos1.below(randomSource.nextInt(5) + 1);
/*  96 */               i1 = randomSource.nextInt(5);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 102 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\IceSpikeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */