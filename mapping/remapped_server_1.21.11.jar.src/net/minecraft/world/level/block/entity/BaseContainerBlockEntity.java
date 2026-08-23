/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class BaseContainerBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {
/*  30 */   private LockCode lockKey = LockCode.NO_LOCK;
/*     */   private Component name;
/*     */   
/*     */   protected BaseContainerBlockEntity(BlockEntityType<?> paramBlockEntityType, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  34 */     super(paramBlockEntityType, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  39 */     super.loadAdditional(paramValueInput);
/*     */     
/*  41 */     this.lockKey = LockCode.fromTag(paramValueInput);
/*     */     
/*  43 */     this.name = parseCustomNameSafe(paramValueInput, "CustomName");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  48 */     super.saveAdditional(paramValueOutput);
/*  49 */     this.lockKey.addToTag(paramValueOutput);
/*     */     
/*  51 */     paramValueOutput.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/*  56 */     if (this.name != null) {
/*  57 */       return this.name;
/*     */     }
/*  59 */     return getDefaultName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getDisplayName() {
/*  64 */     return getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getCustomName() {
/*  69 */     return this.name;
/*     */   }
/*     */   
/*     */   protected abstract Component getDefaultName();
/*     */   
/*     */   public boolean canOpen(Player paramPlayer) {
/*  75 */     return this.lockKey.canUnlock(paramPlayer);
/*     */   }
/*     */   
/*     */   public static void sendChestLockedNotifications(Vec3 paramVec3, Player paramPlayer, Component paramComponent) {
/*  79 */     Level level = paramPlayer.level();
/*  80 */     paramPlayer.displayClientMessage((Component)Component.translatable("container.isLocked", new Object[] { paramComponent }), true);
/*  81 */     if (!level.isClientSide()) {
/*  82 */       level.playSound(null, paramVec3.x(), paramVec3.y(), paramVec3.z(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLocked() {
/*  87 */     return !this.lockKey.equals(LockCode.NO_LOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract NonNullList<ItemStack> getItems();
/*     */   
/*     */   protected abstract void setItems(NonNullList<ItemStack> paramNonNullList);
/*     */   
/*     */   public boolean isEmpty() {
/*  96 */     for (ItemStack itemStack : getItems()) {
/*  97 */       if (!itemStack.isEmpty()) {
/*  98 */         return false;
/*     */       }
/*     */     } 
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/* 106 */     return (ItemStack)getItems().get(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/* 111 */     ItemStack itemStack = ContainerHelper.removeItem((List)getItems(), paramInt1, paramInt2);
/* 112 */     if (!itemStack.isEmpty()) {
/* 113 */       setChanged();
/*     */     }
/* 115 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/* 120 */     return ContainerHelper.takeItem((List)getItems(), paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/* 125 */     getItems().set(paramInt, paramItemStack);
/* 126 */     paramItemStack.limitSize(getMaxStackSize(paramItemStack));
/* 127 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 132 */     return Container.stillValidBlockEntity(this, paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 137 */     getItems().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer) {
/* 142 */     if (canOpen(paramPlayer)) {
/* 143 */       return createMenu(paramInt, paramInventory);
/*     */     }
/*     */     
/* 146 */     sendChestLockedNotifications(getBlockPos().getCenter(), paramPlayer, getDisplayName());
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter paramDataComponentGetter) {
/* 154 */     super.applyImplicitComponents(paramDataComponentGetter);
/* 155 */     this.name = (Component)paramDataComponentGetter.get(DataComponents.CUSTOM_NAME);
/* 156 */     this.lockKey = (LockCode)paramDataComponentGetter.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
/* 157 */     ((ItemContainerContents)paramDataComponentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyInto(getItems());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder paramBuilder) {
/* 162 */     super.collectImplicitComponents(paramBuilder);
/* 163 */     paramBuilder.set(DataComponents.CUSTOM_NAME, this.name);
/* 164 */     if (isLocked()) {
/* 165 */       paramBuilder.set(DataComponents.LOCK, this.lockKey);
/*     */     }
/* 167 */     paramBuilder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems((List)getItems()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput paramValueOutput) {
/* 172 */     paramValueOutput.discard("CustomName");
/* 173 */     paramValueOutput.discard("lock");
/* 174 */     paramValueOutput.discard("Items");
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\BaseContainerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */