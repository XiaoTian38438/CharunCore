/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallSeagrassBlock extends DoublePlantBlock implements LiquidBlockContainer {
/* 24 */   public static final MapCodec<TallSeagrassBlock> CODEC = simpleCodec(TallSeagrassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<TallSeagrassBlock> codec() {
/* 28 */     return CODEC;
/*    */   }
/*    */   
/* 31 */   public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 16.0D);
/*    */   
/*    */   public TallSeagrassBlock(BlockBehaviour.Properties paramProperties) {
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
/*    */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 51 */     return new ItemStack(Blocks.SEAGRASS);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 56 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/*    */     
/* 58 */     if (blockState != null) {
/* 59 */       FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos().above());
/* 60 */       if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
/* 61 */         return blockState;
/*    */       }
/*    */     } 
/*    */     
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 70 */     if (paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/* 71 */       BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/* 72 */       return (blockState.is(this) && blockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*    */     } 
/*    */     
/* 75 */     FluidState fluidState = paramLevelReader.getFluidState(paramBlockPos);
/* 76 */     return (super.canSurvive(paramBlockState, paramLevelReader, paramBlockPos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 81 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(LivingEntity paramLivingEntity, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid) {
/* 86 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\TallSeagrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */