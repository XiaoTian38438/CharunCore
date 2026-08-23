/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class GeodeFeature extends Feature<GeodeConfiguration> {
/*  29 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   public GeodeFeature(Codec<GeodeConfiguration> paramCodec) {
/*  32 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<GeodeConfiguration> paramFeaturePlaceContext) {
/*  37 */     GeodeConfiguration geodeConfiguration = paramFeaturePlaceContext.config();
/*  38 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  39 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  40 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  41 */     int i = geodeConfiguration.minGenOffset;
/*  42 */     int j = geodeConfiguration.maxGenOffset;
/*     */     
/*  44 */     LinkedList<Pair> linkedList = Lists.newLinkedList();
/*  45 */     int k = geodeConfiguration.distributionPoints.sample(randomSource);
/*  46 */     WorldgenRandom worldgenRandom = new WorldgenRandom((RandomSource)new LegacyRandomSource(worldGenLevel.getSeed()));
/*  47 */     NormalNoise normalNoise = NormalNoise.create((RandomSource)worldgenRandom, -4, new double[] { 1.0D });
/*  48 */     LinkedList<BlockPos> linkedList1 = Lists.newLinkedList();
/*  49 */     double d1 = k / geodeConfiguration.outerWallDistance.getMaxValue();
/*  50 */     GeodeLayerSettings geodeLayerSettings = geodeConfiguration.geodeLayerSettings;
/*  51 */     GeodeBlockSettings geodeBlockSettings = geodeConfiguration.geodeBlockSettings;
/*  52 */     GeodeCrackSettings geodeCrackSettings = geodeConfiguration.geodeCrackSettings;
/*  53 */     double d2 = 1.0D / Math.sqrt(geodeLayerSettings.filling);
/*  54 */     double d3 = 1.0D / Math.sqrt(geodeLayerSettings.innerLayer + d1);
/*  55 */     double d4 = 1.0D / Math.sqrt(geodeLayerSettings.middleLayer + d1);
/*  56 */     double d5 = 1.0D / Math.sqrt(geodeLayerSettings.outerLayer + d1);
/*  57 */     double d6 = 1.0D / Math.sqrt(geodeCrackSettings.baseCrackSize + randomSource.nextDouble() / 2.0D + ((k > 3) ? d1 : 0.0D));
/*  58 */     boolean bool = (randomSource.nextFloat() < geodeCrackSettings.generateCrackChance) ? true : false;
/*     */     
/*  60 */     byte b = 0; int m;
/*  61 */     for (m = 0; m < k; m++) {
/*  62 */       int n = geodeConfiguration.outerWallDistance.sample(randomSource);
/*  63 */       int i1 = geodeConfiguration.outerWallDistance.sample(randomSource);
/*  64 */       int i2 = geodeConfiguration.outerWallDistance.sample(randomSource);
/*  65 */       BlockPos blockPos1 = blockPos.offset(n, i1, i2);
/*  66 */       BlockState blockState = worldGenLevel.getBlockState(blockPos1);
/*  67 */       if ((blockState.isAir() || blockState.is(geodeBlockSettings.invalidBlocks)) && 
/*  68 */         ++b > geodeConfiguration.invalidBlocksThreshold) {
/*  69 */         return false;
/*     */       }
/*     */       
/*  72 */       linkedList.add(Pair.of(blockPos1, Integer.valueOf(geodeConfiguration.pointOffset.sample(randomSource))));
/*     */     } 
/*     */     
/*  75 */     if (bool) {
/*  76 */       m = randomSource.nextInt(4);
/*     */       
/*  78 */       int n = k * 2 + 1;
/*  79 */       if (m == 0) {
/*  80 */         linkedList1.add(blockPos.offset(n, 7, 0));
/*  81 */         linkedList1.add(blockPos.offset(n, 5, 0));
/*  82 */         linkedList1.add(blockPos.offset(n, 1, 0));
/*  83 */       } else if (m == 1) {
/*  84 */         linkedList1.add(blockPos.offset(0, 7, n));
/*  85 */         linkedList1.add(blockPos.offset(0, 5, n));
/*  86 */         linkedList1.add(blockPos.offset(0, 1, n));
/*  87 */       } else if (m == 2) {
/*  88 */         linkedList1.add(blockPos.offset(n, 7, n));
/*  89 */         linkedList1.add(blockPos.offset(n, 5, n));
/*  90 */         linkedList1.add(blockPos.offset(n, 1, n));
/*     */       } else {
/*  92 */         linkedList1.add(blockPos.offset(0, 7, 0));
/*  93 */         linkedList1.add(blockPos.offset(0, 5, 0));
/*  94 */         linkedList1.add(blockPos.offset(0, 1, 0));
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     ArrayList<BlockPos> arrayList = Lists.newArrayList();
/*  99 */     Predicate<BlockState> predicate = isReplaceable(geodeConfiguration.geodeBlockSettings.cannotReplace);
/*     */     
/* 101 */     for (BlockPos blockPos1 : BlockPos.betweenClosed(blockPos.offset(i, i, i), blockPos.offset(j, j, j))) {
/* 102 */       double d7 = normalNoise.getValue(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ()) * geodeConfiguration.noiseMultiplier;
/*     */       
/* 104 */       double d8 = 0.0D;
/* 105 */       double d9 = 0.0D;
/*     */       
/* 107 */       for (Pair pair : linkedList) {
/* 108 */         d8 += Mth.invSqrt(blockPos1.distSqr((Vec3i)pair.getFirst()) + ((Integer)pair.getSecond()).intValue()) + d7;
/*     */       }
/*     */       
/* 111 */       for (BlockPos blockPos2 : linkedList1) {
/* 112 */         d9 += Mth.invSqrt(blockPos1.distSqr((Vec3i)blockPos2) + geodeCrackSettings.crackPointOffset) + d7;
/*     */       }
/*     */ 
/*     */       
/* 116 */       if (d8 < d5) {
/*     */         continue;
/*     */       }
/*     */       
/* 120 */       if (bool && d9 >= d6 && d8 < d2) {
/*     */         
/* 122 */         safeSetBlock(worldGenLevel, blockPos1, Blocks.AIR.defaultBlockState(), predicate);
/*     */ 
/*     */         
/* 125 */         for (Direction direction : DIRECTIONS) {
/* 126 */           BlockPos blockPos2 = blockPos1.relative(direction);
/* 127 */           FluidState fluidState = worldGenLevel.getFluidState(blockPos2);
/* 128 */           if (!fluidState.isEmpty())
/* 129 */             worldGenLevel.scheduleTick(blockPos2, fluidState.getType(), 0); 
/*     */         }  continue;
/*     */       } 
/* 132 */       if (d8 >= d2) {
/* 133 */         safeSetBlock(worldGenLevel, blockPos1, geodeBlockSettings.fillingProvider.getState(randomSource, blockPos1), predicate); continue;
/* 134 */       }  if (d8 >= d3) {
/* 135 */         boolean bool1 = (randomSource.nextFloat() < geodeConfiguration.useAlternateLayer0Chance) ? true : false;
/* 136 */         if (bool1) {
/* 137 */           safeSetBlock(worldGenLevel, blockPos1, geodeBlockSettings.alternateInnerLayerProvider.getState(randomSource, blockPos1), predicate);
/*     */         } else {
/* 139 */           safeSetBlock(worldGenLevel, blockPos1, geodeBlockSettings.innerLayerProvider.getState(randomSource, blockPos1), predicate);
/*     */         } 
/*     */         
/* 142 */         if ((!geodeConfiguration.placementsRequireLayer0Alternate || bool1) && randomSource.nextFloat() < geodeConfiguration.usePotentialPlacementsChance)
/* 143 */           arrayList.add(blockPos1.immutable());  continue;
/*     */       } 
/* 145 */       if (d8 >= d4) {
/* 146 */         safeSetBlock(worldGenLevel, blockPos1, geodeBlockSettings.middleLayerProvider.getState(randomSource, blockPos1), predicate); continue;
/* 147 */       }  if (d8 >= d5) {
/* 148 */         safeSetBlock(worldGenLevel, blockPos1, geodeBlockSettings.outerLayerProvider.getState(randomSource, blockPos1), predicate);
/*     */       }
/*     */     } 
/*     */     
/* 152 */     List list = geodeBlockSettings.innerPlacements;
/* 153 */     for (BlockPos blockPos1 : arrayList) {
/* 154 */       BlockState blockState = (BlockState)Util.getRandom(list, randomSource);
/* 155 */       for (Direction direction : DIRECTIONS) {
/* 156 */         if (blockState.hasProperty((Property)BlockStateProperties.FACING)) {
/* 157 */           blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.FACING, (Comparable)direction);
/*     */         }
/*     */         
/* 160 */         BlockPos blockPos2 = blockPos1.relative(direction);
/* 161 */         BlockState blockState1 = worldGenLevel.getBlockState(blockPos2);
/* 162 */         if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/* 163 */           blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(blockState1.getFluidState().isSource()));
/*     */         }
/*     */         
/* 166 */         if (BuddingAmethystBlock.canClusterGrowAtState(blockState1)) {
/* 167 */           safeSetBlock(worldGenLevel, blockPos2, blockState, predicate);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 173 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\GeodeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */