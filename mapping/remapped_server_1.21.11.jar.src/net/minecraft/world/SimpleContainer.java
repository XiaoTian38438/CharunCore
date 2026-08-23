/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SimpleContainer
/*     */   implements Container, StackedContentsCompatible {
/*     */   private final int size;
/*     */   private final NonNullList<ItemStack> items;
/*     */   private List<ContainerListener> listeners;
/*     */   
/*     */   public SimpleContainer(int paramInt) {
/*  23 */     this.size = paramInt;
/*  24 */     this.items = NonNullList.withSize(paramInt, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public SimpleContainer(ItemStack... paramVarArgs) {
/*  28 */     this.size = paramVarArgs.length;
/*  29 */     this.items = NonNullList.of(ItemStack.EMPTY, (Object[])paramVarArgs);
/*     */   }
/*     */   
/*     */   public void addListener(ContainerListener paramContainerListener) {
/*  33 */     if (this.listeners == null) {
/*  34 */       this.listeners = Lists.newArrayList();
/*     */     }
/*  36 */     this.listeners.add(paramContainerListener);
/*     */   }
/*     */   
/*     */   public void removeListener(ContainerListener paramContainerListener) {
/*  40 */     if (this.listeners != null) {
/*  41 */       this.listeners.remove(paramContainerListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/*  47 */     if (paramInt < 0 || paramInt >= this.items.size()) {
/*  48 */       return ItemStack.EMPTY;
/*     */     }
/*  50 */     return (ItemStack)this.items.get(paramInt);
/*     */   }
/*     */   
/*     */   public List<ItemStack> removeAllItems() {
/*  54 */     List<ItemStack> list = (List)this.items.stream().filter(paramItemStack -> !paramItemStack.isEmpty()).collect(Collectors.toList());
/*  55 */     clearContent();
/*  56 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  61 */     ItemStack itemStack = ContainerHelper.removeItem((List<ItemStack>)this.items, paramInt1, paramInt2);
/*  62 */     if (!itemStack.isEmpty()) {
/*  63 */       setChanged();
/*     */     }
/*  65 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItemType(Item paramItem, int paramInt) {
/*  73 */     ItemStack itemStack = new ItemStack((ItemLike)paramItem, 0);
/*     */     
/*  75 */     for (int i = this.size - 1; i >= 0; i--) {
/*  76 */       ItemStack itemStack1 = getItem(i);
/*  77 */       if (itemStack1.getItem().equals(paramItem)) {
/*  78 */         int j = paramInt - itemStack.getCount();
/*  79 */         ItemStack itemStack2 = itemStack1.split(j);
/*  80 */         itemStack.grow(itemStack2.getCount());
/*  81 */         if (itemStack.getCount() == paramInt) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*  86 */     if (!itemStack.isEmpty()) {
/*  87 */       setChanged();
/*     */     }
/*  89 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack addItem(ItemStack paramItemStack) {
/*  97 */     if (paramItemStack.isEmpty()) {
/*  98 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 101 */     ItemStack itemStack = paramItemStack.copy();
/*     */     
/* 103 */     moveItemToOccupiedSlotsWithSameType(itemStack);
/* 104 */     if (itemStack.isEmpty()) {
/* 105 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 108 */     moveItemToEmptySlots(itemStack);
/* 109 */     if (itemStack.isEmpty()) {
/* 110 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 113 */     return itemStack;
/*     */   }
/*     */   
/*     */   public boolean canAddItem(ItemStack paramItemStack) {
/* 117 */     boolean bool = false;
/* 118 */     for (ItemStack itemStack : this.items) {
/* 119 */       if (itemStack.isEmpty() || (ItemStack.isSameItemSameComponents(itemStack, paramItemStack) && itemStack.getCount() < itemStack.getMaxStackSize())) {
/* 120 */         bool = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 124 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/* 129 */     ItemStack itemStack = (ItemStack)this.items.get(paramInt);
/* 130 */     if (itemStack.isEmpty()) {
/* 131 */       return ItemStack.EMPTY;
/*     */     }
/* 133 */     this.items.set(paramInt, ItemStack.EMPTY);
/* 134 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/* 139 */     this.items.set(paramInt, paramItemStack);
/* 140 */     paramItemStack.limitSize(getMaxStackSize(paramItemStack));
/* 141 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 146 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 151 */     for (ItemStack itemStack : this.items) {
/* 152 */       if (!itemStack.isEmpty()) {
/* 153 */         return false;
/*     */       }
/*     */     } 
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 161 */     if (this.listeners != null) {
/* 162 */       for (ContainerListener containerListener : this.listeners) {
/* 163 */         containerListener.containerChanged(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 175 */     this.items.clear();
/* 176 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents paramStackedItemContents) {
/* 181 */     for (ItemStack itemStack : this.items) {
/* 182 */       paramStackedItemContents.accountStack(itemStack);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 188 */     return ((List)this.items.stream()
/* 189 */       .filter(paramItemStack -> !paramItemStack.isEmpty())
/* 190 */       .collect(Collectors.toList()))
/* 191 */       .toString();
/*     */   }
/*     */   
/*     */   private void moveItemToEmptySlots(ItemStack paramItemStack) {
/* 195 */     for (byte b = 0; b < this.size; b++) {
/* 196 */       ItemStack itemStack = getItem(b);
/* 197 */       if (itemStack.isEmpty()) {
/* 198 */         setItem(b, paramItemStack.copyAndClear());
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveItemToOccupiedSlotsWithSameType(ItemStack paramItemStack) {
/* 205 */     for (byte b = 0; b < this.size; b++) {
/* 206 */       ItemStack itemStack = getItem(b);
/* 207 */       if (ItemStack.isSameItemSameComponents(itemStack, paramItemStack)) {
/* 208 */         moveItemsBetweenStacks(paramItemStack, itemStack);
/* 209 */         if (paramItemStack.isEmpty()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void moveItemsBetweenStacks(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 220 */     int i = getMaxStackSize(paramItemStack2);
/* 221 */     int j = Math.min(paramItemStack1.getCount(), i - paramItemStack2.getCount());
/* 222 */     if (j > 0) {
/* 223 */       paramItemStack2.grow(j);
/* 224 */       paramItemStack1.shrink(j);
/* 225 */       setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fromItemList(ValueInput.TypedInputList<ItemStack> paramTypedInputList) {
/* 230 */     clearContent();
/*     */     
/* 232 */     for (ItemStack itemStack : paramTypedInputList) {
/* 233 */       addItem(itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void storeAsItemList(ValueOutput.TypedOutputList<ItemStack> paramTypedOutputList) {
/* 238 */     for (byte b = 0; b < getContainerSize(); b++) {
/* 239 */       ItemStack itemStack = getItem(b);
/* 240 */       if (!itemStack.isEmpty()) {
/* 241 */         paramTypedOutputList.add(itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 247 */     return this.items;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\SimpleContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */