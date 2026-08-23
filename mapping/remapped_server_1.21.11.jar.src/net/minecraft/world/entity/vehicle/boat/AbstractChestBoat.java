/*     */ package net.minecraft.world.entity.vehicle.boat;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public abstract class AbstractChestBoat extends AbstractBoat implements HasCustomInventoryScreen, ContainerEntity {
/*  34 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY); private static final int CONTAINER_SIZE = 27;
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*     */   public AbstractChestBoat(EntityType<? extends AbstractChestBoat> paramEntityType, Level paramLevel, Supplier<Item> paramSupplier) {
/*  39 */     super((EntityType)paramEntityType, paramLevel, paramSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getSinglePassengerXOffset() {
/*  45 */     return 0.15F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getMaxPassengers() {
/*  50 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  55 */     super.addAdditionalSaveData(paramValueOutput);
/*  56 */     addChestVehicleSaveData(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  61 */     super.readAdditionalSaveData(paramValueInput);
/*  62 */     readChestVehicleSaveData(paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/*  67 */     destroy(paramServerLevel, getDropItem());
/*  68 */     chestVehicleDestroyed(paramDamageSource, paramServerLevel, (Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason paramRemovalReason) {
/*  73 */     if (!level().isClientSide() && paramRemovalReason.shouldDestroy()) {
/*  74 */       Containers.dropContents(level(), (Entity)this, (Container)this);
/*     */     }
/*  76 */     super.remove(paramRemovalReason);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/*  81 */     InteractionResult interactionResult = super.interact(paramPlayer, paramInteractionHand);
/*  82 */     if (interactionResult != InteractionResult.PASS) {
/*  83 */       return interactionResult;
/*     */     }
/*  85 */     if (!canAddPassenger((Entity)paramPlayer) || paramPlayer.isSecondaryUseActive()) {
/*  86 */       InteractionResult interactionResult1 = interactWithContainerVehicle(paramPlayer);
/*  87 */       if (interactionResult1.consumesAction()) { Level level = paramPlayer.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  88 */           gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)paramPlayer);
/*  89 */           PiglinAi.angerNearbyPiglins(serverLevel, paramPlayer, true); }
/*     */          }
/*  91 */        return interactionResult1;
/*     */     } 
/*  93 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInventoryScreen(Player paramPlayer) {
/*  98 */     paramPlayer.openMenu((MenuProvider)this);
/*  99 */     Level level = paramPlayer.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 100 */       gameEvent((Holder)GameEvent.CONTAINER_OPEN, (Entity)paramPlayer);
/* 101 */       PiglinAi.angerNearbyPiglins(serverLevel, paramPlayer, true); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 107 */     clearChestVehicleContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 112 */     return 27;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/* 117 */     return getChestVehicleItem(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/* 122 */     return removeChestVehicleItem(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/* 127 */     return removeChestVehicleItemNoUpdate(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/* 132 */     setChestVehicleItem(paramInt, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/* 137 */     return getChestVehicleSlot(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 146 */     return isChestVehicleStillValid(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer) {
/* 151 */     if (this.lootTable == null || !paramPlayer.isSpectator()) {
/* 152 */       unpackLootTable(paramInventory.player);
/* 153 */       return (AbstractContainerMenu)ChestMenu.threeRows(paramInt, paramInventory, (Container)this);
/*     */     } 
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public void unpackLootTable(Player paramPlayer) {
/* 159 */     unpackChestVehicleLootTable(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceKey<LootTable> getContainerLootTable() {
/* 164 */     return this.lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTable(ResourceKey<LootTable> paramResourceKey) {
/* 169 */     this.lootTable = paramResourceKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContainerLootTableSeed() {
/* 174 */     return this.lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTableSeed(long paramLong) {
/* 179 */     this.lootTableSeed = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItemStacks() {
/* 184 */     return this.itemStacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearItemStacks() {
/* 189 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser paramContainerUser) {
/* 194 */     level().gameEvent((Holder)GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of((Entity)paramContainerUser.getLivingEntity()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\boat\AbstractChestBoat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */