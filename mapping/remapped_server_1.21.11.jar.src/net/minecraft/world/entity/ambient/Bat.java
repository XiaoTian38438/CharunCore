/*     */ package net.minecraft.world.entity.ambient;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Bat
/*     */   extends AmbientCreature {
/*     */   public static final float FLAP_LENGTH_SECONDS = 0.5F;
/*     */   public static final float TICKS_PER_FLAP = 10.0F;
/*  37 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Bat.class, EntityDataSerializers.BYTE);
/*     */   private static final int FLAG_RESTING = 1;
/*  39 */   private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0D);
/*     */   
/*     */   private static final byte DEFAULT_FLAGS = 0;
/*     */   
/*  43 */   public final AnimationState flyAnimationState = new AnimationState();
/*  44 */   public final AnimationState restAnimationState = new AnimationState();
/*     */   
/*     */   private BlockPos targetPosition;
/*     */   
/*     */   public Bat(EntityType<? extends Bat> paramEntityType, Level paramLevel) {
/*  49 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  51 */     if (!paramLevel.isClientSide()) {
/*  52 */       setResting(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFlapping() {
/*  59 */     return (!isResting() && this.tickCount % 10.0F == 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  64 */     super.defineSynchedData(paramBuilder);
/*  65 */     paramBuilder.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  70 */     return 0.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVoicePitch() {
/*  75 */     return super.getVoicePitch() * 0.95F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getAmbientSound() {
/*  80 */     if (isResting() && this.random.nextInt(4) != 0) {
/*  81 */       return null;
/*     */     }
/*  83 */     return SoundEvents.BAT_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  88 */     return SoundEvents.BAT_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  93 */     return SoundEvents.BAT_DEATH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity paramEntity) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushEntities() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 113 */     return Mob.createMobAttributes()
/* 114 */       .add(Attributes.MAX_HEALTH, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean isResting() {
/* 118 */     return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setResting(boolean paramBoolean) {
/* 122 */     byte b = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/* 123 */     if (paramBoolean) {
/* 124 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(b | 0x1)));
/*     */     } else {
/* 126 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(b & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 132 */     super.tick();
/* 133 */     if (isResting()) {
/* 134 */       setDeltaMovement(Vec3.ZERO);
/* 135 */       setPosRaw(getX(), Mth.floor(getY()) + 1.0D - getBbHeight(), getZ());
/*     */     } else {
/* 137 */       setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
/*     */     } 
/* 139 */     setupAnimationStates();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 144 */     super.customServerAiStep(paramServerLevel);
/*     */     
/* 146 */     BlockPos blockPos1 = blockPosition();
/* 147 */     BlockPos blockPos2 = blockPos1.above();
/*     */     
/* 149 */     if (isResting()) {
/* 150 */       boolean bool = isSilent();
/* 151 */       if (paramServerLevel.getBlockState(blockPos2).isRedstoneConductor((BlockGetter)paramServerLevel, blockPos1)) {
/* 152 */         if (this.random.nextInt(200) == 0) {
/* 153 */           this.yHeadRot = this.random.nextInt(360);
/*     */         }
/*     */         
/* 156 */         if (paramServerLevel.getNearestPlayer(BAT_RESTING_TARGETING, (LivingEntity)this) != null) {
/* 157 */           setResting(false);
/* 158 */           if (!bool) {
/* 159 */             paramServerLevel.levelEvent(null, 1025, blockPos1, 0);
/*     */           }
/*     */         } 
/*     */       } else {
/* 163 */         setResting(false);
/* 164 */         if (!bool) {
/* 165 */           paramServerLevel.levelEvent(null, 1025, blockPos1, 0);
/*     */         }
/*     */       } 
/*     */     } else {
/* 169 */       if (this.targetPosition != null && (!paramServerLevel.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= paramServerLevel.getMinY())) {
/* 170 */         this.targetPosition = null;
/*     */       }
/* 172 */       if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan((Position)position(), 2.0D)) {
/* 173 */         this.targetPosition = BlockPos.containing(getX() + this.random.nextInt(7) - this.random.nextInt(7), getY() + this.random.nextInt(6) - 2.0D, getZ() + this.random.nextInt(7) - this.random.nextInt(7));
/*     */       }
/*     */ 
/*     */       
/* 177 */       double d1 = this.targetPosition.getX() + 0.5D - getX();
/* 178 */       double d2 = this.targetPosition.getY() + 0.1D - getY();
/* 179 */       double d3 = this.targetPosition.getZ() + 0.5D - getZ();
/*     */       
/* 181 */       Vec3 vec31 = getDeltaMovement();
/* 182 */       Vec3 vec32 = vec31.add((
/* 183 */           Math.signum(d1) * 0.5D - vec31.x) * 0.10000000149011612D, (
/* 184 */           Math.signum(d2) * 0.699999988079071D - vec31.y) * 0.10000000149011612D, (
/* 185 */           Math.signum(d3) * 0.5D - vec31.z) * 0.10000000149011612D);
/*     */       
/* 187 */       setDeltaMovement(vec32);
/*     */       
/* 189 */       float f1 = (float)(Mth.atan2(vec32.z, vec32.x) * 57.2957763671875D) - 90.0F;
/* 190 */       float f2 = Mth.wrapDegrees(f1 - getYRot());
/* 191 */       this.zza = 0.5F;
/* 192 */       setYRot(getYRot() + f2);
/*     */       
/* 194 */       if (this.random.nextInt(100) == 0 && paramServerLevel.getBlockState(blockPos2).isRedstoneConductor((BlockGetter)paramServerLevel, blockPos2)) {
/* 195 */         setResting(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 202 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double paramDouble, boolean paramBoolean, BlockState paramBlockState, BlockPos paramBlockPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 218 */     if (isInvulnerableTo(paramServerLevel, paramDamageSource)) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     if (isResting()) {
/* 223 */       setResting(false);
/*     */     }
/* 225 */     return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 230 */     super.readAdditionalSaveData(paramValueInput);
/* 231 */     this.entityData.set(DATA_ID_FLAGS, Byte.valueOf(paramValueInput.getByteOr("BatFlags", (byte)0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 236 */     super.addAdditionalSaveData(paramValueOutput);
/* 237 */     paramValueOutput.putByte("BatFlags", ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue());
/*     */   }
/*     */   
/*     */   public static boolean checkBatSpawnRules(EntityType<Bat> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 241 */     if (paramBlockPos.getY() >= paramLevelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, paramBlockPos).getY()) {
/* 242 */       return false;
/*     */     }
/*     */     
/* 245 */     if (paramRandomSource.nextBoolean()) {
/* 246 */       return false;
/*     */     }
/*     */     
/* 249 */     if (paramLevelAccessor.getMaxLocalRawBrightness(paramBlockPos) > paramRandomSource.nextInt(4)) {
/* 250 */       return false;
/*     */     }
/*     */     
/* 253 */     if (!paramLevelAccessor.getBlockState(paramBlockPos.below()).is(BlockTags.BATS_SPAWNABLE_ON)) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     return checkMobSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource);
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 261 */     if (isResting()) {
/* 262 */       this.flyAnimationState.stop();
/* 263 */       this.restAnimationState.startIfStopped(this.tickCount);
/*     */     } else {
/* 265 */       this.restAnimationState.stop();
/* 266 */       this.flyAnimationState.startIfStopped(this.tickCount);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ambient\Bat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */