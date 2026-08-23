/*     */ package net.minecraft.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerPlaceRecipe<R extends Recipe<?>>
/*     */ {
/*     */   private static final int ITEM_NOT_FOUND = -1;
/*     */   private final Inventory inventory;
/*     */   private final CraftingMenuAccess<R> menu;
/*     */   private final boolean useMaxItems;
/*     */   private final int gridWidth;
/*     */   private final int gridHeight;
/*     */   private final List<Slot> inputGridSlots;
/*     */   private final List<Slot> slotsToClear;
/*     */   
/*     */   public static <I extends net.minecraft.world.item.crafting.RecipeInput, R extends Recipe<I>> RecipeBookMenu.PostPlaceAction placeRecipe(CraftingMenuAccess<R> paramCraftingMenuAccess, int paramInt1, int paramInt2, List<Slot> paramList1, List<Slot> paramList2, Inventory paramInventory, RecipeHolder<R> paramRecipeHolder, boolean paramBoolean1, boolean paramBoolean2) {
/*  43 */     ServerPlaceRecipe<Recipe<?>> serverPlaceRecipe = new ServerPlaceRecipe<>(paramCraftingMenuAccess, paramInventory, paramBoolean1, paramInt1, paramInt2, paramList1, paramList2);
/*     */ 
/*     */     
/*  46 */     if (!paramBoolean2 && !serverPlaceRecipe.testClearGrid()) {
/*  47 */       return RecipeBookMenu.PostPlaceAction.NOTHING;
/*     */     }
/*     */     
/*  50 */     StackedItemContents stackedItemContents = new StackedItemContents();
/*  51 */     paramInventory.fillStackedContents(stackedItemContents);
/*  52 */     paramCraftingMenuAccess.fillCraftSlotsStackedContents(stackedItemContents);
/*     */     
/*  54 */     return serverPlaceRecipe.tryPlaceRecipe(paramRecipeHolder, stackedItemContents);
/*     */   }
/*     */   
/*     */   private ServerPlaceRecipe(CraftingMenuAccess<R> paramCraftingMenuAccess, Inventory paramInventory, boolean paramBoolean, int paramInt1, int paramInt2, List<Slot> paramList1, List<Slot> paramList2) {
/*  58 */     this.menu = paramCraftingMenuAccess;
/*  59 */     this.inventory = paramInventory;
/*  60 */     this.useMaxItems = paramBoolean;
/*  61 */     this.gridWidth = paramInt1;
/*  62 */     this.gridHeight = paramInt2;
/*  63 */     this.inputGridSlots = paramList1;
/*  64 */     this.slotsToClear = paramList2;
/*     */   }
/*     */   
/*     */   private RecipeBookMenu.PostPlaceAction tryPlaceRecipe(RecipeHolder<R> paramRecipeHolder, StackedItemContents paramStackedItemContents) {
/*  68 */     if (paramStackedItemContents.canCraft(paramRecipeHolder.value(), null)) {
/*  69 */       placeRecipe(paramRecipeHolder, paramStackedItemContents);
/*  70 */       this.inventory.setChanged();
/*  71 */       return RecipeBookMenu.PostPlaceAction.NOTHING;
/*     */     } 
/*  73 */     clearGrid();
/*  74 */     this.inventory.setChanged();
/*  75 */     return RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE;
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearGrid() {
/*  80 */     for (Slot slot : this.slotsToClear) {
/*  81 */       ItemStack itemStack = slot.getItem().copy();
/*  82 */       this.inventory.placeItemBackInInventory(itemStack, false);
/*  83 */       slot.set(itemStack);
/*     */     } 
/*  85 */     this.menu.clearCraftingContent();
/*     */   }
/*     */   
/*     */   private void placeRecipe(RecipeHolder<R> paramRecipeHolder, StackedItemContents paramStackedItemContents) {
/*  89 */     boolean bool = this.menu.recipeMatches(paramRecipeHolder);
/*  90 */     int i = paramStackedItemContents.getBiggestCraftableStack(paramRecipeHolder.value(), null);
/*     */ 
/*     */     
/*  93 */     if (bool) {
/*  94 */       for (Slot slot : this.inputGridSlots) {
/*  95 */         ItemStack itemStack = slot.getItem();
/*  96 */         if (!itemStack.isEmpty() && Math.min(i, itemStack.getMaxStackSize()) < itemStack.getCount() + 1) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 102 */     int j = calculateAmountToCraft(i, bool);
/* 103 */     ArrayList<Holder<Item>> arrayList = new ArrayList();
/* 104 */     Objects.requireNonNull(arrayList); if (!paramStackedItemContents.canCraft(paramRecipeHolder.value(), j, arrayList::add)) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     int k = clampToMaxStackSize(j, arrayList);
/*     */     
/* 110 */     arrayList.clear();
/* 111 */     Objects.requireNonNull(arrayList); if (k != j && !paramStackedItemContents.canCraft(paramRecipeHolder.value(), k, arrayList::add)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 116 */     clearGrid();
/* 117 */     PlaceRecipeHelper.placeRecipe(this.gridWidth, this.gridHeight, paramRecipeHolder.value(), (Iterable<?>)paramRecipeHolder.value().placementInfo().slotsToIngredientIndex(), (paramInteger, paramInt2, paramInt3, paramInt4) -> {
/*     */           if (paramInteger.intValue() == -1) {
/*     */             return;
/*     */           }
/*     */           Slot slot = this.inputGridSlots.get(paramInt2);
/*     */           Holder<Item> holder = paramList.get(paramInteger.intValue());
/*     */           int i = paramInt1;
/*     */           while (i > 0) {
/*     */             i = moveItemToGrid(slot, holder, i);
/*     */             if (i == -1) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int clampToMaxStackSize(int paramInt, List<Holder<Item>> paramList) {
/* 136 */     for (Holder<Item> holder : paramList) {
/* 137 */       paramInt = Math.min(paramInt, ((Item)holder.value()).getDefaultMaxStackSize());
/*     */     }
/* 139 */     return paramInt;
/*     */   }
/*     */   
/*     */   private int calculateAmountToCraft(int paramInt, boolean paramBoolean) {
/* 143 */     if (this.useMaxItems) {
/* 144 */       return paramInt;
/*     */     }
/*     */     
/* 147 */     if (paramBoolean) {
/* 148 */       int i = Integer.MAX_VALUE;
/*     */       
/* 150 */       for (Slot slot : this.inputGridSlots) {
/* 151 */         ItemStack itemStack = slot.getItem();
/* 152 */         if (!itemStack.isEmpty() && i > itemStack.getCount()) {
/* 153 */           i = itemStack.getCount();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 158 */       if (i != Integer.MAX_VALUE) {
/* 159 */         i++;
/*     */       }
/* 161 */       return i;
/*     */     } 
/*     */     
/* 164 */     return 1;
/*     */   }
/*     */   
/*     */   private int moveItemToGrid(Slot paramSlot, Holder<Item> paramHolder, int paramInt) {
/* 168 */     ItemStack itemStack3, itemStack1 = paramSlot.getItem();
/* 169 */     int i = this.inventory.findSlotMatchingCraftingIngredient(paramHolder, itemStack1);
/* 170 */     if (i == -1) {
/* 171 */       return -1;
/*     */     }
/* 173 */     ItemStack itemStack2 = this.inventory.getItem(i);
/*     */ 
/*     */     
/* 176 */     if (paramInt < itemStack2.getCount()) {
/* 177 */       itemStack3 = this.inventory.removeItem(i, paramInt);
/*     */     } else {
/* 179 */       itemStack3 = this.inventory.removeItemNoUpdate(i);
/*     */     } 
/*     */     
/* 182 */     int j = itemStack3.getCount();
/* 183 */     if (itemStack1.isEmpty()) {
/* 184 */       paramSlot.set(itemStack3);
/*     */     }
/*     */     else {
/*     */       
/* 188 */       itemStack1.grow(j);
/*     */     } 
/*     */     
/* 191 */     return paramInt - j;
/*     */   }
/*     */   
/*     */   private boolean testClearGrid() {
/* 195 */     ArrayList<ItemStack> arrayList = Lists.newArrayList();
/* 196 */     int i = getAmountOfFreeSlotsInInventory();
/*     */     
/* 198 */     for (Slot slot : this.inputGridSlots) {
/* 199 */       ItemStack itemStack = slot.getItem().copy();
/* 200 */       if (itemStack.isEmpty()) {
/*     */         continue;
/*     */       }
/*     */       
/* 204 */       int j = this.inventory.getSlotWithRemainingSpace(itemStack);
/* 205 */       if (j == -1 && arrayList.size() <= i) {
/* 206 */         for (ItemStack itemStack1 : arrayList) {
/* 207 */           if (ItemStack.isSameItem(itemStack1, itemStack) && itemStack1.getCount() != itemStack1.getMaxStackSize() && itemStack1.getCount() + itemStack.getCount() <= itemStack1.getMaxStackSize()) {
/* 208 */             itemStack1.grow(itemStack.getCount());
/* 209 */             itemStack.setCount(0);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 214 */         if (!itemStack.isEmpty()) {
/* 215 */           if (arrayList.size() < i) {
/* 216 */             arrayList.add(itemStack); continue;
/*     */           } 
/* 218 */           return false;
/*     */         } 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 225 */       if (j == -1) {
/* 226 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 230 */     return true;
/*     */   }
/*     */   
/*     */   private int getAmountOfFreeSlotsInInventory() {
/* 234 */     byte b = 0;
/* 235 */     for (ItemStack itemStack : this.inventory.getNonEquipmentItems()) {
/* 236 */       if (itemStack.isEmpty()) {
/* 237 */         b++;
/*     */       }
/*     */     } 
/* 240 */     return b;
/*     */   }
/*     */   
/*     */   public static interface CraftingMenuAccess<T extends Recipe<?>> {
/*     */     void fillCraftSlotsStackedContents(StackedItemContents param1StackedItemContents);
/*     */     
/*     */     void clearCraftingContent();
/*     */     
/*     */     boolean recipeMatches(RecipeHolder<T> param1RecipeHolder);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\recipebook\ServerPlaceRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */