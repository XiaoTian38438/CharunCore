/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.npc.ClientSideMerchant;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.ItemCost;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MerchantMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   protected static final int PAYMENT1_SLOT = 0;
/*     */   protected static final int PAYMENT2_SLOT = 1;
/*     */   protected static final int RESULT_SLOT = 2;
/*     */   private static final int INV_SLOT_START = 3;
/*     */   private static final int INV_SLOT_END = 30;
/*     */   private static final int USE_ROW_SLOT_START = 30;
/*     */   private static final int USE_ROW_SLOT_END = 39;
/*     */   private static final int SELLSLOT1_X = 136;
/*     */   private static final int SELLSLOT2_X = 162;
/*     */   private static final int BUYSLOT_X = 220;
/*     */   private static final int ROW_Y = 37;
/*     */   private final Merchant trader;
/*     */   private final MerchantContainer tradeContainer;
/*     */   private int merchantLevel;
/*     */   private boolean showProgressBar;
/*     */   private boolean canRestock;
/*     */   
/*     */   public MerchantMenu(int paramInt, Inventory paramInventory) {
/*  39 */     this(paramInt, paramInventory, (Merchant)new ClientSideMerchant(paramInventory.player));
/*     */   }
/*     */   
/*     */   public MerchantMenu(int paramInt, Inventory paramInventory, Merchant paramMerchant) {
/*  43 */     super(MenuType.MERCHANT, paramInt);
/*  44 */     this.trader = paramMerchant;
/*     */     
/*  46 */     this.tradeContainer = new MerchantContainer(paramMerchant);
/*  47 */     addSlot(new Slot(this.tradeContainer, 0, 136, 37));
/*  48 */     addSlot(new Slot(this.tradeContainer, 1, 162, 37));
/*  49 */     addSlot(new MerchantResultSlot(paramInventory.player, paramMerchant, this.tradeContainer, 2, 220, 37));
/*     */     
/*  51 */     addStandardInventorySlots((Container)paramInventory, 108, 84);
/*     */   }
/*     */   
/*     */   public void setShowProgressBar(boolean paramBoolean) {
/*  55 */     this.showProgressBar = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container paramContainer) {
/*  60 */     this.tradeContainer.updateSellItem();
/*  61 */     super.slotsChanged(paramContainer);
/*     */   }
/*     */   
/*     */   public void setSelectionHint(int paramInt) {
/*  65 */     this.tradeContainer.setSelectionHint(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player paramPlayer) {
/*  70 */     return this.trader.stillValid(paramPlayer);
/*     */   }
/*     */   
/*     */   public int getTraderXp() {
/*  74 */     return this.trader.getVillagerXp();
/*     */   }
/*     */   
/*     */   public int getFutureTraderXp() {
/*  78 */     return this.tradeContainer.getFutureXp();
/*     */   }
/*     */   
/*     */   public void setXp(int paramInt) {
/*  82 */     this.trader.overrideXp(paramInt);
/*     */   }
/*     */   
/*     */   public int getTraderLevel() {
/*  86 */     return this.merchantLevel;
/*     */   }
/*     */   
/*     */   public void setMerchantLevel(int paramInt) {
/*  90 */     this.merchantLevel = paramInt;
/*     */   }
/*     */   
/*     */   public void setCanRestock(boolean paramBoolean) {
/*  94 */     this.canRestock = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean canRestock() {
/*  98 */     return this.canRestock;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack paramItemStack, Slot paramSlot) {
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player paramPlayer, int paramInt) {
/* 108 */     ItemStack itemStack = ItemStack.EMPTY;
/* 109 */     Slot slot = (Slot)this.slots.get(paramInt);
/* 110 */     if (slot != null && slot.hasItem()) {
/* 111 */       ItemStack itemStack1 = slot.getItem();
/* 112 */       itemStack = itemStack1.copy();
/*     */       
/* 114 */       if (paramInt == 2) {
/* 115 */         if (!moveItemStackTo(itemStack1, 3, 39, true)) {
/* 116 */           return ItemStack.EMPTY;
/*     */         }
/* 118 */         slot.onQuickCraft(itemStack1, itemStack);
/*     */         
/* 120 */         playTradeSound();
/* 121 */       } else if (paramInt == 0 || paramInt == 1) {
/* 122 */         if (!moveItemStackTo(itemStack1, 3, 39, false)) {
/* 123 */           return ItemStack.EMPTY;
/*     */         }
/* 125 */       } else if (paramInt >= 3 && paramInt < 30) {
/* 126 */         if (!moveItemStackTo(itemStack1, 30, 39, false)) {
/* 127 */           return ItemStack.EMPTY;
/*     */         }
/* 129 */       } else if (paramInt >= 30 && paramInt < 39 && 
/* 130 */         !moveItemStackTo(itemStack1, 3, 30, false)) {
/* 131 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 134 */       if (itemStack1.isEmpty()) {
/* 135 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 137 */         slot.setChanged();
/*     */       } 
/* 139 */       if (itemStack1.getCount() == itemStack.getCount()) {
/* 140 */         return ItemStack.EMPTY;
/*     */       }
/* 142 */       slot.onTake(paramPlayer, itemStack1);
/*     */     } 
/*     */     
/* 145 */     return itemStack;
/*     */   }
/*     */   
/*     */   private void playTradeSound() {
/* 149 */     if (!this.trader.isClientSide()) {
/* 150 */       Entity entity = (Entity)this.trader;
/* 151 */       entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player paramPlayer) {
/* 157 */     super.removed(paramPlayer);
/* 158 */     this.trader.setTradingPlayer(null);
/*     */     
/* 160 */     if (this.trader.isClientSide()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 165 */     if (!paramPlayer.isAlive() || (paramPlayer instanceof ServerPlayer && ((ServerPlayer)paramPlayer).hasDisconnected())) {
/* 166 */       ItemStack itemStack = this.tradeContainer.removeItemNoUpdate(0);
/* 167 */       if (!itemStack.isEmpty()) {
/* 168 */         paramPlayer.drop(itemStack, false);
/*     */       }
/* 170 */       itemStack = this.tradeContainer.removeItemNoUpdate(1);
/* 171 */       if (!itemStack.isEmpty()) {
/* 172 */         paramPlayer.drop(itemStack, false);
/*     */       }
/*     */     }
/* 175 */     else if (paramPlayer instanceof ServerPlayer) {
/* 176 */       paramPlayer.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(0));
/* 177 */       paramPlayer.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tryMoveItems(int paramInt) {
/* 183 */     if (paramInt < 0 || getOffers().size() <= paramInt) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 188 */     ItemStack itemStack1 = this.tradeContainer.getItem(0);
/* 189 */     if (!itemStack1.isEmpty()) {
/* 190 */       if (!moveItemStackTo(itemStack1, 3, 39, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 194 */       this.tradeContainer.setItem(0, itemStack1);
/*     */     } 
/*     */     
/* 197 */     ItemStack itemStack2 = this.tradeContainer.getItem(1);
/* 198 */     if (!itemStack2.isEmpty()) {
/* 199 */       if (!moveItemStackTo(itemStack2, 3, 39, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 203 */       this.tradeContainer.setItem(1, itemStack2);
/*     */     } 
/*     */ 
/*     */     
/* 207 */     if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
/* 208 */       MerchantOffer merchantOffer = (MerchantOffer)getOffers().get(paramInt);
/* 209 */       moveFromInventoryToPaymentSlot(0, merchantOffer.getItemCostA());
/* 210 */       merchantOffer.getItemCostB().ifPresent(paramItemCost -> moveFromInventoryToPaymentSlot(1, paramItemCost));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveFromInventoryToPaymentSlot(int paramInt, ItemCost paramItemCost) {
/* 215 */     for (byte b = 3; b < 39; b++) {
/* 216 */       ItemStack itemStack = ((Slot)this.slots.get(b)).getItem();
/* 217 */       if (!itemStack.isEmpty() && paramItemCost.test(itemStack)) {
/* 218 */         ItemStack itemStack1 = this.tradeContainer.getItem(paramInt);
/* 219 */         if (itemStack1.isEmpty() || ItemStack.isSameItemSameComponents(itemStack, itemStack1)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 224 */           int i = itemStack.getMaxStackSize();
/* 225 */           int j = Math.min(i - itemStack1.getCount(), itemStack.getCount());
/*     */           
/* 227 */           ItemStack itemStack2 = itemStack.copyWithCount(itemStack1.getCount() + j);
/* 228 */           itemStack.shrink(j);
/* 229 */           this.tradeContainer.setItem(paramInt, itemStack2);
/*     */           
/* 231 */           if (itemStack2.getCount() >= i)
/*     */             break; 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOffers(MerchantOffers paramMerchantOffers) {
/* 239 */     this.trader.overrideOffers(paramMerchantOffers);
/*     */   }
/*     */   
/*     */   public MerchantOffers getOffers() {
/* 243 */     return this.trader.getOffers();
/*     */   }
/*     */   
/*     */   public boolean showProgressBar() {
/* 247 */     return this.showProgressBar;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\MerchantMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */