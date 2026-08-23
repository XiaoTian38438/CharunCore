/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class HorizontalDirectionalBlock extends Block {
/* 10 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   protected HorizontalDirectionalBlock(BlockBehaviour.Properties paramProperties) {
/* 13 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends HorizontalDirectionalBlock> codec();
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 21 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 26 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HorizontalDirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */