/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ public interface SpawnPlacementTypes
/*    */ {
/*    */   static {
/* 14 */     IN_WATER = ((paramLevelReader, paramBlockPos, paramEntityType) -> {
/*    */         if (paramEntityType == null || !paramLevelReader.getWorldBorder().isWithinBounds(paramBlockPos)) {
/*    */           return false;
/*    */         }
/*    */ 
/*    */         
/*    */         BlockPos blockPos = paramBlockPos.above();
/*    */         
/* 22 */         return (paramLevelReader.getFluidState(paramBlockPos).is(FluidTags.WATER) && !paramLevelReader.getBlockState(blockPos).isRedstoneConductor((BlockGetter)paramLevelReader, blockPos));
/*    */       });
/*    */ 
/*    */     
/* 26 */     IN_LAVA = ((paramLevelReader, paramBlockPos, paramEntityType) -> 
/* 27 */       (paramEntityType == null || !paramLevelReader.getWorldBorder().isWithinBounds(paramBlockPos)) ? false : paramLevelReader.getFluidState(paramBlockPos).is(FluidTags.LAVA));
/*    */   }
/*    */   
/*    */   public static final SpawnPlacementType NO_RESTRICTIONS = (paramLevelReader, paramBlockPos, paramEntityType) -> true;
/*    */   public static final SpawnPlacementType IN_WATER;
/*    */   public static final SpawnPlacementType IN_LAVA;
/*    */   
/* 34 */   public static final SpawnPlacementType ON_GROUND = new SpawnPlacementType()
/*    */     {
/*    */       public boolean isSpawnPositionOk(LevelReader param1LevelReader, BlockPos param1BlockPos, EntityType<?> param1EntityType) {
/* 37 */         if (param1EntityType == null || !param1LevelReader.getWorldBorder().isWithinBounds(param1BlockPos)) {
/* 38 */           return false;
/*    */         }
/*    */         
/* 41 */         BlockPos blockPos1 = param1BlockPos.above();
/* 42 */         BlockPos blockPos2 = param1BlockPos.below();
/*    */         
/* 44 */         BlockState blockState = param1LevelReader.getBlockState(blockPos2);
/* 45 */         if (!blockState.isValidSpawn((BlockGetter)param1LevelReader, blockPos2, param1EntityType)) {
/* 46 */           return false;
/*    */         }
/*    */ 
/*    */         
/* 50 */         return (isValidEmptySpawnBlock(param1LevelReader, param1BlockPos, param1EntityType) && 
/* 51 */           isValidEmptySpawnBlock(param1LevelReader, blockPos1, param1EntityType));
/*    */       }
/*    */       
/*    */       private boolean isValidEmptySpawnBlock(LevelReader param1LevelReader, BlockPos param1BlockPos, EntityType<?> param1EntityType) {
/* 55 */         BlockState blockState = param1LevelReader.getBlockState(param1BlockPos);
/* 56 */         return NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)param1LevelReader, param1BlockPos, blockState, blockState.getFluidState(), param1EntityType);
/*    */       }
/*    */ 
/*    */       
/*    */       public BlockPos adjustSpawnPosition(LevelReader param1LevelReader, BlockPos param1BlockPos) {
/* 61 */         BlockPos blockPos = param1BlockPos.below();
/* 62 */         if (param1LevelReader.getBlockState(blockPos).isPathfindable(PathComputationType.LAND)) {
/* 63 */           return blockPos;
/*    */         }
/*    */         
/* 66 */         return param1BlockPos;
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SpawnPlacementTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */