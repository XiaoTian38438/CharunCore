/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import net.minecraft.world.ticks.TickPriority;
/*     */ 
/*     */ public abstract class DiodeBlock extends HorizontalDirectionalBlock {
/*  28 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  30 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 2.0D);
/*     */   
/*     */   protected DiodeBlock(BlockBehaviour.Properties paramProperties) {
/*  33 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends DiodeBlock> codec();
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  41 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  46 */     BlockPos blockPos = paramBlockPos.below();
/*  47 */     return canSurviveOn(paramLevelReader, blockPos, paramLevelReader.getBlockState(blockPos));
/*     */   }
/*     */   
/*     */   protected boolean canSurviveOn(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  51 */     return paramBlockState.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos, Direction.UP, SupportType.RIGID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  56 */     if (isLocked((LevelReader)paramServerLevel, paramBlockPos, paramBlockState)) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     boolean bool1 = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/*  61 */     boolean bool2 = shouldTurnOn((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*  62 */     if (bool1 && !bool2) {
/*  63 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/*  64 */     } else if (!bool1) {
/*     */ 
/*     */       
/*  67 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*  68 */       if (!bool2) {
/*  69 */         paramServerLevel.scheduleTick(paramBlockPos, this, getDelay(paramBlockState), TickPriority.VERY_HIGH);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  76 */     return paramBlockState.getSignal(paramBlockGetter, paramBlockPos, paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  81 */     if (!((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*  82 */       return 0;
/*     */     }
/*     */     
/*  85 */     if (paramBlockState.getValue((Property)FACING) == paramDirection) {
/*  86 */       return getOutputSignal(paramBlockGetter, paramBlockPos, paramBlockState);
/*     */     }
/*     */     
/*  89 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  94 */     if (paramBlockState.canSurvive((LevelReader)paramLevel, paramBlockPos)) {
/*  95 */       checkTickOnNeighbor(paramLevel, paramBlockPos, paramBlockState);
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     BlockEntity blockEntity = paramBlockState.hasBlockEntity() ? paramLevel.getBlockEntity(paramBlockPos) : null;
/* 100 */     dropResources(paramBlockState, (LevelAccessor)paramLevel, paramBlockPos, blockEntity);
/* 101 */     paramLevel.removeBlock(paramBlockPos, false);
/* 102 */     for (Direction direction : Direction.values()) {
/* 103 */       paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkTickOnNeighbor(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 108 */     if (isLocked((LevelReader)paramLevel, paramBlockPos, paramBlockState)) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     boolean bool1 = ((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue();
/* 113 */     boolean bool2 = shouldTurnOn(paramLevel, paramBlockPos, paramBlockState);
/* 114 */     if (bool1 != bool2 && !paramLevel.getBlockTicks().willTickThisTick(paramBlockPos, this)) {
/* 115 */       TickPriority tickPriority = TickPriority.HIGH;
/*     */ 
/*     */       
/* 118 */       if (shouldPrioritize((BlockGetter)paramLevel, paramBlockPos, paramBlockState)) {
/* 119 */         tickPriority = TickPriority.EXTREMELY_HIGH;
/* 120 */       } else if (bool1) {
/* 121 */         tickPriority = TickPriority.VERY_HIGH;
/*     */       } 
/*     */       
/* 124 */       paramLevel.scheduleTick(paramBlockPos, this, getDelay(paramBlockState), tickPriority);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isLocked(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean shouldTurnOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 133 */     return (getInputSignal(paramLevel, paramBlockPos, paramBlockState) > 0);
/*     */   }
/*     */   
/*     */   protected int getInputSignal(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 137 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*     */     
/* 139 */     BlockPos blockPos = paramBlockPos.relative(direction);
/* 140 */     int i = paramLevel.getSignal(blockPos, direction);
/* 141 */     if (i >= 15) {
/* 142 */       return i;
/*     */     }
/*     */     
/* 145 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/* 146 */     return Math.max(i, blockState.is(Blocks.REDSTONE_WIRE) ? ((Integer)blockState.getValue((Property)RedStoneWireBlock.POWER)).intValue() : 0);
/*     */   }
/*     */   
/*     */   protected int getAlternateSignal(SignalGetter paramSignalGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 150 */     Direction direction1 = (Direction)paramBlockState.getValue((Property)FACING);
/* 151 */     Direction direction2 = direction1.getClockWise();
/* 152 */     Direction direction3 = direction1.getCounterClockWise();
/* 153 */     boolean bool = sideInputDiodesOnly();
/* 154 */     return Math.max(paramSignalGetter
/* 155 */         .getControlInputSignal(paramBlockPos.relative(direction2), direction2, bool), paramSignalGetter
/* 156 */         .getControlInputSignal(paramBlockPos.relative(direction3), direction3, bool));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 167 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 172 */     if (shouldTurnOn(paramLevel, paramBlockPos, paramBlockState)) {
/* 173 */       paramLevel.scheduleTick(paramBlockPos, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 179 */     updateNeighborsInFront(paramLevel, paramBlockPos, paramBlockState1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 184 */     if (!paramBoolean) {
/* 185 */       updateNeighborsInFront((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 190 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 191 */     BlockPos blockPos = paramBlockPos.relative(direction.getOpposite());
/*     */     
/* 193 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, direction.getOpposite(), Direction.UP);
/* 194 */     paramLevel.neighborChanged(blockPos, this, orientation);
/* 195 */     paramLevel.updateNeighborsAtExceptFromFacing(blockPos, this, direction, orientation);
/*     */   }
/*     */   
/*     */   protected boolean sideInputDiodesOnly() {
/* 199 */     return false;
/*     */   }
/*     */   
/*     */   protected int getOutputSignal(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 203 */     return 15;
/*     */   }
/*     */   
/*     */   public static boolean isDiode(BlockState paramBlockState) {
/* 207 */     return paramBlockState.getBlock() instanceof DiodeBlock;
/*     */   }
/*     */   
/*     */   public boolean shouldPrioritize(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 211 */     Direction direction = ((Direction)paramBlockState.getValue((Property)FACING)).getOpposite();
/* 212 */     BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos.relative(direction));
/*     */     
/* 214 */     return (isDiode(blockState) && blockState.getValue((Property)FACING) != direction);
/*     */   }
/*     */   
/*     */   protected abstract int getDelay(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DiodeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */