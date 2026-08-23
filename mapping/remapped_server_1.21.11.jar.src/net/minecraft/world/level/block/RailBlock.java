/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ 
/*    */ public class RailBlock extends BaseRailBlock {
/* 14 */   public static final MapCodec<RailBlock> CODEC = simpleCodec(RailBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<RailBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/* 21 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;
/*    */   
/*    */   protected RailBlock(BlockBehaviour.Properties paramProperties) {
/* 24 */     super(false, paramProperties);
/* 25 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateState(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock) {
/* 30 */     if (paramBlock.defaultBlockState().isSignalSource() && (
/* 31 */       new RailState(paramLevel, paramBlockPos, paramBlockState)).countPotentialConnections() == 3) {
/* 32 */       updateDir(paramLevel, paramBlockPos, paramBlockState, false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Property<RailShape> getShapeProperty() {
/* 39 */     return (Property<RailShape>)SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 44 */     RailShape railShape1 = (RailShape)paramBlockState.getValue((Property)SHAPE);
/* 45 */     RailShape railShape2 = rotate(railShape1, paramRotation);
/* 46 */     return (BlockState)paramBlockState.setValue((Property)SHAPE, (Comparable)railShape2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 51 */     RailShape railShape1 = (RailShape)paramBlockState.getValue((Property)SHAPE);
/* 52 */     RailShape railShape2 = mirror(railShape1, paramMirror);
/* 53 */     return (BlockState)paramBlockState.setValue((Property)SHAPE, (Comparable)railShape2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 58 */     paramBuilder.add(new Property[] { (Property)SHAPE, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */