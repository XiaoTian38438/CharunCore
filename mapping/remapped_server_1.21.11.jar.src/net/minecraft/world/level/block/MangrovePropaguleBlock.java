/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.grower.TreeGrower;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MangrovePropaguleBlock extends SaplingBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  27 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)TreeGrower.CODEC.fieldOf("tree").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, MangrovePropaguleBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<MangrovePropaguleBlock> CODEC;
/*     */   
/*     */   public MapCodec<MangrovePropaguleBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
/*     */   
/*     */   public static final int MAX_AGE = 4;
/*  40 */   private static final int[] SHAPE_MIN_Y = new int[] { 13, 10, 7, 3, 0 }; private static final VoxelShape[] SHAPE_PER_AGE; static {
/*  41 */     SHAPE_PER_AGE = Block.boxes(4, paramInt -> Block.column(2.0D, SHAPE_MIN_Y[paramInt], 16.0D));
/*     */   }
/*  43 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  44 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*     */   
/*     */   public MangrovePropaguleBlock(TreeGrower paramTreeGrower, BlockBehaviour.Properties paramProperties) {
/*  47 */     super(paramTreeGrower, paramProperties);
/*  48 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  49 */         .setValue((Property)STAGE, Integer.valueOf(0)))
/*  50 */         .setValue((Property)AGE, Integer.valueOf(0)))
/*  51 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)))
/*  52 */         .setValue((Property)HANGING, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  58 */     paramBuilder.add(new Property[] { (Property)STAGE }).add(new Property[] { (Property)AGE }).add(new Property[] { (Property)WATERLOGGED }).add(new Property[] { (Property)HANGING });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  63 */     return (super.mayPlaceOn(paramBlockState, paramBlockGetter, paramBlockPos) || paramBlockState.is(Blocks.CLAY));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  68 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  69 */     boolean bool = (fluidState.getType() == Fluids.WATER) ? true : false;
/*  70 */     return (BlockState)((BlockState)super.getStateForPlacement(paramBlockPlaceContext).setValue((Property)WATERLOGGED, Boolean.valueOf(bool))).setValue((Property)AGE, Integer.valueOf(4));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  75 */     boolean bool = ((Boolean)paramBlockState.getValue((Property)HANGING)).booleanValue() ? ((Integer)paramBlockState.getValue((Property)AGE)).intValue() : true;
/*  76 */     return SHAPE_PER_AGE[bool].move(paramBlockState.getOffset(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  81 */     if (isHanging(paramBlockState)) {
/*  82 */       return paramLevelReader.getBlockState(paramBlockPos.above()).is(Blocks.MANGROVE_LEAVES);
/*     */     }
/*  84 */     return super.canSurvive(paramBlockState, paramLevelReader, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  89 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  90 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  92 */     if (paramDirection == Direction.UP && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  93 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  95 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 100 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 101 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 103 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 108 */     if (!isHanging(paramBlockState)) {
/*     */ 
/*     */       
/* 111 */       if (paramRandomSource.nextInt(7) == 0) {
/* 112 */         advanceTree(paramServerLevel, paramBlockPos, paramBlockState, paramRandomSource);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     if (!isFullyGrown(paramBlockState)) {
/* 118 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)AGE), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 124 */     return (!isHanging(paramBlockState) || !isFullyGrown(paramBlockState));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 129 */     return isHanging(paramBlockState) ? (!isFullyGrown(paramBlockState)) : super.isBonemealSuccess(paramLevel, paramRandomSource, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 134 */     if (isHanging(paramBlockState) && !isFullyGrown(paramBlockState)) {
/* 135 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)AGE), 2);
/*     */     } else {
/* 137 */       super.performBonemeal(paramServerLevel, paramRandomSource, paramBlockPos, paramBlockState);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isHanging(BlockState paramBlockState) {
/* 142 */     return ((Boolean)paramBlockState.getValue((Property)HANGING)).booleanValue();
/*     */   }
/*     */   
/*     */   private static boolean isFullyGrown(BlockState paramBlockState) {
/* 146 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() == 4);
/*     */   }
/*     */   
/*     */   public static BlockState createNewHangingPropagule() {
/* 150 */     return createNewHangingPropagule(0);
/*     */   }
/*     */   
/*     */   public static BlockState createNewHangingPropagule(int paramInt) {
/* 154 */     return (BlockState)((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState()
/* 155 */       .setValue((Property)HANGING, Boolean.valueOf(true)))
/* 156 */       .setValue((Property)AGE, Integer.valueOf(paramInt));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MangrovePropaguleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */