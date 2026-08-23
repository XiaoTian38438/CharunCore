/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class IronBarsBlock extends CrossCollisionBlock {
/* 21 */   public static final MapCodec<IronBarsBlock> CODEC = simpleCodec(IronBarsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends IronBarsBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected IronBarsBlock(BlockBehaviour.Properties paramProperties) {
/* 29 */     super(2.0F, 16.0F, 2.0F, 16.0F, 16.0F, paramProperties);
/* 30 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 35 */     Level level = paramBlockPlaceContext.getLevel();
/* 36 */     BlockPos blockPos1 = paramBlockPlaceContext.getClickedPos();
/* 37 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*    */     
/* 39 */     BlockPos blockPos2 = blockPos1.north();
/* 40 */     BlockPos blockPos3 = blockPos1.south();
/* 41 */     BlockPos blockPos4 = blockPos1.west();
/* 42 */     BlockPos blockPos5 = blockPos1.east();
/*    */     
/* 44 */     BlockState blockState1 = level.getBlockState(blockPos2);
/* 45 */     BlockState blockState2 = level.getBlockState(blockPos3);
/* 46 */     BlockState blockState3 = level.getBlockState(blockPos4);
/* 47 */     BlockState blockState4 = level.getBlockState(blockPos5);
/*    */     
/* 49 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 50 */       .setValue((Property)NORTH, Boolean.valueOf(attachsTo(blockState1, blockState1.isFaceSturdy((BlockGetter)level, blockPos2, Direction.SOUTH)))))
/* 51 */       .setValue((Property)SOUTH, Boolean.valueOf(attachsTo(blockState2, blockState2.isFaceSturdy((BlockGetter)level, blockPos3, Direction.NORTH)))))
/* 52 */       .setValue((Property)WEST, Boolean.valueOf(attachsTo(blockState3, blockState3.isFaceSturdy((BlockGetter)level, blockPos4, Direction.EAST)))))
/* 53 */       .setValue((Property)EAST, Boolean.valueOf(attachsTo(blockState4, blockState4.isFaceSturdy((BlockGetter)level, blockPos5, Direction.WEST)))))
/* 54 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 60 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 61 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/* 63 */     if (paramDirection.getAxis().isHorizontal()) {
/* 64 */       return (BlockState)paramBlockState1.setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), Boolean.valueOf(attachsTo(paramBlockState2, paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, paramDirection.getOpposite()))));
/*    */     }
/* 66 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 71 */     return Shapes.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/* 77 */     if (paramBlockState2.is(this) || (paramBlockState2.is(BlockTags.BARS) && paramBlockState1.is(BlockTags.BARS) && paramBlockState2.hasProperty((Property)PROPERTY_BY_DIRECTION.get(paramDirection.getOpposite())))) {
/* 78 */       if (!paramDirection.getAxis().isHorizontal()) {
/* 79 */         return true;
/*    */       }
/* 81 */       if (((Boolean)paramBlockState1.getValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection))).booleanValue() && ((Boolean)paramBlockState2.getValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection.getOpposite()))).booleanValue()) {
/* 82 */         return true;
/*    */       }
/*    */     } 
/* 85 */     return super.skipRendering(paramBlockState1, paramBlockState2, paramDirection);
/*    */   }
/*    */   
/*    */   public final boolean attachsTo(BlockState paramBlockState, boolean paramBoolean) {
/* 89 */     return ((!isExceptionForConnection(paramBlockState) && paramBoolean) || paramBlockState.getBlock() instanceof IronBarsBlock || paramBlockState.is(BlockTags.WALLS));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 94 */     paramBuilder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\IronBarsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */