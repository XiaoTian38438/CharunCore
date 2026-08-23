/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ChorusPlantBlock extends PipeBlock {
/*  17 */   public static final MapCodec<ChorusPlantBlock> CODEC = simpleCodec(ChorusPlantBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<ChorusPlantBlock> codec() {
/*  21 */     return CODEC;
/*     */   }
/*     */   
/*     */   protected ChorusPlantBlock(BlockBehaviour.Properties paramProperties) {
/*  25 */     super(10.0F, paramProperties);
/*     */     
/*  27 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)UP, Boolean.valueOf(false))).setValue((Property)DOWN, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  32 */     return getStateWithConnections((BlockGetter)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos(), defaultBlockState());
/*     */   }
/*     */   
/*     */   public static BlockState getStateWithConnections(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  36 */     BlockState blockState1 = paramBlockGetter.getBlockState(paramBlockPos.below());
/*  37 */     BlockState blockState2 = paramBlockGetter.getBlockState(paramBlockPos.above());
/*  38 */     BlockState blockState3 = paramBlockGetter.getBlockState(paramBlockPos.north());
/*  39 */     BlockState blockState4 = paramBlockGetter.getBlockState(paramBlockPos.east());
/*  40 */     BlockState blockState5 = paramBlockGetter.getBlockState(paramBlockPos.south());
/*  41 */     BlockState blockState6 = paramBlockGetter.getBlockState(paramBlockPos.west());
/*     */     
/*  43 */     Block block = paramBlockState.getBlock();
/*  44 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)paramBlockState
/*  45 */       .trySetValue((Property)DOWN, Boolean.valueOf((blockState1.is(block) || blockState1.is(Blocks.CHORUS_FLOWER) || blockState1.is(Blocks.END_STONE)))))
/*  46 */       .trySetValue((Property)UP, Boolean.valueOf((blockState2.is(block) || blockState2.is(Blocks.CHORUS_FLOWER)))))
/*  47 */       .trySetValue((Property)NORTH, Boolean.valueOf((blockState3.is(block) || blockState3.is(Blocks.CHORUS_FLOWER)))))
/*  48 */       .trySetValue((Property)EAST, Boolean.valueOf((blockState4.is(block) || blockState4.is(Blocks.CHORUS_FLOWER)))))
/*  49 */       .trySetValue((Property)SOUTH, Boolean.valueOf((blockState5.is(block) || blockState5.is(Blocks.CHORUS_FLOWER)))))
/*  50 */       .trySetValue((Property)WEST, Boolean.valueOf((blockState6.is(block) || blockState6.is(Blocks.CHORUS_FLOWER))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  56 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  57 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*  58 */       return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */     } 
/*     */     
/*  61 */     boolean bool = (paramBlockState2.is(this) || paramBlockState2.is(Blocks.CHORUS_FLOWER) || (paramDirection == Direction.DOWN && paramBlockState2.is(Blocks.END_STONE))) ? true : false;
/*     */     
/*  63 */     return (BlockState)paramBlockState1.setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), Boolean.valueOf(bool));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  68 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/*  69 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  78 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/*  79 */     boolean bool = (!paramLevelReader.getBlockState(paramBlockPos.above()).isAir() && !blockState.isAir()) ? true : false;
/*     */     
/*  81 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  82 */       BlockPos blockPos = paramBlockPos.relative(direction);
/*  83 */       BlockState blockState1 = paramLevelReader.getBlockState(blockPos);
/*  84 */       if (blockState1.is(this)) {
/*  85 */         if (bool) {
/*  86 */           return false;
/*     */         }
/*  88 */         BlockState blockState2 = paramLevelReader.getBlockState(blockPos.below());
/*  89 */         if (blockState2.is(this) || blockState2.is(Blocks.END_STONE)) {
/*  90 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  94 */     return (blockState.is(this) || blockState.is(Blocks.END_STONE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  99 */     paramBuilder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP, (Property)DOWN });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 104 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ChorusPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */