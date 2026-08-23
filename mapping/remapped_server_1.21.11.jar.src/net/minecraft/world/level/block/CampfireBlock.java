/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.CampfireBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  56 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.BOOL.fieldOf("spawn_particles").forGetter(()), (App)Codec.intRange(0, 1000).fieldOf("fire_damage").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, CampfireBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CampfireBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<CampfireBlock> codec() {
/*  64 */     return CODEC;
/*     */   }
/*     */   
/*  67 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*  68 */   public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
/*  69 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  70 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*     */   
/*  72 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 7.0D);
/*     */   
/*  74 */   private static final VoxelShape SHAPE_VIRTUAL_POST = Block.column(4.0D, 0.0D, 16.0D);
/*     */   
/*     */   private static final int SMOKE_DISTANCE = 5;
/*     */   private final boolean spawnParticles;
/*     */   private final int fireDamage;
/*     */   
/*     */   public CampfireBlock(boolean paramBoolean, int paramInt, BlockBehaviour.Properties paramProperties) {
/*  81 */     super(paramProperties);
/*  82 */     this.spawnParticles = paramBoolean;
/*  83 */     this.fireDamage = paramInt;
/*  84 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(true))).setValue((Property)SIGNAL_FIRE, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*  89 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos);
/*  90 */     if (blockEntity instanceof CampfireBlockEntity) { CampfireBlockEntity campfireBlockEntity = (CampfireBlockEntity)blockEntity;
/*  91 */       ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*  92 */       if (paramLevel.recipeAccess().propertySet(RecipePropertySet.CAMPFIRE_INPUT).test(itemStack)) {
/*  93 */         if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (campfireBlockEntity.placeFood(serverLevel, (LivingEntity)paramPlayer, itemStack)) {
/*  94 */             paramPlayer.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
/*  95 */             return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */           }  }
/*  97 */          return (InteractionResult)InteractionResult.CONSUME;
/*     */       }  }
/*     */ 
/*     */     
/* 101 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 106 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() && paramEntity instanceof LivingEntity) {
/* 107 */       paramEntity.hurt(paramLevel.damageSources().campfire(), this.fireDamage);
/*     */     }
/*     */     
/* 110 */     super.entityInside(paramBlockState, paramLevel, paramBlockPos, paramEntity, paramInsideBlockEffectApplier, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 115 */     Level level = paramBlockPlaceContext.getLevel();
/* 116 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 117 */     boolean bool = (level.getFluidState(blockPos).getType() == Fluids.WATER) ? true : false;
/* 118 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 119 */       .setValue((Property)WATERLOGGED, Boolean.valueOf(bool)))
/* 120 */       .setValue((Property)SIGNAL_FIRE, Boolean.valueOf(isSmokeSource(level.getBlockState(blockPos.below())))))
/* 121 */       .setValue((Property)LIT, Boolean.valueOf(!bool)))
/* 122 */       .setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 127 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 128 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 131 */     if (paramDirection == Direction.DOWN) {
/* 132 */       return (BlockState)paramBlockState1.setValue((Property)SIGNAL_FIRE, Boolean.valueOf(isSmokeSource(paramBlockState2)));
/*     */     }
/* 134 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */   
/*     */   private boolean isSmokeSource(BlockState paramBlockState) {
/* 138 */     return paramBlockState.is(Blocks.HAY_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 143 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 148 */     if (!((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 152 */     if (paramRandomSource.nextInt(10) == 0) {
/* 153 */       paramLevel.playLocalSound(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + paramRandomSource.nextFloat(), paramRandomSource.nextFloat() * 0.7F + 0.6F, false);
/*     */     }
/*     */     
/* 156 */     if (this.spawnParticles && paramRandomSource.nextInt(5) == 0) {
/* 157 */       for (byte b = 0; b < paramRandomSource.nextInt(1) + 1; b++) {
/* 158 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.LAVA, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, (paramRandomSource.nextFloat() / 2.0F), 5.0E-5D, (paramRandomSource.nextFloat() / 2.0F));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void dowse(Entity paramEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 164 */     if (paramLevelAccessor.isClientSide()) {
/* 165 */       for (byte b = 0; b < 20; b++) {
/* 166 */         makeParticles((Level)paramLevelAccessor, paramBlockPos, ((Boolean)paramBlockState.getValue((Property)SIGNAL_FIRE)).booleanValue(), true);
/*     */       }
/*     */     }
/* 169 */     paramLevelAccessor.gameEvent(paramEntity, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 174 */     if (!((Boolean)paramBlockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && paramFluidState.getType() == Fluids.WATER) {
/* 175 */       boolean bool = ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue();
/* 176 */       if (bool) {
/* 177 */         if (!paramLevelAccessor.isClientSide()) {
/* 178 */           paramLevelAccessor.playSound(null, paramBlockPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */         }
/*     */         
/* 181 */         dowse((Entity)null, paramLevelAccessor, paramBlockPos, paramBlockState);
/*     */       } 
/*     */       
/* 184 */       paramLevelAccessor.setBlock(paramBlockPos, (BlockState)((BlockState)paramBlockState.setValue((Property)WATERLOGGED, Boolean.valueOf(true))).setValue((Property)LIT, Boolean.valueOf(false)), 3);
/* 185 */       paramLevelAccessor.scheduleTick(paramBlockPos, paramFluidState.getType(), paramFluidState.getType().getTickDelay((LevelReader)paramLevelAccessor));
/* 186 */       return true;
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 193 */     BlockPos blockPos = paramBlockHitResult.getBlockPos();
/* 194 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramProjectile.isOnFire() && paramProjectile.mayInteract(serverLevel, blockPos) && !((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue() && !((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue())
/* 195 */         paramLevel.setBlock(blockPos, (BlockState)paramBlockState.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)), 11);  }
/*     */   
/*     */   }
/*     */   
/*     */   public static void makeParticles(Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean1, boolean paramBoolean2) {
/* 200 */     RandomSource randomSource = paramLevel.getRandom();
/* 201 */     SimpleParticleType simpleParticleType = paramBoolean1 ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
/* 202 */     paramLevel.addAlwaysVisibleParticle((ParticleOptions)simpleParticleType, true, paramBlockPos
/*     */         
/* 204 */         .getX() + 0.5D + randomSource.nextDouble() / 3.0D * (randomSource.nextBoolean() ? true : -1), paramBlockPos
/* 205 */         .getY() + randomSource.nextDouble() + randomSource.nextDouble(), paramBlockPos
/* 206 */         .getZ() + 0.5D + randomSource.nextDouble() / 3.0D * (randomSource.nextBoolean() ? true : -1), 0.0D, 0.07D, 0.0D);
/*     */ 
/*     */     
/* 209 */     if (paramBoolean2) {
/* 210 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, paramBlockPos
/* 211 */           .getX() + 0.5D + randomSource.nextDouble() / 4.0D * (randomSource.nextBoolean() ? true : -1), paramBlockPos
/* 212 */           .getY() + 0.4D, paramBlockPos
/* 213 */           .getZ() + 0.5D + randomSource.nextDouble() / 4.0D * (randomSource.nextBoolean() ? true : -1), 0.0D, 0.005D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSmokeyPos(Level paramLevel, BlockPos paramBlockPos) {
/* 226 */     for (byte b = 1; b <= 5; b++) {
/* 227 */       BlockPos blockPos = paramBlockPos.below(b);
/* 228 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/* 229 */       if (isLitCampfire(blockState)) {
/* 230 */         return true;
/*     */       }
/*     */       
/* 233 */       boolean bool = Shapes.joinIsNotEmpty(SHAPE_VIRTUAL_POST, blockState.getCollisionShape((BlockGetter)paramLevel, paramBlockPos, CollisionContext.empty()), BooleanOp.AND);
/* 234 */       if (bool) {
/*     */ 
/*     */         
/* 237 */         BlockState blockState1 = paramLevel.getBlockState(blockPos.below());
/* 238 */         return isLitCampfire(blockState1);
/*     */       } 
/*     */     } 
/* 241 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isLitCampfire(BlockState paramBlockState) {
/* 246 */     return (paramBlockState.hasProperty((Property)LIT) && paramBlockState.is(BlockTags.CAMPFIRES) && ((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 251 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 252 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 254 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 259 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 264 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 269 */     paramBuilder.add(new Property[] { (Property)LIT, (Property)SIGNAL_FIRE, (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 274 */     return (BlockEntity)new CampfireBlockEntity(paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 279 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 280 */       if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/* 281 */         RecipeManager.CachedCheck cachedCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
/* 282 */         return createTickerHelper(paramBlockEntityType, BlockEntityType.CAMPFIRE, (paramLevel, paramBlockPos, paramBlockState, paramCampfireBlockEntity) -> CampfireBlockEntity.cookTick(paramServerLevel, paramBlockPos, paramBlockState, paramCampfireBlockEntity, paramCachedCheck));
/*     */       } 
/* 284 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.CAMPFIRE, CampfireBlockEntity::cooldownTick); }
/*     */ 
/*     */     
/* 287 */     if (((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue()) {
/* 288 */       return createTickerHelper(paramBlockEntityType, BlockEntityType.CAMPFIRE, CampfireBlockEntity::particleTick);
/*     */     }
/*     */     
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 296 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState paramBlockState) {
/* 300 */     return (paramBlockState.is(BlockTags.CAMPFIRES, paramBlockStateBase -> (paramBlockStateBase.hasProperty((Property)WATERLOGGED) && paramBlockStateBase.hasProperty((Property)LIT))) && !((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue() && !((Boolean)paramBlockState.getValue((Property)LIT)).booleanValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CampfireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */