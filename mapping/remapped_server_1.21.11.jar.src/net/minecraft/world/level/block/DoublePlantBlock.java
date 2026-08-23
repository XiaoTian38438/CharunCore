/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ 
/*     */ public class DoublePlantBlock extends VegetationBlock {
/*  26 */   public static final MapCodec<DoublePlantBlock> CODEC = simpleCodec(DoublePlantBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends DoublePlantBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*     */   
/*     */   public DoublePlantBlock(BlockBehaviour.Properties paramProperties) {
/*  36 */     super(paramProperties);
/*     */     
/*  38 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  43 */     DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)paramBlockState1.getValue((Property)HALF);
/*  44 */     if (paramDirection.getAxis() == Direction.Axis.Y) if (((doubleBlockHalf == DoubleBlockHalf.LOWER) ? true : false) == ((paramDirection == Direction.UP) ? true : false) && (
/*  45 */         !paramBlockState2.is(this) || paramBlockState2.getValue((Property)HALF) == doubleBlockHalf)) {
/*  46 */         return Blocks.AIR.defaultBlockState();
/*     */       }
/*     */ 
/*     */     
/*  50 */     if (doubleBlockHalf == DoubleBlockHalf.LOWER && paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  51 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  54 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  59 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*  60 */     Level level = paramBlockPlaceContext.getLevel();
/*  61 */     if (blockPos.getY() < level.getMaxY() && level.getBlockState(blockPos.above()).canBeReplaced(paramBlockPlaceContext)) {
/*  62 */       return super.getStateForPlacement(paramBlockPlaceContext);
/*     */     }
/*     */     
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  70 */     BlockPos blockPos = paramBlockPos.above();
/*  71 */     paramLevel.setBlock(blockPos, copyWaterloggedFrom((LevelReader)paramLevel, blockPos, (BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  77 */     if (paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/*  78 */       BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/*  79 */       return (blockState.is(this) && blockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/*  82 */     return super.canSurvive(paramBlockState, paramLevelReader, paramBlockPos);
/*     */   }
/*     */   
/*     */   public static void placeAt(LevelAccessor paramLevelAccessor, BlockState paramBlockState, BlockPos paramBlockPos, @UpdateFlags int paramInt) {
/*  86 */     BlockPos blockPos = paramBlockPos.above();
/*     */     
/*  88 */     paramLevelAccessor.setBlock(paramBlockPos, copyWaterloggedFrom((LevelReader)paramLevelAccessor, paramBlockPos, (BlockState)paramBlockState.setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)), paramInt);
/*  89 */     paramLevelAccessor.setBlock(blockPos, copyWaterloggedFrom((LevelReader)paramLevelAccessor, blockPos, (BlockState)paramBlockState.setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)), paramInt);
/*     */   }
/*     */   
/*     */   public static BlockState copyWaterloggedFrom(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  93 */     if (paramBlockState.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/*  94 */       return (BlockState)paramBlockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(paramLevelReader.isWaterAt(paramBlockPos)));
/*     */     }
/*  96 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 101 */     if (!paramLevel.isClientSide()) {
/* 102 */       if (paramPlayer.preventsBlockDrops()) {
/* 103 */         preventDropFromBottomPart(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */       } else {
/*     */         
/* 106 */         dropResources(paramBlockState, paramLevel, paramBlockPos, (BlockEntity)null, (Entity)paramPlayer, paramPlayer.getMainHandItem());
/*     */       } 
/*     */     }
/*     */     
/* 110 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level paramLevel, Player paramPlayer, BlockPos paramBlockPos, BlockState paramBlockState, BlockEntity paramBlockEntity, ItemStack paramItemStack) {
/* 116 */     super.playerDestroy(paramLevel, paramPlayer, paramBlockPos, Blocks.AIR.defaultBlockState(), paramBlockEntity, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void preventDropFromBottomPart(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 121 */     DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)paramBlockState.getValue((Property)HALF);
/* 122 */     if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
/* 123 */       BlockPos blockPos = paramBlockPos.below();
/* 124 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/* 125 */       if (blockState.is(paramBlockState.getBlock()) && blockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
/*     */         
/* 127 */         BlockState blockState1 = blockState.getFluidState().is((Fluid)Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
/* 128 */         paramLevel.setBlock(blockPos, blockState1, 35);
/* 129 */         paramLevel.levelEvent((Entity)paramPlayer, 2001, blockPos, Block.getId(blockState));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 136 */     paramBuilder.add(new Property[] { (Property)HALF });
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getSeed(BlockState paramBlockState, BlockPos paramBlockPos) {
/* 141 */     return Mth.getSeed(paramBlockPos.getX(), paramBlockPos.below((paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? 0 : 1).getY(), paramBlockPos.getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DoublePlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */