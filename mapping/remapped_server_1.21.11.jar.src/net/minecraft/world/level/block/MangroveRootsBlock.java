/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class MangroveRootsBlock extends Block implements SimpleWaterloggedBlock {
/* 19 */   public static final MapCodec<MangroveRootsBlock> CODEC = simpleCodec(MangroveRootsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<MangroveRootsBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/*    */   protected MangroveRootsBlock(BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties);
/* 30 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/* 36 */     return (paramBlockState2.is(Blocks.MANGROVE_ROOTS) && paramDirection.getAxis() == Direction.Axis.Y);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 41 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 42 */     boolean bool = (fluidState.getType() == Fluids.WATER) ? true : false;
/* 43 */     return (BlockState)super.getStateForPlacement(paramBlockPlaceContext).setValue((Property)WATERLOGGED, Boolean.valueOf(bool));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 48 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 49 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/*    */     
/* 52 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 57 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 58 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 61 */     return super.getFluidState(paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 66 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MangroveRootsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */