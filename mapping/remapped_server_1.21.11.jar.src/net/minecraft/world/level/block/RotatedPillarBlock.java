/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RotatedPillarBlock extends Block {
/* 12 */   public static final MapCodec<RotatedPillarBlock> CODEC = simpleCodec(RotatedPillarBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends RotatedPillarBlock> codec() {
/* 16 */     return CODEC;
/*    */   }
/*    */   
/* 19 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*    */   
/*    */   public RotatedPillarBlock(BlockBehaviour.Properties paramProperties) {
/* 22 */     super(paramProperties);
/* 23 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 28 */     return rotatePillar(paramBlockState, paramRotation);
/*    */   }
/*    */   
/*    */   public static BlockState rotatePillar(BlockState paramBlockState, Rotation paramRotation) {
/* 32 */     switch (paramRotation) {
/*    */       case COUNTERCLOCKWISE_90:
/*    */       case CLOCKWISE_90:
/* 35 */         switch ((Direction.Axis)paramBlockState.getValue((Property)AXIS)) {
/*    */           case COUNTERCLOCKWISE_90:
/* 37 */             return (BlockState)paramBlockState.setValue((Property)AXIS, (Comparable)Direction.Axis.Z);
/*    */           case CLOCKWISE_90:
/* 39 */             return (BlockState)paramBlockState.setValue((Property)AXIS, (Comparable)Direction.Axis.X);
/*    */         } 
/* 41 */         return paramBlockState;
/*    */     } 
/*    */     
/* 44 */     return paramBlockState;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 50 */     paramBuilder.add(new Property[] { (Property)AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 55 */     return (BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)paramBlockPlaceContext.getClickedFace().getAxis());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RotatedPillarBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */