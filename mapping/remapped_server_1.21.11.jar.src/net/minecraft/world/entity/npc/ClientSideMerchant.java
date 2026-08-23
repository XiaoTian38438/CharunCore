/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ import net.minecraft.world.item.trading.MerchantOffers;
/*    */ 
/*    */ public class ClientSideMerchant
/*    */   implements Merchant {
/*    */   private final Player source;
/* 14 */   private MerchantOffers offers = new MerchantOffers();
/*    */   private int xp;
/*    */   
/*    */   public ClientSideMerchant(Player paramPlayer) {
/* 18 */     this.source = paramPlayer;
/*    */   }
/*    */ 
/*    */   
/*    */   public Player getTradingPlayer() {
/* 23 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTradingPlayer(Player paramPlayer) {}
/*    */ 
/*    */   
/*    */   public MerchantOffers getOffers() {
/* 32 */     return this.offers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void overrideOffers(MerchantOffers paramMerchantOffers) {
/* 37 */     this.offers = paramMerchantOffers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void notifyTrade(MerchantOffer paramMerchantOffer) {
/* 42 */     paramMerchantOffer.increaseUses();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void notifyTradeUpdated(ItemStack paramItemStack) {}
/*    */ 
/*    */   
/*    */   public boolean isClientSide() {
/* 51 */     return this.source.level().isClientSide();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player paramPlayer) {
/* 56 */     return (this.source == paramPlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVillagerXp() {
/* 61 */     return this.xp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void overrideXp(int paramInt) {
/* 66 */     this.xp = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean showProgressBar() {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getNotifyTradeSound() {
/* 76 */     return SoundEvents.VILLAGER_YES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\ClientSideMerchant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */