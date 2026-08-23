/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class WritableBookItem extends Item {
/*    */   public WritableBookItem(Item.Properties paramProperties) {
/* 11 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 16 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 17 */     paramPlayer.openItemGui(itemStack, paramInteractionHand);
/* 18 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 19 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\WritableBookItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */