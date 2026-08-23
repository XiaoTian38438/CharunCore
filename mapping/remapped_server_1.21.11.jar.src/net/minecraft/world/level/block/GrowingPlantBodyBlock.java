/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.BlockUtil;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class GrowingPlantBodyBlock extends GrowingPlantBlock implements BonemealableBlock {
/*    */   protected GrowingPlantBodyBlock(BlockBehaviour.Properties paramProperties, Direction paramDirection, VoxelShape paramVoxelShape, boolean paramBoolean) {
/* 23 */     super(paramProperties, paramDirection, paramVoxelShape, paramBoolean);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends GrowingPlantBodyBlock> codec();
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateHeadAfterConvertedFromBody(BlockState paramBlockState1, BlockState paramBlockState2) {
/* 33 */     return paramBlockState2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 38 */     if (paramDirection == this.growthDirection.getOpposite() && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 39 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*    */     }
/*    */     
/* 42 */     GrowingPlantHeadBlock growingPlantHeadBlock = getHeadBlock();
/* 43 */     if (paramDirection == this.growthDirection && 
/* 44 */       !paramBlockState2.is(this) && !paramBlockState2.is(growingPlantHeadBlock))
/*    */     {
/* 46 */       return updateHeadAfterConvertedFromBody(paramBlockState1, growingPlantHeadBlock.getStateForPlacement(paramRandomSource));
/*    */     }
/*    */ 
/*    */     
/* 50 */     if (this.scheduleFluidTicks) {
/* 51 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/*    */     
/* 54 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 59 */     return new ItemStack(getHeadBlock());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 64 */     Optional<BlockPos> optional = getHeadPos((BlockGetter)paramLevelReader, paramBlockPos, paramBlockState.getBlock());
/* 65 */     return (optional.isPresent() && getHeadBlock().canGrowInto(paramLevelReader.getBlockState(((BlockPos)optional.get()).relative(this.growthDirection))));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 75 */     Optional<BlockPos> optional = getHeadPos((BlockGetter)paramServerLevel, paramBlockPos, paramBlockState.getBlock());
/*    */     
/* 77 */     if (optional.isPresent()) {
/* 78 */       BlockState blockState = paramServerLevel.getBlockState(optional.get());
/* 79 */       ((GrowingPlantHeadBlock)blockState.getBlock()).performBonemeal(paramServerLevel, paramRandomSource, optional.get(), blockState);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Optional<BlockPos> getHeadPos(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Block paramBlock) {
/* 84 */     return BlockUtil.getTopConnectedBlock(paramBlockGetter, paramBlockPos, paramBlock, this.growthDirection, getHeadBlock());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/* 89 */     boolean bool = super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/* 90 */     if (bool && paramBlockPlaceContext.getItemInHand().is(getHeadBlock().asItem())) {
/* 91 */       return false;
/*    */     }
/* 93 */     return bool;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 98 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\GrowingPlantBodyBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */