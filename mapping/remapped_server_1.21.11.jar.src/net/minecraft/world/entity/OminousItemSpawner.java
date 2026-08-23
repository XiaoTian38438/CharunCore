/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ProjectileItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class OminousItemSpawner
/*     */   extends Entity {
/*     */   private static final int SPAWN_ITEM_DELAY_MIN = 60;
/*     */   private static final int SPAWN_ITEM_DELAY_MAX = 120;
/*  31 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(OminousItemSpawner.class, EntityDataSerializers.ITEM_STACK); private static final String TAG_SPAWN_ITEM_AFTER_TICKS = "spawn_item_after_ticks"; private static final String TAG_ITEM = "item";
/*     */   public static final int TICKS_BEFORE_ABOUT_TO_SPAWN_SOUND = 36;
/*     */   private long spawnItemAfterTicks;
/*     */   
/*     */   public OminousItemSpawner(EntityType<? extends OminousItemSpawner> paramEntityType, Level paramLevel) {
/*  36 */     super(paramEntityType, paramLevel);
/*  37 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public static OminousItemSpawner create(Level paramLevel, ItemStack paramItemStack) {
/*  41 */     OminousItemSpawner ominousItemSpawner = new OminousItemSpawner(EntityType.OMINOUS_ITEM_SPAWNER, paramLevel);
/*  42 */     ominousItemSpawner.spawnItemAfterTicks = paramLevel.random.nextIntBetweenInclusive(60, 120);
/*  43 */     ominousItemSpawner.setItem(paramItemStack);
/*  44 */     return ominousItemSpawner;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  49 */     super.tick();
/*  50 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  51 */       tickServer(serverLevel); }
/*     */     else
/*  53 */     { tickClient(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void tickServer(ServerLevel paramServerLevel) {
/*  58 */     if (this.tickCount == this.spawnItemAfterTicks - 36L) {
/*  59 */       paramServerLevel.playSound(null, blockPosition(), SoundEvents.TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, SoundSource.NEUTRAL);
/*     */     }
/*  61 */     if (this.tickCount >= this.spawnItemAfterTicks) {
/*  62 */       spawnItem();
/*  63 */       kill(paramServerLevel);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickClient() {
/*  68 */     if (level().getGameTime() % 5L == 0L)
/*  69 */       addParticles(); 
/*     */   }
/*     */   private void spawnItem() {
/*     */     ServerLevel serverLevel;
/*     */     ItemEntity itemEntity;
/*  74 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*     */     
/*  78 */     ItemStack itemStack = getItem();
/*  79 */     if (itemStack.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  84 */     Item item = itemStack.getItem(); if (item instanceof ProjectileItem) { ProjectileItem projectileItem = (ProjectileItem)item;
/*  85 */       Entity entity = spawnProjectile(serverLevel, projectileItem, itemStack); }
/*     */     else
/*  87 */     { itemEntity = new ItemEntity((Level)serverLevel, getX(), getY(), getZ(), itemStack);
/*  88 */       serverLevel.addFreshEntity((Entity)itemEntity); }
/*     */ 
/*     */     
/*  91 */     serverLevel.levelEvent(3021, blockPosition(), 1);
/*  92 */     serverLevel.gameEvent((Entity)itemEntity, (Holder)GameEvent.ENTITY_PLACE, position());
/*  93 */     setItem(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   private Entity spawnProjectile(ServerLevel paramServerLevel, ProjectileItem paramProjectileItem, ItemStack paramItemStack) {
/*  97 */     ProjectileItem.DispenseConfig dispenseConfig = paramProjectileItem.createDispenseConfig();
/*  98 */     dispenseConfig.overrideDispenseEvent().ifPresent(paramInt -> paramServerLevel.levelEvent(paramInt, blockPosition(), 0));
/*  99 */     Direction direction = Direction.DOWN;
/* 100 */     Projectile projectile = Projectile.spawnProjectileUsingShoot(paramProjectileItem
/* 101 */         .asProjectile((Level)paramServerLevel, (Position)position(), paramItemStack, direction), paramServerLevel, paramItemStack, direction
/*     */         
/* 103 */         .getStepX(), direction.getStepY(), direction.getStepZ(), dispenseConfig
/* 104 */         .power(), dispenseConfig.uncertainty());
/*     */     
/* 106 */     projectile.setOwner(this);
/* 107 */     return (Entity)projectile;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 112 */     paramBuilder.define(DATA_ITEM, ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 117 */     setItem(paramValueInput.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 118 */     this.spawnItemAfterTicks = paramValueInput.getLongOr("spawn_item_after_ticks", 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 123 */     if (!getItem().isEmpty()) {
/* 124 */       paramValueOutput.store("item", ItemStack.CODEC, getItem());
/*     */     }
/* 126 */     paramValueOutput.putLong("spawn_item_after_ticks", this.spawnItemAfterTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canAddPassenger(Entity paramEntity) {
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean couldAcceptPassenger() {
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addPassenger(Entity paramEntity) {
/* 141 */     throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 146 */     return PushReaction.IGNORE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   public void addParticles() {
/* 155 */     Vec3 vec3 = position();
/* 156 */     int i = this.random.nextIntBetweenInclusive(1, 3);
/* 157 */     for (byte b = 0; b < i; b++) {
/* 158 */       double d = 0.4D;
/*     */ 
/*     */ 
/*     */       
/* 162 */       Vec3 vec31 = new Vec3(getX() + 0.4D * (this.random.nextGaussian() - this.random.nextGaussian()), getY() + 0.4D * (this.random.nextGaussian() - this.random.nextGaussian()), getZ() + 0.4D * (this.random.nextGaussian() - this.random.nextGaussian()));
/*     */       
/* 164 */       Vec3 vec32 = vec3.vectorTo(vec31);
/* 165 */       level().addParticle((ParticleOptions)ParticleTypes.OMINOUS_SPAWNING, vec3.x(), vec3.y(), vec3.z(), vec32.x(), vec32.y(), vec32.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 170 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   private void setItem(ItemStack paramItemStack) {
/* 174 */     getEntityData().set(DATA_ITEM, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 179 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\OminousItemSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */