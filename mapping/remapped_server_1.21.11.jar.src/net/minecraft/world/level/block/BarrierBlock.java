/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class BarrierBlock extends Block implements SimpleWaterloggedBlock {
/* 25 */   public static final MapCodec<BarrierBlock> CODEC = simpleCodec(BarrierBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BarrierBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/* 32 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/*    */   protected BarrierBlock(BlockBehaviour.Properties paramProperties) {
/* 35 */     super(paramProperties);
/* 36 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 41 */     return paramBlockState.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 46 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 51 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 56 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 57 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/* 59 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 64 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 65 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 67 */     return super.getFluidState(paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 72 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos()).getType() == Fluids.WATER)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 77 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack pickupBlock(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 82 */     if (paramLivingEntity instanceof Player) { Player player = (Player)paramLivingEntity; if (player.isCreative())
/*    */       {
/*    */         
/* 85 */         return super.pickupBlock(paramLivingEntity, paramLevelAccessor, paramBlockPos, paramBlockState); }  }
/*    */     
/*    */     return ItemStack.EMPTY;
/*    */   }
/*    */   public boolean canPlaceLiquid(LivingEntity paramLivingEntity, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid) {
/* 90 */     if (paramLivingEntity instanceof Player) { Player player = (Player)paramLivingEntity; if (player.isCreative())
/*    */       {
/*    */         
/* 93 */         return super.canPlaceLiquid(paramLivingEntity, paramBlockGetter, paramBlockPos, paramBlockState, paramFluid);
/*    */       } }
/*    */     
/*    */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BarrierBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */