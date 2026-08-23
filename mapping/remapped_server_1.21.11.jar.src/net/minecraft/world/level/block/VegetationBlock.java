/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ public abstract class VegetationBlock extends Block {
/*    */   protected VegetationBlock(BlockBehaviour.Properties paramProperties) {
/* 16 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends VegetationBlock> codec();
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 23 */     return (paramBlockState.is(BlockTags.DIRT) || paramBlockState.is(Blocks.FARMLAND));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 28 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 29 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 31 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 36 */     BlockPos blockPos = paramBlockPos.below();
/* 37 */     return mayPlaceOn(paramLevelReader.getBlockState(blockPos), (BlockGetter)paramLevelReader, blockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 42 */     return paramBlockState.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 47 */     if (paramPathComputationType == PathComputationType.AIR && !this.hasCollision) {
/* 48 */       return true;
/*    */     }
/* 50 */     return super.isPathfindable(paramBlockState, paramPathComputationType);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\VegetationBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */