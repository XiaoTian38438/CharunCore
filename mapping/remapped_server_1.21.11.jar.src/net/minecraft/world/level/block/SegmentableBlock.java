/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public interface SegmentableBlock
/*    */ {
/*    */   public static final int MIN_SEGMENT = 1;
/*    */   public static final int MAX_SEGMENT = 4;
/* 19 */   public static final IntegerProperty AMOUNT = BlockStateProperties.SEGMENT_AMOUNT;
/*    */   
/*    */   default Function<BlockState, VoxelShape> getShapeCalculator(EnumProperty<Direction> paramEnumProperty, IntegerProperty paramIntegerProperty) {
/* 22 */     Map map = Shapes.rotateHorizontal(Block.box(0.0D, 0.0D, 0.0D, 8.0D, getShapeHeight(), 8.0D));
/* 23 */     return paramBlockState -> {
/*    */         VoxelShape voxelShape = Shapes.empty();
/*    */         Direction direction = (Direction)paramBlockState.getValue((Property)paramEnumProperty);
/*    */         int i = ((Integer)paramBlockState.getValue((Property)paramIntegerProperty)).intValue();
/*    */         for (byte b = 0; b < i; b++) {
/*    */           voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap.get(direction));
/*    */           direction = direction.getCounterClockWise();
/*    */         } 
/*    */         return voxelShape.singleEncompassing();
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default IntegerProperty getSegmentAmountProperty() {
/* 38 */     return AMOUNT;
/*    */   }
/*    */   
/*    */   default double getShapeHeight() {
/* 42 */     return 1.0D;
/*    */   }
/*    */   
/*    */   default boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext, IntegerProperty paramIntegerProperty) {
/* 46 */     return (!paramBlockPlaceContext.isSecondaryUseActive() && paramBlockPlaceContext.getItemInHand().is(paramBlockState.getBlock().asItem()) && ((Integer)paramBlockState.getValue((Property)paramIntegerProperty)).intValue() < 4);
/*    */   }
/*    */ 
/*    */   
/*    */   default BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext, Block paramBlock, IntegerProperty paramIntegerProperty, EnumProperty<Direction> paramEnumProperty) {
/* 51 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/* 52 */     if (blockState.is(paramBlock)) {
/* 53 */       return (BlockState)blockState.setValue((Property)paramIntegerProperty, Integer.valueOf(Math.min(4, ((Integer)blockState.getValue((Property)paramIntegerProperty)).intValue() + 1)));
/*    */     }
/* 55 */     return (BlockState)paramBlock.defaultBlockState().setValue((Property)paramEnumProperty, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SegmentableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */