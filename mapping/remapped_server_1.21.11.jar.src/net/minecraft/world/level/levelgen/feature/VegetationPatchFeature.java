/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class VegetationPatchFeature extends Feature<VegetationPatchConfiguration> {
/*     */   public VegetationPatchFeature(Codec<VegetationPatchConfiguration> paramCodec) {
/*  20 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<VegetationPatchConfiguration> paramFeaturePlaceContext) {
/*  25 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  26 */     VegetationPatchConfiguration vegetationPatchConfiguration = paramFeaturePlaceContext.config();
/*  27 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  28 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  29 */     Predicate<BlockState> predicate = paramBlockState -> paramBlockState.is(paramVegetationPatchConfiguration.replaceable);
/*     */     
/*  31 */     int i = vegetationPatchConfiguration.xzRadius.sample(randomSource) + 1;
/*  32 */     int j = vegetationPatchConfiguration.xzRadius.sample(randomSource) + 1;
/*     */     
/*  34 */     Set<BlockPos> set = placeGroundPatch(worldGenLevel, vegetationPatchConfiguration, randomSource, blockPos, predicate, i, j);
/*  35 */     distributeVegetation(paramFeaturePlaceContext, worldGenLevel, vegetationPatchConfiguration, randomSource, set, i, j);
/*     */     
/*  37 */     return !set.isEmpty();
/*     */   }
/*     */   
/*     */   protected Set<BlockPos> placeGroundPatch(WorldGenLevel paramWorldGenLevel, VegetationPatchConfiguration paramVegetationPatchConfiguration, RandomSource paramRandomSource, BlockPos paramBlockPos, Predicate<BlockState> paramPredicate, int paramInt1, int paramInt2) {
/*  41 */     BlockPos.MutableBlockPos mutableBlockPos1 = paramBlockPos.mutable();
/*  42 */     BlockPos.MutableBlockPos mutableBlockPos2 = mutableBlockPos1.mutable();
/*  43 */     Direction direction1 = paramVegetationPatchConfiguration.surface.getDirection();
/*  44 */     Direction direction2 = direction1.getOpposite();
/*  45 */     HashSet<BlockPos> hashSet = new HashSet();
/*  46 */     for (int i = -paramInt1; i <= paramInt1; i++) {
/*  47 */       boolean bool = (i == -paramInt1 || i == paramInt1) ? true : false;
/*  48 */       for (int j = -paramInt2; j <= paramInt2; j++) {
/*  49 */         boolean bool1 = (j == -paramInt2 || j == paramInt2) ? true : false;
/*  50 */         boolean bool2 = (bool || bool1) ? true : false;
/*  51 */         boolean bool3 = (bool && bool1) ? true : false;
/*  52 */         boolean bool4 = (bool2 && !bool3) ? true : false;
/*  53 */         if (!bool3 && (!bool4 || (paramVegetationPatchConfiguration.extraEdgeColumnChance != 0.0F && paramRandomSource.nextFloat() <= paramVegetationPatchConfiguration.extraEdgeColumnChance))) {
/*     */ 
/*     */           
/*  56 */           mutableBlockPos1.setWithOffset((Vec3i)paramBlockPos, i, 0, j);
/*  57 */           byte b = 0;
/*  58 */           while (paramWorldGenLevel.isStateAtPosition((BlockPos)mutableBlockPos1, BlockBehaviour.BlockStateBase::isAir) && b < paramVegetationPatchConfiguration.verticalRange) {
/*  59 */             mutableBlockPos1.move(direction1);
/*  60 */             b++;
/*     */           } 
/*  62 */           b = 0;
/*  63 */           while (paramWorldGenLevel.isStateAtPosition((BlockPos)mutableBlockPos1, paramBlockState -> !paramBlockState.isAir()) && b < paramVegetationPatchConfiguration.verticalRange) {
/*  64 */             mutableBlockPos1.move(direction2);
/*  65 */             b++;
/*     */           } 
/*     */           
/*  68 */           mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, paramVegetationPatchConfiguration.surface.getDirection());
/*  69 */           BlockState blockState = paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos2);
/*  70 */           if (paramWorldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos1) && blockState.isFaceSturdy((BlockGetter)paramWorldGenLevel, (BlockPos)mutableBlockPos2, paramVegetationPatchConfiguration.surface.getDirection().getOpposite())) {
/*  71 */             int k = paramVegetationPatchConfiguration.depth.sample(paramRandomSource) + ((paramVegetationPatchConfiguration.extraBottomBlockChance > 0.0F && paramRandomSource.nextFloat() < paramVegetationPatchConfiguration.extraBottomBlockChance) ? 1 : 0);
/*  72 */             BlockPos blockPos = mutableBlockPos2.immutable();
/*  73 */             boolean bool5 = placeGround(paramWorldGenLevel, paramVegetationPatchConfiguration, paramPredicate, paramRandomSource, mutableBlockPos2, k);
/*  74 */             if (bool5)
/*  75 */               hashSet.add(blockPos); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  80 */     return hashSet;
/*     */   }
/*     */   
/*     */   protected void distributeVegetation(FeaturePlaceContext<VegetationPatchConfiguration> paramFeaturePlaceContext, WorldGenLevel paramWorldGenLevel, VegetationPatchConfiguration paramVegetationPatchConfiguration, RandomSource paramRandomSource, Set<BlockPos> paramSet, int paramInt1, int paramInt2) {
/*  84 */     for (BlockPos blockPos : paramSet) {
/*  85 */       if (paramVegetationPatchConfiguration.vegetationChance > 0.0F && paramRandomSource.nextFloat() < paramVegetationPatchConfiguration.vegetationChance) {
/*  86 */         placeVegetation(paramWorldGenLevel, paramVegetationPatchConfiguration, paramFeaturePlaceContext.chunkGenerator(), paramRandomSource, blockPos);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean placeVegetation(WorldGenLevel paramWorldGenLevel, VegetationPatchConfiguration paramVegetationPatchConfiguration, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/*  92 */     return ((PlacedFeature)paramVegetationPatchConfiguration.vegetationFeature.value()).place(paramWorldGenLevel, paramChunkGenerator, paramRandomSource, paramBlockPos.relative(paramVegetationPatchConfiguration.surface.getDirection().getOpposite()));
/*     */   }
/*     */   
/*     */   protected boolean placeGround(WorldGenLevel paramWorldGenLevel, VegetationPatchConfiguration paramVegetationPatchConfiguration, Predicate<BlockState> paramPredicate, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt) {
/*  96 */     for (byte b = 0; b < paramInt; b++) {
/*  97 */       BlockState blockState1 = paramVegetationPatchConfiguration.groundState.getState(paramRandomSource, (BlockPos)paramMutableBlockPos);
/*  98 */       BlockState blockState2 = paramWorldGenLevel.getBlockState((BlockPos)paramMutableBlockPos);
/*  99 */       if (!blockState1.is(blockState2.getBlock())) {
/*     */ 
/*     */ 
/*     */         
/* 103 */         if (!paramPredicate.test(blockState2)) {
/* 104 */           return (b != 0);
/*     */         }
/*     */         
/* 107 */         paramWorldGenLevel.setBlock((BlockPos)paramMutableBlockPos, blockState1, 2);
/* 108 */         paramMutableBlockPos.move(paramVegetationPatchConfiguration.surface.getDirection());
/*     */       } 
/* 110 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\VegetationPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */