/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkSensorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  45 */   public static final MapCodec<SculkSensorBlock> CODEC = simpleCodec(SculkSensorBlock::new); public static final int ACTIVE_TICKS = 30;
/*     */   public static final int COOLDOWN_TICKS = 10;
/*     */   
/*     */   public MapCodec<? extends SculkSensorBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;
/*  56 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*  57 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  59 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 8.0D); private static final float[] RESONANCE_PITCH_BEND;
/*     */   static {
/*  61 */     RESONANCE_PITCH_BEND = (float[])Util.make(new float[16], paramArrayOffloat -> {
/*     */           int[] arrayOfInt = { 
/*     */               0, 0, 2, 4, 6, 7, 9, 10, 12, 14, 
/*     */               15, 18, 19, 21, 22, 24 };
/*     */           for (byte b = 0; b < 16; b++) {
/*     */             paramArrayOffloat[b] = NoteBlock.getPitchFromNote(arrayOfInt[b]);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SculkSensorBlock(BlockBehaviour.Properties paramProperties) {
/*  75 */     super(paramProperties);
/*  76 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PHASE, (Comparable)SculkSensorPhase.INACTIVE)).setValue((Property)POWER, Integer.valueOf(0))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  81 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*  82 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(blockPos);
/*     */     
/*  84 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  89 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  90 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  92 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  97 */     if (getPhase(paramBlockState) != SculkSensorPhase.ACTIVE) {
/*  98 */       if (getPhase(paramBlockState) == SculkSensorPhase.COOLDOWN) {
/*  99 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)PHASE, (Comparable)SculkSensorPhase.INACTIVE), 3);
/* 100 */         if (!((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 101 */           paramServerLevel.playSound(null, paramBlockPos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1.0F, paramServerLevel.random.nextFloat() * 0.2F + 0.8F);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 108 */     deactivate((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Entity paramEntity) {
/* 113 */     if (!paramLevel.isClientSide() && canActivate(paramBlockState) && paramEntity.getType() != EntityType.WARDEN) {
/* 114 */       BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/* 115 */       if (blockEntity instanceof SculkSensorBlockEntity) { SculkSensorBlockEntity sculkSensorBlockEntity = (SculkSensorBlockEntity)blockEntity; if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 116 */           if (sculkSensorBlockEntity.getVibrationUser().canReceiveVibration(serverLevel, paramBlockPos, (Holder)GameEvent.STEP, GameEvent.Context.of(paramBlockState)))
/* 117 */             sculkSensorBlockEntity.getListener().forceScheduleVibration(serverLevel, (Holder)GameEvent.STEP, GameEvent.Context.of(paramEntity), paramEntity.position());  }
/*     */          }
/*     */     
/*     */     } 
/* 121 */     super.stepOn(paramLevel, paramBlockPos, paramBlockState, paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 126 */     if (paramLevel.isClientSide() || paramBlockState1.is(paramBlockState2.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     if (((Integer)paramBlockState1.getValue((Property)POWER)).intValue() > 0 && !paramLevel.getBlockTicks().hasScheduledTick(paramBlockPos, this)) {
/* 131 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState1.setValue((Property)POWER, Integer.valueOf(0)), 18);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 137 */     if (getPhase(paramBlockState) == SculkSensorPhase.ACTIVE) {
/* 138 */       updateNeighbours((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 144 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 145 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 147 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   private static void updateNeighbours(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 151 */     Block block = paramBlockState.getBlock();
/* 152 */     paramLevel.updateNeighborsAt(paramBlockPos, block);
/* 153 */     paramLevel.updateNeighborsAt(paramBlockPos.below(), block);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 158 */     return (BlockEntity)new SculkSensorBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 163 */     if (!paramLevel.isClientSide()) {
/* 164 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.SCULK_SENSOR, (paramLevel, paramBlockPos, paramBlockState, paramSculkSensorBlockEntity) -> VibrationSystem.Ticker.tick(paramLevel, paramSculkSensorBlockEntity.getVibrationData(), paramSculkSensorBlockEntity.getVibrationUser()));
/*     */     }
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 172 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 182 */     return ((Integer)paramBlockState.getValue((Property)POWER)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 187 */     if (paramDirection == Direction.UP) {
/* 188 */       return paramBlockState.getSignal(paramBlockGetter, paramBlockPos, paramDirection);
/*     */     }
/*     */     
/* 191 */     return 0;
/*     */   }
/*     */   
/*     */   public static SculkSensorPhase getPhase(BlockState paramBlockState) {
/* 195 */     return (SculkSensorPhase)paramBlockState.getValue((Property)PHASE);
/*     */   }
/*     */   
/*     */   public static boolean canActivate(BlockState paramBlockState) {
/* 199 */     return (getPhase(paramBlockState) == SculkSensorPhase.INACTIVE);
/*     */   }
/*     */   
/*     */   public static void deactivate(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 203 */     paramLevel.setBlock(paramBlockPos, (BlockState)((BlockState)paramBlockState.setValue((Property)PHASE, (Comparable)SculkSensorPhase.COOLDOWN)).setValue((Property)POWER, Integer.valueOf(0)), 3);
/* 204 */     paramLevel.scheduleTick(paramBlockPos, paramBlockState.getBlock(), 10);
/* 205 */     updateNeighbours(paramLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getActiveTicks() {
/* 210 */     return 30;
/*     */   }
/*     */   
/*     */   public void activate(Entity paramEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, int paramInt1, int paramInt2) {
/* 214 */     paramLevel.setBlock(paramBlockPos, (BlockState)((BlockState)paramBlockState.setValue((Property)PHASE, (Comparable)SculkSensorPhase.ACTIVE)).setValue((Property)POWER, Integer.valueOf(paramInt1)), 3);
/*     */     
/* 216 */     paramLevel.scheduleTick(paramBlockPos, paramBlockState.getBlock(), getActiveTicks());
/* 217 */     updateNeighbours(paramLevel, paramBlockPos, paramBlockState);
/* 218 */     tryResonateVibration(paramEntity, paramLevel, paramBlockPos, paramInt2);
/* 219 */     paramLevel.gameEvent(paramEntity, (Holder)GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, paramBlockPos);
/* 220 */     if (!((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 221 */       paramLevel.playSound(null, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, paramLevel.random.nextFloat() * 0.2F + 0.8F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void tryResonateVibration(Entity paramEntity, Level paramLevel, BlockPos paramBlockPos, int paramInt) {
/* 226 */     for (Direction direction : Direction.values()) {
/* 227 */       BlockPos blockPos = paramBlockPos.relative(direction);
/* 228 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/* 229 */       if (blockState.is(BlockTags.VIBRATION_RESONATORS)) {
/* 230 */         paramLevel.gameEvent(VibrationSystem.getResonanceEventByFrequency(paramInt), blockPos, GameEvent.Context.of(paramEntity, blockState));
/* 231 */         float f = RESONANCE_PITCH_BEND[paramInt];
/* 232 */         paramLevel.playSound(null, blockPos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, f);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 239 */     if (getPhase(paramBlockState) != SculkSensorPhase.ACTIVE) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     Direction direction = Direction.getRandom(paramRandomSource);
/*     */     
/* 245 */     if (direction == Direction.UP || direction == Direction.DOWN) {
/*     */       return;
/*     */     }
/*     */     
/* 249 */     double d1 = paramBlockPos.getX() + 0.5D + ((direction.getStepX() == 0) ? (0.5D - paramRandomSource.nextDouble()) : (direction.getStepX() * 0.6D));
/* 250 */     double d2 = paramBlockPos.getY() + 0.25D;
/* 251 */     double d3 = paramBlockPos.getZ() + 0.5D + ((direction.getStepZ() == 0) ? (0.5D - paramRandomSource.nextDouble()) : (direction.getStepZ() * 0.6D));
/* 252 */     double d4 = paramRandomSource.nextFloat() * 0.04D;
/* 253 */     paramLevel.addParticle((ParticleOptions)DustColorTransitionOptions.SCULK_TO_REDSTONE, d1, d2, d3, 0.0D, d4, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 258 */     paramBuilder.add(new Property[] { (Property)PHASE, (Property)POWER, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState paramBlockState) {
/* 263 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 268 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*     */     
/* 270 */     if (blockEntity instanceof SculkSensorBlockEntity) { SculkSensorBlockEntity sculkSensorBlockEntity = (SculkSensorBlockEntity)blockEntity;
/* 271 */       return (getPhase(paramBlockState) == SculkSensorPhase.ACTIVE) ? sculkSensorBlockEntity.getLastVibrationFrequency() : 0; }
/*     */ 
/*     */     
/* 274 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 279 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 289 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/* 290 */     if (paramBoolean)
/* 291 */       tryDropExperience(paramServerLevel, paramBlockPos, paramItemStack, (IntProvider)ConstantInt.of(5)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SculkSensorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */