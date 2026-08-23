/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ConduitBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final int BLOCK_REFRESH_RATE = 2;
/*     */   private static final int EFFECT_DURATION = 13;
/*     */   private static final float ROTATION_SPEED = -0.0375F;
/*     */   private static final int MIN_ACTIVE_SIZE = 16;
/*     */   private static final int MIN_KILL_SIZE = 42;
/*     */   private static final int KILL_RANGE = 8;
/*  46 */   private static final Block[] VALID_BLOCKS = new Block[] { Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE };
/*     */   
/*     */   public int tickCount;
/*     */   
/*     */   private float activeRotation;
/*     */   private boolean isActive;
/*     */   private boolean isHunting;
/*  53 */   private final List<BlockPos> effectBlocks = Lists.newArrayList();
/*     */   
/*     */   private EntityReference<LivingEntity> destroyTarget;
/*     */   private long nextAmbientSoundActivation;
/*     */   
/*     */   public ConduitBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  59 */     super(BlockEntityType.CONDUIT, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  64 */     super.loadAdditional(paramValueInput);
/*  65 */     this.destroyTarget = EntityReference.read(paramValueInput, "Target");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  70 */     super.saveAdditional(paramValueOutput);
/*  71 */     EntityReference.store(this.destroyTarget, paramValueOutput, "Target");
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  76 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider paramProvider) {
/*  81 */     return saveCustomOnly(paramProvider);
/*     */   }
/*     */   
/*     */   public static void clientTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, ConduitBlockEntity paramConduitBlockEntity) {
/*  85 */     paramConduitBlockEntity.tickCount++;
/*     */     
/*  87 */     long l = paramLevel.getGameTime();
/*     */     
/*  89 */     List<BlockPos> list = paramConduitBlockEntity.effectBlocks;
/*  90 */     if (l % 40L == 0L) {
/*  91 */       paramConduitBlockEntity.isActive = updateShape(paramLevel, paramBlockPos, list);
/*  92 */       updateHunting(paramConduitBlockEntity, list);
/*     */     } 
/*     */     
/*  95 */     LivingEntity livingEntity = EntityReference.getLivingEntity(paramConduitBlockEntity.destroyTarget, paramLevel);
/*  96 */     animationTick(paramLevel, paramBlockPos, list, (Entity)livingEntity, paramConduitBlockEntity.tickCount);
/*  97 */     if (paramConduitBlockEntity.isActive()) {
/*  98 */       paramConduitBlockEntity.activeRotation++;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void serverTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, ConduitBlockEntity paramConduitBlockEntity) {
/* 103 */     paramConduitBlockEntity.tickCount++;
/*     */     
/* 105 */     long l = paramLevel.getGameTime();
/*     */     
/* 107 */     List<BlockPos> list = paramConduitBlockEntity.effectBlocks;
/* 108 */     if (l % 40L == 0L) {
/* 109 */       boolean bool = updateShape(paramLevel, paramBlockPos, list);
/* 110 */       if (bool != paramConduitBlockEntity.isActive) {
/* 111 */         SoundEvent soundEvent = bool ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE;
/* 112 */         paramLevel.playSound(null, paramBlockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/* 114 */       paramConduitBlockEntity.isActive = bool;
/* 115 */       updateHunting(paramConduitBlockEntity, list);
/*     */       
/* 117 */       if (bool) {
/* 118 */         applyEffects(paramLevel, paramBlockPos, list);
/* 119 */         updateAndAttackTarget((ServerLevel)paramLevel, paramBlockPos, paramBlockState, paramConduitBlockEntity, (list.size() >= 42));
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     if (paramConduitBlockEntity.isActive()) {
/* 124 */       if (l % 80L == 0L) {
/* 125 */         paramLevel.playSound(null, paramBlockPos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 128 */       if (l > paramConduitBlockEntity.nextAmbientSoundActivation) {
/* 129 */         paramConduitBlockEntity.nextAmbientSoundActivation = l + 60L + paramLevel.getRandom().nextInt(40);
/* 130 */         paramLevel.playSound(null, paramBlockPos, SoundEvents.CONDUIT_AMBIENT_SHORT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void updateHunting(ConduitBlockEntity paramConduitBlockEntity, List<BlockPos> paramList) {
/* 136 */     paramConduitBlockEntity.setHunting((paramList.size() >= 42));
/*     */   }
/*     */   
/*     */   private static boolean updateShape(Level paramLevel, BlockPos paramBlockPos, List<BlockPos> paramList) {
/* 140 */     paramList.clear();
/*     */     
/*     */     byte b;
/* 143 */     for (b = -1; b <= 1; b++) {
/* 144 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 145 */         for (byte b2 = -1; b2 <= 1; b2++) {
/* 146 */           BlockPos blockPos = paramBlockPos.offset(b, b1, b2);
/* 147 */           if (!paramLevel.isWaterAt(blockPos)) {
/* 148 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 155 */     for (b = -2; b <= 2; b++) {
/* 156 */       for (byte b1 = -2; b1 <= 2; b1++) {
/* 157 */         for (byte b2 = -2; b2 <= 2; b2++) {
/* 158 */           int i = Math.abs(b);
/* 159 */           int j = Math.abs(b1);
/* 160 */           int k = Math.abs(b2);
/* 161 */           if (i > 1 || j > 1 || k > 1)
/*     */           {
/*     */             
/* 164 */             if ((b == 0 && (j == 2 || k == 2)) || (b1 == 0 && (i == 2 || k == 2)) || (b2 == 0 && (i == 2 || j == 2))) {
/* 165 */               BlockPos blockPos = paramBlockPos.offset(b, b1, b2);
/* 166 */               BlockState blockState = paramLevel.getBlockState(blockPos);
/* 167 */               for (Block block : VALID_BLOCKS) {
/* 168 */                 if (blockState.is(block)) {
/* 169 */                   paramList.add(blockPos);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 177 */     return (paramList.size() >= 16);
/*     */   }
/*     */   
/*     */   private static void applyEffects(Level paramLevel, BlockPos paramBlockPos, List<BlockPos> paramList) {
/* 181 */     int i = paramList.size();
/* 182 */     int j = i / 7 * 16;
/*     */ 
/*     */     
/* 185 */     int k = paramBlockPos.getX();
/* 186 */     int m = paramBlockPos.getY();
/* 187 */     int n = paramBlockPos.getZ();
/* 188 */     AABB aABB = (new AABB(k, m, n, (k + 1), (m + 1), (n + 1))).inflate(j).expandTowards(0.0D, paramLevel.getHeight(), 0.0D);
/* 189 */     List list = paramLevel.getEntitiesOfClass(Player.class, aABB);
/*     */     
/* 191 */     if (list.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 195 */     for (Player player : list) {
/* 196 */       if (paramBlockPos.closerThan((Vec3i)player.blockPosition(), j) && player.isInWaterOrRain()) {
/* 197 */         player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void updateAndAttackTarget(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, ConduitBlockEntity paramConduitBlockEntity, boolean paramBoolean) {
/* 203 */     EntityReference<LivingEntity> entityReference = updateDestroyTarget(paramConduitBlockEntity.destroyTarget, paramServerLevel, paramBlockPos, paramBoolean);
/*     */     
/* 205 */     LivingEntity livingEntity = EntityReference.getLivingEntity(entityReference, (Level)paramServerLevel);
/* 206 */     if (livingEntity != null) {
/* 207 */       paramServerLevel.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 208 */       livingEntity.hurtServer(paramServerLevel, paramServerLevel.damageSources().magic(), 4.0F);
/*     */     } 
/*     */     
/* 211 */     if (!Objects.equals(entityReference, paramConduitBlockEntity.destroyTarget)) {
/* 212 */       paramConduitBlockEntity.destroyTarget = entityReference;
/* 213 */       paramServerLevel.sendBlockUpdated(paramBlockPos, paramBlockState, paramBlockState, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static EntityReference<LivingEntity> updateDestroyTarget(EntityReference<LivingEntity> paramEntityReference, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 218 */     if (!paramBoolean) {
/* 219 */       return null;
/*     */     }
/* 221 */     if (paramEntityReference == null) {
/* 222 */       return selectNewTarget(paramServerLevel, paramBlockPos);
/*     */     }
/* 224 */     LivingEntity livingEntity = EntityReference.getLivingEntity(paramEntityReference, (Level)paramServerLevel);
/* 225 */     if (livingEntity == null || !livingEntity.isAlive() || !paramBlockPos.closerThan((Vec3i)livingEntity.blockPosition(), 8.0D))
/*     */     {
/* 227 */       return null;
/*     */     }
/* 229 */     return paramEntityReference;
/*     */   }
/*     */   
/*     */   private static EntityReference<LivingEntity> selectNewTarget(ServerLevel paramServerLevel, BlockPos paramBlockPos) {
/* 233 */     List list = paramServerLevel.getEntitiesOfClass(LivingEntity.class, getDestroyRangeAABB(paramBlockPos), paramLivingEntity -> (paramLivingEntity instanceof net.minecraft.world.entity.monster.Enemy && paramLivingEntity.isInWaterOrRain()));
/* 234 */     if (list.isEmpty()) {
/* 235 */       return null;
/*     */     }
/* 237 */     return EntityReference.of((UniquelyIdentifyable)Util.getRandom(list, paramServerLevel.random));
/*     */   }
/*     */   
/*     */   private static AABB getDestroyRangeAABB(BlockPos paramBlockPos) {
/* 241 */     return (new AABB(paramBlockPos)).inflate(8.0D);
/*     */   }
/*     */   
/*     */   private static void animationTick(Level paramLevel, BlockPos paramBlockPos, List<BlockPos> paramList, Entity paramEntity, int paramInt) {
/* 245 */     RandomSource randomSource = paramLevel.random;
/*     */     
/* 247 */     double d = (Mth.sin(((paramInt + 35) * 0.1F)) / 2.0F + 0.5F);
/* 248 */     d = (d * d + d) * 0.30000001192092896D;
/*     */     
/* 250 */     Vec3 vec3 = new Vec3(paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 1.5D + d, paramBlockPos.getZ() + 0.5D);
/* 251 */     for (BlockPos blockPos1 : paramList) {
/* 252 */       if (randomSource.nextInt(50) != 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 256 */       BlockPos blockPos2 = blockPos1.subtract((Vec3i)paramBlockPos);
/* 257 */       float f1 = -0.5F + randomSource.nextFloat() + blockPos2.getX();
/* 258 */       float f2 = -2.0F + randomSource.nextFloat() + blockPos2.getY();
/* 259 */       float f3 = -0.5F + randomSource.nextFloat() + blockPos2.getZ();
/* 260 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.NAUTILUS, vec3.x, vec3.y, vec3.z, f1, f2, f3);
/*     */     } 
/*     */     
/* 263 */     if (paramEntity != null) {
/* 264 */       Vec3 vec31 = new Vec3(paramEntity.getX(), paramEntity.getEyeY(), paramEntity.getZ());
/* 265 */       float f1 = (-0.5F + randomSource.nextFloat()) * (3.0F + paramEntity.getBbWidth());
/* 266 */       float f2 = -1.0F + randomSource.nextFloat() * paramEntity.getBbHeight();
/* 267 */       float f3 = (-0.5F + randomSource.nextFloat()) * (3.0F + paramEntity.getBbWidth());
/* 268 */       Vec3 vec32 = new Vec3(f1, f2, f3);
/* 269 */       paramLevel.addParticle((ParticleOptions)ParticleTypes.NAUTILUS, vec31.x, vec31.y, vec31.z, vec32.x, vec32.y, vec32.z);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 274 */     return this.isActive;
/*     */   }
/*     */   
/*     */   public boolean isHunting() {
/* 278 */     return this.isHunting;
/*     */   }
/*     */   
/*     */   private void setHunting(boolean paramBoolean) {
/* 282 */     this.isHunting = paramBoolean;
/*     */   }
/*     */   
/*     */   public float getActiveRotation(float paramFloat) {
/* 286 */     return (this.activeRotation + paramFloat) * -0.0375F;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\ConduitBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */