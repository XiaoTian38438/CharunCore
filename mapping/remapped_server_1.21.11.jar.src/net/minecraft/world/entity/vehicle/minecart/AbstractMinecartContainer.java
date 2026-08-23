/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractMinecartContainer
/*     */   extends AbstractMinecart implements ContainerEntity {
/*  27 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(36, ItemStack.EMPTY);
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*     */   protected AbstractMinecartContainer(EntityType<?> paramEntityType, Level paramLevel) {
/*  32 */     super(paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/*  37 */     super.destroy(paramServerLevel, paramDamageSource);
/*  38 */     chestVehicleDestroyed(paramDamageSource, paramServerLevel, (Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/*  43 */     return getChestVehicleItem(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  48 */     return removeChestVehicleItem(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/*  53 */     return removeChestVehicleItemNoUpdate(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/*  58 */     setChestVehicleItem(paramInt, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/*  63 */     return getChestVehicleSlot(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  72 */     return isChestVehicleStillValid(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason paramRemovalReason) {
/*  77 */     if (!level().isClientSide() && paramRemovalReason.shouldDestroy()) {
/*  78 */       Containers.dropContents(level(), (Entity)this, (Container)this);
/*     */     }
/*     */     
/*  81 */     super.remove(paramRemovalReason);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  86 */     super.addAdditionalSaveData(paramValueOutput);
/*  87 */     addChestVehicleSaveData(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  92 */     super.readAdditionalSaveData(paramValueInput);
/*  93 */     readChestVehicleSaveData(paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/*  98 */     return interactWithContainerVehicle(paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 paramVec3) {
/* 103 */     float f = 0.98F;
/*     */     
/* 105 */     if (this.lootTable == null) {
/* 106 */       int i = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer((Container)this);
/* 107 */       f += i * 0.001F;
/*     */     } 
/*     */     
/* 110 */     if (isInWater()) {
/* 111 */       f *= 0.95F;
/*     */     }
/*     */     
/* 114 */     return paramVec3.multiply(f, 0.0D, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 123 */     clearChestVehicleContent();
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> paramResourceKey, long paramLong) {
/* 127 */     this.lootTable = paramResourceKey;
/* 128 */     this.lootTableSeed = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer) {
/* 133 */     if (this.lootTable == null || !paramPlayer.isSpectator()) {
/* 134 */       unpackChestVehicleLootTable(paramInventory.player);
/* 135 */       return createMenu(paramInt, paramInventory);
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*     */   
/*     */   public ResourceKey<LootTable> getContainerLootTable() {
/* 144 */     return this.lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTable(ResourceKey<LootTable> paramResourceKey) {
/* 149 */     this.lootTable = paramResourceKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContainerLootTableSeed() {
/* 154 */     return this.lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContainerLootTableSeed(long paramLong) {
/* 159 */     this.lootTableSeed = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItemStacks() {
/* 164 */     return this.itemStacks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearItemStacks() {
/* 169 */     this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\AbstractMinecartContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */