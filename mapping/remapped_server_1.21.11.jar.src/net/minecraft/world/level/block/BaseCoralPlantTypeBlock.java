/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class BaseCoralPlantTypeBlock extends Block implements SimpleWaterloggedBlock {
/* 23 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 4.0D);
/*    */   
/*    */   protected BaseCoralPlantTypeBlock(BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramProperties);
/* 29 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends BaseCoralPlantTypeBlock> codec();
/*    */   
/*    */   protected void tryScheduleDieTick(BlockState paramBlockState, BlockGetter paramBlockGetter, ScheduledTickAccess paramScheduledTickAccess, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 36 */     if (!scanForWater(paramBlockState, paramBlockGetter, paramBlockPos)) {
/* 37 */       paramScheduledTickAccess.scheduleTick(paramBlockPos, this, 60 + paramRandomSource.nextInt(40));
/*    */     }
/*    */   }
/*    */   
/*    */   protected static boolean scanForWater(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 42 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 43 */       return true;
/*    */     }
/*    */     
/* 46 */     for (Direction direction : Direction.values()) {
/* 47 */       if (paramBlockGetter.getFluidState(paramBlockPos.relative(direction)).is(FluidTags.WATER)) {
/* 48 */         return true;
/*    */       }
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 56 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*    */     
/* 58 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 63 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 68 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 69 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/*    */     
/* 72 */     if (paramDirection == Direction.DOWN && !canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/* 73 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 75 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 80 */     BlockPos blockPos = paramBlockPos.below();
/* 81 */     return paramLevelReader.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramLevelReader, blockPos, Direction.UP);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 86 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 91 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 92 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 95 */     return super.getFluidState(paramBlockState);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BaseCoralPlantTypeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */