/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SeaPickleBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ 
/*    */ public class SeaPickleFeature extends Feature<CountConfiguration> {
/*    */   public SeaPickleFeature(Codec<CountConfiguration> paramCodec) {
/* 16 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<CountConfiguration> paramFeaturePlaceContext) {
/* 21 */     byte b1 = 0;
/* 22 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 23 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 24 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 25 */     int i = ((CountConfiguration)paramFeaturePlaceContext.config()).count().sample(randomSource);
/* 26 */     for (byte b2 = 0; b2 < i; b2++) {
/* 27 */       int j = randomSource.nextInt(8) - randomSource.nextInt(8);
/* 28 */       int k = randomSource.nextInt(8) - randomSource.nextInt(8);
/* 29 */       int m = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos.getX() + j, blockPos.getZ() + k);
/* 30 */       BlockPos blockPos1 = new BlockPos(blockPos.getX() + j, m, blockPos.getZ() + k);
/*    */       
/* 32 */       BlockState blockState = (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue((Property)SeaPickleBlock.PICKLES, Integer.valueOf(randomSource.nextInt(4) + 1));
/* 33 */       if (worldGenLevel.getBlockState(blockPos1).is(Blocks.WATER) && blockState.canSurvive((LevelReader)worldGenLevel, blockPos1)) {
/* 34 */         worldGenLevel.setBlock(blockPos1, blockState, 2);
/* 35 */         b1++;
/*    */       } 
/*    */     } 
/* 38 */     return (b1 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\SeaPickleFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */