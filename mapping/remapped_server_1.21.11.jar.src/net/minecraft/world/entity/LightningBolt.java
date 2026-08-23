/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.LightningRodBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LightningBolt extends Entity {
/*     */   private static final int START_LIFE = 2;
/*     */   private static final double DAMAGE_RADIUS = 3.0D;
/*     */   private static final double DETECTION_RADIUS = 15.0D;
/*     */   private int life;
/*     */   public long seed;
/*     */   private int flashes;
/*     */   private boolean visualOnly;
/*     */   private ServerPlayer cause;
/*  45 */   private final Set<Entity> hitEntities = Sets.newHashSet();
/*     */   private int blocksSetOnFire;
/*     */   
/*     */   public LightningBolt(EntityType<? extends LightningBolt> paramEntityType, Level paramLevel) {
/*  49 */     super(paramEntityType, paramLevel);
/*     */     
/*  51 */     this.life = 2;
/*  52 */     this.seed = this.random.nextLong();
/*  53 */     this.flashes = this.random.nextInt(3) + 1;
/*     */   }
/*     */   
/*     */   public void setVisualOnly(boolean paramBoolean) {
/*  57 */     this.visualOnly = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  62 */     return SoundSource.WEATHER;
/*     */   }
/*     */   
/*     */   public ServerPlayer getCause() {
/*  66 */     return this.cause;
/*     */   }
/*     */   
/*     */   public void setCause(ServerPlayer paramServerPlayer) {
/*  70 */     this.cause = paramServerPlayer;
/*     */   }
/*     */   
/*     */   private void powerLightningRod() {
/*  74 */     BlockPos blockPos = getStrikePosition();
/*  75 */     BlockState blockState = level().getBlockState(blockPos);
/*  76 */     Block block = blockState.getBlock(); if (block instanceof LightningRodBlock) { LightningRodBlock lightningRodBlock = (LightningRodBlock)block;
/*  77 */       lightningRodBlock.onLightningStrike(blockState, level(), blockPos); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  83 */     super.tick();
/*     */     
/*  85 */     if (this.life == 2) {
/*  86 */       if (level().isClientSide()) {
/*  87 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
/*  88 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
/*     */       } else {
/*  90 */         Difficulty difficulty = level().getDifficulty();
/*  91 */         if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
/*  92 */           spawnFire(4);
/*     */         }
/*     */         
/*  95 */         powerLightningRod();
/*  96 */         clearCopperOnLightningStrike(level(), getStrikePosition());
/*     */         
/*  98 */         gameEvent((Holder<GameEvent>)GameEvent.LIGHTNING_STRIKE);
/*     */       } 
/*     */     }
/*     */     
/* 102 */     this.life--;
/* 103 */     if (this.life < 0) {
/* 104 */       if (this.flashes == 0) {
/* 105 */         if (level() instanceof ServerLevel) {
/* 106 */           List list = level().getEntities(this, new AABB(getX() - 15.0D, getY() - 15.0D, getZ() - 15.0D, getX() + 15.0D, getY() + 6.0D + 15.0D, getZ() + 15.0D), paramEntity -> 
/* 107 */               (paramEntity.isAlive() && !this.hitEntities.contains(paramEntity)));
/*     */ 
/*     */           
/* 110 */           for (ServerPlayer serverPlayer : ((ServerLevel)level()).getPlayers(paramServerPlayer -> (paramServerPlayer.distanceTo(this) < 256.0F))) {
/* 111 */             CriteriaTriggers.LIGHTNING_STRIKE.trigger(serverPlayer, this, list);
/*     */           }
/*     */         } 
/*     */         
/* 115 */         discard();
/* 116 */       } else if (this.life < -this.random.nextInt(10)) {
/* 117 */         this.flashes--;
/* 118 */         this.life = 1;
/* 119 */         this.seed = this.random.nextLong();
/* 120 */         spawnFire(0);
/*     */       } 
/*     */     }
/*     */     
/* 124 */     if (this.life >= 0) {
/* 125 */       if (!(level() instanceof ServerLevel)) {
/* 126 */         level().setSkyFlashTime(2);
/* 127 */       } else if (!this.visualOnly) {
/* 128 */         List<? extends Entity> list = level().getEntities(this, new AABB(getX() - 3.0D, getY() - 3.0D, getZ() - 3.0D, getX() + 3.0D, getY() + 6.0D + 3.0D, getZ() + 3.0D), Entity::isAlive);
/* 129 */         for (Entity entity : list) {
/* 130 */           entity.thunderHit((ServerLevel)level(), this);
/*     */         }
/* 132 */         this.hitEntities.addAll(list);
/* 133 */         if (this.cause != null) {
/* 134 */           CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, list);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private BlockPos getStrikePosition() {
/* 141 */     Vec3 vec3 = position();
/* 142 */     return BlockPos.containing(vec3.x, vec3.y - 1.0E-6D, vec3.z);
/*     */   }
/*     */   private void spawnFire(int paramInt) {
/*     */     ServerLevel serverLevel;
/* 146 */     if (!this.visualOnly) { Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */       else { return; }
/*     */        }
/*     */     else { return; }
/* 150 */      BlockPos blockPos = blockPosition();
/* 151 */     if (!serverLevel.canSpreadFireAround(blockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     BlockState blockState = BaseFireBlock.getState((BlockGetter)serverLevel, blockPos);
/*     */     
/* 157 */     if (serverLevel.getBlockState(blockPos).isAir() && blockState.canSurvive((LevelReader)serverLevel, blockPos)) {
/* 158 */       serverLevel.setBlockAndUpdate(blockPos, blockState);
/* 159 */       this.blocksSetOnFire++;
/*     */     } 
/*     */     
/* 162 */     for (byte b = 0; b < paramInt; b++) {
/* 163 */       BlockPos blockPos1 = blockPos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
/* 164 */       blockState = BaseFireBlock.getState((BlockGetter)serverLevel, blockPos1);
/* 165 */       if (serverLevel.getBlockState(blockPos1).isAir() && blockState.canSurvive((LevelReader)serverLevel, blockPos1)) {
/* 166 */         serverLevel.setBlockAndUpdate(blockPos1, blockState);
/* 167 */         this.blocksSetOnFire++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void clearCopperOnLightningStrike(Level paramLevel, BlockPos paramBlockPos) {
/* 173 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */     
/* 175 */     boolean bool = (((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(blockState.getBlock()) != null) ? true : false;
/* 176 */     boolean bool1 = blockState.getBlock() instanceof WeatheringCopper;
/*     */     
/* 178 */     if (!bool1 && !bool) {
/*     */       return;
/*     */     }
/*     */     
/* 182 */     if (bool1) {
/* 183 */       paramLevel.setBlockAndUpdate(paramBlockPos, WeatheringCopper.getFirst(paramLevel.getBlockState(paramBlockPos)));
/*     */     }
/*     */     
/* 186 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 187 */     int i = paramLevel.random.nextInt(3) + 3;
/* 188 */     for (byte b = 0; b < i; b++) {
/* 189 */       int j = paramLevel.random.nextInt(8) + 1;
/* 190 */       randomWalkCleaningCopper(paramLevel, paramBlockPos, mutableBlockPos, j);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void randomWalkCleaningCopper(Level paramLevel, BlockPos paramBlockPos, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt) {
/* 195 */     paramMutableBlockPos.set((Vec3i)paramBlockPos);
/* 196 */     for (byte b = 0; b < paramInt; b++) {
/* 197 */       Optional<BlockPos> optional = randomStepCleaningCopper(paramLevel, (BlockPos)paramMutableBlockPos);
/* 198 */       if (optional.isEmpty()) {
/*     */         break;
/*     */       }
/* 201 */       paramMutableBlockPos.set((Vec3i)optional.get());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<BlockPos> randomStepCleaningCopper(Level paramLevel, BlockPos paramBlockPos) {
/* 207 */     for (Iterator<BlockPos> iterator = BlockPos.randomInCube(paramLevel.random, 10, paramBlockPos, 1).iterator(); iterator.hasNext(); ) { BlockPos blockPos = iterator.next();
/* 208 */       BlockState blockState = paramLevel.getBlockState(blockPos);
/* 209 */       if (blockState.getBlock() instanceof WeatheringCopper) {
/* 210 */         WeatheringCopper.getPrevious(blockState).ifPresent(paramBlockState -> paramLevel.setBlockAndUpdate(paramBlockPos, paramBlockState));
/* 211 */         paramLevel.levelEvent(3002, blockPos, -1);
/*     */         
/* 213 */         return Optional.of(blockPos);
/*     */       }  }
/*     */ 
/*     */     
/* 217 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/* 223 */     double d = 64.0D * getViewScale();
/* 224 */     return (paramDouble < d * d);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {}
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {}
/*     */ 
/*     */   
/*     */   public int getBlocksSetOnFire() {
/* 240 */     return this.blocksSetOnFire;
/*     */   }
/*     */   
/*     */   public Stream<Entity> getHitEntities() {
/* 244 */     return this.hitEntities.stream().filter(Entity::isAlive);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 249 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\LightningBolt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */