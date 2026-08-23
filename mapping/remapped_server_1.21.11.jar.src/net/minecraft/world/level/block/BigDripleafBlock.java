/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.Tilt;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BigDripleafBlock extends HorizontalDirectionalBlock implements BonemealableBlock, SimpleWaterloggedBlock {
/*  47 */   public static final MapCodec<BigDripleafBlock> CODEC = simpleCodec(BigDripleafBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BigDripleafBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  55 */   private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT; private static final int NO_TICK = -1;
/*     */   private static final Object2IntMap<Tilt> DELAY_UNTIL_NEXT_TILT_STATE;
/*     */   
/*     */   static {
/*  59 */     DELAY_UNTIL_NEXT_TILT_STATE = (Object2IntMap<Tilt>)Util.make(new Object2IntArrayMap(), paramObject2IntArrayMap -> {
/*     */           paramObject2IntArrayMap.defaultReturnValue(-1);
/*     */           paramObject2IntArrayMap.put(Tilt.UNSTABLE, 10);
/*     */           paramObject2IntArrayMap.put(Tilt.PARTIAL, 10);
/*     */           paramObject2IntArrayMap.put(Tilt.FULL, 100);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int MAX_GEN_HEIGHT = 5;
/*     */   private static final int ENTITY_DETECTION_MIN_Y = 11;
/*     */   private static final int LOWEST_LEAF_TOP = 13;
/*  71 */   private static final Map<Tilt, VoxelShape> SHAPE_LEAF = Maps.newEnumMap(Map.of(Tilt.NONE, 
/*     */         
/*  73 */         Block.column(16.0D, 11.0D, 15.0D), Tilt.UNSTABLE, 
/*  74 */         Block.column(16.0D, 11.0D, 15.0D), Tilt.PARTIAL, 
/*  75 */         Block.column(16.0D, 11.0D, 13.0D), Tilt.FULL, 
/*  76 */         Shapes.empty()));
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */ 
/*     */   
/*     */   protected BigDripleafBlock(BlockBehaviour.Properties paramProperties) {
/*  82 */     super(paramProperties);
/*  83 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  84 */         .setValue((Property)WATERLOGGED, Boolean.valueOf(false)))
/*  85 */         .setValue((Property)FACING, (Comparable)Direction.NORTH))
/*  86 */         .setValue((Property)TILT, (Comparable)Tilt.NONE));
/*     */     
/*  88 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  92 */     Map map = Shapes.rotateHorizontal(Block.column(6.0D, 0.0D, 13.0D).move(0.0D, 0.0D, 0.25D).optimize());
/*     */     
/*  94 */     return getShapeForEachState(paramBlockState -> Shapes.or(SHAPE_LEAF.get(paramBlockState.getValue((Property)TILT)), (VoxelShape)paramMap.get(paramBlockState.getValue((Property)FACING))), (Property<?>[])new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void placeWithRandomHeight(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, Direction paramDirection) {
/* 101 */     int i = Mth.nextInt(paramRandomSource, 2, 5);
/*     */     
/* 103 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */ 
/*     */     
/* 106 */     byte b = 0;
/* 107 */     while (b < i && canPlaceAt((LevelHeightAccessor)paramLevelAccessor, (BlockPos)mutableBlockPos, paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos))) {
/* 108 */       b++;
/* 109 */       mutableBlockPos.move(Direction.UP);
/*     */     } 
/* 111 */     int j = paramBlockPos.getY() + b - 1;
/*     */ 
/*     */     
/* 114 */     mutableBlockPos.setY(paramBlockPos.getY());
/* 115 */     while (mutableBlockPos.getY() < j) {
/* 116 */       BigDripleafStemBlock.place(paramLevelAccessor, (BlockPos)mutableBlockPos, paramLevelAccessor.getFluidState((BlockPos)mutableBlockPos), paramDirection);
/* 117 */       mutableBlockPos.move(Direction.UP);
/*     */     } 
/*     */ 
/*     */     
/* 121 */     place(paramLevelAccessor, (BlockPos)mutableBlockPos, paramLevelAccessor.getFluidState((BlockPos)mutableBlockPos), paramDirection);
/*     */   }
/*     */   
/*     */   private static boolean canReplace(BlockState paramBlockState) {
/* 125 */     return (paramBlockState.isAir() || paramBlockState.is(Blocks.WATER) || paramBlockState.is(Blocks.SMALL_DRIPLEAF));
/*     */   }
/*     */   
/*     */   protected static boolean canPlaceAt(LevelHeightAccessor paramLevelHeightAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 129 */     return (!paramLevelHeightAccessor.isOutsideBuildHeight(paramBlockPos) && canReplace(paramBlockState));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean place(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, FluidState paramFluidState, Direction paramDirection) {
/* 135 */     BlockState blockState = (BlockState)((BlockState)Blocks.BIG_DRIPLEAF.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(paramFluidState.isSourceOfType((Fluid)Fluids.WATER)))).setValue((Property)FACING, (Comparable)paramDirection);
/* 136 */     return paramLevelAccessor.setBlock(paramBlockPos, blockState, 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 141 */     setTiltAndScheduleTick(paramBlockState, paramLevel, paramBlockHitResult.getBlockPos(), Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 146 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 147 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 149 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 154 */     BlockPos blockPos = paramBlockPos.below();
/* 155 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 156 */     return (blockState.is(this) || blockState.is(Blocks.BIG_DRIPLEAF_STEM) || blockState.is(BlockTags.BIG_DRIPLEAF_PLACEABLE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 161 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 162 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 164 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 165 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 168 */     if (paramDirection == Direction.UP && paramBlockState2.is(this)) {
/* 169 */       return Blocks.BIG_DRIPLEAF_STEM.withPropertiesOf(paramBlockState1);
/*     */     }
/*     */     
/* 172 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 177 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.above());
/* 178 */     return canReplace(blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 188 */     BlockPos blockPos = paramBlockPos.above();
/* 189 */     BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 190 */     if (canPlaceAt((LevelHeightAccessor)paramServerLevel, blockPos, blockState)) {
/* 191 */       Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/* 192 */       BigDripleafStemBlock.place((LevelAccessor)paramServerLevel, paramBlockPos, paramBlockState.getFluidState(), direction);
/* 193 */       place((LevelAccessor)paramServerLevel, blockPos, blockState.getFluidState(), direction);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 199 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 203 */     if (paramBlockState.getValue((Property)TILT) == Tilt.NONE && canEntityTilt(paramBlockPos, paramEntity) && !paramLevel.hasNeighborSignal(paramBlockPos)) {
/* 204 */       setTiltAndScheduleTick(paramBlockState, paramLevel, paramBlockPos, Tilt.UNSTABLE, (SoundEvent)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 210 */     if (paramServerLevel.hasNeighborSignal(paramBlockPos)) {
/* 211 */       resetTilt(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */       
/*     */       return;
/*     */     } 
/* 215 */     Tilt tilt = (Tilt)paramBlockState.getValue((Property)TILT);
/*     */     
/* 217 */     if (tilt == Tilt.UNSTABLE) {
/* 218 */       setTiltAndScheduleTick(paramBlockState, (Level)paramServerLevel, paramBlockPos, Tilt.PARTIAL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/* 219 */     } else if (tilt == Tilt.PARTIAL) {
/* 220 */       setTiltAndScheduleTick(paramBlockState, (Level)paramServerLevel, paramBlockPos, Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/* 221 */     } else if (tilt == Tilt.FULL) {
/* 222 */       resetTilt(paramBlockState, (Level)paramServerLevel, paramBlockPos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 228 */     if (paramLevel.hasNeighborSignal(paramBlockPos)) {
/* 229 */       resetTilt(paramBlockState, paramLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void playTiltSound(Level paramLevel, BlockPos paramBlockPos, SoundEvent paramSoundEvent) {
/* 234 */     float f = Mth.randomBetween(paramLevel.random, 0.8F, 1.2F);
/* 235 */     paramLevel.playSound(null, paramBlockPos, paramSoundEvent, SoundSource.BLOCKS, 1.0F, f);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean canEntityTilt(BlockPos paramBlockPos, Entity paramEntity) {
/* 240 */     return (paramEntity.onGround() && (paramEntity.position()).y > (paramBlockPos.getY() + 0.6875F));
/*     */   }
/*     */   
/*     */   private void setTiltAndScheduleTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Tilt paramTilt, SoundEvent paramSoundEvent) {
/* 244 */     setTilt(paramBlockState, paramLevel, paramBlockPos, paramTilt);
/* 245 */     if (paramSoundEvent != null) {
/* 246 */       playTiltSound(paramLevel, paramBlockPos, paramSoundEvent);
/*     */     }
/* 248 */     int i = DELAY_UNTIL_NEXT_TILT_STATE.getInt(paramTilt);
/* 249 */     if (i != -1) {
/* 250 */       paramLevel.scheduleTick(paramBlockPos, this, i);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void resetTilt(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 255 */     setTilt(paramBlockState, paramLevel, paramBlockPos, Tilt.NONE);
/* 256 */     if (paramBlockState.getValue((Property)TILT) != Tilt.NONE) {
/* 257 */       playTiltSound(paramLevel, paramBlockPos, SoundEvents.BIG_DRIPLEAF_TILT_UP);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setTilt(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Tilt paramTilt) {
/* 262 */     Tilt tilt = (Tilt)paramBlockState.getValue((Property)TILT);
/* 263 */     paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)TILT, (Comparable)paramTilt), 2);
/* 264 */     if (paramTilt.causesVibration() && paramTilt != tilt) {
/* 265 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 271 */     return SHAPE_LEAF.get(paramBlockState.getValue((Property)TILT));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 276 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 281 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().below());
/* 282 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 283 */     boolean bool = (blockState.is(Blocks.BIG_DRIPLEAF) || blockState.is(Blocks.BIG_DRIPLEAF_STEM)) ? true : false;
/*     */     
/* 285 */     return (BlockState)((BlockState)defaultBlockState()
/* 286 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(fluidState.isSourceOfType((Fluid)Fluids.WATER))))
/* 287 */       .setValue((Property)FACING, bool ? blockState.getValue((Property)FACING) : (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 292 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED, (Property)FACING, (Property)TILT });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BigDripleafBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */