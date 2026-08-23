/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class GrowingPlantBlock extends Block {
/*    */   protected final Direction growthDirection;
/*    */   protected final boolean scheduleFluidTicks;
/*    */   protected final VoxelShape shape;
/*    */   
/*    */   protected GrowingPlantBlock(BlockBehaviour.Properties paramProperties, Direction paramDirection, VoxelShape paramVoxelShape, boolean paramBoolean) {
/* 22 */     super(paramProperties);
/* 23 */     this.growthDirection = paramDirection;
/* 24 */     this.shape = paramVoxelShape;
/* 25 */     this.scheduleFluidTicks = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends GrowingPlantBlock> codec();
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 33 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().relative(this.growthDirection));
/* 34 */     if (blockState.is(getHeadBlock()) || blockState.is(getBodyBlock())) {
/* 35 */       return getBodyBlock().defaultBlockState();
/*    */     }
/* 37 */     return getStateForPlacement((paramBlockPlaceContext.getLevel()).random);
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(RandomSource paramRandomSource) {
/* 41 */     return defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 46 */     BlockPos blockPos = paramBlockPos.relative(this.growthDirection.getOpposite());
/* 47 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 48 */     if (!canAttachTo(blockState)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     return (blockState.is(getHeadBlock()) || blockState.is(getBodyBlock()) || blockState.isFaceSturdy((BlockGetter)paramLevelReader, blockPos, this.growthDirection));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 57 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/* 58 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean canAttachTo(BlockState paramBlockState) {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 68 */     return this.shape;
/*    */   }
/*    */   
/*    */   protected abstract GrowingPlantHeadBlock getHeadBlock();
/*    */   
/*    */   protected abstract Block getBodyBlock();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\GrowingPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */