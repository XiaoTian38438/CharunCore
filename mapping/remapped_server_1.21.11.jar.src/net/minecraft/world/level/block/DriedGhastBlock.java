/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DriedGhastBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*  39 */   public static final MapCodec<DriedGhastBlock> CODEC = simpleCodec(DriedGhastBlock::new);
/*     */   public static final int MAX_HYDRATION_LEVEL = 3;
/*     */   
/*     */   public MapCodec<DriedGhastBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  47 */   public static final IntegerProperty HYDRATION_LEVEL = BlockStateProperties.DRIED_GHAST_HYDRATION_LEVELS;
/*  48 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   public static final int HYDRATION_TICK_DELAY = 5000;
/*  51 */   private static final VoxelShape SHAPE = Block.column(10.0D, 10.0D, 0.0D, 10.0D);
/*     */   
/*     */   public DriedGhastBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HYDRATION_LEVEL, Integer.valueOf(0))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  60 */     paramBuilder.add(new Property[] { (Property)FACING, (Property)HYDRATION_LEVEL, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  65 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  66 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  69 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  74 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public int getHydrationLevel(BlockState paramBlockState) {
/*  78 */     return ((Integer)paramBlockState.getValue((Property)HYDRATION_LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   private boolean isReadyToSpawn(BlockState paramBlockState) {
/*  82 */     return (getHydrationLevel(paramBlockState) == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  87 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  88 */       tickWaterlogged(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*     */       return;
/*     */     } 
/*  91 */     int i = getHydrationLevel(paramBlockState);
/*  92 */     if (i > 0) {
/*  93 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)HYDRATION_LEVEL, Integer.valueOf(i - 1)), 2);
/*  94 */       paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickWaterlogged(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  99 */     if (!isReadyToSpawn(paramBlockState)) {
/* 100 */       paramServerLevel.playSound(null, paramBlockPos, SoundEvents.DRIED_GHAST_TRANSITION, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 101 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)HYDRATION_LEVEL, Integer.valueOf(getHydrationLevel(paramBlockState) + 1)), 2);
/* 102 */       paramServerLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramBlockState));
/*     */     } else {
/* 104 */       spawnGhastling(paramServerLevel, paramBlockPos, paramBlockState);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnGhastling(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 109 */     paramServerLevel.removeBlock(paramBlockPos, false);
/*     */     
/* 111 */     HappyGhast happyGhast = (HappyGhast)EntityType.HAPPY_GHAST.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/* 112 */     if (happyGhast != null) {
/* 113 */       Vec3 vec3 = paramBlockPos.getBottomCenter();
/* 114 */       happyGhast.setBaby(true);
/* 115 */       float f = Direction.getYRot((Direction)paramBlockState.getValue((Property)FACING));
/* 116 */       happyGhast.setYHeadRot(f);
/* 117 */       happyGhast.snapTo(vec3.x(), vec3.y(), vec3.z(), f, 0.0F);
/* 118 */       paramServerLevel.addFreshEntity((Entity)happyGhast);
/* 119 */       paramServerLevel.playSound(null, (Entity)happyGhast, SoundEvents.GHASTLING_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 126 */     double d1 = paramBlockPos.getX() + 0.5D;
/* 127 */     double d2 = paramBlockPos.getY() + 0.5D;
/* 128 */     double d3 = paramBlockPos.getZ() + 0.5D;
/*     */     
/* 130 */     if (!((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 131 */       if (paramRandomSource.nextInt(40) == 0 && paramLevel.getBlockState(paramBlockPos.below()).is(BlockTags.TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS)) {
/* 132 */         paramLevel.playLocalSound(d1, d2, d3, SoundEvents.DRIED_GHAST_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */       }
/* 134 */       if (paramRandomSource.nextInt(6) == 0) {
/* 135 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.WHITE_SMOKE, d1, d2, d3, 0.0D, 0.02D, 0.0D);
/*     */       }
/*     */     } else {
/* 138 */       if (paramRandomSource.nextInt(40) == 0) {
/* 139 */         paramLevel.playLocalSound(d1, d2, d3, SoundEvents.DRIED_GHAST_AMBIENT_WATER, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */       }
/* 141 */       if (paramRandomSource.nextInt(6) == 0) {
/* 142 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, d1 + ((paramRandomSource.nextFloat() * 2.0F - 1.0F) / 3.0F), d2 + 0.4D, d3 + ((paramRandomSource.nextFloat() * 2.0F - 1.0F) / 3.0F), 0.0D, paramRandomSource.nextFloat(), 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 149 */     if ((((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue() || ((Integer)paramBlockState.getValue((Property)HYDRATION_LEVEL)).intValue() > 0) && !paramServerLevel.getBlockTicks().hasScheduledTick(paramBlockPos, this)) {
/* 150 */       paramServerLevel.scheduleTick(paramBlockPos, this, 5000);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 156 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/* 157 */     boolean bool = (fluidState.getType() == Fluids.WATER) ? true : false;
/* 158 */     return (BlockState)((BlockState)super.getStateForPlacement(paramBlockPlaceContext).setValue((Property)WATERLOGGED, Boolean.valueOf(bool))).setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 163 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 164 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 166 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 171 */     if (((Boolean)paramBlockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() || paramFluidState.getType() != Fluids.WATER) {
/* 172 */       return false;
/*     */     }
/* 174 */     if (!paramLevelAccessor.isClientSide()) {
/* 175 */       paramLevelAccessor.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
/* 176 */       paramLevelAccessor.scheduleTick(paramBlockPos, paramFluidState.getType(), paramFluidState.getType().getTickDelay((LevelReader)paramLevelAccessor));
/* 177 */       paramLevelAccessor.playSound(null, paramBlockPos, SoundEvents.DRIED_GHAST_PLACE_IN_WATER, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 185 */     super.setPlacedBy(paramLevel, paramBlockPos, paramBlockState, paramLivingEntity, paramItemStack);
/* 186 */     paramLevel.playSound(null, paramBlockPos, ((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue() ? SoundEvents.DRIED_GHAST_PLACE_IN_WATER : SoundEvents.DRIED_GHAST_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 191 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\DriedGhastBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */