/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class FaceAttachedHorizontalDirectionalBlock extends HorizontalDirectionalBlock {
/* 17 */   public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
/*    */   
/*    */   protected FaceAttachedHorizontalDirectionalBlock(BlockBehaviour.Properties paramProperties) {
/* 20 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec();
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 28 */     return canAttach(paramLevelReader, paramBlockPos, getConnectedDirection(paramBlockState).getOpposite());
/*    */   }
/*    */   
/*    */   public static boolean canAttach(LevelReader paramLevelReader, BlockPos paramBlockPos, Direction paramDirection) {
/* 32 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 33 */     return paramLevelReader.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramLevelReader, blockPos, paramDirection.getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 38 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/*    */       BlockState blockState;
/* 40 */       if (direction.getAxis() == Direction.Axis.Y) {
/* 41 */         blockState = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACE, (direction == Direction.UP) ? (Comparable)AttachFace.CEILING : (Comparable)AttachFace.FLOOR)).setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection());
/*    */       } else {
/* 43 */         blockState = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACE, (Comparable)AttachFace.WALL)).setValue((Property)FACING, (Comparable)direction.getOpposite());
/*    */       } 
/*    */       
/* 46 */       if (blockState.canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/* 47 */         return blockState;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 56 */     if (getConnectedDirection(paramBlockState1).getOpposite() == paramDirection && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 57 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 59 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */   
/*    */   protected static Direction getConnectedDirection(BlockState paramBlockState) {
/* 63 */     switch ((AttachFace)paramBlockState.getValue((Property)FACE)) {
/*    */       case CEILING:
/* 65 */         return Direction.DOWN;
/*    */       case FLOOR:
/* 67 */         return Direction.UP;
/*    */     } 
/* 69 */     return (Direction)paramBlockState.getValue((Property)FACING);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FaceAttachedHorizontalDirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */