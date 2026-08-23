/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ 
/*     */ public class WeepingVinesFeature extends Feature<NoneFeatureConfiguration> {
/*  17 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   public WeepingVinesFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/*  20 */     super(paramCodec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/*  25 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/*  26 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/*  27 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/*  28 */     if (!worldGenLevel.isEmptyBlock(blockPos)) {
/*  29 */       return false;
/*     */     }
/*     */     
/*  32 */     BlockState blockState = worldGenLevel.getBlockState(blockPos.above());
/*  33 */     if (!blockState.is(Blocks.NETHERRACK) && !blockState.is(Blocks.NETHER_WART_BLOCK)) {
/*  34 */       return false;
/*     */     }
/*     */     
/*  37 */     placeRoofNetherWart((LevelAccessor)worldGenLevel, randomSource, blockPos);
/*  38 */     placeRoofWeepingVines((LevelAccessor)worldGenLevel, randomSource, blockPos);
/*     */     
/*  40 */     return true;
/*     */   }
/*     */   
/*     */   private void placeRoofNetherWart(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/*  44 */     paramLevelAccessor.setBlock(paramBlockPos, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
/*     */     
/*  46 */     BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
/*  47 */     BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
/*     */     
/*  49 */     for (byte b = 0; b < 'È'; b++) {
/*  50 */       mutableBlockPos1.setWithOffset((Vec3i)paramBlockPos, paramRandomSource.nextInt(6) - paramRandomSource.nextInt(6), paramRandomSource.nextInt(2) - paramRandomSource.nextInt(5), paramRandomSource.nextInt(6) - paramRandomSource.nextInt(6));
/*  51 */       if (paramLevelAccessor.isEmptyBlock((BlockPos)mutableBlockPos1)) {
/*     */ 
/*     */ 
/*     */         
/*  55 */         byte b1 = 0;
/*  56 */         for (Direction direction : DIRECTIONS) {
/*  57 */           BlockState blockState = paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos2.setWithOffset((Vec3i)mutableBlockPos1, direction));
/*  58 */           if (blockState.is(Blocks.NETHERRACK) || blockState.is(Blocks.NETHER_WART_BLOCK)) {
/*  59 */             b1++;
/*     */           }
/*     */           
/*  62 */           if (b1 > 1) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/*  67 */         if (b1 == 1)
/*  68 */           paramLevelAccessor.setBlock((BlockPos)mutableBlockPos1, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeRoofWeepingVines(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/*  74 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */     
/*  76 */     for (byte b = 0; b < 100; b++) {
/*  77 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, paramRandomSource.nextInt(8) - paramRandomSource.nextInt(8), paramRandomSource.nextInt(2) - paramRandomSource.nextInt(7), paramRandomSource.nextInt(8) - paramRandomSource.nextInt(8));
/*  78 */       if (paramLevelAccessor.isEmptyBlock((BlockPos)mutableBlockPos)) {
/*     */ 
/*     */ 
/*     */         
/*  82 */         BlockState blockState = paramLevelAccessor.getBlockState(mutableBlockPos.above());
/*  83 */         if (blockState.is(Blocks.NETHERRACK) || blockState.is(Blocks.NETHER_WART_BLOCK)) {
/*     */ 
/*     */ 
/*     */           
/*  87 */           int i = Mth.nextInt(paramRandomSource, 1, 8);
/*  88 */           if (paramRandomSource.nextInt(6) == 0) {
/*  89 */             i *= 2;
/*     */           }
/*  91 */           if (paramRandomSource.nextInt(5) == 0) {
/*  92 */             i = 1;
/*     */           }
/*     */           
/*  95 */           byte b1 = 17;
/*  96 */           byte b2 = 25;
/*  97 */           placeWeepingVinesColumn(paramLevelAccessor, paramRandomSource, mutableBlockPos, i, 17, 25);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } public static void placeWeepingVinesColumn(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt1, int paramInt2, int paramInt3) {
/* 102 */     for (byte b = 0; b <= paramInt1; b++) {
/* 103 */       if (paramLevelAccessor.isEmptyBlock((BlockPos)paramMutableBlockPos)) {
/* 104 */         if (b == paramInt1 || !paramLevelAccessor.isEmptyBlock(paramMutableBlockPos.below())) {
/* 105 */           paramLevelAccessor.setBlock((BlockPos)paramMutableBlockPos, (BlockState)Blocks.WEEPING_VINES.defaultBlockState().setValue((Property)GrowingPlantHeadBlock.AGE, Integer.valueOf(Mth.nextInt(paramRandomSource, paramInt2, paramInt3))), 2);
/*     */           break;
/*     */         } 
/* 108 */         paramLevelAccessor.setBlock((BlockPos)paramMutableBlockPos, Blocks.WEEPING_VINES_PLANT.defaultBlockState(), 2);
/*     */       } 
/*     */ 
/*     */       
/* 112 */       paramMutableBlockPos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\WeepingVinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */