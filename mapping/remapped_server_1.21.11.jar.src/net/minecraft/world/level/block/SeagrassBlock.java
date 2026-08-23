/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SeagrassBlock extends VegetationBlock implements BonemealableBlock, LiquidBlockContainer {
/* 26 */   public static final MapCodec<SeagrassBlock> CODEC = simpleCodec(SeagrassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SeagrassBlock> codec() {
/* 30 */     return CODEC;
/*    */   }
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*    */   
/*    */   protected SeagrassBlock(BlockBehaviour.Properties paramProperties) {
/* 36 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 41 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 46 */     return (paramBlockState.isFaceSturdy(paramBlockGetter, paramBlockPos, Direction.UP) && !paramBlockState.is(Blocks.MAGMA_BLOCK));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 51 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*    */     
/* 53 */     if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
/* 54 */       return super.getStateForPlacement(paramBlockPlaceContext);
/*    */     }
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 61 */     BlockState blockState = super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/* 62 */     if (!blockState.isAir()) {
/* 63 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/* 65 */     return blockState;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 70 */     return paramLevelReader.getBlockState(paramBlockPos.above()).is(Blocks.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 75 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 80 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 85 */     BlockState blockState1 = Blocks.TALL_SEAGRASS.defaultBlockState();
/* 86 */     BlockState blockState2 = (BlockState)blockState1.setValue((Property)TallSeagrassBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 87 */     BlockPos blockPos = paramBlockPos.above();
/* 88 */     paramServerLevel.setBlock(paramBlockPos, blockState1, 2);
/* 89 */     paramServerLevel.setBlock(blockPos, blockState2, 2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(LivingEntity paramLivingEntity, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid) {
/* 94 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 99 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SeagrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */