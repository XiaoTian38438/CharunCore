/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BaseCoralWallFanBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SeaPickleBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public abstract class CoralFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public CoralFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 23 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 28 */     RandomSource randomSource = paramFeaturePlaceContext.random();
/* 29 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 30 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 31 */     Optional<Block> optional = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.CORAL_BLOCKS, randomSource).map(Holder::value);
/* 32 */     if (optional.isEmpty()) {
/* 33 */       return false;
/*    */     }
/* 35 */     return placeFeature((LevelAccessor)worldGenLevel, randomSource, blockPos, ((Block)optional.get()).defaultBlockState());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean placeCoralBlock(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 41 */     BlockPos blockPos = paramBlockPos.above();
/* 42 */     BlockState blockState = paramLevelAccessor.getBlockState(paramBlockPos);
/*    */     
/* 44 */     if ((!blockState.is(Blocks.WATER) && !blockState.is(BlockTags.CORALS)) || !paramLevelAccessor.getBlockState(blockPos).is(Blocks.WATER)) {
/* 45 */       return false;
/*    */     }
/*    */     
/* 48 */     paramLevelAccessor.setBlock(paramBlockPos, paramBlockState, 3);
/* 49 */     if (paramRandomSource.nextFloat() < 0.25F) {
/* 50 */       BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.CORALS, paramRandomSource).map(Holder::value).ifPresent(paramBlock -> paramLevelAccessor.setBlock(paramBlockPos, paramBlock.defaultBlockState(), 2));
/*    */     
/*    */     }
/* 53 */     else if (paramRandomSource.nextFloat() < 0.05F) {
/* 54 */       paramLevelAccessor.setBlock(blockPos, (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue((Property)SeaPickleBlock.PICKLES, Integer.valueOf(paramRandomSource.nextInt(4) + 1)), 2);
/*    */     } 
/*    */     
/* 57 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 58 */       if (paramRandomSource.nextFloat() < 0.2F) {
/* 59 */         BlockPos blockPos1 = paramBlockPos.relative(direction);
/* 60 */         if (paramLevelAccessor.getBlockState(blockPos1).is(Blocks.WATER)) {
/* 61 */           BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, paramRandomSource).map(Holder::value).ifPresent(paramBlock -> {
/*    */                 BlockState blockState = paramBlock.defaultBlockState();
/*    */                 
/*    */                 if (blockState.hasProperty((Property)BaseCoralWallFanBlock.FACING)) {
/*    */                   blockState = (BlockState)blockState.setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)paramDirection);
/*    */                 }
/*    */                 paramLevelAccessor.setBlock(paramBlockPos, blockState, 2);
/*    */               });
/*    */         }
/*    */       } 
/*    */     } 
/* 72 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract boolean placeFeature(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\CoralFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */