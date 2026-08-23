/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class EndRodBlock extends RodBlock {
/* 14 */   public static final MapCodec<EndRodBlock> CODEC = simpleCodec(EndRodBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<EndRodBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected EndRodBlock(BlockBehaviour.Properties paramProperties) {
/* 22 */     super(paramProperties);
/* 23 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 29 */     Direction direction = paramBlockPlaceContext.getClickedFace();
/*    */     
/* 31 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().relative(direction.getOpposite()));
/* 32 */     if (blockState.is(this) && blockState.getValue((Property)FACING) == direction) {
/* 33 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)direction.getOpposite());
/*    */     }
/*    */     
/* 36 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)direction);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 41 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 42 */     double d1 = paramBlockPos.getX() + 0.55D - (paramRandomSource.nextFloat() * 0.1F);
/* 43 */     double d2 = paramBlockPos.getY() + 0.55D - (paramRandomSource.nextFloat() * 0.1F);
/* 44 */     double d3 = paramBlockPos.getZ() + 0.55D - (paramRandomSource.nextFloat() * 0.1F);
/* 45 */     double d4 = (0.4F - (paramRandomSource.nextFloat() + paramRandomSource.nextFloat()) * 0.4F);
/*    */     
/* 47 */     if (paramRandomSource.nextInt(5) == 0) {
/* 48 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.END_ROD, d1 + direction.getStepX() * d4, d2 + direction.getStepY() * d4, d3 + direction.getStepZ() * d4, paramRandomSource.nextGaussian() * 0.005D, paramRandomSource.nextGaussian() * 0.005D, paramRandomSource.nextGaussian() * 0.005D);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 54 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\EndRodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */