/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ 
/*    */ public class MerchantResultSlot extends Slot {
/*    */   private final MerchantContainer slots;
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   private final Merchant merchant;
/*    */   
/*    */   public MerchantResultSlot(Player paramPlayer, Merchant paramMerchant, MerchantContainer paramMerchantContainer, int paramInt1, int paramInt2, int paramInt3) {
/* 16 */     super(paramMerchantContainer, paramInt1, paramInt2, paramInt3);
/* 17 */     this.player = paramPlayer;
/* 18 */     this.merchant = paramMerchant;
/* 19 */     this.slots = paramMerchantContainer;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int paramInt) {
/* 29 */     if (hasItem()) {
/* 30 */       this.removeCount += Math.min(paramInt, getItem().getCount());
/*    */     }
/* 32 */     return super.remove(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack paramItemStack, int paramInt) {
/* 37 */     this.removeCount += paramInt;
/* 38 */     checkTakeAchievements(paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack paramItemStack) {
/* 43 */     paramItemStack.onCraftedBy(this.player, this.removeCount);
/* 44 */     this.removeCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTake(Player paramPlayer, ItemStack paramItemStack) {
/* 49 */     checkTakeAchievements(paramItemStack);
/*    */     
/* 51 */     MerchantOffer merchantOffer = this.slots.getActiveOffer();
/*    */     
/* 53 */     if (merchantOffer != null) {
/* 54 */       ItemStack itemStack1 = this.slots.getItem(0);
/* 55 */       ItemStack itemStack2 = this.slots.getItem(1);
/*    */ 
/*    */       
/* 58 */       if (merchantOffer.take(itemStack1, itemStack2) || merchantOffer.take(itemStack2, itemStack1)) {
/* 59 */         this.merchant.notifyTrade(merchantOffer);
/* 60 */         paramPlayer.awardStat(Stats.TRADED_WITH_VILLAGER);
/*    */         
/* 62 */         this.slots.setItem(0, itemStack1);
/* 63 */         this.slots.setItem(1, itemStack2);
/*    */       } 
/* 65 */       this.merchant.overrideXp(this.merchant.getVillagerXp() + merchantOffer.getXp());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\MerchantResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */