/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class LeavesBlock extends Block implements SimpleWaterloggedBlock {
/*     */   public static final int DECAY_DISTANCE = 7;
/*  35 */   public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
/*  36 */   public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
/*  37 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   protected final float leafParticleChance;
/*     */   private static final int TICK_DELAY = 1;
/*     */   private static boolean cutoutLeaves = true;
/*     */   
/*     */   public abstract MapCodec<? extends LeavesBlock> codec();
/*     */   
/*     */   public LeavesBlock(float paramFloat, BlockBehaviour.Properties paramProperties) {
/*  45 */     super(paramProperties);
/*  46 */     this.leafParticleChance = paramFloat;
/*  47 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DISTANCE, Integer.valueOf(7))).setValue((Property)PERSISTENT, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skipRendering(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/*  52 */     if (!cutoutLeaves && paramBlockState2.getBlock() instanceof LeavesBlock) {
/*  53 */       return true;
/*     */     }
/*  55 */     return super.skipRendering(paramBlockState1, paramBlockState2, paramDirection);
/*     */   }
/*     */   
/*     */   public static void setCutoutLeaves(boolean paramBoolean) {
/*  59 */     cutoutLeaves = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  64 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  69 */     return (((Integer)paramBlockState.getValue((Property)DISTANCE)).intValue() == 7 && !((Boolean)paramBlockState.getValue((Property)PERSISTENT)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  74 */     if (decaying(paramBlockState)) {
/*  75 */       dropResources(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*  76 */       paramServerLevel.removeBlock(paramBlockPos, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean decaying(BlockState paramBlockState) {
/*  81 */     return (!((Boolean)paramBlockState.getValue((Property)PERSISTENT)).booleanValue() && ((Integer)paramBlockState.getValue((Property)DISTANCE)).intValue() == 7);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  86 */     paramServerLevel.setBlock(paramBlockPos, updateDistance(paramBlockState, (LevelAccessor)paramServerLevel, paramBlockPos), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLightBlock(BlockState paramBlockState) {
/*  91 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  96 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  97 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  99 */     int i = getDistanceAt(paramBlockState2) + 1;
/* 100 */     if (i != 1 || ((Integer)paramBlockState1.getValue((Property)DISTANCE)).intValue() != i) {
/* 101 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/* 103 */     return paramBlockState1;
/*     */   }
/*     */   
/*     */   private static BlockState updateDistance(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos) {
/* 107 */     int i = 7;
/* 108 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 109 */     for (Direction direction : Direction.values()) {
/* 110 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction);
/* 111 */       i = Math.min(i, getDistanceAt(paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos)) + 1);
/* 112 */       if (i == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/* 116 */     return (BlockState)paramBlockState.setValue((Property)DISTANCE, Integer.valueOf(i));
/*     */   }
/*     */   
/*     */   private static int getDistanceAt(BlockState paramBlockState) {
/* 120 */     return getOptionalDistanceAt(paramBlockState).orElse(7);
/*     */   }
/*     */   
/*     */   public static OptionalInt getOptionalDistanceAt(BlockState paramBlockState) {
/* 124 */     if (paramBlockState.is(BlockTags.LOGS)) {
/* 125 */       return OptionalInt.of(0);
/*     */     }
/* 127 */     if (paramBlockState.hasProperty((Property)DISTANCE)) {
/* 128 */       return OptionalInt.of(((Integer)paramBlockState.getValue((Property)DISTANCE)).intValue());
/*     */     }
/* 130 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 135 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 136 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 138 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 143 */     super.animateTick(paramBlockState, paramLevel, paramBlockPos, paramRandomSource);
/* 144 */     BlockPos blockPos = paramBlockPos.below();
/* 145 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/*     */     
/* 147 */     makeDrippingWaterParticles(paramLevel, paramBlockPos, paramRandomSource, blockState, blockPos);
/* 148 */     makeFallingLeavesParticles(paramLevel, paramBlockPos, paramRandomSource, blockState, blockPos);
/*     */   }
/*     */   
/*     */   private static void makeDrippingWaterParticles(Level paramLevel, BlockPos paramBlockPos1, RandomSource paramRandomSource, BlockState paramBlockState, BlockPos paramBlockPos2) {
/* 152 */     if (!paramLevel.isRainingAt(paramBlockPos1.above())) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     if (paramRandomSource.nextInt(15) != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     if (paramBlockState.canOcclude() && paramBlockState.isFaceSturdy((BlockGetter)paramLevel, paramBlockPos2, Direction.UP)) {
/*     */       return;
/*     */     }
/*     */     
/* 164 */     ParticleUtils.spawnParticleBelow(paramLevel, paramBlockPos1, paramRandomSource, (ParticleOptions)ParticleTypes.DRIPPING_WATER);
/*     */   }
/*     */   
/*     */   private void makeFallingLeavesParticles(Level paramLevel, BlockPos paramBlockPos1, RandomSource paramRandomSource, BlockState paramBlockState, BlockPos paramBlockPos2) {
/* 168 */     if (paramRandomSource.nextFloat() >= this.leafParticleChance) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     if (isFaceFull(paramBlockState.getCollisionShape((BlockGetter)paramLevel, paramBlockPos2), Direction.UP)) {
/*     */       return;
/*     */     }
/*     */     
/* 176 */     spawnFallingLeavesParticle(paramLevel, paramBlockPos1, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void spawnFallingLeavesParticle(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource);
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 183 */     paramBuilder.add(new Property[] { (Property)DISTANCE, (Property)PERSISTENT, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 188 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 189 */     BlockState blockState = (BlockState)((BlockState)defaultBlockState().setValue((Property)PERSISTENT, Boolean.valueOf(true))).setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/* 190 */     return updateDistance(blockState, (LevelAccessor)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */