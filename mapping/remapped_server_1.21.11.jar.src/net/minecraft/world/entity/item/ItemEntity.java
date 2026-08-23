/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ItemEntity
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*  43 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private static final float FLOAT_HEIGHT = 0.1F;
/*     */   
/*     */   public static final float EYE_HEIGHT = 0.2125F;
/*     */   
/*     */   private static final int LIFETIME = 6000;
/*     */   
/*     */   private static final int INFINITE_PICKUP_DELAY = 32767;
/*     */   
/*     */   private static final int INFINITE_LIFETIME = -32768;
/*     */   
/*     */   private static final int DEFAULT_HEALTH = 5;
/*     */   private static final short DEFAULT_AGE = 0;
/*     */   private static final short DEFAULT_PICKUP_DELAY = 0;
/*  58 */   private int age = 0;
/*  59 */   private int pickupDelay = 0;
/*  60 */   private int health = 5;
/*     */   private EntityReference<Entity> thrower;
/*     */   private UUID target;
/*     */   public final float bobOffs;
/*     */   
/*     */   public ItemEntity(EntityType<? extends ItemEntity> paramEntityType, Level paramLevel) {
/*  66 */     super(paramEntityType, paramLevel);
/*  67 */     this.bobOffs = this.random.nextFloat() * 3.1415927F * 2.0F;
/*  68 */     setYRot(this.random.nextFloat() * 360.0F);
/*     */   }
/*     */   
/*     */   public ItemEntity(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/*  72 */     this(paramLevel, paramDouble1, paramDouble2, paramDouble3, paramItemStack, paramLevel.random.nextDouble() * 0.2D - 0.1D, 0.2D, paramLevel.random.nextDouble() * 0.2D - 0.1D);
/*     */   }
/*     */   
/*     */   public ItemEntity(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack, double paramDouble4, double paramDouble5, double paramDouble6) {
/*  76 */     this(EntityType.ITEM, paramLevel);
/*  77 */     setPos(paramDouble1, paramDouble2, paramDouble3);
/*  78 */     setDeltaMovement(paramDouble4, paramDouble5, paramDouble6);
/*  79 */     setItem(paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean dampensVibrations() {
/*  84 */     return getItem().is(ItemTags.DAMPENS_VIBRATIONS);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getOwner() {
/*  89 */     return EntityReference.getEntity(this.thrower, level());
/*     */   }
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity paramEntity) {
/*  94 */     super.restoreFrom(paramEntity);
/*  95 */     if (paramEntity instanceof ItemEntity) { ItemEntity itemEntity = (ItemEntity)paramEntity;
/*  96 */       this.thrower = itemEntity.thrower; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 102 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 107 */     paramBuilder.define(DATA_ITEM, ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 112 */     return 0.04D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 117 */     if (getItem().isEmpty()) {
/* 118 */       discard();
/*     */       return;
/*     */     } 
/* 121 */     super.tick();
/* 122 */     if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
/* 123 */       this.pickupDelay--;
/*     */     }
/* 125 */     this.xo = getX();
/* 126 */     this.yo = getY();
/* 127 */     this.zo = getZ();
/*     */     
/* 129 */     Vec3 vec3 = getDeltaMovement();
/*     */ 
/*     */     
/* 132 */     if (isInWater() && getFluidHeight(FluidTags.WATER) > 0.10000000149011612D) {
/* 133 */       setUnderwaterMovement();
/* 134 */     } else if (isInLava() && getFluidHeight(FluidTags.LAVA) > 0.10000000149011612D) {
/* 135 */       setUnderLavaMovement();
/*     */     } else {
/* 137 */       applyGravity();
/*     */     } 
/*     */     
/* 140 */     if (level().isClientSide()) {
/* 141 */       this.noPhysics = false;
/*     */     } else {
/* 143 */       this.noPhysics = !level().noCollision(this, getBoundingBox().deflate(1.0E-7D));
/* 144 */       if (this.noPhysics) {
/* 145 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/*     */       }
/*     */     } 
/* 148 */     if (!onGround() || getDeltaMovement().horizontalDistanceSqr() > 9.999999747378752E-6D || (this.tickCount + getId()) % 4 == 0) {
/* 149 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 151 */       applyEffectsFromBlocks();
/*     */       
/* 153 */       float f = 0.98F;
/* 154 */       if (onGround()) {
/* 155 */         f = level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
/*     */       }
/*     */       
/* 158 */       setDeltaMovement(getDeltaMovement().multiply(f, 0.98D, f));
/*     */ 
/*     */       
/* 161 */       if (onGround()) {
/* 162 */         Vec3 vec31 = getDeltaMovement();
/* 163 */         if (vec31.y < 0.0D) {
/* 164 */           setDeltaMovement(vec31.multiply(1.0D, -0.5D, 1.0D));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     boolean bool = (Mth.floor(this.xo) != Mth.floor(getX()) || Mth.floor(this.yo) != Mth.floor(getY()) || Mth.floor(this.zo) != Mth.floor(getZ())) ? true : false;
/* 170 */     byte b = bool ? 2 : 40;
/*     */     
/* 172 */     if (this.tickCount % b == 0 && 
/* 173 */       !level().isClientSide() && isMergable()) {
/* 174 */       mergeWithNeighbours();
/*     */     }
/*     */ 
/*     */     
/* 178 */     if (this.age != -32768) {
/* 179 */       this.age++;
/*     */     }
/*     */ 
/*     */     
/* 183 */     this.needsSync |= updateInWaterStateAndDoFluidPushing();
/*     */     
/* 185 */     if (!level().isClientSide()) {
/*     */ 
/*     */ 
/*     */       
/* 189 */       double d = getDeltaMovement().subtract(vec3).lengthSqr();
/* 190 */       if (d > 0.01D) {
/* 191 */         this.needsSync = true;
/*     */       }
/*     */     } 
/*     */     
/* 195 */     if (!level().isClientSide() && this.age >= 6000) {
/* 196 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getBlockPosBelowThatAffectsMyMovement() {
/* 203 */     return getOnPos(0.999999F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 208 */     setFluidMovement(0.9900000095367432D);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUnderLavaMovement() {
/* 213 */     setFluidMovement(0.949999988079071D);
/*     */   }
/*     */   
/*     */   private void setFluidMovement(double paramDouble) {
/* 217 */     Vec3 vec3 = getDeltaMovement();
/* 218 */     setDeltaMovement(vec3.x * paramDouble, vec3.y + (
/*     */         
/* 220 */         (vec3.y < 0.05999999865889549D) ? 5.0E-4F : 0.0F), vec3.z * paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeWithNeighbours() {
/* 226 */     if (!isMergable()) {
/*     */       return;
/*     */     }
/* 229 */     List list = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.5D, 0.0D, 0.5D), paramItemEntity -> (paramItemEntity != this && paramItemEntity.isMergable()));
/* 230 */     for (ItemEntity itemEntity : list) {
/* 231 */       if (itemEntity.isMergable()) {
/* 232 */         tryToMerge(itemEntity);
/* 233 */         if (isRemoved()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMergable() {
/* 241 */     ItemStack itemStack = getItem();
/* 242 */     return (isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && itemStack.getCount() < itemStack.getMaxStackSize());
/*     */   }
/*     */   
/*     */   private void tryToMerge(ItemEntity paramItemEntity) {
/* 246 */     ItemStack itemStack1 = getItem();
/* 247 */     ItemStack itemStack2 = paramItemEntity.getItem();
/*     */     
/* 249 */     if (!Objects.equals(this.target, paramItemEntity.target) || !areMergable(itemStack1, itemStack2)) {
/*     */       return;
/*     */     }
/*     */     
/* 253 */     if (itemStack2.getCount() < itemStack1.getCount()) {
/* 254 */       merge(this, itemStack1, paramItemEntity, itemStack2);
/*     */     } else {
/* 256 */       merge(paramItemEntity, itemStack2, this, itemStack1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean areMergable(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 261 */     if (paramItemStack2.getCount() + paramItemStack1.getCount() > paramItemStack2.getMaxStackSize()) {
/* 262 */       return false;
/*     */     }
/* 264 */     return ItemStack.isSameItemSameComponents(paramItemStack1, paramItemStack2);
/*     */   }
/*     */   
/*     */   public static ItemStack merge(ItemStack paramItemStack1, ItemStack paramItemStack2, int paramInt) {
/* 268 */     int i = Math.min(Math.min(paramItemStack1.getMaxStackSize(), paramInt) - paramItemStack1.getCount(), paramItemStack2.getCount());
/* 269 */     ItemStack itemStack = paramItemStack1.copyWithCount(paramItemStack1.getCount() + i);
/* 270 */     paramItemStack2.shrink(i);
/* 271 */     return itemStack;
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity paramItemEntity, ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 275 */     ItemStack itemStack = merge(paramItemStack1, paramItemStack2, 64);
/* 276 */     paramItemEntity.setItem(itemStack);
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity paramItemEntity1, ItemStack paramItemStack1, ItemEntity paramItemEntity2, ItemStack paramItemStack2) {
/* 280 */     merge(paramItemEntity1, paramItemStack1, paramItemStack2);
/* 281 */     paramItemEntity1.pickupDelay = Math.max(paramItemEntity1.pickupDelay, paramItemEntity2.pickupDelay);
/* 282 */     paramItemEntity1.age = Math.min(paramItemEntity1.age, paramItemEntity2.age);
/*     */     
/* 284 */     if (paramItemStack2.isEmpty()) {
/* 285 */       paramItemEntity2.discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fireImmune() {
/* 291 */     return (!getItem().canBeHurtBy(damageSources().inFire()) || super.fireImmune());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldPlayLavaHurtSound() {
/* 296 */     if (this.health <= 0) {
/* 297 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 302 */     return (this.tickCount % 10 == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtClient(DamageSource paramDamageSource) {
/* 307 */     if (isInvulnerableToBase(paramDamageSource)) {
/* 308 */       return false;
/*     */     }
/* 310 */     return getItem().canBeHurtBy(paramDamageSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 315 */     if (isInvulnerableToBase(paramDamageSource)) {
/* 316 */       return false;
/*     */     }
/* 318 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && paramDamageSource.getEntity() instanceof net.minecraft.world.entity.Mob) {
/* 319 */       return false;
/*     */     }
/* 321 */     if (!getItem().canBeHurtBy(paramDamageSource)) {
/* 322 */       return false;
/*     */     }
/* 324 */     markHurt();
/* 325 */     this.health = (int)(this.health - paramFloat);
/* 326 */     gameEvent((Holder)GameEvent.ENTITY_DAMAGE, paramDamageSource.getEntity());
/* 327 */     if (this.health <= 0) {
/* 328 */       getItem().onDestroyed(this);
/* 329 */       discard();
/*     */     } 
/* 331 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion paramExplosion) {
/* 336 */     if (paramExplosion.shouldAffectBlocklikeEntities()) {
/* 337 */       return super.ignoreExplosion(paramExplosion);
/*     */     }
/* 339 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 344 */     paramValueOutput.putShort("Health", (short)this.health);
/* 345 */     paramValueOutput.putShort("Age", (short)this.age);
/* 346 */     paramValueOutput.putShort("PickupDelay", (short)this.pickupDelay);
/* 347 */     EntityReference.store(this.thrower, paramValueOutput, "Thrower");
/* 348 */     paramValueOutput.storeNullable("Owner", UUIDUtil.CODEC, this.target);
/* 349 */     if (!getItem().isEmpty()) {
/* 350 */       paramValueOutput.store("Item", ItemStack.CODEC, getItem());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 356 */     this.health = paramValueInput.getShortOr("Health", (short)5);
/* 357 */     this.age = paramValueInput.getShortOr("Age", (short)0);
/* 358 */     this.pickupDelay = paramValueInput.getShortOr("PickupDelay", (short)0);
/* 359 */     this.target = paramValueInput.read("Owner", UUIDUtil.CODEC).orElse(null);
/* 360 */     this.thrower = EntityReference.read(paramValueInput, "Thrower");
/* 361 */     setItem(paramValueInput.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 362 */     if (getItem().isEmpty()) {
/* 363 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player paramPlayer) {
/* 369 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 373 */     ItemStack itemStack = getItem();
/* 374 */     Item item = itemStack.getItem();
/* 375 */     int i = itemStack.getCount();
/* 376 */     if (this.pickupDelay == 0 && (this.target == null || this.target.equals(paramPlayer.getUUID())) && paramPlayer.getInventory().add(itemStack)) {
/* 377 */       paramPlayer.take(this, i);
/* 378 */       if (itemStack.isEmpty()) {
/* 379 */         discard();
/*     */ 
/*     */         
/* 382 */         itemStack.setCount(i);
/*     */       } 
/* 384 */       paramPlayer.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
/* 385 */       paramPlayer.onItemPickup(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 391 */     Component component = getCustomName();
/* 392 */     if (component != null) {
/* 393 */       return component;
/*     */     }
/*     */     
/* 396 */     return getItem().getItemName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 401 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition paramTeleportTransition) {
/* 406 */     Entity entity = super.teleport(paramTeleportTransition);
/*     */     
/* 408 */     if (!level().isClientSide() && entity instanceof ItemEntity) { ItemEntity itemEntity = (ItemEntity)entity;
/* 409 */       itemEntity.mergeWithNeighbours(); }
/*     */     
/* 411 */     return entity;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 415 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack paramItemStack) {
/* 419 */     getEntityData().set(DATA_ITEM, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 424 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/* 425 */     if (DATA_ITEM.equals(paramEntityDataAccessor)) {
/* 426 */       getItem().setEntityRepresentation(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(UUID paramUUID) {
/* 434 */     this.target = paramUUID;
/*     */   }
/*     */   
/*     */   public void setThrower(Entity paramEntity) {
/* 438 */     this.thrower = EntityReference.of((UniquelyIdentifyable)paramEntity);
/*     */   }
/*     */   
/*     */   public int getAge() {
/* 442 */     return this.age;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultPickUpDelay() {
/* 447 */     this.pickupDelay = 10;
/*     */   }
/*     */   
/*     */   public void setNoPickUpDelay() {
/* 451 */     this.pickupDelay = 0;
/*     */   }
/*     */   
/*     */   public void setNeverPickUp() {
/* 455 */     this.pickupDelay = 32767;
/*     */   }
/*     */   
/*     */   public void setPickUpDelay(int paramInt) {
/* 459 */     this.pickupDelay = paramInt;
/*     */   }
/*     */   
/*     */   public boolean hasPickUpDelay() {
/* 463 */     return (this.pickupDelay > 0);
/*     */   }
/*     */   
/*     */   public void setUnlimitedLifetime() {
/* 467 */     this.age = -32768;
/*     */   }
/*     */   
/*     */   public void setExtendedLifetime() {
/* 471 */     this.age = -6000;
/*     */   }
/*     */   
/*     */   public void makeFakeItem() {
/* 475 */     setNeverPickUp();
/* 476 */     this.age = 5999;
/*     */   }
/*     */   
/*     */   public static float getSpin(float paramFloat1, float paramFloat2) {
/* 480 */     return paramFloat1 / 20.0F + paramFloat2;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 485 */     return SoundSource.AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVisualRotationYInDegrees() {
/* 490 */     return 180.0F - getSpin(getAge() + 0.5F, this.bobOffs) / 6.2831855F * 360.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/* 495 */     if (paramInt == 0) {
/* 496 */       return SlotAccess.of(this::getItem, this::setItem);
/*     */     }
/* 498 */     return super.getSlot(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\item\ItemEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */