/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.SeededContainerLoot;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public abstract class RandomizableContainerBlockEntity
/*     */   extends BaseContainerBlockEntity implements RandomizableContainer {
/*     */   protected ResourceKey<LootTable> lootTable;
/*  21 */   protected long lootTableSeed = 0L;
/*     */   
/*     */   protected RandomizableContainerBlockEntity(BlockEntityType<?> paramBlockEntityType, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  24 */     super(paramBlockEntityType, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceKey<LootTable> getLootTable() {
/*  29 */     return this.lootTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> paramResourceKey) {
/*  34 */     this.lootTable = paramResourceKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLootTableSeed() {
/*  39 */     return this.lootTableSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLootTableSeed(long paramLong) {
/*  44 */     this.lootTableSeed = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  49 */     unpackLootTable(null);
/*  50 */     return super.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/*  55 */     unpackLootTable(null);
/*  56 */     return super.getItem(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  61 */     unpackLootTable(null);
/*  62 */     return super.removeItem(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/*  67 */     unpackLootTable(null);
/*  68 */     return super.removeItemNoUpdate(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/*  73 */     unpackLootTable(null);
/*  74 */     super.setItem(paramInt, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canOpen(Player paramPlayer) {
/*  79 */     return (super.canOpen(paramPlayer) && (this.lootTable == null || !paramPlayer.isSpectator()));
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer) {
/*  84 */     if (canOpen(paramPlayer)) {
/*  85 */       unpackLootTable(paramInventory.player);
/*  86 */       return createMenu(paramInt, paramInventory);
/*     */     } 
/*  88 */     BaseContainerBlockEntity.sendChestLockedNotifications(getBlockPos().getCenter(), paramPlayer, getDisplayName());
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/*  94 */     super.applyImplicitComponents(paramDataComponentGetter);
/*  95 */     SeededContainerLoot seededContainerLoot = (SeededContainerLoot)paramDataComponentGetter.get(DataComponents.CONTAINER_LOOT);
/*  96 */     if (seededContainerLoot != null) {
/*  97 */       this.lootTable = seededContainerLoot.lootTable();
/*  98 */       this.lootTableSeed = seededContainerLoot.seed();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 104 */     super.collectImplicitComponents(paramBuilder);
/* 105 */     if (this.lootTable != null) {
/* 106 */       paramBuilder.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 112 */     super.removeComponentsFromTag(paramValueOutput);
/* 113 */     paramValueOutput.discard("LootTable");
/* 114 */     paramValueOutput.discard("LootTableSeed");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\RandomizableContainerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */