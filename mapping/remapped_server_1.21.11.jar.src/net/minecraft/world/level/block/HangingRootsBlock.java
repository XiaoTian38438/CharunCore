/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class HangingRootsBlock extends Block implements SimpleWaterloggedBlock {
/* 22 */   public static final MapCodec<HangingRootsBlock> CODEC = simpleCodec(HangingRootsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<HangingRootsBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/* 29 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 10.0D, 16.0D);
/*    */   
/*    */   protected HangingRootsBlock(BlockBehaviour.Properties paramProperties) {
/* 34 */     super(paramProperties);
/* 35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 40 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 45 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 46 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 48 */     return super.getFluidState(paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 53 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/* 54 */     if (blockState != null) {
/* 55 */       FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 56 */       return (BlockState)blockState.setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*    */     } 
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 63 */     BlockPos blockPos = paramBlockPos.above();
/* 64 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 65 */     return blockState.isFaceSturdy((BlockGetter)paramLevelReader, blockPos, Direction.DOWN);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 70 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 75 */     if (paramDirection == Direction.UP && !canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/* 76 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 78 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 79 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/* 81 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HangingRootsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */