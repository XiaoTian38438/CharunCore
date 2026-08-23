/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemUtils
/*    */ {
/*    */   public static InteractionResult startUsingInstantly(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 17 */     paramPlayer.startUsingItem(paramInteractionHand);
/* 18 */     return (InteractionResult)InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack paramItemStack1, Player paramPlayer, ItemStack paramItemStack2, boolean paramBoolean) {
/* 27 */     boolean bool = paramPlayer.hasInfiniteMaterials();
/* 28 */     if (paramBoolean && bool) {
/* 29 */       if (!paramPlayer.getInventory().contains(paramItemStack2)) {
/* 30 */         paramPlayer.getInventory().add(paramItemStack2);
/*    */       }
/* 32 */       return paramItemStack1;
/*    */     } 
/*    */     
/* 35 */     paramItemStack1.consume(1, (LivingEntity)paramPlayer);
/* 36 */     if (paramItemStack1.isEmpty()) {
/* 37 */       return paramItemStack2;
/*    */     }
/* 39 */     if (!paramPlayer.getInventory().add(paramItemStack2)) {
/* 40 */       paramPlayer.drop(paramItemStack2, false);
/*    */     }
/* 42 */     return paramItemStack1;
/*    */   }
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack paramItemStack1, Player paramPlayer, ItemStack paramItemStack2) {
/* 46 */     return createFilledResult(paramItemStack1, paramPlayer, paramItemStack2, true);
/*    */   }
/*    */   
/*    */   public static void onContainerDestroyed(ItemEntity paramItemEntity, Iterable<ItemStack> paramIterable) {
/* 50 */     Level level = paramItemEntity.level();
/* 51 */     if (level.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     paramIterable.forEach(paramItemStack -> paramLevel.addFreshEntity((Entity)new ItemEntity(paramLevel, paramItemEntity.getX(), paramItemEntity.getY(), paramItemEntity.getZ(), paramItemStack)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ItemUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */