/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import org.apache.commons.lang3.math.Fraction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mutable
/*     */ {
/*     */   private final List<ItemStack> items;
/*     */   private Fraction weight;
/*     */   private int selectedItem;
/*     */   
/*     */   public Mutable(BundleContents paramBundleContents) {
/* 156 */     this.items = new ArrayList<>(paramBundleContents.items);
/* 157 */     this.weight = paramBundleContents.weight;
/* 158 */     this.selectedItem = paramBundleContents.selectedItem;
/*     */   }
/*     */   
/*     */   public Mutable clearItems() {
/* 162 */     this.items.clear();
/* 163 */     this.weight = Fraction.ZERO;
/* 164 */     this.selectedItem = -1;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   private int findStackIndex(ItemStack paramItemStack) {
/* 169 */     if (!paramItemStack.isStackable()) {
/* 170 */       return -1;
/*     */     }
/* 172 */     for (byte b = 0; b < this.items.size(); b++) {
/* 173 */       if (ItemStack.isSameItemSameComponents(this.items.get(b), paramItemStack)) {
/* 174 */         return b;
/*     */       }
/*     */     } 
/* 177 */     return -1;
/*     */   }
/*     */   
/*     */   private int getMaxAmountToAdd(ItemStack paramItemStack) {
/* 181 */     Fraction fraction = Fraction.ONE.subtract(this.weight);
/* 182 */     return Math.max(fraction.divideBy(BundleContents.getWeight(paramItemStack)).intValue(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int tryInsert(ItemStack paramItemStack) {
/* 189 */     if (!BundleContents.canItemBeInBundle(paramItemStack)) {
/* 190 */       return 0;
/*     */     }
/*     */     
/* 193 */     int i = Math.min(paramItemStack.getCount(), getMaxAmountToAdd(paramItemStack));
/* 194 */     if (i == 0) {
/* 195 */       return 0;
/*     */     }
/*     */     
/* 198 */     this.weight = this.weight.add(BundleContents.getWeight(paramItemStack).multiplyBy(Fraction.getFraction(i, 1)));
/*     */     
/* 200 */     int j = findStackIndex(paramItemStack);
/* 201 */     if (j != -1) {
/* 202 */       ItemStack itemStack1 = this.items.remove(j);
/* 203 */       ItemStack itemStack2 = itemStack1.copyWithCount(itemStack1.getCount() + i);
/* 204 */       paramItemStack.shrink(i);
/*     */       
/* 206 */       this.items.add(0, itemStack2);
/*     */     } else {
/* 208 */       this.items.add(0, paramItemStack.split(i));
/*     */     } 
/*     */     
/* 211 */     return i;
/*     */   }
/*     */   
/*     */   public int tryTransfer(Slot paramSlot, Player paramPlayer) {
/* 215 */     ItemStack itemStack = paramSlot.getItem();
/* 216 */     int i = getMaxAmountToAdd(itemStack);
/* 217 */     return BundleContents.canItemBeInBundle(itemStack) ? tryInsert(paramSlot.safeTake(itemStack.getCount(), i, paramPlayer)) : 0;
/*     */   }
/*     */   
/*     */   public void toggleSelectedItem(int paramInt) {
/* 221 */     this.selectedItem = (this.selectedItem == paramInt || indexIsOutsideAllowedBounds(paramInt)) ? -1 : paramInt;
/*     */   }
/*     */   
/*     */   private boolean indexIsOutsideAllowedBounds(int paramInt) {
/* 225 */     return (paramInt < 0 || paramInt >= this.items.size());
/*     */   }
/*     */   
/*     */   public ItemStack removeOne() {
/* 229 */     if (this.items.isEmpty()) {
/* 230 */       return null;
/*     */     }
/* 232 */     boolean bool = indexIsOutsideAllowedBounds(this.selectedItem) ? false : this.selectedItem;
/* 233 */     ItemStack itemStack = ((ItemStack)this.items.remove(bool)).copy();
/* 234 */     this.weight = this.weight.subtract(BundleContents.getWeight(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
/* 235 */     toggleSelectedItem(-1);
/* 236 */     return itemStack;
/*     */   }
/*     */   
/*     */   public Fraction weight() {
/* 240 */     return this.weight;
/*     */   }
/*     */   
/*     */   public BundleContents toImmutable() {
/* 244 */     return new BundleContents(List.copyOf(this.items), this.weight, this.selectedItem);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\component\BundleContents$Mutable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */