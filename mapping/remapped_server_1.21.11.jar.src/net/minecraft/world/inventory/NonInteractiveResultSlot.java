/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class NonInteractiveResultSlot
/*    */   extends Slot
/*    */ {
/*    */   public NonInteractiveResultSlot(Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/* 12 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onQuickCraft(ItemStack paramItemStack1, ItemStack paramItemStack2) {}
/*    */ 
/*    */   
/*    */   public boolean mayPickup(Player paramPlayer) {
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<ItemStack> tryRemove(int paramInt1, int paramInt2, Player paramPlayer) {
/* 26 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack safeTake(int paramInt1, int paramInt2, Player paramPlayer) {
/* 31 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack safeInsert(ItemStack paramItemStack) {
/* 36 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack safeInsert(ItemStack paramItemStack, int paramInt) {
/* 41 */     return safeInsert(paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean allowModification(Player paramPlayer) {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int paramInt) {
/* 56 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTake(Player paramPlayer, ItemStack paramItemStack) {}
/*    */ 
/*    */   
/*    */   public boolean isHighlightable() {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFake() {
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\NonInteractiveResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */