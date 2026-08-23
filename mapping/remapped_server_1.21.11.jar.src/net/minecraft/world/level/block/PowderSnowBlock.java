/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PowderSnowBlock extends Block implements BucketPickup {
/*  38 */   public static final MapCodec<PowderSnowBlock> CODEC = simpleCodec(PowderSnowBlock::new);
/*     */   private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 0.083333336F;
/*     */   
/*     */   public MapCodec<PowderSnowBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9F;
/*     */   
/*     */   private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5F;
/*     */   private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;
/*  50 */   private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.8999999761581421D, 1.0D);
/*     */   private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0D;
/*     */   private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0D;
/*     */   
/*     */   public PowderSnowBlock(BlockBehaviour.Properties paramProperties) {
/*  55 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skipRendering(BlockState paramBlockState1, BlockState paramBlockState2, Direction paramDirection) {
/*  60 */     if (paramBlockState2.is(this)) {
/*  61 */       return true;
/*     */     }
/*  63 */     return super.skipRendering(paramBlockState1, paramBlockState2, paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  68 */     if (!(paramEntity instanceof LivingEntity) || paramEntity.getInBlockState().is(this)) {
/*  69 */       paramEntity.makeStuckInBlock(paramBlockState, new Vec3(0.8999999761581421D, 1.5D, 0.8999999761581421D));
/*     */       
/*  71 */       if (paramLevel.isClientSide()) {
/*  72 */         RandomSource randomSource = paramLevel.getRandom();
/*  73 */         boolean bool = (paramEntity.xOld != paramEntity.getX() || paramEntity.zOld != paramEntity.getZ()) ? true : false;
/*     */         
/*  75 */         if (bool && randomSource.nextBoolean()) {
/*  76 */           paramLevel.addParticle((ParticleOptions)ParticleTypes.SNOWFLAKE, paramEntity.getX(), (paramBlockPos.getY() + 1), paramEntity.getZ(), (Mth.randomBetween(randomSource, -1.0F, 1.0F) * 0.083333336F), 0.05000000074505806D, (Mth.randomBetween(randomSource, -1.0F, 1.0F) * 0.083333336F));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     BlockPos blockPos = paramBlockPos.immutable();
/*  82 */     paramInsideBlockEffectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, paramEntity -> {
/*     */           if (paramLevel instanceof ServerLevel) {
/*     */             ServerLevel serverLevel = (ServerLevel)paramLevel;
/*     */             if (paramEntity.isOnFire() && (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || paramEntity instanceof net.minecraft.world.entity.player.Player) && paramEntity.mayInteract(serverLevel, paramBlockPos))
/*     */               paramLevel.destroyBlock(paramBlockPos, false); 
/*     */           } 
/*     */         });
/*  89 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.FREEZE);
/*  90 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/*     */     LivingEntity livingEntity;
/*  96 */     if (paramDouble >= 4.0D && paramEntity instanceof LivingEntity) { livingEntity = (LivingEntity)paramEntity; }
/*     */     else
/*     */     { return; }
/*     */     
/* 100 */     LivingEntity.Fallsounds fallsounds = livingEntity.getFallSounds();
/* 101 */     SoundEvent soundEvent = (paramDouble < 7.0D) ? fallsounds.small() : fallsounds.big();
/*     */     
/* 103 */     paramEntity.playSound(soundEvent, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/* 108 */     VoxelShape voxelShape = getCollisionShape(paramBlockState, paramBlockGetter, paramBlockPos, CollisionContext.of(paramEntity));
/* 109 */     return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 114 */     if (!paramCollisionContext.isPlacement() && paramCollisionContext instanceof EntityCollisionContext) { EntityCollisionContext entityCollisionContext = (EntityCollisionContext)paramCollisionContext;
/* 115 */       Entity entity = entityCollisionContext.getEntity();
/* 116 */       if (entity != null) {
/* 117 */         if (entity.fallDistance > 2.5D) {
/* 118 */           return FALLING_COLLISION_SHAPE;
/*     */         }
/*     */         
/* 121 */         boolean bool = entity instanceof net.minecraft.world.entity.item.FallingBlockEntity;
/* 122 */         if (bool || (canEntityWalkOnPowderSnow(entity) && paramCollisionContext.isAbove(Shapes.block(), paramBlockPos, false) && !paramCollisionContext.isDescending())) {
/* 123 */           return super.getCollisionShape(paramBlockState, paramBlockGetter, paramBlockPos, paramCollisionContext);
/*     */         }
/*     */       }  }
/*     */     
/* 127 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getVisualShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 132 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   public static boolean canEntityWalkOnPowderSnow(Entity paramEntity) {
/* 136 */     if (paramEntity.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
/* 137 */       return true;
/*     */     }
/*     */     
/* 140 */     if (paramEntity instanceof LivingEntity) {
/* 141 */       return ((LivingEntity)paramEntity).getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS);
/*     */     }
/*     */     
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity paramLivingEntity, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 149 */     paramLevelAccessor.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 11);
/* 150 */     if (!paramLevelAccessor.isClientSide()) {
/* 151 */       paramLevelAccessor.levelEvent(2001, paramBlockPos, Block.getId(paramBlockState));
/*     */     }
/* 153 */     return new ItemStack((ItemLike)Items.POWDER_SNOW_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 158 */     return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 163 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PowderSnowBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */