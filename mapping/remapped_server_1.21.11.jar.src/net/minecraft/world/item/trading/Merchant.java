/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.SimpleMenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MerchantMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Merchant
/*    */ {
/*    */   void setTradingPlayer(Player paramPlayer);
/*    */   
/*    */   Player getTradingPlayer();
/*    */   
/*    */   MerchantOffers getOffers();
/*    */   
/*    */   void overrideOffers(MerchantOffers paramMerchantOffers);
/*    */   
/*    */   void notifyTrade(MerchantOffer paramMerchantOffer);
/*    */   
/*    */   default boolean canRestock() {
/* 35 */     return false;
/*    */   } void notifyTradeUpdated(ItemStack paramItemStack); int getVillagerXp(); void overrideXp(int paramInt); boolean showProgressBar();
/*    */   SoundEvent getNotifyTradeSound();
/*    */   default void openTradingScreen(Player paramPlayer, Component paramComponent, int paramInt) {
/* 39 */     OptionalInt optionalInt = paramPlayer.openMenu((MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new MerchantMenu(paramInt, paramInventory, this), paramComponent));
/*    */     
/* 41 */     if (optionalInt.isPresent()) {
/* 42 */       MerchantOffers merchantOffers = getOffers();
/* 43 */       if (!merchantOffers.isEmpty())
/* 44 */         paramPlayer.sendMerchantOffers(optionalInt.getAsInt(), merchantOffers, paramInt, getVillagerXp(), showProgressBar(), canRestock()); 
/*    */     } 
/*    */   }
/*    */   
/*    */   boolean isClientSide();
/*    */   
/*    */   boolean stillValid(Player paramPlayer);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\trading\Merchant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */