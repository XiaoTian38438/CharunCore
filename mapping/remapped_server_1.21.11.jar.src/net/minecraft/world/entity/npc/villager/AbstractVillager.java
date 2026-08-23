/*     */ package net.minecraft.world.entity.npc.villager;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.npc.Npc;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractVillager
/*     */   extends AgeableMob
/*     */   implements InventoryCarrier, Npc, Merchant
/*     */ {
/*  44 */   private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER = SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int VILLAGER_SLOT_OFFSET = 300;
/*     */   
/*     */   private static final int VILLAGER_INVENTORY_SIZE = 8;
/*     */   
/*     */   private Player tradingPlayer;
/*     */   protected MerchantOffers offers;
/*  52 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   public AbstractVillager(EntityType<? extends AbstractVillager> paramEntityType, Level paramLevel) {
/*  55 */     super(paramEntityType, paramLevel);
/*  56 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  57 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/*  62 */     if (paramSpawnGroupData == null) {
/*  63 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false);
/*     */     }
/*     */     
/*  66 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   public int getUnhappyCounter() {
/*  70 */     return ((Integer)this.entityData.get(DATA_UNHAPPY_COUNTER)).intValue();
/*     */   }
/*     */   
/*     */   public void setUnhappyCounter(int paramInt) {
/*  74 */     this.entityData.set(DATA_UNHAPPY_COUNTER, Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVillagerXp() {
/*  79 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  84 */     super.defineSynchedData(paramBuilder);
/*  85 */     paramBuilder.define(DATA_UNHAPPY_COUNTER, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTradingPlayer(Player paramPlayer) {
/*  90 */     this.tradingPlayer = paramPlayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public Player getTradingPlayer() {
/*  95 */     return this.tradingPlayer;
/*     */   }
/*     */   
/*     */   public boolean isTrading() {
/*  99 */     return (this.tradingPlayer != null);
/*     */   }
/*     */   
/*     */   public MerchantOffers getOffers() {
/*     */     ServerLevel serverLevel;
/* 104 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 105 */     else { throw new IllegalStateException("Cannot load Villager offers on the client"); }
/*     */     
/* 107 */     if (this.offers == null) {
/* 108 */       this.offers = new MerchantOffers();
/* 109 */       updateTrades(serverLevel);
/*     */     } 
/* 111 */     return this.offers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideOffers(MerchantOffers paramMerchantOffers) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideXp(int paramInt) {}
/*     */ 
/*     */   
/*     */   public void notifyTrade(MerchantOffer paramMerchantOffer) {
/* 124 */     paramMerchantOffer.increaseUses();
/* 125 */     this.ambientSoundTime = -getAmbientSoundInterval();
/*     */     
/* 127 */     rewardTradeXp(paramMerchantOffer);
/*     */     
/* 129 */     if (this.tradingPlayer instanceof ServerPlayer) {
/* 130 */       CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, paramMerchantOffer.getResult());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void rewardTradeXp(MerchantOffer paramMerchantOffer);
/*     */   
/*     */   public boolean showProgressBar() {
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void notifyTradeUpdated(ItemStack paramItemStack) {
/* 143 */     if (!level().isClientSide() && this.ambientSoundTime > -getAmbientSoundInterval() + 20) {
/* 144 */       this.ambientSoundTime = -getAmbientSoundInterval();
/* 145 */       makeSound(getTradeUpdatedSound(!paramItemStack.isEmpty()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getNotifyTradeSound() {
/* 151 */     return SoundEvents.VILLAGER_YES;
/*     */   }
/*     */   
/*     */   protected SoundEvent getTradeUpdatedSound(boolean paramBoolean) {
/* 155 */     return paramBoolean ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
/*     */   }
/*     */   
/*     */   public void playCelebrateSound() {
/* 159 */     makeSound(SoundEvents.VILLAGER_CELEBRATE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 164 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 166 */     if (!level().isClientSide()) {
/* 167 */       MerchantOffers merchantOffers = getOffers();
/* 168 */       if (!merchantOffers.isEmpty()) {
/* 169 */         paramValueOutput.store("Offers", MerchantOffers.CODEC, merchantOffers);
/*     */       }
/*     */     } 
/* 172 */     writeInventoryToTag(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 177 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 179 */     this.offers = paramValueInput.read("Offers", MerchantOffers.CODEC).orElse(null);
/*     */     
/* 181 */     readInventoryFromTag(paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition paramTeleportTransition) {
/* 186 */     stopTrading();
/* 187 */     return super.teleport(paramTeleportTransition);
/*     */   }
/*     */   
/*     */   protected void stopTrading() {
/* 191 */     setTradingPlayer((Player)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource paramDamageSource) {
/* 196 */     super.die(paramDamageSource);
/* 197 */     stopTrading();
/*     */   }
/*     */   
/*     */   protected void addParticlesAroundSelf(ParticleOptions paramParticleOptions) {
/* 201 */     for (byte b = 0; b < 5; b++) {
/* 202 */       double d1 = this.random.nextGaussian() * 0.02D;
/* 203 */       double d2 = this.random.nextGaussian() * 0.02D;
/* 204 */       double d3 = this.random.nextGaussian() * 0.02D;
/* 205 */       level().addParticle(paramParticleOptions, getRandomX(1.0D), getRandomY() + 1.0D, getRandomZ(1.0D), d1, d2, d3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed() {
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 216 */     return this.inventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/* 221 */     int i = paramInt - 300;
/* 222 */     if (i >= 0 && i < this.inventory.getContainerSize()) {
/* 223 */       return this.inventory.getSlot(i);
/*     */     }
/* 225 */     return super.getSlot(paramInt);
/*     */   }
/*     */   
/*     */   protected abstract void updateTrades(ServerLevel paramServerLevel);
/*     */   
/*     */   protected void addOffersFromItemListings(ServerLevel paramServerLevel, MerchantOffers paramMerchantOffers, VillagerTrades.ItemListing[] paramArrayOfItemListing, int paramInt) {
/* 231 */     ArrayList<VillagerTrades.ItemListing> arrayList = Lists.newArrayList((Object[])paramArrayOfItemListing);
/* 232 */     byte b = 0;
/* 233 */     while (b < paramInt && !arrayList.isEmpty()) {
/* 234 */       MerchantOffer merchantOffer = ((VillagerTrades.ItemListing)arrayList.remove(this.random.nextInt(arrayList.size()))).getOffer(paramServerLevel, (Entity)this, this.random);
/* 235 */       if (merchantOffer != null) {
/* 236 */         paramMerchantOffers.add(merchantOffer);
/* 237 */         b++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRopeHoldPosition(float paramFloat) {
/* 244 */     float f = Mth.lerp(paramFloat, this.yBodyRotO, this.yBodyRot) * 0.017453292F;
/* 245 */     Vec3 vec3 = new Vec3(0.0D, getBoundingBox().getYsize() - 1.0D, 0.2D);
/* 246 */     return getPosition(paramFloat).add(vec3.yRot(-f));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClientSide() {
/* 251 */     return level().isClientSide();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 256 */     return (getTradingPlayer() == paramPlayer && isAlive() && paramPlayer.isWithinEntityInteractionRange((Entity)this, 4.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\villager\AbstractVillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */