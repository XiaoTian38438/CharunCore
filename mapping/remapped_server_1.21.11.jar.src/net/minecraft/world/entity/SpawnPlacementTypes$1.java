/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements SpawnPlacementType
/*    */ {
/*    */   public boolean isSpawnPositionOk(LevelReader paramLevelReader, BlockPos paramBlockPos, EntityType<?> paramEntityType) {
/* 37 */     if (paramEntityType == null || !paramLevelReader.getWorldBorder().isWithinBounds(paramBlockPos)) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     BlockPos blockPos1 = paramBlockPos.above();
/* 42 */     BlockPos blockPos2 = paramBlockPos.below();
/*    */     
/* 44 */     BlockState blockState = paramLevelReader.getBlockState(blockPos2);
/* 45 */     if (!blockState.isValidSpawn((BlockGetter)paramLevelReader, blockPos2, paramEntityType)) {
/* 46 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 50 */     return (isValidEmptySpawnBlock(paramLevelReader, paramBlockPos, paramEntityType) && 
/* 51 */       isValidEmptySpawnBlock(paramLevelReader, blockPos1, paramEntityType));
/*    */   }
/*    */   
/*    */   private boolean isValidEmptySpawnBlock(LevelReader paramLevelReader, BlockPos paramBlockPos, EntityType<?> paramEntityType) {
/* 55 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos);
/* 56 */     return NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)paramLevelReader, paramBlockPos, blockState, blockState.getFluidState(), paramEntityType);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos adjustSpawnPosition(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 61 */     BlockPos blockPos = paramBlockPos.below();
/* 62 */     if (paramLevelReader.getBlockState(blockPos).isPathfindable(PathComputationType.LAND)) {
/* 63 */       return blockPos;
/*    */     }
/*    */     
/* 66 */     return paramBlockPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SpawnPlacementTypes$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */