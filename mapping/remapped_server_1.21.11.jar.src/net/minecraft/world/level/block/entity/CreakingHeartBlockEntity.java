/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.TrailParticleOption;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpawnUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.Mutable;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreakingHeartBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final int PLAYER_DETECTION_RANGE = 32;
/*     */   public static final int CREAKING_ROAMING_RADIUS = 32;
/*     */   private static final int DISTANCE_CREAKING_TOO_FAR = 34;
/*     */   private static final int SPAWN_RANGE_XZ = 16;
/*     */   private static final int SPAWN_RANGE_Y = 8;
/*     */   private static final int ATTEMPTS_PER_SPAWN = 5;
/*     */   private static final int UPDATE_TICKS = 20;
/*  63 */   private static final Optional<Creaking> NO_CREAKING = Optional.empty(); private static final int UPDATE_TICKS_VARIANCE = 5; private static final int HURT_CALL_TOTAL_TICKS = 100; private static final int NUMBER_OF_HURT_CALLS = 10; private static final int HURT_CALL_INTERVAL = 10; private static final int HURT_CALL_PARTICLE_TICKS = 50; private static final int MAX_DEPTH = 2; private static final int MAX_COUNT = 64;
/*     */   private static final int TICKS_GRACE_PERIOD = 30;
/*     */   private Either<Creaking, UUID> creakingInfo;
/*     */   private long ticksExisted;
/*     */   private int ticker;
/*     */   private int emitter;
/*     */   private Vec3 emitterTarget;
/*     */   private int outputSignal;
/*     */   
/*     */   public CreakingHeartBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  73 */     super(BlockEntityType.CREAKING_HEART, paramBlockPos, paramBlockState);
/*     */   }
/*     */   public static void serverTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, CreakingHeartBlockEntity paramCreakingHeartBlockEntity) {
/*     */     ServerLevel serverLevel;
/*  77 */     paramCreakingHeartBlockEntity.ticksExisted++;
/*  78 */     if (paramLevel instanceof ServerLevel) { serverLevel = (ServerLevel)paramLevel; }
/*     */     else
/*     */     { return; }
/*     */     
/*  82 */     int i = paramCreakingHeartBlockEntity.computeAnalogOutputSignal();
/*  83 */     if (paramCreakingHeartBlockEntity.outputSignal != i) {
/*  84 */       paramCreakingHeartBlockEntity.outputSignal = i;
/*  85 */       paramLevel.updateNeighbourForOutputSignal(paramBlockPos, Blocks.CREAKING_HEART);
/*     */     } 
/*     */     
/*  88 */     if (paramCreakingHeartBlockEntity.emitter > 0) {
/*  89 */       if (paramCreakingHeartBlockEntity.emitter > 50) {
/*  90 */         paramCreakingHeartBlockEntity.emitParticles(serverLevel, 1, true);
/*  91 */         paramCreakingHeartBlockEntity.emitParticles(serverLevel, 1, false);
/*     */       } 
/*  93 */       if (paramCreakingHeartBlockEntity.emitter % 10 == 0 && paramCreakingHeartBlockEntity.emitterTarget != null) {
/*  94 */         paramCreakingHeartBlockEntity.getCreakingProtector().ifPresent(paramCreaking -> paramCreakingHeartBlockEntity.emitterTarget = paramCreaking.getBoundingBox().getCenter());
/*     */         
/*  96 */         Vec3 vec31 = Vec3.atCenterOf((Vec3i)paramBlockPos);
/*     */         
/*  98 */         float f1 = 0.2F + 0.8F * (100 - paramCreakingHeartBlockEntity.emitter) / 100.0F;
/*  99 */         Vec3 vec32 = vec31.subtract(paramCreakingHeartBlockEntity.emitterTarget).scale(f1).add(paramCreakingHeartBlockEntity.emitterTarget);
/* 100 */         BlockPos blockPos = BlockPos.containing((Position)vec32);
/* 101 */         float f2 = paramCreakingHeartBlockEntity.emitter / 2.0F / 100.0F + 0.5F;
/* 102 */         serverLevel.playSound(null, blockPos, SoundEvents.CREAKING_HEART_HURT, SoundSource.BLOCKS, f2, 1.0F);
/*     */       } 
/*     */       
/* 105 */       paramCreakingHeartBlockEntity.emitter--;
/*     */     } 
/*     */     
/* 108 */     if (paramCreakingHeartBlockEntity.ticker-- >= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     paramCreakingHeartBlockEntity.ticker = (paramCreakingHeartBlockEntity.level == null) ? 20 : (paramCreakingHeartBlockEntity.level.random.nextInt(5) + 20);
/*     */     
/* 114 */     BlockState blockState = updateCreakingState(paramLevel, paramBlockState, paramBlockPos, paramCreakingHeartBlockEntity);
/* 115 */     if (blockState != paramBlockState) {
/* 116 */       paramLevel.setBlock(paramBlockPos, blockState, 3);
/* 117 */       if (blockState.getValue((Property)CreakingHeartBlock.STATE) == CreakingHeartState.UPROOTED) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 122 */     if (paramCreakingHeartBlockEntity.creakingInfo != null) {
/* 123 */       Optional<Creaking> optional = paramCreakingHeartBlockEntity.getCreakingProtector();
/* 124 */       if (optional.isPresent()) {
/* 125 */         Creaking creaking = optional.get();
/* 126 */         if ((!((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, paramBlockPos)).booleanValue() && !creaking.isPersistenceRequired()) || paramCreakingHeartBlockEntity.distanceToCreaking() > 34.0D || creaking.playerIsStuckInYou()) {
/* 127 */           paramCreakingHeartBlockEntity.removeProtector((DamageSource)null);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 133 */     if (blockState.getValue((Property)CreakingHeartBlock.STATE) != CreakingHeartState.AWAKE) {
/*     */       return;
/*     */     }
/*     */     
/* 137 */     if (!serverLevel.isSpawningMonsters()) {
/*     */       return;
/*     */     }
/*     */     
/* 141 */     Player player = paramLevel.getNearestPlayer(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), 32.0D, false);
/* 142 */     if (player != null) {
/* 143 */       Creaking creaking = spawnProtector(serverLevel, paramCreakingHeartBlockEntity);
/* 144 */       if (creaking != null) {
/* 145 */         paramCreakingHeartBlockEntity.setCreakingInfo(creaking);
/* 146 */         creaking.makeSound(SoundEvents.CREAKING_SPAWN);
/* 147 */         paramLevel.playSound(null, paramCreakingHeartBlockEntity.getBlockPos(), SoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState updateCreakingState(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, CreakingHeartBlockEntity paramCreakingHeartBlockEntity) {
/* 153 */     if (!CreakingHeartBlock.hasRequiredLogs(paramBlockState, (LevelReader)paramLevel, paramBlockPos) && paramCreakingHeartBlockEntity.creakingInfo == null) {
/* 154 */       return (BlockState)paramBlockState.setValue((Property)CreakingHeartBlock.STATE, (Comparable)CreakingHeartState.UPROOTED);
/*     */     }
/* 156 */     CreakingHeartState creakingHeartState = ((Boolean)paramLevel.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, paramBlockPos)).booleanValue() ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT;
/* 157 */     return (BlockState)paramBlockState.setValue((Property)CreakingHeartBlock.STATE, (Comparable)creakingHeartState);
/*     */   }
/*     */   
/*     */   private double distanceToCreaking() {
/* 161 */     return ((Double)getCreakingProtector().<Double>map(paramCreaking -> Double.valueOf(Math.sqrt(paramCreaking.distanceToSqr(Vec3.atBottomCenterOf((Vec3i)getBlockPos()))))).orElse(Double.valueOf(0.0D))).doubleValue();
/*     */   }
/*     */   
/*     */   private void clearCreakingInfo() {
/* 165 */     this.creakingInfo = null;
/* 166 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setCreakingInfo(Creaking paramCreaking) {
/* 170 */     this.creakingInfo = Either.left(paramCreaking);
/* 171 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setCreakingInfo(UUID paramUUID) {
/* 175 */     this.creakingInfo = Either.right(paramUUID);
/* 176 */     this.ticksExisted = 0L;
/* 177 */     setChanged();
/*     */   }
/*     */   
/*     */   private Optional<Creaking> getCreakingProtector() {
/* 181 */     if (this.creakingInfo == null) {
/* 182 */       return NO_CREAKING;
/*     */     }
/* 184 */     if (this.creakingInfo.left().isPresent()) {
/* 185 */       Creaking creaking = this.creakingInfo.left().get();
/* 186 */       if (!creaking.isRemoved()) {
/* 187 */         return Optional.of(creaking);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 192 */       setCreakingInfo(creaking.getUUID());
/*     */     } 
/* 194 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (this.creakingInfo.right().isPresent()) {
/*     */ 
/*     */         
/* 197 */         UUID uUID = this.creakingInfo.right().get();
/* 198 */         Entity entity = serverLevel.getEntity(uUID);
/* 199 */         if (entity instanceof Creaking) { Creaking creaking = (Creaking)entity;
/* 200 */           setCreakingInfo(creaking);
/* 201 */           return Optional.of(creaking); }
/*     */         
/* 203 */         if (this.ticksExisted >= 30L) {
/* 204 */           clearCreakingInfo();
/*     */         }
/* 206 */         return NO_CREAKING;
/*     */       }  }
/* 208 */      return NO_CREAKING;
/*     */   }
/*     */   
/*     */   private static Creaking spawnProtector(ServerLevel paramServerLevel, CreakingHeartBlockEntity paramCreakingHeartBlockEntity) {
/* 212 */     BlockPos blockPos = paramCreakingHeartBlockEntity.getBlockPos();
/* 213 */     Optional<Creaking> optional = SpawnUtil.trySpawnMob(EntityType.CREAKING, EntitySpawnReason.SPAWNER, paramServerLevel, blockPos, 5, 16, 8, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER_NO_LEAVES, true);
/* 214 */     if (optional.isEmpty()) {
/* 215 */       return null;
/*     */     }
/* 217 */     Creaking creaking = optional.get();
/* 218 */     paramServerLevel.gameEvent((Entity)creaking, (Holder)GameEvent.ENTITY_PLACE, creaking.position());
/* 219 */     paramServerLevel.broadcastEntityEvent((Entity)creaking, (byte)60);
/* 220 */     creaking.setTransient(blockPos);
/* 221 */     return creaking;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 226 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/* 231 */     return saveCustomOnly(paramProvider);
/*     */   } public void creakingHurt() {
/*     */     Creaking creaking1;
/*     */     ServerLevel serverLevel;
/* 235 */     Creaking creaking2 = (Creaking)getCreakingProtector().orElse(null); if (creaking2 instanceof Creaking) { creaking1 = creaking2; }
/*     */     else
/*     */     { return; }
/* 238 */      Level level = this.level; if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/* 241 */      if (this.emitter > 0) {
/*     */       return;
/*     */     }
/* 244 */     emitParticles(serverLevel, 20, false);
/* 245 */     if (getBlockState().getValue((Property)CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE) {
/* 246 */       int i = this.level.getRandom().nextIntBetweenInclusive(2, 3);
/* 247 */       for (byte b = 0; b < i; b++) {
/* 248 */         spreadResin(serverLevel).ifPresent(paramBlockPos -> {
/*     */               this.level.playSound(null, paramBlockPos, SoundEvents.RESIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */               this.level.gameEvent((Holder)GameEvent.BLOCK_PLACE, paramBlockPos, GameEvent.Context.of(getBlockState()));
/*     */             });
/*     */       } 
/*     */     } 
/* 254 */     this.emitter = 100;
/* 255 */     this.emitterTarget = creaking1.getBoundingBox().getCenter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<BlockPos> spreadResin(ServerLevel paramServerLevel) {
/* 262 */     MutableObject mutableObject = new MutableObject(null);
/* 263 */     BlockPos.breadthFirstTraversal(this.worldPosition, 2, 64, (paramBlockPos, paramConsumer) -> {
/*     */           for (Direction direction : Util.shuffledCopy((Object[])Direction.values(), paramServerLevel.random)) {
/*     */             BlockPos blockPos = paramBlockPos.relative(direction);
/*     */             
/*     */             if (paramServerLevel.getBlockState(blockPos).is(BlockTags.PALE_OAK_LOGS)) {
/*     */               paramConsumer.accept(blockPos);
/*     */             }
/*     */           } 
/*     */         }paramBlockPos -> {
/*     */           if (!paramServerLevel.getBlockState(paramBlockPos).is(BlockTags.PALE_OAK_LOGS)) {
/*     */             return BlockPos.TraversalNodeStatus.ACCEPT;
/*     */           }
/*     */           for (Direction direction1 : Util.shuffledCopy((Object[])Direction.values(), paramServerLevel.random)) {
/*     */             BlockPos blockPos = paramBlockPos.relative(direction1);
/*     */             BlockState blockState = paramServerLevel.getBlockState(blockPos);
/*     */             Direction direction2 = direction1.getOpposite();
/*     */             if (blockState.isAir()) {
/*     */               blockState = Blocks.RESIN_CLUMP.defaultBlockState();
/*     */             } else if (blockState.is(Blocks.WATER) && blockState.getFluidState().isSource()) {
/*     */               blockState = (BlockState)Blocks.RESIN_CLUMP.defaultBlockState().setValue((Property)MultifaceBlock.WATERLOGGED, Boolean.valueOf(true));
/*     */             } 
/*     */             if (blockState.is(Blocks.RESIN_CLUMP) && !MultifaceBlock.hasFace(blockState, direction2)) {
/*     */               paramServerLevel.setBlock(blockPos, (BlockState)blockState.setValue((Property)MultifaceBlock.getFaceProperty(direction2), Boolean.valueOf(true)), 3);
/*     */               paramMutable.setValue(blockPos);
/*     */               return BlockPos.TraversalNodeStatus.STOP;
/*     */             } 
/*     */           } 
/*     */           return BlockPos.TraversalNodeStatus.ACCEPT;
/*     */         });
/* 292 */     return Optional.ofNullable((BlockPos)mutableObject.get());
/*     */   }
/*     */   
/*     */   private void emitParticles(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean) {
/* 296 */     Creaking creaking1, creaking2 = (Creaking)getCreakingProtector().orElse(null); if (creaking2 instanceof Creaking) { creaking1 = creaking2; }
/*     */     else
/*     */     { return; }
/* 299 */      int i = paramBoolean ? 16545810 : 6250335;
/*     */     
/* 301 */     RandomSource randomSource = paramServerLevel.random;
/*     */     double d;
/* 303 */     for (d = 0.0D; d < paramInt; d++) {
/* 304 */       AABB aABB = creaking1.getBoundingBox();
/* 305 */       Vec3 vec31 = aABB.getMinPosition().add(randomSource.nextDouble() * aABB.getXsize(), randomSource.nextDouble() * aABB.getYsize(), randomSource.nextDouble() * aABB.getZsize());
/* 306 */       Vec3 vec32 = Vec3.atLowerCornerOf((Vec3i)getBlockPos()).add(randomSource.nextDouble(), randomSource.nextDouble(), randomSource.nextDouble());
/* 307 */       if (paramBoolean) {
/* 308 */         Vec3 vec3 = vec31;
/* 309 */         vec31 = vec32;
/* 310 */         vec32 = vec3;
/*     */       } 
/*     */       
/* 313 */       TrailParticleOption trailParticleOption = new TrailParticleOption(vec32, i, randomSource.nextInt(40) + 10);
/* 314 */       paramServerLevel.sendParticles((ParticleOptions)trailParticleOption, true, true, vec31.x, vec31.y, vec31.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 320 */     removeProtector((DamageSource)null);
/*     */   }
/*     */   
/*     */   public void removeProtector(DamageSource paramDamageSource) {
/* 324 */     Creaking creaking = (Creaking)getCreakingProtector().orElse(null); if (creaking instanceof Creaking) { Creaking creaking1 = creaking;
/* 325 */       if (paramDamageSource == null) {
/* 326 */         creaking1.tearDown();
/*     */       } else {
/* 328 */         creaking1.creakingDeathEffects(paramDamageSource);
/* 329 */         creaking1.setTearingDown();
/* 330 */         creaking1.setHealth(0.0F);
/*     */       } 
/* 332 */       clearCreakingInfo(); }
/*     */   
/*     */   }
/*     */   
/*     */   public boolean isProtector(Creaking paramCreaking) {
/* 337 */     return ((Boolean)getCreakingProtector().<Boolean>map(paramCreaking2 -> Boolean.valueOf((paramCreaking2 == paramCreaking1))).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public int getAnalogOutputSignal() {
/* 341 */     return this.outputSignal;
/*     */   }
/*     */   
/*     */   public int computeAnalogOutputSignal() {
/* 345 */     if (this.creakingInfo == null || getCreakingProtector().isEmpty()) {
/* 346 */       return 0;
/*     */     }
/* 348 */     double d1 = distanceToCreaking();
/* 349 */     double d2 = Math.clamp(d1, 0.0D, 32.0D) / 32.0D;
/* 350 */     return 15 - (int)Math.floor(d2 * 15.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/* 355 */     super.loadAdditional(paramValueInput);
/* 356 */     paramValueInput.read("creaking", UUIDUtil.CODEC).ifPresentOrElse(this::setCreakingInfo, this::clearCreakingInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/* 364 */     super.saveAdditional(paramValueOutput);
/* 365 */     if (this.creakingInfo != null)
/* 366 */       paramValueOutput.store("creaking", UUIDUtil.CODEC, this.creakingInfo.map(Entity::getUUID, paramUUID -> paramUUID)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\CreakingHeartBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */