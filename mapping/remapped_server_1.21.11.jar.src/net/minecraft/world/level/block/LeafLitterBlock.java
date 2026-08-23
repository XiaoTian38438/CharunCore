/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class LeafLitterBlock extends VegetationBlock implements SegmentableBlock {
/* 19 */   public static final MapCodec<LeafLitterBlock> CODEC = simpleCodec(LeafLitterBlock::new);
/*    */   
/* 21 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   private final Function<BlockState, VoxelShape> shapes;
/*    */   
/*    */   public LeafLitterBlock(BlockBehaviour.Properties paramProperties) {
/* 26 */     super(paramProperties);
/* 27 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)getSegmentAmountProperty(), Integer.valueOf(1)));
/* 28 */     this.shapes = makeShapes();
/*    */   }
/*    */   
/*    */   private Function<BlockState, VoxelShape> makeShapes() {
/* 32 */     return getShapeForEachState(getShapeCalculator(FACING, getSegmentAmountProperty()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<LeafLitterBlock> codec() {
/* 37 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 42 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 47 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/* 52 */     if (canBeReplaced(paramBlockState, paramBlockPlaceContext, getSegmentAmountProperty())) {
/* 53 */       return true;
/*    */     }
/* 55 */     return super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 60 */     BlockPos blockPos = paramBlockPos.below();
/* 61 */     return paramLevelReader.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramLevelReader, blockPos, Direction.UP);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 66 */     return this.shapes.apply(paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 71 */     return getStateForPlacement(paramBlockPlaceContext, this, getSegmentAmountProperty(), FACING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 76 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)getSegmentAmountProperty() });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LeafLitterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */