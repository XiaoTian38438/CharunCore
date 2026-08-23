/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class TransientCraftingContainer implements CraftingContainer {
/*     */   private final NonNullList<ItemStack> items;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final AbstractContainerMenu menu;
/*     */   
/*     */   public TransientCraftingContainer(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2) {
/*  18 */     this(paramAbstractContainerMenu, paramInt1, paramInt2, NonNullList.withSize(paramInt1 * paramInt2, ItemStack.EMPTY));
/*     */   }
/*     */   
/*     */   private TransientCraftingContainer(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2, NonNullList<ItemStack> paramNonNullList) {
/*  22 */     this.items = paramNonNullList;
/*  23 */     this.menu = paramAbstractContainerMenu;
/*  24 */     this.width = paramInt1;
/*  25 */     this.height = paramInt2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  30 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  35 */     for (ItemStack itemStack : this.items) {
/*  36 */       if (!itemStack.isEmpty()) {
/*  37 */         return false;
/*     */       }
/*     */     } 
/*  40 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/*  45 */     if (paramInt >= getContainerSize()) {
/*  46 */       return ItemStack.EMPTY;
/*     */     }
/*  48 */     return (ItemStack)this.items.get(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/*  53 */     return ContainerHelper.takeItem((List)this.items, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  58 */     ItemStack itemStack = ContainerHelper.removeItem((List)this.items, paramInt1, paramInt2);
/*  59 */     if (!itemStack.isEmpty()) {
/*  60 */       this.menu.slotsChanged(this);
/*     */     }
/*  62 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/*  67 */     this.items.set(paramInt, paramItemStack);
/*  68 */     this.menu.slotsChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/*  82 */     this.items.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  87 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  92 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ItemStack> getItems() {
/*  97 */     return List.copyOf((Collection<? extends ItemStack>)this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents paramStackedItemContents) {
/* 102 */     for (ItemStack itemStack : this.items)
/* 103 */       paramStackedItemContents.accountSimpleStack(itemStack); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\TransientCraftingContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */