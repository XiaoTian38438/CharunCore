/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class FishingRodItem extends Item {
/*    */   public FishingRodItem(Item.Properties paramProperties) {
/* 19 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 24 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 25 */     if (paramPlayer.fishing != null) {
/* 26 */       if (!paramLevel.isClientSide()) {
/* 27 */         int i = paramPlayer.fishing.retrieve(itemStack);
/* 28 */         itemStack.hurtAndBreak(i, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/*    */       } 
/* 30 */       paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */       
/* 32 */       itemStack.causeUseVibration((Entity)paramPlayer, GameEvent.ITEM_INTERACT_FINISH);
/*    */     } else {
/* 34 */       paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (paramLevel.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */       
/* 36 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 37 */         int i = (int)(EnchantmentHelper.getFishingTimeReduction(serverLevel, itemStack, (Entity)paramPlayer) * 20.0F);
/* 38 */         int j = EnchantmentHelper.getFishingLuckBonus(serverLevel, itemStack, (Entity)paramPlayer);
/* 39 */         Projectile.spawnProjectile((Projectile)new FishingHook(paramPlayer, paramLevel, j, i), serverLevel, itemStack); }
/*    */       
/* 41 */       paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*    */       
/* 43 */       itemStack.causeUseVibration((Entity)paramPlayer, GameEvent.ITEM_INTERACT_START);
/*    */     } 
/* 45 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\FishingRodItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */