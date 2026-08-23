/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class ObserverBlock extends DirectionalBlock {
/*  22 */   public static final MapCodec<ObserverBlock> CODEC = simpleCodec(ObserverBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<ObserverBlock> codec() {
/*  26 */     return CODEC;
/*     */   }
/*     */   
/*  29 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   public ObserverBlock(BlockBehaviour.Properties paramProperties) {
/*  32 */     super(paramProperties);
/*     */     
/*  34 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.SOUTH)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  39 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/*  44 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/*  49 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  54 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue()) {
/*  55 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/*     */     } else {
/*  57 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*  58 */       paramServerLevel.scheduleTick(paramBlockPos, this, 2);
/*     */     } 
/*  60 */     updateNeighborsInFront((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  65 */     if (paramBlockState1.getValue((Property)FACING) == paramDirection && !((Boolean)paramBlockState1.getValue((Property)POWERED)).booleanValue()) {
/*  66 */       startSignal(paramLevelReader, paramScheduledTickAccess, paramBlockPos1);
/*     */     }
/*     */     
/*  69 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   private void startSignal(LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos) {
/*  73 */     if (!paramLevelReader.isClientSide() && !paramScheduledTickAccess.getBlockTicks().hasScheduledTick(paramBlockPos, this)) {
/*  74 */       paramScheduledTickAccess.scheduleTick(paramBlockPos, this, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  79 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*  80 */     BlockPos blockPos = paramBlockPos.relative(direction.getOpposite());
/*     */     
/*  82 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, direction.getOpposite(), null);
/*  83 */     paramLevel.neighborChanged(blockPos, this, orientation);
/*  84 */     paramLevel.updateNeighborsAtExceptFromFacing(blockPos, this, direction, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  94 */     return paramBlockState.getSignal(paramBlockGetter, paramBlockPos, paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  99 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() && paramBlockState.getValue((Property)FACING) == paramDirection) {
/* 100 */       return 15;
/*     */     }
/* 102 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 107 */     if (paramBlockState1.is(paramBlockState2.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     if (!paramLevel.isClientSide() && ((Boolean)paramBlockState1.getValue((Property)POWERED)).booleanValue() && !paramLevel.getBlockTicks().hasScheduledTick(paramBlockPos, this)) {
/* 112 */       BlockState blockState = (BlockState)paramBlockState1.setValue((Property)POWERED, Boolean.valueOf(false));
/*     */       
/* 114 */       paramLevel.setBlock(paramBlockPos, blockState, 18);
/* 115 */       updateNeighborsInFront(paramLevel, paramBlockPos, blockState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 121 */     if (((Boolean)paramBlockState.getValue((Property)POWERED)).booleanValue() && paramServerLevel.getBlockTicks().hasScheduledTick(paramBlockPos, this))
/*     */     {
/* 123 */       updateNeighborsInFront((Level)paramServerLevel, paramBlockPos, (BlockState)paramBlockState.setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 129 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getNearestLookingDirection().getOpposite().getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ObserverBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */