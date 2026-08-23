/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class ExperienceOrb
/*     */   extends Entity
/*     */ {
/*  33 */   protected static final EntityDataAccessor<Integer> DATA_VALUE = SynchedEntityData.defineId(ExperienceOrb.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int LIFETIME = 6000;
/*     */   private static final int ENTITY_SCAN_PERIOD = 20;
/*     */   private static final int MAX_FOLLOW_DIST = 8;
/*     */   private static final int ORB_GROUPS_PER_AREA = 40;
/*     */   private static final double ORB_MERGE_DISTANCE = 0.5D;
/*     */   private static final short DEFAULT_HEALTH = 5;
/*     */   private static final short DEFAULT_AGE = 0;
/*     */   private static final short DEFAULT_VALUE = 0;
/*     */   private static final int DEFAULT_COUNT = 1;
/*  44 */   private int age = 0;
/*  45 */   private int health = 5;
/*  46 */   private int count = 1;
/*     */   private Player followingPlayer;
/*  48 */   private final InterpolationHandler interpolation = new InterpolationHandler(this);
/*     */   
/*     */   public ExperienceOrb(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) {
/*  51 */     this(paramLevel, new Vec3(paramDouble1, paramDouble2, paramDouble3), Vec3.ZERO, paramInt);
/*     */   }
/*     */   
/*     */   public ExperienceOrb(Level paramLevel, Vec3 paramVec31, Vec3 paramVec32, int paramInt) {
/*  55 */     this(EntityType.EXPERIENCE_ORB, paramLevel);
/*  56 */     setPos(paramVec31);
/*  57 */     if (!paramLevel.isClientSide()) {
/*  58 */       setYRot(this.random.nextFloat() * 360.0F);
/*     */ 
/*     */ 
/*     */       
/*  62 */       Vec3 vec3 = new Vec3((this.random.nextDouble() * 0.2D - 0.1D) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * 0.2D - 0.1D) * 2.0D);
/*     */       
/*  64 */       if (paramVec32.lengthSqr() > 0.0D && paramVec32.dot(vec3) < 0.0D) {
/*  65 */         vec3 = vec3.scale(-1.0D);
/*     */       }
/*     */       
/*  68 */       double d = getBoundingBox().getSize();
/*  69 */       setPos(paramVec31.add(paramVec32.normalize().scale(d * 0.5D)));
/*  70 */       setDeltaMovement(vec3);
/*     */       
/*  72 */       if (!paramLevel.noCollision(getBoundingBox())) {
/*  73 */         unstuckIfPossible(d);
/*     */       }
/*     */     } 
/*  76 */     setValue(paramInt);
/*     */   }
/*     */   
/*     */   public ExperienceOrb(EntityType<? extends ExperienceOrb> paramEntityType, Level paramLevel) {
/*  80 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   protected void unstuckIfPossible(double paramDouble) {
/*  84 */     Vec3 vec3 = position().add(0.0D, getBbHeight() / 2.0D, 0.0D);
/*  85 */     VoxelShape voxelShape = Shapes.create(AABB.ofSize(vec3, paramDouble, paramDouble, paramDouble));
/*  86 */     level().findFreePosition(this, voxelShape, vec3, getBbWidth(), getBbHeight(), getBbWidth())
/*  87 */       .ifPresent(paramVec3 -> setPos(paramVec3.add(0.0D, -getBbHeight() / 2.0D, 0.0D)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  92 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  97 */     paramBuilder.define(DATA_VALUE, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 102 */     return 0.03D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 107 */     this.interpolation.interpolate();
/* 108 */     if (this.firstTick && level().isClientSide()) {
/* 109 */       this.firstTick = false;
/*     */       
/*     */       return;
/*     */     } 
/* 113 */     super.tick();
/*     */     
/* 115 */     boolean bool = !level().noCollision(getBoundingBox()) ? true : false;
/*     */     
/* 117 */     if (isEyeInFluid(FluidTags.WATER)) {
/* 118 */       setUnderwaterMovement();
/* 119 */     } else if (!bool) {
/* 120 */       applyGravity();
/*     */     } 
/*     */     
/* 123 */     if (level().getFluidState(blockPosition()).is(FluidTags.LAVA)) {
/* 124 */       setDeltaMovement(((this.random
/* 125 */           .nextFloat() - this.random.nextFloat()) * 0.2F), 0.20000000298023224D, ((this.random
/*     */           
/* 127 */           .nextFloat() - this.random.nextFloat()) * 0.2F));
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (this.tickCount % 20 == 1) {
/* 132 */       scanForMerges();
/*     */     }
/*     */     
/* 135 */     followNearbyPlayer();
/*     */     
/* 137 */     if (this.followingPlayer == null && !level().isClientSide() && bool) {
/* 138 */       boolean bool1 = !level().noCollision(getBoundingBox().move(getDeltaMovement())) ? true : false;
/* 139 */       if (bool1) {
/* 140 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/* 141 */         this.needsSync = true;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     double d = (getDeltaMovement()).y;
/* 146 */     move(MoverType.SELF, getDeltaMovement());
/* 147 */     applyEffectsFromBlocks();
/*     */     
/* 149 */     float f = 0.98F;
/* 150 */     if (onGround()) {
/* 151 */       f = level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
/*     */     }
/*     */     
/* 154 */     setDeltaMovement(getDeltaMovement().scale(f));
/* 155 */     if (this.verticalCollisionBelow && d < -getGravity()) {
/* 156 */       setDeltaMovement(new Vec3((getDeltaMovement()).x, -d * 0.4D, (getDeltaMovement()).z));
/*     */     }
/*     */     
/* 159 */     this.age++;
/* 160 */     if (this.age >= 6000) {
/* 161 */       discard();
/*     */     }
/*     */   }
/*     */   
/*     */   private void followNearbyPlayer() {
/* 166 */     if (this.followingPlayer == null || this.followingPlayer
/* 167 */       .isSpectator() || this.followingPlayer
/* 168 */       .distanceToSqr(this) > 64.0D) {
/* 169 */       Player player = level().getNearestPlayer(this, 8.0D);
/* 170 */       if (player != null && !player.isSpectator() && !player.isDeadOrDying()) {
/* 171 */         this.followingPlayer = player;
/*     */       } else {
/* 173 */         this.followingPlayer = null;
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     if (this.followingPlayer != null) {
/*     */ 
/*     */ 
/*     */       
/* 181 */       Vec3 vec3 = new Vec3(this.followingPlayer.getX() - getX(), this.followingPlayer.getY() + this.followingPlayer.getEyeHeight() / 2.0D - getY(), this.followingPlayer.getZ() - getZ());
/*     */       
/* 183 */       double d1 = vec3.lengthSqr();
/* 184 */       double d2 = 1.0D - Math.sqrt(d1) / 8.0D;
/* 185 */       setDeltaMovement(getDeltaMovement().add(vec3.normalize().scale(d2 * d2 * 0.1D)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getBlockPosBelowThatAffectsMyMovement() {
/* 192 */     return getOnPos(0.999999F);
/*     */   }
/*     */   
/*     */   private void scanForMerges() {
/* 196 */     if (level() instanceof ServerLevel) {
/* 197 */       List list = level().getEntities(EntityTypeTest.forClass(ExperienceOrb.class), getBoundingBox().inflate(0.5D), this::canMerge);
/* 198 */       for (ExperienceOrb experienceOrb : list) {
/* 199 */         merge(experienceOrb);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void award(ServerLevel paramServerLevel, Vec3 paramVec3, int paramInt) {
/* 205 */     awardWithDirection(paramServerLevel, paramVec3, Vec3.ZERO, paramInt);
/*     */   }
/*     */   
/*     */   public static void awardWithDirection(ServerLevel paramServerLevel, Vec3 paramVec31, Vec3 paramVec32, int paramInt) {
/* 209 */     while (paramInt > 0) {
/* 210 */       int i = getExperienceValue(paramInt);
/* 211 */       paramInt -= i;
/* 212 */       if (!tryMergeToExisting(paramServerLevel, paramVec31, i)) {
/* 213 */         paramServerLevel.addFreshEntity(new ExperienceOrb((Level)paramServerLevel, paramVec31, paramVec32, i));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean tryMergeToExisting(ServerLevel paramServerLevel, Vec3 paramVec3, int paramInt) {
/* 219 */     AABB aABB = AABB.ofSize(paramVec3, 1.0D, 1.0D, 1.0D);
/* 220 */     int i = paramServerLevel.getRandom().nextInt(40);
/* 221 */     List<ExperienceOrb> list = paramServerLevel.getEntities(EntityTypeTest.forClass(ExperienceOrb.class), aABB, paramExperienceOrb -> canMerge(paramExperienceOrb, paramInt1, paramInt2));
/* 222 */     if (!list.isEmpty()) {
/* 223 */       ExperienceOrb experienceOrb = list.get(0);
/* 224 */       experienceOrb.count++;
/* 225 */       experienceOrb.age = 0;
/* 226 */       return true;
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canMerge(ExperienceOrb paramExperienceOrb) {
/* 232 */     return (paramExperienceOrb != this && 
/* 233 */       canMerge(paramExperienceOrb, getId(), getValue()));
/*     */   }
/*     */   
/*     */   private static boolean canMerge(ExperienceOrb paramExperienceOrb, int paramInt1, int paramInt2) {
/* 237 */     return (!paramExperienceOrb.isRemoved() && (paramExperienceOrb
/* 238 */       .getId() - paramInt1) % 40 == 0 && paramExperienceOrb
/* 239 */       .getValue() == paramInt2);
/*     */   }
/*     */   
/*     */   private void merge(ExperienceOrb paramExperienceOrb) {
/* 243 */     this.count += paramExperienceOrb.count;
/* 244 */     this.age = Math.min(this.age, paramExperienceOrb.age);
/* 245 */     paramExperienceOrb.discard();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 250 */     Vec3 vec3 = getDeltaMovement();
/*     */     
/* 252 */     setDeltaMovement(vec3.x * 0.9900000095367432D, 
/*     */         
/* 254 */         Math.min(vec3.y + 5.000000237487257E-4D, 0.05999999865889549D), vec3.z * 0.9900000095367432D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doWaterSplashEffect() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hurtClient(DamageSource paramDamageSource) {
/* 265 */     return !isInvulnerableToBase(paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 270 */     if (isInvulnerableToBase(paramDamageSource)) {
/* 271 */       return false;
/*     */     }
/* 273 */     markHurt();
/* 274 */     this.health = (int)(this.health - paramFloat);
/* 275 */     if (this.health <= 0) {
/* 276 */       discard();
/*     */     }
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 283 */     paramValueOutput.putShort("Health", (short)this.health);
/* 284 */     paramValueOutput.putShort("Age", (short)this.age);
/* 285 */     paramValueOutput.putShort("Value", (short)getValue());
/* 286 */     paramValueOutput.putInt("Count", this.count);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 291 */     this.health = paramValueInput.getShortOr("Health", (short)5);
/* 292 */     this.age = paramValueInput.getShortOr("Age", (short)0);
/* 293 */     setValue(paramValueInput.getShortOr("Value", (short)0));
/* 294 */     this.count = ((Integer)paramValueInput.read("Count", ExtraCodecs.POSITIVE_INT).orElse(Integer.valueOf(1))).intValue();
/*     */   }
/*     */   
/*     */   public void playerTouch(Player paramPlayer) {
/*     */     ServerPlayer serverPlayer;
/* 299 */     if (paramPlayer instanceof ServerPlayer) { serverPlayer = (ServerPlayer)paramPlayer; }
/*     */     else
/*     */     { return; }
/*     */     
/* 303 */     if (paramPlayer.takeXpDelay == 0) {
/* 304 */       paramPlayer.takeXpDelay = 2;
/* 305 */       paramPlayer.take(this, 1);
/* 306 */       int i = repairPlayerItems(serverPlayer, getValue());
/* 307 */       if (i > 0) {
/* 308 */         paramPlayer.giveExperiencePoints(i);
/*     */       }
/* 310 */       this.count--;
/* 311 */       if (this.count == 0) {
/* 312 */         discard();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int repairPlayerItems(ServerPlayer paramServerPlayer, int paramInt) {
/* 318 */     Optional<EnchantedItemInUse> optional = EnchantmentHelper.getRandomItemWith(EnchantmentEffectComponents.REPAIR_WITH_XP, (LivingEntity)paramServerPlayer, ItemStack::isDamaged);
/* 319 */     if (optional.isPresent()) {
/* 320 */       ItemStack itemStack = ((EnchantedItemInUse)optional.get()).itemStack();
/* 321 */       int i = EnchantmentHelper.modifyDurabilityToRepairFromXp(paramServerPlayer.level(), itemStack, paramInt);
/* 322 */       int j = Math.min(i, itemStack.getDamageValue());
/* 323 */       itemStack.setDamageValue(itemStack.getDamageValue() - j);
/*     */       
/* 325 */       if (j > 0) {
/*     */         
/* 327 */         int k = paramInt - j * paramInt / i;
/* 328 */         if (k > 0) {
/* 329 */           return repairPlayerItems(paramServerPlayer, k);
/*     */         }
/*     */       } 
/* 332 */       return 0;
/*     */     } 
/* 334 */     return paramInt;
/*     */   }
/*     */   
/*     */   public int getValue() {
/* 338 */     return ((Integer)this.entityData.get(DATA_VALUE)).intValue();
/*     */   }
/*     */   
/*     */   private void setValue(int paramInt) {
/* 342 */     this.entityData.set(DATA_VALUE, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public int getIcon() {
/* 346 */     int i = getValue();
/* 347 */     if (i >= 2477)
/* 348 */       return 10; 
/* 349 */     if (i >= 1237)
/* 350 */       return 9; 
/* 351 */     if (i >= 617)
/* 352 */       return 8; 
/* 353 */     if (i >= 307)
/* 354 */       return 7; 
/* 355 */     if (i >= 149)
/* 356 */       return 6; 
/* 357 */     if (i >= 73)
/* 358 */       return 5; 
/* 359 */     if (i >= 37)
/* 360 */       return 4; 
/* 361 */     if (i >= 17)
/* 362 */       return 3; 
/* 363 */     if (i >= 7)
/* 364 */       return 2; 
/* 365 */     if (i >= 3) {
/* 366 */       return 1;
/*     */     }
/*     */     
/* 369 */     return 0;
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
/*     */   public static int getExperienceValue(int paramInt) {
/* 381 */     if (paramInt >= 2477)
/* 382 */       return 2477; 
/* 383 */     if (paramInt >= 1237)
/* 384 */       return 1237; 
/* 385 */     if (paramInt >= 617)
/* 386 */       return 617; 
/* 387 */     if (paramInt >= 307)
/* 388 */       return 307; 
/* 389 */     if (paramInt >= 149)
/* 390 */       return 149; 
/* 391 */     if (paramInt >= 73)
/* 392 */       return 73; 
/* 393 */     if (paramInt >= 37)
/* 394 */       return 37; 
/* 395 */     if (paramInt >= 17)
/* 396 */       return 17; 
/* 397 */     if (paramInt >= 7)
/* 398 */       return 7; 
/* 399 */     if (paramInt >= 3) {
/* 400 */       return 3;
/*     */     }
/*     */     
/* 403 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 408 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 413 */     return SoundSource.AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 418 */     return this.interpolation;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ExperienceOrb.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */