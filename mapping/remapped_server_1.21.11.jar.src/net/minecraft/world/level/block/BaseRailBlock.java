/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BaseRailBlock extends Block implements SimpleWaterloggedBlock {
/*  27 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  29 */   private static final VoxelShape SHAPE_FLAT = Block.column(16.0D, 0.0D, 2.0D);
/*  30 */   private static final VoxelShape SHAPE_SLOPE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*     */   private final boolean isStraight;
/*     */   
/*     */   public static boolean isRail(Level paramLevel, BlockPos paramBlockPos) {
/*  35 */     return isRail(paramLevel.getBlockState(paramBlockPos));
/*     */   }
/*     */   
/*     */   public static boolean isRail(BlockState paramBlockState) {
/*  39 */     return (paramBlockState.is(BlockTags.RAILS) && paramBlockState.getBlock() instanceof BaseRailBlock);
/*     */   }
/*     */   
/*     */   protected BaseRailBlock(boolean paramBoolean, BlockBehaviour.Properties paramProperties) {
/*  43 */     super(paramProperties);
/*  44 */     this.isStraight = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends BaseRailBlock> codec();
/*     */   
/*     */   public boolean isStraight() {
/*  51 */     return this.isStraight;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  56 */     return ((RailShape)paramBlockState.getValue(getShapeProperty())).isSlope() ? SHAPE_SLOPE : SHAPE_FLAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  61 */     return canSupportRigidBlock((BlockGetter)paramLevelReader, paramBlockPos.below());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  66 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/*  69 */     updateState(paramBlockState1, paramLevel, paramBlockPos, paramBoolean);
/*     */   }
/*     */   
/*     */   protected BlockState updateState(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/*  73 */     paramBlockState = updateDir(paramLevel, paramBlockPos, paramBlockState, true);
/*     */     
/*  75 */     if (this.isStraight) {
/*  76 */       paramLevel.neighborChanged(paramBlockState, paramBlockPos, this, null, paramBoolean);
/*     */     }
/*     */     
/*  79 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  84 */     if (paramLevel.isClientSide() || !paramLevel.getBlockState(paramBlockPos).is(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  88 */     RailShape railShape = (RailShape)paramBlockState.getValue(getShapeProperty());
/*     */     
/*  90 */     if (shouldBeRemoved(paramBlockPos, paramLevel, railShape)) {
/*  91 */       dropResources(paramBlockState, paramLevel, paramBlockPos);
/*  92 */       paramLevel.removeBlock(paramBlockPos, paramBoolean);
/*     */     } else {
/*  94 */       updateState(paramBlockState, paramLevel, paramBlockPos, paramBlock);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldBeRemoved(BlockPos paramBlockPos, Level paramLevel, RailShape paramRailShape) {
/*  99 */     if (!canSupportRigidBlock((BlockGetter)paramLevel, paramBlockPos.below())) {
/* 100 */       return true;
/*     */     }
/* 102 */     switch (paramRailShape) {
/*     */       case LEFT_RIGHT:
/* 104 */         return !canSupportRigidBlock((BlockGetter)paramLevel, paramBlockPos.east());
/*     */       case FRONT_BACK:
/* 106 */         return !canSupportRigidBlock((BlockGetter)paramLevel, paramBlockPos.west());
/*     */       case null:
/* 108 */         return !canSupportRigidBlock((BlockGetter)paramLevel, paramBlockPos.north());
/*     */       case null:
/* 110 */         return !canSupportRigidBlock((BlockGetter)paramLevel, paramBlockPos.south());
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock) {}
/*     */ 
/*     */   
/*     */   protected BlockState updateDir(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 120 */     if (paramLevel.isClientSide()) {
/* 121 */       return paramBlockState;
/*     */     }
/* 123 */     RailShape railShape = (RailShape)paramBlockState.getValue(getShapeProperty());
/* 124 */     return (new RailState(paramLevel, paramBlockPos, paramBlockState)).place(paramLevel.hasNeighborSignal(paramBlockPos), paramBoolean, railShape).getState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 129 */     if (paramBoolean) {
/*     */       return;
/*     */     }
/*     */     
/* 133 */     if (((RailShape)paramBlockState.getValue(getShapeProperty())).isSlope()) {
/* 134 */       paramServerLevel.updateNeighborsAt(paramBlockPos.above(), this);
/*     */     }
/*     */     
/* 137 */     if (this.isStraight) {
/* 138 */       paramServerLevel.updateNeighborsAt(paramBlockPos, this);
/* 139 */       paramServerLevel.updateNeighborsAt(paramBlockPos.below(), this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 145 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 146 */     boolean bool1 = (fluidState.getType() == Fluids.WATER) ? true : false;
/* 147 */     BlockState blockState = defaultBlockState();
/* 148 */     Direction direction = paramBlockPlaceContext.getHorizontalDirection();
/* 149 */     boolean bool2 = (direction == Direction.EAST || direction == Direction.WEST) ? true : false;
/* 150 */     return (BlockState)((BlockState)blockState.setValue(getShapeProperty(), bool2 ? (Comparable)RailShape.EAST_WEST : (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(bool1));
/*     */   }
/*     */   
/*     */   public abstract Property<RailShape> getShapeProperty();
/*     */   
/*     */   protected RailShape rotate(RailShape paramRailShape, Rotation paramRotation) {
/* 156 */     switch (paramRotation) { case LEFT_RIGHT:
/* 157 */         switch (paramRailShape) { default: throw new MatchException(null, null);
/*     */           case null: 
/*     */           case null: 
/*     */           case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: break; } 
/*     */       case FRONT_BACK:
/* 169 */         switch (paramRailShape) { default: throw new MatchException(null, null);
/*     */           case null: 
/*     */           case null: 
/*     */           case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: break; } 
/*     */       case null:
/* 181 */         switch (paramRailShape) { default: throw new MatchException(null, null);
/*     */           case null: 
/*     */           case null: 
/*     */           case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null:
/*     */           
/*     */           case null:
/* 193 */             break; }  }  return paramRailShape;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RailShape mirror(RailShape paramRailShape, Mirror paramMirror) {
/* 198 */     switch (paramMirror) { case LEFT_RIGHT:
/* 199 */         switch (paramRailShape) { case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null:
/*     */           
/*     */           case null:
/*     */            } 
/*     */       case FRONT_BACK:
/* 208 */         switch (paramRailShape) { case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/* 217 */            }  }  return paramRailShape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 223 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 224 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 226 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 231 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 232 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 234 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BaseRailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */