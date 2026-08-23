/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BambooStalkBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class BambooFeature extends Feature<ProbabilityFeatureConfiguration> {
/* 17 */   private static final BlockState BAMBOO_TRUNK = (BlockState)((BlockState)((BlockState)Blocks.BAMBOO.defaultBlockState().setValue((Property)BambooStalkBlock.AGE, Integer.valueOf(1))).setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)BambooStalkBlock.STAGE, Integer.valueOf(0));
/* 18 */   private static final BlockState BAMBOO_FINAL_LARGE = (BlockState)((BlockState)BAMBOO_TRUNK.setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.LARGE)).setValue((Property)BambooStalkBlock.STAGE, Integer.valueOf(1));
/* 19 */   private static final BlockState BAMBOO_TOP_LARGE = (BlockState)BAMBOO_TRUNK.setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.LARGE);
/* 20 */   private static final BlockState BAMBOO_TOP_SMALL = (BlockState)BAMBOO_TRUNK.setValue((Property)BambooStalkBlock.LEAVES, (Comparable)BambooLeaves.SMALL);
/*    */   
/*    */   public BambooFeature(Codec<ProbabilityFeatureConfiguration> paramCodec) {
/* 23 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> paramFeaturePlaceContext) {
/* 28 */     byte b = 0;
/*    */     
/* 30 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 31 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 32 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 33 */     ProbabilityFeatureConfiguration probabilityFeatureConfiguration = paramFeaturePlaceContext.config();
/* 34 */     BlockPos.MutableBlockPos mutableBlockPos1 = blockPos.mutable();
/* 35 */     BlockPos.MutableBlockPos mutableBlockPos2 = blockPos.mutable();
/* 36 */     if (worldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos1)) {
/* 37 */       if (Blocks.BAMBOO.defaultBlockState().canSurvive((LevelReader)worldGenLevel, (BlockPos)mutableBlockPos1)) {
/* 38 */         int i = randomSource.nextInt(12) + 5;
/*    */ 
/*    */         
/* 41 */         if (randomSource.nextFloat() < probabilityFeatureConfiguration.probability) {
/* 42 */           int j = randomSource.nextInt(4) + 1;
/* 43 */           for (int k = blockPos.getX() - j; k <= blockPos.getX() + j; k++) {
/* 44 */             for (int m = blockPos.getZ() - j; m <= blockPos.getZ() + j; m++) {
/* 45 */               int n = k - blockPos.getX();
/* 46 */               int i1 = m - blockPos.getZ();
/* 47 */               if (n * n + i1 * i1 <= j * j) {
/*    */ 
/*    */ 
/*    */                 
/* 51 */                 mutableBlockPos2.set(k, worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE, k, m) - 1, m);
/* 52 */                 if (isDirt(worldGenLevel.getBlockState((BlockPos)mutableBlockPos2))) {
/* 53 */                   worldGenLevel.setBlock((BlockPos)mutableBlockPos2, Blocks.PODZOL.defaultBlockState(), 2);
/*    */                 }
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/* 59 */         for (byte b1 = 0; b1 < i && 
/* 60 */           worldGenLevel.isEmptyBlock((BlockPos)mutableBlockPos1); b1++) {
/* 61 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos1, BAMBOO_TRUNK, 2);
/*    */ 
/*    */ 
/*    */           
/* 65 */           mutableBlockPos1.move(Direction.UP, 1);
/*    */         } 
/*    */         
/* 68 */         if (mutableBlockPos1.getY() - blockPos.getY() >= 3) {
/* 69 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos1, BAMBOO_FINAL_LARGE, 2);
/* 70 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos1.move(Direction.DOWN, 1), BAMBOO_TOP_LARGE, 2);
/* 71 */           worldGenLevel.setBlock((BlockPos)mutableBlockPos1.move(Direction.DOWN, 1), BAMBOO_TOP_SMALL, 2);
/*    */         } 
/*    */       } 
/* 74 */       b++;
/*    */     } 
/*    */     
/* 77 */     return (b > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\BambooFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */