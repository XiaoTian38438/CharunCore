/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ReputationEventHandler;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.gossip.GossipContainer;
/*     */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class ZombieVillager
/*     */   extends Zombie
/*     */   implements VillagerDataHolder {
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.BOOLEAN);
/*  63 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.VILLAGER_DATA);
/*     */   
/*     */   private static final int VILLAGER_CONVERSION_WAIT_MIN = 3600;
/*     */   
/*     */   private static final int VILLAGER_CONVERSION_WAIT_MAX = 6000;
/*     */   private static final int MAX_SPECIAL_BLOCKS_COUNT = 14;
/*     */   private static final int SPECIAL_BLOCK_RADIUS = 4;
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   private static final int DEFAULT_XP = 0;
/*  72 */   private static final Set<EntitySpawnReason> REASONS_NOT_TO_SET_TYPE = EnumSet.of(EntitySpawnReason.LOAD, new EntitySpawnReason[] { EntitySpawnReason.DIMENSION_TRAVEL, EntitySpawnReason.CONVERSION, EntitySpawnReason.SPAWN_ITEM_USE, EntitySpawnReason.SPAWNER, EntitySpawnReason.TRIAL_SPAWNER });
/*     */   
/*     */   private int villagerConversionTime;
/*     */   
/*     */   private UUID conversionStarter;
/*     */   
/*     */   private GossipContainer gossips;
/*     */   
/*     */   private MerchantOffers tradeOffers;
/*     */   
/*  82 */   private int villagerXp = 0;
/*     */   
/*     */   public ZombieVillager(EntityType<? extends ZombieVillager> paramEntityType, Level paramLevel) {
/*  85 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  90 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  92 */     paramBuilder.define(DATA_CONVERTING_ID, Boolean.valueOf(false));
/*  93 */     paramBuilder.define(DATA_VILLAGER_DATA, initializeVillagerData());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  98 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 100 */     paramValueOutput.store("VillagerData", VillagerData.CODEC, getVillagerData());
/*     */     
/* 102 */     paramValueOutput.storeNullable("Offers", MerchantOffers.CODEC, this.tradeOffers);
/*     */     
/* 104 */     paramValueOutput.storeNullable("Gossips", GossipContainer.CODEC, this.gossips);
/*     */     
/* 106 */     paramValueOutput.putInt("ConversionTime", isConverting() ? this.villagerConversionTime : -1);
/*     */     
/* 108 */     paramValueOutput.storeNullable("ConversionPlayer", UUIDUtil.CODEC, this.conversionStarter);
/*     */     
/* 110 */     paramValueOutput.putInt("Xp", this.villagerXp);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 115 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 117 */     this.entityData.set(DATA_VILLAGER_DATA, paramValueInput.read("VillagerData", VillagerData.CODEC).orElseGet(this::initializeVillagerData));
/*     */     
/* 119 */     this.tradeOffers = paramValueInput.read("Offers", MerchantOffers.CODEC).orElse(null);
/*     */     
/* 121 */     this.gossips = paramValueInput.read("Gossips", GossipContainer.CODEC).orElse(null);
/*     */     
/* 123 */     int i = paramValueInput.getIntOr("ConversionTime", -1);
/* 124 */     if (i != -1) {
/* 125 */       UUID uUID = paramValueInput.read("ConversionPlayer", UUIDUtil.CODEC).orElse(null);
/* 126 */       startConverting(uUID, i);
/*     */     } else {
/* 128 */       getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(false));
/* 129 */       this.villagerConversionTime = -1;
/*     */     } 
/*     */     
/* 132 */     this.villagerXp = paramValueInput.getIntOr("Xp", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 137 */     if (!REASONS_NOT_TO_SET_TYPE.contains(paramEntitySpawnReason)) {
/* 138 */       setVillagerData(getVillagerData().withType((HolderGetter.Provider)paramServerLevelAccessor.registryAccess(), VillagerType.byBiome(paramServerLevelAccessor.getBiome(blockPosition()))));
/*     */     }
/* 140 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   private VillagerData initializeVillagerData() {
/* 144 */     Optional<Holder> optional = BuiltInRegistries.VILLAGER_PROFESSION.getRandom(this.random);
/* 145 */     VillagerData villagerData = Villager.createDefaultVillagerData();
/* 146 */     if (optional.isPresent()) {
/* 147 */       villagerData = villagerData.withProfession(optional.get());
/*     */     }
/* 149 */     return villagerData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 154 */     if (!level().isClientSide() && isAlive() && isConverting()) {
/* 155 */       int i = getConversionProgress();
/*     */       
/* 157 */       this.villagerConversionTime -= i;
/*     */       
/* 159 */       if (this.villagerConversionTime <= 0) {
/* 160 */         finishConversion((ServerLevel)level());
/*     */       }
/*     */     } 
/*     */     
/* 164 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 169 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 170 */     if (itemStack.is(Items.GOLDEN_APPLE)) {
/* 171 */       if (hasEffect(MobEffects.WEAKNESS)) {
/* 172 */         itemStack.consume(1, (LivingEntity)paramPlayer);
/*     */         
/* 174 */         if (!level().isClientSide()) {
/* 175 */           startConverting(paramPlayer.getUUID(), this.random.nextInt(2401) + 3600);
/*     */         }
/*     */ 
/*     */         
/* 179 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */       } 
/* 181 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 184 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/* 194 */     return (!isConverting() && this.villagerXp == 0);
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 198 */     return ((Boolean)getEntityData().get(DATA_CONVERTING_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   private void startConverting(UUID paramUUID, int paramInt) {
/* 202 */     this.conversionStarter = paramUUID;
/* 203 */     this.villagerConversionTime = paramInt;
/* 204 */     getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(true));
/*     */     
/* 206 */     removeEffect(MobEffects.WEAKNESS);
/* 207 */     addEffect(new MobEffectInstance(MobEffects.STRENGTH, paramInt, Math.min(level().getDifficulty().getId() - 1, 0)));
/*     */     
/* 209 */     level().broadcastEntityEvent((Entity)this, (byte)16);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 214 */     if (paramByte == 16) {
/* 215 */       if (!isSilent()) {
/* 216 */         level().playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/*     */       return;
/*     */     } 
/* 220 */     super.handleEntityEvent(paramByte);
/*     */   }
/*     */   
/*     */   private void finishConversion(ServerLevel paramServerLevel) {
/* 224 */     convertTo(EntityType.VILLAGER, ConversionParams.single((Mob)this, false, false), paramVillager -> {
/*     */           for (EquipmentSlot equipmentSlot : dropPreservedEquipment(paramServerLevel, ())) {
/*     */             SlotAccess slotAccess = paramVillager.getSlot(equipmentSlot.getIndex() + 300);
/*     */             if (slotAccess != null) {
/*     */               slotAccess.set(getItemBySlot(equipmentSlot));
/*     */             }
/*     */           } 
/*     */           paramVillager.setVillagerData(getVillagerData());
/*     */           if (this.gossips != null) {
/*     */             paramVillager.setGossips(this.gossips);
/*     */           }
/*     */           if (this.tradeOffers != null) {
/*     */             paramVillager.setOffers(this.tradeOffers.copy());
/*     */           }
/*     */           paramVillager.setVillagerXp(this.villagerXp);
/*     */           paramVillager.finalizeSpawn((ServerLevelAccessor)paramServerLevel, paramServerLevel.getCurrentDifficultyAt(paramVillager.blockPosition()), EntitySpawnReason.CONVERSION, null);
/*     */           paramVillager.refreshBrain(paramServerLevel);
/*     */           if (this.conversionStarter != null) {
/*     */             Player player = paramServerLevel.getPlayerByUUID(this.conversionStarter);
/*     */             if (player instanceof ServerPlayer) {
/*     */               CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((ServerPlayer)player, this, paramVillager);
/*     */               paramServerLevel.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, (Entity)player, (ReputationEventHandler)paramVillager);
/*     */             } 
/*     */           } 
/*     */           paramVillager.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0));
/*     */           if (!isSilent()) {
/*     */             paramServerLevel.levelEvent(null, 1027, blockPosition(), 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setVillagerConversionTime(int paramInt) {
/* 260 */     this.villagerConversionTime = paramInt;
/*     */   }
/*     */   
/*     */   private int getConversionProgress() {
/* 264 */     byte b = 1;
/*     */     
/* 266 */     if (this.random.nextFloat() < 0.01F) {
/* 267 */       byte b1 = 0;
/*     */       
/* 269 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*     */       
/* 271 */       for (int i = (int)getX() - 4; i < (int)getX() + 4 && b1 < 14; i++) {
/* 272 */         for (int j = (int)getY() - 4; j < (int)getY() + 4 && b1 < 14; j++) {
/* 273 */           for (int k = (int)getZ() - 4; k < (int)getZ() + 4 && b1 < 14; k++) {
/* 274 */             BlockState blockState = level().getBlockState((BlockPos)mutableBlockPos.set(i, j, k));
/* 275 */             if (blockState.is(Blocks.IRON_BARS) || blockState.getBlock() instanceof net.minecraft.world.level.block.BedBlock) {
/* 276 */               if (this.random.nextFloat() < 0.3F) {
/* 277 */                 b++;
/*     */               }
/* 279 */               b1++;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 285 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVoicePitch() {
/* 290 */     if (isBaby()) {
/* 291 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F;
/*     */     }
/* 293 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getAmbientSound() {
/* 298 */     return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 303 */     return SoundEvents.ZOMBIE_VILLAGER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getDeathSound() {
/* 308 */     return SoundEvents.ZOMBIE_VILLAGER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getStepSound() {
/* 313 */     return SoundEvents.ZOMBIE_VILLAGER_STEP;
/*     */   }
/*     */   
/*     */   public void setTradeOffers(MerchantOffers paramMerchantOffers) {
/* 317 */     this.tradeOffers = paramMerchantOffers;
/*     */   }
/*     */   
/*     */   public void setGossips(GossipContainer paramGossipContainer) {
/* 321 */     this.gossips = paramGossipContainer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVillagerData(VillagerData paramVillagerData) {
/* 326 */     VillagerData villagerData = getVillagerData();
/* 327 */     if (!villagerData.profession().equals(paramVillagerData.profession())) {
/* 328 */       this.tradeOffers = null;
/*     */     }
/*     */     
/* 331 */     this.entityData.set(DATA_VILLAGER_DATA, paramVillagerData);
/*     */   }
/*     */ 
/*     */   
/*     */   public VillagerData getVillagerData() {
/* 336 */     return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA);
/*     */   }
/*     */   
/*     */   public int getVillagerXp() {
/* 340 */     return this.villagerXp;
/*     */   }
/*     */   
/*     */   public void setVillagerXp(int paramInt) {
/* 344 */     this.villagerXp = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/* 349 */     if (paramDataComponentType == DataComponents.VILLAGER_VARIANT) {
/* 350 */       return (T)castComponentValue(paramDataComponentType, getVillagerData().type());
/*     */     }
/*     */     
/* 353 */     return (T)super.get(paramDataComponentType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 358 */     applyImplicitComponentIfPresent(paramDataComponentGetter, DataComponents.VILLAGER_VARIANT);
/* 359 */     super.applyImplicitComponents(paramDataComponentGetter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> paramDataComponentType, T paramT) {
/* 364 */     if (paramDataComponentType == DataComponents.VILLAGER_VARIANT) {
/* 365 */       Holder holder = (Holder)castComponentValue(DataComponents.VILLAGER_VARIANT, paramT);
/* 366 */       setVillagerData(getVillagerData().withType(holder));
/* 367 */       return true;
/*     */     } 
/*     */     
/* 370 */     return super.applyImplicitComponent(paramDataComponentType, paramT);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\zombie\ZombieVillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */