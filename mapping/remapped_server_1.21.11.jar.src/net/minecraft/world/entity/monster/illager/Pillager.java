/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Pillager
/*     */   extends AbstractIllager
/*     */   implements CrossbowAttackMob, InventoryCarrier
/*     */ {
/*  62 */   private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int INVENTORY_SIZE = 5;
/*     */   
/*     */   private static final int SLOT_OFFSET = 300;
/*  67 */   private final SimpleContainer inventory = new SimpleContainer(5);
/*     */   
/*     */   public Pillager(EntityType<? extends Pillager> paramEntityType, Level paramLevel) {
/*  70 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  75 */     super.registerGoals();
/*     */     
/*  77 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  78 */     this.goalSelector.addGoal(1, (Goal)new AvoidEntityGoal((PathfinderMob)this, Creaking.class, 8.0F, 1.0D, 1.2D));
/*  79 */     this.goalSelector.addGoal(2, (Goal)new Raider.HoldGroundAttackGoal(this, 10.0F));
/*  80 */     this.goalSelector.addGoal(3, (Goal)new RangedCrossbowAttackGoal((Monster)this, 1.0D, 8.0F));
/*  81 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  82 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 15.0F, 1.0F));
/*  83 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 15.0F));
/*     */     
/*  85 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  86 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  87 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/*  88 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  92 */     return Monster.createMonsterAttributes()
/*  93 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  94 */       .add(Attributes.MAX_HEALTH, 24.0D)
/*  95 */       .add(Attributes.ATTACK_DAMAGE, 5.0D)
/*  96 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 101 */     super.defineSynchedData(paramBuilder);
/*     */     
/* 103 */     paramBuilder.define(IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseNonMeleeWeapon(ItemStack paramItemStack) {
/* 108 */     return (paramItemStack.getItem() == Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   public boolean isChargingCrossbow() {
/* 112 */     return ((Boolean)this.entityData.get(IS_CHARGING_CROSSBOW)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChargingCrossbow(boolean paramBoolean) {
/* 117 */     this.entityData.set(IS_CHARGING_CROSSBOW, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrossbowAttackPerformed() {
/* 122 */     this.noActionTime = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 127 */     return ItemTags.PILLAGER_PREFERRED_WEAPONS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 132 */     super.addAdditionalSaveData(paramValueOutput);
/* 133 */     writeInventoryToTag(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/* 138 */     if (isChargingCrossbow())
/* 139 */       return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE; 
/* 140 */     if (isHolding(Items.CROSSBOW))
/* 141 */       return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD; 
/* 142 */     if (isAggressive()) {
/* 143 */       return AbstractIllager.IllagerArmPose.ATTACKING;
/*     */     }
/*     */     
/* 146 */     return AbstractIllager.IllagerArmPose.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 151 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 153 */     readInventoryFromTag(paramValueInput);
/*     */     
/* 155 */     setCanPickUpLoot(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 161 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 166 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 171 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/* 172 */     populateDefaultEquipmentSlots(randomSource, paramDifficultyInstance);
/* 173 */     populateDefaultEquipmentEnchantments(paramServerLevelAccessor, randomSource, paramDifficultyInstance);
/*     */     
/* 175 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/* 180 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.CROSSBOW));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void enchantSpawnedWeapon(ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/* 185 */     super.enchantSpawnedWeapon(paramServerLevelAccessor, paramRandomSource, paramDifficultyInstance);
/*     */     
/* 187 */     if (paramRandomSource.nextInt(300) == 0) {
/* 188 */       ItemStack itemStack = getMainHandItem();
/* 189 */       if (itemStack.is(Items.CROSSBOW)) {
/* 190 */         EnchantmentHelper.enchantItemFromProvider(itemStack, paramServerLevelAccessor.registryAccess(), VanillaEnchantmentProviders.PILLAGER_SPAWN_CROSSBOW, paramDifficultyInstance, paramRandomSource);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 197 */     return SoundEvents.PILLAGER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 202 */     return SoundEvents.PILLAGER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 207 */     return SoundEvents.PILLAGER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity paramLivingEntity, float paramFloat) {
/* 212 */     performCrossbowAttack((LivingEntity)this, 1.6F);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 217 */     return this.inventory;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel paramServerLevel, ItemEntity paramItemEntity) {
/* 222 */     ItemStack itemStack = paramItemEntity.getItem();
/* 223 */     if (itemStack.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 224 */       super.pickUpItem(paramServerLevel, paramItemEntity);
/*     */     }
/* 226 */     else if (wantsItem(itemStack)) {
/* 227 */       onItemPickup(paramItemEntity);
/* 228 */       ItemStack itemStack1 = this.inventory.addItem(itemStack);
/* 229 */       if (itemStack1.isEmpty()) {
/* 230 */         paramItemEntity.discard();
/*     */       } else {
/* 232 */         itemStack.setCount(itemStack1.getCount());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean wantsItem(ItemStack paramItemStack) {
/* 239 */     return (hasActiveRaid() && paramItemStack.is(Items.WHITE_BANNER));
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/* 244 */     int i = paramInt - 300;
/* 245 */     if (i >= 0 && i < this.inventory.getContainerSize()) {
/* 246 */       return this.inventory.getSlot(i);
/*     */     }
/* 248 */     return super.getSlot(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean) {
/* 253 */     Raid raid = getCurrentRaid();
/* 254 */     boolean bool = (this.random.nextFloat() <= raid.getEnchantOdds()) ? true : false;
/*     */     
/* 256 */     if (bool) {
/* 257 */       ResourceKey resourceKey; ItemStack itemStack = new ItemStack((ItemLike)Items.CROSSBOW);
/*     */       
/* 259 */       if (paramInt > raid.getNumGroups(Difficulty.NORMAL)) {
/* 260 */         resourceKey = VanillaEnchantmentProviders.RAID_PILLAGER_POST_WAVE_5;
/* 261 */       } else if (paramInt > raid.getNumGroups(Difficulty.EASY)) {
/* 262 */         resourceKey = VanillaEnchantmentProviders.RAID_PILLAGER_POST_WAVE_3;
/*     */       } else {
/* 264 */         resourceKey = null;
/*     */       } 
/*     */       
/* 267 */       if (resourceKey != null) {
/* 268 */         EnchantmentHelper.enchantItemFromProvider(itemStack, paramServerLevel.registryAccess(), resourceKey, paramServerLevel.getCurrentDifficultyAt(blockPosition()), getRandom());
/* 269 */         setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 276 */     return SoundEvents.PILLAGER_CELEBRATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\illager\Pillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */