/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Slot
/*     */ {
/*     */   private final int slot;
/*     */   public final Container container;
/*     */   public int index;
/*     */   public final int x;
/*     */   public final int y;
/*     */   
/*     */   public Slot(Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/*  20 */     this.container = paramContainer;
/*  21 */     this.slot = paramInt1;
/*  22 */     this.x = paramInt2;
/*  23 */     this.y = paramInt3;
/*     */   }
/*     */   
/*     */   public void onQuickCraft(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/*  27 */     int i = paramItemStack2.getCount() - paramItemStack1.getCount();
/*  28 */     if (i > 0) {
/*  29 */       onQuickCraft(paramItemStack2, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onQuickCraft(ItemStack paramItemStack, int paramInt) {}
/*     */ 
/*     */   
/*     */   protected void onSwapCraft(int paramInt) {}
/*     */ 
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack paramItemStack) {}
/*     */ 
/*     */   
/*     */   public void onTake(Player paramPlayer, ItemStack paramItemStack) {
/*  44 */     setChanged();
/*     */   }
/*     */   
/*     */   public boolean mayPlace(ItemStack paramItemStack) {
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/*  52 */     return this.container.getItem(this.slot);
/*     */   }
/*     */   
/*     */   public boolean hasItem() {
/*  56 */     return !getItem().isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setByPlayer(ItemStack paramItemStack) {
/*  62 */     setByPlayer(paramItemStack, getItem());
/*     */   }
/*     */   public void setByPlayer(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/*  65 */     set(paramItemStack1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(ItemStack paramItemStack) {
/*  74 */     this.container.setItem(this.slot, paramItemStack);
/*  75 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setChanged() {
/*  79 */     this.container.setChanged();
/*     */   }
/*     */   
/*     */   public int getMaxStackSize() {
/*  83 */     return this.container.getMaxStackSize();
/*     */   }
/*     */   
/*     */   public int getMaxStackSize(ItemStack paramItemStack) {
/*  87 */     return Math.min(getMaxStackSize(), paramItemStack.getMaxStackSize());
/*     */   }
/*     */   
/*     */   public Identifier getNoItemIcon() {
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   public ItemStack remove(int paramInt) {
/*  95 */     return this.container.removeItem(this.slot, paramInt);
/*     */   }
/*     */   
/*     */   public boolean mayPickup(Player paramPlayer) {
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ItemStack> tryRemove(int paramInt1, int paramInt2, Player paramPlayer) {
/* 108 */     if (!mayPickup(paramPlayer)) {
/* 109 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (!allowModification(paramPlayer) && paramInt2 < getItem().getCount()) {
/* 114 */       return Optional.empty();
/*     */     }
/*     */     
/* 117 */     paramInt1 = Math.min(paramInt1, paramInt2);
/* 118 */     ItemStack itemStack = remove(paramInt1);
/* 119 */     if (itemStack.isEmpty()) {
/* 120 */       return Optional.empty();
/*     */     }
/* 122 */     if (getItem().isEmpty()) {
/* 123 */       setByPlayer(ItemStack.EMPTY, itemStack);
/*     */     }
/* 125 */     return Optional.of(itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack safeTake(int paramInt1, int paramInt2, Player paramPlayer) {
/* 134 */     Optional<ItemStack> optional = tryRemove(paramInt1, paramInt2, paramPlayer);
/* 135 */     optional.ifPresent(paramItemStack -> onTake(paramPlayer, paramItemStack));
/* 136 */     return optional.orElse(ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack safeInsert(ItemStack paramItemStack) {
/* 143 */     return safeInsert(paramItemStack, paramItemStack.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack safeInsert(ItemStack paramItemStack, int paramInt) {
/* 150 */     if (paramItemStack.isEmpty() || !mayPlace(paramItemStack)) {
/* 151 */       return paramItemStack;
/*     */     }
/*     */     
/* 154 */     ItemStack itemStack = getItem();
/* 155 */     int i = Math.min(Math.min(paramInt, paramItemStack.getCount()), getMaxStackSize(paramItemStack) - itemStack.getCount());
/*     */     
/* 157 */     if (i <= 0)
/* 158 */       return paramItemStack; 
/* 159 */     if (itemStack.isEmpty()) {
/* 160 */       setByPlayer(paramItemStack.split(i));
/* 161 */     } else if (ItemStack.isSameItemSameComponents(itemStack, paramItemStack)) {
/* 162 */       paramItemStack.shrink(i);
/* 163 */       itemStack.grow(i);
/*     */       
/* 165 */       setByPlayer(itemStack);
/*     */     } 
/* 167 */     return paramItemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowModification(Player paramPlayer) {
/* 172 */     return (mayPickup(paramPlayer) && mayPlace(getItem()));
/*     */   }
/*     */   
/*     */   public int getContainerSlot() {
/* 176 */     return this.slot;
/*     */   }
/*     */   
/*     */   public boolean isHighlightable() {
/* 180 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFake() {
/* 184 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\Slot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */