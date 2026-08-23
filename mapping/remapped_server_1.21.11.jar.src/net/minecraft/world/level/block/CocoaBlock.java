/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
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
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CocoaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
/*  29 */   public static final MapCodec<CocoaBlock> CODEC = simpleCodec(CocoaBlock::new);
/*     */   public static final int MAX_AGE = 2;
/*     */   
/*     */   public MapCodec<CocoaBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
/*     */   private static final List<Map<Direction, VoxelShape>> SHAPES;
/*     */   
/*     */   static {
/*  41 */     SHAPES = IntStream.rangeClosed(0, 2).<Map<Direction, VoxelShape>>mapToObj(paramInt -> Shapes.rotateHorizontal(Block.column((4 + paramInt * 2), (7 - paramInt * 2), 12.0D).move(0.0D, 0.0D, (paramInt - 5) / 16.0D).optimize())).toList();
/*     */   }
/*     */   public CocoaBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  50 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  55 */     if (paramServerLevel.random.nextInt(5) == 0) {
/*  56 */       int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  57 */       if (i < 2) {
/*  58 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1)), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  65 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.relative((Direction)paramBlockState.getValue((Property)FACING)));
/*  66 */     return blockState.is(BlockTags.JUNGLE_LOGS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  71 */     return (VoxelShape)((Map)SHAPES.get(((Integer)paramBlockState.getValue((Property)AGE)).intValue())).get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  76 */     BlockState blockState = defaultBlockState();
/*     */     
/*  78 */     Level level = paramBlockPlaceContext.getLevel();
/*  79 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*     */     
/*  81 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/*  82 */       if (direction.getAxis().isHorizontal()) {
/*  83 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction);
/*  84 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/*  85 */           return blockState;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  95 */     if (paramDirection == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 104 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 114 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(((Integer)paramBlockState.getValue((Property)AGE)).intValue() + 1)), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 119 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 124 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CocoaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */