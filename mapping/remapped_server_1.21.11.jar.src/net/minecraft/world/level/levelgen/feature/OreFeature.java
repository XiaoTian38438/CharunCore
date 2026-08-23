/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.BulkSectionAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ 
/*     */ public class OreFeature extends Feature<OreConfiguration> {
/*     */   public OreFeature(Codec<OreConfiguration> paramCodec) {
/*  20 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<OreConfiguration> paramFeaturePlaceContext) {
/*  25 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  26 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  27 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  28 */     OreConfiguration oreConfiguration = paramFeaturePlaceContext.config();
/*  29 */     float f1 = randomSource.nextFloat() * 3.1415927F;
/*     */     
/*  31 */     float f2 = oreConfiguration.size / 8.0F;
/*  32 */     int i = Mth.ceil((oreConfiguration.size / 16.0F * 2.0F + 1.0F) / 2.0F);
/*  33 */     double d1 = blockPos.getX() + Math.sin(f1) * f2;
/*  34 */     double d2 = blockPos.getX() - Math.sin(f1) * f2;
/*  35 */     double d3 = blockPos.getZ() + Math.cos(f1) * f2;
/*  36 */     double d4 = blockPos.getZ() - Math.cos(f1) * f2;
/*     */     
/*  38 */     byte b = 2;
/*  39 */     double d5 = (blockPos.getY() + randomSource.nextInt(3) - 2);
/*  40 */     double d6 = (blockPos.getY() + randomSource.nextInt(3) - 2);
/*     */     
/*  42 */     int j = blockPos.getX() - Mth.ceil(f2) - i;
/*  43 */     int k = blockPos.getY() - 2 - i;
/*  44 */     int m = blockPos.getZ() - Mth.ceil(f2) - i;
/*  45 */     int n = 2 * (Mth.ceil(f2) + i);
/*  46 */     int i1 = 2 * (2 + i);
/*     */ 
/*     */     
/*  49 */     for (int i2 = j; i2 <= j + n; i2++) {
/*  50 */       for (int i3 = m; i3 <= m + n; i3++) {
/*  51 */         if (k <= worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, i2, i3)) {
/*  52 */           return doPlace(worldGenLevel, randomSource, oreConfiguration, d1, d2, d3, d4, d5, d6, j, k, m, n, i1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean doPlace(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, OreConfiguration paramOreConfiguration, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/*  61 */     byte b1 = 0;
/*     */     
/*  63 */     BitSet bitSet = new BitSet(paramInt4 * paramInt5 * paramInt4);
/*  64 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  65 */     int i = paramOreConfiguration.size;
/*  66 */     double[] arrayOfDouble = new double[i * 4];
/*     */     byte b2;
/*  68 */     for (b2 = 0; b2 < i; b2++) {
/*  69 */       float f = b2 / i;
/*  70 */       double d1 = Mth.lerp(f, paramDouble1, paramDouble2);
/*  71 */       double d2 = Mth.lerp(f, paramDouble5, paramDouble6);
/*  72 */       double d3 = Mth.lerp(f, paramDouble3, paramDouble4);
/*     */       
/*  74 */       double d4 = paramRandomSource.nextDouble() * i / 16.0D;
/*  75 */       double d5 = ((Mth.sin((3.1415927F * f)) + 1.0F) * d4 + 1.0D) / 2.0D;
/*     */       
/*  77 */       arrayOfDouble[b2 * 4 + 0] = d1;
/*  78 */       arrayOfDouble[b2 * 4 + 1] = d2;
/*  79 */       arrayOfDouble[b2 * 4 + 2] = d3;
/*  80 */       arrayOfDouble[b2 * 4 + 3] = d5;
/*     */     } 
/*     */     
/*  83 */     for (b2 = 0; b2 < i - 1; b2++) {
/*  84 */       if (arrayOfDouble[b2 * 4 + 3] > 0.0D)
/*     */       {
/*     */ 
/*     */         
/*  88 */         for (int j = b2 + 1; j < i; j++) {
/*  89 */           if (arrayOfDouble[j * 4 + 3] > 0.0D) {
/*     */ 
/*     */ 
/*     */             
/*  93 */             double d1 = arrayOfDouble[b2 * 4 + 0] - arrayOfDouble[j * 4 + 0];
/*  94 */             double d2 = arrayOfDouble[b2 * 4 + 1] - arrayOfDouble[j * 4 + 1];
/*  95 */             double d3 = arrayOfDouble[b2 * 4 + 2] - arrayOfDouble[j * 4 + 2];
/*  96 */             double d4 = arrayOfDouble[b2 * 4 + 3] - arrayOfDouble[j * 4 + 3];
/*     */             
/*  98 */             if (d4 * d4 > d1 * d1 + d2 * d2 + d3 * d3)
/*  99 */               if (d4 > 0.0D) {
/* 100 */                 arrayOfDouble[j * 4 + 3] = -1.0D;
/*     */               } else {
/* 102 */                 arrayOfDouble[b2 * 4 + 3] = -1.0D;
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 108 */     BulkSectionAccess bulkSectionAccess = new BulkSectionAccess((LevelAccessor)paramWorldGenLevel); 
/* 109 */     try { for (byte b = 0; b < i; b++) {
/* 110 */         double d = arrayOfDouble[b * 4 + 3];
/* 111 */         if (d >= 0.0D) {
/*     */ 
/*     */ 
/*     */           
/* 115 */           double d1 = arrayOfDouble[b * 4 + 0];
/* 116 */           double d2 = arrayOfDouble[b * 4 + 1];
/* 117 */           double d3 = arrayOfDouble[b * 4 + 2];
/*     */ 
/*     */           
/* 120 */           int j = Math.max(Mth.floor(d1 - d), paramInt1);
/* 121 */           int k = Math.max(Mth.floor(d2 - d), paramInt2);
/* 122 */           int m = Math.max(Mth.floor(d3 - d), paramInt3);
/*     */           
/* 124 */           int n = Math.max(Mth.floor(d1 + d), j);
/* 125 */           int i1 = Math.max(Mth.floor(d2 + d), k);
/* 126 */           int i2 = Math.max(Mth.floor(d3 + d), m);
/*     */           
/* 128 */           for (int i3 = j; i3 <= n; i3++) {
/* 129 */             double d4 = (i3 + 0.5D - d1) / d;
/* 130 */             if (d4 * d4 < 1.0D)
/* 131 */               for (int i4 = k; i4 <= i1; i4++) {
/* 132 */                 double d5 = (i4 + 0.5D - d2) / d;
/* 133 */                 if (d4 * d4 + d5 * d5 < 1.0D)
/* 134 */                   for (int i5 = m; i5 <= i2; i5++) {
/* 135 */                     double d6 = (i5 + 0.5D - d3) / d;
/* 136 */                     if (d4 * d4 + d5 * d5 + d6 * d6 < 1.0D && 
/* 137 */                       !paramWorldGenLevel.isOutsideBuildHeight(i4)) {
/*     */ 
/*     */                       
/* 140 */                       int i6 = i3 - paramInt1 + (i4 - paramInt2) * paramInt4 + (i5 - paramInt3) * paramInt4 * paramInt5;
/* 141 */                       if (!bitSet.get(i6)) {
/*     */ 
/*     */                         
/* 144 */                         bitSet.set(i6);
/*     */                         
/* 146 */                         mutableBlockPos.set(i3, i4, i5);
/* 147 */                         if (paramWorldGenLevel.ensureCanWrite((BlockPos)mutableBlockPos)) {
/*     */ 
/*     */                           
/* 150 */                           LevelChunkSection levelChunkSection = bulkSectionAccess.getSection((BlockPos)mutableBlockPos);
/* 151 */                           if (levelChunkSection != null) {
/*     */ 
/*     */                             
/* 154 */                             int i7 = SectionPos.sectionRelative(i3);
/* 155 */                             int i8 = SectionPos.sectionRelative(i4);
/* 156 */                             int i9 = SectionPos.sectionRelative(i5);
/*     */                             
/* 158 */                             BlockState blockState = levelChunkSection.getBlockState(i7, i8, i9);
/* 159 */                             for (OreConfiguration.TargetBlockState targetBlockState : paramOreConfiguration.targetStates) {
/* 160 */                               Objects.requireNonNull(bulkSectionAccess); if (canPlaceOre(blockState, bulkSectionAccess::getBlockState, paramRandomSource, paramOreConfiguration, targetBlockState, mutableBlockPos)) {
/* 161 */                                 levelChunkSection.setBlockState(i7, i8, i9, targetBlockState.state, false);
/* 162 */                                 b1++; break;
/*     */                               } 
/*     */                             } 
/*     */                           } 
/*     */                         } 
/*     */                       } 
/*     */                     } 
/*     */                   }  
/*     */               }  
/*     */           } 
/*     */         } 
/* 173 */       }  bulkSectionAccess.close(); } catch (Throwable throwable) { try { bulkSectionAccess.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 175 */      return (b1 > 0);
/*     */   }
/*     */   
/*     */   public static boolean canPlaceOre(BlockState paramBlockState, Function<BlockPos, BlockState> paramFunction, RandomSource paramRandomSource, OreConfiguration paramOreConfiguration, OreConfiguration.TargetBlockState paramTargetBlockState, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 179 */     if (!paramTargetBlockState.target.test(paramBlockState, paramRandomSource)) {
/* 180 */       return false;
/*     */     }
/* 182 */     if (shouldSkipAirCheck(paramRandomSource, paramOreConfiguration.discardChanceOnAirExposure)) {
/* 183 */       return true;
/*     */     }
/* 185 */     return !isAdjacentToAir(paramFunction, (BlockPos)paramMutableBlockPos);
/*     */   }
/*     */   
/*     */   protected static boolean shouldSkipAirCheck(RandomSource paramRandomSource, float paramFloat) {
/* 189 */     if (paramFloat <= 0.0F) {
/* 190 */       return true;
/*     */     }
/* 192 */     if (paramFloat >= 1.0F) {
/* 193 */       return false;
/*     */     }
/* 195 */     return (paramRandomSource.nextFloat() >= paramFloat);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\OreFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */