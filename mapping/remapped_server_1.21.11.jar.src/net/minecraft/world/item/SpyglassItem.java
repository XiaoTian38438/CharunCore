/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SpyglassItem
/*    */   extends Item {
/*    */   public static final int USE_DURATION = 1200;
/*    */   public static final float ZOOM_FOV_MODIFIER = 0.1F;
/*    */   
/*    */   public SpyglassItem(Item.Properties paramProperties) {
/* 17 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack paramItemStack, LivingEntity paramLivingEntity) {
/* 22 */     return 1200;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemUseAnimation getUseAnimation(ItemStack paramItemStack) {
/* 27 */     return ItemUseAnimation.SPYGLASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 32 */     paramPlayer.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
/* 33 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 34 */     return ItemUtils.startUsingInstantly(paramLevel, paramPlayer, paramInteractionHand);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack paramItemStack, Level paramLevel, LivingEntity paramLivingEntity) {
/* 39 */     stopUsing(paramLivingEntity);
/* 40 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean releaseUsing(ItemStack paramItemStack, Level paramLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 45 */     stopUsing(paramLivingEntity);
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   private void stopUsing(LivingEntity paramLivingEntity) {
/* 50 */     paramLivingEntity.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SpyglassItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */