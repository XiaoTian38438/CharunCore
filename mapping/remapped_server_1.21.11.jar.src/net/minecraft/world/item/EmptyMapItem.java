/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EmptyMapItem extends Item {
/*    */   public EmptyMapItem(Item.Properties paramProperties) {
/* 13 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/*    */     ServerLevel serverLevel;
/* 18 */     ItemStack itemStack1 = paramPlayer.getItemInHand(paramInteractionHand);
/*    */     
/* 20 */     if (paramLevel instanceof ServerLevel) { serverLevel = (ServerLevel)paramLevel; }
/* 21 */     else { return (InteractionResult)InteractionResult.SUCCESS; }
/*    */ 
/*    */     
/* 24 */     itemStack1.consume(1, (LivingEntity)paramPlayer);
/*    */     
/* 26 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 27 */     serverLevel.playSound(null, (Entity)paramPlayer, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, paramPlayer.getSoundSource(), 1.0F, 1.0F);
/*    */     
/* 29 */     ItemStack itemStack2 = MapItem.create(serverLevel, paramPlayer.getBlockX(), paramPlayer.getBlockZ(), (byte)0, true, false);
/* 30 */     if (itemStack1.isEmpty()) {
/* 31 */       return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(itemStack2);
/*    */     }
/* 33 */     if (!paramPlayer.getInventory().add(itemStack2.copy())) {
/* 34 */       paramPlayer.drop(itemStack2, false);
/*    */     }
/*    */     
/* 37 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\EmptyMapItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */