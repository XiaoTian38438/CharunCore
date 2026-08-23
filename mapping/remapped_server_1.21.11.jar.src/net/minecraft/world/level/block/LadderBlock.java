/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LadderBlock extends Block implements SimpleWaterloggedBlock {
/*  26 */   public static final MapCodec<LadderBlock> CODEC = simpleCodec(LadderBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<LadderBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  34 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  36 */   public static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   protected LadderBlock(BlockBehaviour.Properties paramProperties) {
/*  39 */     super(paramProperties);
/*  40 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  45 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   private boolean canAttachTo(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  49 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos);
/*  50 */     return blockState.isFaceSturdy(paramBlockGetter, paramBlockPos, paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  55 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*  56 */     return canAttachTo((BlockGetter)paramLevelReader, paramBlockPos.relative(direction.getOpposite()), direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  61 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  62 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  64 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  65 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  68 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  74 */     if (!paramBlockPlaceContext.replacingClickedOnBlock()) {
/*  75 */       BlockState blockState1 = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().relative(paramBlockPlaceContext.getClickedFace().getOpposite()));
/*  76 */       if (blockState1.is(this) && blockState1.getValue((Property)FACING) == paramBlockPlaceContext.getClickedFace()) {
/*  77 */         return null;
/*     */       }
/*     */     } 
/*     */     
/*  81 */     BlockState blockState = defaultBlockState();
/*     */     
/*  83 */     Level level = paramBlockPlaceContext.getLevel();
/*  84 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*  85 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */     
/*  87 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/*  88 */       if (direction.getAxis().isHorizontal()) {
/*  89 */         blockState = (BlockState)blockState.setValue((Property)FACING, (Comparable)direction.getOpposite());
/*  90 */         if (blockState.canSurvive((LevelReader)level, blockPos)) {
/*  91 */           return (BlockState)blockState.setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 101 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 106 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 111 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 116 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 117 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 119 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LadderBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */