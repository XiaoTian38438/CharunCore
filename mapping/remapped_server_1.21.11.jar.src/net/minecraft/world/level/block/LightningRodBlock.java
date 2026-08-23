/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ 
/*     */ public class LightningRodBlock extends RodBlock implements SimpleWaterloggedBlock {
/*  28 */   public static final MapCodec<LightningRodBlock> CODEC = simpleCodec(LightningRodBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends LightningRodBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */   
/*  35 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  36 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   private static final int ACTIVATION_TICKS = 8;
/*     */   public static final int RANGE = 128;
/*     */   private static final int SPARK_CYCLE = 200;
/*     */   
/*     */   public LightningRodBlock(BlockBehaviour.Properties paramProperties) {
/*  42 */     super(paramProperties);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP)).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  48 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  49 */     boolean bool = (fluidState.getType() == Fluids.WATER) ? true : false;
/*  50 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getClickedFace())).setValue((Property)WATERLOGGED, Boolean.valueOf(bool));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  55 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  56 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  58 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  63 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  64 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  66 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  71 */     return ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  76 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() && paramBlockState.getValue((Property)FACING) == paramDirection) {
/*  77 */       return 15;
/*     */     }
/*  79 */     return 0;
/*     */   }
/*     */   
/*     */   public void onLightningStrike(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  83 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true)), 3);
/*  84 */     updateNeighbours(paramBlockState, paramLevel, paramBlockPos);
/*  85 */     paramLevel.scheduleTick(paramBlockPos, this, 8);
/*     */     
/*  87 */     paramLevel.levelEvent(3002, paramBlockPos, ((Direction)paramBlockState.getValue((Property)FACING)).getAxis().ordinal());
/*     */   }
/*     */   
/*     */   private void updateNeighbours(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  91 */     Direction direction = ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite();
/*  92 */     paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this, ExperimentalRedstoneUtils.initialOrientation(paramLevel, direction, null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  97 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false)), 3);
/*  98 */     updateNeighbours(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 103 */     if (!paramLevel.isThundering() || paramLevel.random
/* 104 */       .nextInt(200) > paramLevel.getGameTime() % 200L || paramBlockPos
/* 105 */       .getY() != paramLevel.getHeight(Heightmap.Types.WORLD_SURFACE, paramBlockPos.getX(), paramBlockPos.getZ()) - 1) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     ParticleUtils.spawnParticlesAlongAxis(((Direction)paramBlockState.getValue((Property)FACING)).getAxis(), paramLevel, paramBlockPos, 0.125D, (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 114 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/* 115 */       updateNeighbours(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 121 */     if (paramBlockState1.is(paramBlockState2.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     if (((Boolean)paramBlockState1.getValue((Property)POWERED)).booleanValue() && !paramLevel.getBlockTicks().hasScheduledTick(paramBlockPos, this)) {
/* 126 */       paramLevel.scheduleTick(paramBlockPos, this, 8);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 132 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 137 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LightningRodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */