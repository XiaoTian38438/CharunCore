/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MenuConstructor;
/*    */ 
/*    */ public final class SimpleMenuProvider implements MenuProvider {
/*    */   private final Component title;
/*    */   private final MenuConstructor menuConstructor;
/*    */   
/*    */   public SimpleMenuProvider(MenuConstructor paramMenuConstructor, Component paramComponent) {
/* 14 */     this.menuConstructor = paramMenuConstructor;
/* 15 */     this.title = paramComponent;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 20 */     return this.title;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer) {
/* 25 */     return this.menuConstructor.createMenu(paramInt, paramInventory, paramPlayer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\SimpleMenuProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */