/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BubbleColumnBlock extends Block implements BucketPickup {
/*  36 */   public static final MapCodec<BubbleColumnBlock> CODEC = simpleCodec(BubbleColumnBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BubbleColumnBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final BooleanProperty DRAG_DOWN = BlockStateProperties.DRAG;
/*     */   private static final int CHECK_PERIOD = 5;
/*     */   
/*     */   public BubbleColumnBlock(BlockBehaviour.Properties paramProperties) {
/*  47 */     super(paramProperties);
/*  48 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DRAG_DOWN, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  53 */     if (paramBoolean) {
/*  54 */       BlockState blockState = paramLevel.getBlockState(paramBlockPos.above());
/*  55 */       boolean bool = (blockState.getCollisionShape((BlockGetter)paramLevel, paramBlockPos).isEmpty() && blockState.getFluidState().isEmpty()) ? true : false;
/*  56 */       if (bool) {
/*  57 */         paramEntity.onAboveBubbleColumn(((Boolean)paramBlockState.getValue((Property)DRAG_DOWN)).booleanValue(), paramBlockPos);
/*     */       } else {
/*  59 */         paramEntity.onInsideBubbleColumn(((Boolean)paramBlockState.getValue((Property)DRAG_DOWN)).booleanValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  66 */     updateColumn((LevelAccessor)paramServerLevel, paramBlockPos, paramBlockState, paramServerLevel.getBlockState(paramBlockPos.below()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  71 */     return Fluids.WATER.getSource(false);
/*     */   }
/*     */   
/*     */   public static void updateColumn(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  75 */     updateColumn(paramLevelAccessor, paramBlockPos, paramLevelAccessor.getBlockState(paramBlockPos), paramBlockState);
/*     */   }
/*     */   
/*     */   public static void updateColumn(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2) {
/*  79 */     if (!canExistIn(paramBlockState1)) {
/*     */       return;
/*     */     }
/*  82 */     BlockState blockState = getColumnState(paramBlockState2);
/*  83 */     paramLevelAccessor.setBlock(paramBlockPos, blockState, 2);
/*     */     
/*  85 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable().move(Direction.UP);
/*  86 */     while (canExistIn(paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos))) {
/*  87 */       if (!paramLevelAccessor.setBlock((BlockPos)mutableBlockPos, blockState, 2)) {
/*     */         return;
/*     */       }
/*  90 */       mutableBlockPos.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean canExistIn(BlockState paramBlockState) {
/*  95 */     return (paramBlockState.is(Blocks.BUBBLE_COLUMN) || (paramBlockState.is(Blocks.WATER) && paramBlockState.getFluidState().getAmount() >= 8 && paramBlockState.getFluidState().isSource()));
/*     */   }
/*     */   
/*     */   private static BlockState getColumnState(BlockState paramBlockState) {
/*  99 */     if (paramBlockState.is(Blocks.BUBBLE_COLUMN)) {
/* 100 */       return paramBlockState;
/*     */     }
/* 102 */     if (paramBlockState.is(Blocks.SOUL_SAND)) {
/* 103 */       return (BlockState)Blocks.BUBBLE_COLUMN.defaultBlockState().setValue((Property)DRAG_DOWN, Boolean.valueOf(false));
/*     */     }
/* 105 */     if (paramBlockState.is(Blocks.MAGMA_BLOCK)) {
/* 106 */       return (BlockState)Blocks.BUBBLE_COLUMN.defaultBlockState().setValue((Property)DRAG_DOWN, Boolean.valueOf(true));
/*     */     }
/*     */     
/* 109 */     return Blocks.WATER.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 114 */     double d1 = paramBlockPos.getX();
/* 115 */     double d2 = paramBlockPos.getY();
/* 116 */     double d3 = paramBlockPos.getZ();
/*     */     
/* 118 */     if (((Boolean)paramBlockState.getValue((Property)DRAG_DOWN)).booleanValue()) {
/* 119 */       paramLevel.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.CURRENT_DOWN, d1 + 0.5D, d2 + 0.8D, d3, 0.0D, 0.0D, 0.0D);
/* 120 */       if (paramRandomSource.nextInt(200) == 0) {
/* 121 */         paramLevel.playLocalSound(d1, d2, d3, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.2F + paramRandomSource.nextFloat() * 0.2F, 0.9F + paramRandomSource.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } else {
/* 124 */       paramLevel.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.BUBBLE_COLUMN_UP, d1 + 0.5D, d2, d3 + 0.5D, 0.0D, 0.04D, 0.0D);
/* 125 */       paramLevel.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.BUBBLE_COLUMN_UP, d1 + paramRandomSource.nextFloat(), d2 + paramRandomSource.nextFloat(), d3 + paramRandomSource.nextFloat(), 0.0D, 0.04D, 0.0D);
/* 126 */       if (paramRandomSource.nextInt(200) == 0) {
/* 127 */         paramLevel.playLocalSound(d1, d2, d3, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + paramRandomSource.nextFloat() * 0.2F, 0.9F + paramRandomSource.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 134 */     paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     
/* 136 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1) || paramDirection == Direction.DOWN || (paramDirection == Direction.UP && 
/*     */       
/* 138 */       !paramBlockState2.is(Blocks.BUBBLE_COLUMN) && canExistIn(paramBlockState2)))
/*     */     {
/* 140 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 5);
/*     */     }
/*     */     
/* 143 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 148 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/*     */     
/* 150 */     return (blockState.is(Blocks.BUBBLE_COLUMN) || blockState.is(Blocks.MAGMA_BLOCK) || blockState.is(Blocks.SOUL_SAND));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 155 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState paramBlockState) {
/* 160 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 165 */     paramBuilder.add(new Property[] { (Property)DRAG_DOWN });
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 170 */     paramLevelAccessor.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 11);
/* 171 */     return new ItemStack((ItemLike)Items.WATER_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 176 */     return Fluids.WATER.getPickupSound();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BubbleColumnBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */