/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ 
/*    */ public class FurnaceResultSlot extends Slot {
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   
/*    */   public FurnaceResultSlot(Player paramPlayer, Container paramContainer, int paramInt1, int paramInt2, int paramInt3) {
/* 14 */     super(paramContainer, paramInt1, paramInt2, paramInt3);
/* 15 */     this.player = paramPlayer;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack paramItemStack) {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int paramInt) {
/* 25 */     if (hasItem()) {
/* 26 */       this.removeCount += Math.min(paramInt, getItem().getCount());
/*    */     }
/* 28 */     return super.remove(paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTake(Player paramPlayer, ItemStack paramItemStack) {
/* 33 */     checkTakeAchievements(paramItemStack);
/* 34 */     super.onTake(paramPlayer, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack paramItemStack, int paramInt) {
/* 39 */     this.removeCount += paramInt;
/* 40 */     checkTakeAchievements(paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack paramItemStack) {
/* 45 */     paramItemStack.onCraftedBy(this.player, this.removeCount);
/* 46 */     Player player = this.player; if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 47 */       Container container = this.container; if (container instanceof AbstractFurnaceBlockEntity) { AbstractFurnaceBlockEntity abstractFurnaceBlockEntity = (AbstractFurnaceBlockEntity)container;
/* 48 */         abstractFurnaceBlockEntity.awardUsedRecipesAndPopExperience(serverPlayer); }
/*    */        }
/*    */     
/* 51 */     this.removeCount = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\FurnaceResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */