/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ public class MerchantContainer implements Container {
/*     */   private final Merchant merchant;
/*  15 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   private MerchantOffer activeOffer;
/*     */   private int selectionHint;
/*     */   private int futureXp;
/*     */   
/*     */   public MerchantContainer(Merchant paramMerchant) {
/*  21 */     this.merchant = paramMerchant;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  26 */     return this.itemStacks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  31 */     for (ItemStack itemStack : this.itemStacks) {
/*  32 */       if (!itemStack.isEmpty()) {
/*  33 */         return false;
/*     */       }
/*     */     } 
/*  36 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int paramInt) {
/*  41 */     return (ItemStack)this.itemStacks.get(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int paramInt1, int paramInt2) {
/*  46 */     ItemStack itemStack1 = (ItemStack)this.itemStacks.get(paramInt1);
/*  47 */     if (paramInt1 == 2 && !itemStack1.isEmpty()) {
/*  48 */       return ContainerHelper.removeItem((List)this.itemStacks, paramInt1, itemStack1.getCount());
/*     */     }
/*     */     
/*  51 */     ItemStack itemStack2 = ContainerHelper.removeItem((List)this.itemStacks, paramInt1, paramInt2);
/*  52 */     if (!itemStack2.isEmpty() && isPaymentSlot(paramInt1)) {
/*  53 */       updateSellItem();
/*     */     }
/*  55 */     return itemStack2;
/*     */   }
/*     */   
/*     */   private boolean isPaymentSlot(int paramInt) {
/*  59 */     return (paramInt == 0 || paramInt == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int paramInt) {
/*  64 */     return ContainerHelper.takeItem((List)this.itemStacks, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int paramInt, ItemStack paramItemStack) {
/*  69 */     this.itemStacks.set(paramInt, paramItemStack);
/*  70 */     paramItemStack.limitSize(getMaxStackSize(paramItemStack));
/*  71 */     if (isPaymentSlot(paramInt)) {
/*  72 */       updateSellItem();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  78 */     return (this.merchant.getTradingPlayer() == paramPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  83 */     updateSellItem();
/*     */   }
/*     */   public void updateSellItem() {
/*     */     ItemStack itemStack1, itemStack2;
/*  87 */     this.activeOffer = null;
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (((ItemStack)this.itemStacks.get(0)).isEmpty()) {
/*  92 */       itemStack1 = (ItemStack)this.itemStacks.get(1);
/*  93 */       itemStack2 = ItemStack.EMPTY;
/*     */     } else {
/*  95 */       itemStack1 = (ItemStack)this.itemStacks.get(0);
/*  96 */       itemStack2 = (ItemStack)this.itemStacks.get(1);
/*     */     } 
/*     */     
/*  99 */     if (itemStack1.isEmpty()) {
/* 100 */       setItem(2, ItemStack.EMPTY);
/* 101 */       this.futureXp = 0;
/*     */       
/*     */       return;
/*     */     } 
/* 105 */     MerchantOffers merchantOffers = this.merchant.getOffers();
/* 106 */     if (!merchantOffers.isEmpty()) {
/* 107 */       MerchantOffer merchantOffer = merchantOffers.getRecipeFor(itemStack1, itemStack2, this.selectionHint);
/* 108 */       if (merchantOffer == null || merchantOffer.isOutOfStock()) {
/*     */         
/* 110 */         this.activeOffer = merchantOffer;
/* 111 */         merchantOffer = merchantOffers.getRecipeFor(itemStack2, itemStack1, this.selectionHint);
/*     */       } 
/*     */       
/* 114 */       if (merchantOffer != null && !merchantOffer.isOutOfStock()) {
/* 115 */         this.activeOffer = merchantOffer;
/* 116 */         setItem(2, merchantOffer.assemble());
/* 117 */         this.futureXp = merchantOffer.getXp();
/*     */       } else {
/* 119 */         setItem(2, ItemStack.EMPTY);
/* 120 */         this.futureXp = 0;
/*     */       } 
/*     */     } 
/* 123 */     this.merchant.notifyTradeUpdated(getItem(2));
/*     */   }
/*     */   
/*     */   public MerchantOffer getActiveOffer() {
/* 127 */     return this.activeOffer;
/*     */   }
/*     */   
/*     */   public void setSelectionHint(int paramInt) {
/* 131 */     this.selectionHint = paramInt;
/* 132 */     updateSellItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 137 */     this.itemStacks.clear();
/*     */   }
/*     */   
/*     */   public int getFutureXp() {
/* 141 */     return this.futureXp;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\MerchantContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */