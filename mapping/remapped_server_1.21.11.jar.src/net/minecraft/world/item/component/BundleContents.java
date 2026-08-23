/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public final class BundleContents implements TooltipComponent {
/*     */   public static final Codec<BundleContents> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> STREAM_CODEC;
/*  24 */   public static final BundleContents EMPTY = new BundleContents(List.of());
/*     */   static {
/*  26 */     CODEC = ItemStack.CODEC.listOf().flatXmap(BundleContents::checkAndCreate, paramBundleContents -> DataResult.success(paramBundleContents.items));
/*  27 */     STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BundleContents::new, paramBundleContents -> paramBundleContents.items);
/*     */   }
/*  29 */   private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
/*     */   
/*     */   private static final int NO_STACK_INDEX = -1;
/*     */   public static final int NO_SELECTED_ITEM_INDEX = -1;
/*     */   final List<ItemStack> items;
/*     */   final Fraction weight;
/*     */   final int selectedItem;
/*     */   
/*     */   BundleContents(List<ItemStack> paramList, Fraction paramFraction, int paramInt) {
/*  38 */     this.items = paramList;
/*  39 */     this.weight = paramFraction;
/*  40 */     this.selectedItem = paramInt;
/*     */   }
/*     */   
/*     */   private static DataResult<BundleContents> checkAndCreate(List<ItemStack> paramList) {
/*     */     try {
/*  45 */       Fraction fraction = computeContentWeight(paramList);
/*  46 */       return DataResult.success(new BundleContents(paramList, fraction, -1));
/*  47 */     } catch (ArithmeticException arithmeticException) {
/*  48 */       return DataResult.error(() -> "Excessive total bundle weight");
/*     */     } 
/*     */   }
/*     */   
/*     */   public BundleContents(List<ItemStack> paramList) {
/*  53 */     this(paramList, computeContentWeight(paramList), -1);
/*     */   }
/*     */   
/*     */   private static Fraction computeContentWeight(List<ItemStack> paramList) {
/*  57 */     Fraction fraction = Fraction.ZERO;
/*  58 */     for (ItemStack itemStack : paramList) {
/*  59 */       fraction = fraction.add(getWeight(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
/*     */     }
/*  61 */     return fraction;
/*     */   }
/*     */ 
/*     */   
/*     */   static Fraction getWeight(ItemStack paramItemStack) {
/*  66 */     BundleContents bundleContents = (BundleContents)paramItemStack.get(DataComponents.BUNDLE_CONTENTS);
/*  67 */     if (bundleContents != null) {
/*  68 */       return BUNDLE_IN_BUNDLE_WEIGHT.add(bundleContents.weight());
/*     */     }
/*  70 */     List<BeehiveBlockEntity.Occupant> list = ((Bees)paramItemStack.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
/*  71 */     if (!list.isEmpty()) {
/*  72 */       return Fraction.ONE;
/*     */     }
/*  74 */     return Fraction.getFraction(1, paramItemStack.getMaxStackSize());
/*     */   }
/*     */   
/*     */   public static boolean canItemBeInBundle(ItemStack paramItemStack) {
/*  78 */     return (!paramItemStack.isEmpty() && paramItemStack.getItem().canFitInsideContainerItems());
/*     */   }
/*     */   
/*     */   public int getNumberOfItemsToShow() {
/*  82 */     int i = size();
/*  83 */     byte b1 = (i > 12) ? 11 : 12;
/*  84 */     int j = i % 4;
/*  85 */     byte b2 = (j == 0) ? 0 : (4 - j);
/*  86 */     return Math.min(i, b1 - b2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItemUnsafe(int paramInt) {
/*  94 */     return this.items.get(paramInt);
/*     */   }
/*     */   
/*     */   public Stream<ItemStack> itemCopyStream() {
/*  98 */     return this.items.stream().map(ItemStack::copy);
/*     */   }
/*     */   
/*     */   public Iterable<ItemStack> items() {
/* 102 */     return this.items;
/*     */   }
/*     */   
/*     */   public Iterable<ItemStack> itemsCopy() {
/* 106 */     return Lists.transform(this.items, ItemStack::copy);
/*     */   }
/*     */   
/*     */   public int size() {
/* 110 */     return this.items.size();
/*     */   }
/*     */   
/*     */   public Fraction weight() {
/* 114 */     return this.weight;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 118 */     return this.items.isEmpty();
/*     */   }
/*     */   
/*     */   public int getSelectedItem() {
/* 122 */     return this.selectedItem;
/*     */   }
/*     */   
/*     */   public boolean hasSelectedItem() {
/* 126 */     return (this.selectedItem != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 131 */     if (this == paramObject) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (paramObject instanceof BundleContents) { BundleContents bundleContents = (BundleContents)paramObject;
/* 135 */       return (this.weight.equals(bundleContents.weight) && ItemStack.listMatches(this.items, bundleContents.items)); }
/*     */     
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return ItemStack.hashStackList(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return "BundleContents" + String.valueOf(this.items);
/*     */   }
/*     */   
/*     */   public static class Mutable {
/*     */     private final List<ItemStack> items;
/*     */     private Fraction weight;
/*     */     private int selectedItem;
/*     */     
/*     */     public Mutable(BundleContents param1BundleContents) {
/* 156 */       this.items = new ArrayList<>(param1BundleContents.items);
/* 157 */       this.weight = param1BundleContents.weight;
/* 158 */       this.selectedItem = param1BundleContents.selectedItem;
/*     */     }
/*     */     
/*     */     public Mutable clearItems() {
/* 162 */       this.items.clear();
/* 163 */       this.weight = Fraction.ZERO;
/* 164 */       this.selectedItem = -1;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     private int findStackIndex(ItemStack param1ItemStack) {
/* 169 */       if (!param1ItemStack.isStackable()) {
/* 170 */         return -1;
/*     */       }
/* 172 */       for (byte b = 0; b < this.items.size(); b++) {
/* 173 */         if (ItemStack.isSameItemSameComponents(this.items.get(b), param1ItemStack)) {
/* 174 */           return b;
/*     */         }
/*     */       } 
/* 177 */       return -1;
/*     */     }
/*     */     
/*     */     private int getMaxAmountToAdd(ItemStack param1ItemStack) {
/* 181 */       Fraction fraction = Fraction.ONE.subtract(this.weight);
/* 182 */       return Math.max(fraction.divideBy(BundleContents.getWeight(param1ItemStack)).intValue(), 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int tryInsert(ItemStack param1ItemStack) {
/* 189 */       if (!BundleContents.canItemBeInBundle(param1ItemStack)) {
/* 190 */         return 0;
/*     */       }
/*     */       
/* 193 */       int i = Math.min(param1ItemStack.getCount(), getMaxAmountToAdd(param1ItemStack));
/* 194 */       if (i == 0) {
/* 195 */         return 0;
/*     */       }
/*     */       
/* 198 */       this.weight = this.weight.add(BundleContents.getWeight(param1ItemStack).multiplyBy(Fraction.getFraction(i, 1)));
/*     */       
/* 200 */       int j = findStackIndex(param1ItemStack);
/* 201 */       if (j != -1) {
/* 202 */         ItemStack itemStack1 = this.items.remove(j);
/* 203 */         ItemStack itemStack2 = itemStack1.copyWithCount(itemStack1.getCount() + i);
/* 204 */         param1ItemStack.shrink(i);
/*     */         
/* 206 */         this.items.add(0, itemStack2);
/*     */       } else {
/* 208 */         this.items.add(0, param1ItemStack.split(i));
/*     */       } 
/*     */       
/* 211 */       return i;
/*     */     }
/*     */     
/*     */     public int tryTransfer(Slot param1Slot, Player param1Player) {
/* 215 */       ItemStack itemStack = param1Slot.getItem();
/* 216 */       int i = getMaxAmountToAdd(itemStack);
/* 217 */       return BundleContents.canItemBeInBundle(itemStack) ? tryInsert(param1Slot.safeTake(itemStack.getCount(), i, param1Player)) : 0;
/*     */     }
/*     */     
/*     */     public void toggleSelectedItem(int param1Int) {
/* 221 */       this.selectedItem = (this.selectedItem == param1Int || indexIsOutsideAllowedBounds(param1Int)) ? -1 : param1Int;
/*     */     }
/*     */     
/*     */     private boolean indexIsOutsideAllowedBounds(int param1Int) {
/* 225 */       return (param1Int < 0 || param1Int >= this.items.size());
/*     */     }
/*     */     
/*     */     public ItemStack removeOne() {
/* 229 */       if (this.items.isEmpty()) {
/* 230 */         return null;
/*     */       }
/* 232 */       boolean bool = indexIsOutsideAllowedBounds(this.selectedItem) ? false : this.selectedItem;
/* 233 */       ItemStack itemStack = ((ItemStack)this.items.remove(bool)).copy();
/* 234 */       this.weight = this.weight.subtract(BundleContents.getWeight(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
/* 235 */       toggleSelectedItem(-1);
/* 236 */       return itemStack;
/*     */     }
/*     */     
/*     */     public Fraction weight() {
/* 240 */       return this.weight;
/*     */     }
/*     */     
/*     */     public BundleContents toImmutable() {
/* 244 */       return new BundleContents(List.copyOf(this.items), this.weight, this.selectedItem);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\BundleContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */