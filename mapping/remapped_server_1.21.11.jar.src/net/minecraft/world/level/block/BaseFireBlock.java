/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BaseFireBlock extends Block {
/*     */   private static final int SECONDS_ON_FIRE = 8;
/*     */   private static final int MIN_FIRE_TICKS_TO_ADD = 1;
/*     */   private static final int MAX_FIRE_TICKS_TO_ADD = 3;
/*     */   private final float fireDamage;
/*  33 */   protected static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.0D);
/*     */   
/*     */   public BaseFireBlock(BlockBehaviour.Properties paramProperties, float paramFloat) {
/*  36 */     super(paramProperties);
/*  37 */     this.fireDamage = paramFloat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  45 */     return getState((BlockGetter)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos());
/*     */   }
/*     */   
/*     */   public static BlockState getState(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  49 */     BlockPos blockPos = paramBlockPos.below();
/*  50 */     BlockState blockState = paramBlockGetter.getBlockState(blockPos);
/*     */     
/*  52 */     if (SoulFireBlock.canSurviveOnBlock(blockState)) {
/*  53 */       return Blocks.SOUL_FIRE.defaultBlockState();
/*     */     }
/*     */     
/*  56 */     return ((FireBlock)Blocks.FIRE).getStateForPlacement(paramBlockGetter, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  61 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  66 */     if (paramRandomSource.nextInt(24) == 0) {
/*  67 */       paramLevel.playLocalSound(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 0.5D, paramBlockPos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + paramRandomSource.nextFloat(), paramRandomSource.nextFloat() * 0.7F + 0.3F, false);
/*     */     }
/*     */     
/*  70 */     BlockPos blockPos = paramBlockPos.below();
/*  71 */     BlockState blockState = paramLevel.getBlockState(blockPos);
/*  72 */     if (canBurn(blockState) || blockState.isFaceSturdy((BlockGetter)paramLevel, blockPos, Direction.UP)) {
/*  73 */       for (byte b = 0; b < 3; b++) {
/*  74 */         double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/*  75 */         double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble() * 0.5D + 0.5D;
/*  76 */         double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*  77 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } else {
/*  80 */       if (canBurn(paramLevel.getBlockState(paramBlockPos.west()))) {
/*  81 */         for (byte b = 0; b < 2; b++) {
/*  82 */           double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble() * 0.10000000149011612D;
/*  83 */           double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble();
/*  84 */           double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*  85 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*  88 */       if (canBurn(paramLevel.getBlockState(paramBlockPos.east()))) {
/*  89 */         for (byte b = 0; b < 2; b++) {
/*  90 */           double d1 = (paramBlockPos.getX() + 1) - paramRandomSource.nextDouble() * 0.10000000149011612D;
/*  91 */           double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble();
/*  92 */           double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/*  93 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*  96 */       if (canBurn(paramLevel.getBlockState(paramBlockPos.north()))) {
/*  97 */         for (byte b = 0; b < 2; b++) {
/*  98 */           double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/*  99 */           double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble();
/* 100 */           double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble() * 0.10000000149011612D;
/* 101 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/* 104 */       if (canBurn(paramLevel.getBlockState(paramBlockPos.south()))) {
/* 105 */         for (byte b = 0; b < 2; b++) {
/* 106 */           double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/* 107 */           double d2 = paramBlockPos.getY() + paramRandomSource.nextDouble();
/* 108 */           double d3 = (paramBlockPos.getZ() + 1) - paramRandomSource.nextDouble() * 0.10000000149011612D;
/* 109 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/* 112 */       if (canBurn(paramLevel.getBlockState(paramBlockPos.above()))) {
/* 113 */         for (byte b = 0; b < 2; b++) {
/* 114 */           double d1 = paramBlockPos.getX() + paramRandomSource.nextDouble();
/* 115 */           double d2 = (paramBlockPos.getY() + 1) - paramRandomSource.nextDouble() * 0.10000000149011612D;
/* 116 */           double d3 = paramBlockPos.getZ() + paramRandomSource.nextDouble();
/* 117 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 127 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 128 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.FIRE_IGNITE);
/* 129 */     paramInsideBlockEffectApplier.runAfter(InsideBlockEffectType.FIRE_IGNITE, paramEntity -> paramEntity.hurt(paramEntity.level().damageSources().inFire(), this.fireDamage));
/*     */   }
/*     */   
/*     */   public static void fireIgnite(Entity paramEntity) {
/* 133 */     if (!paramEntity.fireImmune()) {
/* 134 */       if (paramEntity.getRemainingFireTicks() < 0) {
/* 135 */         paramEntity.setRemainingFireTicks(paramEntity.getRemainingFireTicks() + 1);
/* 136 */       } else if (paramEntity instanceof net.minecraft.server.level.ServerPlayer) {
/* 137 */         int i = paramEntity.level().getRandom().nextInt(1, 3);
/* 138 */         paramEntity.setRemainingFireTicks(paramEntity.getRemainingFireTicks() + i);
/*     */       } 
/*     */       
/* 141 */       if (paramEntity.getRemainingFireTicks() >= 0) {
/* 142 */         paramEntity.igniteForSeconds(8.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 149 */     if (paramBlockState2.is(paramBlockState1.getBlock())) {
/*     */       return;
/*     */     }
/* 152 */     if (inPortalDimension(paramLevel)) {
/* 153 */       Optional<PortalShape> optional = PortalShape.findEmptyPortalShape((LevelAccessor)paramLevel, paramBlockPos, Direction.Axis.X);
/*     */       
/* 155 */       if (optional.isPresent()) {
/* 156 */         ((PortalShape)optional.get()).createPortalBlocks((LevelAccessor)paramLevel);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     if (!paramBlockState1.canSurvive((LevelReader)paramLevel, paramBlockPos)) {
/* 163 */       paramLevel.removeBlock(paramBlockPos, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean inPortalDimension(Level paramLevel) {
/* 168 */     return (paramLevel.dimension() == Level.OVERWORLD || paramLevel.dimension() == Level.NETHER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void spawnDestroyParticles(Level paramLevel, Player paramPlayer, BlockPos paramBlockPos, BlockState paramBlockState) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Player paramPlayer) {
/* 178 */     if (!paramLevel.isClientSide()) {
/* 179 */       paramLevel.levelEvent(null, 1009, paramBlockPos, 0);
/*     */     }
/* 181 */     return super.playerWillDestroy(paramLevel, paramBlockPos, paramBlockState, paramPlayer);
/*     */   }
/*     */   
/*     */   public static boolean canBePlacedAt(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 185 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */     
/* 187 */     if (!blockState.isAir()) {
/* 188 */       return false;
/*     */     }
/*     */     
/* 191 */     return (getState((BlockGetter)paramLevel, paramBlockPos).canSurvive((LevelReader)paramLevel, paramBlockPos) || isPortal(paramLevel, paramBlockPos, paramDirection));
/*     */   }
/*     */   
/*     */   private static boolean isPortal(Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 195 */     if (!inPortalDimension(paramLevel)) {
/* 196 */       return false;
/*     */     }
/* 198 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 199 */     boolean bool = false;
/* 200 */     for (Direction direction : Direction.values()) {
/* 201 */       if (paramLevel.getBlockState((BlockPos)mutableBlockPos.set((Vec3i)paramBlockPos).move(direction)).is(Blocks.OBSIDIAN)) {
/* 202 */         bool = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 207 */     if (!bool) {
/* 208 */       return false;
/*     */     }
/*     */     
/* 211 */     Direction.Axis axis = paramDirection.getAxis().isHorizontal() ? paramDirection.getCounterClockWise().getAxis() : Direction.Plane.HORIZONTAL.getRandomAxis(paramLevel.random);
/* 212 */     return PortalShape.findEmptyPortalShape((LevelAccessor)paramLevel, paramBlockPos, axis).isPresent();
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends BaseFireBlock> codec();
/*     */   
/*     */   protected abstract boolean canBurn(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BaseFireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */