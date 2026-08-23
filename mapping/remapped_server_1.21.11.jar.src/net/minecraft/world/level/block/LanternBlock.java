/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LanternBlock extends Block implements SimpleWaterloggedBlock {
/*  24 */   public static final MapCodec<LanternBlock> CODEC = simpleCodec(LanternBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends LanternBlock> codec() {
/*  28 */     return CODEC;
/*     */   }
/*     */   
/*  31 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*  32 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  34 */   private static final VoxelShape SHAPE_STANDING = Shapes.or(
/*  35 */       Block.column(4.0D, 7.0D, 9.0D), 
/*  36 */       Block.column(6.0D, 0.0D, 7.0D));
/*     */   
/*  38 */   private static final VoxelShape SHAPE_HANGING = SHAPE_STANDING.move(0.0D, 0.0625D, 0.0D).optimize();
/*     */   
/*     */   public LanternBlock(BlockBehaviour.Properties paramProperties) {
/*  41 */     super(paramProperties);
/*  42 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HANGING, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  47 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */     
/*  49 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/*     */       
/*  51 */       if (direction.getAxis() == Direction.Axis.Y) {
/*  52 */         BlockState blockState = (BlockState)defaultBlockState().setValue((Property)HANGING, Boolean.valueOf((direction == Direction.UP)));
/*     */         
/*  54 */         if (blockState.canSurvive((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos())) {
/*  55 */           return (BlockState)blockState.setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  65 */     return ((Boolean)paramBlockState.getValue((Property)HANGING)).booleanValue() ? SHAPE_HANGING : SHAPE_STANDING;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  70 */     paramBuilder.add(new Property[] { (Property)HANGING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  75 */     Direction direction = getConnectedDirection(paramBlockState).getOpposite();
/*  76 */     return Block.canSupportCenter(paramLevelReader, paramBlockPos.relative(direction), direction.getOpposite());
/*     */   }
/*     */   
/*     */   protected static Direction getConnectedDirection(BlockState paramBlockState) {
/*  80 */     return ((Boolean)paramBlockState.getValue((Property)HANGING)).booleanValue() ? Direction.DOWN : Direction.UP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  85 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  86 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  88 */     if (getConnectedDirection(paramBlockState1).getOpposite() == paramDirection && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  89 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  91 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  96 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  97 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  99 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 104 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LanternBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */