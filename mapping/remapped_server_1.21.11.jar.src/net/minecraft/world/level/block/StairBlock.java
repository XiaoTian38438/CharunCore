/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StairBlock extends Block implements SimpleWaterloggedBlock {
/*     */   public static final MapCodec<StairBlock> CODEC;
/*     */   
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockState.CODEC.fieldOf("base_state").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, StairBlock::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<? extends StairBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  41 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  42 */   public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
/*  43 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  45 */   private static final VoxelShape SHAPE_OUTER = Shapes.or(
/*  46 */       Block.column(16.0D, 0.0D, 8.0D), 
/*  47 */       Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D));
/*     */   
/*  49 */   private static final VoxelShape SHAPE_STRAIGHT = Shapes.or(SHAPE_OUTER, Shapes.rotate(SHAPE_OUTER, OctahedralGroup.BLOCK_ROT_Y_90));
/*  50 */   private static final VoxelShape SHAPE_INNER = Shapes.or(SHAPE_STRAIGHT, Shapes.rotate(SHAPE_STRAIGHT, OctahedralGroup.BLOCK_ROT_Y_90));
/*     */   
/*  52 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_OUTER = Shapes.rotateHorizontal(SHAPE_OUTER);
/*  53 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_STRAIGHT = Shapes.rotateHorizontal(SHAPE_STRAIGHT);
/*  54 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_INNER = Shapes.rotateHorizontal(SHAPE_INNER);
/*     */   
/*  56 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_OUTER = Shapes.rotateHorizontal(SHAPE_OUTER, OctahedralGroup.INVERT_Y);
/*  57 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_STRAIGHT = Shapes.rotateHorizontal(SHAPE_STRAIGHT, OctahedralGroup.INVERT_Y);
/*  58 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_INNER = Shapes.rotateHorizontal(SHAPE_INNER, OctahedralGroup.INVERT_Y);
/*     */   
/*     */   private final Block base;
/*     */   protected final BlockState baseState;
/*     */   
/*     */   protected StairBlock(BlockState paramBlockState, BlockBehaviour.Properties paramProperties) {
/*  64 */     super(paramProperties);
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)SHAPE, (Comparable)StairsShape.STRAIGHT)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*  66 */     this.base = paramBlockState.getBlock();
/*  67 */     this.baseState = paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  77 */     boolean bool = (paramBlockState.getValue((Property)HALF) == Half.BOTTOM) ? true : false;
/*  78 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*     */     
/*  80 */     switch ((StairsShape)paramBlockState.getValue((Property)SHAPE)) { default: throw new MatchException(null, null);
/*  81 */       case LEFT_RIGHT: if (bool);
/*  82 */       case null: case null: if (bool);
/*  83 */       case FRONT_BACK: case null: if (bool); break; }  switch ((StairsShape)paramBlockState
/*  84 */       .getValue((Property)SHAPE)) { default: throw new MatchException(null, null);case LEFT_RIGHT: case FRONT_BACK: case null: case null: case null: break; }  return SHAPE_TOP_OUTER.get(
/*     */ 
/*     */         
/*  87 */         direction.getClockWise());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getExplosionResistance() {
/*  93 */     return this.base.getExplosionResistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  98 */     Direction direction = paramBlockPlaceContext.getClickedFace();
/*  99 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 100 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(blockPos);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     BlockState blockState = (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection())).setValue((Property)HALF, (direction == Direction.DOWN || (direction != Direction.UP && (paramBlockPlaceContext.getClickLocation()).y - blockPos.getY() > 0.5D)) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM)).setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */     
/* 107 */     return (BlockState)blockState.setValue((Property)SHAPE, (Comparable)getStairsShape(blockState, (BlockGetter)paramBlockPlaceContext.getLevel(), blockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 112 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 113 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 115 */     if (paramDirection.getAxis().isHorizontal()) {
/* 116 */       return (BlockState)paramBlockState1.setValue((Property)SHAPE, (Comparable)getStairsShape(paramBlockState1, (BlockGetter)paramLevelReader, paramBlockPos1));
/*     */     }
/* 118 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   private static StairsShape getStairsShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 122 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 123 */     BlockState blockState1 = paramBlockGetter.getBlockState(paramBlockPos.relative(direction));
/* 124 */     if (isStairs(blockState1) && paramBlockState.getValue((Property)HALF) == blockState1.getValue((Property)HALF)) {
/* 125 */       Direction direction1 = (Direction)blockState1.getValue((Property)FACING);
/* 126 */       if (direction1.getAxis() != ((Direction)paramBlockState.getValue((Property)FACING)).getAxis() && canTakeShape(paramBlockState, paramBlockGetter, paramBlockPos, direction1.getOpposite())) {
/* 127 */         if (direction1 == direction.getCounterClockWise()) {
/* 128 */           return StairsShape.OUTER_LEFT;
/*     */         }
/* 130 */         return StairsShape.OUTER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     BlockState blockState2 = paramBlockGetter.getBlockState(paramBlockPos.relative(direction.getOpposite()));
/* 135 */     if (isStairs(blockState2) && paramBlockState.getValue((Property)HALF) == blockState2.getValue((Property)HALF)) {
/* 136 */       Direction direction1 = (Direction)blockState2.getValue((Property)FACING);
/* 137 */       if (direction1.getAxis() != ((Direction)paramBlockState.getValue((Property)FACING)).getAxis() && canTakeShape(paramBlockState, paramBlockGetter, paramBlockPos, direction1)) {
/* 138 */         if (direction1 == direction.getCounterClockWise()) {
/* 139 */           return StairsShape.INNER_LEFT;
/*     */         }
/* 141 */         return StairsShape.INNER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     return StairsShape.STRAIGHT;
/*     */   }
/*     */   
/*     */   private static boolean canTakeShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 149 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos.relative(paramDirection));
/* 150 */     return (!isStairs(blockState) || blockState.getValue((Property)FACING) != paramBlockState.getValue((Property)FACING) || blockState.getValue((Property)HALF) != paramBlockState.getValue((Property)HALF));
/*     */   }
/*     */   
/*     */   public static boolean isStairs(BlockState paramBlockState) {
/* 154 */     return paramBlockState.getBlock() instanceof StairBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 159 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 164 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 165 */     StairsShape stairsShape = (StairsShape)paramBlockState.getValue((Property)SHAPE);
/* 166 */     switch (paramMirror) {
/*     */       case LEFT_RIGHT:
/* 168 */         if (direction.getAxis() == Direction.Axis.Z) {
/* 169 */           switch (stairsShape) {
/*     */             case null:
/* 171 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_RIGHT);
/*     */             case null:
/* 173 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_LEFT);
/*     */             case FRONT_BACK:
/* 175 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_RIGHT);
/*     */             case null:
/* 177 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_LEFT);
/*     */           } 
/* 179 */           return paramBlockState.rotate(Rotation.CLOCKWISE_180);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 184 */         if (direction.getAxis() == Direction.Axis.X) {
/* 185 */           switch (stairsShape) {
/*     */             case null:
/* 187 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_LEFT);
/*     */             case null:
/* 189 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_RIGHT);
/*     */             case FRONT_BACK:
/* 191 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_RIGHT);
/*     */             case null:
/* 193 */               return (BlockState)paramBlockState.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_LEFT);
/*     */             case LEFT_RIGHT:
/* 195 */               return paramBlockState.rotate(Rotation.CLOCKWISE_180);
/*     */           } 
/*     */         
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 202 */     return super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 207 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)HALF, (Property)SHAPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 212 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 213 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 215 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 220 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StairBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */