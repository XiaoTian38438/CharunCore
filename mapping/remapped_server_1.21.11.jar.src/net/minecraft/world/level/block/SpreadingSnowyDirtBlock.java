/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.lighting.LightEngine;
/*    */ 
/*    */ public abstract class SpreadingSnowyDirtBlock extends SnowyDirtBlock {
/*    */   protected SpreadingSnowyDirtBlock(BlockBehaviour.Properties paramProperties) {
/* 17 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   private static boolean canBeGrass(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 21 */     BlockPos blockPos = paramBlockPos.above();
/* 22 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 23 */     if (blockState.is(Blocks.SNOW) && ((Integer)blockState.getValue((Property)SnowLayerBlock.LAYERS)).intValue() == 1) {
/* 24 */       return true;
/*    */     }
/*    */     
/* 27 */     if (blockState.getFluidState().getAmount() == 8) {
/* 28 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 32 */     int i = LightEngine.getLightBlockInto(paramBlockState, blockState, Direction.UP, blockState.getLightBlock());
/*    */     
/* 34 */     return (i < 15);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends SpreadingSnowyDirtBlock> codec();
/*    */   
/*    */   private static boolean canPropagate(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 41 */     BlockPos blockPos = paramBlockPos.above();
/* 42 */     return (canBeGrass(paramBlockState, paramLevelReader, paramBlockPos) && !paramLevelReader.getFluidState(blockPos).is(FluidTags.WATER));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 47 */     if (!canBeGrass(paramBlockState, (LevelReader)paramServerLevel, paramBlockPos)) {
/* 48 */       paramServerLevel.setBlockAndUpdate(paramBlockPos, Blocks.DIRT.defaultBlockState());
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     if (paramServerLevel.getMaxLocalRawBrightness(paramBlockPos.above()) >= 9) {
/* 53 */       BlockState blockState = defaultBlockState();
/*    */       
/* 55 */       for (byte b = 0; b < 4; b++) {
/* 56 */         BlockPos blockPos = paramBlockPos.offset(paramRandomSource.nextInt(3) - 1, paramRandomSource.nextInt(5) - 3, paramRandomSource.nextInt(3) - 1);
/* 57 */         if (paramServerLevel.getBlockState(blockPos).is(Blocks.DIRT) && canPropagate(blockState, (LevelReader)paramServerLevel, blockPos))
/* 58 */           paramServerLevel.setBlockAndUpdate(blockPos, (BlockState)blockState.setValue((Property)SNOWY, Boolean.valueOf(isSnowySetting(paramServerLevel.getBlockState(blockPos.above()))))); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SpreadingSnowyDirtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */