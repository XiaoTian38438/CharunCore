/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class RodBlock extends DirectionalBlock {
/* 16 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateAllAxis(Block.cube(4.0D, 4.0D, 16.0D));
/*    */   
/*    */   protected RodBlock(BlockBehaviour.Properties paramProperties) {
/* 19 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends RodBlock> codec();
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 27 */     return SHAPES.get(((Direction)paramBlockState.getValue((Property)FACING)).getAxis());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 32 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 37 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramMirror.mirror((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */